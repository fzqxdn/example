package com.cln.dubbo.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cln.dubbo.base.service.IAreaService;
import com.cln.dubbo.model.CardCloudResult;
import com.cln.dubbo.model.Security;
import com.cln.dubbo.msg.service.IShbPushSetService;

@Component
public class ConsumerClient implements Runnable
{

	ClassPathXmlApplicationContext context = ServiceBeanUtils.getContext();
	
	/*
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		//ProcessService demoService = StaticUtils.getService();
	
		// invocation
		Long startTime = null;
		
		JSONObject param = new JSONObject();
		param.put("mainPhone", "13712265043");
		param.put("id", "1");
		param.put("msgType", "1");
		
		IShbPushSetService shbPushSetService = ServiceBeanUtils.getIShbPushSetService();
		CardCloudResult selectAllProvince = shbPushSetService.updateShbPushSet(param);
		System.out.println("&&&&&&&&&&&&&&响应值="+selectAllProvince);
		
		
/*		for(int i=0; i<10; i++)
		{
			String task = i+"_"+Thread.currentThread().getId();
			// proxy
			String hello = demoService.deal(task); // do invoke!
			if(!hello.equals(task))
			{
				System.out.println(task + "^^^" + hello);
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				break;
			}
			else
			{
				//System.out.println(task + "<>" + hello);
			}
			
			if(startTime == null)
			{
				startTime = System.currentTimeMillis();
			}
			System.out.println(Thread.currentThread().getName() + " " + hello);
			
			//System.out.println(System.currentTimeMillis()-startTime);
			
			
			System.out.println(task + "<>" + hello);
			
			
			if(i%200 == 0)
			{
				System.out.println(task + "<>" + hello);
			}
		}
		
		System.out.println("mybatis="+demoService.getBusData().toString());
		
		
		MathService bidService = StaticUtils.getMathService();
		
		
		
		Integer a = 1;
		Integer b = 2;
		User user = new User(11, "测试", "hello word !");
		
		//System.err.println("注入的是同一个对象：" + bidService.equals(service));
		System.err.printf("%s+%s=%s", a, b, bidService.add(a, b));
		System.err.println();
		System.err.printf("list=%s", bidService.toList(1, "22", true, 'b', user));
		System.err.println();
		System.err.println(bidService.getUser(user));
		System.err.println(bidService.excute2());
		try
		{
			bidService.throwThrowable();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
*/
	}
		

	public static void main(String[] args)
	{
/*		for(int i=0; i<1; i++)
		{
			new Thread(new ConsumerClient()).start();
		}*/
		
//		JSONObject object = new JSONObject();
//		
//		object.put("cmd", "1001011");
//		object.put("phone", "13752645261");
//		
//		System.out.println(object.toJSONString());
		new Thread(new ConsumerClient()).start();
	}
}