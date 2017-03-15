package com.protalk.rd.resources;

import com.protalk.serial.*;

public class FlameDetectionResource extends SensorResource {
	public FlameDetectionResource(Serial _serial) {
		super("FlameDetection", _serial, 'F');
	}
}
