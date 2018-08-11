package com.cln.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.cln.handler.BusiHandler;
import com.cln.utils.StaticUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Ws2HttpServerHandler extends SimpleChannelInboundHandler<Object>
{

	private static final Logger log = Logger.getLogger(WebSocketServerHandshaker.class.getName());
	private WebSocketServerHandshaker handshaker;
	public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	//校巴http请求
	public static final String httpUri = "/shbhttp";
	//校巴ws请求
	public static final String wsUri = "/shb";
	//请求页面
	public static final String wsUrl = "ws://localhost:"+StaticUtils.SERVER_IP+wsUri;


	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof FullHttpRequest)
		{
			handleHttpRequest(ctx, ((FullHttpRequest) msg));
		}
		else if (msg instanceof WebSocketFrame)
		{
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
	{
		ctx.flush();
	}

	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg)
	{
		// 判断是否关闭链路的指令
		if (msg instanceof CloseWebSocketFrame)
		{
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
		}
		// 判断是否ping消息
		if (msg instanceof PingWebSocketFrame)
		{
			ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
			return;
		}
		// 本例程仅支持文本消息
		if (!(msg instanceof TextWebSocketFrame))
		{
			//System.out.println("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", msg.getClass().getName()));
		}

		// 原始数据
		String data = ((TextWebSocketFrame) msg).text();

		//ws消息处理
		dohandler(ctx, data, 0);
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req)
	{
		//uri
		String uri = req.uri();
		//是否校巴HTTP请求
		if(req.method() == HttpMethod.POST && httpUri.equals(uri))
		{
			// 原始数据
			//String data = req.content().toString();
			ByteBuf jsonBuf = req.content();
			byte[] xx = new byte[jsonBuf.readableBytes()];
			jsonBuf.readBytes(xx);
	        String data = new String(xx);
			//http消息处理
			dohandler(ctx, data, 1);
		}
		else
		{
			if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade"))))
			{
				sendHttpResponse(ctx, req,
						new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
				return;
			}
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(wsUrl, null, false);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null)
			{
				WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			}
			else
			{
				handshaker.handshake(ctx.channel(), req);
			}
		}
	}
	
	/**
	 * 消息处理
	 * @param ctx
	 * @param req
	 * @param reqType
	 */
	private static void dohandler(ChannelHandlerContext ctx, String data, int reqType)
	{
		// 会话ID
		String newChannelId = ctx.channel().id().asLongText();

		log.info("****************接收客户端消息****************channelId=" + newChannelId + ">>>>>>>>data=" + data);
		// 是否第一次建立连接
		boolean firstFlag = false;
		// 用户名,手机号码
		String userName = null;
		// 查询
		JSONObject reqData = new JSONObject();

		try
		{
			// 1.解析获取会话ID和userName等
			newChannelId = ctx.channel().id().asLongText();
			userName = ChannelHandler.getChannelUserName(ctx);

			// 2.当userName为空时,则为第一次建立连接
			if (data != null)
			{
				reqData = JSONObject.parseObject(data);
				// userName = reqData.getString("phone");
			}
			if (userName == null)
			{
				// TODO 获取用户名
				JSONObject jsonObject = reqData.getJSONObject("data");
				userName = jsonObject.getString("userPhone");
				if (StringUtils.isNotEmpty(userName))
				{
					firstFlag = true;
				}
			}

			// 3.ws通讯方式情况下将通道绑定到该用户名下面;
			if (firstFlag && reqType == 0)
			{
				ChannelHandler.addChannel(ctx, userName);
			}
			// 具体业务
			BusiHandler.doHandler(reqData, ctx, userName, reqType);
			// ctx.channel().writeAndFlush(new
			// TextWebSocketFrame("vvvvvvvvvvv-->userName="+userName));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXX请求处理失败(cmd=" + reqData.getString("cmd") + ",userName=" + userName
					+ ",channelId=" + newChannelId + ",data=" + data + "):" + ex.getMessage());
		}

	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res)
	{
		// 返回应答给客户端
		if (res.status().code() != 200)
		{
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.status().code() != 200)
		{
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static boolean isKeepAlive(FullHttpRequest req)
	{
		return false;
	}

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		// 添加
		group.add(ctx.channel());
		System.out.println("客户端与服务端连接开启");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		// sessionId
		String channelId = null;
		// 用户名
		String userName = null;
		
		try
		{
			// 移除
			group.remove(ctx.channel());
			
			if (ctx != null)
			{
				// 获取sessionId
				channelId = ctx.channel().id().asLongText();
				// 获取用户名
				userName = StaticUtils.USER_MAP.get(channelId);
				// 打印日志
				log.info("&&&&&&&&&&&&&&连接关闭&&&&&&&&&&&&&&(userName=" + userName + ",channelId=" + channelId + ")");
				// 删除id对应用户
				ChannelHandler.removeChannel(ctx);
			}
		}
		catch (Exception ex)
		{
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXX:" + ex.getMessage());
		}
	
	}

	
	// 每个channel都有一个唯一的id值
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception
	{
		String channelId = ctx.channel().id().asLongText();
		log.info("+++++++++++++++++++建立新连接+++++++++++++++@@@@@@@：channel=" + channelId + ",客户端地址="
				+ ctx.channel().remoteAddress());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
	{
		ChannelHandler.removeChannel(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		// sessionId
		String channelId = null;
		// 用户名
		String userName = null;
		try
		{
			if (ctx != null)
			{
				// 获取sessionId
				channelId = ctx.channel().id().asLongText();
				// 获取用户名
				userName = StaticUtils.USER_MAP.get(channelId);
				// 打印日志
				log.info("&&&&&&&&&&&&&&连接发生异常&&&&&&&&&&&&&&(userName=" + userName + ",channelId=" + channelId + ")"
						+ ",cause=" + cause.getMessage());
				// 删除id对应用户
				ChannelHandler.removeChannel(ctx);
			}
		}
		catch (Exception ex)
		{
			log.error("XXXXXXXXXXXXXXXXXXXXXXXXX:" + ex.getMessage());
		}
	}

}
