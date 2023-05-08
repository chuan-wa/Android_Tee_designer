package com.example.teedesigner;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    private final Context context;
    static final String DATABASE_NAME = "teeDesigner.db";
    private static final String TABLE_NAME = "UserDesigns";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PREVIEW_PATH = "preImagePath";
    private static final String COLUMN_DESIGN = "designGson";
    private static final String COLUMN_ELEMENTS = "elementsGson";
    private static final String COLUMN_BACKGROUND = "background";
    private static final String COLUMN_SIZE = "printSize";

    public SQLHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TABLE_NAME + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_NAME + " text," +
                COLUMN_PREVIEW_PATH + " text," +
                COLUMN_DESIGN + " text," +
                COLUMN_ELEMENTS + " text," +
                COLUMN_BACKGROUND + " text," +
                COLUMN_SIZE + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addData(String name, String preImagePath, String designGson, String elementsGson, String background, String printSize) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PREVIEW_PATH, preImagePath);
        cv.put(COLUMN_DESIGN, designGson);
        cv.put(COLUMN_ELEMENTS, elementsGson);
        cv.put(COLUMN_BACKGROUND, background);
        cv.put(COLUMN_SIZE, printSize);
        long result = writableDatabase.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateData(int id, String name, String preImagePath, String designGson, String elementsGson, String background, String printSize){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PREVIEW_PATH, preImagePath);
        cv.put(COLUMN_DESIGN, designGson);
        cv.put(COLUMN_ELEMENTS, elementsGson);
        cv.put(COLUMN_BACKGROUND, background);
        cv.put(COLUMN_SIZE, printSize);
        long result=writableDatabase.update(TABLE_NAME, cv,"ID=?",new String[] {String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
        }
        return;
    }
    public void delete(int ID){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        long result= writableDatabase.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(ID)});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    public List<UserDesignModule> queryData() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        StringBuffer sb = new StringBuffer();
        List<UserDesignModule> userDesignModules = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String preImagePath = cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW_PATH));
            String designGson = cursor.getString(cursor.getColumnIndex(COLUMN_DESIGN));
            String elementsGson = cursor.getString(cursor.getColumnIndex(COLUMN_ELEMENTS));
            String background = cursor.getString(cursor.getColumnIndex(COLUMN_BACKGROUND));
            String printSize = cursor.getString(cursor.getColumnIndex(COLUMN_SIZE));

            UserDesignModule model = new UserDesignModule(name, preImagePath, designGson, elementsGson, background, printSize);
            model.setId(Integer.parseInt(id));
            userDesignModules.add(model);
        }
        return userDesignModules;
    }


}

