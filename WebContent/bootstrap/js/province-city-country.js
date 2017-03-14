/**
 * 
 * @param eleConfigs
 */
var provice = [];
var city = [];
var couty = [];
function provinceCityCounty(data) {

	provice = data[0];
	city = data[1];
	couty = data[2];

	genProvince(provice["ele"]["items"]); // 选择省后，级联选择相应的市

}

function genProvince(items) {
	var provinceSelect = document.getElementById("myprovince");
	for (var i = 0; i < items.length; i++) {
		var text = items[i]["text"];
		var id = items[i]["extendAttr"]["id"];
		var option = new Option(text);

		option.setAttribute("text", text);
		option.setAttribute("value", text);
		option.setAttribute("data-id", id);

		provinceSelect.add(option);

	}

}

/**
 * 省份选择后触发的方法
 */
function selectTargetCity() {

	$("#city_id").val("");//先清空值，防止多次搜索后有影响
	$("#country_id").val("");//先清空值，防止多次搜索后有影响
	
	
	var provinceSelect = document.getElementById("myprovince");

	var option = provinceSelect.options[provinceSelect.selectedIndex];
	var proId = option.getAttribute("data-id");
	// alert(proId);
	$("#province_id").val(proId);
	$("#province_id").text(proId);

	var citySelect = document.getElementById("mycity");
	$("#mycity").find("option").not(":first").remove(); // jquery 的东西
	$("#mycounty").find("option").not(":first").remove(); // jquery 的东西
	var items = city["ele"]["items"];
	for (var i = 0; i < items.length; i++) {
		var parentId = items[i]["extendAttr"]["parentId"];
		if (parentId == proId) {
			var text = items[i]["text"];
			var id = items[i]["extendAttr"]["id"];
			var option = new Option(text);
			option.setAttribute("text", text);
			option.setAttribute("value", text);
			option.setAttribute("data-id", id);
			option.setAttribute("data-parentid", parentId);
			citySelect.add(option);
		}

	}

}

function selectTargetCounty() {

	$("#country_id").val("");//先清空值，防止多次搜索后有影响

	
	var citySelect = document.getElementById("mycity");
	var option = citySelect.options[citySelect.selectedIndex];
	var cityId = option.getAttribute("data-id");

	$("#city_id").val(cityId);
	$("#city_id").text(cityId);

	var citycounty = document.getElementById("mycounty");
	$("#mycounty").find("option").not(":first").remove(); // jquery 的东西
	var items = couty["ele"]["items"];
	for (var i = 0; i < items.length; i++) {
		var parentId = items[i]["extendAttr"]["id"];
		if (parentId - cityId > 0 && parentId - cityId < 100) { // 县级市
			// 上级加了100，规则有点不同
			// 会和下一个市的重合
			// 按照第一列前四位判断比较合理
			var text = items[i]["text"];
			var id = items[i]["extendAttr"]["id"];
			var option = new Option(text);
			option.setAttribute("text", text);
			option.setAttribute("value", text);
			option.setAttribute("data-id", id);
			option.setAttribute("data-parentid", parentId);
			citycounty.add(option);
		}

	}

}

function selectTargetCountyNext() {

	var citymycounty = document.getElementById("mycounty");
	var option = citymycounty.options[citymycounty.selectedIndex];
	var contyId = option.getAttribute("data-id");

	$("#country_id").val(contyId);
	$("#country_id").text(contyId);

}

/**
 * 
 * 更改后的初始化动作 初始化，地区信息
 * 
 * 
 */
function intiPCC() {

	var province_id = $("#province_id").val();
	var city_id = $("#city_id").val();
	var country_id = $("#country_id").val();

	// 第一步 显出出来省级
	$("#myprovince option[data-id=" + province_id + "]").attr("selected", true); // 在入的时候还没被初始化？

	
	//第二步 显示出来 市级 先要进行数据设置
	selectTargetCity();
	$("#mycity option[data-id=" + city_id + "]").attr("selected", true); // 在入的时候还没被初始化？
	
	//第三步 显示出来 市级
	selectTargetCounty();
	$("#mycounty option[data-id=" + country_id + "]").attr("selected", true); // 在入的时候还没被初始化？
	$("#country_id").val(country_id);//最后加上一句话  前面哪一步导致了这个字段的消失

	
	
}
