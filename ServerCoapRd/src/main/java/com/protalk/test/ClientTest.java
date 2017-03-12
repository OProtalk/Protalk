package com.protalk.test;

import java.net.*;

import org.eclipse.californium.core.*;

public class ClientTest {
	public static void main(String args[]) throws URISyntaxException {
		/*
		URI uri = null;
		try {
			uri = new URI("coap://127.0.0.1/SayHello");
		} catch (URISyntaxException e) {
			System.err.println("Invalid URI: " + e.getMessage());
			System.exit(-1);
		}
		CoapClient client = new CoapClient(uri);
		*/
		CoapResponse response = new CoapClient(new URI("coap://127.0.0.1/SayHello")).get();
		if (response != null) {
			System.out.println(response.getCode()); // 2.05 출력.
			System.out.println(response.getOptions()); // {"Content-Format":"text/plain"} 출력.
			System.out.println(response.getResponseText()); // Hello! 출력.
			System.out.println(response.getClass()); // class org.eclipse.californium.core.CoapResponse 출력.
			System.out.println(new String(response.getPayload())); // 페이로드를 바이트스트림으로 받아오기 때문에 String으로 캐스팅하지 않으면 깨진 문자열이 출력된다. String으로 캐스팅하면 getResponseText()와 출력은 같다. 
			System.out.println(response.advanced()); // ACK-2.05   MID=61475, Token=31, OptionSet={"Content-Format":"text/plain"}, "Hello!" // advanced()는 reponse의 모든 정보를 한 번에 반환. 
			System.out.println(Utils.prettyPrint(response)); // Utils.prettyPrint()는 response의 advanced()를 읽기 좋게 편집해서 String 형식으로 반환. 
		} else {
			System.out.println("No response received.");
		}
	} // func
} // public class


