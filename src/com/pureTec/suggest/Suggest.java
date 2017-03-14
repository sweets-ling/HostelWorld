package com.pureTec.suggest;

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
public class Suggest extends Model<Suggest>{
	public static final Suggest me = new Suggest();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	
	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return 消息列表
	 */
	public Page<Suggest> paginate(int pageNumber, int pageSize) {

		return paginate(pageNumber, pageSize, "SELECT s.* ", "from suggestion s order by s.time desc");

	}


}


