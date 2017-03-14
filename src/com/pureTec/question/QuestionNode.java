package com.pureTec.question;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pureTec.behavior.Behavior;

@SuppressWarnings("serial")
public class QuestionNode extends Model<QuestionNode> {
	public static final QuestionNode me = new QuestionNode();

	/**
	 * 
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param userID
	 *            客服id
	 * @param isAsw
	 *            是否已回答
	 * @param type
	 *            该用户是否是管理员
	 * @return
	 */
	public Page<QuestionNode> paginate(int pageNumber, int pageSize,
			int userID, int isAsw, int type) {

		if (type == 1) // /客服
		{
			if (isAsw == 1) {
				return QuestionNode.me.paginate(pageNumber, pageSize,
						"select * ",
						" from questionmsg where isAsw=1 and aswId=" + userID
								+ " order by qstTime desc ");
			} else {
				return QuestionNode.me.paginate(pageNumber, pageSize,
						"select * ",
						" from questionmsg where isAsw=0 and aswId=" + userID
								+ " order by qstTime ");
			}

		} else if (type == 0) // /管理员
		{
			if (isAsw == 1) {
				return QuestionNode.me.paginate(pageNumber, pageSize,
						"select * ",
						" from questionmsg where isAsw=1  order by qstTime desc");
			} else {
				return QuestionNode.me.paginate(pageNumber, pageSize,
						"select * ",
						" from questionmsg where isAsw=0  order by qstTime ");
			}

		} else // /若不是客服 也不是管理员
		{
			// /返回空
			return QuestionNode.me
					.paginate(pageNumber, pageSize, "select * ",
							" from questionmsg where isAsw=1 and aswId=-1 order by qstTime desc");

		}

	}

	public int count(String userID, boolean flag) {
		// //long anum =
		// Db.queryLong("select count(1) from gbook where aid = ? and state = 1",top.get("aid"));
		// return QuestionNode.me.
		return 10;
	}

	public void update(int id, String answer,int aswid,String aswname) {
		Record question = Db.findById("questionmsg", id).set("aswCon", answer)
				.set("aswTime", new Date()).set("isAsw", 1).set("aswId",aswid).set("aswName",aswname).set("isRead",0);
		Db.update("questionmsg", question);
	}

	public int getUnAnswerNum(int userId) {
		// TODO Auto-generated method stub

		return Db.queryLong(
				"select count(*) from questionmsg where aswId=" + userId
						+ " and isAsw=0").intValue();
	}

	
	public int getUnAnswerNum() {
		// TODO Auto-generated method stub

		return Db.queryLong(
				"select count(*) from questionmsg where  isAsw=0").intValue();
	}
	
	
	/**
	 * 
	 * 找到某类型客服中当前未回答问题最少的一个
	 * 
	 * @param type
	 * @return
	 */
	public int findFisrtByType(int type) {
		// TODO Auto-generated method stub
		QuestionNode node = me
				.findFirst("select  aswId  from admin as ad left join (select count(*) count ,aswId from questionmsg where transType="
						+ type
						+ " and isAsw=0 group by aswId  ) as t on ad.id=t.aswId where ad.transType="
						+ type
						+ " and ad.type=1 order by t.count desc limit 1;");
		if (node != null)
			return node.getInt("aswId");

		return -1;
	}

	
	/**
	 * 根据 id  找到前序问题
	 * 
	 * @param id   
	 * @param isAsw   已回答还是未回答
	 * @param userId   客服id
	 * @return
	 */
	public int findFront(int id, int isAsw, int userId) {
		// TODO Auto-generated method stub
		String sql="select id from questionmsg where id<"+id+" and isAsw="+isAsw+" and aswId="+userId+" order by id desc limit 0,1";
		QuestionNode node=me.findFirst(sql);
		if(node==null)
		 return -1;
		else
		 return node.getInt("id");
	}

	

	/**
	 * 根据 id  找到后继问题
	 * 
	 * @param id   
	 * @param isAsw   已回答还是未回答
	 * @param userId 
	 * @return
	 */
	public int findBehind(int id, int isAsw, int userId) {
		// TODO Auto-generated method stub
		String sql="select id from questionmsg where id>"+id+" and isAsw="+isAsw+" and aswId="+userId+" order by id  limit 0,1";
		QuestionNode node=me.findFirst(sql);
		if(node==null)
		 return -1;
		else
		 return node.getInt("id");
	}
	
	/**
	 * 根据提出的问题关键词搜索    此方法未使用
	 */
	public Page<QuestionNode> searchByQuestion(int pageNumber, int pageSize,String keyword) {
		return paginate(pageNumber, pageSize, "SELECT *", "from questionmsg  where isAsw=1  and qstCon like '%" + keyword + "%' order by qstTime desc");
	}

	
	/**
	 * 
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param userID
	 *            客服id
	 * @param isAsw
	 *            是否已回答
	 * @param type
	 *            该用户是否是管理员
	 *            
	 * @order 1 表示升序，0表示降序
	 * @return
	 */
	public Page<QuestionNode> orderByTime(int pageNumber, int pageSize,
			int userID, int isAsw, int type,int order) {
		if(order==1){
			if (type == 1) // /客服
			{
				if (isAsw == 1) {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=1 and aswId=" + userID
									+ " order by qstTime asc ");
				} else {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=0 and aswId=" + userID
									+ " order by qstTime asc");
				}

			} else if (type == 0) // /管理员
			{
				if (isAsw == 1) {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=1  order by qstTime asc");
				} else {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=0  order by qstTime asc");
				}

			} else // /若不是客服 也不是管理员
			{
				// /返回空
				return QuestionNode.me
						.paginate(pageNumber, pageSize, "select * ",
								" from questionmsg where isAsw=1 and aswId=-1 order by qstTime asc");

			}
		}else{
			
			
			
			
			if (type == 1) // /客服
			{
				if (isAsw == 1) {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=1 and aswId=" + userID
									+ " order by qstTime desc ");
				} else {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=0 and aswId=" + userID
									+ " order by qstTime desc");
				}

			} else if (type == 0) // /管理员
			{
				if (isAsw == 1) {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=1  order by qstTime desc");
				} else {
					return QuestionNode.me.paginate(pageNumber, pageSize,
							"select * ",
							" from questionmsg where isAsw=0  order by qstTime desc");
				}

			} else // /若不是客服 也不是管理员
			{
				// /返回空
				return QuestionNode.me
						.paginate(pageNumber, pageSize, "select * ",
								" from questionmsg where isAsw=1 and aswId=-1 order by qstTime desc");

			}
		}
		

	}
	
	
	/**
	 * 
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param userID
	 *            客服id
	 * @param isAsw
	 *            是否已回答
	 * @param type
	 *            该用户是否是管理员
	 *            
	 * @sql    查询的后半段sql语句
	 * @return
	 */
	
	
	
	public Page<QuestionNode> searchBy(int pageNumber, int pageSize,int isAsw,String sql) {
		
		String presqlSelect = "SELECT * ";

		String presqlContent = " from questionmsg where isAsw="+isAsw+" ";
		System.out.println(presqlSelect+presqlContent + sql);
		return paginate(pageNumber, pageSize, presqlSelect, presqlContent + sql);


		
			
		}
		

	public boolean deleteQuestion(int id){
		return deleteById(id);
		
	}
	}


