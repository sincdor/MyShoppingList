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

        DBHelper db = new DBHelper(this);
        SQLiteDatabase sq = db.getWritableDatabase();

        sq.execSQL("DELETE FROM table_shop_list WHERE shop_name LIKE '%" + storeName + "%';");
        sq.execSQL("DELETE FROM table_items_list WHERE item_store_name LIKE '%" + storeName + "%';");
        //sq.execSQL("DELETE FROM messages WHERE friendusername LIKE '%" + toDeleteName + "%' AND myusername LIKE '%"+ myUsername +"%'");
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
