package com.pureTec.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import com.pureTec.about.About;
import com.pureTec.about.AboutController;
import com.pureTec.ad.Ad;
import com.pureTec.ad.AdController;
import com.pureTec.admin.Admin;
import com.pureTec.admin.AdminController;
import com.pureTec.behavior.Behavior;
import com.pureTec.behavior.BehaviorController;
import com.pureTec.course.Course;
import com.pureTec.course.CourseController;
import com.pureTec.course.CourseData;
import com.pureTec.customer_server.CustomerServer;
import com.pureTec.device.Device;
import com.pureTec.device.DeviceController;
import com.pureTec.favorite.Favorite;
import com.pureTec.favorite.FavoriteController;
import com.pureTec.index.IndexController;
import com.pureTec.price.Price;
import com.pureTec.price.PriceController;
import com.pureTec.purchase.ActiveCode;
import com.pureTec.purchase.Purchase;
import com.pureTec.purchase.PurchaseController;
import com.pureTec.question.QuestionController;
import com.pureTec.question.QuestionNode;
import com.pureTec.suggest.Suggest;
import com.pureTec.suggest.SuggestController;
import com.pureTec.user.MCToken;
import com.pureTec.user.User;
import com.pureTec.user.UserController;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("/res/a_little_config.txt");//本地地址
//		PropKit.use("a_little_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setViewType(ViewType.FREE_MARKER);
		
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/course", CourseController.class, "WEB-INF/course");
//		me.add("/user", UserController.class, "inspinia");
//		me.add("/Angular", UserController.class, "Angular");
	////问答系统   
		//url访问路径  方法类 网页代码位置
        me.add("/question",QuestionController.class,"WEB-INF/question");
        me.add("/user",UserController.class,"WEB-INF/user");       
        me.add("/admin",AdminController.class,"WEB-INF/admin");
        me.add("/",AdminController.class,"WEB-INF/admin");
		me.add("/ad",AdController.class,"WEB-INF/ad");
		me.add("/behavior",BehaviorController.class,"WEB-INF/behavior");
		me.add("/purchase",PurchaseController.class,"WEB-INF/purchase");
		me.add("/about",AboutController.class,"WEB-INF/about");
		me.add("/device",DeviceController.class,"WEB-INF/device");	
		me.add("/price",PriceController.class,"WEB-INF/price");
		me.add("/suggest",SuggestController.class,"WEB-INF/about");
		me.add("/favorite",FavoriteController.class,"WEB-INF/favorite");
		me.add("/homepage",IndexController.class,"WEB-INF/common");
		me.add("/cs",CustomerServer.class,"WEB-INF/customer_server");
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(c3p0Plugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);

	//	arp.addMapping("device", Device.class); 
	//	arp.addMapping("device_data", DeviceData.class); 
		arp.addMapping("course", Course.class); 
		arp.addMapping("course_data", CourseData.class); 
		
		arp.addMapping("questionmsg", QuestionNode.class);
		arp.addMapping("user", User.class);
		arp.addMapping("admin", Admin.class);
		arp.addMapping("ad",Ad.class);
		arp.addMapping("behavior", Behavior.class);
		arp.addMapping("purchase",Purchase.class);
		arp.addMapping("about",About.class);
		arp.addMapping("active_code",ActiveCode.class);
		arp.addMapping("device",Device.class);
//		me.add(new EhCachePlugin());	
		
		
		arp.addMapping("pricelist",Price.class);
		arp.addMapping("suggestion",Suggest.class);
		
		arp.addMapping("favorite",Favorite.class);
		arp.addMapping("token", MCToken.class);// 映射duty 表
		RedisPlugin trf;
	}

	
	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		super.afterJFinalStart();
		
		
		//FreeMarkerRender 写在这里才能生效
		// 模板更更新时间,1秒扫描一次，正式运行的时候改为3600 因为freemarker把模板导入内存，减少文件的读取
		FreeMarkerRender.setProperty("template_update_delay", "1");
//		FreeMarkerRender.setProperty("template_update_delay", "3600");// 模板更更新时间,1秒扫描一次，正式运行的时候改为3600
		FreeMarkerRender.setProperty("classic_compatible", "true");// 如果为null则转为空值,不需要再用!处理
		FreeMarkerRender.setProperty("whitespace_stripping", "true");// 去除首尾多余空格
		FreeMarkerRender.setProperty("date_format", "yyyy-MM-dd");
		FreeMarkerRender.setProperty("time_format", "HH:mm:ss");
		FreeMarkerRender.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
		FreeMarkerRender.setProperty("default_encoding", "UTF-8");
		
		
		
		
//		BaseConf.proviceCityCounty =  ProvinceCity.me.findProvice();	 //开机载入省市区信息	

		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}
	
	
//	/**
//	 * 配置全局拦截器
//	 */
//	public void configInterceptor(Interceptors me) {
//
//	}
//
//	/**
//	 * 配置处理器
//	 */
//	public void configHandler(Handlers me) {
//
//	}
//
//	/**
//	 * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
//	 */
//	public static void main(String[] args) {
//		JFinal.start("WebRoot", 80, "/", 5);
//	}
}
