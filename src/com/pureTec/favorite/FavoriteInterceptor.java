package com.pureTec.favorite;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class FavoriteInterceptor implements Interceptor {
	public void intercept(Invocation inv) {
		System.out.println("Before invoking " + inv.getActionKey());
		

		String path = inv.getController().getRequest().getContextPath();
		String basePath = inv.getController().getRequest().getScheme() + "://" + inv.getController().getRequest().getServerName() + ":" + inv.getController().getRequest().getServerPort() + path
				+ "/";
		inv.getController().setAttr("path", path);
		inv.getController().setAttr("basePath", basePath);
		inv.getController().setAttr("slide", "#favorite");
		inv.getController().setAttr("slidesecond", "#"+inv.getActionKey().replaceAll("/", "_"));
			inv.invoke();
		System.out.println("After invoking " + inv.getActionKey());
	}
}
