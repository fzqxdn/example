package com.cln.service.intercepter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cln.dubbo.client.ServiceBeanUtils;
import com.cln.dubbo.model.CardCloudResult;
import com.cln.dubbo.user.service.SecurityService;
import com.cln.utils.StaticUtils;

/**
* Title: Intercepter
* Description: 接口拦截器
* @author jh
* @date 2018年6月25日
 */
public class Intercepter
{
	
	/**
	 * Title: excludeMapping
	 * Description:判断该接口是否需要进行校验
	 * @param json
	 * @return
	 */
	public static boolean isCheckMapping(JSONObject jMsg)
	{
		//请求接口类型
		String cmd = jMsg.getString("cmd");
		//不需要进行权限鉴定的接口
		String excludeMapping = StaticUtils.EXCLUDEMAPPING;
		//是否进行拦截
		boolean flag = true;
		if(StringUtils.isNotEmpty(excludeMapping))
		{
			String[] split = excludeMapping.split(",");
			//包含在里面不进行权限校验
			flag = !ArrayUtils.contains(split, cmd);
		}
		return flag;
//		return false;
	}
	
	/**
	 * Title: intercepter
	 * Description:对部分接口请求进行权限鉴定
	 */
	public static CardCloudResult authChek(JSONObject jMsg)
	{
		if(isCheckMapping(jMsg))
		{
			//参数json
			JSONObject param = jMsg.getJSONObject("data");
			SecurityService securityService = ServiceBeanUtils.getSecurityService();
			CardCloudResult authCheck = securityService.authCheck(param);
			authCheck.setData(jMsg.toJSONString());
			return authCheck;
		}
		return CardCloudResult.success();
	}

	
	
	/**
	 * Title: signalLogin
	 * Description:单点登录校验
	 * @param json
	 */
	public static CardCloudResult signalLogin(JSONObject jMsg)
	{
		if(isCheckMapping(jMsg))
		{
			//参数json
			JSONObject param = jMsg.getJSONObject("data");
			SecurityService securityService = ServiceBeanUtils.getSecurityService();
			return securityService.signalLogin(param);
		}
		return CardCloudResult.success();
	}
	

	
}
