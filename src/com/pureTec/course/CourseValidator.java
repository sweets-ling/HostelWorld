package com.pureTec.course;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.pureTec.common.ResultData;

/**
 * MemberValidator.
 */
public class CourseValidator extends Validator {

	// 输入验证
	protected void validate(Controller controller) {

		String actionKey = getActionKey();
		if (actionKey.equals("/member/login")) {

			validateRequiredString("phone", "phoneMsg", "请输入手机号码");
			validateRequiredString("key", "keyMsg", "请输入手机号码");
		} else if (actionKey.equals("/member/register")) {

			validateRequiredString("phone", "phoneMsg", "请输入手机号码");
			validateRequiredString("key", "keyMsg", "请输入手机号码");
			validateRequiredString("business_name", "business_nameMsg",
					"请输入店铺名称");

		}
	}

	// 错误处理
	protected void handleError(Controller controller) {
		controller.keepModel(Course.class); // 保留已经输入的字段,用户无需输入已经输入字段,这个在移动app
											// 可不用

		String actionKey = getActionKey();
		ResultData resultData = new ResultData();

		if (actionKey.equals("/member/login")) {

			resultData.setCode(-2);
			if (controller.getAttr("phoneMsg") != null) {
				resultData.setError("请输入手机号码");
			}
			if (controller.getAttr("keyMsg") != null) {
				resultData.setError("请输入密码");
			}

		} else if (actionKey.equals("/member/register")) {
			resultData.setCode(-2);
			if (controller.getAttr("phoneMsg") != null) {
				resultData.setError("请输入手机号码");
			}
			if (controller.getAttr("keyMsg") != null) {
				resultData.setError("请输入密码");
			}
			if (controller.getAttr("business_nameMsg") != null) {
				resultData.setError("请输入密店铺名称");
			}
		}
		controller.renderJson(resultData);

	}
}
