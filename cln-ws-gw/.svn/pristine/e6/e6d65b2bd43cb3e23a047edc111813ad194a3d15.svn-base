package com.cln.handler;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.netty.channel.ChannelHandlerContext;

public class Sender extends Thread
{

	public static ConcurrentLinkedQueue<ChannelHandlerContext> contextQueue = new ConcurrentLinkedQueue<ChannelHandlerContext>();

	public static ConcurrentLinkedQueue<ChannelHandlerContext> newContextQueue = new ConcurrentLinkedQueue<ChannelHandlerContext>();


	public void run()
	{
		// 0=M1,1=cpu,2=银行卡,3=不清楚,195=二维码
		while (true)
		{
			try
			{
				Thread.sleep(3000l);
				System.out.println(111);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			
		}
	}

	public static void main(String[] args)
	{
//		new Thread(new Sender()).start();
		new Sender().start();
	}
	
}
