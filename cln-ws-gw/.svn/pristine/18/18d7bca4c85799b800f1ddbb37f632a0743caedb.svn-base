package com.cln.handler;



import com.cln.ws.WsFrameHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

public class WsChannelInitializer extends ChannelInitializer<SocketChannel>
{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		//管道
		ChannelPipeline pipeline = ch.pipeline();
		// websocket协议本身是基于http协议的，所以这边也要使用http解编码器
		pipeline.addLast(new HttpServerCodec());
		// 以块的方式来写的处理器
		pipeline.addLast(new ChunkedWriteHandler());
		// netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
		pipeline.addLast(new HttpObjectAggregator(65536));
		// 设置读写空闲监听器，避免死链接
		pipeline.addLast(new ReadTimeoutHandler(10));
		pipeline.addLast(new WriteTimeoutHandler(10));

		// 参数指的是contex_path
		pipeline.addLast(new WebSocketServerProtocolHandler("/shb"));
		// websocket定义了传递数据的6中frame类型
		pipeline.addLast(new WsFrameHandler());

	}
}