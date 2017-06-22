package sharkfeel.homeautomation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jimyeol on 2017-06-13.
 */

public class BedroomFrag extends Fragment {

    private ListView listBedRoom = null;
    private ListViewAdapter mAdapter = null;

    private static final String KEY_MY_PREFERENCE = "sensor_option";
    private static final String KEY_Bedroom_Light_Name = "Bedroom_Light_Name";
    private static final String KEY_Bedroom_Light_IP = "Bedroom_Light_IP";
    private static final String KEY_Bedroom_Window = "Bedroom_Window";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.bedroom_fragment, container, false);

//        ((TextView)rootView.findViewById(R.id.textBedroomHum)).setText("습도 : 30%");
//        ((TextView)rootView.findViewById(R.id.textBedroomPres)).setText("기압 : 30");
//        ((TextView)rootView.findViewById(R.id.textBedroomTemp)).setText("온도 : 30도");
//        ((TextView)rootView.findViewById(R.id.textBedroomLux)).setText("밝기 : 3000");

        listBedRoom = (ListView)rootView.findViewById(R.id.bedroomList);
        mAdapter = new ListViewAdapter(getContext());
        listBedRoom.setAdapter(mAdapter);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등1",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Bedroom/Led1",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등2",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Bedroom/Led2",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "형광등3",
                getContext().getResources().getString(R.string.server1_ipv6),
                "Bedroom/Led3",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.window_icon),
                "창문",
                getContext().getResources().getString(R.string.server2_ipv6),
                "Bedroom/Window",
                "Switch",
                "data",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.temp_icon),
                "온도",
                getContext().getResources().getString(R.string.sensor1_ipv6),
                getContext().getResources().getString(R.string.sensor_tempURL),
                "Text",
                "25",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.hum_icon),
                "습도",
                getContext().getResources().getString(R.string.sensor1_ipv6),
                getContext().getResources().getString(R.string.sensor_humURL),
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.atm_icon),
                "기압",
                getContext().getResources().getString(R.string.server1_ipv6),
                getContext().getResources().getString(R.string.sensor_atmURL),
                "Text",
                "20",
                false);

        mAdapter.addItem(getResources().getDrawable(R.drawable.light_icon),
                "밝기",
                getContext().getResources().getString(R.string.sensor1_ipv6),
                getContext().getResources().getString(R.string.sensor_luxURL),
                "Text",
                "20",
                false);


        listBedRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServerMenuIPSetting(position);
            }

        });
        return rootView;
    }

    public void ServerMenuIPSetting(int pos){
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewInDialog = inflater.inflate(
                R.layout.dlg_sensor_ip_set, null);

        final android.app.AlertDialog dlgIPServer = new android.app.AlertDialog.Builder(
                getActivity()).setView(viewInDialog).create();
        dlgIPServer.setTitle("센서 IP 설정");

        final EditText editSensorName = (EditText)viewInDialog.findViewById(R.id.editSensorName);
        final EditText editSensorServerIP = (EditText)viewInDialog.findViewById(R.id.editSensorServerIP);

        editSensorName.setText(mAdapter.mListData.get(pos).mTitle);
        editSensorServerIP.setText(mAdapter.mListData.get(pos).mServerIP);

        Button btnOK = (Button)viewInDialog.findViewById(R.id.btnSensorSetting);
        Button btnCancel = (Button)viewInDialog.findViewById(R.id.btnSensorCancel);

        //확인버튼
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //optionFileWrite(KEY_Bedroom_Light_Name, editSensorName.getText().toString());
                //optionFileWrite(KEY_Bedroom_Window, editSensorServerIP.getText().toString());
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

        dlgIPServer.show();
    }

    private void optionFileWrite(String kindOption, String strValue) {
        //파일저장
        SharedPreferences prefs = getActivity().getSharedPreferences(kindOption, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MY_PREFERENCE, strValue);
        editor.commit();
    }

    public String optionFileRead(String kindOption) {
        //파일읽어오기
        SharedPreferences prefs = getActivity().getSharedPreferences(kindOption, MODE_PRIVATE);
        String value = prefs.getString(KEY_MY_PREFERENCE, "coap://[2005::ba27:ebff:fe48:52b4]");
        return value;
    }


}