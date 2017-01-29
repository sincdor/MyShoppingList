package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DialogItem extends Activity {
    Item item;
    Integer index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_item);

        Intent it = getIntent();
        item = (Item)it.getSerializableExtra("item");
        index = it.getExtras().getInt("index");
    }

    public void bl_d_edit_item(View view) {


        Intent it = new Intent(this, EditItemActivity.class);
        it.putExtra("item", item);
        it.putExtra("old", item.getName());
        it.putExtra("index", index);
        startActivity(it);
        finish();
    }

    public void bl_d_delete_item(View view) {
        Utils.removeItemFromDB(item, this);
        finish();
    }

    public void bl_d_cancel_item(View view) {
        finish();
    }
}
