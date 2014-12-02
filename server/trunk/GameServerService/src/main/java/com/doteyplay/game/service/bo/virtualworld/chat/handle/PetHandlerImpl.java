package com.doteyplay.game.service.bo.virtualworld.chat.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.doteyplay.core.bhns.BOServiceManager;
import com.doteyplay.core.dbcs.DBCS;
import com.doteyplay.core.messageagent.TransferAgent;
import com.doteyplay.game.CommonConstants;
import com.doteyplay.game.config.template.SpriteDataObject;
import com.doteyplay.game.constants.IdType;
import com.doteyplay.game.domain.common.IdGenerator;
import com.doteyplay.game.domain.gamebean.PetBean;
import com.doteyplay.game.domain.gamebean.RoleBean;
import com.doteyplay.game.domain.pet.Pet;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.message.pet.PetAddRemoveMessage;
import com.doteyplay.game.persistence.serverdata.pet.IPetBeanDao;
import com.doteyplay.game.persistence.serverdata.role.IRoleBeanDao;
import com.doteyplay.game.service.bo.role.IRoleService;
import com.doteyplay.game.service.bo.virtualworld.chat.patameter.PetPatameterObject;
import com.doteyplay.game.service.runtime.GlobalRoleCache;
import com.doteyplay.game.util.excel.TemplateService;
/**
 * 宠物处理类
* @className:PetHandlerImpl.java
* @classDescription:
* @author:Tom.Zheng
* @createTime:2014年8月19日 下午5:46:33
 */
public class PetHandlerImpl extends AbstractHandlerImpl<PetPatameterObject> {

	private static  Logger logger  = Logger.getLogger(PetHandlerImpl.class);
	@Override
	public void onlineHandle(Role role, PetPatameterObject t) {
		// TODO Auto-generated method stub
		for (PetPatameterObject.ItemObject item : t.getItemObjects()) {
			Pet pet = role.getPetManager().addPet(item.itemId);
			PetAddRemoveMessage message = new PetAddRemoveMessage();
			message.setAddOrRemove(true);
			
			List<Pet> petList= new ArrayList<Pet>();
			petList.add(pet);
			message.setPetList(petList);
			role.sendMessage(message);
		}
	}

	@Override
	public void offlineHandle(long roleId, PetPatameterObject t) {
		// TODO Auto-generated method stub
		for (PetPatameterObject.ItemObject item : t.getItemObjects()) {
			IRoleBeanDao roleDao = DBCS.getExector(IRoleBeanDao.class);
			RoleBean selectRoleBean = roleDao.selectRoleBean(roleId);
			if(selectRoleBean==null){
				logger.error("角色Id不存在");
				return;
			}
			IPetBeanDao petBeanDao = DBCS.getExector(IPetBeanDao.class);
			List<PetBean> selectedPets = petBeanDao.selectPetBeanListByRoleId(roleId);
			Map<Integer,PetBean> petMap = new HashMap<Integer,PetBean>();
			for (PetBean pet : selectedPets) {
				petMap.put(pet.getSpriteId(), pet);
			}
			if(petMap.containsKey(item.itemId)){
				logger.error("该宠物Id每个用户只能有一个,目前已经存在");
				return;
			}
			
			SpriteDataObject petSpriteData = TemplateService.getInstance().get(item.itemId, SpriteDataObject.class);
			if (petSpriteData == null){
				logger.error("该宠物Id 输入是错误的");
				return;
			}
			PetBean petBean = new PetBean();
			petBean.setAreaId(selectRoleBean.getAreaId());
			petBean.setId(IdGenerator.getInstance().getId(IdType.PET_ID,
					selectRoleBean.getAreaId()));
			petBean.setLevel(1);
			petBean.setStar(petSpriteData.getInitStar());
			petBean.setRoleId(roleId);
			petBean.setSpriteId(item.itemId);
			
			if(petSpriteData.getIsHero() == CommonConstants.TRUE){
				petBean.setGroupId(1);
			}else{
				petBean.setGroupId(0);
			}
			
			petBeanDao.insertPetBean(petBean);
			
			logger.error("GM 向 roleId="+roleId+"添加宠物Id="+item.itemId+"成功");
		}
	}
}
