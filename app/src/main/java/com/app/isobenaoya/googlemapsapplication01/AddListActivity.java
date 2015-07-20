package com.app.isobenaoya.googlemapsapplication01;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import java.io.IOException;
import java.util.ArrayList;


public class AddListActivity extends Activity {

    private ListView myListView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
    }

    @Override
    public void onStart(){
        super.onStart();
        myListView = (ListView)findViewById(R.id.listview);
        final MyDBHelper dbHelper = new MyDBHelper(this);
        db=dbHelper.getReadableDatabase();

        //スウィッチの取得
        final Switch deSw = ((Switch)findViewById(R.id.deleteSwitch));
        final Switch upSw = ((Switch)findViewById(R.id.updateSwitch));

        if(db!=null) {
            try {
                Cursor c = serchToDB();
                String[] from = {
                        MyDBHelper.ID,
                        MyDBHelper.REG_ADDR,
                        MyDBHelper.REG_SHOP,
                        MyDBHelper.REG_NAME,
                        MyDBHelper.REG_TIME,
                        MyDBHelper.REG_SHOPNUM
                };
                int[] to = {
                        R.id.regId,
                        R.id.regAddr,
                        R.id.regShop,
                        R.id.regName,
                        R.id.regTime,
                        R.id.regShopNum
                };
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitem_layout, c, from, to, 0);
                myListView.setAdapter(adapter);
                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent addrIntent;
                        String id_text = ((TextView) view.findViewById(R.id.regId)).getText().toString();
                        String shop_text = ((TextView) view.findViewById(R.id.regShop)).getText().toString();
                        String addr_text = ((TextView) view.findViewById(R.id.regAddr)).getText().toString();
                        String shopnum_text = ((TextView) view.findViewById(R.id.regShopNum)).getText().toString();
                        String name_text = ((TextView) view.findViewById(R.id.regName)).getText().toString();
                        String time_text = ((TextView) view.findViewById(R.id.regTime)).getText().toString();
                        //スウィッチのON/OFF状態でアイテムをクリックしたときの移動先を制御
                        if (deSw.isChecked()) {
                            addrIntent = new Intent(AddListActivity.this, DeleteActivity.class);
                        }else if(upSw.isChecked()){
                            addrIntent = new Intent(AddListActivity.this, UpdateActivity.class);
                        } else {
                            addrIntent = new Intent(AddListActivity.this, MapsActivity.class);
                        }

                        addrIntent.putExtra("addr",addr_text);
                        addrIntent.putExtra("shop",shop_text);
                        addrIntent.putExtra("id",id_text);
                        addrIntent.putExtra("shopnum",shopnum_text);
                        addrIntent.putExtra("name",name_text);
                        addrIntent.putExtra("time",time_text);

                        startActivity(addrIntent);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }

        }

        //スウィッチの切り替えで両方ON状態にならないように制御
        deSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(deSw.isChecked()){
                    upSw.setChecked(false);
                }else if(upSw.isChecked()){
                    deSw.setChecked(false);
                }
            }
        });
        upSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(upSw.isChecked()){
                    deSw.setChecked(false);
                }else if(deSw.isChecked()){
                    upSw.setChecked(false);
                }
            }
        });


        //新規登録用ボタン
        Button addrRegBtn = (Button)findViewById(R.id.regButton);
        addrRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addrIntent = new Intent(AddListActivity.this, RegisterActivity.class);
                startActivity(addrIntent);
            }
        });

    }

    private Cursor serchToDB() throws Exception {
        Cursor c = db.rawQuery("select * from " + MyDBHelper.TABLE_NAME, null);
        return c;
    }
}
