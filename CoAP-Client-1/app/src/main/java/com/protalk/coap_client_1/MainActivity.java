package com.protalk.coap_client_1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new OnClickListenerGet("coap://10.0.2.2/SayHello"));
        findViewById(R.id.btn2).setOnClickListener(new OnClickListenerGet("coap://10.0.2.2/JustAnotherResource"));
    }

    private class OnClickListenerGet implements View.OnClickListener {
        protected String mUri = "coap://10.0.2.2/"; // 10.0.2.2 는 에뮬레이터 구동시 로컬 pc의 주소
        public OnClickListenerGet(String uri) {
            mUri = uri;
        }
        @Override
        public void onClick(View v) {
            CoapResponse response = null;
            try {
                response = new CoapClient(new URI(mUri)).get();
            } catch (URISyntaxException e) {
                Log.d("틀림...", "Invalid URI");
                e.printStackTrace();
            }
            Log.d("클릭","clicked");
            if (response != null) {
                Toast.makeText(getApplicationContext(), response.getResponseText(), Toast.LENGTH_SHORT).show();
                Log.e("", "" + response.getCode()); // 2.05 출력.
                Log.e("", "" + response.getOptions()); // {"Content-Format":"text/plain"} 출력.
                Log.e("", "" + response.getResponseText()); // Hello! 출력.
                Log.e("", "" + response.getClass()); // class org.eclipse.californium.core.CoapResponse 출력.
                //Log.e("", "" + new String(response.getPayload())); // 페이로드를 바이트스트림으로 받아오기 때문에 String으로 캐스팅하지 않으면 깨진 문자열이 출력된다. String으로 캐스팅하면 getResponseText()와 출력은 같다.
                Log.e("", "" + response.advanced()); // ACK-2.05   MID=61475, Token=31, OptionSet={"Content-Format":"text/plain"}, "Hello!" // advanced()는 reponse의 모든 정보를 한 번에 반환.
                Log.e("", "" + Utils.prettyPrint(response)); // Utils.prettyPrint()는 response의 advanced()를 읽기 좋게 편집해서 String 형식으로 반환.
            } else {
                Log.e("", "" + "No response received.");
            }
        } // onClick
    } // inner class

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
