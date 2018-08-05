package com.cln.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 静态方法类
 * 
 * @author LYP
 * @version 1.0
 * @Date 2014-02-20
 */
public class StaticMethod
{
	private static Logger log = Logger.getLogger(StaticMethod.class.getName());

	/**
	 * 去掉左边空格Map
	 * 
	 * @param args
	 */
	public static Map<String, String> getSpaceTrimMap(Map<String, String> map)
	{
		Map<String, String> retMap = new HashMap<String, String>();
		if (map != null && !map.isEmpty())
		{
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				String key = entry.getKey();
				String mapvalue = trim(entry.getValue());
				String value = null;

				value = mapvalue;
				retMap.put(key, value);
			}
		}
		return retMap;
	}

	/**
	 * 左右空格都去掉
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str)
	{
		if (str == null || str.equals(""))
		{
			return str;
		}
		else
		{
			return str.replaceAll("^[　 ]+|[　 ]+$", "");
		}
	}
	
	/**
	 * 位数不足，右补空格
	 * @param param
	 * @param len
	 * @return
	 */
	public static String getLenStr(String param, int len)
	{
		String ret = null;
		
		StringBuffer buf = new StringBuffer();
		
		int lens = 0;
		int strLen = 0;
		
		try 
		{
			
			if(param != null)
			{
				strLen = param.getBytes("GBK").length;
				lens = param.length();
				
				int loc = 0;
				
				for(int i=0; i<lens; i++)
				{
					String xx = param.substring(i, i+1);
					if(!xx.equals("0") && !xx.equals(" "))
					{
						loc = i++;
						strLen = param.substring(loc, lens).getBytes("GBK").length;
						break;
					}
				}
				
				param = param.substring(loc, lens);
				
				lens = param.length();
				
				buf.append(param);
			} 
			if(strLen<len)
			{
				for(int i=strLen; i<len; i++)
				{
					buf.append(" ");
				}
				
				ret = new String(buf);
			}
			else
			{
				ret = param;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * 设置日志
	 * 
	 * @return
	 */
	public static String locationLog()
	{
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		sb.append("##########").append(stacks[1].getClassName()).append("************").append(stacks[1].getMethodName()).append(":");

		return sb.toString();
	}

	/**
	 * 打印方法日志头
	 * 
	 * @return
	 */
	public static void printMethodStartLog()
	{
		// 如果为info模式
		if (log.isDebugEnabled())
		{
			StringBuffer sb = new StringBuffer();
			StackTraceElement[] stacks = new Throwable().getStackTrace();
			sb.append("##########").append(stacks[1].getClassName()).append("************").append(stacks[1].getMethodName()).append("start:");
			log.debug(sb.toString());
		}
	}

	/**
	 * 传入参数日志打印
	 * 
	 * @param args
	 */
	public static void printParamLog(Map<String, String> map)
	{
		// 如果为debug模式
		if (log.isDebugEnabled())
		{
			if (map != null && !map.isEmpty())
			{
				for (Map.Entry<String, String> entry : map.entrySet())
				{
					log.debug("$$$$$$$$$$$$$$$$$$$$$***" + entry.getKey() + "=" + entry.getValue());
				}
			}
		}
	}

	/**
	 * 传入参数日志打印
	 * 
	 * @param args
	 */
	public static void printParamLogs(Map<String, byte[]> map)
	{
		// 如果为debug模式
		if (log.isDebugEnabled())
		{
			if (map != null && !map.isEmpty())
			{
				for (Map.Entry<String, byte[]> entry : map.entrySet())
				{
					log.debug("$$$$$$$$$$$$$$$$$$$$$***" + entry.getKey() + "=" + entry.getValue());
				}
			}
		}
	}

	/**
	 * 获取调用代码的类名，方法名，代码行数
	 * 
	 * @return
	 */
	public static String getTraceInfo()
	{
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append("; number: ").append(stacks[1].getLineNumber());

		return sb.toString();
	}

	/**
	 * 获取调用代码的类名，方法名，代码行数
	 * 
	 * @return
	 */
	public static String logmark(int mark)
	{
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		if (mark == 0 && log.isDebugEnabled())
		{
			log.debug(sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append(": start>>").toString());
		}
		else
		{
			log.debug(sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append(": end<<").toString());
		}
		return sb.toString();
	}
	

	/**
	 * 预支付流水生成规则（D+2位合作商编号+yyyyMMddHHmmssSSS）
	 * 
	 * @param mercode
	 * @return
	 */
	public static String generMercode(String mercode)
	{
		String retString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String datatime = sdf.format(new Date());
		retString = "D" + mercode + datatime;
		return retString;
	}

	/**
	 * 生成时间（年月日时分秒）
	 * 
	 * @param mercode
	 * @return
	 */
	public static String generDataTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String datatime = sdf.format(new Date());
		return datatime;
	}

	/**
	 * 生成时间（年-月-日 时:分:秒）
	 * 
	 * @param mercode
	 * @return
	 */
	public static String getPreNDayTime(int nDay)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -nDay);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
	}
	
	/**
	 * 昨天（年月日）
	 * 
	 * @param mercode
	 * @return
	 */
	public static String getYesterday(int nDay)
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -nDay);
		return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
	}
	
	/**
	 * 生成时间（年月日）
	 * 
	 * @param mercode
	 * @return
	 */
	public static String generSvrDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String datatime = sdf.format(new Date());
		return datatime;
	}
	
	/**
	 * 生成时间（年月日）
	 * 
	 * @param mercode
	 * @return
	 */
	public static String generSvrTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String datatime = sdf.format(new Date());
		return datatime;
	}
	
	public static String getLastDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String datatime = sdf.format(new Date());
		return datatime;
	}


	/**
	  * java生成随机数字和字母组合
	  * @param length[生成随机数的长度]
	  * @return
	  */
	public static String getCharAndNumr(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) 
			{
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} 
			else if ("num".equalsIgnoreCase(charOrNum)) 
			{ 
				// 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val.toUpperCase();
	}
	
	/**
	 * 遍历Map<String, String> ，输出
	 * 
	 * @param obj
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static void printMap(Map<String, String> map) throws UnsupportedEncodingException
	{
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println(" ------------------ " + key + " = " + value );
		}
	}
	
	public static String addZeroForNum(String str, int strLength) 
	{
		int strLen = str.length();
		if (strLen < strLength) 
		
		{
		while (strLen < strLength) 
		{
		StringBuffer sb = new StringBuffer();
		sb.append("0").append(str);//左补0
		// sb.append(str).append("0");//右补0
		str = sb.toString();
		strLen = str.length();
		}
		}
		return str;
	}
	
	/**
	 * 解析JSON
	 * 
	 * @param json
	 * @param retMap
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> decodeJSONObject(JSONObject json, Map<String, String> retMap) throws JSONException
	{
		//Iterator<String> keys = json.keys();
		
		 for (Map.Entry<String, Object> entry : json.entrySet()) 
		 {
			 retMap.put(entry.getKey(), String.valueOf(entry.getValue()));
	     }
		
/*		String value = null;
		String key;
		while (keys.hasNext())
		{
			key = keys.next();
			value = String.valueOf(json.get(key));
			retMap.put(key, value);
		}*/
		
		
		return retMap;
	}
	
	public static JSONObject stringToJson(String str) throws JSONException
	{
		String aa = str.replace("{", "");
		aa = aa.replace("}", "");
		String[] values = aa.split(", ");
		JSONObject object = new JSONObject();
		for(String value : values) 
		{
			String[] entity = value.split("=");
			object.put(entity[0], entity[1]);
		}
		return object;
	}

}
