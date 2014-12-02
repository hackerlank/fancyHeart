package com.doteyplay.game.service.bo.tollgate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.doteyplay.core.bhns.AbstractSimpleService;
import com.doteyplay.game.CommonConstants;
import com.doteyplay.game.MessageCommands;
import com.doteyplay.game.config.template.BattleDataTemplate;
import com.doteyplay.game.config.template.DropDataTemplate;
import com.doteyplay.game.config.template.TollgateDataManager;
import com.doteyplay.game.config.template.TollgateNodeDataTemplate;
import com.doteyplay.game.constants.common.RewardType;
import com.doteyplay.game.constants.tollgate.BattleResultType;
import com.doteyplay.game.constants.tollgate.EnterBattleResultType;
import com.doteyplay.game.constants.tollgate.GroupUpdateResultType;
import com.doteyplay.game.constants.tollgate.NodeUpdateState;
import com.doteyplay.game.constants.tollgate.TollgateRewardExp;
import com.doteyplay.game.domain.drop.DropDataManager;
import com.doteyplay.game.domain.pet.Pet;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.domain.tollgate.BattleResult;
import com.doteyplay.game.domain.tollgate.NodeInfo;
import com.doteyplay.game.domain.tollgate.RoleTollgate;
import com.doteyplay.game.message.tollgate.BattleResultMessage;
import com.doteyplay.game.message.tollgate.EnterBattleMessage;
import com.doteyplay.game.message.tollgate.NodeChangeMessage;
import com.doteyplay.game.message.tollgate.ShowTollgateDetailMessage;
import com.doteyplay.game.message.tollgate.TollgateChangeMessage;
import com.doteyplay.game.message.utils.ResponseMessageUtils;
import com.doteyplay.game.service.runtime.GlobalRoleCache;
import com.doteyplay.game.util.GameUtil;
import com.doteyplay.game.util.excel.TemplateService;


/**
 * 
 * @className:TollgateInfoService.java
 * @classDescription: 关卡服务类.
 * 
 * @author:Tom.Zheng
 * @createTime:2014年7月16日 下午3:29:31
 */
public class TollgateInfoService extends
		AbstractSimpleService<ITollgateInfoService> implements
		ITollgateInfoService {
	private static Logger logger = Logger.getLogger(TollgateInfoService.class);

	private RoleTollgate roleTollgate = null;

	@Override
	public int getPortalId() {
		// TODO Auto-generated method stub
		return PORTAL_ID;
	}

	@Override
	public void initlize() {
		roleTollgate = new RoleTollgate(this.getServiceId());

		roleTollgate.initlize();

		logger.error("角色的副本数据加载成功！roleId ="+this.getServiceId());

	}

	/**
	 * 向用户发送所有的关卡信息.
	 */
	@Override
	public ShowTollgateDetailMessage showTollgateDetailInfo() {
		// TODO Auto-generated method stub
		ShowTollgateDetailMessage message = new ShowTollgateDetailMessage();

		roleTollgate.showTollgateDetailInfo(message);

		return message;
	}

	/**
	 * 发送开启或关闭某个节点的信息. 附带着,另一节点的星级改变信息.
	 */
	@Override
	public void sendNodeChangeInfo(int tollgateId, int nodeId) {
		NodeChangeMessage message = new NodeChangeMessage();
		message.setTollgateId(tollgateId);
		message.addUpdateItem(nodeId, 1, 3, 0);

		this.sendMessage(message);

	}

	private void sendTollgateChangeMessage(int tollgateId,NodeInfo... nodeInfo) {
		TollgateChangeMessage message = new TollgateChangeMessage();

		message.addOperateTollgate(tollgateId,true,nodeInfo);

		this.sendMessage(message);
	}
	/**
	 * 进入战斗. 
	   1.检查节点激活情况.
	   2.成功后,进入节点.
	 */
	@Override
	public void enterBattle(int tollgateId, int nodeId, int groupId) {
		// TODO Auto-generated method stub
		EnterBattleMessage  message = new EnterBattleMessage();
		// 进入关卡或者战斗事件.
		boolean isOpenTollgateAndNode = checkNodeIsAcitived(tollgateId, nodeId);
		if(!isOpenTollgateAndNode){
			return;
		}
	    Role role = getRole();
		BattleDataTemplate battleDataTemplate = getBattleDataTemplate(tollgateId, nodeId);
		if(battleDataTemplate==null){
			logger.error("战场数据不存在tollgateId="+tollgateId+"nodeId="+nodeId);
			ResponseMessageUtils.sendResponseMessage(message.getCommandId(),
					EnterBattleResultType.ENTER_NO_DATA,
					this.getServiceId());
			return;
		}
		boolean checkEnergyPoint = checkEnergyPoint(tollgateId, role);
		if(!checkEnergyPoint){
			ResponseMessageUtils.sendResponseMessage(message.getCommandId(),
					EnterBattleResultType.ENTER_NO_Engergy,
					this.getServiceId());
			return;
		}
			
		String dropNumStr = battleDataTemplate.getDropNumStr();
		int random = getRandomDropNum(dropNumStr);
		//生成战场结果的奖励物品数据缓存.
		List<DropDataTemplate> list = DropDataManager.getInstance().getTollgateDropGroup(getServiceId(),battleDataTemplate.getDropGroupId(), random,true);
//		//将物品向客户端发送信息.
		if(list==null){
			logger.error("生成的物品信息为null");	
		}
		
		message.setState(CommonConstants.TRUE);
		message.setItemNum(list==null?0:list.size());
		this.sendMessage(message);
		// 发送成功的消息
		
	}

	private boolean checkEnergyPoint(int tollgateId, Role role) {
		int tollgateShowType = TollgateDataManager.getInstance()
				.getTollgateShowType(tollgateId);

		int costEnergyPoint = TollgateRewardExp
				.getCostEnergyPoint(tollgateShowType);
		//检查是否有足够的体力.
		if(role.getRoleBean().getEnergy()<costEnergyPoint){
			logger.error("体力不足");
			return false;
		}
		return true;
	}

	private static int getRandomDropNum(String dropNumStr) {
		String[] split = dropNumStr.split(",");
		int random = 0;
		if(split==null||split.length!=2){
			random= 1;
			logger.error("战场数据有关掉数量的编辑错误dropNumStr="+dropNumStr);
		}else{
			random = GameUtil.random(Integer.parseInt(split[0]), Integer.parseInt(split[1])+1);
		}
		return random;
	}
	/**
	 * 接收战斗结果
	 * 1.检查该节点,激活情况.
	 * @param star
	 */
	@Override
	public void acceptBattleResult(int tollgateId, int nodeId, int star) {
		BattleResultMessage message = new BattleResultMessage();
		if(star==0){
			this.sendMessage(message);
			return;
		}
		if (star < 0 || star > 3) {
			logger.error("客户端传送星极的参数不对!star="+star);
			
			return;
			
		}
		// 检查节点情况.
		boolean isOpenTollgateAndNode = checkNodeIsAcitived(tollgateId, nodeId);
		if (!isOpenTollgateAndNode) {
			ResponseMessageUtils.sendResponseMessage(message.getCommandId(),
					BattleResultType.NODE_NO_ACTIVED,
					this.getServiceId());
			return;
		}
		// 根据战斗结果,给玩家添加新的经验.
		int isUpdate = roleTollgate.acceptBattleResult(tollgateId, nodeId, star);
		if(star>0){
			//如果战斗胜利,才可以发东西.
			addBattleResultReward(tollgateId, nodeId, star);
		}
		//触发新的关卡的开启.
		openTriggerTollgate(tollgateId, nodeId, star, isUpdate);

	}
	
	/**
	 * 检查节点是否已经激活.
	 * 1.如果结点已经激活,进入战场,回复成功.
	 * 2.如果结点未激活,提示错误.
	 * @param tollgateId
	 * @param nodeId
	 * @return
	 */
	private boolean checkNodeIsAcitived(int tollgateId, int nodeId){
		boolean isOpenTollgateAndNode = roleTollgate.isOpenTollgateAndNode(
				tollgateId, nodeId);

		if (!isOpenTollgateAndNode) {
			// 发送错误信息.未激活该节点。
//			ResponseMessageUtils.sendResponseMessage(
//					MessageCommands.ENTER_BATTLE_MESSAGE.ordinal(),
//					TollgateErrorType.NoNode.ordinal(), this.getServiceId());
			EnterBattleMessage  message = new EnterBattleMessage();
			
			message.setState(CommonConstants.FALSE);
			message.setItemNum(0);
			this.sendMessage(message);
			return false;
		} 
		
		return true;
	}

	
	
	/**
	 * 解发开启关卡的操作.
	 * @param tollgateId
	 * @param nodeId
	 * @param star
	 */
	private void openTriggerTollgate(int tollgateId, int nodeId, int star,int isUpdate) {
		//原有的结点的更新操作.
		NodeChangeMessage message = new NodeChangeMessage();
		message.setTollgateId(tollgateId);
		if(isUpdate==NodeUpdateState.UPDATE.ordinal()){
			message.addUpdateItem(nodeId, 3, star, 2);//历史节点是更新.
		}
		//是否要开启下一个节点.
		boolean isOpenNextNode = false;
		//星级为0时,不需要开启.
		if(star>0){
			isOpenNextNode =true;
		}
		//从当前结点,找到可能触发的新结点.
		BattleDataTemplate temp = getBattleDataTemplate(tollgateId, nodeId);
		
		String openTollgateNodeIdStr = temp.getOpenTollgateNodeIdStr();
		
		int triggerNodeId[]=getNeedTollgateNodeId(openTollgateNodeIdStr);
		if(triggerNodeId!=null&&isOpenNextNode){
			//当前关卡是最后一个,不再产生下一个.
			for (int triggerId : triggerNodeId) {
				TollgateNodeDataTemplate nodeDataTemplate=TollgateDataManager.getInstance().getTollgateNodeDataTemplate(triggerId);
				if(nodeDataTemplate!=null){
					//是否已经开启.
					boolean hasOpen = roleTollgate.isOpenTollgateAndNode(
							nodeDataTemplate.getTollgateGateId(), nodeDataTemplate.getId());
					if (!hasOpen) {
						// 开启下一关.
						roleTollgate.openTollgateOrNodeAndUpdateDB(
								nodeDataTemplate.getTollgateGateId(),
								nodeDataTemplate.getId());
						message.addUpdateItem(nodeDataTemplate.getId(), 3, 0, 1);//当前节点.是添加.
					} 
				}
			}
		}
		//如果更新消息不为null,即可以发送消息.
		if(!message.isItemsEmpty()){
			this.sendMessage(message);
		}
		
		
	}

	private int[] getNeedTollgateNodeId(String openTollgateNodeIdStr) {
		// TODO Auto-generated method stub
		if(openTollgateNodeIdStr==null||"".equals(openTollgateNodeIdStr)){
			
			return null;
		}
		
		String[] split = openTollgateNodeIdStr.split(",");
		if(split==null||split.length==0){
			return null;
		}
		int nodeId[] = new int[split.length];
		int index = 0;
		for (String i : split) {
			nodeId[index++]=Integer.parseInt(i);
		}
		return nodeId;
	}

	/**
	 * 由指定的关卡信息,找到指定的奖励信息,进行添加战队奖励.添加每一个宠物的奖励.
	 * 
	 * @param tollgateId
	 * @param nodeId
	 * @param star
	 * @param petIds
	 */
	private void addBattleResultReward(int tollgateId, int nodeId, int star) {

		BattleDataTemplate temp = getBattleDataTemplate(tollgateId, nodeId);

		int petExp = temp.getPetExp();// 战队经验

		int gameCoin = temp.getGameCoin();// 角色金币

		int dropGroupId = temp.getDropGroupId();
		BattleResult result = new BattleResult();
		result.gameCoin = gameCoin;
		result.star = star;
		recordRoleHistory(result, false);
		recordPetCurrent(result, false);
		addRoleExp(tollgateId);
		addPetExp(petExp,getRole());//添加经验值.
		addGameCoin(gameCoin,getRole());
		addDropGroupId(result,dropGroupId);
		recordRoleHistory(result, true);
		recordPetCurrent(result, true);
		showBattleResultMsg(result);

	}
	/**
	 * 查寻战斗原型信息.
	 * @param tollgateId
	 * @param nodeId
	 * @return
	 */
	private BattleDataTemplate getBattleDataTemplate(int tollgateId, int nodeId) {
		TollgateNodeDataTemplate nodeDataTemplate = TollgateDataManager
				.getInstance().getTollgateData(tollgateId, nodeId);
		if (nodeDataTemplate == null) {
			logger.error("模板数据不存在!tollgateId="+tollgateId+"nodeId="+nodeId);
			return null;
		}
		int opreateType = nodeDataTemplate.getOpreateType();

		if (opreateType == 0) {
			logger.error("结点的操作类型不能为opreateType=0,0是进入下一个战场!tollgateId="+tollgateId+"nodeId="+nodeId);
			return null;
		}
		int battleId = nodeDataTemplate.getOpreateId();

		Map<Integer, BattleDataTemplate> all = TemplateService.getInstance()
				.getAll(BattleDataTemplate.class);

		if (!all.containsKey(battleId)) {
			logger.error("结点的原型类型中关联的战场Id在战场表中没有相关数据,battleId="+battleId+"tollgateId="+tollgateId+"nodeId="+nodeId);
			return null;
		}
		BattleDataTemplate temp = all.get(battleId);
		return temp;
	}
	/**
	 * 记录经验和级别的变化
	 * @param result
	 * @param isNew
	 */
	private void recordRoleHistory(BattleResult result, boolean isNew) {
		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		if(role==null){
			logger.error("角色为null,无法添加经验!roleId="+getServiceId());
			return;
		}
		if (isNew) {
			result.battleRoleResult.recordNewRole(
					role.getRoleBean().getLevel(), role.getRoleBean().getExp());
		} else {
			result.battleRoleResult.recordOldRole(
					role.getRoleBean().getLevel(), role.getRoleBean().getExp());

		}

	}
	/**
	 * 记录宠物的等级和经验
	 * @param result
	 * @param isNew
	 */
	private void recordPetCurrent(BattleResult result, boolean isNew) {
		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		Collection<Pet> curPetList = role.getPetManager().getCurPetList();
		for (Pet pet : curPetList) {
			if (isNew) {
				result.recordNewPet(pet.getId(), pet.getBean().getLevel(), pet
						.getBean().getExp());
			} else {
				result.recordOldPet(pet.getId(), pet.getBean().getLevel(), pet
						.getBean().getExp());
			}
		}

	}
	/*
	 *发送战斗结果. 
	 */
	private void showBattleResultMsg(BattleResult result) {
		BattleResultMessage message = new BattleResultMessage();
		message.setBattleResult(result);
		this.sendMessage(message);
	}

	private void addRoleExp(int tollgateId) {

		int tollgateShowType = TollgateDataManager.getInstance()
				.getTollgateShowType(tollgateId);

		int costEnergyPoint = TollgateRewardExp
				.getCostEnergyPoint(tollgateShowType);

		int roleExp = TollgateRewardExp
				.rewardRoleExpByEnergyPoint(costEnergyPoint);

		if (roleExp < 0) {
			throw new RuntimeException("副本奖励经验不能为负数");
		}

		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		// 由于早期没有体力,经验会加不上的.
		// 扣体力.
		if (role.addEnergy(-costEnergyPoint, RewardType.BATTLE, true)) {
			role.addExp(roleExp);
			// 加经验
			logger.error("测试期间,体力值,还没有正式使用.");
		}

	}
	/**
	 * 添加宠物的经验.
	 * @param petExp
	 */
	private void addPetExp(int petExp,Role role) {
		role.getPetManager().addCurPetListExp(petExp);

	}
	/**
	 * 添加游戏币.
	 * @param gameCoin
	 */
	private void addGameCoin(int gameCoin,Role role) {

		if (gameCoin < 0) {
			throw new RuntimeException("副本奖励游戏币不能为负数");
		}
		// 加钱
		role.addMoney(gameCoin, RewardType.BATTLE, true);

	}
	/**
	 * 添加掉落组物品
	 * @param dropGroupId
	 */
	private void addDropGroupId(BattleResult result,int dropGroupId) {
		logger.error("测试时,掉落组物品,还没有添加!");
		
		
		List<DropDataTemplate> rollDropGroup = DropDataManager.getInstance().getTollgateDropGroup(getServiceId(),dropGroupId, 1,false);
		
		if(rollDropGroup==null){
			logger.error("掉落组,dropGroupId="+dropGroupId+"不存在!");
			return;
		}
		
		DropDataManager.getInstance().rewardDropItem(getServiceId(),rollDropGroup);
		
		result.rollDropGroup = rollDropGroup;
	}

	private Role getRole() {
		Role role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
		return role;
	}
	public void release() {
		if (roleTollgate != null) {
			roleTollgate.release();
			roleTollgate = null;
			DropDataManager.getInstance().clearHistory(getServiceId());
		}
	}
	/**
	 * 编辑组的结果,进行保存入库的操作.  
	 * 1.首先检测,组Id是否合法.<0 或>=5都是不合法的
	 * 2.检查每一个npcId 是不是当前存在的.
	 */
	@Override
	public void updateGroupOperateResult(List<Long> petIdsList) {
		// TODO Auto-generated method stub
		
		Role role = getRole();
		List<Long> npcIdList = petIdsList;
		if (!checkPetIdExsit(role, npcIdList)) {
			//发送检查失败的信息.
			ResponseMessageUtils.sendResponseMessage(
					MessageCommands.GROUP_UPDATE_MESSAGE.ordinal(),
					GroupUpdateResultType.ERROR, this.getServiceId());
			return;
		}
		
		
		updateGroupResult(role,npcIdList);
		
	}
	/**
	   1.调用设置方法
	 * 2.成功返回信息
	 * @param role
	 * @param groupsList
	 */	
	private void updateGroupResult(Role role,List<Long> petIdsList){
		//1.
		role.getPetManager().resetPetGroup(petIdsList);
		//2.发送成功的消息.
		ResponseMessageUtils.sendResponseMessage(
				MessageCommands.GROUP_UPDATE_MESSAGE.ordinal(),
				GroupUpdateResultType.Success, this.getServiceId());
	}
	
	
	
	private boolean checkPetIdExsit(Role role,List<Long> npcIdList){
		Map<Long, Pet> petMap = role.getPetManager().getPetMap();
		
		if(npcIdList.isEmpty()){
			return false;
		}
		for (Long petId : npcIdList) {
			if(!petMap.containsKey(petId)){
				return false;
			}
		}
		
		return true;
	}
	
	

}
