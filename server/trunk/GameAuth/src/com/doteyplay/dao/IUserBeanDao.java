package com.doteyplay.dao;


import java.util.List;

import com.doteyplay.bean.UserBean;
import com.doteyplay.core.dbcs.daoproxy.IDaoExecutor;
import com.doteyplay.core.dbcs.executor.DAOInfo;

public interface IUserBeanDao extends IDaoExecutor {
	@DAOInfo(Params = "")
	public UserBean selectUserBean(int userId);
	
	@DAOInfo(Params = "")
	public List<UserBean> selectAllUserBean();
	
	@DAOInfo(Params = "")
	public UserBean selectUserBeanByName(String userName);

	@DAOInfo(Params = "")
	public long insertUserBean(UserBean userBean);
	
	@DAOInfo(Params = "")
	public void updateUserBean(UserBean userBean);
	
	@DAOInfo(Params = "")
	public UserBean selectUserBeanByUUID(String uuId);

}