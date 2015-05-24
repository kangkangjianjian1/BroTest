package mobi.bluepadge.brotest;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashSet;
import java.util.Set;

import mobi.bluepadge.brotest.db.StaffDataBaseHelper;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String COMID = "comid";
//    private StaffDataBaseHelper dataBaseHelper;
    private ImageButton searchScan;
    private ImageButton searchEdit;
    private EditText editText;
    private ProgressDialog progressDialog;
    private Button saveChecked;
    private Button saveNew;

    private AutoCompleteTextView mWorkPosition;

    private ListView mUnchecked;

    private ArrayAdapter<String> positionAdapter;

    private String[] array = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //判断是否需要重写数据库
        searchScan = (ImageButton) findViewById(R.id.search_from_scan);
        searchEdit = (ImageButton) findViewById(R.id.search_from_edit);
        editText = (EditText) findViewById(R.id.computerId);
        saveChecked = (Button) findViewById(R.id.saveToSdbtn);
        saveNew = (Button) findViewById(R.id.saveNewToSdbtn);

        mUnchecked = (ListView) findViewById(R.id.showResultLv);

        mWorkPosition = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        saveChecked.setOnClickListener(this);
        saveNew.setOnClickListener(this);
        searchEdit.setOnClickListener(this);
        searchScan.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onStart() {
        super.onStart();
        if (Utility.isWriteDb(MainActivity.this)) {
            writeDb wd = new writeDb(MainActivity.this);
            wd.execute("hello.xls");
        } else {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Set set = prefs.getStringSet(Utility.POSITIONSET, null);
            array = new String[set.size()];
            set.toArray(array);

            positionAdapter = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_dropdown_item_1line, array);
            mWorkPosition.setAdapter(positionAdapter);

            mWorkPosition.setThreshold(1);
            //mWorkPosition.setOnClickListener(this);
            //设置点击事件
            mWorkPosition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String obj = (String) parent.getItemAtPosition(position);
                    Cursor cursor = Utility.queryUnchecked(MainActivity.this, obj);
                    String[] from = new String[]{
                            StaffDataBaseHelper.TABLE_COLUMN_SOURCENAME,
                            StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM,
                            StaffDataBaseHelper.TABLE_COLUMN_DEPARTMENTOFUSER,
                            StaffDataBaseHelper.TABLE_COLUMN_USER,
                    };
                    int[] to = new int[]{
                            R.id.sourceName,
                            R.id.sourceNum,
                            R.id.department,
                            R.id.username_item,
                    };
                    final ListAdapter listAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.list_item, cursor, from, to);
                    mUnchecked.setAdapter(listAdapter);
                    mUnchecked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Cursor cursor = ((SimpleCursorAdapter) listAdapter).getCursor();
                            if (cursor != null && cursor.moveToPosition(position)) {
                                String surNum = cursor.getString(1);
                                showResult(surNum);
                            }
                        }
                    });
                    //Toast.makeText(MainActivity.this, obj,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_from_edit:
                String comId = editText.getText().toString();
                if (!"".equals(comId)) {
                    showResult(comId);
                }else {
                    Toast.makeText(getApplicationContext(), "请输入序列号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.search_from_scan:
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
//                integrator.addExtra("SCAN_WIDTH", 640);
//                integrator.addExtra("SCAN_HEIGHT", 480);
//                integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
//                integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
                //customize the prompt message before scanning
                integrator.addExtra("PROMPT_MESSAGE", "开始扫描");
//                integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.initiateScan();
                break;
            case R.id.saveToSdbtn:
                WriteSd writeSd = new WriteSd(MainActivity.this);
                writeSd.execute("old");
                break;
            case R.id.autoCompleteTextView:
                Toast.makeText(this, "test",Toast.LENGTH_SHORT).show();
                break;
            case R.id.saveNewToSdbtn:
                WriteSd writeNewToSd = new WriteSd(MainActivity.this);
                writeNewToSd.execute("new");
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                showResult(contents);
            } else {
                Toast.makeText(MainActivity.this, "取消扫描", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showResult(String comId) {
        //跳转到展示页面
        Intent intent = new Intent(MainActivity.this,ResultActivity.class);
        intent.putExtra(COMID, comId);
        startActivity(intent);
    }

    class writeDb extends AsyncTask<String, Void, Void> {
        private Context context;

        writeDb(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {

            Utility.writeXlmToDb(context, params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在初始化数据...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
//            Toast.makeText(context, "数据初始化仅执行一次,请耐心等待", Toast.LENGTH_LONG).show();
            progressDialog.show();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Set temp = new HashSet();
            temp.add("工程测试");
            Set set = prefs.getStringSet(Utility.POSITIONSET, temp);

            array = new String[set.size()];
            set.toArray(array);
            positionAdapter = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_dropdown_item_1line, array);
            mWorkPosition.setAdapter(positionAdapter);

            mWorkPosition.setThreshold(1);
            //mWorkPosition.setOnClickListener(this);
            //设置点击事件
            mWorkPosition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String obj = (String) parent.getItemAtPosition(position);
                    Cursor cursor = Utility.queryUnchecked(MainActivity.this, obj);
                    String[] from = new String[]{
                            StaffDataBaseHelper.TABLE_COLUMN_SOURCENAME,
                            StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM,
                            StaffDataBaseHelper.TABLE_COLUMN_DEPARTMENTOFUSER,
                            StaffDataBaseHelper.TABLE_COLUMN_USER,
                    };
                    int[] to = new int[]{
                            R.id.sourceName,
                            R.id.sourceNum,
                            R.id.department,
                            R.id.username_item,
                    };
                    final ListAdapter listAdapter= new SimpleCursorAdapter(MainActivity.this, R.layout.list_item,cursor,from, to);
                    mUnchecked.setAdapter(listAdapter);
                    mUnchecked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Cursor cursor = ((SimpleCursorAdapter)listAdapter).getCursor();
                            if (cursor != null && cursor.moveToPosition(position)) {
                                String surNum = cursor.getString(1);
                                showResult(surNum);
                            }
                        }
                    });
                    //Toast.makeText(MainActivity.this, obj,Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    class WriteSd extends AsyncTask<String, Void, Void> {
        private Context context;

        WriteSd(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            if ("old".equals(params[0])) {
                Utility.writeSd(context, StaffDataBaseHelper.CHECKED);
                return null;
            }
            if ("new".equals(params[0])) {
                Utility.writeNewToSd(context);
                return null;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在写到SD...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

    }

}
