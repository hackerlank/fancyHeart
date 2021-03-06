package com.doteyplay.game.config.template;

import com.doteyplay.game.constants.sprite.SpritePropType;
import com.doteyplay.game.constants.sprite.SpriteQualityType;
import com.doteyplay.game.gamedata.data.sprite.SpriteQualityPropData;
import com.doteyplay.game.util.excel.ExcelCellBinding;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateConfigException;
import com.doteyplay.game.util.excel.TemplateObject;
import com.doteyplay.game.util.excel.TemplateService;

@ExcelRowBinding
public class SpriteQualiltyPropDataTemplate extends TemplateObject
{
	@ExcelCellBinding
	protected int hpBase;

	@ExcelCellBinding
	protected int hpRate;
	
	@ExcelCellBinding
	protected int apBase;
	
	@ExcelCellBinding
	protected int apRate;
	
	@ExcelCellBinding
	protected int pdBase;
	
	@ExcelCellBinding
	protected int pdRate;
	
	@ExcelCellBinding
	protected int mdBase;
	
	@ExcelCellBinding
	protected int mdRate;
	
	@ExcelCellBinding
	protected int dodgeBase;
	
	@ExcelCellBinding
	protected int crhBase;
	
	@ExcelCellBinding
	protected int healBase;
	
	@ExcelCellBinding
	protected int item1;
	
	@ExcelCellBinding
	protected int item2;
	
	@ExcelCellBinding
	protected int item3;
	
	@ExcelCellBinding
	protected int item4;
	
	@ExcelCellBinding
	protected int item5;
	
	@ExcelCellBinding
	protected int item6;

	@Override
	public void onLoadFinished()
	{
		int spriteId = this.id / 10;
		int quality = this.id % 10;
		SpriteDataObject spriteDataObject = TemplateService.getInstance().get(spriteId, SpriteDataObject.class);
		
		SpriteQualityPropData propData = new SpriteQualityPropData();
		propData.baseValueList.add(hpBase);
		propData.rateList.add(hpRate);
		
		propData.baseValueList.add(apBase);
		propData.rateList.add(apRate);
		
		propData.baseValueList.add(pdBase);
		propData.rateList.add(pdRate);
		
		propData.baseValueList.add(mdBase);
		propData.rateList.add(mdRate);
		
		propData.baseValueList.add(dodgeBase);
		propData.rateList.add(0);
		
		propData.baseValueList.add(crhBase);
		propData.rateList.add(0);
		
		propData.baseValueList.add(healBase);
		propData.rateList.add(0);
		
		propData.spriteQualityType = SpriteQualityType.values()[quality];
		propData.getCurQualityItemList().add(item1);
		propData.getCurQualityItemList().add(item2);
		propData.getCurQualityItemList().add(item3);
		propData.getCurQualityItemList().add(item4);
		propData.getCurQualityItemList().add(item5);
		propData.getCurQualityItemList().add(item6);
		spriteDataObject.propDataList.add(propData);	
	
	}

	public int getHpBase()
	{
		return hpBase;
	}

	public void setHpBase(int hpBase)
	{
		this.hpBase = hpBase;
	}

	public int getHpRate()
	{
		return hpRate;
	}

	public void setHpRate(int hpRate)
	{
		this.hpRate = hpRate;
	}

	public int getApBase()
	{
		return apBase;
	}

	public void setApBase(int apBase)
	{
		this.apBase = apBase;
	}

	public int getApRate()
	{
		return apRate;
	}

	public void setApRate(int apRate)
	{
		this.apRate = apRate;
	}

	public int getPdBase()
	{
		return pdBase;
	}

	public void setPdBase(int pdBase)
	{
		this.pdBase = pdBase;
	}

	public int getPdRate()
	{
		return pdRate;
	}

	public void setPdRate(int pdRate)
	{
		this.pdRate = pdRate;
	}

	public int getMdBase()
	{
		return mdBase;
	}

	public void setMdBase(int mdBase)
	{
		this.mdBase = mdBase;
	}

	public int getMdRate()
	{
		return mdRate;
	}

	public void setMdRate(int mdRate)
	{
		this.mdRate = mdRate;
	}

	public int getDodgeBase()
	{
		return dodgeBase;
	}

	public void setDodgeBase(int dodgeBase)
	{
		this.dodgeBase = dodgeBase;
	}

	public int getCrhBase()
	{
		return crhBase;
	}

	public void setCrhBase(int crhBase)
	{
		this.crhBase = crhBase;
	}

	public int getHealBase()
	{
		return healBase;
	}

	public void setHealBase(int healBase)
	{
		this.healBase = healBase;
	}

	public int getItem1()
	{
		return item1;
	}

	public void setItem1(int item1)
	{
		this.item1 = item1;
	}

	public int getItem2()
	{
		return item2;
	}

	public void setItem2(int item2)
	{
		this.item2 = item2;
	}

	public int getItem3()
	{
		return item3;
	}

	public void setItem3(int item3)
	{
		this.item3 = item3;
	}

	public int getItem4()
	{
		return item4;
	}

	public void setItem4(int item4)
	{
		this.item4 = item4;
	}

	public int getItem5()
	{
		return item5;
	}

	public void setItem5(int item5)
	{
		this.item5 = item5;
	}

	public int getItem6()
	{
		return item6;
	}

	public void setItem6(int item6)
	{
		this.item6 = item6;
	}

	@Override
	public String toString()
	{
		return "SpriteQualiltyPropDataTemplate [hpBase=" + hpBase + ", hpRate="
				+ hpRate + ", apBase=" + apBase + ", apRate=" + apRate
				+ ", pdBase=" + pdBase + ", pdRate=" + pdRate + ", mdBase="
				+ mdBase + ", mdRate=" + mdRate + ", dodgeBase=" + dodgeBase
				+ ", crhBase=" + crhBase + ", healBase=" + healBase
				+ ", item1=" + item1 + ", item2=" + item2 + ", item3=" + item3
				+ ", item4=" + item4 + ", item5=" + item5 + ", item6=" + item6
				+ "]";
	}

}
