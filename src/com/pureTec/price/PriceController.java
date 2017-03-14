package com.pureTec.price;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.admin.AdminInterceptor;

@Before({ AdminInterceptor.class, PriceInterceptor.class })
public class PriceController extends Controller {

	public void priceList() {

		// /最多10个价格
		Page<Price> adminList = Price.me.paginate(1, 10);

		setAttr("priceList", adminList);
		renderFreeMarker("priceList.html");

	}

	public void delete() {
		int id = getParaToInt("id");
		// 删除自身 不可行

		Price.me.deleteById(id);

		redirect("/price/priceList");
	}

	public void pricedetail() {
		int id = getParaToInt("id");
		Price price = Price.me.findById(id);
		setAttr("node", price);
		setAttr("id", id);
		renderFreeMarker("priceCon.html");

	}

	public void addorupdate() {
		String flag = "保存成功";
		try {

			int id = getParaToInt("id");
			int month = getParaToInt("month");
			int price = getParaToInt("price");
			String content = getPara("content");

			if (id != -1) {
				Price admin = Price.me.findById(id);

				admin.set("price", price);
				admin.set("month", month);
				admin.set("content", content);
				admin.update();
			} else {
				Price admin = new Price();
				admin.set("price", price);
				admin.set("month", month);
				admin.set("content", content);

				if (Price.me.existMonth(month)) // /重名
				{
					flag = "重复时间";

				} else {
					admin.save();
				}

			}
			renderText(flag);

		} catch (Exception e) {
			flag = "保存失败";
			renderText(flag);
		}

	}

}
