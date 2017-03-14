package com.protalk.rd;

import java.io.*;
import java.net.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.network.*;
import org.eclipse.californium.core.network.config.*;

import com.protalk.rd.resources.*;
import com.protalk.serial.*;

import gnu.io.*;

public class RdServer extends CoapServer {
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

	// Constructor for a new Hello-World server initializes the resources of the server.
	public RdServer() throws NoSuchPortException, UnsupportedCommOperationException, PortInUseException, IOException {
		add(new LedSensorResource(new Serial("/dev/ttyUSB0")));
		//add(new LedSensorResource(new Serial("COM4")));
		//add(new HelloResource());
		//add(new AnotherResource());
	} // func
	
	// Add individual endpoints listening on default CoAP port on all IPv6 addresses of all network interfaces.
	private final void addEndpoints() {
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			if (addr instanceof Inet6Address || addr.isLoopbackAddress()) { // only binds to IPv6 addresses and localhost
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			} // if
		} // for
	} // func

	// entry point
	public static final void main(String[] args) throws NoSuchPortException, UnsupportedCommOperationException, PortInUseException, IOException {
		try {
			RdServer server = new RdServer(); // create server
			server.addEndpoints(); // add endpoints on all IP addresses
			server.start();
		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	} // func

} // public class

