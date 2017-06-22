package sharkfeel.homeautomation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jimyeol on 2017-06-13.
 */

/*
리빙 목록 : 온도, 습도, 기압, Led, LUX
* */
public class LivingFrag extends Fragment {

    private ListView listLiving = null;
    private ListViewAdapter mAdapter = null;
    private Switch aSwitch = null;

    TextView textLivingHum, textLivingPres, textLivingTemp, textLivingLux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.living_fragment, container, false);

        listLiving = (ListView)rootView.findViewById(R.id.listLiving);
        mAdapter = new ListViewAdapter(getContext());
        listLiving.setAdapter(mAdapter);


//        ((TextView)rootView.findViewById(R.id.textLivingHum)).setText("습도 : 30%");
//        ((TextView)rootView.findViewById(R.id.textLivingPres)).setText("기압 : 30");
//        ((TextView)rootView.findViewById(R.id.textLivingTemp)).setText("온도 : 30도");
//        ((TextView)rootView.findViewById(R.id.textLivingLux)).setText("밝기 : 3000");

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등1",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Living/Led6",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등2",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Living/Led7",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.temp_icon),
                "온도",
                getContext().getResources().getString(R.string.sensor4_ipv6),
                getContext().getResources().getString(R.string.sensor_tempURL),
                "Text",
                "25",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.hum_icon),
                "습도",
                getContext().getResources().getString(R.string.sensor4_ipv6),
                getContext().getResources().getString(R.string.sensor_humURL),
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.atm_icon),
                "기압",
                getContext().getResources().getString(R.string.sensor4_ipv6),
                getContext().getResources().getString(R.string.sensor_atmURL),
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "밝기",
                getContext().getResources().getString(R.string.sensor4_ipv6),
                getContext().getResources().getString(R.string.sensor_luxURL),
                "Text",
                "20",
                false);

        listLiving.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(getContext(), mData.mTitle, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;

    }

}