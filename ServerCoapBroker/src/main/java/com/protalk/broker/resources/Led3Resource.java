package com.protalk.broker.resources;

import com.protalk.serial.*;

public class Led3Resource extends LedResource {
	public Led3Resource(Serial _serial) {
		// TODO Auto-generated constructor stub
		super("Led3", _serial, '3', 'E', 'C');
	}
}

