package sharkfeel.homeautomation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jimyeol on 2017-06-13.
 */

public class BathroomFrag extends Fragment {

    private ListView listBathroom = null;
    private ListViewAdapter mAdapter = null;

    private static final String KEY_MY_PREFERENCE = "sensor_option";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.bathroom_fragment, container, false);

//        ((TextView)rootView.findViewById(R.id.textBathroomHum)).setText("습도 : 30%");
//        ((TextView)rootView.findViewById(R.id.textBathroomPres)).setText("기압 : 30");
//        ((TextView)rootView.findViewById(R.id.textBathroomTemp)).setText("온도 : 30도");
//        ((TextView)rootView.findViewById(R.id.textBathroomLux)).setText("밝기 : 3000");

        listBathroom = (ListView)rootView.findViewById(R.id.BathroomList);
        mAdapter = new ListViewAdapter(getContext());
        listBathroom.setAdapter(mAdapter);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등1",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Bathroom/Led4",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등2",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Bathroom/Led5",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.temp_icon),
                "온도",
                getContext().getResources().getString(R.string.sensor2_ipv6),
                getContext().getResources().getString(R.string.sensor_tempURL),
                "Text",
                "25",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.hum_icon),
                "습도",
                getContext().getResources().getString(R.string.sensor2_ipv6),
                getContext().getResources().getString(R.string.sensor_humURL),
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.atm_icon),
                "기압",
                getContext().getResources().getString(R.string.sensor2_ipv6),
                getContext().getResources().getString(R.string.sensor_atmURL),
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "밝기",
                getContext().getResources().getString(R.string.sensor2_ipv6),
                getContext().getResources().getString(R.string.sensor_luxURL),
                "Text",
                "20",
                false);

        listBathroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(getContext(), mData.mTitle, Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }
}