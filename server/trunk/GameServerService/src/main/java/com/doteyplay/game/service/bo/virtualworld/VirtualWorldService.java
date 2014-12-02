package com.doteyplay.game.service.bo.virtualworld;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.doteyplay.core.bhns.AbstractSimpleService;
import com.doteyplay.core.bhns.BOServiceManager;
import com.doteyplay.core.bhns.active.ActiveServiceHolder;
import com.doteyplay.core.bhns.gateway.IGateWayService;
import com.doteyplay.core.dbcs.DBCS;
import com.doteyplay.game.config.ServerConfig;
import com.doteyplay.game.constants.IdType;
import com.doteyplay.game.constants.account.LoginResult;
import com.doteyplay.game.constants.chat.ChatConstant;
import com.doteyplay.game.domain.common.IdGenerator;
import com.doteyplay.game.domain.common.NameGenerator;
import com.doteyplay.game.domain.gamebean.RoleBean;
import com.doteyplay.game.domain.gamebean.UserBean;
import com.doteyplay.game.domain.role.Role;
import com.doteyplay.game.domain.user.User;
import com.doteyplay.game.message.chat.ChatMessage;
import com.doteyplay.game.message.login.LoginGameMessage;
import com.doteyplay.game.persistence.serverdata.role.IRoleBeanDao;
import com.doteyplay.game.service.bo.gateway.IGameGateWayService;
import com.doteyplay.game.service.bo.item.IItemService;
import com.doteyplay.game.service.bo.role.IRoleService;
import com.doteyplay.game.service.bo.tollgate.ITollgateInfoService;
import com.doteyplay.game.service.bo.virtualworld.chat.ChatHanderFactory;
import com.doteyplay.game.service.bo.virtualworld.chat.ResolvePatameterFactory;
import com.doteyplay.game.service.bo.virtualworld.chat.handle.IChatHandler;
import com.doteyplay.game.service.bo.virtualworld.chat.patameter.IPatameterObject;
import com.doteyplay.game.service.bo.virtualworld.chat.resolve.IChatResolve;
import com.doteyplay.game.service.runtime.GlobalRoleCache;
import com.doteyplay.game.service.runtime.GlobalUserCache;
import com.doteyplay.support.auth.AuthHandler;

public class VirtualWorldService extends
		AbstractSimpleService<IVirtualWorldService> implements
		IVirtualWorldService
{

	private static final Logger logger = Logger
			.getLogger(VirtualWorldService.class);

	private Role role;

	@Override
	public int getPortalId()
	{
		return IVirtualWorldService.PORTAL_ID;
	}

	@Override
	public void initialize()
	{
		role = GlobalRoleCache.getInstance().getRoleById(getServiceId());
	}

	@Override
	public void doLogin(long sessionId, String uuId, String sessionKey,
			int areaId)
	{
		LoginGameMessage message = new LoginGameMessage();

		if (uuId == null || sessionKey == null)
		{
			message.setResult(LoginResult.AUTH_FAIL);
			IGateWayService gateWayService = BOServiceManager
					.findDefaultService(IGateWayService.PORTAL_ID,
							getGatewayEndpointBySessionId(sessionId));
			IoBuffer buffer = message.encodeIoBuffer();
			int byteLength = buffer.limit();
			byte[] tmpMessageBytes = new byte[byteLength];
			buffer.get(tmpMessageBytes, 0, byteLength);
			gateWayService.sendMessage(tmpMessageBytes, sessionId);
			return;
		}

		AuthHandler.getInstance().sendCheckAuthReq(sessionId, uuId,
				sessionKey, areaId);

	}

	public void loginResult(boolean success, UserBean userBean, long sessionId)
	{
		LoginGameMessage message = new LoginGameMessage();

		if (success)
		{
			User old = GlobalUserCache.getInstance().getUserById(
					userBean.getId());
			if (old != null)
				this.doLogout(old);

			User user = new User();
			user.setUserBean(userBean);
			user.setSessionId(sessionId);
			GlobalUserCache.getInstance().putUserMap(user.getId(), user);

			IRoleBeanDao roleBeanDao = DBCS.getExector(IRoleBeanDao.class);

			RoleBean roleBean = roleBeanDao.selectRoleBeanByUserId(
					user.getId(), user.getUserBean().getLastAreaId());
			if (roleBean != null)
				this.roleLogin(user, roleBean);
			else
			{
				GlobalUserCache.getInstance().getNotHasRoleTmpUserMap()
						.put(sessionId, user);

				message.setResult(LoginResult.ROLE_NOT_EXSIT);

				message.setRandomRoleName(NameGenerator.getInstance()
						.randomNameList());
				IGateWayService gateWayService = BOServiceManager
						.findDefaultService(IGateWayService.PORTAL_ID,
								getGatewayEndpointBySessionId(sessionId));
				IoBuffer buffer = message.encodeIoBuffer();
				int byteLength = buffer.limit();
				byte[] tmpMessageBytes = new byte[byteLength];
				buffer.get(tmpMessageBytes, 0, byteLength);
				gateWayService.sendMessage(tmpMessageBytes, sessionId);
				return;
			}

		} else
		{
			message.setResult(LoginResult.AUTH_FAIL);
			IGateWayService gateWayService = BOServiceManager
					.findDefaultService(IGateWayService.PORTAL_ID,
							getGatewayEndpointBySessionId(sessionId));
			IoBuffer buffer = message.encodeIoBuffer();
			int byteLength = buffer.limit();
			byte[] tmpMessageBytes = new byte[byteLength];
			buffer.get(tmpMessageBytes, 0, byteLength);
			gateWayService.sendMessage(tmpMessageBytes, sessionId);
			return;
		}
	}

	private Role roleLogin(User user, RoleBean roleBean)
	{
		LoginGameMessage message = new LoginGameMessage();

		Date time = new Date();
		roleBean.setLastLoginTime(time);

		Role role = new Role(roleBean, user);
		role.init();
		GlobalRoleCache.getInstance().putRoleMap(role.getRoleId(), role);

		// 激活角色所有的服务.一个角色,自己独有一堆服务器.

		IGameGateWayService gateWayService = BOServiceManager.activeService(
				IGameGateWayService.PORTAL_ID, role.getRoleId(),
				getGatewayEndpointBySessionId(user.getSessionId()), false);

		ActiveServiceHolder.activeAllService(role.getRoleId());
		gateWayService.loginNotify(user.getId(), user.getSessionId());

		IItemService itemService = BOServiceManager.findService(
				IItemService.PORTAL_ID, role.getRoleId());
		message.setOutfitMap(itemService.getAllOutfitMap());

		message.setBagItemList(itemService.getBagItemList());

		message.setResult(LoginResult.SUCCESS);
		message.setRole(role);

		this.sendMessage(role.getRoleId(), message);
		return role;
	}

	public void createRole(int spriteId, String roleName, long sessionId)
	{
		if(spriteId<=0){
			throw new RuntimeException("创建角色的精灵Id---客户端传入参数不对!");
		}
		IRoleBeanDao roleBeanDao = DBCS.getExector(IRoleBeanDao.class);
		if (roleBeanDao.selectRoleBeanByName(roleName) != null)
		{
			LoginGameMessage message = new LoginGameMessage();
			message.setResult(LoginResult.ROLE_NAME_EXSIT);
			message.setRandomRoleName(NameGenerator.getInstance()
					.randomNameList());
			
			IGateWayService gateWayService = BOServiceManager
					.findDefaultService(IGateWayService.PORTAL_ID,
							getGatewayEndpointBySessionId(sessionId));
			IoBuffer buffer = message.encodeIoBuffer();
			int byteLength = buffer.limit();
			byte[] tmpMessageBytes = new byte[byteLength];
			buffer.get(tmpMessageBytes, 0, byteLength);
			gateWayService.sendMessage(tmpMessageBytes, sessionId);
			return;
		}
		User user = GlobalUserCache.getInstance().getNotHasRoleTmpUserMap()
				.remove(sessionId);

		RoleBean roleBean = new RoleBean();
		long roleId = IdGenerator.getInstance().getId(IdType.ROLE_ID,
				user.getUserBean().getLastAreaId());
		roleBean.setAreaId(user.getUserBean().getLastAreaId());
		roleBean.setId(roleId);
		roleBean.setName(roleName);
		roleBean.setLevel(1);
		roleBean.setUserId(user.getId());
		roleBean.setCreateTime(new Date());
		roleBean.setEnergy(999999);
		roleBean.setSpriteId(spriteId);
		roleBean.setLastLoginTime(new Date());
		roleBeanDao.insertRoleBean(roleBean);

		this.roleLogin(user, roleBean);
	}

	@Override
	public void doLogout(long roleId)
	{
		logger.debug("roleId:" + roleId + "登出游戏");
		Role role = GlobalRoleCache.getInstance().removeRoleById(roleId);
		if (role == null)
			logger.debug("roleId:" + roleId + "登出游戏失败");
		else
		{
			role.onLeaveGame();
			GlobalUserCache.getInstance()
					.removeUserById(role.getUser().getId());
		}

		BOServiceManager.destroyService(IGateWayService.PORTAL_ID, roleId);
		BOServiceManager.destroyService(IVirtualWorldService.PORTAL_ID, roleId);
		BOServiceManager.destroyService(ITollgateInfoService.PORTAL_ID, roleId);
		BOServiceManager.destroyService(IRoleService.PORTAL_ID, roleId);
		BOServiceManager.destroyService(IItemService.PORTAL_ID, roleId);
		BOServiceManager.destroyService(IItemService.PORTAL_ID, roleId);
	}

	public void doLogout(User user)
	{
		IGateWayService gateWayService = BOServiceManager.findDefaultService(
				IGateWayService.PORTAL_ID,
				getGatewayEndpointBySessionId(user.getSessionId()));
		gateWayService.kick(user.getId(), false);

		if (user.getRole() != null)
			this.doLogout(user.getRole().getRoleId());
		else
			GlobalUserCache.getInstance().removeUserById(user.getId());
	}



}
