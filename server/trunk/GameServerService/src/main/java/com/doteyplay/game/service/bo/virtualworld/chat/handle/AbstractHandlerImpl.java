package com.doteyplay.game.service.bo.virtualworld.chat.handle;

import com.doteyplay.core.dbcs.DBCS;
import com.doteyplay.game.domain.gamebean.RoleBean;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.persistence.serverdata.role.IRoleBeanDao;
import com.doteyplay.game.service.bo.virtualworld.chat.patameter.IPatameterObject;
import com.doteyplay.game.service.runtime.GlobalRoleCache;
import common.Logger;

/**
 * @className:AbstractHandlerImpl.java
 * @classDescription:
 * @author:Tom.Zheng
 * @createTime:2014年7月10日 下午5:40:59
 */
public abstract class AbstractHandlerImpl<T extends IPatameterObject > implements IChatHandler<T>{

	private static  Logger logger  = Logger.getLogger(AbstractHandlerImpl.class);
	
	@Override
	public void handle(T t,long selfRoleId) {
		// TODO Auto-generated method stub
		if(t.getRoleIds()==null||t.getRoleIds().size()==0){//不写的时候.
			handleRoleItem(t, selfRoleId);
		}else{
			for(Long roleId:t.getRoleIds()){
				if(roleId==-1){//写-1的时候.-1是指自已.
					roleId = selfRoleId;
				}
			    handleRoleItem(t, roleId);
				
				
			}
		}
	}

	private void handleRoleItem(T t, Long roleId) {
		Role role = GlobalRoleCache.getInstance().getRoleById(roleId);
		if(role != null){
			onlineHandle(role, t);
		}else{
			if(!checkRoleExsit(roleId)){
				logger.error("GM do 角色物品添加,Id对应的角色不存在");
			}
			offlineHandle(roleId, t);
		}
	}
	
	public abstract void onlineHandle(Role role,T t);
	
	public abstract void offlineHandle(long roleId,T t);
	
	private boolean checkRoleExsit(long roleId){
		//采用邮件服务.发送物品或直接插入DB
		IRoleBeanDao dao = DBCS.getExector(IRoleBeanDao.class);
		
		RoleBean roleBean = dao.selectRoleBean(roleId);
		
		if(roleBean == null){
//			throw new RuntimeException("GM do 角色物品添加,Id对应的角色不存在");
			return false;
		}
		return true;
	}
	
}
