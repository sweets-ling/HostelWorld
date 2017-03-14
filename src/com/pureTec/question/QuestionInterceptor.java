package com.pureTec.question;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 此拦截器仅做为示例展示，在本 demo 中并不需要  拦截器记录日志
 */
public class QuestionInterceptor implements Interceptor {
	
	public void intercept(Invocation inv) {
		System.out.println("Before invoking " + inv.getActionKey());
		

		String path = inv.getController().getRequest().getContextPath();
		String basePath = inv.getController().getRequest().getScheme() + "://" + inv.getController().getRequest().getServerName() + ":" + inv.getController().getRequest().getServerPort() + path
				+ "/";
		inv.getController().setAttr("path", path);
		inv.getController().setAttr("basePath", basePath);
		inv.getController().setAttr("slide", "#question");
		inv.getController().setAttr("slidesecond", "#"+inv.getActionKey().replaceAll("/", "_"));
		try {
			inv.invoke();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		
		System.out.println("After invoking " + inv.getActionKey());
	}
}
