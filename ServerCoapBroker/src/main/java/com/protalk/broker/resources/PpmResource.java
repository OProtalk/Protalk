package com.protalk.broker.resources;

import com.protalk.serial.*;

public class PpmResource extends SensorResource {
	public PpmResource(Serial _serial) {
		super("PPM", _serial, 'P');
	}
}
