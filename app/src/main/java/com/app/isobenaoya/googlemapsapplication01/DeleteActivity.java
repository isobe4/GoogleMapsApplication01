package com.app.isobenaoya.googlemapsapplication01;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class DeleteActivity extends Activity {

    //����delete���������邾���̃A�N�e�B�r�e�B�B
    //���̃A�N�e�B�r�e�B���\������邱�Ƃ͂Ȃ��B
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Intent deleteIntent = getIntent();
        String id = deleteIntent.getStringExtra("id");

        MyDBHelper dbHelper = new MyDBHelper(this);

        if(dbHelper!=null&&id!=null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.deleteToDB(db, id);
        }

        Intent moveIntent = new Intent(DeleteActivity.this,AddListActivity.class);
        startActivity(moveIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
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


}
