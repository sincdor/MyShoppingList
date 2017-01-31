package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ItemInfo extends Activity {
    Item item;

    TextView tv_amount_edit;
    TextView tv_unit_edit;
    TextView tv_price_edit;
    TextView tv_brand_edit;
    TextView tv_comments_edit;
    TextView tv_item_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);


        item = (Item) getIntent().getSerializableExtra("item");
        if(item == null) finish();


        tv_amount_edit = (TextView) findViewById(R.id.tv_amount_edit);
        tv_unit_edit = (TextView) findViewById(R.id.tv_unit_edit);
        tv_price_edit = (TextView) findViewById(R.id.tv_price_edit);
        tv_brand_edit = (TextView) findViewById(R.id.tv_brand_edit);
        tv_comments_edit = (TextView) findViewById(R.id.tv_comments_edit);
        tv_item_name = (TextView) findViewById(R.id.tv_item_name);


        tv_amount_edit.setText(String.valueOf(item.getQuantidade()));
        tv_unit_edit.setText(item.getUnidade());
        tv_price_edit.setText(String.valueOf(item.getPrice()));
        tv_brand_edit.setText(item.getBrand());
        tv_comments_edit.setText(item.getObservacoes());
        tv_item_name.setText(item.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == R.id.mIRemoveItem){
            Utils.removeItemFromDB(item, getApplicationContext());
            finish();
            return true;
        }else if (id == R.id.mIEditItem){
            Intent it = new Intent(getApplicationContext(), EditItemActivity.class);
            it.putExtra("item", item);
            startActivity(it);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    public void bl_bought_item(View view) {
        if(item.getComprado() != 1){
            item.setComprado(1);
        }else
            item.setComprado(0);
        Utils.updateItem(item, item.getName(), getApplicationContext());
        finish();
    }

    public void bl_cancel_item(View view) {
        finish();
    }
}
