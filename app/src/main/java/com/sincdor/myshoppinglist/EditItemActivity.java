package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends Activity {

    Item item;
    EditText et_item_name_edit;
    EditText et_brand_edit;
    EditText et_amount_edit;
    EditText et_price_edit;
    EditText et_unit_edit;
    EditText et_comments_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent it = getIntent();
        item = (Item) it.getSerializableExtra("item");
        if(item == null)
            finish();
        et_item_name_edit = (EditText) findViewById(R.id.et_item_name_edit);
        et_brand_edit = (EditText) findViewById(R.id.et_brand_edit);
        et_amount_edit = (EditText) findViewById(R.id.et_amount_edit);
        et_price_edit = (EditText) findViewById(R.id.et_price_edit);
        et_unit_edit = (EditText) findViewById(R.id.et_unit_edit);
        et_comments_edit = (EditText) findViewById(R.id.et_comments_edit);

        et_item_name_edit.setText(item.getName());
        et_brand_edit.setText(item.getBrand());
        et_amount_edit.setText(String.valueOf(item.getQuantidade()));
        et_price_edit.setText(String.valueOf(item.getPrice()));
        et_unit_edit.setText(item.getUnidade());
        et_comments_edit.setText(item.getObservacoes());

    }

    public void bl_new_item_edit(View view) {
            if (et_item_name_edit.getText().toString().length() > 0) {
                Float price = null;
                Float amount = null;

                try {
                    if (et_amount_edit.getText().length() > 0) {
                        amount = Float.valueOf(et_amount_edit.getText().toString());
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(this, R.string.error_amount, Toast.LENGTH_SHORT).show();
                }

                try {
                    if (et_price_edit.getText().length() > 0) {
                        price = Float.valueOf(et_price_edit.getText().toString());
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(this, R.string.error_input_price, Toast.LENGTH_SHORT).show();
                }

                Item nItem = new Item(et_item_name_edit.getText().toString(),
                        item.getShopName(),
                        et_brand_edit.getText().toString(),
                        price,
                        amount,
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
                    resultIntent.putExtra("removed", 0);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            } else
                Toast.makeText(this, R.string.error_fill_store_name, Toast.LENGTH_SHORT).show();
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    public void bl_cancel_bought_item_edit(View view) {
        if(item.getComprado() == 0)
            item.setComprado(1);
        else
            item.setComprado(0);
        if(!Utils.updateItem(item, item.getName() ,getApplicationContext())){
            finish();
        }
        else{
            Intent resultIntent = new Intent();
            resultIntent.putExtra("bought", 0);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    public void hide_keyboard_edit(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
