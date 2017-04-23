package com.protalk.broker.resources;

import com.protalk.serial.*;

public class Led2Resource extends LedResource {
	public Led2Resource(Serial _serial) {
		// TODO Auto-generated constructor stub
		super("Led2", _serial, '2', 'W', 'X');
	}
}
