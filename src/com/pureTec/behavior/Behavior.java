package com.pureTec.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.ad.Ad;
import com.pureTec.course.Course;
@SuppressWarnings("serial")
public class Behavior extends Model<Behavior>{
	public static final Behavior me = new Behavior();
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Behavior> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, "SELECT u.lgnname,t.bname,b.time,b.describe", "from behavior b,behavior_type t,user u where t.id=b.bid and b.uid=u.id order by b.time desc");
	}

	
	public Page<Behavior> orderByUser(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize,"SELECT u.lgnname,t.bname,b.time,b.describe", "from behavior b,behavior_type t,user u where t.id=b.bid and b.uid=u.id order by u.lgnname" );
	}
	
	public Page<Behavior> orderByTime(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "SELECT u.lgnname,t.bname,b.time,b.describe", "from behavior b,behavior_type t,user u where t.id=b.bid and b.uid=u.id order by b.time DESC");
	}

	public Page<Behavior> orderByType(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "SELECT u.lgnname,t.bname,b.time,b.describe", "from behavior b,behavior_type t,user u where t.id=b.bid and b.uid=u.id order by t.bname");
	}
	
	public Page<Behavior> selectByUser(int pageNumber, int pageSize,String keyword) {
		return paginate(pageNumber, pageSize, "SELECT u.lgnname,t.bname,b.time,b.describe", "from behavior b,behavior_type t,user u where t.id=b.bid and b.uid=u.id and u.lgnname like '%" + keyword + "%'");
	}
/*
	public ArrayList<Object> behaviorToListData(Behavior behavior) {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(behavior.getLong("uid"));
		data.add(behavior.getStr("behavior"));
		data.add(behavior.getDate("time"));
		data.add(behavior.getStr("parameter"));
		return data;
	}
	*/
	public long getBehaviorCount(String date,int type){
		return findFirst("select count(*) as count from behavior where bid=? and time like '"+date+"%'",new Object[]{type}).getLong("count");
	}
}
