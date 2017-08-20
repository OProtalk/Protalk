package sharkfeel.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String strSever = "";


    //서버 IP
    private static final String KEY_MY_PREFERENCE = "option";
    private static final String KEY_SERVER1_IP = "Server1IP";
    private static final String KEY_SERVER2_IP = "Server2IP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strSever = "coap://localhost:5683/";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("방"));
        tabLayout.addTab(tabLayout.newTab().setText("거실"));
        tabLayout.addTab(tabLayout.newTab().setText("욕실"));
        tabLayout.addTab(tabLayout.newTab().setText("부엌"));
        tabLayout.addTab(tabLayout.newTab().setText("마당"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void ServerMenuIPSetting(){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInDialog = inflater.inflate(
                R.layout.dlg_server_ip_set, null);

        final android.app.AlertDialog dlgIPServer = new android.app.AlertDialog.Builder(
                this).setView(viewInDialog).create();
        dlgIPServer.setTitle("프록시 IP 설정");
        final EditText editServer1 = (EditText)viewInDialog.findViewById(R.id.editServer1);
        final EditText editServer2 = (EditText)viewInDialog.findViewById(R.id.editServer2);

        editServer1.setText(optionFileRead(KEY_SERVER1_IP));
        editServer2.setText(optionFileRead(KEY_SERVER2_IP));

        Button btnOK = (Button)viewInDialog.findViewById(R.id.btnSetting);
        Button btnCancel = (Button)viewInDialog.findViewById(R.id.btnCancel);
        Button btnDefault = (Button)viewInDialog.findViewById(R.id.btnDefault);


        //확인버튼
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionFileWrite(KEY_SERVER1_IP, editServer1.getText().toString());
                optionFileWrite(KEY_SERVER2_IP, editServer2.getText().toString());
                dlgIPServer.dismiss();
            }
        });
        //취소버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgIPServer.dismiss();
            }
        });

        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionFileWrite(KEY_SERVER1_IP, strSever);
                optionFileWrite(KEY_SERVER2_IP, strSever);
                dlgIPServer.dismiss();
            }
        });

        dlgIPServer.show();
    }

    private void optionFileWrite(String kindOption, String strValue) {
        //파일저장
        SharedPreferences prefs = this.getSharedPreferences(kindOption, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MY_PREFERENCE, strValue);
        editor.commit();
    }

    public String optionFileRead(String kindOption) {
        //파일읽어오기
        SharedPreferences prefs = this.getSharedPreferences(kindOption, MODE_PRIVATE);
        String value = prefs.getString(KEY_MY_PREFERENCE, "coap://[2005::ba27:ebff:fe48:52b4]");
        return value;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_ip_setting:
                ServerMenuIPSetting();
                return true;
            case R.id.action_local:
                strSever = "coap://localhost:5683/";
                return true;
            case R.id.action_sandbox:
                strSever = "coap://coap.me:5683/";
                return true;
            case R.id.action_start:
                if (!item.isChecked()) {
                    startService(new Intent(this, ServerService.class));
                    item.setChecked(true);
                } else {
                    stopService(new Intent(this, ServerService.class));
                    item.setChecked(false);
                }
                return true;
            case R.id.action_get:
                String uri = strSever + "Bedroom/Lights/Led1";
                new CoapGetTask().execute(uri);
                return true;
            case R.id.action_voice:
                Intent intent = new Intent(this, VoiceActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server, menu);
        return true;
    }

    class CoapGetTask extends AsyncTask<String, String, CoapResponse> {

        protected void onPreExecute() {
            // reset text fields
        }

        protected CoapResponse doInBackground(String... args) {
            CoapClient client = new CoapClient(args[0]);
            return client.get();
        }

        protected void onPostExecute(CoapResponse response) {
            if (response!=null) {
                Toast.makeText(MainActivity.this, response.getResponseText(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, response.getResponseText(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
