package com.example.sqlitememo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private DbAdapter dbAdapter;
    ListView memo_list;
    TextView no_memo;
    ArrayList<Memo> memos = new ArrayList<>();
    Cursor cursor;
    private ListAdapter dataSimpleAdapter;
    int item_id;
    private AlertDialog dialog = null;
    AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memo_list = findViewById(R.id.memoList);
        dbAdapter = new DbAdapter(this);
        no_memo = findViewById(R.id.no_memo);

        //判斷目前是否有備忘錄並設定顯示元件，如果沒有，就顯示「目前無資料」
        if(dbAdapter.listMemos().getCount() == 0){
            memo_list.setVisibility(View.INVISIBLE);
            no_memo.setVisibility(View.VISIBLE);
        }else{
            memo_list.setVisibility(View.VISIBLE);
            no_memo.setVisibility(View.INVISIBLE);
        }
        displayList(); // 顯示所有 memo 資料

        memo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.move(position);
                item_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                intent = new Intent();
                intent.putExtra("item_id",item_id);
                intent.putExtra("type","EDIT");
                intent.setClass(MainActivity.this,EditMemoActivity.class);
                startActivity(intent);
            }
        });
        memo_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.move(position);
                item_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        builder = new AlertDialog.Builder(this);
        builder.setTitle("訊息")
                .setMessage("確定刪除此便條？")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    // 設定確定按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean is_deleted = dbAdapter.deleteMemo(item_id);
                        if(is_deleted){
                            Toast.makeText(MainActivity.this,"已刪除一筆資料 ! ", Toast.LENGTH_SHORT).show();
                            memos = new ArrayList<>();
                            displayList();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void displayList() {
        // 在 ListView 上顯示所有 memo 資料
        cursor = dbAdapter.listMemos();
        if(cursor.moveToFirst()){
            do{
                memos.add(new Memo(
                        cursor.getInt(cursor.getColumnIndexOrThrow("_id")), // cursor.getInt(0)
                        cursor.getString(cursor.getColumnIndexOrThrow("date")), // cursor.getString(1)
                        cursor.getString(cursor.getColumnIndexOrThrow("memo")), // cursor.getString(2)
                        cursor.getString(cursor.getColumnIndexOrThrow("bgcolor")))); // cursor.getString(3)
            }while (cursor.moveToNext());
        }
        cursor.moveToFirst();
        dataSimpleAdapter = new ListAdapter(this,memos);
        memo_list.setAdapter(dataSimpleAdapter);
    }


    // ------ 設定 Menu 開始 ------ //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.add:
                intent = new Intent(MainActivity.this,EditMemoActivity.class);
                intent.putExtra("type","ADD"); // 要加這句 ，不然在 EditMemoActivity 的 btn_OK 無法進行判斷式（判斷要新增還是編輯備忘）
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // ------ 設定 Menu 結束 ------ //

}
