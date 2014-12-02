package com.doteyplay.game.constants.item;

import com.doteyplay.game.constants.IResponseTextId;

public enum OutfitInstallResult  implements IResponseTextId
{
	SUCCESS(0),
	//已经穿上了
	ITEM_HAS_EXIST(20005),
	//物品不存在
	NOT_FOUND_ITEM(20006),
	//等级不足
	LEVEL_LIMIT(20007),
	//找不到穿装备的精灵
	NOT_FOUND_SPRITE(20008),
	
	//装备不匹配
	ITEM_NOT_FIT(20009),
	//物品数量不足
	ITEM_NOT_ENOUGH(20010),
	;
	OutfitInstallResult(int textId)
	{
		this.textId = textId;
	}
	
	private int textId;
	
	public int getTextId()
	{
		return textId;
	}
}
