package com.pureTec.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.admin.AdminInterceptor;
import com.pureTec.common.SessionManager;
import com.pureTec.question.QuestionInterceptor;
import com.pureTec.question.QuestionNode;
import com.pureTec.tool.MyFormater;

/**
 * BlogController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */


@Before({AdminInterceptor.class, UserInterceptor.class} )
public class UserController extends Controller {
	

	public void changeState()
	{
		String id =  getPara("id").toString();
		int valid = 1-Integer.parseInt(getPara("state").toString());
		
		//删除数据库token
		User.me.updateValid(id,valid);
		//删除内存token
		if(valid==0)
		  AppService.deletetoken(Integer.valueOf(id));
		
		renderJson("result",new String[]{"OK",getPara("state").toString(),id});
	}
	
	
     ////获取问题列表
	public void userListType0()
	{
	userList(0);
	}
	
	////获取问题列表
	public void userListType1()
	{
	userList(1);
		
		
		
	
	}
	////获取问题列表
	public void userList(int type)
	{
	
//		int type = getParaToInt("type", 1);		
		
		String keyword="";
		Page<User> userList=null;
		if(getPara("keyword")!=null)
		{
			keyword=getPara("keyword");
			userList = User.me.search(keyword,type);	
		}
		
		else
		{
			int pageNumber = getParaToInt("pageNumber", 1);		
			userList = User.me.paginate(pageNumber, 8,type);	
		}
		String state[]={"封杀","正常"};
		String str= (type==1?"正常用户列表":"封杀用户列表");
		setAttr("title",str);
		setAttr("type",type);
		setAttr("state",state);
		setAttr("userList", userList);
		setAttr("pageNumber", userList.getPageNumber());
		setAttr("countPage", userList.getTotalPage());
		setAttr("pageSize", 8);
		setAttr("keyword", keyword);
		render("userList.html");
	}
	
	
	/*
	 * 
	 * 去除token检查
	 * 
	 */
	////获取空闲在线客服
	@Clear({AdminInterceptor.class, UserInterceptor.class} )
	public void getCSNum()
	{
		int transtype=getParaToInt("type"); //0 老挝  1 泰语
		
		int id=SessionManager.calcCSNum(transtype);
		
		
		this.renderJson(id);
		
		
		
		
	}
	
	
	
	
	
////获取某一问题信息 
	public void userdetail()
	{
		int id = Integer.parseInt(getPara("id").toString());
		User qst=User.me.findById(id);
		setAttr("node", qst);
		renderFreeMarker("userdetail.html");
	}
	


	

}
