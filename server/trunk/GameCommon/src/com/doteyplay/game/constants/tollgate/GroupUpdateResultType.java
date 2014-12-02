package com.doteyplay.game.constants.tollgate;

import com.doteyplay.game.constants.IResponseTextId;

/**
 * @className:TollgateErrorType.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年7月18日 下午6:24:50
 */
public enum GroupUpdateResultType implements IResponseTextId{

	Success(0),
	ERROR(40141),//宠物组更新失败
	
	;
	
	private int textId;
	
	private GroupUpdateResultType(int  textId){
		this.textId = textId;
	}

	@Override
	public int getTextId() {
		// TODO Auto-generated method stub
		return textId;
	}
}

