package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ShopsActivity extends Activity {

    ListView lVList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lVList = (ListView) findViewById(R.id.lVShops);
        lVList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        startListView();
        lVList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getApplicationContext(), ShoppingListActivity.class);
                it.putExtra("store_name", String.valueOf(adapterView.getItemAtPosition(i)));
                startActivity(it);
            }
        });

        lVList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String rowInfo = String.valueOf(parent.getItemAtPosition(position));
                String [] storeName = rowInfo.split(": ");
                Log.d(TAG, "onItemLongClick: storeName: " + storeName[1]);
                Intent it = new Intent(ShopsActivity.this, DeleteStoreActivity.class);
                it.putExtra("store_delete", storeName[1]);
                it.putExtra("index", position);
                startActivityForResult(it, 20);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mItemAddShop){
            startActivityForResult(new Intent(getApplicationContext(), NewStoreDialog.class), 3);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 3:{
                if(resultCode == Activity.RESULT_OK){
                    Bundle b = data.getExtras();

                    String s = (String) b.get("new_store");
                    Log.d(TAG, "onActivityResult: " + s);
                    addItemToListView(s);
                    break;
                }
            }case 20:{
                if(resultCode == Activity.RESULT_OK){
                    Bundle b = data.getExtras();

                    //Está a funcionar muito mal
                    int index = b.getInt("index");
                    adapter.remove(adapter.getItem(index));
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void addItemToListView(String s){
        adapter.add(s);
        adapter.notifyDataSetChanged();
    }

    private void startListView(){
        DBHelper bdh = new DBHelper(this);

        SQLiteDatabase bd = bdh.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = bd.query("table_shop_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return;
        }
        int iShopName = cursor.getColumnIndex("shop_name");
        int iShopId = cursor.getColumnIndex("_id");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(iShopName) != null) {
                addItemToListView(cursor.getString(iShopName));
            }
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        bdh.close();
        return;
    }

}
