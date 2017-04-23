package com.protalk.broker.resources;

import com.protalk.serial.*;

public class DustSensorResource extends SensorResource {
	public DustSensorResource(Serial _serial) {
		super("DustSensor", _serial, 'U');
	}
}
