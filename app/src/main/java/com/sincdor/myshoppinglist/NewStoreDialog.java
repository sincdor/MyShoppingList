package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class NewStoreDialog extends Activity {
    EditText shop_name;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);

        shop_name = (EditText) findViewById(R.id.store_name);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        i = sp.getInt("i", 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("i", i);
        ed.commit();
    }

    public void createShop(View view) {
        String name = shop_name.getText().toString();
        if(name!= null && name.length() > 0){
            if(!isStoreCreated(name)) {
                i++;
                try {
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    SQLiteDatabase bd = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("_id", i);
                    values.put("shop_name", name);
                    values.put("item_number", 0);
                    bd.insert("table_shop_list", null, values);
                    Toast.makeText(getApplicationContext(), "Inseri: " + values.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("USERINFODB ERROR", "Error Creating Database");
                }
                File database = getApplicationContext().getDatabasePath("shoppinglist.db");
                if (database.exists()) {
                    Toast.makeText(NewStoreDialog.this, "[DATABASE] - Created", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("new_store", i+ ": " + name);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(NewStoreDialog.this, "[DATABASE] - Not Created", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NewStoreDialog.this, "[APP] - I Will exit now", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }else{
                Toast.makeText(this, "This name already exists", Toast.LENGTH_SHORT).show();
                shop_name.setText("");
            }
        }else{
            Toast.makeText(this, R.string.erro_fill_store_name, Toast.LENGTH_SHORT).show();
            shop_name.setText("");
        }
    }
    public void cancelShop(View view) {
        finish();
    }

    boolean isStoreCreated(String sName){
        DBHelper bdh = new DBHelper(this);

        SQLiteDatabase bd = bdh.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = bd.query("table_shop_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return false;
        }
        int iShopName = cursor.getColumnIndex("shop_name");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String v = cursor.getString(iShopName);
            if (cursor.getString(iShopName) != null && cursor.getString(iShopName).equals(sName)) {
                cursor.close();
                bd.close();
                bdh.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        bdh.close();
        return false;
    }
}
