package com.doteyplay.game.domain.gamebean;

public class PetBean extends SpriteBean
{
	private long roleId;
	/**
	 * 英雄是否在组内
	 * 0表示不在组内.1表示在组 
	 */
	private int groupId;
	
	
	public long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(long roleId)
	{
		this.roleId = roleId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	
	

	
}
