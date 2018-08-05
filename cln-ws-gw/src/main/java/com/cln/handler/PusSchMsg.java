package com.cln.handler;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cache.redis.RedisUtil;
import com.cln.dubbo.client.ServiceBeanUtils;
import com.cln.dubbo.common.util.DatesUtils;
import com.cln.dubbo.common.util.SysLog;
import com.cln.dubbo.constant.RedisNameSpache;
import com.cln.dubbo.model.CardCloudResult;
import com.cln.dubbo.msg.service.IShbPushMsgService;
import com.cln.dubbo.msg.service.IWebChatService;
import com.cln.utils.StaticUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
* Title: PusSchMsg
* Description: 向外推送消息
* @author jh
* @date 2018年7月13日
* 步骤: 1,定时从消息数据库中取消息
* 		2,放入redis的队列中
* 		3,间隔几秒,通过websocket通道检查用户是否在线
* 		4,用户在线,从redis中取出数据,进行推送
* 		5,推送成功,删除redis中记录
* 		6,保存到推送成功到日志表中
* 		7,当用户在线时,若数据已经过了有效期,则日志记录未推送失败
* 		8,当用户在线是,若消息未过有效期,且推送失败,redis中不删除,下次继续推送.直到成功推送,或过期后,记录到日志表中
 */
public class PusSchMsg
{

	//获取消息推送的业务对象
	private static IShbPushMsgService shbPushMsgService = ServiceBeanUtils.getIShbPushMsgService();
	
	private static String pushMsgInfoInterval = StaticUtils.SENTPUSHMSGINTERVAL;
	
	public static void excuteScheduleJob()
	{
		try
		{
			SysLog.info("**************开始向前端推送消息************************");

			// 1,站点信息定时缓存到redis,间隔时间,单位小时
			sheulder(pushMsgInfoInterval, new Runnable()
			{
				public void run()
				{
					//对消息进行推送
					pushMsgFromRedis();
				}
			});
		}
		catch (Exception e)
		{
			SysLog.error(e);
		}
	}
	
	
	/**
	 * Title: scheduleSavePushMsgInRedis
	 * Description:从redis的右边取出消息并进行推送
	 */
	private static void pushMsgFromRedis()
	{
		try
		{
			Set<String> msgSet = RedisUtil.zrang(RedisNameSpache.SCHBUSPUSHMSG, 0, -1);
			if (CollectionUtils.isEmpty(msgSet))
			{
				return;
			}
			for (String msgStr : msgSet)
			{
				//获取消息队列
				JSONObject msg = JSONObject.parseObject(msgStr);
				//添加原始数据,便于删除
				msg.put("msgInfo", msgStr);
				//判断消息是否在有效期内
				long nowTime = System.currentTimeMillis();
				Long startTime = msg.getLong("startTime");
				Long endTime = msg.getLong("endTime");
				//数据已经过期了
				if (nowTime>=endTime)
				{
					//消息推送失败,记录到日志表中
					SysLog.info("*******推送消息失效,该消息"+msg+"有效期为"+msg.getString("startTime")+"至"+msg.getString("endTime"));
					msg.put("msg", "expire");
					msg.put("status", "N");
					shbPushMsgService.insertPushMsgLog(msg);
					return;
				}
				//不在推送时间内
				else if (nowTime<=startTime)
				{
					return;
				}
				//对有效期内数据进行推送
				//1,登录手机号
				String  mainPhone = msg.getString("mainPhone");
				
				//2,通过登录的手机号获取连接的通道
				ChannelHandlerContext onlineCtx = StaticUtils.SESSION_MAP.get(mainPhone);
				//不在线,将消息重新放入redis中
				if (onlineCtx!=null)
				{
					//当前端收到消息后,会回调
					JSONObject pushData = new JSONObject();
					pushData.put("cmd", "1606022");
					pushData.put("protocolVer", "0.0.1");
					pushData.put("isEnc", "0");
					pushData.put("encVer", "0");
					pushData.put("termType", "0");
					pushData.put("equipModel", "3");
					pushData.put("status", "200");
					pushData.put("msg", "校巴公众号消息");
					pushData.put("data",msgStr);
					String jsonString = pushData.toJSONString();
					//页面推送
					onlineCtx.channel().writeAndFlush(new TextWebSocketFrame(jsonString));
					SysLog.info("*******用户:"+mainPhone+"页面消息推送成功!消息内容为:"+jsonString+"******");
				}
				
				//3,公众号推送
				IWebChatService webChatService = ServiceBeanUtils.getIWebChatService();
				CardCloudResult result = webChatService.sendTextMessageToUser(msg);
				if ("200".equals(result.getStatus()))
				{
					SysLog.info("*******用户:"+mainPhone+"公众号消息推送成功!消息内容为:"+msg+"******");
				}
				
				//4,短信推送,第一期未做
			}
		}
		catch (Exception e)
		{
			SysLog.error(e);
		}
	}
	
	/**
	 * Title:executeSaveJob
	 * Description:创建定时任务执行
	 */
	private static void sheulder(String Interval,Runnable runnable)
	{
		try
		{
			if (StringUtils.isEmpty(Interval)||"-1".equals(Interval))
			{
				return;
			}
			//执行的间隔时间
			long intervalTime = Long.valueOf(Interval);			
			//创建定时线程,线程池1个
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(runnable, 0L, intervalTime, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			SysLog.error(e);
		}
	}
	
}
