package com.doteyplay.game.gamedata.data.skill;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.doteyplay.game.constants.ActivateEvent;
import com.doteyplay.game.constants.DamageType;
import com.doteyplay.game.constants.skill.SkillActionType;
import com.doteyplay.game.constants.skill.SkillAmmoType;
import com.doteyplay.game.constants.skill.SkillEffectRange;
import com.doteyplay.game.constants.skill.SkillTargetSelectType;
import com.doteyplay.game.gamedata.data.IGameData;

public class SkillGameData implements IGameData
{
	/**
	 * �ȼ�����
	 */
	public int levelCount;
	/**
	 * ����id
	 */
	public int descId;
	/**
	 * ʩ������Ч��id
	 */
	public String resSkillActionId="";
	/**
	 * ʩ����Чid
	 */
	public String resSkillEffectId="";
	/**
	 * ������Ч��id
	 */
	public String resBeAttackId="";

	/**
	 * Ӱ�췶Χ
	 */
	public SkillEffectRange effectRange = SkillEffectRange.SELF;
	/**
	 * �˺�����
	 */
	public DamageType damageType = DamageType.PHYSICS;
	/**
	 * �¼�����
	 */
	public ActivateEvent event = ActivateEvent.NONE;
	
	/**
	 * �¼�����
	 */
	public String eventParam = "";

	/**
	 * ��������
	 */
	public SkillAmmoType ammoType = SkillAmmoType.DEFAULT;

	/**
	 * ѡ�����
	 */
	public SkillTargetSelectType selectType = SkillTargetSelectType.DEFAULT;

	/**
	 * ��������
	 */
	public SkillActionType actionType = SkillActionType.INITIATIVE;
	
	/**
	 * ��������
	 */
	public int leadCount;
	
	/**
	 * ��������֮��Ĳ�ֵ
	 */
	public int leadTimeDelta;

	/**
	 * �������ķ�����
	 */
	public int cost;
	
	/////////////////////////////////////�������///////////////////////////////////////////
	
	/**
	 * ����ʱ��
	 */
	public int storageTime;
	
	/**
	 * ��ʼ�ӳ�
	 */
	public int storageStartRate;
	
	/**
	 * ��ʼ��ֵ
	 */
	public int storageStartValue;
	
	/**
	 * �����ӳ�
	 */
	public int storageEndRate;
	
	/**
	 * ������ֵ
	 */
	public int storageEndValue;
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * �ȼ�����
	 */
	public List<SkillLevelGameData> levelDataList = new  ArrayList<SkillLevelGameData>();
	

	@Override
	public void load(DataInputStream in) throws IOException
	{
		this.levelCount = in.readInt();
		this.descId = in.readInt();
		
		this.resSkillActionId = in.readUTF();
		this.resSkillEffectId = in.readUTF();
		this.resBeAttackId = in.readUTF();

		this.effectRange = SkillEffectRange.values()[in.readByte()];
		this.damageType = DamageType.values()[in.readByte()];
		this.event = ActivateEvent.values()[in.readByte()];
		this.eventParam = in.readUTF();
		
		this.ammoType = SkillAmmoType.values()[in.readByte()];
		this.selectType = SkillTargetSelectType.values()[in.readByte()];
		this.actionType = SkillActionType.values()[in.readByte()];
		
		this.leadCount = in.readInt();
		this.leadTimeDelta = in.readInt();
		
		this.cost = in.readInt();
		
		this.storageTime = in.readInt();
		this.storageStartRate = in.readInt();
		this.storageStartValue = in.readInt();
		this.storageEndRate = in.readInt();
		this.storageEndValue = in.readInt();
				
		int size = in.readInt();
		for(int i = 0; i < size ; i ++)
		{
			SkillLevelGameData data = new SkillLevelGameData();
			data.load(in);
			levelDataList.add(data);
		}
	}

	@Override
	public void save(DataOutputStream out) throws IOException
	{
		out.writeInt(levelCount);
		out.writeInt(descId);
		
		out.writeUTF(resSkillActionId);
		out.writeUTF(resSkillEffectId);
		out.writeUTF(resBeAttackId);
		
		out.writeByte(this.effectRange.ordinal());
		out.writeByte(this.damageType.ordinal());
		out.writeByte(this.event.ordinal());
		
		out.writeUTF(this.eventParam);
		
		out.writeByte(this.ammoType.ordinal());
		out.writeByte(this.selectType.ordinal());
		out.writeByte(this.actionType.ordinal());
		
		out.writeInt(leadCount);
		out.writeInt(leadTimeDelta);
		
		out.writeInt(cost);
		
		out.writeInt(storageTime);
		out.writeInt(storageStartRate);
		out.writeInt(storageStartValue);
		out.writeInt(storageEndRate);
		out.writeInt(storageEndValue);
		
		out.writeInt(this.levelDataList.size());
		for(int i = 0 ; i < levelDataList.size() ; i ++)
		{
			levelDataList.get(i).save(out);
		}
		
	}

}