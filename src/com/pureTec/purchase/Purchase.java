package com.pureTec.purchase;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.course.Course;

/**
 * Blog model.
 * 
 * 将表结构放在此，消除记忆负担 mysql> desc blog; +---------+--------------+------+-----+---------+----------------+ | Field | Type | Null | Key | Default | Extra |
 * +---------+--------------+------+-----+---------+----------------+ | id | int(11) | NO | PRI | NULL | auto_increment | | title | varchar(200) | NO
 * | | NULL | | | content | mediumtext | NO | | NULL | | +---------+--------------+------+-----+---------+----------------+
 * 
 * 数据库字段名建议使用驼峰命名规则，便于与 java 代码保持一致，如字段名： userId
 * 
 * model与数据库表一一映射
 */
@SuppressWarnings("serial")
public class Purchase extends Model<Purchase>{
	public static final Purchase me = new Purchase();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Purchase> paginate(int pageNumber, int pageSize) {

//		return paginate(pageNumber, pageSize, "SELECT p.name,p.time,p.payid,p.type,c.name as course,a.lgnname as operator,p.isActive ", "from purchase p,course c,admin a where p.id=c.id and a.id=p.operator order by p.time desc");
		return paginate(pageNumber, pageSize, "SELECT p.name,p.time,p.payid,p.type,p.channel,p.description,c.name as course,a.lgnname as operator,p.isActive ","from purchase p LEFT JOIN course c  ON p.id=c.id  LEFT JOIN admin a ON a.id=p.operator");

	}


	
	public Page<Purchase> searchByUser(int pageNumber, int pageSize,String user) {
		return paginate(pageNumber, pageSize, "SELECT p.name,p.time,p.payid,p.type,c.name as course,a.lgnname as operator,p.isActive ", "from purchase p,course c,admin a where p.id=c.id and a.id=p.operator and p.name like '%" + user + "%'");
	}
	
	public void remove(String user,String course){
		Db.update("UPDATE purchase set isActive=0 where name='"+user+"' and id='"+Course.me.findIdbyName(course)+"'");
	}
	public void recover(String user,String course){
		Db.update("UPDATE purchase set isActive=1 where name='"+user+"' and id='"+Course.me.findIdbyName(course)+"'");
	}

	
	
	public Page<Purchase> paginateBysql(int pageNumber, int pageSize, String sql) {
		String presqlSelect = "SELECT p.name,p.time,p.payid,p.type,c.name as course,a.lgnname as operator,p.isActive ";
//		String presqlContent = "from purchase p,course c,admin a where p.id=c.id and a.id=p.operator ";
		String presqlContent = "from purchase p,course c,admin a where p.id=c.id ";
		System.out.println(presqlSelect+presqlContent + sql);
		return paginate(pageNumber, pageSize, presqlSelect, presqlContent + sql);

	}
}


