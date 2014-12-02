package com.doteyplay.game.service.bo.virtualworld.chat.patameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @className:AbstractPatameterObject.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014��7��10�� ����4:25:46
 */
public abstract class AbstractPatameterObject implements IPatameterObject {

	private List<Long> roleIds;

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public void addRoles(String[] rIds) {
		if (roleIds == null) {
			roleIds = new ArrayList<Long>();
		}
		if(rIds!=null){
			for (String str : rIds) {
				if(str!=null&&!str.equals("")){
					roleIds.add(Long.parseLong(str));
				}
			}
		}
	}

}
