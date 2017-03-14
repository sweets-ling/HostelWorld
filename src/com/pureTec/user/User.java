package com.pureTec.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 */
@SuppressWarnings("serial")
public class User extends Model<User> {
	public static final User me = new User();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<User> paginate(int pageNumber, int pageSize,int valid) {
		return paginate(pageNumber, pageSize, "SELECT * ", "from user  where isValid="+valid);

	}
	
	
	public void updateValid(String id ,int valid)
	{
		Record user = Db.findById("user", id).set("isValid", valid);
		if(valid==0)
		{
			MCToken.me.findFirst("select * from token  where mcid = ?",new Object[] { id}).set("token", "").update();//  ("update token set token='' where mcid="+id);
		}	
		
		Db.update("user", user); 
	}

	
	public User getUserByName(String name){
		User user=me.findFirst("SELECT u.*  from user u   where u.lgnname = ?",
						new Object[] { name});
				
				return user;
	}
	public User getUserById(int id){
		User user=me.findFirst("SELECT u.*  from user u   where u.id = ?",
						new Object[] {id});
				
				return user;
	}


	public Page<User> search(String keyword, int type) {
		// TODO Auto-generated method stub
		return paginate(1, 20, "SELECT * ", "from user  where  isValid="+type+" and lgnname  like '%"+keyword.trim()+"%'");
	}

}
