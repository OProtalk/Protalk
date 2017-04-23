package com.protalk.broker.resources;

import com.protalk.serial.*;

public class HumidityResource extends SensorResource {
	public HumidityResource(Serial _serial) {
		super("Humidity", _serial, 'H');
	}
}
