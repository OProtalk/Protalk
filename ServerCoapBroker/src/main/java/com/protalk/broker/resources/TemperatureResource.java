package com.protalk.broker.resources;

import com.protalk.serial.*;

public class TemperatureResource extends SensorResource {
	public TemperatureResource(Serial _serial) {
		super("Temperature", _serial, 'T');
	}
}
