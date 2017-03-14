package com.pureTec.index;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.pureTec.admin.AdminInterceptor;
import com.pureTec.behavior.Behavior;
import com.pureTec.favorite.Favorite;
import com.pureTec.question.QuestionNode;
import com.pureTec.tool.ExcleUtil;

@Before({ AdminInterceptor.class ,IndexInterceptor.class})
public class IndexController extends Controller{
	
	public void index(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String date=df.format(new Date());
		
	
		long buyCount=Behavior.me.getBehaviorCount(date, 3);
		long downloadCount=Behavior.me.getBehaviorCount(date, 1);
		long loginCount=Behavior.me.getBehaviorCount(date, 0);
		//System.out.println(loginCount);
		Calendar   cal   =   Calendar.getInstance();
		  cal.add(Calendar.DATE,   -1);
		  String yesterday = df.format(cal.getTime());
		  long yesbuyCount=Behavior.me.getBehaviorCount(yesterday, 3);
			long yesdownloadCount=Behavior.me.getBehaviorCount(yesterday, 1);
			long yesloginCount=Behavior.me.getBehaviorCount(yesterday, 0);
		setAttr("buyCount", buyCount);
		setAttr("downloadCount", downloadCount);
		setAttr("loginCount", loginCount);
		setAttr("yesbuyCount", parseToString(yesbuyCount,buyCount));
		setAttr("yesdownloadCount", parseToString(yesdownloadCount,downloadCount));
		setAttr("yesloginCount", parseToString(yesloginCount,loginCount));
		renderFreeMarker("index.html");
	}
	public String parseToString(long yes,long today){
		String result="";
		long i=today-yes;
		if(i>0){
			result="+"+i;
		}
		if(i==0){
			result="无变化";
		}if(i<0){
			result=String.valueOf(i);
		}
		return result;
		
	}

}
