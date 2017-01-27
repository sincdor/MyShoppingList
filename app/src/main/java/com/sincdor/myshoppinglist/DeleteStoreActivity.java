package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class DeleteStoreActivity extends Activity {

    String store;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_store);
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        store = bundle.getString("store_delete");
        index = bundle.getInt("index");
    }

    void deleteStore(String storeName){
        Utils.deleteShopFromDB(storeName, getApplicationContext());
    }

    public void bl_deleteStore(View view){
        deleteStore(store);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("index", index);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    public void bl_cancelDelete(View view){
        finish();
    }
}
