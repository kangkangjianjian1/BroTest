package mobi.bluepadge.brotest;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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
        initData(ResultActivity.this,comid);
    }

    private void initData(Context context,String comid) {
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
            if (checkBox.isChecked()) {
                Utility.markAsChecked(context, comid);
            } else {
                Utility.unmark(context, comid);
            }
        } else {
            userName.setText("序列码出错,请重试");
            department.setVisibility(View.INVISIBLE);
            sourceName.setVisibility(View.INVISIBLE);
            specifications.setVisibility(View.INVISIBLE);
            monSerNum.setVisibility(View.INVISIBLE);
            position.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
        }
    }

}
