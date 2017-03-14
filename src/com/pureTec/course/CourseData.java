package com.pureTec.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class CourseData extends Model<CourseData> {
	public static final CourseData	me	= new CourseData();

	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<CourseData> paginate(int pageNumber, int pageSize) {
		// return paginate(pageNumber, pageSize, "select *", "from device_data order by id asc");
		// return paginate(pageNumber, pageSize, "select *", "from device_data order by id asc");
		// return paginate(pageNumber, pageSize, "SELECT device_data.* ,device.id",
		// " from device ,device_data where device.id = device_data.id ORDER BY device_data.time desc");
		return paginate(pageNumber, pageSize, "SELECT da.*  ,aa.aaa146",
				"from device d ,device_data da ,aa26 aa where d.id = da.d_id and d.country_id=aa.aac031 ORDER BY da.time desc");
	}

	public Page<CourseData> findListbyid(int pageNumber, int pageSize, String divice_id) {
		return paginate(
				pageNumber,
				pageSize,
				"SELECT da.*  ,aa.aaa146",
				"from device d ,device_data da ,aa26 aa where d.id = da.d_id and d.country_id=aa.aac031 and d.id = ? ORDER BY da.time desc",
				new Object[] { divice_id });
	}

	public List<CourseData> findCharpter(int cid) {
		List<CourseData> cds = me.find("select * from course_data where cid = ? and kind = 1", new Object[] { cid });
		return cds;
	}
	
	/**
	 * 获取chapter对象
	 * @param chapterid
	 * @return
	 */
	public CourseData findCharpterCD(int cid, int chapter) {
		CourseData cd = me.findFirst("select * from course_data where cid = ? and chapter = ? and kind = 1",new Object[] { cid, chapter });
		return cd;
	}
	
	public CourseData findSectionCD(int cid, int chapter,int section) {
		CourseData cd = me.findFirst("select * from course_data where cid = ? and chapter = ? and section = ? and kind = 2",new Object[] { cid, chapter,section});
		return cd;
	}
	
	
	/**
	 * 目录
	 * @param cid
	 * @return
	 */
	public List<CourseData> findCata(int cid) {
		
		List<CourseData> cds = me.find("select * from course_data  c where c.cid = ? and (c.kind = 2 or c.kind = 1)  ORDER BY c.chapter,c.section,c.order", new Object[] { cid });
		return cds;
//		ORDER BY c.chapter,c.section,c.order
		
		//select * from course_data  c where c.cid = 1 and c.kind = 2  ORDER BY c.chapter,c.order;
		
	}
	
	/**
	 * 当前节的所有内容
	 * @param cid
	 * @return
	 */
	public List<CourseData> sourceSection(int cid,int chid,int sid) {
		
		List<CourseData> words = me.find("select * from course_data  c where c.cid = ? and c.chapter = ? and c.section = ? and c.kind = 3  ORDER BY c.chapter,c.order", new Object[] { cid,chid,sid });
		return words;
		
//		select * from course_data  c where c.cid = ? and c.chapter = ? and c.section = ? and c.kind = 3  ORDER BY c.chapter,c.order
	}
	/**
	 * 所有的内容
	 * @param cid
	 * @return
	 */
	public List<CourseData> sourceWordAll(int cid) {
		
		List<CourseData> words = me.find("select * from course_data  c where c.cid = ? and c.kind = 3  ORDER BY c.chapter,c.order", new Object[] { cid });
		return words;
		
//		select * from course_data  c where c.cid = ? and c.chapter = ? and c.section = ? and c.kind = 3  ORDER BY c.chapter,c.order
	}

	public List<CourseData> findSection(int cid, int chapter) {
		List<CourseData> cds = me.find("select * from course_data where cid = ? and chapter = ? and kind = 2",
				new Object[] { cid, chapter });
		return cds;
	}

	public List<CourseData> findWords(int cid, int chapter, int section) {
		List<CourseData> cds = me.find(
				"select * from course_data where cid = ? and chapter = ? and section = ? and kind = 3", new Object[] {
						cid, chapter, section });
		return cds;
	}

	public Map<String, Object> deviceToMapData(CourseData device) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("imei", device.getLong("id"));
		// data.put("imei",getStr("aaa146"));
		data.put("d_id", device.getStr("d_id"));
		data.put("v0", device.getFloat("v0"));
		data.put("a0", device.getFloat("a0"));
		data.put("v1", device.getFloat("v1"));
		data.put("v2", device.getFloat("v2"));
		data.put("v3", device.getFloat("v3"));
		data.put("v4", device.getFloat("v4"));
		data.put("v5", device.getFloat("v5"));
		data.put("a1", device.getFloat("a1"));
		data.put("a2", device.getFloat("a2"));
		data.put("a3", device.getFloat("a3"));
		data.put("a4", device.getFloat("a4"));
		data.put("a5", device.getFloat("a5"));
		data.put("t1", device.getInt("t1"));
		data.put("t2", device.getInt("t2"));
		data.put("longitude", device.getDouble("longitude"));
		data.put("latitude", device.getDouble("latitude"));
		data.put("time", device.getDate("time"));
		return data;
	}

	public ArrayList<Object> courseToListData(CourseData cd) {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(cd.getLong("id"));
		data.add(cd.getInt("cid"));
		data.add(cd.getInt("chapter"));
		data.add(cd.getInt("section")); 
		data.add(cd.getStr("name"));
		data.add(cd.getStr("chinese"));
//		data.add(soureDirName+"/"+cd.getStr("audio")+".mp3");

//		<td><audio src="http://localhost:8080/MinLanWeb//test1.mp3" controls="controls"></audio> </td>   
		
		
		
		return data;
	}

	public ArrayList<Object> courseToListDataForGenExcelFile(CourseData cd) {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(cd.getLong("id"));
		data.add(cd.getInt("order"));
		data.add(cd.getInt("chapter"));
		data.add(cd.getStr("name"));
		data.add(cd.getStr("chinese"));
		return data;
	}

	public ArrayList<Object> courseToListDataForGenExcelFileForWords(CourseData cd) {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(cd.getLong("id"));
		data.add(cd.getInt("order"));
		data.add(cd.getStr("chinese"));
		data.add(cd.getStr("chinese_say"));
		data.add(cd.getStr("foreign"));
		data.add(cd.getStr("audio"));
		data.add(cd.getStr("img1"));
		data.add(cd.getStr("img2"));
		data.add(cd.getStr("describe"));
		return data;
	}

	public ArrayList<Object> toListDataWord(CourseData cd,String soureDirName) {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(cd.getLong("id"));
		data.add(cd.getInt("cid"));
		data.add(cd.getInt("chapter"));
		data.add(cd.getInt("section"));
		// data.add(cd.getStr("name"));
		data.add(cd.getStr("chinese"));
		data.add(cd.getStr("chinese_say"));
		data.add(cd.getStr("foreign"));
//		data.add(cd.getStr("audio"));
		data.add("file_source/"+soureDirName+"/source/audio/"+cd.getStr("audio")+".mp3");
//		MinLanWeb\file_souce\B001\audio
//		<td><audio src="http://localhost:8080/MinLanWeb//test1.mp3" controls="controls"></audio> </td>   
		data.add(cd.getStr("img1"));
		data.add(cd.getStr("img2"));
		return data;
	}
	
	/**
	 * 目录所需数据
	 * 
	 * @param cd
	 * @return
	 */
	public List<Map<String, Object>> toListCata(List<CourseData> cds) {

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < cds.size(); i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", cds.get(i).getLong("id"));
			data.put("cid", cds.get(i).getInt("cid"));
			data.put("chapter", cds.get(i).getInt("chapter"));
			data.put("section", cds.get(i).getInt("section"));
			data.put("kind", cds.get(i).getInt("kind"));
			data.put("name", cds.get(i).getStr("name"));

			// data.add(cd.getStr("name"));
			data.put("chinese", cds.get(i).getStr("chinese"));
			datas.add(data);
		}
		return datas;
	}
	
	/**
	 * 页面所需数据
	 * 
	 * @param cd
	 * @return
	 */
	public List<Map<String, Object>> toListPage(List<CourseData> cds) {
		
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < cds.size(); i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("id", cds.get(i).getLong("id"));
			data.put("cid", cds.get(i).getInt("cid"));
			data.put("chapter", cds.get(i).getInt("chapter"));
			data.put("section", cds.get(i).getInt("section"));
			data.put("chinese", cds.get(i).getStr("chinese"));
			data.put("foreign", cds.get(i).getStr("foreign"));
			data.put("chinese_say", cds.get(i).getStr("chinese_say"));
//			System.out.println(cds.get(i).getStr("foreign"));
			data.put("audio", cds.get(i).getStr("audio"));
			data.put("img1", cds.get(i).getStr("img1"));
			data.put("img2", cds.get(i).getStr("img2"));
			
			datas.add(data);
		}
		return datas;
	}
	
}
