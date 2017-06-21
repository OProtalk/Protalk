package com.protalk.broker.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.protalk.serial.Serial;

public class DirectoryResource extends CoapResource {
	
	public DirectoryResource(String _name, Serial _serial) {
		// TODO Auto-generated constructor stub
		super(_name);
		onCreate(_serial);
	}
	
	protected void onCreate(Serial _serial) {} // hook
	
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
	
	// static stuff below
	
	public static final int
	TYPE_0_LIGHTS = 1,
	TYPE_0_WHEATHER = 2,
	TYPE_1_BEDROOM = 3,
	TYPE_1_BATHROOM = 4,
	TYPE_1_LIVING = 5,
	TYPE_1_KITCHEN = 6,
	TYPE_1_YARD = 7,
	TYPE_2_BEDROOM = 8,
	TYPE_2_KITCHEN = 9,
	TYPE_3_YARD = 10;	
	
	private static DirectoryResourceFactory mFactory = null;
	
	public static DirectoryResourceFactory getFactory() {
		if (mFactory == null) { // 1
			synchronized(DirectoryResource.class) { // 2 // DirectoryResource.mFactory 때문에.
				if (mFactory == null) { // 3
					mFactory = new DirectoryResourceFactory();
				}
			}
		} // Double-Checked Locking
		return mFactory;
	} // func
	
	public static class DirectoryResourceFactory {
		public DirectoryResource create(int _type, Serial _serial) {
			switch (_type) {
			case DirectoryResource.TYPE_0_LIGHTS:
				return new DirectoryResource("Lights", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().createLed(1, _serial));
						add(ControlableResource.getFactory().createLed(2, _serial));
						add(ControlableResource.getFactory().createLed(3, _serial));
					}
				};
			case DirectoryResource.TYPE_0_WHEATHER:
				return new DirectoryResource("Whether", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ReadonlyResource.getFactory().create(ReadonlyResource.TYPE_HUMIDITY, _serial));
						add(ReadonlyResource.getFactory().create(ReadonlyResource.TYPE_TEMPERATURE, _serial));
						add(ReadonlyResource.getFactory().create(ReadonlyResource.TYPE_PPM, _serial));
						add(ReadonlyResource.getFactory().create(ReadonlyResource.TYPE_FLAME_DETCTION, _serial));
					}
				};
			case TYPE_1_BEDROOM:
				return new DirectoryResource("Bedroom", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().createLed(1, _serial));
						add(ControlableResource.getFactory().createLed(2, _serial));
						add(ControlableResource.getFactory().createLed(3, _serial));
					}
				};
			case TYPE_1_BATHROOM:
				return new DirectoryResource("Bathroom", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().createLed(4, _serial));
						add(ControlableResource.getFactory().createLed(5, _serial));
					}
				};
			case TYPE_1_LIVING:
				return new DirectoryResource("Living", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().createLed(6, _serial));
						add(ControlableResource.getFactory().createLed(7, _serial));
					}
				};
			case TYPE_1_KITCHEN:
				return new DirectoryResource("Kitchen", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().createLed(8, _serial));
					}
				};
			case TYPE_1_YARD:
				return new DirectoryResource("Yard", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().createLed(9, _serial));
					}
				};
			case TYPE_2_BEDROOM:
				return new DirectoryResource("Bedroom", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().create(ControlableResource.TYPE_WINDOW, _serial));
					}
				};
			case TYPE_2_KITCHEN:
				return new DirectoryResource("Kitchen", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ReadonlyResource.getFactory().create(ReadonlyResource.TYPE_PPM, _serial));
					}
				};
			case TYPE_3_YARD:
				return new DirectoryResource("Yard", _serial) {
					@Override
					protected void onCreate(Serial _serial) {
						// TODO Auto-generated method stub
						super.onCreate(_serial);
						add(ControlableResource.getFactory().create(ControlableResource.TYPE_WINDOW, _serial));
					}
				};
			default:
				return null;
			}
		} // constructor
	} // public static class

} // public class
