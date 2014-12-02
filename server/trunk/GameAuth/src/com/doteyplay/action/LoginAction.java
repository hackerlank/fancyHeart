package com.doteyplay.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.doteyplay.manager.AuthManager;
import com.doteyplay.utils.IPUtils;

public class LoginAction extends HttpServlet
{
	/**
	 * @author:Tom.Zheng
	 * @createTime:2014年11月24日 下午7:05:32
	 */
	private static final long serialVersionUID = -5371641951576787920L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		resp.setContentType("text/xml;charset=UTF-8");

		String account = req.getParameter("account");
		String password = req.getParameter("password");

		String result = AuthManager.getInstance().login(account, password,
				IPUtils.getIpAddr(req));
		resp.getWriter().print(result);
	}

}
