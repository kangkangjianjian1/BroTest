package mobi.bluepadge.brotest;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import mobi.bluepadge.brotest.db.StaffDataBaseHelper;

/**
 * Created by bluepadge on 14-12-11.
 */
public class Utility {
    private static final String  writetoDb ="WriteToDb" ;
    public static boolean isWriteDb(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(writetoDb, true);
    }

    public static void setWritetoDb(Context context,boolean stub) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(writetoDb, stub);
    }

    public static void writeXlmToDb(Context context,String fileName) {
        if (!isWriteDb(context)) {
            return;
        }
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        StaffDataBaseHelper dbHelper =
                new StaffDataBaseHelper(context, StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db =
                dbHelper.getWritableDatabase();
        //得到可写的数据库,接下来就是写入数据了
        try {
            inputStream = am.open(fileName);
            Workbook wb = Workbook.getWorkbook(inputStream);
            Sheet sheet = wb.getSheet(0);//得到数据表
            int row = sheet.getRows();
            //从第一行开始跳过标题栏
            ContentValues contentValues = new ContentValues();
            for (int i = 1; i < row; i++) {
                Cell cellName = sheet.getCell(0, i);//第一列
                Cell cellEnglish = sheet.getCell(1, i);
                Cell cellChinese = sheet.getCell(2, i);
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_NAME,
                        cellName.getContents());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_ENGILISH,
                        cellEnglish.getContents());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_CHINESE,
                        cellChinese.getContents());
                db.insert(StaffDataBaseHelper.TABLENAME, null, contentValues);
                contentValues.clear();
            }
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        设置sharedPreference中的读取数据库的值为false
        setWritetoDb(context, false);
    }

    public static void query(Context context, String comId) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
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
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}
