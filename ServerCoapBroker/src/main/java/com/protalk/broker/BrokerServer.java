package com.protalk.broker;

import java.io.*;
import java.net.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.network.*;
import org.eclipse.californium.core.network.config.*;

import com.protalk.broker.resources.*;
import com.protalk.serial.*;

import gnu.io.*;

public class BrokerServer extends CoapServer {
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
	private Serial mSerial;

	// create server
	public BrokerServer(int _mode) throws IllegalArgumentException, NoSuchPortException, UnsupportedCommOperationException, PortInUseException, IOException {
		mSerial = new Serial("/dev/ttyUSB0");
//		mSerial = new Serial("COM5");
		run(_mode);
	} // func
	
	private final void run(int _mode) throws IllegalArgumentException {
		setMode(_mode);
		addEndpoints();
		start();
	}
	
	@SuppressWarnings("serial")
	private void setMode(int _mode) throws IllegalArgumentException {
		switch (_mode) {
		case 0: // proto
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_0_LIGHTS, mSerial));
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_0_WHEATHER, mSerial));
			//add(new HelloResource());
			//add(new AnotherResource());
			break;
		case 1: // server 1
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_1_BEDROOM, mSerial));
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_1_BATHROOM, mSerial));
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_1_LIVING, mSerial));
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_1_KITCHEN, mSerial));
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_1_YARD, mSerial));
			break;
		case 2: // server 2
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_2_BEDROOM, mSerial));
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_2_KITCHEN, mSerial));
			break;
		case 3: // server 3
			add(DirectoryResource.getFactory().create(DirectoryResource.TYPE_3_YARD, mSerial));
			break;
		default:
			System.err.println("Invalid parameter.");
			//this.destroy();
			throw new IllegalArgumentException() {
				@Override
				public String getMessage() {
					// TODO Auto-generated method stub
					return super.getMessage() + "\n" + "BrokerServer.setMode() failed.";
				}
			};
		} // switch
	} // func
	
	// Add individual endpoints listening on default CoAP port on all IPv6 addresses of all network interfaces.
	private final void addEndpoints() { // add endpoints on all IP addresses
		for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
			if (addr instanceof Inet6Address || addr.isLoopbackAddress()) { // only binds to IPv6 addresses and localhost
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			} // if
		} // for
	} // func

	// entry point
	public static final void main(String[] args) throws IllegalArgumentException, NoSuchPortException, UnsupportedCommOperationException, PortInUseException, IOException {
		if (args.length == 0) {
			System.err.println("Arguments missing.");
			return;
		}
		int arg0 = Integer.parseInt(args[0]); // 첫 번째 매개변수에서 모드 설정
		try {
			new BrokerServer(arg0); // 서버 시작
		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
		/*
		if (arg0 >= 0 && arg0 <= 3) {
			try {
				new BrokerServer(arg0); // 서버 시작
			} catch (SocketException e) {
				System.err.println("Failed to initialize server: " + e.getMessage());
			}
		} else {
			System.err.println("Invalid parameter.");
		}
		*/
	} // func

} // public class

