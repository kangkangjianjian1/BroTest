package mobi.bluepadge.brotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by padgeblue on 15/5/24.
 */
public class AddNewActivity extends ActionBarActivity {

    private String comid;

    private TextView comidText;

    private EditText name;
    private EditText department;
    private EditText sourceName;
    private EditText type;
    private EditText moniNum;
    private EditText position;
    private EditText date;

    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addnew);
        comid = getIntent().getStringExtra("addNew");
        comidText = (TextView) findViewById(R.id.comid);
        comidText.setText("主机序列号:" + comid);

        name = (EditText) findViewById(R.id.name);
        department = (EditText) findViewById(R.id.department);
        sourceName = (EditText) findViewById(R.id.source_name);
        type = (EditText) findViewById(R.id.type);
        moniNum = (EditText) findViewById(R.id.monitor_num);
        position = (EditText) findViewById(R.id.position);
        date = (EditText) findViewById(R.id.date);

        saveBtn = (Button) findViewById(R.id.save_new);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> infos = new ArrayList<String>();


                String nameString = name.getText().toString().trim();
                String departmentString = department.getText().toString().trim();
                String sourceString = sourceName.getText().toString().trim();
                String typeString = type.getText().toString().trim();
                String monitorString = moniNum.getText().toString().trim();
                String positionString = position.getText().toString().trim();
                String dateString = date.getText().toString().trim();

                infos.add(0, comid);
                infos.add(1, nameString);
                infos.add(2, departmentString);
                infos.add(3, sourceString);
                infos.add(4, typeString);
                infos.add(5, monitorString);
                infos.add(6, positionString);
                infos.add(7, dateString);

                Utility.addToDb(AddNewActivity.this, infos);

                startActivity(new Intent(AddNewActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}
