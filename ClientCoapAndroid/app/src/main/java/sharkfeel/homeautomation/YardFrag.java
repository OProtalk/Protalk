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

public class YardFrag extends Fragment {

    private ListView listYard = null;
    private ListViewAdapter mAdapter = null;

    private static final String KEY_MY_PREFERENCE = "sensor_option";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.yard_fragment, container, false);

        ((TextView)rootView.findViewById(R.id.textYardHum)).setText("습도 : 30%");
        ((TextView)rootView.findViewById(R.id.textYardPres)).setText("기압 : 30");
        ((TextView)rootView.findViewById(R.id.textYardTemp)).setText("온도 : 30도");
        ((TextView)rootView.findViewById(R.id.textYardLux)).setText("밝기 : 3000");

        listYard = (ListView)rootView.findViewById(R.id.YardList);
        mAdapter = new ListViewAdapter(getContext());
        listYard.setAdapter(mAdapter);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등1",
                "coap://localhost:5683/",
                "Yard/Lights/Led1",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등2",
                "coap://localhost:5683/",
                "Yard/Lights/Led1",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.window_icon),
                "차고지 열기",
                "coap://localhost:5683/",
                "Yard/Windows/YardDoor",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.window_icon),
                "온도",
                "coap://localhost:5683/",
                "Yard/Weather/Temperature",
                "Text",
                "25",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.window_icon),
                "습도",
                "coap://localhost:5683/",
                "Yard/Weather/Humidity",
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.window_icon),
                "기압",
                "coap://localhost:5683/",
                "Yard/Weather/Atmosphere",
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.window_icon),
                "밝기",
                "coap://localhost:5683/",
                "Yard/Weather/Lux",
                "Text",
                "20",
                false);

        listYard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(getContext(), mData.mTitle, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}