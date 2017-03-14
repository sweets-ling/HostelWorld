package com.pureTec.suggest;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@Before(SuggestInterceptor.class)
public class SuggestController extends Controller {

/**
 * 消息列表
 */
	public void list() {
		Page<Suggest> list = Suggest.me.paginate(getParaToInt("pageNumber", 1), 10);
		
		setAttr("data", list);
		setAttr("pageNumber", list.getPageNumber());
		setAttr("countPage", list.getTotalPage());
		renderFreeMarker("suggest_list.html");
	}
	
	public void detail(){
		int id=getParaToInt("suggestId");
		Suggest data=Suggest.me.findById(id);
		setAttr("data",data);
		System.out.println(data.toString());
		renderFreeMarker("suggest_detail.html");
		
	}

	
}
