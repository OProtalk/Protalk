package sharkfeel.homeautomation;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
    데이터 베이스 관리
    KEY_SAY - "LED 켜"
    KEY_URL - "/Led/Lights"
 */
public class DBAdapter {

    public static final String KEY_ROWID = "_id";       //row
    public static final String KEY_SAY = "say";     //say
    public static final String KEY_SAY_REPLACE = "say_replace";     //say
    public static final String KEY_SERVER = "server";     //say
    public static final String KEY_URL = "url";     //say
    public static final String KEY_MEANS = "means";     //PUT, GET
    public static final String KEY_MESSAGE = "message";     //LED ON


    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "voicecontrol";
    private static final int DATABASE_VERSION = 1;

    //디비 생성 Create
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + "("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_SAY + " text not null, "
                    + KEY_SAY_REPLACE + " text not null, "
                    + KEY_SERVER +  " text not null, "
                    + KEY_URL +  " text not null, "
                    + KEY_MEANS + " text not null, "
                    + KEY_MESSAGE + " text not null" + ");";



    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "업그레이드 " + oldVersion + " to "
                    + newVersion + ", 삭제하고 새롭게");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }


    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long createNote(String say, String server, String url, String means, String message) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SAY, say);
        initialValues.put(KEY_SAY_REPLACE, say.replaceAll(" ", ""));
        initialValues.put(KEY_SERVER, server);
        initialValues.put(KEY_URL, url);
        initialValues.put(KEY_MEANS, means);
        initialValues.put(KEY_MESSAGE, message);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    //노트 삭제
    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //모든 노트 삭제 //삭제시 유의사항
    //바로 삭제되오니 AlterDIalog를 사용해서 삭제할건지
    //물어보는게 예의.
    public boolean deleteAllNote() {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID, null) > 0;
    }

    //모든 노트 보여주기
    public Cursor fetchAllNotes(String key, String str) {
        //첫번째 인자값은 어떤것을 정렬할것인가
        //두번째 인자값은 내림차순인가 오름차순인가
        //내림차순인데 KEY_TIME값이 제일 최근이 위로감
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SAY, KEY_SERVER,
                        KEY_URL, KEY_MEANS, KEY_MESSAGE}, null, null, null,
                null, key + " " + str, null);    //ASC
        // Order by (내림차순 정렬기능)
    }


    //한 노트만 보여주기
    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_SAY, KEY_SERVER, KEY_URL, KEY_MEANS, KEY_MESSAGE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //노트 업데이트
    public boolean updateNote(long rowId, String say, String url) {
        ContentValues args = new ContentValues();
        args.put(KEY_SAY, say);
        args.put(KEY_URL, url);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


    //노트검색
    public Cursor searchNote (String strKeyTitle) {
        //메모 내용까지 검색합니다.
        Cursor mCursor = mDb.query(DATABASE_TABLE,
                new String[] {KEY_ROWID,
                        KEY_SAY, KEY_SAY_REPLACE, KEY_SERVER, KEY_URL, KEY_MEANS, KEY_MESSAGE},
                KEY_SAY  + " like ? " + " or " + KEY_SAY_REPLACE + " like? ",
                new String[]{"%" + strKeyTitle + "%", "%" + strKeyTitle + "%"}, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
