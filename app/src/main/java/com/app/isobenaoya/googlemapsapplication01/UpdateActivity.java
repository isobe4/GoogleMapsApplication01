package com.app.isobenaoya.googlemapsapplication01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;


public class UpdateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void  onStart(){
        super.onStart();
        Intent updateIntent = getIntent();
        final String[] update = {
                updateIntent.getStringExtra("addr"),
                updateIntent.getStringExtra("shop"),
                updateIntent.getStringExtra("name"),
                updateIntent.getStringExtra("shopnum"),
                updateIntent.getStringExtra("id")
        };
        //元々入力していた値を初期値として設定
        //住所の初期値設定
        final Spinner upAdd = (Spinner) findViewById(R.id.upAddspinner);
        String[] add =getResources().getStringArray(R.array.addlist);
        for(int i = 0;add.length>i;i++) {
            if(update[0].equals(add[i])) {
                upAdd.setSelection(i);
                break;
            }
        }
        //種類の初期値設定
        final Spinner upShop = (Spinner) findViewById(R.id.upShopspinner);
        String[] shop =getResources().getStringArray(R.array.shoplist);
        for(int i = 0;shop.length>i;i++) {
            if(update[1].equals(shop[i])) {
                upShop.setSelection(i);
                break;
            }
        }

        //登録者の初期値設定
        final EditText upName = (EditText) findViewById(R.id.upNameeditText);
        upName.setText(update[2]);
        //検索件数の初期値設定
        final EditText upShopNum = (EditText) findViewById(R.id.upShopNumeditText);
        upShopNum.setText(update[3]);
        //修正ボタンのクリック判定
        Button upBtn = (Button)findViewById(R.id.upbutton);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upIntent = new Intent(UpdateActivity.this, AddListActivity.class);
                update[0] = upAdd.getSelectedItem().toString();
                update[1] = upShop.getSelectedItem().toString();
                update[2] = upName.getText().toString();
                update[3] = upShopNum.getText().toString();
                updateToDB(update);
                startActivity(upIntent);
            }
        });
        //キャンセルボタンのクリック判定
        Button backBtn = (Button)findViewById(R.id.backbutton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upIntent = new Intent(UpdateActivity.this,AddListActivity.class);
                startActivity(upIntent);
            }
        });
    }

    public void updateToDB(String upDate[]){
        MyDBHelper dbHelper = new MyDBHelper(UpdateActivity.this);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //編集用SQL
        db.execSQL("update "+MyDBHelper.TABLE_NAME + " set "
                + MyDBHelper.REG_ADDR + " = '"+upDate[0]+"', "
                + MyDBHelper.REG_SHOP + " = '"+upDate[1]+"', "
                + MyDBHelper.REG_NAME + " = '"+upDate[2]+"', "
                + MyDBHelper.REG_SHOPNUM + " = '"+upDate[3]+"' "
                + "where " + MyDBHelper.ID + " = '"+upDate[4]+"'"
        );
    }
}
