package com.protalk;

import java.net.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.network.*;
import org.eclipse.californium.core.network.config.*;
import org.eclipse.californium.core.server.resources.*;

public class RdServer extends CoapServer {
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

	// Constructor for a new Hello-World server initializes the resources of the server.
	public RdServer() throws SocketException {
		add(new HelloWorldResource()); // provide an instance of a Hello-World resource
	} // func

	private final class HelloWorldResource extends CoapResource {
		public HelloWorldResource() {
			super("helloWorld"); // set resource identifier
			getAttributes().setTitle("Hello-World Resource"); // set display name
		} // func

		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond("1. Hello World!"); // respond to the request
			exchange.respond("2. just another hello world respond");
		} // func

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

		@Override
		public void handleRequest(Exchange exchange) {
			// TODO Auto-generated method stub
			super.handleRequest(exchange);
		}
		
	} // class

	// Add individual endpoints listening on default CoAP port on all IPv6 addresses of all network interfaces.
	private final void addEndpoints() {
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			// only binds to IPv6 addresses and localhost
			if (addr instanceof Inet6Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
	} // func

	// entry point
	public static final void main(String[] args) {
		try {
			RdServer server = new RdServer(); // create server
			server.addEndpoints(); // add endpoints on all IP addresses
			server.start();
		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	} // func

} // public class

