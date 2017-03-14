package com.pureTec.admin;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.common.SessionManager;

/**
 * BlogController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */


//@Before(AdminInterceptor.class)
public class AdminController extends Controller {
	
	
	
	
	public void index()
	{
		render("login.html");
	}
	
	
	
	//@Before(AdminValidator.class)
	public void login() {

	


		String phone = getPara("name");
		String key = getPara("key");


		////
		Admin admin =Admin.me.findFirst("select * from admin  where lgnname = ? and password = ?", new Object[] { phone, key });
		
		if(admin==null)
		{
			//登录失败
			redirect("/");
		}
		else
		{
			////登录成功
			
			///放置session
			setSessionAttr("userId",admin.getInt("id"));
			setSessionAttr("userType",admin.getInt("type"));
			setSessionAttr("userName",admin.getStr("lgnname"));
			setSessionAttr("transType",admin.getInt("transType"));
			
		

			
			
			setAttr("userType",admin.getInt("type"));
			setAttr("userName",admin.getStr("lgnname"));
			setAttr("userId",admin.getInt("id"));
			
			
			if(admin.getInt("type")==1){//客服人员   加入到在线客服列表
				SessionManager.addCustomServer(admin.getInt("id"),admin.getInt("transType"));
			
				redirect("/cs/answerListType0");			//客服只有回答问题的权限 有单独的菜单
                return;
			}
			
			redirect("/homepage/index");
				
			
		}

	}
	
	
	@Before({ AdminInterceptor.class })
	public void adminList()
	{
	
			int pageSize = 10;
			try{
				pageSize = getPara("size")==null?10:Integer.parseInt(getAttr("size").toString());
			}catch(Exception e){
				pageSize = 10;
			}
			
			
			int pageNumber = 1;
			try{
			pageNumber = getPara("page")==null?1:Integer.parseInt(getAttr("page").toString());
			}catch(Exception e){
				pageNumber = 1;
			}
			
				
			Page<Admin> adminList =Admin.me.paginate(pageNumber, pageSize);
		
			setAttr("adminList", adminList);
			renderFreeMarker("adminList.html");
		
		
		
	}
	
	@Before({ AdminInterceptor.class })
	public void update()
	{
	
		String id=getPara("id");
		

		String pwd=getPara("pwd");
		int type=getParaToInt("type");
		
		int transType=getParaToInt("transType");
		
		Admin admin=new Admin();
		admin.set("id", Integer.valueOf(id));
		admin.set("password", pwd);
		admin.set("type", type);
		admin.set("transType",transType);
		
		
		Admin.me.update(admin);
		String flag= "修改成功";
		renderText(flag);
		
		
	}
	
	
	
	@Before({ AdminInterceptor.class })
	public void add()
	{
		String name = getPara("name");
		String pwd=getPara("pwd");
		int type=getParaToInt("type");
		int transType=getParaToInt("transType");
		
		Admin admin=new Admin();
		admin.set("lgnname", name);
		admin.set("password", pwd);
		admin.set("type",type);
		admin.set("transType",transType);
		
		String flag= "保存失败";
		if(Admin.me.existName(name)) ///重名
		{
		   flag="重名";
		}
		else
		{
			admin.save();
			flag= "保存成功";
		}
		
		renderText(flag);
		
	}
	
	@Before({ AdminInterceptor.class })
	public void delete()
	{
		int id = getParaToInt("id");
		//删除自身 不可行
		if(id!=(int)getSessionAttr("userId"))
		{
		
			Admin.me.deleteById(id);
			
			
		}
		redirect("/admin/adminList");
		
	}
	
	
//	@Before({ AdminInterceptor.class })
	public void logout()
	{
		///放置session
		SessionManager.delCustomServer((Integer)getSessionAttr("userId"), (Integer)getSessionAttr("transType"));
		removeSessionAttr("userId");
		removeSessionAttr("userType");
		removeSessionAttr("userName");
		removeSessionAttr("transType");
		
		
		
		
		redirect("/");
		
	}
	
	
	@Before({ AdminInterceptor.class })
	public void getById()
	{
		
		String id = getPara("id");
		if(id!=null)
		{
			Admin admin=Admin.me.findById(Integer.valueOf(id));
			setAttr("node", admin);
		} 	
		renderFreeMarker("admin.html");
	
	}
	@Before({ AdminInterceptor.class })
	public void showMyInfo(){
		  int userId=getSessionAttr("userId");
		  Admin admin=Admin.me.getMyInfo(userId);
			setAttr("name", admin.getStr("lgnname"));
			setAttr("credit", admin.getLong("aswNum"));
			renderFreeMarker("myinfo.html");
	}
}
