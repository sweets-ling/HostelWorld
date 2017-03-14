package com.pureTec.purchase;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.admin.Admin;
import com.pureTec.admin.AdminInterceptor;
import com.pureTec.course.Course;
import com.pureTec.course.CourseInterceptor;
import com.pureTec.tool.RandomStr;
import com.pureTec.user.User;

@Before({ AdminInterceptor.class })
public class PurchaseController extends Controller {
	/**
	 * 将para转换为购买的对象
	 * 
	 * @return
	 */
	public Purchase paraToPurchase() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int userId = getSessionAttr("userId");
		Purchase newPurchase = new Purchase();
		newPurchase.set("id", Course.me.findIdbyName(getPara("course")));
		newPurchase.set("name", getPara("uname"));
		newPurchase.set("time", new Date());
		newPurchase.set("payid", getPara("payid"));
		newPurchase.set("operator", userId);
		return newPurchase;
	}

	/*
	 * 
	 * 增加用户购买记录的页面映射
	 */
	public void add() {
		List<Course> list = Course.me.getCourseList();
		setAttr("data", list);
		renderFreeMarker("add_purchase.html");
	}
	/**
	 * 用户购买记录的列表
	 */
	public void list() {
		Page<Purchase> list = Purchase.me.paginate(getParaToInt("pageNumber", 1), 10);
		List<Record> operators = Admin.me.getOperators();
		setAttr("data", list);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		setAttr("operators", operators);
		setAttr("operator", "");
		renderFreeMarker("list_purchase.html");
	}

	/*
	 * 提交新增记录返回列表界面
	 */
	public void addPurchase() {
		paraToPurchase().save();
		redirect("/purchase/list");
	}
	/**
	 * 根据用户过滤列表
	 */
	public void selectByUser() {
		String user = getPara("keyword").toString();
		Page<Purchase> list = Purchase.me.searchByUser(getParaToInt("pageNumber", 1), 10, user);
		setAttr("data", list);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		renderFreeMarker("list_purchase.html");
	}

	/**
	 * 校验 user那么是否存在
	 */
	public void validatorUserName() {
		// TODO Auto-generated method stub
		String userName = getPara("uname"); // disable 提交的时候不带这个数据 用readonly
		User user = User.me.getUserByName(userName);
		if (user == null) {// 可以注册
			Map<String, Object> data = new HashMap<>();
			data.put("valid", false);
			renderJson(data); // 这里是 renderJson("true"); 的标准
			System.out.println("false");
		} else { // 不可以注册
			Map<String, Object> data = new HashMap<>();
			data.put("valid", true);
			renderJson(data); // 这里是 renderJson("true"); 的标准
			System.out.println("true");
		}
	}
	/**
	 * 将一条记录设置为失效
	 */
	public void remove() {
		String user = getPara("user");
		String course = getPara("course");
		try {
			course = new String(course.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Purchase.me.remove(user, course);
		Page<Purchase> list = Purchase.me.paginate(getParaToInt("pageNumber", 1), 10);
		setAttr("data", list);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		renderFreeMarker("list_purchase.html");
	}
	/**
	 * 恢复一条记录（取消失效）
	 */
	public void recover() {
		String user = getPara("user");
		String course = getPara("course");
		try {
			course = new String(course.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Purchase.me.recover(user, course);
		Page<Purchase> list = Purchase.me.paginate(getParaToInt("pageNumber", 1), 10);
		setAttr("data", list);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		renderFreeMarker("list_purchase.html");
	}

	
	/**
	 * 根据筛选条件过滤列表
	 */
	public void searchReport() {
		String name = getPara("name");
		String operator = getPara("operator");
		String startDate = getPara("start_date", "");
		String endDate = getPara("end_date", "");
		StringBuffer sql = new StringBuffer();
		// sql.append(" and sr.finished =1 ");
		if (name != null && !name.equals("")) {
			sql.append("and p.name=");
			sql.append("'");
			sql.append(name);
			sql.append("'");
		}
		if (operator != null && !operator.equals("") && (!operator.equals("--请选择--"))) {
			int operatorId = Admin.me.findIdbyName(operator);
			sql.append(" and p.operator=");
			sql.append(operatorId);
		}
		// sr.servicedate >= '2015-09-01' and sr.servicedate <= '2015-09-15'
		if (startDate != null && !startDate.equals("")) {
			sql.append(" and p.time >=");
			sql.append("'");
			sql.append(startDate);
			sql.append("'");
		}
		if (endDate != null && !endDate.equals("")) {
			sql.append(" and p.time<=");
			sql.append("'");
			sql.append(endDate);
			sql.append("'");
		}
		sql.append(" order by p.time desc");
		Page<Purchase> list = Purchase.me.paginateBysql(getParaToInt("pageNumber", 1), 10, sql.toString());
		
		setAttr("data", list);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		setAttr("name", name);
		setAttr("operator", operator);
		setAttr("start_date", startDate);
		setAttr("end_date", endDate);
		List<Record> operators1 = Admin.me.getOperators();
		setAttr("operators", operators1);
		renderFreeMarker("list_purchase.html");
	}

	public void genActiceCodeIni() {
		List<Course> list = Course.me.getCourseList();
		setAttr("data", list);
		renderFreeMarker("add_code.html");
	}

	public void genActiceCode() {
		String cid = getPara("course");
		int codenum = getParaToInt("codenum");
		String end_date_value = getPara("end_date");
		
		if(getPara("end_date")==null ||getPara("end_date").equals("")){ // 时间不能为空 
			
			System.out.println("getPara(end_date)"+getPara("end_date"));
			redirect("/purchase/codeList");
            return;
		}
		
//		System.out.println(cid);
//		System.out.println(codenum);
//		System.out.println(end_date_value);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

		for (int i = 0; i < codenum; i++) {
			String code = RandomStr.getRandomString(50);
			ActiveCode acode = new ActiveCode();
			acode.set("cid", cid);
			acode.set("code", code);
			try {
				acode.set("end_date", sdf.parse(end_date_value));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			acode.save();
		}
		
		
		redirect("/purchase/codeList");
	
	
	
	
	}
	
	
	
	
	
	
	/**
	 * 所有激活码
	 */
	public void codeList(){
		
		Page<ActiveCode>  list = ActiveCode.me.paginate(getParaToInt("pageNumber", 1), 30);
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		List<ActiveCode> activeCodess = list.getList();
		
		for (ActiveCode activeCode : activeCodess) {
			rows.add(ActiveCode.me.courseToListData(activeCode));
		}
		
		setAttr("rows", rows);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		renderFreeMarker("list_code.html");
		
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
