package com.doteyplay.game.message.utils;

import com.doteyplay.core.bhns.BOServiceManager;
import com.doteyplay.core.bhns.gateway.IGateWayService;
import com.doteyplay.game.constants.IResponseTextId;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.message.common.CommonResponseMessage;

/**
 * @className:ResponseMessageUtils.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年7月18日 下午6:14:14
 */
public class ResponseMessageUtils {

	/**
	 * @param messageId
	 * @param state
	 * @param roleId
	 */
	public static  void sendResponseMessage(int messageId,IResponseTextId textId,long roleId){
		CommonResponseMessage message = new CommonResponseMessage();
		message.setMessageId(messageId);
		message.setState(textId.getTextId());
		
		IGateWayService gameGateWayService = BOServiceManager.findService(IGateWayService.PORTAL_ID, roleId);
		gameGateWayService.sendMessage(message);
		
	}
	
	/**
	 * 发送给客户端提醒参数错误的消息
	 * @param roleId
	 */
	public static void sendClientArgementErrorMessage(long roleId)
	{
		CommonResponseMessage message = new CommonResponseMessage();
		message.setMessageId(-1);
		message.setState(-1);
		
		IGateWayService gameGateWayService = BOServiceManager.findService(IGateWayService.PORTAL_ID, roleId);
		gameGateWayService.sendMessage(message);
	}
	
	
}
