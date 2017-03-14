package com.pureTec.customer_server;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.admin.Admin;
import com.pureTec.admin.AdminInterceptor;
import com.pureTec.question.QuestionNode;

/**
 * 
 *   
 */
@Before({AdminInterceptor.class})
public class CustomerServer extends Controller {
	
	
	 ////获取问题列表
		public void answerListType0()
		{
			 answerList(0);
		}
		
		public void answerListType1()
		{
	      answerList(1);
		}
		
		public void answerList(int isAsw)  ///transtype 已回答还是未回答
		{
	        int userId=getSessionAttr("userId");
			int pageNumber = getParaToInt("pageNumber", 1);
			
			
			
			
			/**
			 * 原逻辑为  区分 管理员和 一般客服 
			 * 管理员：可以看所有问题
			 * 客服：  查看自己的问题
			 * 
			 * 
			 * 当前  修改为  客服可以看所有问题 
			 * 所有直接 将 userType都 改为为 管理员****** 
			 *
			 */

		    int userType=0;   ///getSessionAttr("userType"); ///是管理员 还是客服 
		  
			
			Page<QuestionNode> questionList = QuestionNode.me.paginate(pageNumber, 8,userId,isAsw,userType);

			
			String str= (isAsw==0?"未翻译列表":"翻译历史");
			setAttr("title",str);
			setAttr("isAsw",isAsw);
			setAttr("questionList", questionList);
			setAttr("pageNumber", questionList.getPageNumber());
			setAttr("countPage", questionList.getTotalPage());
			setAttr("pageSize", 8);
			render("answerList.html");
		}


////获取某一问题信息 
	public void cs_answerDetail()
	{
		int id = Integer.parseInt(getPara("id").toString());
		int isAsw=getParaToInt("isAsw");
		int userId=getSessionAttr("userId");
		
		QuestionNode qst=QuestionNode.me.findById(id);
		
		
		/**
		 * 
		 * 取消 查找上一题 下一题的逻辑
		 * 
		 */
//		/// 找到前序问题  后序问题
//		int front=QuestionNode.me.findFront(id,isAsw,userId);
//		int behind=QuestionNode.me.findBehind(id,isAsw,userId);
//		
		setAttr("isAsw",isAsw);
		setAttr("frontid", -1);
		setAttr("behindid", -1);
		
		
		setAttr("node", qst);
		setAttr("aswName",(String)getSessionAttr("userName"));
		if(qst.get("aswName")!=null)
			setAttr("aswName",qst.getStr("aswName"));
		renderFreeMarker("answer.html");     
	}
	
	
	//人工回答一个问题
		public void answer()
		{
			int id = Integer.parseInt( getPara("id").toString());
			String answer=getPara("answer").toString();
			QuestionNode.me.update(id,answer,(int)getSessionAttr("userId"),(String)getSessionAttr("userName"));
			renderText("回答成功");
		}
		
		
		
		
	//排序功能
	
		public void orderByTimeDescType0(){
			orderByTime(0,0);
		}
		public void orderByTimeAscType0(){
			orderByTime(0,1);
		}
		public void orderByTimeDescType1(){
			orderByTime(1,0);
		}
		public void orderByTimeAscType1(){
			orderByTime(1,1);
		}
		
		
		//问题列表按时间排序
		public void orderByTime(int isAsw,int order){
			  int userId=getSessionAttr("userId");
				int pageNumber = getParaToInt("pageNumber", 1);
				
				
				
				
				/**
				 * 原逻辑为  区分 管理员和 一般客服 
				 * 管理员：可以看所有问题
				 * 客服：  查看自己的问题
				 * 
				 * 
				 * 当前  修改为  客服可以看所有问题 
				 * 所有直接 将 userType都 改为为 管理员****** 
				 *
				 */

			    int userType=0;   ///getSessionAttr("userType"); ///是管理员 还是客服 
			  
				
				Page<QuestionNode> questionList = QuestionNode.me.orderByTime(pageNumber, 8,userId,isAsw,userType,order);

				
				String str= (isAsw==0?"未翻译列表":"翻译历史");
				setAttr("title",str);
				setAttr("isAsw",isAsw);
				setAttr("questionList", questionList);
				setAttr("pageNumber", questionList.getPageNumber());
				setAttr("countPage", questionList.getTotalPage());
				setAttr("pageSize", 8);
				render("answerList.html");
		}

		

}
