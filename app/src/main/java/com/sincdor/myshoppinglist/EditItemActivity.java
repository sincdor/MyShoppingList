package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class EditItemActivity extends Activity {

    Item item;
    EditText et_item_name_edit;
    EditText et_brand_edit;
    EditText et_amount_edit;
    EditText et_price_edit;
    EditText et_unit_edit;
    EditText et_comments_edit;
    String old;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent it = getIntent();
        item = (Item) it.getSerializableExtra("item");
        index = it.getExtras().getInt("index");
        if(item == null)
            finish();
        old = it.getExtras().getString("old");
        et_item_name_edit = (EditText) findViewById(R.id.et_item_name_edit);
        et_brand_edit = (EditText) findViewById(R.id.et_brand_edit);
        et_amount_edit = (EditText) findViewById(R.id.et_amount_edit);
        et_price_edit = (EditText) findViewById(R.id.et_price_edit);
        et_unit_edit = (EditText) findViewById(R.id.et_unit_edit);
        et_comments_edit = (EditText) findViewById(R.id.et_comments_edit);

        et_item_name_edit.setText(item.getName());
        et_brand_edit.setText(item.getBrand());
        et_amount_edit.setText(String.valueOf(item.getQuantidade()));
        et_price_edit.setText(item.getPrice());
        et_unit_edit.setText(item.getUnidade());
        et_comments_edit.setText(item.getObservacoes());

    }

    public void bl_new_item_edit(View view) {
            if (et_item_name_edit.getText().toString().length() > 0) {
                Item nItem = new Item(et_item_name_edit.getText().toString(),
                        item.getShopName(),
                        et_brand_edit.getText().toString(),
                        et_price_edit.getText().toString(),
                        Float.valueOf(et_amount_edit.getText().toString()),
                        et_comments_edit.getText().toString(),
                        et_unit_edit.getText().toString(),
                        null,
                        null);
                if(!Utils.updateItem(nItem, item.getName() ,getApplicationContext())){
                    finish();
                }
                else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("item", nItem.getName() + ":" + String.valueOf(nItem.getQuantidade()) + nItem.getUnidade() + ":" + nItem.getBrand());
                    resultIntent.putExtra("index", index);
                    resultIntent.putExtra("old", old);
                    resultIntent.putExtra("removed", 0);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            } else
                Toast.makeText(this, R.string.erro_fill_store_name, Toast.LENGTH_SHORT).show();
    }

    public void bl_cancel_new_item_edit(View view) {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == R.id.mIRemoveItem){
            Utils.removeItemFromDB(item, getApplicationContext());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("removed", 1);
            resultIntent.putExtra("index", index);
            resultIntent.putExtra("old", old);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

}
