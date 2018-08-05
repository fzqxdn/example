package com.cln.ws;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.cln.dubbo.constant.SchBusRespCode;
import com.cln.utils.StaticUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 静态方法类
 * 
 * @author Yxb
 * @version 1.0
 * @Date 2014-02-20
 */
public class ChannelHandler
{
	private static Logger log = Logger.getLogger(ChannelHandler.class.getName());


	/**
	 * 删除会话
	 * @param ctx
	 */
	public static void removeChannel(ChannelHandlerContext ctx)
	{
		//sessionId
		String channelId = null;
		//用户名
		String userName = null;
		try
		{
			if(ctx != null)
			{
				//获取sessionId
				channelId = ctx.channel().id().asLongText();
				//获取用户名
				userName = StaticUtils.USER_MAP.get(channelId);
				//删除id对应用户
				StaticUtils.USER_MAP.remove(channelId);
				
				if(userName != null)
				{
					//删除用户对应会话
					StaticUtils.SESSION_MAP.remove(userName);
				}

				//关闭连接
				ctx.close();
				ctx = null;
			}
			//后面获取连接的时候必须是否获取才行
			log.info("------------------删除会话------------------@@@@@@@：channelId=" + channelId+",userName="+userName);
		}
		catch(Exception ex)
		{
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXXremoveChannel删除会话(userName="+userName+",channelId="+channelId+")失败:"+ex.getMessage());
		}
	}
	
	
	/**
	 * 新连接处理
	 * @param ctx
	 */
	public static void addChannel(ChannelHandlerContext ctx, String userName)
	{
		//旧会话ID
		String oldChannelId = null;
		//会话ID
		String newChannelId = null;
		//添加会话标识
		boolean addFlag = false;
		
		try
		{
			//判空
			if(ctx != null && userName != null)
			{

				//1.获取新会话ID
				newChannelId = ctx.channel().id().asLongText();
				
				//2.找到原来的会话
				ChannelHandlerContext oldCtx = StaticUtils.SESSION_MAP.get(userName);
				if(oldCtx != null)
				{
					//3.获取原来的会话ID
					oldChannelId = oldCtx.channel().id().asLongText();

				}
				else
				{
					addFlag = true;
				}

				//4.如果旧的不为空,新的ID与旧ID不同,则删除
				if(oldChannelId != null && !oldChannelId.equals(newChannelId))
				{
					/**
					 * 1.删除原来用户对应的会话
					 */
					//删除对应关系
					StaticUtils.SESSION_MAP.remove(userName);
					//删除原来的会话
					removeChannel(oldCtx);
					
					addFlag = true;

				}
				
				if(addFlag)
				{
					/**
					 * 2.添加新的会话
					 */
					//将通道绑定到该用户名下面;
					if(userName != null && !"".equals(userName))
					{
						StaticUtils.SESSION_MAP.put(userName, ctx);
						StaticUtils.USER_MAP.put(newChannelId, userName);
					}
					log.info("&&&&&&&&&&&&&&&&&&&会话绑定&&&&&&&&&&&&&&&：channelId=" + newChannelId+",userName="+userName);
				}
			}
		}
		catch(Exception ex)
		{
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXXaddChannel建立新会话(userName="+userName+",channelId="+newChannelId+")失败:"+ex.getMessage());
		}
	}
	
	
	/**
	 * 根据会话获取用户
	 * @param ctx
	 * @return
	 */
	public static String getChannelUserName(ChannelHandlerContext ctx)
	{
		//sessionId
		String channelId = null;
		//用户名
		String userName = null;
		try
		{
			if(ctx != null)
			{
				//获取sessionId
				channelId = ctx.channel().id().asLongText();
				//获取用户名
				userName = StaticUtils.USER_MAP.get(channelId);
			}
			//后面获取连接的时候必须是否获取才行
			log.info("*******************根据会话获取用户@@@@@@@：channelId=" + channelId+",userName="+userName);
		}
		catch(Exception ex)
		{
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXXgetChannelUserName获取会话用户失败(userName="+userName+",channelId="+channelId+")失败:"+ex.getMessage());
		}
		
		return userName;
	}
	
	
	/**
	 * 封装处理失败了返回数据
	 */
	public static void sendErrorRsp(ChannelHandlerContext ctx, String cmd, String retCode, String userName,JSONObject jMsg)
	{
		try
		{
			String resp = "";
			if(StringUtils.isEmpty(cmd))
			{
				resp = "请求命令cmd为空";
			}
			if(StringUtils.isEmpty(userName))
			{
				resp = "请求用户名为空";
			}
			jMsg.put("status", SchBusRespCode.BUS_ERROR_CODE_9001.getCode());
			jMsg.put("msg", resp);
			resp = jMsg.toJSONString();
			ctx.channel().writeAndFlush(new TextWebSocketFrame(resp));
		}
		catch(Exception ex)
		{
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXXpackErrorData发送异常处理返回数据失败(cmd="+cmd+",userName="+userName+",channelId="+ctx.channel().id().asLongText()+",retCode="+retCode+"):"+ex.getMessage());
		}
	}

}
