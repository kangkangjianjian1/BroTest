package mobi.bluepadge.brotest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mobi.bluepadge.brotest.db.StaffDataBaseHelper;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private StaffDataBaseHelper dataBaseHelper;
    private Button searchScan;
    private Button searchEdit;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.writeXlmToDb(MainActivity.this, "ROM.xls");
        searchScan = (Button) findViewById(R.id.search_from_scan);
        searchEdit = (Button) findViewById(R.id.search_from_edit);
        editText = (EditText) findViewById(R.id.computerId);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString() != null) {
                    searchEdit.setClickable(true);
                }else {
                    searchEdit.setClickable(false);
                }
            }
        });
        searchEdit.setOnClickListener(this);
        searchScan.setOnClickListener(this);
        /*
        dataBaseHelper = new StaffDataBaseHelper(MainActivity.this,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor =
        db.query(StaffDataBaseHelper.TABLENAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name =
                        cursor.getString(cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_NAME));
                String english =
                        cursor.getString(cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_ENGILISH));
                String chinese =
                        cursor.getString(cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_CHINESE));
                Log.d("MainActivity", name);
                Log.d("MainActivity", english);
                Log.d("MainActivity", chinese);
            }while (cursor.moveToNext());
        }
        cursor.close();
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_from_edit:
                String comId = editText.getText().toString();
                Utility.query(MainActivity.this,comId);
                break;
            case R.id.search_from_scan:
                //TODO Create Scan Method
                Intent intentScan = new Intent(MainActivity.this,Scan.class);
                startActivity(intentScan);
                break;
            default:
                break;
        }
    }
}
