package com.pureTec.behavior;

import java.io.File;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.admin.AdminInterceptor;

@Before({ AdminInterceptor.class ,BehaviorInterceptor.class})
public class BehaviorController extends Controller{

	private static String	Excel_PATH_FOLDER	= File.separator + "upload_xls" + File.separator;		// Excel存储路劲
	// 存放临时文件的目录
	private static String	Excel_TEMP_FOLDER	= File.separator + "uploadTemp_xls+File.separator";

	/**
	 * 将para转换behavior对象
	 * 
	 * @return
	 */
	public Behavior paraToBehavior() {
		Behavior behavior = new Behavior();
		behavior.set("uid", getParaToInt("uid"));
		behavior.set("behavior",getPara("behavior"));
		behavior.set("time",getParaToDate("time"));
		behavior.set("parameter",getPara("parameter"));
		return behavior;
	}

	/**
	 * behaviors list
	 */
	public void list() {
		Page<Behavior> Behaviors = Behavior.me.paginate(getParaToInt("pageNumber", 1), 10);
	
		setAttr("data", Behaviors);
		setAttr("pageNumber", Behaviors.getPageNumber());
		setAttr("countPage", Behaviors.getTotalPage());
		renderFreeMarker("behavior_list.html");
	}
	/**
	 * 根据用户过滤列表 select by user
	 */
	public void searchReport(){
		String keyword=getPara("keyword").toString();
		Page<Behavior> Behaviors = Behavior.me.selectByUser(getParaToInt("pageNumber", 1), 10,keyword);
		setAttr("data", Behaviors);
		setAttr("keyword",keyword);
		setAttr("pageNumber", Behaviors.getPageNumber());
		setAttr("countPage", Behaviors.getTotalPage());
		renderFreeMarker("behavior_list.html");
	}
	/**
	 * 根据用户排序
	 */
	public void orderByUser() {
		Page<Behavior> Behaviors = Behavior.me.orderByUser(getParaToInt("pageNumber", 1), 10);
		setAttr("data", Behaviors);
		setAttr("pageNumber", Behaviors.getPageNumber());
		setAttr("countPage", Behaviors.getTotalPage());
		renderFreeMarker("behavior_list.html");
	}
	/**
	 * 根据时间排序
	 */
	public void orderByTime() {
		Page<Behavior> Behaviors = Behavior.me.orderByTime(getParaToInt("pageNumber", 1), 10);
		setAttr("data", Behaviors);
		setAttr("pageNumber", Behaviors.getPageNumber());
		setAttr("countPage", Behaviors.getTotalPage());
		renderFreeMarker("behavior_list.html");
	}
	/**
	 * 根据行为类型排序
	 */
	public void orderByType() {
		Page<Behavior> Behaviors = Behavior.me.orderByType(getParaToInt("pageNumber", 1), 10);
		setAttr("data", Behaviors);
		setAttr("pageNumber", Behaviors.getPageNumber());
		setAttr("countPage", Behaviors.getTotalPage());
		renderFreeMarker("behavior_list.html");
	}	
}
