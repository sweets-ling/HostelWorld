package com.pureTec.course;

import java.io.File;

import com.pureTec.tool.EncryptionNew;
import com.pureTec.tool.JavaFileDir;
import com.pureTec.tool.ZipUtil;

/**
 * 压缩线程
 * @author kk
 *
 */
public class ZipThread implements Runnable{
	String sourseDir; 
	int coureID;
	String courseIDStr;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//这个线程里面先对资源进行加密
//        Course.me.updateZipTime(getCoureID(),size); // 先更新版本

		
		EncryptionNew 	encryptionNew = new EncryptionNew();
		encryptionNew.encryptDir(getSourseDir()+"source"+ File.separator, "abcd");

		ZipUtil course = new ZipUtil();
		try {
//			course.zip("G:\\zipTest\\", "G:\\test",null);// 你要压缩的文件夹
//			course.zip(getSourseDir()+"source"+ File.separator, getSourseDir()+"file","source");// 你要压缩的文件夹
//			course.zip(getSourseDir()+"source"+ File.separator, getSourseDir()+courseIDStr,"source");// 你要压缩的文件夹
			course.zip(getSourseDir()+"MMsource"+ File.separator, getSourseDir()+courseIDStr,"MMsource");// 你要压缩的文件夹
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//File courseFile = new File("getSourseDir()+courseIDStr");
		JavaFileDir jfd = new  JavaFileDir();
		String size = jfd.fileSize(getSourseDir()+courseIDStr+".lwyr");
//		System.out.println(size);
		
        Course.me.updateZipTime(getCoureID(),size); // 根系版本
        // 出一个新版本需要做的事情
        // 文件大小size，version，更新时间，新版本事件
        // 先要读取version 然后加0.1 上去
        
        
        
        
        

	}
	
	public String getSourseDir() {
		return sourseDir;
	}

	public void setSourseDir(String sourseDir) {
		this.sourseDir = sourseDir;
	}


	public int getCoureID() {
		return coureID;
	}

	public void setCoureID(int coureID) {
		this.coureID = coureID;
	}

	public String getCourseIDStr() {
		return courseIDStr;
	}

	public void setCourseIDStr(String courseIDStr) {
		this.courseIDStr = courseIDStr;
	}


}
