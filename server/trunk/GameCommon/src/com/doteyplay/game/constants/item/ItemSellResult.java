package com.doteyplay.game.constants.item;

import com.doteyplay.game.constants.IResponseTextId;

public enum ItemSellResult implements IResponseTextId
{
	SUCCESS(0),
	ITEM_NOT_ENOUGH(20004)
	;
	
	private ItemSellResult(int textId)
	{
		this.textId = textId;
	}
	
	private int textId;
	
	public int getTextId()
	{
		return textId;
	}
}
