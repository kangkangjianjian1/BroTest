package mobi.bluepadge.brotest;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import mobi.bluepadge.brotest.db.StaffDataBaseHelper;

/**
 * Created by bluepadge on 14-12-11.
 */
public class Utility {
    private static final String writetoDb = "WriteToDb";
    private static final String POSITIONS = "positions";

    /**
     * 返回数据库是否已经写入
     * @param context
     * @return
     */
    public static boolean isWriteDb(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(writetoDb, true);
    }

    public static void setWritetoDb(Context context, boolean stub) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(writetoDb, stub);
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setPositions(Context context, Set set) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(POSITIONS, set);
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set getPositions(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getStringSet(POSITIONS, null);
    }

    public static void writeXlmToDb(Context context, String fileName) {
//        if (!isWriteDb(context)) {
//            Log.d("Utility.class", "数据库已经存在,不需要重新读取文件写数据库");
//            return;
//        }
        Log.d("Utility.class", "第一次运行,需要创建数据库");
//        Toast.makeText(context, "数据初始化仅执行一次,请耐心等待", Toast.LENGTH_LONG).show();
//        Set set = new HashSet();
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
            Sheet sheet = wb.getSheet(1);//得到数据表
            int row = sheet.getRows();
            //从第一行开始跳过标题栏
            ContentValues contentValues = new ContentValues();
            for (int i = 1; i < row; i++) {
                if (sheet.getRow(i).length > 5) {
                Cell cellSourceName = sheet.getCell(0, i);//第一列
                Cell cellSpecification = sheet.getCell(1, i);
                Cell cellDate = sheet.getCell(2, i);
                Cell cellComSerisesNum = sheet.getCell(3, i);
                Cell cellMonSerisedNum = sheet.getCell(4, i);
                Cell cellStorePosition = sheet.getCell(5, i);
//                set.add(cellStorePosition.getContents().trim());
                Cell cellDepartmentofUser = sheet.getCell(6, i);
                Cell cellUser = sheet.getCell(7, i);
                Cell cellAddedNote = sheet.getCell(8, i);
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_SOURCENAME,
                        cellSourceName.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_SPECIFICATION,
                        cellSpecification.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_DATE,
                        cellDate.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM,
                        cellComSerisesNum.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_MONSERIESNUM,
                        cellMonSerisedNum.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_STOREPOSITION,
                        cellStorePosition.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_DEPARTMENTOFUSER,
                        cellDepartmentofUser.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_USER,
                        cellUser.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_ADDEDNOTE,
                        cellAddedNote.getContents().trim());
                contentValues.put(StaffDataBaseHelper.TABLE_COLUMN_CHECKED,
                        StaffDataBaseHelper.UNCHECKED);
                db.insert(StaffDataBaseHelper.TABLENAME, null, contentValues);
                contentValues.clear();
                }
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
//        setPositions(context, set);
    }

    public static Cursor query(Context context, String comId) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        if ("".equals(comId)) {
            return null;
        }
        Cursor cursor =
                db.query(StaffDataBaseHelper.TABLENAME,
                        null,
                        StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM + " = ?",
                        new String[]{comId},
                        null, null, null);
        return cursor;
    }
    public static Cursor queryPosition(Context context) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        Cursor cursor = db.query(StaffDataBaseHelper.DBNAME,
                null,null,null,null,null,null);
        return cursor;
    }
    public static Cursor queryUnchecked(Context contect,String position){
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(contect,
                StaffDataBaseHelper.DBNAME,null,1);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        if ("".equals(position)) {
            return null;
        }
        Cursor cursor =
                db.query(StaffDataBaseHelper.TABLENAME,
                        null,
                        StaffDataBaseHelper.TABLE_COLUMN_STOREPOSITION + " =? AND "
                        +StaffDataBaseHelper.TABLE_COLUMN_CHECKED+" =? ",
                        new String[]{position,StaffDataBaseHelper.UNCHECKED},
                        null,null,null);
        return cursor;
    }

    public static void markAsChecked(Context context, String comSerNum) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StaffDataBaseHelper.TABLE_COLUMN_CHECKED, StaffDataBaseHelper.CHECKED);
        db.update(StaffDataBaseHelper.TABLENAME,
                cv,
                StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM + " = ?",
                new String[]{comSerNum}
        );
    }

    public static void writeAddedNote(Context context,String comid, String addNote) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StaffDataBaseHelper.TABLE_COLUMN_RESULT, addNote);
        db.update(StaffDataBaseHelper.TABLENAME,
                cv,
                StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM + " = ?",
                new String[]{comid}
        );
    }

    public static void unmark(Context context, String comid) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StaffDataBaseHelper.TABLE_COLUMN_CHECKED, StaffDataBaseHelper.UNCHECKED);
        db.update(StaffDataBaseHelper.TABLENAME,
                cv,
                StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM + " = ?",
                new String[]{comid}
        );
    }

    public static void writeSd(Context context,String checked) {
        StaffDataBaseHelper dataBaseHelper = new StaffDataBaseHelper(context,
                StaffDataBaseHelper.DBNAME, null, 1);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor =
                db.query(StaffDataBaseHelper.TABLENAME,
                        null,
                        StaffDataBaseHelper.TABLE_COLUMN_CHECKED + " = ?",
                        new String[]{checked},
                        null, null, null
                );
        String dir = Environment.getExternalStorageDirectory() + "/CheckedExcel";
        File a = new File(dir);
        if (!a.exists()) {
            a.mkdir();
        }
        File file = new File(a, "checked.xls");
        try {
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
            WritableSheet sheet = writableWorkbook.createSheet("已检查", 0);
            sheet.addCell(new Label(0, 0, "资产名称"));
            sheet.addCell(new Label(1, 0, "规格型号"));
            sheet.addCell(new Label(2, 0, "入账日期"));
            sheet.addCell(new Label(3, 0, "主机序列号"));
            sheet.addCell(new Label(4, 0, "显示器序列号"));
            sheet.addCell(new Label(5, 0, "存放地点"));
            sheet.addCell(new Label(6, 0, "现使用人部门"));
            sheet.addCell(new Label(7, 0, "现使用人"));
            sheet.addCell(new Label(8, 0, "备注"));
            sheet.addCell(new Label(9, 0, "检查结果"));

            if (cursor.moveToFirst()) {
                int index = 1;
                do {
                    String sourceName = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_SOURCENAME));
                    String specifications = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_SPECIFICATION));
                    String date = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_DATE));
                    String comSerId = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_COMSERIESNUM));
                    Log.d("Utility.class", comSerId);
                    String monSerId = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_MONSERIESNUM));
                    String position = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_STOREPOSITION));
                    String department = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_DEPARTMENTOFUSER));
                    String userName = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_USER));
                    String addedNote = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_ADDEDNOTE));
                    String resultNote = cursor.getString(
                            cursor.getColumnIndex(StaffDataBaseHelper.TABLE_COLUMN_RESULT));
                    sheet.addCell(new Label(0, index, sourceName));
                    sheet.addCell(new Label(1, index, specifications));
                    sheet.addCell(new Label(2, index, date));
                    sheet.addCell(new Label(3, index, comSerId));
                    sheet.addCell(new Label(4, index, monSerId));
                    sheet.addCell(new Label(5, index, position));
                    sheet.addCell(new Label(6, index, department));
                    sheet.addCell(new Label(7, index, userName));
                    sheet.addCell(new Label(8, index, addedNote));
                    sheet.addCell(new Label(9, index, resultNote));
                    index++;
                } while (cursor.moveToNext());
            }
            writableWorkbook.write();
            writableWorkbook.close();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }
}
