package com.doteyplay.game.action.tollgate;

import com.doteyplay.core.action.ActionAnnotation;
import com.doteyplay.core.action.ServiceMessageAction;
import com.doteyplay.exception.MessageProcessException;
import com.doteyplay.game.message.tollgate.GroupUpdateMessage;
import com.doteyplay.game.service.bo.tollgate.ITollgateInfoService;

/**
 * 英雄编组操作信息
* @className:GroupUpdateMessageAction.java
* @classDescription:
* @author:Tom.Zheng
* @createTime:2014年8月19日 下午3:42:00
 */
@ActionAnnotation(message = com.doteyplay.game.message.tollgate.GroupUpdateMessage.class)
public  class GroupUpdateMessageAction implements ServiceMessageAction<GroupUpdateMessage, ITollgateInfoService>{

	@Override
	public void processMessage(GroupUpdateMessage message,
			ITollgateInfoService p) throws MessageProcessException {
		// TODO Auto-generated method stub
		p.updateGroupOperateResult(message.getPetIdsList());
	}

}
