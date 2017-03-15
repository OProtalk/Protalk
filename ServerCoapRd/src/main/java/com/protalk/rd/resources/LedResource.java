package com.protalk.rd.resources;

import java.io.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP.*;
import org.eclipse.californium.core.server.resources.*;

import com.protalk.serial.*;

public class LedResource extends CoapResource {

	private Serial serial = null;
	private int ON_ = '1'; // 123: 켜짐
	private int OFF_ = 'Q'; // QWE: 꺼짐
	private int REQ_ = 'Z'; // ZXC: 라즈베리가 아두이노에 요청

	public LedResource(String _name, Serial _serial, int _on, int _off, int _req) {
		super(_name);
		this.serial = _serial;
		this.ON_ = _on;
		this.OFF_ = _off;
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
						int chr = serial.getInputStream().read();
						if(chr == '1') {
							exchange2.respond(ResponseCode.CONTENT, "ON");
						} else if (chr == 'Q') {
							exchange2.respond(ResponseCode.CONTENT, "OFF");
						} else {
							exchange2.respond(ResponseCode.INTERNAL_SERVER_ERROR, "ERROR1: your led node might not be connected to the server");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						exchange2.respond(ResponseCode.INTERNAL_SERVER_ERROR, "ERROR2");
					}
				}
			}).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handlePUT(CoapExchange exchange) {
		// TODO Auto-generated method stub
		try {
			System.out.println(exchange.getSourceAddress().toString());
			System.out.println(exchange.getRequestText());
			if (exchange.getRequestText().equals("ON")) {
				serial.getOutputStream().write(ON_);
				exchange.respond(ResponseCode.CHANGED, "light turned on");
				changed();
			} else if (exchange.getRequestText().equals("OFF")) {
				serial.getOutputStream().write(OFF_);
				exchange.respond(ResponseCode.CHANGED, "light turned off");
				changed();
			} else {
				exchange.respond(ResponseCode.BAD_REQUEST, "wrong payload");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR, "ioexcption");
			e.printStackTrace();
		}
	} // func

}
