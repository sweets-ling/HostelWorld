package com.pureTec.favorite;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.ad.Ad;

import com.pureTec.course.Course;
@SuppressWarnings("serial")
public class Favorite extends Model<Favorite>{
	public static final Favorite me = new Favorite();
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Favorite> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, "SELECT u.lgnname as uname,f.add_time,c.chinese as word", "from favorite f,course_data c,user u where f.state=1 and f.uid=u.id and f.word_id=c.id order by f.add_time desc");
	}
	public Favorite findByUidAndWordid(int uid,int word_id){
		Favorite favorite=findFirst("select * from favorite where uid=? and word_id=?",new Object[]{uid,word_id});
		return favorite;
	}

}
