package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class ShoppingListActivity extends Activity {

    ListView lVlists;
    String storeName;
    ArrayAdapter<String> adapter;
    TextView tv_shopName;
    ArrayList<HashMap<String, Object>> itemsList;
    MyAdapter myAdapter;


    void adiciona_elemento(String nome,Float amount, String unit, String brand, Float price, Integer bought) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("nome", nome);
        hm.put("amount", amount);
        hm.put("unit", unit);
        hm.put("brand", brand);
        hm.put("price", price);
        hm.put("bought", bought);
        itemsList.add(hm);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        storeName = bundle.getString("store_name");

        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>())
        itemsList = new ArrayList<>();
        myAdapter = new MyAdapter();


        lVlists = (ListView) findViewById(R.id.lVList);
        tv_shopName = (TextView) findViewById(R.id.tv_shopName);
        //lVlists.setAdapter(adapter);
        lVlists.setAdapter(myAdapter);
        tv_shopName.setText(storeName);

        //ListView Click Listener

        lVlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ShoppingListActivity.this, EditItemActivity.class);
                HashMap aux = (HashMap) parent.getItemAtPosition(position);
                it.putExtra("item", Utils.getItemFromDB((String) aux.get("nome"), storeName, getApplicationContext()));
                it.putExtra("index", position);
                it.putExtra("old", (String) aux.get("nome"));
                startActivityForResult(it, 26);
            }
        });

        lVlists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(ShoppingListActivity.this, DialogItem.class);
                HashMap aux = (HashMap) parent.getItemAtPosition(position);
                it.putExtra("item", Utils.getItemFromDB((String) aux.get("nome"), storeName, getApplicationContext()));
                it.putExtra("index", position);
                it.putExtra("old", (String) aux.get("nome"));
                startActivityForResult(it, 27);
                return true;
            }
        });

        startListView();
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

        Float amount = null;
        Float price = null;
        Integer bought = null;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(iItem_name) != null) {
                if (cursor.getString(iStore).equals(storeName)) {
                    amount = cursor.getFloat(iAmount);
                    price = cursor.getFloat(iPrice);
                    bought = cursor.getInt(iComprado);
                    if(amount == null)
                        amount = Float.valueOf(0);
                    if(price == null)
                        price = Float.valueOf(0);
                    if(bought == null)
                        bought = 0;


                    adiciona_elemento(cursor.getString(iItem_name),
                            amount,
                            cursor.getString(iUnit),
                            cursor.getString(iBrand),
                            price,
                            bought);

                    //addItemToListView(cursor.getString(iItem_name) + ":" + amount + cursor.getString(iUnit) + ":" + cursor.getString(iBrand));

                }
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
                    itemsList = new ArrayList<>();
                    startListView();
                    myAdapter.notifyDataSetChanged();
                }
            }case 26: {
                if (resultCode == Activity.RESULT_OK) {
                    itemsList = new ArrayList<>();
                    startListView();
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int i) {
            return itemsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout = getLayoutInflater().inflate(R.layout.row_items_list, null);



            LinearLayout linear = (LinearLayout) layout.findViewById(R.id.ll_row_list_items);

            TextView tV_row_name = (TextView) layout.findViewById(R.id.tV_row_name);
            TextView tV_row_quantity = (TextView) layout.findViewById(R.id.tV_row_quantity);
            TextView tV_row_unit = (TextView) layout.findViewById(R.id.tV_row_unit);
            TextView tV_row_price = (TextView) layout.findViewById(R.id.tV_row_price);
            TextView tV_row_brand = (TextView) layout.findViewById(R.id.tV_row_brand);

            String nome = (String) itemsList.get(i).get("nome");
            Float amount = (Float) itemsList.get(i).get("amount");
            String unit = (String) itemsList.get(i).get("unit");
            String brand = (String) itemsList.get(i).get("brand");
            Float price = (Float) itemsList.get(i).get("price");
            Integer bought = (Integer) itemsList.get(i).get("bought");

            tV_row_name.setText(nome);
            if(amount == null) tV_row_quantity.setText("0.0");
            else tV_row_quantity.setText(String.valueOf(amount));
            tV_row_unit.setText(unit);
            if(price == null) tV_row_price.setText("0.0");
            else tV_row_price.setText(String.valueOf(price));
            tV_row_brand.setText(brand);

            if(bought == 1){
                linear.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            }else{
                linear.setBackgroundColor(getResources().getColor(R.color.colorRed));
            }

            return layout;
        }

    }


}
