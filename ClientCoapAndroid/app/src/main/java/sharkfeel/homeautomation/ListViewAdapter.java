package sharkfeel.homeautomation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jimyeol on 2017-06-14.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context mContext = null;
    public ArrayList<ListData> mListData = new ArrayList<ListData>();
    //서버 IP
    private static final String KEY_MY_PREFERENCE = "option";
    private static final String KEY_SERVER1_IP = "Server1IP";
    private static final String KEY_SERVER2_IP = "Server2IP";

    public ListViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int pos = position;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_main, null);

            holder.mIcon = (ImageView) convertView.findViewById(R.id.imgView);
            holder.mSensorName = (TextView) convertView.findViewById(R.id.tvSensor);
            holder.mData = (TextView) convertView.findViewById(R.id.tv_Data);
            holder.mSwitch = (Switch) convertView.findViewById(R.id.switch_Btn);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final ListData mData = mListData.get(position);

        if (mData.mIcon != null) {
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageDrawable(mData.mIcon);
        }else{
            holder.mIcon.setVisibility(View.GONE);
        }

        holder.mSensorName.setText(mData.mTitle);
        holder.mData.setText(mData.mData);

        if( mData.mCheck ) {
            holder.mSwitch.setChecked(true);
        } else {
            holder.mSwitch.setChecked(false);
        }


        if( mData.mClass.equals("Switch")) {
            holder.mSwitch.setVisibility(View.VISIBLE);
            holder.mData.setVisibility(View.GONE);
        } else {
            holder.mData.setVisibility(View.VISIBLE);
            holder.mSwitch.setVisibility(View.GONE);
        }

//        if(!mData.bCoapGetTask && mListData.get(pos).mClass.equals("Text")) {
//            String uri2 = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
//            new CoapObserveTask(holder).execute(uri2, String.valueOf(pos));
//            mData.bCoapGetTask = true;
//        }

//        if (!mData.bCoapGetTask) {
//            final String uri2 = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
//            new CoapGetTask(holder).execute(uri2, String.valueOf(pos));
//            mData.bCoapGetTask = true;
//        }

//        if (!mData.bCoapGetTask) {
//            final String uri2 = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    new CoapGetTask(holder).execute(uri2, String.valueOf(pos));
//                }
//            }, 500);
////            new CoapObserveTask(holder).execute(uri2, String.valueOf(pos));
////            mData.bCoapGetTask = true;
//        }


        holder.mData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String uri2 = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
               // new CoapGetTask(holder).execute(uri2, String.valueOf(pos));
                String strUrl="http://" +
                        optionFileRead(KEY_SERVER1_IP) +
                        ":" +
                        optionFileRead(KEY_SERVER2_IP) +
                        "/proxy/" +
                        mListData.get(pos).mServerIP +
                        ":5683/" +
                        mListData.get(pos).mSensorURL;
                //Toast.makeText(mContext, strUrl, Toast.LENGTH_SHORT).show();
                new DownloadWebpageTask(holder).execute(strUrl, String.valueOf(pos));

                //String html = downloadURL("http://www.naver.com");
                //holder.mData.setText(html);
            }
        });

        if(!mData.bOnCheckedChangeListener) {
            holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String uri = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
                    if (isChecked == true) {
                        new CoapPostTask().execute(uri, "ON");
                        mData.mCheck = true;
                    } else {
                        new CoapPostTask().execute(uri, "OFF");
                        mData.mCheck = false;
                    }
                }
            });
            mData.bOnCheckedChangeListener = true;
        }

        return convertView;
    }

    public String optionFileRead(String kindOption) {
        //파일읽어오기
        SharedPreferences prefs = mContext.getSharedPreferences(kindOption, MODE_PRIVATE);
        String value = prefs.getString(KEY_MY_PREFERENCE, "coap://[2005::ba27:ebff:fe48:52b4]");
        return value;
    }

    private class DownloadWebpageTask extends AsyncTask<String,Void,String> {

        private ViewHolder holder;

        public DownloadWebpageTask(ViewHolder viewholder) {
            holder  = viewholder;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return (String)downloadUrl((String)args[0]);
            } catch (IOException e) {
                return "download error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            holder.mData.setText(result);
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

//    class CoapObserveTask extends AsyncTask<String, String, Object> {
//        private ViewHolder holder;
//
//        public CoapObserveTask(ViewHolder viewholder) {
//            holder  = viewholder;
//        }
//
//        @Override
//        protected Object doInBackground(String... args) {
//            final String posView = args[1];
//            for(;;) {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                publishProgress("onLoad", posView);
//                Log.d("sdf","load");
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            if(values[0].equals("onLoad")) {
//
//                final String uri2 = "" + mListData.get(Integer.valueOf(values[1])).mServerIP
//                        + mListData.get(Integer.valueOf(values[1])).mSensorURL;
//                Log.d("sdf",uri2);
//                new CoapGetTask(holder).execute(uri2, values[1]);
//
//            } else if(values[0].equals("onError")) {
//                Toast.makeText(mContext, "서버를 확인하세요.", Toast.LENGTH_SHORT).show();
//            } // if
//        } // func
//
//    } // class CoapObserveTask


    class CoapGetTask extends AsyncTask<String, String, CoapResponse> {
        private ViewHolder holder;
        String posView;

        public CoapGetTask(ViewHolder viewholder) {
            holder  = viewholder;
        }

        protected void onPreExecute() {
            // reset text fields
        }

        protected CoapResponse doInBackground(String... args) {
            final CoapClient client = new CoapClient(args[0]); // uri
            posView = args[1];
            return client.get();
        }

        protected void onPostExecute(CoapResponse response) {
            if (response!=null) {
                holder.mData.setText(response.getResponseText());
                if( mListData.get(Integer.valueOf(posView)).mSensorURL.contains("Temperature")) {
                    holder.mData.append("도");
                }else if(mListData.get(Integer.valueOf(posView)).mSensorURL.contains("Humidity")) {
                    holder.mData.append("%");
                }else if( mListData.get(Integer.valueOf(posView)).mSensorURL.contains("Atmosphere")) {
                    holder.mData.append("atm");
                }else if( mListData.get(Integer.valueOf(posView)).mSensorURL.contains("Lux")) {
                    holder.mData.append("lux");
                }else if( mListData.get(Integer.valueOf(posView)).mSensorURL.contains("PPM")) {
                    holder.mData.append("ppm");
                }
            } else {
                Toast.makeText(mContext, "Get Error!!!", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(mContext, response.getResponseText(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "서버를 확인하세요.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, response.getResponseText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addItem(Drawable mIcon, String mSensorName, String mServerIP, String mSensorURL,
                        String mClass, String mData, Boolean mSwitchCheck){
        ListData addInfo = null;
        addInfo = new ListData();
        addInfo.mIcon = mIcon;
        addInfo.mTitle = mSensorName;
        addInfo.mServerIP = mServerIP;
        addInfo.mSensorURL = mSensorURL;
        addInfo.mClass = mClass;
        addInfo.mData = mData;
        addInfo.mCheck = mSwitchCheck;

        mListData.add(addInfo);
    }
}
