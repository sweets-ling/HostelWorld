package com.pureTec.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.purchase.Purchase;
import com.pureTec.tool.MyFormater;

@SuppressWarnings("serial")
public class Course extends Model<Course> {
	public static final Course me = new Course();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Course> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, "SELECT c.*", "from course c");
	}
	public Page<Course> getLastpaginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, "SELECT d.* ,aa.aaa146", "from course d  ,aa26 aa where  d.country_id=aa.aac031 order by add_date desc");
	}

	public Page<Course> paginateBysql(int pageNumber, int pageSize, String sql1, String sql2) {
		// return paginate(pageNumber, pageSize, "select *", "from Course order by id asc");
		// SELECT d.* ,aa.aaa146 from Course d ,aa26 aa where d.country_id=aa.aac031;
		return paginate(pageNumber, pageSize, sql1, sql2);
	}

	
	public Long getAllCourseCount() {

		Course Course = me.findFirst("SELECT COUNT(id) from Course ");

		return Course.getLong("COUNT(id)");
	}
	
	public Course findbyid(int id) {

		Course course = me.findFirst("SELECT c.*  from course c   where c.id = ? ",
				new Object[] { id });

		return course;
	}
	/**
	 * 根据Id查找Cid B0001
	 * @param id
	 * @return
	 */
	public String findCIDbyid(int id) {
		
		Course course = me.findFirst("SELECT c.cid  from course c   where c.id = ? ",
				new Object[] { id });
		
		return course.getStr("cid");
	}

	public int findIdbyName(String name) {
		
		Course course = me.findFirst("SELECT c.id  from course c   where c.name = ?",
				new Object[] { name});
		
		return course.getInt("id");
	}
	
	
	/**
	 * 形成压缩包后 更新最新压缩的时间
	 */
	public void updateZipTime(int id,String size){
		Course c = me.findbyid(id);
		String version = c.getStr("version");
		String newVersion = MyFormater.floatFormate(Float.parseFloat(version)+(float)0.1);
		
		
		me.set("id", id);
		me.set("size", size);
		me.set("version", newVersion);
		me.set("zip_time", new Date());
		me.update();
		
	}
	

	public Map<String, Object> courseToMapData(Course course,String path) {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", course.getInt("id")); // id he imei 之间等同
		data.put("cid", course.getStr("cid"));
		data.put("name", course.getStr("name"));
		data.put("csize", course.getStr("size"));
		data.put("price", course.getInt("price").toString());
		
		String version = course.getStr("version");
		String newVersion = MyFormater.floatFormate(Float.parseFloat(version)+(float)0.1);
		
		
		data.put("version", newVersion); // size 和 version 要提前一个 每次导出会加）0。1 导致version之后0.1 所以要提前加0.1
		data.put("status",course.getInt("status").toString());
		data.put("describe", course.getStr("describe"));
		
		data.put("img1name", course.getStr("img1"));
		data.put("img2name", course.getStr("img2"));
		data.put("img3name", course.getStr("img3"));
		
		data.put("img1", path+course.getStr("img1"));
		data.put("img2", path+course.getStr("img2"));
		data.put("img3", path+course.getStr("img3"));
		return data;

	}

	public ArrayList<Object> courseToListData(Course course) {

		ArrayList<Object> data = new ArrayList<Object>();

		data.add(course.getInt("id"));
		data.add(course.getStr("cid"));
		data.add(course.getStr("name"));
		data.add(course.getStr("size"));
		data.add(course.getInt("price"));
//		data.add(course.getStr("describe"));

		return data;

	}
	public ArrayList<Object> courseFileToListData(Course course) {
		
		ArrayList<Object> data = new ArrayList<Object>();
		
		data.add(course.getInt("id"));
		data.add(course.getStr("cid"));
		data.add(course.getStr("name"));
		data.add(course.getStr("size"));
		
		data.add(course.getLong("download"));//总计下载量
		data.add(course.getLong("sales"));//总计销售量 
		data.add(course.getStr("version"));//最新版本号
		data.add(course.getDate("zip_time"));//最新版本号
		
		java.util.Date zipTime = course.getDate("zip_time");
		java.util.Date updateTime = course.getDate("update_time");
		String canUpate = zipTime.getTime() > updateTime.getTime()?"否":"是";
		
		
		data.add(canUpate);//是否有新版本
//		data.add("是");//是否有新版本

		
		return data;
		
	}
	public ArrayList<Object> courseToListDataForSearch(Course Course) {
		
		ArrayList<Object> data = new ArrayList<Object>();
		
//		data.add(Course.getStr("id"));
//		data.add(Course.getStr("aaa146"));
//		data.add(Course.getInt("sell_status"));
//		String status= CourseData.me.findLastRunStatusbyid(Course.getStr("id"));//获取设备的最新验证码   状态，从CourseDataList里面获取
//		data.add(status);
//		data.add(Course.getDate("product_date"));
		
		return data;
		
	}

	
	public List<Course> getCourseList(){
//		return paginate(1,Integer.MAX_VALUE, "SELECT c.name", "from course c").getList();
	
		
	    return find("select c.id,c.name from course c");
	
	
	}

}
