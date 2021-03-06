package mobi.bluepadge.brotest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import mobi.bluepadge.brotest.db.StaffDataBaseHelper;


public class ResultActivity extends ActionBarActivity{
    private String comid;
    private TextView userName;
    private TextView department;
    private TextView sourceName;
    private TextView specifications;
    private TextView monSerNum;
    private TextView position;
    private TextView date;
    private CheckBox checkBox;

    private RadioGroup radioGroup;
    private RadioButton radioButtonOK;
    private RadioButton radioButtonQRcodeWrong;
    private RadioButton radioButtonPosition;
    private RadioButton radioButtonUser;
    private RadioButton radioButtonmonitor;

    private EditText editAddNote;
    private Button saveMarks;
    private String addedNoteString = "正常;";

    private TextView errorMessage;
    private Button addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        comid = getIntent().getStringExtra(MainActivity.COMID);
        getSupportActionBar().setTitle(comid);
        userName = (TextView) findViewById(R.id.userName);
        department = (TextView) findViewById(R.id.department);
        sourceName = (TextView) findViewById(R.id.sourName);
        specifications = (TextView) findViewById(R.id.specifications);
        monSerNum = (TextView) findViewById(R.id.monSerNum);
        position = (TextView) findViewById(R.id.position);
        date = (TextView) findViewById(R.id.date);
        checkBox = (CheckBox) findViewById(R.id.checkAsRead);
        radioGroup = (RadioGroup) findViewById(R.id.noteAdded);
        radioButtonOK = (RadioButton) findViewById(R.id.ok);
        radioButtonQRcodeWrong = (RadioButton) findViewById(R.id.QRcodeWrong);
        radioButtonPosition = (RadioButton) findViewById(R.id.positonWrong);
        radioButtonUser = (RadioButton) findViewById(R.id.userWrong);
//        radioButtonmonitor = (RadioButton) findViewById(R.id.monitorWrong);

        errorMessage = (TextView) findViewById(R.id.error_message);
        addNew = (Button) findViewById(R.id.add_new);

        saveMarks = (Button) findViewById(R.id.save);

        editAddNote = (EditText) findViewById(R.id.EditNote);


        initData(ResultActivity.this, comid);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ok:
                        addedNoteString = "正常;";
                        break;
                    case R.id.QRcodeWrong:
                        addedNoteString = "使用人与台账不符;";
                        break;
                    case R.id.positonWrong:
                        addedNoteString = "设备存放位置错误;";
                        break;
                    case R.id.userWrong:
                        addedNoteString = "标签损坏;";
                        break;
                    case R.id.monitorWrong:
                        addedNoteString = "显示器序列号错误;";
                        break;
                    default:
                        break;
                }
            }
        });
    saveMarks.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkBox.isChecked()) {
                Utility.markAsChecked(ResultActivity.this, comid);
            } else {
                Utility.unmark(ResultActivity.this, comid);
            }
            String newEditNote = editAddNote.getText().toString();
//            if (!newEditNote.equals("")) {
//                addedNoteString += newEditNote;
//            }
            Utility.writeAddedNote(ResultActivity.this, comid,addedNoteString ,newEditNote);
            Toast.makeText(ResultActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            ResultActivity.this.finish();
        }
    });
    }

    private void initData(Context context, final String comid) {
        Cursor cursor = Utility.query(context, comid);
        if (cursor.moveToFirst()) {
            String userName = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_USER));
            String department = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_DEPARTMENTOFUSER));
            String sourceName = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_SOURCENAME));
            String specifications = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_SPECIFICATION));
            String monSerNum = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_MONSERIESNUM));
            String position = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_STOREPOSITION));
            String date = cursor.getString(
                    cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_DATE));
            ResultActivity.this.userName.setText(userName);
            ResultActivity.this.department.setText(department);
            ResultActivity.this.sourceName.setText(sourceName);
            ResultActivity.this.specifications.setText(specifications);
            ResultActivity.this.monSerNum.setText(monSerNum);
            ResultActivity.this.position.setText(position);
            ResultActivity.this.date.setText(date);

        } else {
//            userName.setText("序列码出错,请重试");
            userName.setVisibility(View.INVISIBLE);
            department.setVisibility(View.INVISIBLE);
            sourceName.setVisibility(View.INVISIBLE);
            specifications.setVisibility(View.INVISIBLE);
            monSerNum.setVisibility(View.INVISIBLE);
            position.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.INVISIBLE);
            editAddNote.setVisibility(View.INVISIBLE);

            saveMarks.setVisibility(View.INVISIBLE);

            errorMessage.setVisibility(View.VISIBLE);
            addNew.setVisibility(View.VISIBLE);

            addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAddNew = new Intent(ResultActivity.this, AddNewActivity.class);
                    intentAddNew.putExtra("addNew", comid);
                    startActivity(intentAddNew);
                    ResultActivity.this.finish();

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
//        saveMarks.performClick();
        super.onBackPressed();
    }
}
