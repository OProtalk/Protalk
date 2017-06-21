package com.protalk.broker.resources;

import java.io.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP.*;
import org.eclipse.californium.core.server.resources.*;
import com.protalk.serial.*;

public class ReadonlyResource extends CoapResource {
	private Serial serial = null;
	private int REQ_ = 'H'; // HC: 라즈베리가 아두이노에 요청

	public ReadonlyResource(String _name, Serial _serial, int _req) {
		super(_name);
		this.serial = _serial;
		this.REQ_ = _req;
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		// TODO Auto-generated method stub
		try {
			serial.getOutputStream().write(REQ_);
			final CoapExchange exchange2 = exchange;
			new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(1000);
						//String str = new BufferedReader(new InputStreamReader(serial.getInputStream())).readLine(); // 한 줄 읽어 보내
						int chr = serial.getInputStream().read();
						if(chr == -1) {
							exchange2.respond(ResponseCode.NOT_FOUND, "can't get the value");
						} else {
							exchange2.respond(ResponseCode.CONTENT, "" + chr);						
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						exchange2.respond(ResponseCode.INTERNAL_SERVER_ERROR, "ERROR");
					}
				}
			}).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// static stuff below
	
	public static final int
	TYPE_HUMIDITY = 1,
	TYPE_TEMPERATURE = 2,
	TYPE_PPM = 3,
	TYPE_FLAME_DETCTION = 4,
	TYPE_DUST_SENSOR = 5;

	private static ReadonlyResourceFactory mFactory = null;
	
	public static ReadonlyResourceFactory getFactory() {
		if (mFactory == null) { // 1
			synchronized(ReadonlyResource.class) { // 2
				if (mFactory == null) { // 3
					mFactory = new ReadonlyResourceFactory();
				}
			}
		} // Double-Checked Locking
		return mFactory;
	} // func
	
	public static class ReadonlyResourceFactory {
		public ReadonlyResource create(int _type, Serial _serial) {
			switch (_type) {
			case ReadonlyResource.TYPE_HUMIDITY:
				return new ReadonlyResource("Humidity", _serial, 'H');
			case ReadonlyResource.TYPE_TEMPERATURE:
				return new ReadonlyResource("Temperature", _serial, 'T');
			case ReadonlyResource.TYPE_PPM:
				return new ReadonlyResource("PPM", _serial, 'P');
			case ReadonlyResource.TYPE_FLAME_DETCTION:
				return new ReadonlyResource("FlameDetection", _serial, 'F');
			case ReadonlyResource.TYPE_DUST_SENSOR:
				return new ReadonlyResource("DustSensor", _serial, 'U');
			default:
				return null;
			}
		} // constructor
	} // public static class

} // public class
