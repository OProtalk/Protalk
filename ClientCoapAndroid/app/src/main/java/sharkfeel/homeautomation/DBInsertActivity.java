package sharkfeel.homeautomation;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class DBInsertActivity extends AppCompatActivity {

    //데이터 베이스 어답터
    static public DBAdapter mDbHelper;

    //리스트뷰 생성, 어댑터
    private ListView listView;
    private voiceLvAdapter adapter;
    private Button btnInsert;

    private Long mRowId;
    private static final int DELETE_ID = 0;

    //데이터 베이스에서 유저가 선택한 노트의 String을 리턴해준다 ㅋㅋ
    private String getDBString(AdapterView.AdapterContextMenuInfo info, String selectKey) {
        //모든 음성 검색해서
        Cursor notesCursor = mDbHelper.fetchAllNotes(mDbHelper.KEY_ROWID, "DESC");
        startManagingCursor(notesCursor);
        //포지션에 맞는거 찾으면
        notesCursor.moveToPosition(info.position);

        String labelColumn_body = notesCursor.getString(notesCursor.
                getColumnIndex(selectKey));
        //그거 리턴
        return labelColumn_body;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //설정시 무엇눌렀는지 팝업메뉴 위에 TITLE띄어줌
        AdapterView.AdapterContextMenuInfo info
                = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(getDBString(info, DBAdapter.KEY_SAY));
        menu.add(0, DELETE_ID, 0, "삭제하기");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info
                = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case DELETE_ID:
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbinsert);

//        //DB오픈
        mDbHelper = new DBAdapter (this);
        mDbHelper.open();

        adapter = new voiceLvAdapter() ;
        listView = (ListView)findViewById(R.id.sayList);
        fillData();

        //컨텍스트 메뉴 사용
        registerForContextMenu(listView);

        btnInsert = (Button)findViewById(R.id.btnDBInsert);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogInsert();
            }
        });

    }

    //데이터 채우기 (모든 보여주기)
    private void fillData() {
        Cursor notesCursor =  mDbHelper.fetchAllNotes(mDbHelper.KEY_ROWID, "DESC");
        // Get all of the notes from the database and create the item list
        startManagingCursor(notesCursor);

        String[] from = new String[] { DBAdapter.KEY_SAY , DBAdapter.KEY_SERVER, DBAdapter.KEY_URL,
                DBAdapter.KEY_MEANS, DBAdapter.KEY_MESSAGE};
        int[] to = new int[] { R.id.textVoiceMessage,R.id.textURL, R.id.textMeans, R.id.textPutMessage};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter saylist =
                new SimpleCursorAdapter(this, R.layout.insert_item, notesCursor, from, to);
        listView.setAdapter(saylist);
    }
    private void alertDialogInsert() {
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_dbinsert, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(DBInsertActivity.this);

        dialog.setTitle("음성인식을 적용하세요.")
                .setView(innerView)
                .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editSay = (EditText)innerView.findViewById(R.id.edit_Say);
                        Spinner spnServer = (Spinner)innerView.findViewById(R.id.spn_servers);
                        String strServer = spnServer.getSelectedItem().toString();


                        Spinner spnLocal = (Spinner)innerView.findViewById(R.id.spn_locals);
                        String strLocal = spnLocal.getSelectedItem().toString();

                        EditText editUrl = (EditText)innerView.findViewById(R.id.edit_Url);
                        RadioGroup radioMeans = (RadioGroup)innerView.findViewById(R.id.radioMeans);

                        final EditText editMessage = (EditText)innerView.findViewById(R.id.editPutMessage);

                        int checkedId = radioMeans.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton)radioMeans.findViewById(checkedId);
                        String str = rb.getText().toString();

                        long id = mDbHelper.createNote(editSay.getText().toString(), strServer,
                                strLocal + editUrl.getText().toString(), str, editMessage.getText().toString());
                        if(id > 0){
                            mRowId = id;
                        }else{
                            Log.e("dialog","생성실패");
                        }
                        fillData();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
}