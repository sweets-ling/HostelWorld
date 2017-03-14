package com.pureTec.ad;

import java.util.ArrayList;
import java.util.List;

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
 * 
 * model与数据库表一一映射
 */
@SuppressWarnings("serial")
public class Ad extends Model<Ad>{
	public static final Ad me = new Ad();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Ad> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, "SELECT ad.*", "from ad");
	}

	/**
	 * get ads by location
	 * @param location
	 * @return Ad
	 */
	public Ad findbyLoc(String location) {

		Ad ad = me.findFirst("SELECT *  from ad where ad.is_active=1 and ad.location = ? ",
				new Object[] { location });

		return ad;
	}

}


