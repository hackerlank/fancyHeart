package com.doteyplay.game.constants.pet;

import com.doteyplay.game.constants.IResponseTextId;

public enum PetStarUpgradeResult  implements IResponseTextId
{
	SUCCESS(0),
	//宠物不存在
	PET_NOT_FOUND(30006),
	//星级达到最大等级
	STAR_AT_MAX_LEVEL(30007),
	//缺少星级数据
	STAR_DATA_NULL(30008),	
	//金币不足
	MONEY_NOT_ENOUGH(30009),
	//物品不足
	ITEM_NOT_ENOUGH(30010),
	;
	
	PetStarUpgradeResult(int textId)
	{
		this.textId = textId;
	}
	
	private int textId;
	
	public int getTextId()
	{
		return textId;
	}
}
