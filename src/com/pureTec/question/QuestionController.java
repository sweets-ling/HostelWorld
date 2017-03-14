package com.pureTec.question;



import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.admin.Admin;
import com.pureTec.admin.AdminInterceptor;
import com.pureTec.common.SessionManager;
import com.pureTec.purchase.Purchase;




@Before({AdminInterceptor.class ,QuestionInterceptor.class})
public class QuestionController extends Controller {
	

	//人工回答一个问题
	public void answer()
	{
		int id = Integer.parseInt( getPara("id").toString());
		String answer=getPara("answer").toString();
		QuestionNode.me.update(id,answer,(int)getSessionAttr("userId"),(String)getSessionAttr("userName"));
		renderText("回答成功");
	}
	
	
	
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
	    int userType=0;  ///getSessionAttr("userType"); ///是管理员0   还是客服1  
	  
		
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
	public void answerDetail()
	{
		int id = Integer.parseInt(getPara("id").toString());
		QuestionNode qst=QuestionNode.me.findById(id);
		setAttr("node", qst);
		setAttr("aswName",(String)getSessionAttr("userName"));
		if(qst.get("aswName")!=null)
			setAttr("aswName",qst.getStr("aswName"));
		renderFreeMarker("answer.html");
	}
	
	
	public void getUnAnswerNum()
	{
		int num=0;
		int userId=getSessionAttr("userId");
	    int transType=getSessionAttr("transType");
	    int type=getSessionAttr("userType");
	    if(type==1)//客服
	    	SessionManager.addCustomServer(userId, transType);
		
		//num=QuestionNode.me.getUnAnswerNum(userId);
	    ////  所有未回答问题 不区分是哪个客服的
	    num=QuestionNode.me.getUnAnswerNum();
	    
	    renderJson("num",num);
			
	}
	
	
	public void searchReport(){
     
		String name = getPara("name");
		String context = getPara("context");
		String startDate = getPara("start_date", "");
		String endDate = getPara("end_date", "");
		int isAsw=getParaToInt("isAsw");
		int pageNumber = getParaToInt("pageNumber", 1);
		  int userId=getSessionAttr("userId");
			
		StringBuffer sql = new StringBuffer();
	
		if (name != null && !name.equals("")) {
			sql.append("and qstName=");
			sql.append("'");
			sql.append(name);
			sql.append("'");
		}
		if ( context!= null && !context.equals("") ) {
			sql.append("and qstCon like ");
			sql.append("'%");
			sql.append(context);
			sql.append("%'");
		}
		// sr.servicedate >= '2015-09-01' and sr.servicedate <= '2015-09-15'
		if (startDate != null && !startDate.equals("")) {
			sql.append(" and qstTime >=");
			sql.append("'");
			sql.append(startDate);
			sql.append("'");
		}
		if (endDate != null && !endDate.equals("")) {
			sql.append(" and qstTime<=");
			sql.append("'");
			sql.append(endDate);
			sql.append("'");
		}

		sql.append(" order by qstTime desc");
		Page<QuestionNode> questionList=QuestionNode.me.searchBy(pageNumber, 8,isAsw, sql.toString());
		

		setAttr("name", name);
		setAttr("context", context);
		setAttr("start_date", startDate);
		setAttr("end_date", endDate);
		setAttr("isAsw",isAsw);
		
		
			String str= (isAsw==0?"未翻译列表":"翻译历史");
			setAttr("title",str);
			setAttr("questionList", questionList);
			setAttr("pageNumber", questionList.getPageNumber());
			setAttr("countPage", questionList.getTotalPage());
			setAttr("pageSize", 8);
			render("answerList.html");
		
		
	}
	
	//删除一条翻译的记录
	public void deleteRecord(){
		int isAsw=getParaToInt("isAsw");
		int id=getParaToInt("id");
		System.out.println("deleteRecord");
		boolean ok=QuestionNode.me.deleteQuestion(id);
		System.out.println("删除记录"+ok);
		answerList(isAsw);
	}
	//删除所有查询到的翻译记录
	public void deleteAllRecord(){
		String name = getPara("name");
		String context = getPara("context");
		String startDate = getPara("start_date", "");
		String endDate = getPara("end_date", "");
		int isAsw=getParaToInt("isAsw");
		int pageNumber = getParaToInt("pageNumber", 1);
		  int userId=getSessionAttr("userId");
			
		StringBuffer sql = new StringBuffer();
	
		if (name != null && !name.equals("")) {
			sql.append("and qstName=");
			sql.append("'");
			sql.append(name);
			sql.append("'");
		}
		if ( context!= null && !context.equals("") ) {
			sql.append("and qstCon like ");
			sql.append("'%");
			sql.append(context);
			sql.append("%'");
		}
		// sr.servicedate >= '2015-09-01' and sr.servicedate <= '2015-09-15'
		if (startDate != null && !startDate.equals("")) {
			sql.append(" and qstTime >=");
			sql.append("'");
			sql.append(startDate);
			sql.append("'");
		}
		if (endDate != null && !endDate.equals("")) {
			sql.append(" and qstTime<=");
			sql.append("'");
			sql.append(endDate);
			sql.append("'");
		}

		sql.append(" order by qstTime desc");
		Page<QuestionNode> questionList=QuestionNode.me.searchBy(pageNumber, 8,isAsw, sql.toString());
		for(QuestionNode node:questionList.getList()){
			QuestionNode.me.deleteQuestion(node.getInt("id"));
		}

		searchReport();
		
		
	}
	
	
}
