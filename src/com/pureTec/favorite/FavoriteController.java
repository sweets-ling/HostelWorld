package com.pureTec.favorite;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.admin.AdminInterceptor;

@Before({ AdminInterceptor.class ,FavoriteInterceptor.class})
public class FavoriteController extends Controller{

	/**
	 * Favorites list
	 */
	public void list() {
		Page<Favorite> Favorites = Favorite.me.paginate(getParaToInt("pageNumber", 1), 10);
		setAttr("data", Favorites);
		setAttr("pageNumber", Favorites.getPageNumber());
		setAttr("countPage", Favorites.getTotalPage());
		renderFreeMarker("favorite_list.html");
	}

	public Favorite paraToFavorite() {
		Favorite favorite = new Favorite();
		favorite.set("uid", getParaToInt("uid"));
		favorite.set("word_id",getPara("word_id"));
		favorite.set("add_time",new Date());
		return favorite;
	}
	////////////////////////app////////////////////////////////
	public void addFavorite() {
		boolean flag=false;
		flag= paraToFavorite().save();
	}

}
