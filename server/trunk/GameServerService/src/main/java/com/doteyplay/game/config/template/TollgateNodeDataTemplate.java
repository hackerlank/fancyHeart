package com.doteyplay.game.config.template;

import java.util.Map;

import com.doteyplay.game.util.SimpleReflectUtils;
import com.doteyplay.game.util.excel.ExcelCellBinding;
import com.doteyplay.game.util.excel.ExcelRowBinding;
import com.doteyplay.game.util.excel.TemplateConfigException;
import com.doteyplay.game.util.excel.TemplateObject;
import com.doteyplay.game.util.excel.TemplateService;
/**
 * 
* @className:TollgateNodeDataTemplate.java
* @classDescription: 关卡节点数据
* @author:Tom.Zheng
* @createTime:2014年6月23日 下午3:51:30
 */
@ExcelRowBinding
public class TollgateNodeDataTemplate extends TemplateObject {

	
	/**
	 * 对应关卡id
	 */
	@ExcelCellBinding
	protected int tollgateGateId;

	/**
	 * 关卡操作类型
	 */
	@ExcelCellBinding
	protected int opreateType;
	/**
	 * 关卡Id/战役Id
	 */
	@ExcelCellBinding
	protected int opreateId;
	
	/**
	 * 节点可以打多少次
	 */
	@ExcelCellBinding
	protected int times;
	/**
	 * 次数是否可以购买
	 */
	@ExcelCellBinding
	protected int isBuyTimes;
	/**
	 * 通关后是否显示
	 */
	@ExcelCellBinding
	protected int isShow;
	
	
	

	@Override
	public void check() throws TemplateConfigException {
		// TODO Auto-generated method stub

	}
	
	

	

	@Override
	public void patchUp() throws Exception {
		// TODO Auto-generated method stub
		super.patchUp();
		
		Map<Integer, TollgateDataObject> all = TemplateService
				.getInstance().getAll(TollgateDataObject.class);
		all.get(this.getTollgateGateId()).addNode(this);
		
	}





	public int getTollgateGateId() {
		return tollgateGateId;
	}



	public void setTollgateGateId(int tollgateGateId) {
		this.tollgateGateId = tollgateGateId;
	}

	public int getOpreateType() {
		return opreateType;
	}

	public void setOpreateType(int opreateType) {
		this.opreateType = opreateType;
	}

	public int getOpreateId() {
		return opreateId;
	}

	public void setOpreateId(int opreateId) {
		this.opreateId = opreateId;
	}

	public int getTimes() {
		return times;
	}



	public void setTimes(int times) {
		this.times = times;
	}



	public int getIsBuyTimes() {
		return isBuyTimes;
	}



	public void setIsBuyTimes(int isBuyTimes) {
		this.isBuyTimes = isBuyTimes;
	}



	public int getIsShow() {
		return isShow;
	}



	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+SimpleReflectUtils.reflect(this);
	}

}
