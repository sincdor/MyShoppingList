package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

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

    public void bl_new_item(View view) {
        Float quantidade = null;
        Float price = null;
        String name = et_name.getText().toString();
        try {
            if(et_amount.getText().length() > 0)
                quantidade = Float.valueOf(et_amount.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(this, R.string.error_amount, Toast.LENGTH_SHORT).show();
        }
        String brand = et_brand.getText().toString();
        String comments = et_comments.getText().toString();
        String unit = et_unit.getText().toString();
        Integer comprado = 0;
        try {
            if(et_price.getText().length() > 0)
                price = Float.valueOf(et_price.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(this, R.string.error_input_price, Toast.LENGTH_SHORT).show();
        }
        if (name.length() > 0) {
            Item item = new Item(name, store, brand, price, quantidade, comments, unit, null, comprado);

            Utils.addItemToDB(item, getApplicationContext());

            File database = getApplicationContext().getDatabasePath("shoppinglist.db");
            if (database.exists()) {
                Toast.makeText(NewItemActivity.this, "[DATABASE] - Created", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                assert quantidade != null;
                resultIntent.putExtra("new_item", name + ":" + quantidade.toString() + unit + ":" + brand);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(NewItemActivity.this, "[DATABASE] - Not Created", Toast.LENGTH_SHORT).show();
                Toast.makeText(NewItemActivity.this, "[APP] - I Will exit now", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }

        } else {
            Toast.makeText(this, R.string.error_fill_store_name, Toast.LENGTH_SHORT).show();
            et_name.setText("");
        }
    }

    public void bl_cancel_new_item(View view) {
        finish();
    }
}
