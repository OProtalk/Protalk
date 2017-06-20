package sharkfeel.homeautomation;

import android.content.Context;
import android.graphics.Interpolator;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

/**
 * Created by Jimyeol on 2017-06-14.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context mContext = null;
    public ArrayList<ListData> mListData = new ArrayList<ListData>();

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
        holder.mSwitch.setChecked(mData.mCheck);

        if( mData.mClass.equals("Switch")) {
            holder.mSwitch.setVisibility(View.VISIBLE);
        } else {
            holder.mData.setVisibility(View.VISIBLE);
        }

        String uri2 = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
        new CoapGetTask(holder).execute(uri2, String.valueOf(pos));


        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String uri = "" + mListData.get(pos).mServerIP + mListData.get(pos).mSensorURL;
                if( isChecked == true ) {
                    new CoapPostTask().execute(uri, "ON");
                } else {
                    new CoapPostTask().execute(uri, "OFF");
                }
            }
        });

        return convertView;
    }

    class CoapGetTask extends AsyncTask<String, String, CoapResponse> {

        ViewHolder holder;
        public CoapGetTask(ViewHolder viewholder) {
            holder  = viewholder;
        }

        protected void onPreExecute() {
            // reset text fields
        }

        protected CoapResponse doInBackground(String... args) {
            CoapClient client = new CoapClient(args[0]);
            final String a = args[1];
            CoapObserveRelation relation1 = client.observe(new CoapHandler() {
                @Override
                public void onLoad(CoapResponse response) {
                    String content = response.getResponseText();
                    Log.i("info", content);
                    mListData.get(Integer.valueOf(a)).mData = content;
                    holder.mData.setText(response.getResponseText());
                }

                @Override
                public void onError() {
                    Toast.makeText(mContext, "에러", Toast.LENGTH_SHORT).show();
                }
            });
            return client.get();
        }

        protected void onPostExecute(CoapResponse response) {
            if (response!=null) {
                holder.mData.setText(response.getResponseText());
                Toast.makeText(mContext, response.getResponseText(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "에러", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, response.getResponseText(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, response.getResponseText(), Toast.LENGTH_SHORT).show();
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
