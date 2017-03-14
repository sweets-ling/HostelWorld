package com.pureTec.about;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.ad.Ad;

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
public class About extends Model<About>{
	public static final About me = new About();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<About> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, "SELECT ad.*", "from ad");
	}

	/**
	 * get about by isActive
	 */
	public About findActive() {

		About about = me.findFirst("SELECT *  from about where about.isActive=1");
		return about;
	}

	/**
	 * 设置为失效
	 * @param location
	 */
	public void setNoActive(String location)
	{
		List<Record> users = Db.find("SELECT *  from about");
		Db.update("UPDATE about set isActive=0 where location='"+location+"'");
	}
	
	public About findbyLoc(String location) {

		About about = me.findFirst("SELECT *  from about where isActive=1 and location = ? ",
				new Object[] { location });

		return about;
	}

}


