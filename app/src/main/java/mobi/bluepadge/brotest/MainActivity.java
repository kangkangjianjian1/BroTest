package mobi.bluepadge.brotest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
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

import java.util.Set;
import java.util.Vector;

import mobi.bluepadge.brotest.db.StaffDataBaseHelper;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String COMID = "comid";
//    private StaffDataBaseHelper dataBaseHelper;
    private ImageButton searchScan;
    private ImageButton searchEdit;
    private EditText editText;
    private ProgressDialog progressDialog;
    private Button save;

    private AutoCompleteTextView mWorkPosition;

    private ListView mUnchecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //判断是否需要重写数据库
        searchScan = (ImageButton) findViewById(R.id.search_from_scan);
        searchEdit = (ImageButton) findViewById(R.id.search_from_edit);
        editText = (EditText) findViewById(R.id.computerId);
        save = (Button) findViewById(R.id.saveToSdbtn);

        mUnchecked = (ListView) findViewById(R.id.showResultLv);

        mWorkPosition = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        //Cursor cursorPosition = Utility.queryPosition(MainActivity.this);
        /*
        Set set = Utility.getPositions(MainActivity.this);

        String[] array = (String[]) set.toArray();
        */
        String[] array = new String[]{
                "试制中心", "试验中心一层", "天津整车二层", "安全试验室一层", "上海哈弗分公司", "试验中心二层（控制室）", "徐水", "试验中心二层-电器试验室 ",
                "三部停车场", "工程院四层", "工程院一层", "七楼文印室", "试验中心一层（中控室）", "碰撞", "股份大楼8层", "开发一中心一层", "试验中心二层（资料室）",
                "股份大楼3层", "科技节展馆", "股份大楼4层", "安全实验室/假人标定间", "博泰电池实验室", "三中心北机柜堆叠1号", "徐水集中办公室", "博泰拆车大厅 ", "试验中心三层",
                "精工模具", "新试制部现场", "开发一中心二层", "股份大楼7层", "博泰二层", "各楼层会议室", "售后（H9应急作战小组）", "开发二中心", "股份大楼10层",
                "博泰二层 ", "股份大楼7层（IT小库房）", "徐水焊装车间", "新试验中心现场", "发动机试验室后", "徐水理化办公区", "开发三中心", "天津传动二层",
                "新技术中心现场", "试验中心", "开发一中心一层（造型油泥室）", "新技术中心试制部一楼作战室 ", "博泰一层会议室四", "三部成品库", "天津",
                "新技术中心四楼机房", "试验中心一层（报告厅）", "试验中心一层四六通试验室 ", "博泰造型", "股份大楼6层", "博泰电控试验室", "三部涂装机房",
                "天津总装二层", "安全试验室/声学试验室", "内外饰事业部", "智腾自动化事业部（新大通3号车间）", "试验中心二层-环境试验室 ", "存放地点", "试验中心二层",
                "股份大楼9层", "博泰一层", "物资管理部", "售后（销售国内）", "博泰三层", "试验中心二层（机房）", "总务本部", "安全试验室/模态实验室",
                "安全试验室二层", "徐水高环休息区", "开发一中心一层（油泥室）", "整车一部", "三部", "股份大楼5层", "天津食堂二层", "股份", "物资管理部二层", "工程院小二层",
                "天津传动一层", "试验中心一层（欧五排放试验室）", "股份食堂一层", "徐水总装车间二层", "安全报告厅", "试验中心前厅、大厅、四六通", "博泰一层会议室五", "三期试验部",
                "知识管理本部（齐总办公室）", "新技术中心试制部一楼作战室", "天津传动三层",
        };
        final ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,array);
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

        save.setOnClickListener(this);
        searchEdit.setOnClickListener(this);
        searchScan.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Utility.isWriteDb(MainActivity.this)) {
            writeDb wd = new writeDb(MainActivity.this);
            wd.execute("hello.xls");
        }
        /*
        else{
            Set set = Utility.getPositions(MainActivity.this);
            String[] array = (String[]) set.toArray();
            final ArrayAdapter<String> positionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,array);
            mWorkPosition.setAdapter(positionAdapter);
        }
        */
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
                writeSd.execute(StaffDataBaseHelper.CHECKED);
                break;
            case R.id.autoCompleteTextView:
                Toast.makeText(this, "test",Toast.LENGTH_SHORT).show();
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

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            /*
            Set set = Utility.getPositions(MainActivity.this);
            String[] array = (String[]) set.toArray();
            final ArrayAdapter<String> positionAdapter =
                    new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,array);
            mWorkPosition.setAdapter(positionAdapter);
            */
        }

    }
    class WriteSd extends AsyncTask<String, Void, Void> {
        private Context context;

        WriteSd(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            Utility.writeSd(context, params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在写到SD...");
                progressDialog.setCanceledOnTouchOutside(false);
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
