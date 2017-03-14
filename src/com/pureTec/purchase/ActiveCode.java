package com.pureTec.purchase;

import java.text.SimpleDateFormat;
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
public class ActiveCode extends Model<ActiveCode>{
	public static final ActiveCode me = new ActiveCode();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<ActiveCode> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
//		return paginate(pageNumber, pageSize, "select ac.*,c.name ","from active_code ac ,course c where ac.cid = c.id order by ac.end_date desc");
		return paginate(pageNumber, pageSize, "select ac.*,c.name ","from active_code ac ,course c where ac.cid = c.id order by c.id");
		
		
//		select ac.*,c.name from active_code ac ,course c where ac.cid = c.id;
		
		
	}


	
//	public List<ActiveCode> getActiveCodeList(){
////		return paginate(1,Integer.MAX_VALUE, "SELECT c.name", "from course c").getList();
//	    return find("select c.id,c.code,c.end_date from active_code c");
//	}
//	
	
	
	
	public ArrayList<Object> courseToListData(ActiveCode course) {

		ArrayList<Object> data = new ArrayList<Object>();
		data.add(course.getLong("id"));
		data.add(course.getStr("name"));
		data.add(course.getStr("code"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		String end_date_Str=sdf.format(course.getDate("end_date"));
		
		data.add(end_date_Str);
//		是否有效 0未使用 1人工置无效 3已使用
		switch (course.getInt("active")) {
		case 0:
			data.add("未使用");
			break;
		case 1:
			data.add("人工置无效");
			break;
		case 2:
			data.add("已使用");
			break;
		case 3:
			data.add("用户已激活使用");
			break;
		default:
			break;
		}
		
		

		
		
		
		
		
		return data;

	}
	
	
}


