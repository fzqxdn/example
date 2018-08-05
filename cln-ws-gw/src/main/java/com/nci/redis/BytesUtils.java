package com.nci.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;


public class BytesUtils implements Serializable
{
	private static Logger LOGGER = Logger.getLogger(BytesUtils.class);
	
	/**
	 * 
	 */
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
}
