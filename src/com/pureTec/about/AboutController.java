package com.pureTec.about;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.course.Course;
import com.pureTec.course.CourseData;
import com.pureTec.course.CourseInterceptor;
import com.pureTec.tool.ExcleUtil;
@Before(AboutInterceptor.class)
public class AboutController extends Controller{
	private static String	IMG_PATH_FOLDER		= File.separator + "upload_img" + File.separator;		// 图片储存路劲
	private static String	IMG_TEMP_FOLDER		= File.separator + "uploadTemp_img" + File.separator;


	/**
	 * 将para转换为about
	 */
	public About paraToAbout(String location) {
		About newAbout = new About();
		newAbout.set("picture", getPara("picture"));
		newAbout.set("describe", getPara("describe"));
		newAbout.set("location", location);
		newAbout.set("isActive",1);
		return newAbout;
	}
	
	/**
	 * 添加图文介绍
	 */
	public void set() {
		About about=About.me.findbyLoc("about");
		About share=About.me.findbyLoc("share");
		setAttr("about", about);
		setAttr("share",share);
		renderFreeMarker("set_about.html");
	}	
	
	/**
	 * 
	 * 保存关于我们图文介绍
	 */
	public void saveAbout(){
		About.me.setNoActive("about");
		paraToAbout("about").save();
		redirect("/about/set");	
	}
	/**
	 * 保存分享到qq空间的图文介绍
	 */
	public void saveShare(){
		About.me.setNoActive("share");
		paraToAbout("share").save();
		redirect("/about/set");	
	}
	/**
	 * 上传图片
	 */
	public void uploadImg() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
		// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		/**
		 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份
		 * ，第一个是以 .tem 格式的 然后再将其真正写到 对应目录的硬盘上
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
			String picUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":"
					+ getRequest().getServerPort() + getRequest().getContextPath() + IMG_PATH_FOLDER + saveName;
			// String picUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":"
			// + getRequest().getServerPort() + getRequest().getContextPath() + "/upload/" + saveName;
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
}
