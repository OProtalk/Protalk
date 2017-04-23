package com.protalk.broker.resources;

import com.protalk.serial.*;

public class Led1Resource extends LedResource {
	public Led1Resource(Serial _serial) {
		// TODO Auto-generated constructor stub
		super("Led1", _serial, '1', 'Q', 'Z');
	}
} // public class
