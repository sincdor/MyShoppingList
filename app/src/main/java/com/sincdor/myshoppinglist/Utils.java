package com.sincdor.myshoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by andre on 1/26/17.
 */

public class Utils {

    public static String getDate(){
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    //Table Stores
    public static boolean addShopToDB(Context appContext, String shopName, Integer i){
        if(!isStore(shopName, appContext)) {
            try {
                DBHelper dbHelper = new DBHelper(appContext);
                SQLiteDatabase bd = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("_id", i);
                values.put("shop_name", shopName);
                values.put("item_number", 0);
                bd.insert("table_shop_list", null, values);
                dbHelper.close();
                bd.close();
                return true;
            } catch (Exception e) {
                Log.e("USERINFODB ERROR", "Error Creating Database");
                return false;
            }
        }
        return false;
    }
    public static boolean deleteShopFromDB(String shopName, Context appContext){

        DBHelper db = new DBHelper(appContext);
        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM table_shop_list WHERE shop_name LIKE '%" + shopName + "%';");
        sql.execSQL("DELETE FROM table_items_list WHERE item_store_name LIKE '%" + shopName + "%';");

        return true;
    }
    static boolean isStore(String sName, Context appContext){
        DBHelper bdh = new DBHelper(appContext);

        SQLiteDatabase bd = bdh.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = bd.query("table_shop_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return false;
        }
        int iShopName = cursor.getColumnIndex("shop_name");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(iShopName) != null && cursor.getString(iShopName).equals(sName)) {
                cursor.close();
                bd.close();
                bdh.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        bdh.close();
        return false;
    }

    //Table Items
    public static boolean addItemToDB(Item item, Context appContext){
        if(!isItem(item.getName(), item.getShopName(), appContext)) {
            try {
                DBHelper dbHelper = new DBHelper(appContext);
                SQLiteDatabase bd = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("item_name", item.getName());
                values.put("item_store_name", item.getShopName());
                values.put("quantidade", item.getQuantidade());
                values.put("item_marca", item.getBrand());
                values.put("observacoes", item.getObservacoes());
                values.put("unidade", item.getUnidade());
                values.put("comprado", item.getComprado());
                values.put("date", getDate());
                values.put("price", item.getPrice());
                bd.insert("table_items_list", null, values);

                dbHelper.close();
                bd.close();
            } catch (Exception e) {
                Log.e("USERINFODB ERROR", "Error Creating Database");
            }
            File database = appContext.getDatabasePath("shoppinglist.db");
            if (database.exists()) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
    public static boolean removeItemFromDB(Item item, Context appContext){

        if(!isItem(item.getName(), item.getShopName(), appContext))
            return false;

        DBHelper db = new DBHelper(appContext);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM table_items_list WHERE item_store_name LIKE '%" + item.getShopName() + "%' AND item_name LIKE '%" + item.getName() +  "%'");
        return true;
    }
    static boolean isItem(String item_name, String shopName,  Context appContext){

        DBHelper bdh = new DBHelper(appContext);

        SQLiteDatabase bd = bdh.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = bd.query("table_items_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return false;
        }
        int iName = cursor.getColumnIndex("item_name");
        int iShopName = cursor.getColumnIndex("item_store_name");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(iName) != null && cursor.getString(iName).equals(item_name) &&
                    cursor.getString(iShopName) != null && cursor.getString(iShopName).equals(shopName) ) {
                cursor.close();
                bd.close();
                bdh.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        bdh.close();
        return false;
    }
    static boolean updateItem(Item item, String oldName,Context appContext){
        if(isItem(oldName, item.getShopName(), appContext)) {
            DBHelper db = new DBHelper(appContext);
            SQLiteDatabase sql = db.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("item_store_name", item.getShopName()); //These Fields should be your String values of actual column names
            cv.put("item_name", item.getName());
            cv.put("item_marca", item.getBrand());
            cv.put("price", item.getPrice());
            cv.put("quantidade", item.getQuantidade());
            cv.put("observacoes", item.getObservacoes());
            cv.put("unidade", item.getUnidade());
            cv.put("date", getDate());
            cv.put("comprado", item.getComprado());
            sql.update("table_items_list", cv, "item_name LIKE '%" + oldName + "%'", null);
            db.close();
            sql.close();
            return true;
        }else
            return false;
    }
    static Item getItemFromDB(String name, String storeName, Context appContext){
        Float quantidade;
        String brand;
        String comments;
        String unit;
        Integer comprado;
        String price;
        String date;


        DBHelper bdh = new DBHelper(appContext);

        SQLiteDatabase bd = bdh.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = bd.query("table_items_list", null, null, null, null, null, null);
        } catch (Exception e) {
            return null;
        }
        int iName = cursor.getColumnIndex("item_name");
        int iShopName = cursor.getColumnIndex("item_store_name");
        int iQuantidade = cursor.getColumnIndex("quantidade");
        int iBrand = cursor.getColumnIndex("item_marca");
        int iComments = cursor.getColumnIndex("observacoes");
        int iUnit = cursor.getColumnIndex("unidade");
        int iComprado = cursor.getColumnIndex("comprado");
        int iPrice = cursor.getColumnIndex("price");
        int iDate = cursor.getColumnIndex("date");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(iName) != null && cursor.getString(iName).equals(name) &&
                    cursor.getString(iShopName) != null && cursor.getString(iShopName).equals(storeName) ) {

                quantidade = cursor.getFloat(iQuantidade);
                brand = cursor.getString(iBrand);
                comments = cursor.getString(iComments);
                unit = cursor.getString(iUnit);
                comprado = cursor.getInt(iComprado);
                price = cursor.getString(iPrice);
                date = cursor.getString(iDate);

                cursor.close();
                bd.close();
                bdh.close();
                return new Item(name, storeName, brand, price, quantidade, comments, unit, date, comprado);
            }
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        bdh.close();
        return null;
    }
}
