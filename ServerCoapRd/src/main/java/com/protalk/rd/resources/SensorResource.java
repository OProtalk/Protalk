package com.protalk.rd.resources;

import java.io.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP.*;
import org.eclipse.californium.core.server.resources.*;

import com.protalk.serial.*;

public class SensorResource extends CoapResource {
	private Serial serial = null;
	private int REQ_ = 'H'; // HC: 라즈베리가 아두이노에 요청

	public SensorResource(String _name, Serial _serial, int _req) {
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
}
