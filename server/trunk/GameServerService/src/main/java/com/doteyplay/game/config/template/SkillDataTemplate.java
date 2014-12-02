package com.doteyplay.game.config.template;

import com.doteyplay.game.util.excel.ExcelCellBinding;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateConfigException;
import com.doteyplay.game.util.excel.TemplateObject;

@ExcelRowBinding
public class SkillDataTemplate extends TemplateObject
{
	/**
	 * 技能名id
	 */
	@ExcelCellBinding
	protected String skillNameId;

	/**
	 * 技能描述id
	 */
	@ExcelCellBinding
	protected String skillDescId;

	/**
	 * 技能类型
	 */
	@ExcelCellBinding
	protected int skillType;
	/**
	 * 能量消耗
	 */
	@ExcelCellBinding
	protected boolean isAtkFlyTarget;

	/**
	 * 被动类型
	 */
	@ExcelCellBinding
	protected int skillPassiveType;
	/**
	 * 被动触发机率
	 */
	@ExcelCellBinding
	protected int passiveTriggerProp;

	/**
	 * 被动技能参数
	 */
	@ExcelCellBinding
	protected int passiveSkillPrameter;
	/**
	 * 技能伤害类型
	 */
	@ExcelCellBinding
	protected int skillDamageType;
	/**
	 * 引导次数
	 */
	@ExcelCellBinding
	protected int skillGuideCount;
	/**
	 * 引导间隔
	 */
	@ExcelCellBinding
	protected int skillGuideBetween;
	/**
	 * 影响范围类型
	 */
	@ExcelCellBinding
	protected int affectScopeType;
	/**
	 * 参数1
	 */
	@ExcelCellBinding
	protected int affectParam1;
	/**
	 * 参数2
	 */
	@ExcelCellBinding
	protected int affectParam2;
	/**
	 * 参数3
	 */
	@ExcelCellBinding
	protected int affectParam3;
	/**
	 * 参数4
	 */
	@ExcelCellBinding
	protected int affectParam4;

	/**
	 * 参数5
	 */
	@ExcelCellBinding
	protected int affectParam5;
	/**
	 * 目标选择策略
	 */
	@ExcelCellBinding
	protected int targetSelectStrategy;
	/**
	 * 【最大蓄力时间】
	 */
	@ExcelCellBinding
	protected int maxAccumulatePowerTime;
	/**
	 * 【蓄力起始万分比】
	 */
	@ExcelCellBinding
	protected int accumulatePowerAddBit;

	/**
	 * 【蓄力起始加成实数】
	 */
	@ExcelCellBinding
	protected int accumulatePowerAddValue;

	/**
	 * 【蓄力攻击加成万分比】
	 */
	@ExcelCellBinding
	protected int accumulatePowerAttackAddBit;
	/**
	 * 【蓄力攻击加成实数】
	 */
	@ExcelCellBinding
	protected int accumulatePowerAttackAddValue;

	/**
	 * 技能效果类型
	 */
	@ExcelCellBinding
	protected int skillEffectType;
	/**
	 * 技能效果参数1
	 */
	@ExcelCellBinding
	protected int skillEffectParam1;
	/**
	 * 技能效果参数2
	 */
	@ExcelCellBinding
	protected int skillEffectParam2;
	/**
	 * 技能效果参数3
	 */
	@ExcelCellBinding
	protected int skillEffectParam3;
	/**
	 * 技能效果参数4
	 */
	@ExcelCellBinding
	protected int skillEffectParam4;

	/**
	 * buffID
	 */
	@ExcelCellBinding
	protected int buffId;
	/**
	 * buff概率
	 */
	@ExcelCellBinding
	protected int buffProp;
//
//	/**
//	 * 施法时间
//	 */
//	@ExcelCellBinding
//	protected int castSkillTime;

	public String getSkillNameId()
	{
		return skillNameId;
	}

	public void setSkillNameId(String skillNameId)
	{
		this.skillNameId = skillNameId;
	}

	public String getSkillDescId()
	{
		return skillDescId;
	}

	public void setSkillDescId(String skillDescId)
	{
		this.skillDescId = skillDescId;
	}

	public int getSkillType()
	{
		return skillType;
	}

	public void setSkillType(int skillType)
	{
		this.skillType = skillType;
	}

	public boolean isAtkFlyTarget()
	{
		return isAtkFlyTarget;
	}

	public void setAtkFlyTarget(boolean isAtkFlyTarget)
	{
		this.isAtkFlyTarget = isAtkFlyTarget;
	}

	public int getSkillPassiveType()
	{
		return skillPassiveType;
	}

	public void setSkillPassiveType(int skillPassiveType)
	{
		this.skillPassiveType = skillPassiveType;
	}

	public int getPassiveTriggerProp()
	{
		return passiveTriggerProp;
	}

	public void setPassiveTriggerProp(int passiveTriggerProp)
	{
		this.passiveTriggerProp = passiveTriggerProp;
	}

	public int getPassiveSkillPrameter()
	{
		return passiveSkillPrameter;
	}

	public void setPassiveSkillPrameter(int passiveSkillPrameter)
	{
		this.passiveSkillPrameter = passiveSkillPrameter;
	}

	public int getSkillDamageType()
	{
		return skillDamageType;
	}

	public void setSkillDamageType(int skillDamageType)
	{
		this.skillDamageType = skillDamageType;
	}

	public int getSkillGuideCount()
	{
		return skillGuideCount;
	}

	public void setSkillGuideCount(int skillGuideCount)
	{
		this.skillGuideCount = skillGuideCount;
	}

	public int getSkillGuideBetween()
	{
		return skillGuideBetween;
	}

	public void setSkillGuideBetween(int skillGuideBetween)
	{
		this.skillGuideBetween = skillGuideBetween;
	}

	public int getAffectScopeType()
	{
		return affectScopeType;
	}

	public void setAffectScopeType(int affectScopeType)
	{
		this.affectScopeType = affectScopeType;
	}

	public int getAffectParam1()
	{
		return affectParam1;
	}

	public void setAffectParam1(int affectParam1)
	{
		this.affectParam1 = affectParam1;
	}

	public int getAffectParam2()
	{
		return affectParam2;
	}

	public void setAffectParam2(int affectParam2)
	{
		this.affectParam2 = affectParam2;
	}

	public int getAffectParam3()
	{
		return affectParam3;
	}

	public void setAffectParam3(int affectParam3)
	{
		this.affectParam3 = affectParam3;
	}

	public int getAffectParam4()
	{
		return affectParam4;
	}

	public void setAffectParam4(int affectParam4)
	{
		this.affectParam4 = affectParam4;
	}

	public int getAffectParam5()
	{
		return affectParam5;
	}

	public void setAffectParam5(int affectParam5)
	{
		this.affectParam5 = affectParam5;
	}

	public int getTargetSelectStrategy()
	{
		return targetSelectStrategy;
	}

	public void setTargetSelectStrategy(int targetSelectStrategy)
	{
		this.targetSelectStrategy = targetSelectStrategy;
	}

	public int getMaxAccumulatePowerTime()
	{
		return maxAccumulatePowerTime;
	}

	public void setMaxAccumulatePowerTime(int maxAccumulatePowerTime)
	{
		this.maxAccumulatePowerTime = maxAccumulatePowerTime;
	}

	public int getAccumulatePowerAddBit()
	{
		return accumulatePowerAddBit;
	}

	public void setAccumulatePowerAddBit(int accumulatePowerAddBit)
	{
		this.accumulatePowerAddBit = accumulatePowerAddBit;
	}

	public int getAccumulatePowerAddValue()
	{
		return accumulatePowerAddValue;
	}

	public void setAccumulatePowerAddValue(int accumulatePowerAddValue)
	{
		this.accumulatePowerAddValue = accumulatePowerAddValue;
	}

	public int getAccumulatePowerAttackAddBit()
	{
		return accumulatePowerAttackAddBit;
	}

	public void setAccumulatePowerAttackAddBit(int accumulatePowerAttackAddBit)
	{
		this.accumulatePowerAttackAddBit = accumulatePowerAttackAddBit;
	}

	public int getAccumulatePowerAttackAddValue()
	{
		return accumulatePowerAttackAddValue;
	}

	public void setAccumulatePowerAttackAddValue(
			int accumulatePowerAttackAddValue)
	{
		this.accumulatePowerAttackAddValue = accumulatePowerAttackAddValue;
	}

	public int getSkillEffectType()
	{
		return skillEffectType;
	}

	public void setSkillEffectType(int skillEffectType)
	{
		this.skillEffectType = skillEffectType;
	}

	public int getSkillEffectParam1()
	{
		return skillEffectParam1;
	}

	public void setSkillEffectParam1(int skillEffectParam1)
	{
		this.skillEffectParam1 = skillEffectParam1;
	}

	public int getSkillEffectParam2()
	{
		return skillEffectParam2;
	}

	public void setSkillEffectParam2(int skillEffectParam2)
	{
		this.skillEffectParam2 = skillEffectParam2;
	}

	public int getSkillEffectParam3()
	{
		return skillEffectParam3;
	}

	public void setSkillEffectParam3(int skillEffectParam3)
	{
		this.skillEffectParam3 = skillEffectParam3;
	}

	public int getSkillEffectParam4()
	{
		return skillEffectParam4;
	}

	public void setSkillEffectParam4(int skillEffectParam4)
	{
		this.skillEffectParam4 = skillEffectParam4;
	}

	public int getBuffId()
	{
		return buffId;
	}

	public void setBuffId(int buffId)
	{
		this.buffId = buffId;
	}

	public int getBuffProp()
	{
		return buffProp;
	}

	public void setBuffProp(int buffProp)
	{
		this.buffProp = buffProp;
	}
//
//	public int getCastSkillTime()
//	{
//		return castSkillTime;
//	}
//
//	public void setCastSkillTime(int castSkillTime)
//	{
//		this.castSkillTime = castSkillTime;
//	}

	@Override
	public String toString()
	{
		return "SkillDataTemplate skillId=[" + skillNameId + "],skillDescId=["
				+ skillDescId + "],skillType[" + skillType + "],isAtkFlyTarget["
				+ isAtkFlyTarget + "],skillPassiveType[" + skillPassiveType
				+ "],passiveTriggerProp[" + passiveTriggerProp
				+ "],passiveSkillPrameter[" + passiveSkillPrameter
				+ "],skillDamageType[" + skillDamageType + "],skillGuideCount["
				+ skillGuideCount + "],skillGuideBetween[" + skillGuideBetween
				+ "],affectScopeType[" + affectScopeType + "],affectParam1["
				+ affectParam1 + "],affectParam2[" + affectParam2
				+ "],affectParam3[" + affectParam3 + "],affectParam4["
				+ affectParam4 + "],affectParam5[" + affectParam5
				+ "],targetSelectStrategy[" + targetSelectStrategy
				+ "],maxAccumulatePowerTime[" + maxAccumulatePowerTime
				+ "],accumulatePowerAddBit[" + accumulatePowerAddBit
				+ "],accumulatePowerAddValue[" + accumulatePowerAddValue
				+ "],accumulatePowerAttackAddBit["
				+ accumulatePowerAttackAddBit
				+ "],accumulatePowerAttackAddValue["
				+ accumulatePowerAttackAddValue + "],skillEffectType["
				+ skillEffectType + "],skillEffectParam1[" + skillEffectParam1
				+ "],skillEffectParam2[" + skillEffectParam2
				+ "],skillEffectParam3[" + skillEffectParam3
				+ "],skillEffectParam4[" + skillEffectParam4 + "],buffId["
				+ buffId + "],buffProp[" + buffProp + "],castSkillTime[";
//				+ castSkillTime + "]";
	}

}
