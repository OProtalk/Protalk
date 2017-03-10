package com.protalk;

import java.net.*;
import java.util.*;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP.*;
import org.eclipse.californium.core.network.*;
import org.eclipse.californium.core.network.config.*;
import org.eclipse.californium.core.server.resources.*;

public class RdServer extends CoapServer {
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

	// Constructor for a new Hello-World server initializes the resources of the server.
	public RdServer() throws SocketException {
		add(new HelloResource()); // provide an instance of a Hello-World resource
		add(new AnotherResource());
	} // func

	// reproductive
	private final class HelloResource extends CoapResource {
		
		private static final String HELLO = "Hello!";
		private String mGreeting = HELLO;
		
		public HelloResource() {
			super("SayHello"); // 리소스 이름 설정한다. 이 자원에 대한 주소는 이런 식이 될 것이다. coap://[2005::ba27:ebff:fe3c:ba5a]:5683/SayHello
			getAttributes().setTitle("Greetings Resource"); // 리소스의 title 어트리뷰트를 설정한다. 이 경우 리소스에 대한 링크 서술(link description)은 이런 식이다. </SayHello>;title="Greetings Resource"
		} // func

		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond(mGreeting); // 리퀘스트에 대한 응답이다. ACK-2.05 메시지다. respond(String)를 한 번 더 호출하면 CON-2.05를 전송한다.
		} // func

		@Override
		public void handlePOST(CoapExchange exchange) {
			// TODO Auto-generated method stub
			exchange.accept(); // 서버가 반환값을 계산하느라고 메시지 전송이 지연될 수 있을 때, 타임아웃이 일어나지 않게 우선 빈 ack만 보낸다. 진정한 반환 내용은 이 직후에 전송한다.
			List<String> queries = exchange.getRequestOptions().getUriQuery();
			//
			add(new HelloResource());
			add(new AnotherResource());
			String res = new String(exchange.getRequestPayload());
			for(String str : queries) {
				res += str;
			}
			mGreeting += res;
			//
			exchange.respond(ResponseCode.CREATED, "greeting set to " + mGreeting);
		}

		@Override
		public void handlePUT(CoapExchange exchange) {
			// TODO Auto-generated method stub
			//
			add(new HelloResource());
			add(new AnotherResource());
			mGreeting = new String(exchange.getRequestPayload());
			//
			exchange.respond(ResponseCode.CHANGED, "greeting set to " + mGreeting);
			changed(); // notify all observers
		}

		@Override
		public void handleDELETE(CoapExchange exchange) {
			// TODO Auto-generated method stub
			// super.handleDELETE(exchange);
			delete();
            exchange.respond(ResponseCode.DELETED); // DELETED = 응답 코드 2.02
		}

	} // class
	
	// immature
	private final class AnotherResource extends CoapResource {
		public AnotherResource() {
			super("JustAnotherResouce"); // 리소스 이름 설정한다. 이 자원에 대한 주소는 이런 식이 될 것이다. coap://[2005::ba27:ebff:fe3c:ba5a]:5683/SayHello
		} // func

		@Override
		public void handleGET(CoapExchange exchange) {
			exchange.respond("Hey.");
		} // func

		@Override
		public void handleDELETE(CoapExchange exchange) {
			// TODO Auto-generated method stub
			delete();
            exchange.respond(ResponseCode.DELETED); 
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

