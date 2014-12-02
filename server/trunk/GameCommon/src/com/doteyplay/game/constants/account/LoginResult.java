package com.doteyplay.game.constants.account;

import com.doteyplay.game.constants.IResponseTextId;

public enum LoginResult implements IResponseTextId
{
	//成功
	SUCCESS(0),
	//身份认证失败
	AUTH_FAIL(10001),
	//角色不存在
	ROLE_NOT_EXSIT(10002),
	//角色名称重复
	ROLE_NAME_EXSIT(10003),
	;
	private int textId;
	
	LoginResult(int textId)
	{
		this.textId = textId;
	}
	
	@Override
	public int getTextId()
	{
		return textId;
	}
	
}
