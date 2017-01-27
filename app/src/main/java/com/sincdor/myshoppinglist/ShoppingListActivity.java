package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ShoppingListActivity extends Activity {

    ListView lVlists;
    String storeName;
    ArrayAdapter<String> adapter;
    TextView tv_shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        storeName = bundle.getString("store_name");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lVlists = (ListView) findViewById(R.id.lVList);
        tv_shopName = (TextView) findViewById(R.id.tv_shopName);
        lVlists.setAdapter(adapter);
        tv_shopName.setText(storeName);

        //ListView Click Listener
        lVlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ShoppingListActivity.this, EditItemActivity.class);
                String aux = String.valueOf(parent.getItemAtPosition(position));
                String [] aux2 = aux.split(":");
                it.putExtra("item", Utils.getItemFromDB(aux2[0], storeName, getApplicationContext()));
                it.putExtra("index", position);
                it.putExtra("old", aux);
                startActivityForResult(it, 26);
            }
        });


        startListView();




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
            cursor = bd.query("table_items_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return;
        }
        int iItem_name = cursor.getColumnIndex("item_name");
        int iBrand = cursor.getColumnIndex("item_marca");
        int iStore = cursor.getColumnIndex("item_store_name");
        int iPrice = cursor.getColumnIndex("price");
        int iAmount = cursor.getColumnIndex("quantidade");
        int iComments = cursor.getColumnIndex("observacoes");
        int iUnit = cursor.getColumnIndex("unidade");
        int iDate = cursor.getColumnIndex("date");
        int iComprado = cursor.getColumnIndex("comprado");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(iItem_name) != null) {
                if(cursor.getString(iStore).equals(storeName))
                    addItemToListView(cursor.getString(iItem_name) + ":" + cursor.getString(iAmount)+cursor.getString(iUnit)+ ":" + cursor.getString(iBrand));
            }
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        bdh.close();
        return;
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
            Intent it = new Intent(getApplicationContext(), NewItemActivity.class);
            it.putExtra("marca", storeName);
            startActivityForResult(it, 6);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 6:{
                if(resultCode == Activity.RESULT_OK){
                    Bundle b = data.getExtras();

                    String s = (String) b.get("new_item");
                    Log.d(TAG, "onActivityResult: " + s);
                    addItemToListView(s);
                    break;
                }
            }case 26: {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle b = data.getExtras();
                    int removed = b.getInt("removed");
                    if(removed == 1){
                        String oldObj = b.getString("old");
                        adapter.remove(oldObj);
                    }else {
                        int index = b.getInt("index");
                        String s = b.getString("item");
                        String oldObj = b.getString("old");
                        adapter.insert(s, index);
                        adapter.remove(oldObj);
                    }
                    break;
                }
            }
        }
    }
}
