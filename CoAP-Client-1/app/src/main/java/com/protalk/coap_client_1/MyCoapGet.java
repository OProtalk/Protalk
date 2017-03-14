package com.protalk.coap_client_1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Jimyeol on 2017-03-14.
 */

public class MyCoapGet {
    protected String mUri = "coap://10.0.2.2/"; // 10.0.2.2 는 에뮬레이터 구동시 로컬 pc의 주소
    Context mContext;

    public MyCoapGet(String uri, Context context) { mUri = uri; mContext = context;}

    public CoapResponse onSensorPost(String uri, String payload) {
        CoapResponse response = null;
        try {
            response = new CoapClient(new URI(uri)).put(payload, MediaTypeRegistry.TEXT_PLAIN);

        } catch (URISyntaxException e) {
            Log.d("틀림...", "Invalid URI");
            Toast.makeText(mContext, "Invalid URI", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        if (response != null) {
        } else {
            Toast.makeText(mContext, "No response received", Toast.LENGTH_SHORT).show();
        }
        return response;
    }

    public CoapResponse onSensorGet(String uri){
        CoapResponse response = null;
        try {
            response = new CoapClient(new URI(uri)).get();

        } catch (URISyntaxException e) {
            Log.d("틀림...", "Invalid URI");
            Toast.makeText(mContext, "Invalid URI", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.d("클릭", "clicked");
        if (response != null) {
            Log.e("", "" + response.getCode()); // 2.05 출력.
            Log.e("", "" + response.getOptions()); // {"Content-Format":"text/plain"} 출력.
            Log.e("", "" + response.getResponseText()); // Hello! 출력.
            Log.e("", "" + response.getClass()); // class org.eclipse.californium.core.CoapResponse 출력.
            //Log.e("", "" + new String(response.getPayload())); // 페이로드를 바이트스트림으로 받아오기 때문에 String으로 캐스팅하지 않으면 깨진 문자열이 출력된다. String으로 캐스팅하면 getResponseText()와 출력은 같다.
            Log.e("", "" + response.advanced()); // ACK-2.05   MID=61475, Token=31, OptionSet={"Content-Format":"text/plain"}, "Hello!" // advanced()는 reponse의 모든 정보를 한 번에 반환.
            Log.e("", "" + Utils.prettyPrint(response)); // Utils.prettyPrint()는 response의 advanced()를 읽기 좋게 편집해서 String 형식으로 반환.
        } else {
            Log.e("", "" + "No response received.");
            Toast.makeText(mContext, "No response received", Toast.LENGTH_SHORT).show();
        }

        return response;
    }
}