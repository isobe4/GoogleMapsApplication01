package com.app.isobenaoya.googlemapsapplication01;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface;
import android.widget.Spinner;

import java.util.Calendar;

public class RegisterActivity extends Activity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onStart(){
        super.onStart();

        Button regBtn = (Button)findViewById(R.id.button);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner spAdd = (Spinner) findViewById(R.id.addspinner);
                String addr = spAdd.getSelectedItem().toString();
                Spinner spShop = (Spinner) findViewById(R.id.shopspinner);
                String shop = spShop.getSelectedItem().toString();
                EditText etName = (EditText) findViewById(R.id.nameeditText);
                String name = etName.getText().toString();
                EditText etShopNum = (EditText) findViewById(R.id.shopnumeditText);
                String shopnum = etShopNum.getText().toString();

                MyDBHelper helper = new MyDBHelper(RegisterActivity.this);
                db = helper.getWritableDatabase();

                String reg[] = {
                        addr,
                        shop,
                        name,
                        Calendar.getInstance().getTime().toString(),
                        shopnum
                };
                if (db != null) {
                    try {
                        insertToDB(reg);
                        Intent regIntent = new Intent(RegisterActivity.this, AddListActivity.class);
                        startActivity(regIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void insertToDB(String regData[]) throws Exception{
        db.execSQL("insert into " + MyDBHelper.TABLE_NAME
                + "("
                    + MyDBHelper.REG_ADDR + ","
                    + MyDBHelper.REG_SHOP + ","
                    + MyDBHelper.REG_NAME + ","
                    + MyDBHelper.REG_TIME + ","
                    + MyDBHelper.REG_SHOPNUM
                + ")values('"
                    + regData[0] + "','"
                    + regData[1] + "','"
                    + regData[2] + "','"
                    + regData[3] + "','"
                    + regData[4]
                + "');");
    }
}
