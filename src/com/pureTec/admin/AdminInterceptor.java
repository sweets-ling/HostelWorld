package com.pureTec.admin;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 此拦截器仅做为示例展示，在本 demo 中并不需要  拦截器记录日志
 */
public class AdminInterceptor implements Interceptor {
	
	public void intercept(Invocation inv) {
		System.out.println("Before invoking " + inv.getActionKey());
		

		String path = inv.getController().getRequest().getContextPath();
		String basePath = inv.getController().getRequest().getScheme() + "://" + inv.getController().getRequest().getServerName() + ":" + inv.getController().getRequest().getServerPort() + path
				+ "/";
		inv.getController().setAttr("path", path);
		inv.getController().setAttr("basePath", basePath);
		System.out.println(basePath);
		inv.getController().setAttr("slide", "#admin");
		inv.getController().setAttr("slidesecond", "#"+inv.getActionKey().replaceAll("/", "_"));
	
		String name=inv.getController().getSessionAttr("userName");
		
		if(name==null)
		{
			
			inv.getController().redirect("/");
		}
		else
		{
			int id=inv.getController().getSessionAttr("userId");
			int type=inv.getController().getSessionAttr("userType");
			inv.getController().setAttr("userType",type);
			inv.getController().setAttr("userName",name);
			inv.getController().setAttr("userId",id);
			try {
				inv.invoke();

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
			}
		}
		
		
		
		System.out.println("After invoking " + inv.getActionKey());
	}
}
