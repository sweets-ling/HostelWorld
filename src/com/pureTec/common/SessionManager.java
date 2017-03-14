package com.pureTec.common;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import com.pureTec.admin.Admin;
import com.pureTec.question.QuestionNode;

public class SessionManager {
	
	private static CopyOnWriteArrayList<Integer>  taiList=new CopyOnWriteArrayList<Integer>();  ////存储泰语的客服
	
	static CopyOnWriteArrayList<Integer>  laowoList=new CopyOnWriteArrayList<Integer>();  ////存储老挝语的客服
	
	static int  taiIndex=0;
	static int laowoIndex=0;
	
	
	
	static {
		 Timer timer = new Timer();  
	       
		 timer.scheduleAtFixedRate(new TimerTask() {  
	            public void run() {  
	                System.out.println("-------设定要指定任务--------");  
	                ///清除list
	                laowoList.clear();
	                taiList.clear(); 
	                
	                
	                
	            }  
	        }, 1000*60*30, 1000*60*30);  
	}
	
	
	public   static  void addCustomServer(int id,int type)
	{
		if(type==0) //老挝
		{
			if(!laowoList.contains(id))
				laowoList.add(id);
		}
			
		else
			if(!taiList.contains(id))
				taiList.add(id);
	}
	
	
	public   static void  delCustomServer(int id,int type)
	{
		if(type==0) //老挝
		{
			if(laowoList.contains(id))
				laowoList.remove((Object)id);
		}
		else
		{
			if(taiList.contains(id))
				taiList.remove((Object)id);
		}
	}
	
	
	public static int calcCSNum(int type)
	{
		if(type==0) //老挝
		{
			int num =laowoList.size();
			if(num>0)
			{
				if(num<laowoIndex)
					laowoIndex=0;
				return laowoList.get(laowoIndex++);
			}
			///在线客服为零
			else
				return QuestionNode.me.findFisrtByType(type);
				
				
		}
			
		else
		{
			int num =taiList.size();
			if(num>0)
			{
				if(num<taiIndex)
					taiIndex=0;
				return taiList.get(taiIndex++);
			}
			///在线客服为零
			else
				return QuestionNode.me.findFisrtByType(type);
				
				
		}
	}
		

	

}




