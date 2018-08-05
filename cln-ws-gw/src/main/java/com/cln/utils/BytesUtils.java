package com.cln.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.log4j.Logger;

public class BytesUtils implements Serializable
{
	private static Logger LOGGER = Logger.getLogger(BytesUtils.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 数组转对
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object byteToObject(byte[] bytes)
	{
		java.lang.Object obj = null;
		
		try
		{
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		}
		catch (Exception e)
		{
			LOGGER.debug("ByteToObject>>translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 对象转字节数
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] ObjectToByte(java.lang.Object obj)
	{
		byte[] bytes = null;
		try
		{
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		}
		catch (Exception e)
		{
			LOGGER.debug("ObjectToByte>>translation" + e.getMessage());
			e.printStackTrace();
		}
		
		return bytes;
	}
	
	
	
	/**
	 * 遍历Map<String, byte[]> ，输出
	 * 
	 * @param obj
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static void printBMap(Map<String, byte[]> map) throws UnsupportedEncodingException
	{
		for (Map.Entry<String, byte[]> entry : map.entrySet())
		{
			String key = entry.getKey();
			String value = new String(entry.getValue(), "GB2312");
			System.out.println(key + " = " + value );
		}
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
			System.out.println(key + " = " + value );
		}
	}
}
