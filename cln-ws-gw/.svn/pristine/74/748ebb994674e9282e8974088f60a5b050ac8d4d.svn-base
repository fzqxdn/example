package com.cln.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.ChannelHandlerContext;



/**
 * Description 静态变量配置类
 * 
 * @author LYP
 * @version 1.0
 * @Date 2014-02-20
 */
public class StaticUtils
{
	
	/**
	 * 读取端口号
	 */
	public static final int SERVER_IP = Integer.valueOf(Configration.getProperty("server.port")) ;
	
	/**
	 * 不要进行拦截的接口
	 */
	public static final String EXCLUDEMAPPING = Configration.getProperty("exclude-mapping");
	
	/**
	 * 推送消息的时间间隔
	 */
	public static final String SENTPUSHMSGINTERVAL = Configration.getProperty("sentPushMsg-interval");
	
	/**
	 * 通过用户登录手机号记录响应的socket接口
	 */
	public static ConcurrentMap<String, ChannelHandlerContext> SESSION_MAP = new ConcurrentHashMap<String, ChannelHandlerContext>();
	
	/**
	 * 根据自己ID找到是对应用户
	 */
	public static ConcurrentMap<String, String> USER_MAP = new ConcurrentHashMap<String, String>();
	
	/**
	 * 从Redis数据库获取需发送消息
	 */
	public static ConcurrentMap<String, String> NEW_MSG_MAP = new ConcurrentHashMap<String,String>();
	
	/**
	 * 本地需发送消息
	 */
	public static ConcurrentMap<String, String> MSG_MAP = new ConcurrentHashMap<String,String>();
	
	/**
	 * 推送消息队列
	 */
	public static ConcurrentLinkedQueue<Map<String, String>> MSG_QUEUE = new ConcurrentLinkedQueue<Map<String,String>>();
	
	/**
	 * 需推送用户信息队列
	 */
	public static ConcurrentLinkedQueue<Map<String, String>> USER_QUEUE = new ConcurrentLinkedQueue<Map<String,String>>();
	
	/**
	 * 需推送用户信息
	 */
	public static ConcurrentMap<String, Map<String, String>> LS_USER_MAP = new ConcurrentHashMap<String, Map<String,String>>();
	
	public static void main(String[] args)
	{
		System.out.println(StaticUtils.EXCLUDEMAPPING);
	}
}
