package com.protalk.broker.resources;

import java.io.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP.*;
import org.eclipse.californium.core.server.resources.*;

import com.protalk.serial.*;

public class ControlableResource extends CoapResource {

	private Serial serial = null;
	private int ON_ = '1'; // 123: 켜짐
	private int OFF_ = 'Q'; // QWE: 꺼짐
	private int REQ_ = 'Z'; // ZXC: 라즈베리가 아두이노에 요청

	public ControlableResource(String _name, Serial _serial, int _on, int _off, int _req) {
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
						//Thread.sleep(1000); // 1초 기다림
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

	// static stuff below
	
	public static final int
	TYPE_WINDOW = 1;
	public static final int
	TYPE_WINDOW2 = 3;
	
	private static ControlableResourceFactory mFactory = null;
	
	public static ControlableResourceFactory getFactory() {
		if (mFactory == null) { // 1
			synchronized(ControlableResource.class) { // 2
				if (mFactory == null) { // 3
					mFactory = new ControlableResourceFactory();
				}
			}
		} // Double-Checked Locking
		return mFactory;
	} // func
	
	public static class ControlableResourceFactory {
		public ControlableResource create(int _type, Serial _serial) {
			switch (_type) {
			case TYPE_WINDOW:
				return new ControlableResource("Window", _serial, 'H', 'L', 'l');
			case TYPE_WINDOW2:
				return new ControlableResource("Window", _serial, 'R', 'T', 'l');
			default:
				return null;
			}
		}
		public ControlableResource createLed(int _index, Serial _serial) {
			switch (_index) {
			case 1:
				return new ControlableResource("Led1", _serial, '1', 'Q', 'q');
			case 2:
				return new ControlableResource("Led2", _serial, '2', 'W', 'w');
			case 3:
				return new ControlableResource("Led3", _serial, '3', 'E', 'e');
			case 4:
				return new ControlableResource("Led4", _serial, '4', 'A', 'a');
			case 5:
				return new ControlableResource("Led5", _serial, '5', 'S', 's');
			case 6:
				return new ControlableResource("Led6", _serial, '6', 'Z', 'z');
			case 7:
				return new ControlableResource("Led7", _serial, '7', 'X', 'x');
			case 8:
				return new ControlableResource("Led8", _serial, '8', 'I', 'i');
			case 9:
				return new ControlableResource("Led9", _serial, '9', 'O', 'o');
			default:
				return new ControlableResource("Led" + _index, _serial,
						Character.forDigit(_index, 10), ' ', ' ');
			} // switch
		} // constructor
	} // public static class

} // public class
