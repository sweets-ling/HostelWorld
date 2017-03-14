package com.pureTec.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * Blog model.
 * 
 * 将表结构放在此，消除记忆负担 mysql> desc blog; +---------+--------------+------+-----+---------+----------------+ | Field | Type | Null | Key | Default | Extra |
 * +---------+--------------+------+-----+---------+----------------+ | id | int(11) | NO | PRI | NULL | auto_increment | | title | varchar(200) | NO
 * | | NULL | | | content | mediumtext | NO | | NULL | | +---------+--------------+------+-----+---------+----------------+
 * 
 * 数据库字段名建议使用驼峰命名规则，便于与 java 代码保持一致，如字段名： userId
 */
@SuppressWarnings("serial")
public class Admin extends Model<Admin> {
	public static final Admin me = new Admin();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Admin> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "SELECT * ", "from admin ");

	}
	
	


	public void update(Admin admin) {
		// TODO Auto-generated method stub
		
		
		Record ad = Db.findById("admin", admin.getInt("id")).set("password", admin.get("password")).set("type", admin.getInt("type"));
		Db.update("admin",ad);
		
	}




	public boolean existName(String name) {
		// TODO Auto-generated method stub
		
		if (Db.queryLong("select count(*) from admin where lgnname='"+name+"'")>0)
			return true;	
		return false;
	}

	public List<Record> getOperators(){
		return Db.find("select admin.lgnname from admin");
	}

	public int findIdbyName(String name){
		Admin admin=me.findFirst("SELECT a.*  from admin a where a.lgnname = ?",
				new Object[] { name});
		return admin.getInt("id");
	}

	public Admin getMyInfo(int id){
		Admin admin=me.findFirst("SELECT a.*  from admin a where a.id = ?",
				new Object[] {id});
		return admin;
	}


	
}
