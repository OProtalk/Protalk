package com.protalk.broker.resources.examples;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP.*;
import org.eclipse.californium.core.server.resources.*;

// immature
public class AnotherResource extends CoapResource {
	public AnotherResource() {
		super("JustAnotherResource"); // 리소스 이름 설정한다. 이 자원에 대한 주소는 이런 식이 될 것이다. coap://[2005::ba27:ebff:fe3c:ba5a]:5683/SayHello
	} // func

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond("Hey.");
	} // func

	@Override
	public void handleDELETE(CoapExchange exchange) {
		// TODO Auto-generated method stub
		delete();
        exchange.respond(ResponseCode.DELETED); 
	}

} // public class

