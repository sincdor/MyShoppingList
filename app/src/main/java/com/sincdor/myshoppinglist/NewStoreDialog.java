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
                i++;
                if(Utils.addShopToDB(getApplicationContext(), name, i)){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("new_store", i+ ": " + name);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }else{
                    Toast.makeText(this, R.string.store_exists, Toast.LENGTH_SHORT).show();
                    i--;
                }
        } else {
            Toast.makeText(this, R.string.erro_fill_store_name, Toast.LENGTH_SHORT).show();
            shop_name.setText("");
        }
    }
    public void cancelShop(View view) {
        finish();
    }
}
