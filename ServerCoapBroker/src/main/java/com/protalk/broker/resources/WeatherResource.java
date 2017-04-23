package com.protalk.broker.resources;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.server.resources.*;

import com.protalk.serial.*;

public class WeatherResource extends CoapResource {
	
	public WeatherResource(Serial _serial) {
		super("Weather");
		add(new HumidityResource(_serial));
		add(new TemperatureResource(_serial));
		add(new PpmResource(_serial));
		add(new FlameDetectionResource(_serial));
//		add(new DustSensorResource(_serial));
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
	
}
