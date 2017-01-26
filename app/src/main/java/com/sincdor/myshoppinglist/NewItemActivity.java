package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

public class NewItemActivity extends Activity {
    String store;
    EditText et_name;
    EditText et_comments;
    EditText et_unit;
    EditText et_price;
    EditText et_brand;
    EditText et_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Intent it = getIntent();
        Bundle myBundle = it.getExtras();
        store = myBundle.getString("marca");

        et_amount = (EditText) findViewById(R.id.et_amount);
        et_name = (EditText) findViewById(R.id.et_item_name);
        et_comments = (EditText) findViewById(R.id.et_comments);
        et_unit = (EditText) findViewById(R.id.et_unit);
        et_price = (EditText) findViewById(R.id.et_price);
        et_brand = (EditText) findViewById(R.id.et_brand);
    }

    private String getDate(){
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    public void bl_new_item(View view) {
        String name = et_name.getText().toString();
        if(name!= null && name.length() > 0){
            if(!isItem(name)) {
                try {
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    SQLiteDatabase bd = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("item_name", name);
                    values.put("item_store_name", store);
                    values.put("quantidade", et_amount.getText().toString());
                    values.put("item_marca", et_brand.getText().toString());
                    values.put("observacoes", et_comments.getText().toString());
                    values.put("unidade", et_unit.getText().toString());
                    values.put("comprado", 0);
                    values.put("date", getDate());
                    values.put("price", et_price.getText().toString());
                    bd.insert("table_items_list", null, values);
                    Toast.makeText(getApplicationContext(), "Inseri: " + values.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("USERINFODB ERROR", "Error Creating Database");
                }
                File database = getApplicationContext().getDatabasePath("shoppinglist.db");
                if (database.exists()) {
                    Toast.makeText(NewItemActivity.this, "[DATABASE] - Created", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("new_item", name + " " + et_amount.getText().toString()  + et_unit.getText().toString() + " " + et_brand.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(NewItemActivity.this, "[DATABASE] - Not Created", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NewItemActivity.this, "[APP] - I Will exit now", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }else{
                Toast.makeText(this, R.string.item_exists, Toast.LENGTH_SHORT).show();
                et_name.setText("");
            }
        }else{
            Toast.makeText(this, R.string.erro_fill_store_name, Toast.LENGTH_SHORT).show();
            et_name.setText("");
        }
    }

    public void bl_cancel_new_item(View view) {
    }

    boolean isItem(String item_name){

        DBHelper bdh = new DBHelper(this);

        SQLiteDatabase bd = bdh.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = bd.query("table_items_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return false;
        }
        int iShopName = cursor.getColumnIndex("item_name");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String v = cursor.getString(iShopName);
            if (cursor.getString(iShopName) != null && cursor.getString(iShopName).equals(item_name)) {
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
