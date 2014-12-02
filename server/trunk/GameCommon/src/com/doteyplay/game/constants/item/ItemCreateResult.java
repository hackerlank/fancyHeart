package com.doteyplay.game.constants.item;

import com.doteyplay.game.constants.IResponseTextId;

public enum ItemCreateResult implements IResponseTextId
{
	SUCCESS(0),
	ITEM_NOT_ENOUGH(20001),
	CREATE_ITEM_NOT_EXIST(20002),
	MONEY_NOT_ENOUGH(20003),
	;

	ItemCreateResult(int textId)
	{
		this.textId = textId;
	}
	
	private int textId;

	@Override
	public int getTextId()
	{
		return textId;
	}
}
