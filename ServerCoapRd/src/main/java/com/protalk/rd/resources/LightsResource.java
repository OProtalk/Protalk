package com.protalk.rd.resources;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.server.resources.*;

import com.protalk.serial.*;

public class LightsResource extends CoapResource {
	
	public LightsResource(Serial _serial) {
		super("Lights");
		add(new Led1Resource(_serial));
		add(new Led2Resource(_serial));
		add(new Led3Resource(_serial));
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		// TODO Auto-generated method stub
		super.handleGET(exchange);
	}
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		// TODO Auto-generated method stub
		super.handlePOST(exchange);
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {
		// TODO Auto-generated method stub
		super.handlePUT(exchange);
	}
	
	@Override
	public void handleDELETE(CoapExchange exchange) {
		// TODO Auto-generated method stub
		super.handleDELETE(exchange);
	}
	
} // public class
