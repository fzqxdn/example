package com.cln.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * 返回码
 * 
 * @author LYP
 * @version 1.0
 * @Date 2014-02-20
 */
public class RetCode
{
	
	private static Map<String, String> retCodeDescription = new HashMap<String, String>();
	
	
	public static String getRetCodeDescription(String retCode) throws IllegalArgumentException, IllegalAccessException
	{
		if (retCodeDescription.isEmpty())
		{
			init(RetCode.class);
		}
		return retCodeDescription.get(retCode);
	}
	
	private static void init(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException
	{
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
		{
			if (f.isAnnotationPresent(AnnotationColumn.class))
			{
				AnnotationColumn annotationColumn = f.getAnnotation(AnnotationColumn.class);
				retCodeDescription.put(f.get(clazz).toString(), annotationColumn.value());
			}
		}
	}
	
	/**
	 * 返回成功
	 */
	@AnnotationColumn("成功")
	public static String RETCODE_SUCC = "0";
	
	/**
	 * 请求码错误
	 */
	@AnnotationColumn("请求码错误")
	public static String CMD_CODE_ERROR = "10010102";
	
	/**
	 * 业务模块程序异常
	 */
	@AnnotationColumn("业务模块程序异常")
	public static String BUSI_HANDLER_ERROR = "200";
	
	/**
	 * 用户名不合法
	 */
	@AnnotationColumn("用户名不合法")
	public static String RETCODE_USERNAME_FAIL = "201";
	
	/**
	 * 用户卡不合法
	 */
	@AnnotationColumn("用户卡不合法")
	public static String RETCODE_USERCARD_FAIL = "202";
	
	/**
	 * 商户不合法
	 */
	@AnnotationColumn("商户不合法")
	public static String RETCODE_MER_FAIL = "203";
	
	/**
	 * 交易金额不合法
	 */
	@AnnotationColumn("交易金额不合法")
	public static String RETCODE_MONEY_FAIL = "204";
	
	/**
	 * 业务流水不存在
	 */
	@AnnotationColumn("业务流水不存在")
	public static String RETCODE_BUSIFLOWID_IS_NULL = "205";
	/**
	 * 业务代码不合法
	 */
	@AnnotationColumn("业务代码不合法")
	public static String RETCODE_BUSICODE_FAIL = "206";
	
	/**
	 * 安全模块验证MAC1失败
	 */
	@AnnotationColumn("安全模块验证MAC1失败")
	public static String RETCODE_VAL_MAC1_FAIL = "207";
	
	/**
	 * 记录订单信息异常
	 */
	@AnnotationColumn("记录订单信息异常")
	public static String RETCODE_SAVEORDER_ERROR = "208";
	
	/**
	 * 封装下单返回信息异常
	 */
	@AnnotationColumn("封装下单返回信息异常")
	public static String RETCODE_RETORDER_ERROR = "209";
	
	/**
	 * 下订单异常
	 */
	@AnnotationColumn("下订单异常")
	public static String RETCODE_ORDER_ERROR = "210";
	
	/**
	 * 中心流水号不存在
	 */
	@AnnotationColumn("中心流水号不存在")
	public static String RETCODE_BUSIFLOW_FAIL = "211";
	
	/**
	 * 获取交易明细表最大中心流水号异常
	 */
	@AnnotationColumn("获取交易明细表最大中心流水号异常")
	public static String RETCODE_MAXBUSIFLOW_FAIL = "212";
	
	/**
	 * 商户离线账户金额不足
	 */
	@AnnotationColumn("商户离线账户金额不足")
	public static String RETCODE_MERACC_MONEY_FAIL = "213";
	
	/**
	 * 城市代码不合法
	 */
	@AnnotationColumn("城市代码不合法")
	public static String RETCODE_CITY_FAIL = "214";
	
	/**
	 * 在线支付第一步异常
	 */
	@AnnotationColumn("在线支付第一步异常")
	public static String RETCODE_FIRST_ERROR = "215";
	
	/**
	 * 在线支付第二步异常
	 */
	@AnnotationColumn("在线支付第二步异常")
	public static String RETCODE_SECOND_ERROR = "216";
	
	/**
	 * 修改交易流水表状态为在线支付第一步开始时异常
	 */
	@AnnotationColumn("修改交易流水表状态为在线支付第一步开始时异常")
	public static String RETCODE_UPDATEFIRST_ERROR = "217";
	
	/**
	 * 返回字段缺失 缺少必须字段
	 */
	@AnnotationColumn("返回字段缺失 缺少必须字段")
	public static String RETCODE_MISSING = "218";
	
	/**
	 * 超出单笔限额
	 */
	@AnnotationColumn("超出单笔限额")
	public static String RETCODE_OVER_LIMIT = "219";
	
	/**
	 * 判断同一台手机刷卡数量异常
	 */
	@AnnotationColumn("判断同一台手机刷卡数量异常")
	public static String RETCODE_USE_CARD_ERROR = "220";
	
	/**
	 * 同一台手机同一天内已超过限定刷卡数量
	 */
	@AnnotationColumn("同一台手机同一天内已超过限定刷卡数量")
	public static String RETCODE_ONE_CARD_FAIL = "221";
	
	/**
	 * 同一台手机多天内已超过限定刷卡数量
	 */
	@AnnotationColumn("同一台手机多天内已超过限定刷卡数量")
	public static String RETCODE_MORE_CARD_FAIL = "222";
	
	/**
	 * 互联互通交易明细交易流水不存在
	 */
	@AnnotationColumn("互联互通交易明细交易流水不存在")
	public static String RETCODE_BUSIFLOWHLHT_IS_NULL = "223";
	
	
	/**
	 * 查询表结构失败
	 */
	@AnnotationColumn("查询表结构失败")
	public static String TABLE_STRUCT_QUERY_FAIL = "500";
	
	/**
	 * 封装SQL语句失败
	 */
	@AnnotationColumn("封装SQL语句失败")
	public static String SQL_PACK_FAIL = "501";
	
	/**
	 * SQL预处理放置失败
	 */
	@AnnotationColumn("SQL预处理放置失败")
	public static String SQL_PREPARE_FAIL = "502";
	
	/**
	 * SQL语句为空或不存在
	 */
	@AnnotationColumn("SQL语句为空或不存在")
	public static String SQL_NOT_EXIST = "503";
	
	/**
	 * 数据库更新失败
	 */
	@AnnotationColumn("数据库更新失败")
	public static String DATA_UPDATE_FAIL = "504";
	
	/**
	 * 系统配置表无数据
	 */
	@AnnotationColumn("系统配置表无数据")
	public static String RC_703 = "703";

	/**
	 * 查询系统配置表异常
	 */
	@AnnotationColumn("查询系统配置表异常")
	public static String RC_702 = "702";
	
	/**
	 * 数据库更新异常
	 */
	@AnnotationColumn("数据库更新异常")
	public static String RC_706 = "706";
	
		
}
