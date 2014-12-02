package com.doteyplay.game.service.bo.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.doteyplay.core.bhns.AbstractSimpleService;
import com.doteyplay.core.bhns.BOServiceManager;
import com.doteyplay.core.dbcs.DBCS;
import com.doteyplay.game.CommonConstants;
import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.config.template.SpriteDataObject;
import com.doteyplay.game.constants.common.CommonPropUpdateType;
import com.doteyplay.game.constants.common.RewardType;
import com.doteyplay.game.constants.item.ItemConstants;
import com.doteyplay.game.constants.item.OutfitUpgradeResult;
import com.doteyplay.game.constants.item.PetGoldUpgradeResultType;
import com.doteyplay.game.constants.pet.PetErrorMsg;
import com.doteyplay.game.constants.pet.PetStarUpgradeResult;
import com.doteyplay.game.constants.sprite.SpriteQualityType;
import com.doteyplay.game.constants.sprite.SpriteStarType;
import com.doteyplay.game.domain.item.RoleItem;
import com.doteyplay.game.domain.pet.Pet;
import com.doteyplay.game.domain.pet.PetSkillManager;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.domain.skill.SpriteSkill;
import com.doteyplay.game.domain.sprite.AbstractSprite;
import com.doteyplay.game.gamedata.data.sprite.SpriteStarData;
import com.doteyplay.game.message.common.CommonPropUpdateMessage;
import com.doteyplay.game.message.pet.PetAddRemoveMessage;
import com.doteyplay.game.message.pet.PetGoldQualityUpgradeMessage;
import com.doteyplay.game.message.utils.ResponseMessageUtils;
import com.doteyplay.game.persistence.serverdata.pet.IPetBeanDao;
import com.doteyplay.game.service.bo.item.IItemService;
import com.doteyplay.game.service.runtime.GlobalRoleCache;
import com.doteyplay.game.util.excel.TemplateService;

public class RoleService extends AbstractSimpleService<IRoleService> implements
		IRoleService
{
	private static final Logger logger = Logger.getLogger(RoleService.class);

	private Role role;
	private static Random random = new Random();

	@Override
	public int getPortalId()
	{
		return IRoleService.PORTAL_ID;
	}

	@Override
	public void initialize()
	{
		this.role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.doteyplay.game.service.bo.role.IRoleService#summonPet(int)
	 */
	@Override
	public void summonPet(int petSpriteId)
	{
		if (role.getPetManager().getPetBySriteId(petSpriteId) != null)
		{
			// 已经存在该宠物
			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.PET_CALL_MESSAGE.ordinal(),
					PetErrorMsg.ERROR_HAS_EXIST, getServiceId());
			return;
		}
		SpriteDataObject petData = TemplateService.getInstance().get(
				petSpriteId, SpriteDataObject.class);
		if (petData == null || petData.getCanSummon() == CommonConstants.FALSE)
		{
			// 无法召唤
			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.PET_CALL_MESSAGE.ordinal(),
					PetErrorMsg.ERROR_NO_EXIST, getServiceId());
			return;
		}
		IItemService itemService = BOServiceManager.findService(
				IItemService.PORTAL_ID, role.getRoleId());
		if (itemService.lock())
		{
			try
			{
				RoleItem item = itemService.addOrRemoveItem(
						petData.getStoneId(), -petData.getCostStoneNum(), true);
				if (item == null)
				{
					// 物品不足
					ResponseMessageUtils.sendResponseMessage(
							MessageCommands.PET_CALL_MESSAGE.ordinal(),
							PetErrorMsg.ERROR_NO_STONE,
							getServiceId());
					return;
				}
			} finally
			{
				itemService.unlock();
			}
		} else
		{
			// 系统忙碌中.
			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.PET_CALL_MESSAGE.ordinal(),
					PetErrorMsg.ERROR_SYSTEM_0, getServiceId());
			return;
		}

		Pet pet = role.getPetManager().addPet(petSpriteId);
		if (pet == null)
		{
			// 召唤宠物系统失败
			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.PET_CALL_MESSAGE.ordinal(),
					PetErrorMsg.ERROR_SYSTEM_1, getServiceId());
			return;
		}

		PetAddRemoveMessage message = new PetAddRemoveMessage();
		message.setAddOrRemove(true);

		List<Pet> petList = new ArrayList<Pet>();
		petList.add(pet);
		message.setPetList(petList);
		this.sendMessage(message);
	}

	public OutfitUpgradeResult qualityUpgrade(long petId)
	{
		Pet pet = role.findPet(petId);
		if (pet == null)
			return OutfitUpgradeResult.NOT_FOUND_SPRITE;

		int oldQuality = pet.getSpriteBean().getQuality();
		if (oldQuality >= SpriteQualityType.QUALITY_8.ordinal())
			return OutfitUpgradeResult.MAX_QUALITY_ALREADY;

		OutfitUpgradeResult result = null;
		IItemService itemService = BOServiceManager.findService(
				IItemService.PORTAL_ID, role.getRoleId());
		if (itemService.lock())
		{
			try
			{
				result = itemService.upgradeQualityRemoveOutfit(petId);
			} finally
			{
				itemService.unlock();
			}
		}

		if (result == OutfitUpgradeResult.SUCCESS)
		{
			pet.getSpriteBean().setQuality(oldQuality + 1);
			IPetBeanDao dao = DBCS.getExector(IPetBeanDao.class);
			dao.updatePetBean(pet.getBean());

			if (pet.getSpriteBean().getQuality() == SpriteQualityType.QUALITY_1
					.ordinal() && pet.getSpriteDataObject().getSkill2() > 0)
			{
				pet.getSkillManager().addSKill(
						pet.getSpriteDataObject().getSkill2());
			} else if (pet.getSpriteBean().getQuality() == SpriteQualityType.QUALITY_3
					.ordinal() && pet.getSpriteDataObject().getSkill3() > 0)
			{
				pet.getSkillManager().addSKill(
						pet.getSpriteDataObject().getSkill3());
			} else if (pet.getSpriteBean().getQuality() == SpriteQualityType.QUALITY_6
					.ordinal() && pet.getSpriteDataObject().getSkill4() > 0)
			{
				pet.getSkillManager().addSKill(
						pet.getSpriteDataObject().getSkill4());
			}

			CommonPropUpdateMessage message = new CommonPropUpdateMessage(
					petId, CommonPropUpdateType.QUALITY, 1, pet.getSpriteBean()
							.getQuality(), RewardType.DEFAULT);
			sendMessage(message);
		}
		return result;
	}

	public PetStarUpgradeResult starUpgrade(long petId)
	{
		Pet pet = role.findPet(petId);
		if (pet == null)
			return PetStarUpgradeResult.PET_NOT_FOUND;
		int oldStar = pet.getSpriteBean().getStar();
		if (oldStar >= SpriteStarType.values().length)
			return PetStarUpgradeResult.STAR_AT_MAX_LEVEL;

		SpriteStarData starData = pet.getSpriteDataObject().getStarDataList()
				.get(oldStar - 1);
		if (starData == null)
			return PetStarUpgradeResult.STAR_DATA_NULL;

		if (role.getRoleBean().getMoney() < starData.upgradeMoney)
			return PetStarUpgradeResult.MONEY_NOT_ENOUGH;

		IItemService itemService = BOServiceManager.findService(
				IItemService.PORTAL_ID, role.getRoleId());
		if (itemService.lock())
		{
			try
			{
				RoleItem item = itemService.addOrRemoveItem(pet
						.getSpriteDataObject().getStoneId(),
						-starData.stoneNum, true);
				if (item == null)
					return PetStarUpgradeResult.ITEM_NOT_ENOUGH;
			} finally
			{
				itemService.unlock();
			}
		}

		role.addMoney(-starData.upgradeMoney, RewardType.UP_STAR, true);
		pet.getBean().setStar(oldStar + 1);
		IPetBeanDao dao = DBCS.getExector(IPetBeanDao.class);
		dao.updatePetBean(pet.getBean());

		CommonPropUpdateMessage message = new CommonPropUpdateMessage(petId,
				CommonPropUpdateType.STAR, 1, pet.getSpriteBean().getStar(),
				RewardType.DEFAULT);
		sendMessage(message);

		return PetStarUpgradeResult.SUCCESS;
	}

	public void highQualityUpgrade(long petId)
	{
		OutfitUpgradeResult result = OutfitUpgradeResult.SUCCESS;
		Pet pet = role.findPet(petId);
		if (pet == null)
			result = OutfitUpgradeResult.NOT_FOUND_SPRITE;
		else
		{
			int oldQuality = pet.getSpriteBean().getQuality();
			if (oldQuality < SpriteQualityType.QUALITY_8.ordinal()
					|| pet.getBean().getStar() != SpriteStarType.STAR_6
							.ordinal() + 1)
				result = OutfitUpgradeResult.NOT_FIT_UP_QUALITY_LEVEL;
			else
			{
				if (pet.getSkillManager().isAllPassiveSkillLevelMAX())
					result = OutfitUpgradeResult.SKILL_LEVEL_FULL;
				else
				{
					if (role.getRoleBean().getMoney() < pet
							.getSpriteDataObject().getHighQualityCostMoney())
						result = OutfitUpgradeResult.MONEY_NOT_ENOUGH;
					else
					{
						IItemService itemService = BOServiceManager
								.findService(IItemService.PORTAL_ID,
										role.getRoleId());
						if (itemService.lock())
						{
							try
							{
								RoleItem item = itemService.addOrRemoveItem(pet
										.getSpriteDataObject().getStoneId(),
										-pet.getSpriteDataObject()
												.getHighQualityStoneNum(),
										true);
								if (item == null)
									result = OutfitUpgradeResult.ITEM_NOT_ENOUGH;
							} finally
							{
								itemService.unlock();
							}
						}
					}
				}
			}
		}

		PetGoldQualityUpgradeMessage message = new PetGoldQualityUpgradeMessage();
		if (result == OutfitUpgradeResult.SUCCESS)
		{
			// 没有达到金色品质
			if (pet.getSpriteBean().getQuality() < SpriteQualityType.QUALITY_9
					.ordinal())
			{
				if (random.nextInt(10000) <= pet.getSpriteDataObject()
						.getGoldQualityPercent())
				{
					message.setType(PetGoldUpgradeResultType.UP_2_GOLD);
					qualityUp2Gold(pet);
				} else
				{
					SpriteSkill newSkill = ((PetSkillManager) pet
							.getSkillManager()).randomSkillLevelUp();
					if (newSkill == null)
					{
						qualityUp2Gold(pet);
						message.setType(PetGoldUpgradeResultType.UP_2_GOLD);
					} else
					{
						message.setType(PetGoldUpgradeResultType.SKILL_LEVELUP);
						message.setSkillId(newSkill.getSkillData().getId());
					}
				}
			} else
			{
				// 已经是金色品质
				int totalPercent = pet.getSpriteDataObject().getSkill6Percent()
						+ pet.getSpriteDataObject().getSkill7Percent();

				// 随机获得技能
				if (random.nextInt(10000) < totalPercent)
				{
					boolean hasAddSkill = false;
					if (!pet.getSkillManager().hasSkill(
							pet.getSpriteDataObject().getSkill6())
							|| !pet.getSkillManager().hasSkill(
									pet.getSpriteDataObject().getSkill7()))
					{

						if (!pet.getSkillManager().hasSkill(
								pet.getSpriteDataObject().getSkill6()) && pet.getSpriteDataObject().getSkill6() > 0 )
						{
							pet.getSkillManager().addSKill(
									pet.getSpriteDataObject().getSkill6());
							hasAddSkill = true;

							message.setType(PetGoldUpgradeResultType.ADD_SKILL);
							message.setSkillId(pet.getSpriteDataObject()
									.getSkill6());

						} else if (!pet.getSkillManager().hasSkill(
								pet.getSpriteDataObject().getSkill7()) && pet.getSpriteDataObject().getSkill7() > 0)
						{
							pet.getSkillManager().addSKill(
									pet.getSpriteDataObject().getSkill7());
							hasAddSkill = true;

							message.setType(PetGoldUpgradeResultType.ADD_SKILL);
							message.setSkillId(pet.getSpriteDataObject()
									.getSkill7());
						}

					}
					// 获得技能失败则随机提升技能等级
					if (!hasAddSkill)
					{
						SpriteSkill newSkill = ((PetSkillManager) pet
								.getSkillManager()).randomSkillLevelUp();
						message.setType(PetGoldUpgradeResultType.SKILL_LEVELUP);
						message.setSkillId(newSkill.getSkillData().getId());
					}
				} else
				{
					// 尝试提升技能
					SpriteSkill newSkill = ((PetSkillManager) pet
							.getSkillManager()).randomSkillLevelUp();
					if (newSkill == null)
					{
						// 技能提升失败则获得技能
						if (!pet.getSkillManager().hasSkill(
								pet.getSpriteDataObject().getSkill6()) && pet.getSpriteDataObject().getSkill6() > 0)
						{
							pet.getSkillManager().addSKill(
									pet.getSpriteDataObject().getSkill6());

							message.setType(PetGoldUpgradeResultType.ADD_SKILL);
							message.setSkillId(pet.getSpriteDataObject()
									.getSkill6());

						} else if (!pet.getSkillManager().hasSkill(
								pet.getSpriteDataObject().getSkill7()) && pet.getSpriteDataObject().getSkill7() > 0)
						{
							pet.getSkillManager().addSKill(
									pet.getSpriteDataObject().getSkill7());

							message.setType(PetGoldUpgradeResultType.ADD_SKILL);
							message.setSkillId(pet.getSpriteDataObject()
									.getSkill7());
						}
					} else
					{
						message.setType(PetGoldUpgradeResultType.SKILL_LEVELUP);
						message.setSkillId(newSkill.getSkillData().getId());
					}
				}
			}
		}
		message.setResult(result);
		this.sendMessage(message);
	}

	private void qualityUp2Gold(Pet pet)
	{
		pet.getSpriteBean().setQuality(SpriteQualityType.QUALITY_9.ordinal());
		IPetBeanDao dao = DBCS.getExector(IPetBeanDao.class);
		dao.updatePetBean(pet.getBean());

		if(pet.getSpriteDataObject().getSkill5() > 0)
			pet.getSkillManager().addSKill(pet.getSpriteDataObject().getSkill5());

		CommonPropUpdateMessage message = new CommonPropUpdateMessage(
				pet.getId(), CommonPropUpdateType.QUALITY, 1, pet
						.getSpriteBean().getQuality(), RewardType.DEFAULT);
		sendMessage(message);
	}
}
