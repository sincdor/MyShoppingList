package com.sincdor.myshoppinglist;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shoppinglist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "_id";
    private static final String KEY_ITEM_NUMBER = "item_number";
    private static final String KEY_NOME = "shop_name";
    private static final String DICTIONARY_TABLE_NAME = "table_shop_list";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    KEY_ID + " NUMBER, " +
                    KEY_ITEM_NUMBER + " NUMBER, " +
                    KEY_NOME + " TEXT);";


    private static final String KEY_ID_MSG = "_id";
    private static final String KEY_STORE_NAME = "item_store_name";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_MARCA = "item_marca";
    private static final String KEY_PRICE= "price";
    private static final String KEY_QUANTIDADE = "quantidade";
    private static final String KEY_OBSERVACOES = "observacoes";
    private static final String KEY_UNIDADE = "unidade";
    private static final String KEY_DATE = "date";
    private static final String KEY_COMPRADO = "comprado";
    private static final String DICTIONARY_TABLE_MSG = "table_items_list";
    private static final String DICTIONARY_TABLE_CREATE2 =
            "CREATE TABLE " + DICTIONARY_TABLE_MSG + " (" +
                    KEY_ID_MSG + " NUMBER, " +
                    KEY_STORE_NAME + " TEXT, " +
                    KEY_ITEM_NAME + " TEXT, " +
                    KEY_PRICE + " NUMBER, " +
                    KEY_QUANTIDADE + " NUMBER, " +
                    KEY_ITEM_MARCA + " TEXT, " +
                    KEY_DATE + " TEXT, " +
                    KEY_UNIDADE + " TEXT, " +
                    KEY_COMPRADO + " NUMBER, " +
                    KEY_OBSERVACOES + " TEXT);";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
        db.execSQL(DICTIONARY_TABLE_CREATE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
