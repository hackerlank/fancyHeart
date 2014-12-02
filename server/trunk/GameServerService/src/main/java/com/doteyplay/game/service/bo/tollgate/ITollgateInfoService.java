package com.doteyplay.game.service.bo.tollgate;

import java.util.List;

import com.doteyplay.core.bhns.ISimpleService;
import com.doteyplay.game.BOConst;
import com.doteyplay.game.message.tollgate.ShowTollgateDetailMessage;

/**
 * 副本信息服务接口
 * 
 * @author Tom.Zheng
 * 
 */
public interface ITollgateInfoService extends ISimpleService {

	public final static int PORTAL_ID = BOConst.BO_TOLLGATE;

	/**
	 * 初使化基本信息.玩家登录时,需要从DB中,将关卡相关的信息读出来,
	 * 进行解析,附值相关的内容.
	 * 如果玩家是第一次登录,需要建立第一条信息,存贮DB.
	 * 非第一次,将原信息解析出来.
	 */
	public void initlize();

	/**
	 * 向用户展示,所有的关卡的信息及关卡内部节点的信息.
	 * 详情信息,在登录时,提供给C.或C主动来调用.
	 */
	public ShowTollgateDetailMessage showTollgateDetailInfo();
	/**
	 * 发送关卡开放信息.
	 * 开启或关闭 某一关卡或节点时,主动通知C.
	 */
	public void sendNodeChangeInfo(int tollgateId,int nodeId);

	/**
	 * 进入节点时,需要进行验证.
	 * 提示进入节点的次数.
	 */
	public void enterBattle(int tollgateId,int nodeId,int groupId);
	
	/**
	 * 接受战斗结果的消息.
	 * @param tollgateId 关卡Id
	 * @param nodeId 节点Id
	 * @param star 战斗结果.几星.
	 */
	public void acceptBattleResult(int tollgateId,int nodeId,int star);
	
	/**
	 * 释放资源
	 */
	public void release();
	
	/**
	 * 更新组的编辑信息
	 * @param groupsList
	 */
	public void updateGroupOperateResult(List<Long> petIdsList);


}
