package com.doteyplay.game.constants.pet;

import com.doteyplay.game.constants.IResponseTextId;

/**
 * @className:PetErrorMsg.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年8月29日 下午4:44:38
 */
public enum PetErrorMsg implements IResponseTextId {

	Success("成功",0),
	ERROR_HAS_EXIST("已经存在该宠物",30001),
	ERROR_NO_EXIST("不存在宠物",30002),
	ERROR_NO_STONE("没有足够石头",30003),
	ERROR_SYSTEM_0("系统忙碌",30004),
	ERROR_SYSTEM_1("系统召唤失败",30005),
	;
	
	private String msg;
	
	private int textId;
	
	private PetErrorMsg(String msg,int textId){
		this.msg = msg;
	}

	@Override
	public int getTextId()
	{
		return textId;
	}
}
