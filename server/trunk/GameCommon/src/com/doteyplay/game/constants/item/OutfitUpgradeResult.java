package com.doteyplay.game.constants.item;

import com.doteyplay.game.constants.IResponseTextId;

public enum OutfitUpgradeResult  implements IResponseTextId
{
	SUCCESS(0),
	//物品不足
	ITEM_NOT_ENOUGH(20011),
	//找不到穿装备的精灵
	NOT_FOUND_SPRITE(20012),
	//品质最大值
	MAX_QUALITY_ALREADY(20013),
	//金币不足
	MONEY_NOT_ENOUGH(20014),
	//不符合变异品质条件
	NOT_FIT_UP_QUALITY_LEVEL(20015),
	//被动技能等级已满
	SKILL_LEVEL_FULL(20016),
	;
	
	private OutfitUpgradeResult(int textId)
	{
		this.textId = textId;
	}
	
	private int textId;
	
	public int getTextId()
	{
		return textId;
	}
}
