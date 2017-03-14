package com.pureTec.course;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.admin.AdminInterceptor;
import com.pureTec.tool.ExcleUtil;
import com.pureTec.tool.SaveObject;

/**
 * 
 *   
 */
@Before({ AdminInterceptor.class, CourseInterceptor.class })
public class CourseController extends Controller {
	private static String IMG_PATH_FOLDER = File.separator + "upload_img" + File.separator; // 图片储存路劲
	// 存放临时文件的目录
	// private static String IMG_TEMP_FOLDER = "/uploadTemp_img/";
	private static String IMG_TEMP_FOLDER = File.separator + "uploadTemp_img" + File.separator;
	private static String Excel_PATH_FOLDER = File.separator + "upload_xls" + File.separator; // Excel存储路劲
	// 存放临时文件的目录
	private static String Excel_TEMP_FOLDER = File.separator + "uploadTemp_xls+File.separator";
	
	private static String FILE_SOURCE_FOLDER = File.separator + "file_source" + File.separator; // 图片储存路劲

	/**
	 * 将para转换为Course
	 * 
	 * @return
	 */
	public Course paraToCourse() {
		Course newCourse = new Course();
		if (!getPara("id").equals("")) {
			newCourse.set("id", getPara("id")); // 有id是更新课件用处，和新建区别开来
		}
		newCourse.set("cid", getPara("cid")); //
		newCourse.set("name", getPara("name"));
		newCourse.set("size", getPara("size"));
		newCourse.set("price", getParaToInt("price"));
		newCourse.set("describe", getPara("describe"));
		newCourse.set("update_time", new Date());
		newCourse.set("zip_time",  new Date());
		newCourse.set("status", getPara("status"));
		newCourse.set("img1", getPara("img1"));
		newCourse.set("img2", getPara("img2"));
		newCourse.set("img3", getPara("img3"));
		
		
		
		return newCourse;
	}

	/**
	 * 所有课件的列表数据
	 */
	public void list() {
		Page<Course> Courses = Course.me.paginate(getParaToInt("pageNumber", 1), 10);
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		List<Course> needCourses = Courses.getList();
		for (Course Course : needCourses) {
			rows.add(Course.me.courseToListData(Course));
		}
		setAttr("rows", rows);
		setAttr("pageNumber", Courses.getPageNumber());
		setAttr("countPage", Courses.getTotalPage());
		renderFreeMarker("list_course.html");
		// renderFreeMarker("123");
	}

	/**
	 * 所有课件的列表数据
	 */
	public void listFile() {
		Page<Course> Courses = Course.me.paginate(getParaToInt("pageNumber", 1), 10);
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		List<Course> needCourses = Courses.getList();
		// 这里要做一些统计，统计下载的次数，在行为表里面进行
		for (Course Course : needCourses) {
			rows.add(Course.me.courseFileToListData(Course));
		}
		setAttr("rows", rows);
		setAttr("pageNumber", Courses.getPageNumber());
		setAttr("countPage", Courses.getTotalPage());
		renderFreeMarker("list_course_file.html");
		// renderFreeMarker("123");
	}

	public void searchIni() {
		// renderFreeMarker("lCourse.jsp");
		renderFreeMarker("search_Course.html");
	}

	/**
	 * 添加课件
	 */
	public void addCourseIni() {
		setAttr("operation", "add");
		// renderFreeMarker("add_device.jsp");
		renderFreeMarker("add_course.html");
	}

	/**
	 * 新增设备
	 */
	public void addCourse() {
		paraToCourse().save();
		// redirect("/device/info");
		// 需要新建文件夹，用来给课件上传
		String dir = getPara("cid");
		
		String realPath = getRequest().getRealPath("") ;

		String fileSoucePath =realPath+ FILE_SOURCE_FOLDER+dir;
		
		String fileSoucePathSource =fileSoucePath+File.separator+"source";

		File file = new File(fileSoucePath);
		file.mkdir();
	    file = new File(fileSoucePathSource);
		file.mkdir();
		file = new File(fileSoucePathSource+File.separator+"audio");
		file.mkdir();
		
		
		redirect("/course/list");
	}
	
	/*
	 * 删除设备
	 */
	public void deleteCourse(){
		int id = getParaToInt("id");
        Course.me.deleteById(id);
		redirect("/course/list");
	}
	

	/**
	 * 修改预先
	 */
	public void updateCourseIni() {
		int id = getParaToInt("id");
		Course course = Course.me.findbyid(id);
		String path = getRequest().getContextPath();
		String basePath = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + path;
		// setAttr("data",
		// Course.me.courseToMapData(course,basePath+"upload_img/"));
		setAttr("data", Course.me.courseToMapData(course, basePath + IMG_PATH_FOLDER));
		setAttr("operation", "update");
		renderFreeMarker("add_course.html");
	}

	public void updateCourse() {
		paraToCourse().update();
		redirect("/course/list");
	}

	public void chapter() {
		// paraToCourse().update();
		renderFreeMarker("charpter.html");
	}

	public void charpterList() {
		int cid = getParaToInt("cid");
		List<CourseData> cds = CourseData.me.findCharpter(cid);
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		for (CourseData cd : cds) {
			rows.add(CourseData.me.courseToListData(cd));
		}
		// 需要获得course的相关详细
		Course course = Course.me.findbyid(cid);
		setAttr("cname", course.get("name"));
		setAttr("cid", cid);
		setAttr("rows", rows);
		// setAttr("operation", "update");
		renderFreeMarker("charpter.html");
	}

	public void sectionList() {
		int cid = getParaToInt("cid");
		// 需要获取课件编号，作为文件夹名称，获取音频资源
		Course course = Course.me.findbyid(cid);
		int chapter = getParaToInt("chapter");
		List<CourseData> cds = CourseData.me.findSection(cid, chapter);
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		for (CourseData cd : cds) {
			rows.add(CourseData.me.courseToListData(cd));
		}
		// Course course = Course.me.findbyid(cid);
		setAttr("cname", course.get("name"));
		// if (getPara("cdId") != null) {
		// Long cdId = getParaToLong("cdId");
		CourseData chapterCD = CourseData.me.findCharpterCD(cid, chapter);
		setAttr("chapterName", chapterCD.get("name"));
		// }
		setAttr("cid", cid);
		setAttr("chapter", chapter);
		setAttr("rows", rows);
		renderFreeMarker("section.html");
	}

	public void wordList() {
		int cid = getParaToInt("cid");
		int chapter = getParaToInt("chapter");
		int section = getParaToInt("section");
		List<CourseData> cds = CourseData.me.findWords(cid, chapter, section);
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		Course course = Course.me.findbyid(cid);
		String soureDirName = course.getStr("cid"); // 课件编号
		for (CourseData cd : cds) {
			rows.add(CourseData.me.toListDataWord(cd, soureDirName));
		}
		setAttr("cname", course.get("name"));
		CourseData chapterCD = CourseData.me.findCharpterCD(cid, chapter);
		CourseData sectionCD = CourseData.me.findSectionCD(cid, chapter, section);
		setAttr("cid", cid);
		setAttr("chapter", chapter);
		setAttr("chapterName", chapterCD.get("name"));
		setAttr("sname", sectionCD.get("name"));
		setAttr("cid", cid);
		setAttr("chapter", chapter);
		setAttr("section", section);
		setAttr("rows", rows);
		renderFreeMarker("word.html");
	}

	public void uploadImg() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
		// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		/**
		 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的 然后再将其真正写到 对应目录的硬盘上
		 */
		factory.setRepository(new File(IMG_TEMP_FOLDER));
		// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024 * 1024);
		// getServletContext().getRealPath("/");
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			// 提交上来的信息都在这个list里面
			// 这意味着可以上传多个文件
			// 请自行组织代码
			List<FileItem> list = upload.parseRequest(getRequest());
			// 获取上传的文件
			FileItem item = getUploadFileItem(list);
			// 获取文件名
			String filename = getUploadFileName(item);
			// 保存后的文件名
			String saveName = new Date().getTime() + filename.substring(filename.lastIndexOf("."));
			// 保存后图片的浏览器访问路径
			String picUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort()
					+ getRequest().getContextPath() + IMG_PATH_FOLDER + saveName;
			// String picUrl = getRequest().getScheme() + "://" +
			// getRequest().getServerName() + ":"
			// + getRequest().getServerPort() + getRequest().getContextPath() +
			// "/upload/" + saveName;
			String realPath = getRequest().getRealPath("") + IMG_PATH_FOLDER;
			// System.out.println("存放目录:" + PATH_FOLDER);
			System.out.println("存放目录:" + realPath);
			System.out.println("文件名:" + filename);
			System.out.println("浏览器访问路径:" + picUrl);
			// 真正写到磁盘上
			// item.write(new File(PATH_FOLDER, saveName)); // 第三方提供的
			item.write(new File(realPath, saveName)); // 第三方提供的
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("fileName", filename);
			result.put("serverfileName", picUrl.substring(picUrl.lastIndexOf(File.separator) + 1));
			result.put("picUrl", picUrl);
			renderJson(result);
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件列表的第一个文件
	 * 
	 * @param list
	 * @return
	 */
	private FileItem getUploadFileItem(List<FileItem> list) {
		for (FileItem fileItem : list) {
			if (!fileItem.isFormField()) {
				return fileItem;
			}
		}
		return null;
	}

	/**
	 * 对含有文件的表单的特殊处理 返回第一个input表单的值
	 * 
	 * @param list
	 * @return
	 */
	private String getUploadNotFileItem(List<FileItem> list) {
		for (FileItem item : list) {
			// if (item.isFormField()) {
			if (item.isFormField()) {
				// cid--------1
				// System.out.println(item.getFieldName() + "--------" +
				// item.getString()); // 值在这里
				return item.getString();
			}
		}
		return null;
	}

	private String getUploadNotFileItem2nd(List<FileItem> list) {
		int count = 1; // 返回第二个非文件参数
		for (FileItem item : list) {
			if (item.isFormField()) {
				if (count == 0) {
					return item.getString();
				}
				count--;
			}
		}
		return null;
	}

	private String getUploadNotFileItem3nd(List<FileItem> list) {
		int count = 2; // 返回第二个非文件参数
		for (FileItem item : list) {
			if (item.isFormField()) {
				if (count == 0) {
					return item.getString();
				}
				count--;
			}
		}
		return null;
	}

	/**
	 * 截取上传文件名
	 * 
	 * @param item
	 * @return
	 */
	private String getUploadFileName(FileItem item) {
		// 获取路径名
		String value = item.getName();
		// 索引到最后一个反斜杠
		int start = value.lastIndexOf("/");
		// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
		String filename = value.substring(start + 1);
		return filename;
	}

	/**
	 * 测试用，最后可以删除
	 */
	public void uploadImgInt() {
		renderFreeMarker("ajaxFileUpload.jsp");
	}

	public String saveExcelFile(String filename, FileItem item) {
		// 获取文件名
		// String filename = getUploadFileName(item);
		// getUploadNotFileItem(list);
		//
		// 保存后的文件名
		String saveName = new Date().getTime() + filename.substring(filename.lastIndexOf("."));
		// 保存后图片的浏览器访问路径
		String picUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort()
				+ getRequest().getContextPath() + Excel_PATH_FOLDER + saveName;
		String realPath = getRequest().getRealPath("") + Excel_PATH_FOLDER;
		// System.out.println("存放目录:" + PATH_FOLDER);
		System.out.println("存放目录:" + realPath);
		System.out.println("文件名:" + filename);
		System.out.println("浏览器访问路径:" + picUrl);
		// 真正写到磁盘上
		// item.write(new File(PATH_FOLDER, saveName)); // 第三方提供的
		try {
			item.write(new File(realPath, saveName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 第三方提供的
			// PrintWriter writer = getResponse().getWriter();
			//
			// writer.print("{");
			// writer.print("msg:\"fileSize:"+item.getSize()+",filename:"+filename+"\"");
			// writer.print(",picUrl:\"" + picUrl + "\"");
			// writer.print("}");
			//
			// writer.close();
		StringBuffer result = new StringBuffer();
		String serverfileName = picUrl.substring(picUrl.lastIndexOf(File.separator) + 1);
		// String serverfileName = picUrl.substring(picUrl.lastIndexOf("/") +
		// 1);
		result.append("{");
		result.append("fileName:\"" + filename + "\"");
		result.append(",serverfileName:\"" + serverfileName + "\"");
		result.append(",picUrl:\"" + picUrl + "\"");
		result.append("}");
		System.out.println("--------" + result.toString());
		// renderText(result.toString());
		return serverfileName;
	}

	/**
	 * Excel 表的导入导出操作 提交上去一个Excel文件，到后台，让后后台解析这个文件，最后刷新当前数据。<br/>
	 * 更新 章 列表
	 */
	public void uploadExcelChapter() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(Excel_TEMP_FOLDER));
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> list = null;
		try {
			list = upload.parseRequest(getRequest());
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
		String cid = getUploadNotFileItem(list);
		// 获取上传的文件
		FileItem item = getUploadFileItem(list); // 这里获取文件
		String filename = getUploadFileName(item);
		/**
		 * 第一步把文件保存到服务器上面
		 */
		String filePath = Excel_PATH_FOLDER + saveExcelFile(filename, item);
		String path = getRequest().getRealPath("/");
		filePath = path + filePath;
		System.out.println("--------" + filePath);
		/**
		 * 第二步把读取上面的文件，解析Excel文件，更新数据库，然后刷新页面 <br/>
		 * 读取为coureData 对象的文件，然后对指定的对象进行更新，这里的细节蛮多的<br/>
		 * 有id 则对id进行更新<br/>
		 * 无id 则新增，需要一些额外的信息<br/>
		 */
		// String filePath
		// ="genExcle"+File.separator+divice_id+"_"+System.currentTimeMillis()+".xlsx";
		// 读取文件
		// 保存文件的时候 是数值类型的就是保存为数值类型，
		try {
			List<CourseData> cds = ExcleUtil.readCoureData(filePath);
			for (int i = 0; i < cds.size(); i++) {
				// System.out.println("-----chinese---" +
				// cds.get(i).getStr("chinese"));
				// System.out.println("-----删除---" +
				// cds.get(i).getInt("order"));
				if (cds.get(i).getLong("id") != 0 && cds.get(i).getInt("order") != null) { // 对数据进行更新
					cds.get(i).update(); // 测试过了，只对有值得部分进行更新，没有值得部分保持数据库原状
				}
				if (cds.get(i).getLong("id") != 0 && cds.get(i).getInt("order") == null) {// 删除的操作
					cds.get(i).delete();
				}
				if (cds.get(i).getLong("id") == 0) {// 新增记录
					cds.get(i).remove("id");
					cds.get(i).set("cid", cid); // 表示courseId
					cds.get(i).set("kind", 1);// 表示章
					cds.get(i).save();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 重新刷新当前页面
		// int cid = getParaToInt("cid");
		// http://localhost:8080/MinLanWeb/course/charpterList?cid=1
		redirect("/course/charpterList?cid=" + cid);
		// http://localhost:8080/MinLanWebcharpterList?cid=1
		// // charpterList();
		// List<CourseData> cds =
		// CourseData.me.findCharpter(Integer.parseInt(cid));
		// List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		// for (CourseData cd : cds) {
		// rows.add(CourseData.me.deviceToListData(cd));
		// }
		// setAttr("cid", cid);
		// setAttr("rows", rows);
		// renderFreeMarker("charpter.html");
		// // renderText("OK"+cid);
	}

	/**
	 * Excel 表的导入导出操作 提交上去一个Excel文件，到后台，让后后台解析这个文件，最后刷新当前数据。<br/>
	 * 更新 节 列表
	 */
	public void uploadExcelSection() {
		// int cid = getParaToInt("cid");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
		// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		/**
		 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的 然后再将其真正写到 对应目录的硬盘上
		 */
		factory.setRepository(new File(Excel_TEMP_FOLDER));
		// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024 * 1024);
		// getServletContext().getRealPath("/");
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 提交上来的信息都在这个list里面
		// 这意味着可以上传多个文件
		// 请自行组织代码
		List<FileItem> list = null;
		try {
			list = upload.parseRequest(getRequest());
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String cid = getUploadNotFileItem(list);
		String chapter = getUploadNotFileItem2nd(list);
		// 获取上传的文件
		FileItem item = getUploadFileItem(list); // 这里获取文件
		String filename = getUploadFileName(item);
		/**
		 * 第一步把文件保存到服务器上面
		 */
		String filePath = Excel_PATH_FOLDER + saveExcelFile(filename, item);
		String path = getRequest().getRealPath("/");
		filePath = path + filePath;
		System.out.println("--------" + filePath);
		/**
		 * 第二步把读取上面的文件，解析Excel文件，更新数据库，然后刷新页面 <br/>
		 * 读取为coureData 对象的文件，然后对指定的对象进行更新，这里的细节蛮多的<br/>
		 * 有id 则对id进行更新<br/>
		 * 无id 则新增，需要一些额外的信息<br/>
		 */
		// String filePath
		// ="genExcle"+File.separator+divice_id+"_"+System.currentTimeMillis()+".xlsx";
		// 读取文件
		// 保存文件的时候 是数值类型的就是保存为数值类型，
		try {
			List<CourseData> cds = ExcleUtil.readCoureDataForSection(filePath, Integer.parseInt(chapter));
			for (int i = 0; i < cds.size(); i++) {
				// System.out.println("-----chinese---" +
				// cds.get(i).getStr("chinese"));
				// System.out.println("-----删除---" +
				// cds.get(i).getInt("order"));
				if (cds.get(i).getLong("id") != 0 && cds.get(i).getInt("order") != null) { // 对数据进行更新
					cds.get(i).update(); // 测试过了，只对有值得部分进行更新，没有值得部分保持数据库原状
				}
				if (cds.get(i).getLong("id") != 0 && cds.get(i).getInt("order") == null) {// 删除的操作
					cds.get(i).delete();
				}
				if (cds.get(i).getLong("id") == 0) {// 新增记录
					cds.get(i).remove("id");
					cds.get(i).set("cid", cid); // 表示courseId
					cds.get(i).set("kind", 2);// 表示节
					cds.get(i).save();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 重新刷新当前页面
		redirect("/course/sectionList?cid=" + cid + "&&chapter=" + chapter);
		// sectionList?cid=1&&chapter=1
	}

	/**
	 * Excel 表的导入导出操作 提交上去一个Excel文件，到后台，让后后台解析这个文件，最后刷新当前数据。<br/>
	 * 更新 单词 列表
	 * 1、支持单词里面的小编号，2、支持单词注释功能2016-9-7 <br/>
	 */
	public void uploadExcelWords() {
		// int cid = getParaToInt("cid");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
		// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		/**
		 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的 然后再将其真正写到 对应目录的硬盘上
		 */
		factory.setRepository(new File(Excel_TEMP_FOLDER));
		// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024 * 1024);
		// getServletContext().getRealPath("/");
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 提交上来的信息都在这个list里面
		// 这意味着可以上传多个文件
		// 请自行组织代码
		List<FileItem> list = null;
		try {
			list = upload.parseRequest(getRequest());
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String cid = getUploadNotFileItem(list);
		String chapter = getUploadNotFileItem2nd(list);
		String section = getUploadNotFileItem3nd(list);
		// 获取上传的文件
		FileItem item = getUploadFileItem(list); // 这里获取文件
		String filename = getUploadFileName(item);
		/**
		 * 第一步把文件保存到服务器上面
		 */
		String filePath = Excel_PATH_FOLDER + saveExcelFile(filename, item);
		String path = getRequest().getRealPath("/");
		filePath = path + filePath;
		System.out.println("--------" + filePath);
		/**
		 * 第二步把读取上面的文件，解析Excel文件，更新数据库，然后刷新页面 <br/>
		 * 读取为coureData 对象的文件，然后对指定的对象进行更新，这里的细节蛮多的<br/>
		 * 有id 则对id进行更新<br/>
		 * 无id 则新增，需要一些额外的信息<br/>
		 */
		// String filePath
		// ="genExcle"+File.separator+divice_id+"_"+System.currentTimeMillis()+".xlsx";
		// 读取文件
		// 保存文件的时候 是数值类型的就是保存为数值类型，
		try {
			List<CourseData> cds = ExcleUtil.readCoureDataWords(filePath, Integer.parseInt(chapter), Integer.parseInt(section));
			for (int i = 0; i < cds.size(); i++) {
				// System.out.println("-----chinese---" +
				// cds.get(i).getStr("chinese"));
				// System.out.println("-----删除---" +
				// cds.get(i).getInt("order"));
				if (cds.get(i).getLong("id") != 0 && cds.get(i).getInt("order") != null) { // 对数据进行更新
					cds.get(i).update(); // 测试过了，只对有值得部分进行更新，没有值得部分保持数据库原状
				}
				if (cds.get(i).getLong("id") != 0 && cds.get(i).getInt("order") == null) {// 删除的操作
					cds.get(i).delete();
				}
				if (cds.get(i).getLong("id") == 0) {// 新增记录
					cds.get(i).remove("id");
					cds.get(i).set("cid", cid); // 表示courseId
					cds.get(i).set("kind", 3);// 表示章
					cds.get(i).save();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 重新刷新当前页面
		// int cid = getParaToInt("cid");
		// http://localhost:8080/MinLanWeb/course/charpterList?cid=1
		redirect("/course/wordList?cid=" + cid + "&&chapter=" + chapter + "&&section=" + section);
		// /wordList?cid=1&&chapter=1&&section=1
	}

	/**
	 * 按照需求下载Excel内容
	 */
	public void downExcel() {
		// 从数据库中提取出数据，然后写入Excel文件，最后renderFile下载
		int cid = getParaToInt("cid");
		int kind = getParaToInt("kind");
		if (kind == 1) {
			List<CourseData> cds = CourseData.me.findCharpter(cid);
			List<List<Object>> rows = new ArrayList<List<Object>>();
			for (CourseData cd : cds) {
				rows.add(CourseData.me.courseToListDataForGenExcelFile(cd));
			}
			String path = getRequest().getRealPath("/");
			// G:\software\apache-tomcat-7.0.63\webapps\TowerPowerMonitor\
			System.out.println(path);
			// 第一步先生成excel
			String titles[] = { "ID", "排序编号", "章编号", "章名称", "标题" };
			// 生成excel
			String filePath = "genExcel" + File.separator + cid + "_0_0_" + System.currentTimeMillis() + ".xlsx";
			String excelPath = path + filePath;
			try {
				ExcleUtil.write(excelPath, titles, rows);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(excelPath);
			redirect(File.separator + filePath); // 下载文件
		}
		if (kind == 2) {
			int chapter = getParaToInt("chapter");
			List<CourseData> cds = CourseData.me.findSection(cid, chapter);
			List<List<Object>> rows = new ArrayList<List<Object>>();
			for (CourseData cd : cds) {
				rows.add(CourseData.me.courseToListDataForGenExcelFile(cd));
			}
			String path = getRequest().getRealPath("/");
			// G:\software\apache-tomcat-7.0.63\webapps\TowerPowerMonitor\
			System.out.println(path);
			// 第一步先生成excel
			String titles[] = { "ID", "排序编号", "节编号", "节名称", "标题" };
			// 生成excel
			String filePath = "genExcel" + File.separator + cid + "_" + chapter + "_0_" + System.currentTimeMillis() + ".xlsx";
			String excelPath = path + filePath;
			try {
				ExcleUtil.write(excelPath, titles, rows);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(excelPath);
			redirect(File.separator + filePath); // 下载文件
		}
		if (kind == 3) {
			int chapter = getParaToInt("chapter");
			int section = getParaToInt("section");
			List<CourseData> cds = CourseData.me.findWords(cid, chapter, section);
			List<List<Object>> rows = new ArrayList<List<Object>>();
			for (CourseData cd : cds) {
				rows.add(CourseData.me.courseToListDataForGenExcelFileForWords(cd));
			}
			String path = getRequest().getRealPath("/");
			// G:\software\apache-tomcat-7.0.63\webapps\TowerPowerMonitor\
			System.out.println(path);
			// 第一步先生成excel
			String titles[] = { "ID", "排序编号", "中文", "中文注音", "外文", "音频编号", "图片1", "图片2" ,"注释"};
			// 生成excel
			String filePath = "genExcel" + File.separator + cid + "_" + chapter + "_" + section + "_" + System.currentTimeMillis() + ".xlsx";
			String excelPath = path + filePath;
			try {
				ExcleUtil.write(excelPath, titles, rows);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(excelPath);
			redirect(File.separator + filePath); // 下载文件
		}
	}

	/**
	 * 
	 * 将最新的课件素材压缩为单个压缩文件
	 * 
	 */
	public void courseToZipFile() {
		// 要记录生成课件的事件
		// 生成 资源索引 目录 本身等信息保存到本地
		// 记录最新版本号到数据库
		// 压缩另外启动一个线程，主线程不做事情。
		int cid = getParaToInt("cid");
		String courseID = Course.me.findCIDbyid(cid);
		String sourdir = File.separator + "file_source" + File.separator + courseID + File.separator; // 需要压缩的课件路劲
		String realPath = getRequest().getRealPath("") + sourdir;
		// System.out.println("存放目录:" + PATH_FOLDER);
		System.out.println("存放目录:" + realPath);
		/*
		 * 生成 生成资源文件，直接用json格式保存 课件自身的信息.dat data0.dat 目录.dat data1.dat 资源索引.dat data2.dat
		 */
		Course course = Course.me.findbyid(cid);// 先不做预处理，统一放到data0.dat里面去
		SaveObject.saveObject(Course.me.courseToMapData(course, ""), realPath + "source" + File.separator + "data0.dat");
		/*
		 * 提取目录 data1.dat
		 */
		List<CourseData> cata = CourseData.me.findCata(cid);
		List<Map<String, Object>> datas = CourseData.me.toListCata(cata);
		SaveObject.saveObject(datas, realPath + "source" + File.separator + "data1.dat");
		/*
		 * 提取资源索引文件 data1.dat 就是kind=3 cid=cid 的所有数据
		 */
		List<CourseData> words = CourseData.me.sourceWordAll(cid);
		List<Map<String, Object>> wordsdatas = CourseData.me.toListPage(words);
		SaveObject.saveObject(wordsdatas, realPath + "source" + File.separator + "data2.dat"); //在内存里面是正确的
		ZipThread zipThread = new ZipThread();
		zipThread.setSourseDir(realPath);
		zipThread.setCoureID(getParaToInt("cid"));
		zipThread.setCourseIDStr(courseID);
		new Thread(zipThread).start();
		// renderText("系统正在生成课件包，请5分钟后刷新本页面查看生成结果。");
		// String result = "系统正在生成课件包，请5分钟后刷新本页面查看生成结果。" ;
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("msg", "系统正在生成课件包，请5分钟后刷新本页面查看生成结果。");
		renderJson(result);
	}
}
