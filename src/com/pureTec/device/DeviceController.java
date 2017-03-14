package com.pureTec.device;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.pureTec.user.User;
@Before(DeviceInterceptor.class)
public class DeviceController extends Controller{
	/**
	 * 将para转换为Device
	 * 
	 * @return
	 */
	public Device paraToDevice() {
		Device newDevice = new Device();
		newDevice.set("uid", getPara("uid"));
		newDevice.set("deviceid", getPara("deviceid"));
		newDevice.set("changetime", getParaToInt("changetime"));
		return newDevice;
	}

	/**
	 * list device
	 */
	public void list() {
		Page<Device> Devices = Device.me.paginate(getParaToInt("pageNumber", 1), 10);
		setAttr("data", Devices);
		setAttr("pageNumber", Devices.getPageNumber());
		setAttr("countPage", Devices.getTotalPage());
		renderFreeMarker("device_list.html");		
	}
	
	/**
	 * 修改页面映射
	 */
	public void updateDeviceIni() {
		int id = getParaToInt("uid");
		Device device=Device.me.findByUserId(id);
		User user=User.me.getUserById(id);
		setAttr("user",user);
		setAttr("data",device);
		renderFreeMarker("device_update.html");
	}
   /**
    * 保存更改并返回列表页面
    */
	public void updateDevice() {
		int uid=getParaToInt("uid");
		String deviceid=getPara("deviceid");
		int changetime=getParaToInt("changetime");
		Device.me.updateDevice(uid, deviceid, changetime);
		redirect("/device/list");
	}
	
	/**
	 * 根据用户过滤列表 select by user
	 */
	public void searchReport(){
		String keyword=getPara("keyword").toString();
		Page<Device> Devices = Device.me.selectByUser(getParaToInt("pageNumber", 1), 10,keyword);
		setAttr("data", Devices);
		setAttr("keyword",keyword);
		setAttr("pageNumber", Devices.getPageNumber());
		setAttr("countPage", Devices.getTotalPage());
		renderFreeMarker("device_list.html");
	}

}
