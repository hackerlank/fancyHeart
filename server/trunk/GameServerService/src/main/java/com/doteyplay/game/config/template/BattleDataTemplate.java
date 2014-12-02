package com.doteyplay.game.config.template;

import com.doteyplay.game.util.SimpleReflectUtils;
import com.doteyplay.game.util.excel.ExcelCellBinding;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateConfigException;
import com.doteyplay.game.util.excel.TemplateObject;

@ExcelRowBinding
public class BattleDataTemplate extends TemplateObject {

	/**
	 * 战役名字id
	 */
	@ExcelCellBinding
	protected String battleNameId;

	/**
	 * 战役描述Id
	 */
	@ExcelCellBinding
	protected String battleDescId;
	/**
	 * 怪物组1Id
	 */
	@ExcelCellBinding
	protected int monsterGroup1Id;
	/**
	 * 怪物组2Id
	 */
	@ExcelCellBinding
	protected int monsterGroup2Id;
	/**
	 * 怪物组3Id
	 */
	@ExcelCellBinding
	protected int monsterGroup3Id;
	/**
	 * 怪物组4Id
	 */
	@ExcelCellBinding
	protected int monsterGroup4Id;
	/**
	 * 怪物组5Id
	 */
	@ExcelCellBinding
	protected int monsterGroup5Id;

	/**
	 * 游戏币
	 */
	@ExcelCellBinding
	protected int gameCoin;
	/**
	 * 宠物经验
	 */
	@ExcelCellBinding
	protected int petExp;
	/**
	 * 掉落组
	 */
	@ExcelCellBinding
	protected int dropGroupId;
	/**
	 * 场景Id
	 */
	@ExcelCellBinding
	protected String sceneId;
	/**
	 * 掉落物品数量串
	 */
	@ExcelCellBinding
	protected String dropNumStr;
	/**
	 * 掉落物品展示
	 */
	@ExcelCellBinding
	protected String dropGoodsShowStr;
	/**
	 * 开启关卡节点Id串.
	 */
	@ExcelCellBinding
	protected String openTollgateNodeIdStr;
	
	

	public void check() throws TemplateConfigException {
		// TODO Auto-generated method stub
	}

	public String getBattleNameId() {
		return battleNameId;
	}

	public void setBattleNameId(String battleNameId) {
		this.battleNameId = battleNameId;
	}

	public String getBattleDescId() {
		return battleDescId;
	}

	public void setBattleDescId(String battleDescId) {
		this.battleDescId = battleDescId;
	}

	public int getMonsterGroup1Id() {
		return monsterGroup1Id;
	}

	public void setMonsterGroup1Id(int monsterGroup1Id) {
		this.monsterGroup1Id = monsterGroup1Id;
	}

	public int getMonsterGroup2Id() {
		return monsterGroup2Id;
	}

	public void setMonsterGroup2Id(int monsterGroup2Id) {
		this.monsterGroup2Id = monsterGroup2Id;
	}

	public int getMonsterGroup3Id() {
		return monsterGroup3Id;
	}

	public void setMonsterGroup3Id(int monsterGroup3Id) {
		this.monsterGroup3Id = monsterGroup3Id;
	}

	public int getMonsterGroup4Id() {
		return monsterGroup4Id;
	}

	public void setMonsterGroup4Id(int monsterGroup4Id) {
		this.monsterGroup4Id = monsterGroup4Id;
	}

	public int getMonsterGroup5Id() {
		return monsterGroup5Id;
	}

	public void setMonsterGroup5Id(int monsterGroup5Id) {
		this.monsterGroup5Id = monsterGroup5Id;
	}

	public int getGameCoin() {
		return gameCoin;
	}

	public void setGameCoin(int gameCoin) {
		this.gameCoin = gameCoin;
	}

	public int getPetExp() {
		return petExp;
	}

	public void setPetExp(int petExp) {
		this.petExp = petExp;
	}

	public int getDropGroupId() {
		return dropGroupId;
	}

	public void setDropGroupId(int dropGroupId) {
		this.dropGroupId = dropGroupId;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	
	


	public String getDropNumStr() {
		return dropNumStr;
	}

	public void setDropNumStr(String dropNumStr) {
		this.dropNumStr = dropNumStr;
	}

	public String getDropGoodsShowStr() {
		return dropGoodsShowStr;
	}

	public void setDropGoodsShowStr(String dropGoodsShowStr) {
		this.dropGoodsShowStr = dropGoodsShowStr;
	}

	public String getOpenTollgateNodeIdStr() {
		return openTollgateNodeIdStr;
	}

	public void setOpenTollgateNodeIdStr(String openTollgateNodeIdStr) {
		this.openTollgateNodeIdStr = openTollgateNodeIdStr;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method
		return super.toString()+SimpleReflectUtils.reflect(this);
	}

}
