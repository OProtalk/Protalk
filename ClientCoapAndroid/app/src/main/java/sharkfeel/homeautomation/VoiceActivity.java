package sharkfeel.homeautomation;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Jimyeol on 2017-08-21.
 */

public class VoiceActivity extends AppCompatActivity {

    private static final int RESULT_SPEECH = 1; // REQUEST_CODE로 쓰임
    private static final String KEY_MY_PREFERENCE = "option";
    private static final String KEY_SERVER_IP = "ServerIP";
    //서버 IP
    private static final String KEY_SERVER1_IP = "Server1IP";
    private static final String KEY_SERVER2_IP = "Server2IP";
    private Intent i;
    private TextView tv;
    private ImageButton btnTTS;
    private TextToSpeech myTTS;

    private ListView listView;
    private voiceLvAdapter adapter;

    //데이터 베이스 어답터
    static public DBAdapter mDbHelper;

    //테스트용
    EditText editTest;
    Button btnTest;
    Button btnCreateVoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        //        //DB오픈
        mDbHelper = new DBAdapter (this);
        mDbHelper.open();

        adapter = new voiceLvAdapter() ;
        listView = (ListView)findViewById(R.id.list_message);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        btnCreateVoice = (Button)findViewById(R.id.btnCreateVoice);

        editTest = (EditText)findViewById(R.id.editTest);
        btnTest = (Button)findViewById(R.id.btnSend);


        //Test to Speach
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    myTTS.setLanguage(Locale.KOREAN);
                }
            }
        });

        btnTTS = (ImageButton)findViewById(R.id.btnTTS);

        btnTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnTTS) {
                    i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName()); // 호출한 패키지
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR"); // 인식할 언어를 설정한다.
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해주세요"); // 유저에게 보여줄 문자
                    try {
                        startActivityForResult(i, RESULT_SPEECH);
                    }catch(ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(),"Speech To Text를 지원하지 않습니다.",Toast.LENGTH_SHORT).show();
                        e.getStackTrace();
                    }
                }
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result_sst = editTest.getText().toString();
                String replaceString = result_sst.replaceAll(" ", "");
                itemVoice checkME = doSearch(replaceString);
                if( checkME == null ) {
                    Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_SHORT).show();
                    adapter.addItem(result_sst, "없는 명령어입니다.") ;
                    result_sst += "에 대한 명령은 없습니다.";
                    resultSpeach(result_sst);
                } else {
                    Toast.makeText(getApplicationContext(), checkME.url, Toast.LENGTH_SHORT).show();
                    //adapter.addItem(result_sst, "실행 완료") ;
                    //String uri = optionFileRead(KEY_SERVER_IP) + checkME.url;
                    switch (checkME.means) {
                        case "GET":
                            String get_uri="http://" +
                                    optionFileRead(KEY_SERVER1_IP) +
                                    ":" +
                                    optionFileRead(KEY_SERVER2_IP) +
                                    "/proxy/" +
                                    checkME.server +
                                    checkME.url;
                            //new CoapGetTask().execute(get_uri);
                            new DownloadWebpageTaskGet().execute(get_uri, result_sst);
                            break;
                        case "PUT":
                            adapter.addItem(result_sst, "실행 완료") ;
                            String put_uri = checkME.server + checkME.url;
                            new CoapPostTask().execute(put_uri, checkME.putmessage);
                            break;
                    }
                }

            }
        });

        btnCreateVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceActivity.this, DBInsertActivity.class);
                startActivity(intent);
            }
        });

//
//        tv = (TextView)findViewById(R.id.tv);
//        bt = (ImageButton)findViewById(R.id.button);

    }
    //
    private class DownloadWebpageTaskGet extends AsyncTask<String,Void,String> {

        String sst;
        public DownloadWebpageTaskGet() {
            sst = "";
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                sst = (String)args[1];
                return (String)downloadUrl((String)args[0]);
            } catch (IOException e) {
                return "download error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            resultSpeach(result + "입니다.");
            adapter.addItem(sst, result + "입니다.") ;
            Toast.makeText(getApplicationContext(), result + "입니다.", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }

        private String downloadUrl(String myurl) throws IOException{
            HttpURLConnection conn=null;
            URL url=new URL(myurl);
            conn=(HttpURLConnection)url.openConnection();
            BufferedInputStream buf=new BufferedInputStream(conn.getInputStream());
            BufferedReader bufreader=new BufferedReader(new InputStreamReader(buf,"utf-8"));
            String line=null;
            String page="";
            while((line=bufreader.readLine())!=null){
                Log.d("test",line);
                page+=line;
            }
            return page;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == RESULT_SPEECH)) {
       /* data.getString...() 호출로 음성 인식 결과를 ArrayList로 받는다. */
            ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
       /* 결과들 중 음성과 가장 유사한 단어부터 시작되는 0번째 문자열을 저장한다.*/
            String result_sst = sstResult.get(0);

            //tv.setText("" + result_sst); // 텍스트 뷰에 보여준다.
            String replaceString = result_sst.replaceAll(" ", "");
            itemVoice checkME = doSearch(replaceString);
            if( checkME == null) {
                adapter.addItem(result_sst, "없는 명령어입니다.") ;
                result_sst += "에 대한 명령은 없습니다.";
                resultSpeach(result_sst);
            } else {
                //String uri = optionFileRead(KEY_SERVER_IP) + checkME.url;
                switch (checkME.means) {
                    case "GET":
                        String get_uri="http://" +
                                optionFileRead(KEY_SERVER1_IP) +
                                ":" +
                                optionFileRead(KEY_SERVER2_IP) +
                                "/proxy/" +
                                checkME.server +
                                checkME.url;
                        new DownloadWebpageTaskGet().execute(get_uri, result_sst);
                        break;
                    case "PUT":
                        adapter.addItem(result_sst, "실행 완료") ;
                        String put_uri = checkME.server + checkME.url;
                        new CoapPostTask().execute(put_uri, checkME.putmessage);
                        break;
                }
                //result_sst += "에 대한 명령을 실행했습니다.";
            }
            adapter.notifyDataSetChanged();

        }
    }

    //검색전용
    private itemVoice doSearch(String search) {

        //mDbHelper.searchEditions();
        Cursor notesCursor = mDbHelper.searchNote(search);

        startManagingCursor(notesCursor);


        String[] from = new String[] { DBAdapter.KEY_URL};
        int[] to = new int[] { R.id.textComMessage};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.list_item, notesCursor, from, to);



        if ( notes.getCount() > 0 ) {
            itemVoice item = new itemVoice();
            item.server = notesCursor.getString(notesCursor.getColumnIndex("server"));
            item.url = notesCursor.getString(notesCursor.getColumnIndex("url"));
            item.means = notesCursor.getString(notesCursor.getColumnIndex("means"));
            item.putmessage = notesCursor.getString(notesCursor.getColumnIndex("message"));
            return item;
        } else {
            return null;
        }
    }

    class itemVoice {
        public String server;
        public String url;
        public String means;
        public String putmessage;
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_start) {
//            if (!item.isChecked()) {
//                startService(new Intent(this, ServerService.class));
//                item.setChecked(true);
//            } else {
//                stopService(new Intent(this, ServerService.class));
//                item.setChecked(false);
//            }
//            return true;
        } else if (id == R.id.action_option) {
        } else if (id == R.id.action_revice_ip) {

        }

        return super.onOptionsItemSelected(item);
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
    //
//    public void clickGet(View view) {
//        String uri = ((EditText)findViewById(R.id.editUri)).getText().toString();
//        new CoapGetTask().execute(uri);
//    }
//
    class CoapGetTask extends AsyncTask<String, String, CoapResponse> {

        protected void onPreExecute() {
            // reset text fields
//            ((TextView)findViewById(R.id.textCode)).setText("");
//            ((TextView)findViewById(R.id.textCodeName)).setText("Loading...");
//            ((TextView)findViewById(R.id.textRtt)).setText("");
//            ((TextView)findViewById(R.id.textContent)).setText("");
        }

        protected CoapResponse doInBackground(String... args) {
            CoapClient client = new CoapClient(args[0]);
            return client.get();
        }

        protected void onPostExecute(CoapResponse response) {
            if (response!=null) {
                Toast.makeText(VoiceActivity.this, response.getResponseText(), Toast.LENGTH_SHORT).show();
                //resultSpeach(response.getResponseText());
//                ((TextView)findViewById(R.id.textCode)).setText(response.getCode().toString());
//                ((TextView)findViewById(R.id.textCodeName)).setText(response.getCode().name());
//                ((TextView)findViewById(R.id.textRtt)).setText(response.advanced().getRTT()+" ms");
//                ((TextView)findViewById(R.id.textContent)).setText(response.getResponseText());
            } else {
                Toast.makeText(VoiceActivity.this, "No response", Toast.LENGTH_SHORT).show();
                //resultSpeach("서버를 확인하세요.");
//                ((TextView)findViewById(R.id.textCodeName)).setText("No response");
            }
        }
    }

    class CoapPostTask extends AsyncTask<String, String, CoapResponse> {

        protected void onPreExecute() {
            // reset text fields
        }

        protected CoapResponse doInBackground(String... args) {
            CoapClient client = new CoapClient(args[0]);
            return client.put(args[1], MediaTypeRegistry.TEXT_PLAIN);
        }

        protected void onPostExecute(CoapResponse response) {
            if (response!=null) {
                Toast.makeText(getApplicationContext(), response.getResponseText(), Toast.LENGTH_SHORT).show();
                //resultSpeach(response.getResponseText());

            } else {
                Toast.makeText(getApplicationContext(), "서버를 확인하세요.", Toast.LENGTH_SHORT).show();
                //resultSpeach("서버를 확인하세요.");
                //Toast.makeText(mContext, response.getResponseText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resultSpeach(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(str);
        } else {
            ttsUnder20(str);
        }
    }
}