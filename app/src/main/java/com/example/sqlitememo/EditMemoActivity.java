package com.example.sqlitememo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditMemoActivity extends AppCompatActivity implements View.OnClickListener {
    // 新增跟編輯備忘錄都在這個 Activity 中執行
    Intent intent;
    TextView txt_title;
    EditText edt_memo;
    Spinner sp_colors;
    Button btn_back, btn_ok;
    ArrayList<ColorData> color_list = null;
    SpinnerAdapter spinnerAdapter;
    String selected_color; // 記錄目前所選的顏色
    private DbAdapter dbAdapter;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String new_memo, currentTime;
    int index;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        initView(); // 把 findViewById 另外獨立出來
        dbAdapter = new DbAdapter(this);
        Log.i("datetime=", new Date(System.currentTimeMillis()).toString());

        bundle = this.getIntent().getExtras();
        // 判斷目前是否為編輯狀態
        if (bundle.getString("type").equals("EDIT")) {
            txt_title.setText("編輯便條");
            index = bundle.getInt("item_id");
            Cursor cursor = dbAdapter.queryByID(index);
            edt_memo.setText(cursor.getString(cursor.getColumnIndexOrThrow("memo")));
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (color_list.get(i).code.equals(cursor.getString(cursor.getColumnIndexOrThrow("bgcolor")))) {
                    sp_colors.setSelection(i);
                }
            }
        }
    }

    private void initView() {
        txt_title = findViewById(R.id.txtTitle);
        edt_memo = findViewById(R.id.edtMemo);
        sp_colors = findViewById(R.id.spColors);
        btn_back = findViewById(R.id.btn_back);
        btn_ok = findViewById(R.id.btn_ok);
        btn_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        color_list = new ArrayList<ColorData>();
        color_list.add(new ColorData("PAPAYAWHIP", "#FFEFD5"));
        color_list.add(new ColorData("MOCCASIN", "#FFE4B5"));
        color_list.add(new ColorData("PEACHPUFF", "#FFDAB9"));
        color_list.add(new ColorData("PALEGOLDENROD", "#EEE8AA"));
        color_list.add(new ColorData("KHAKI", "#F0E68C"));
        color_list.add(new ColorData("DARKKHAKI", "#BDB76B"));

        spinnerAdapter = new SpinnerAdapter(color_list, this);
        sp_colors.setAdapter(spinnerAdapter);


        sp_colors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //取得選取的顏色代碼
                selected_color = color_list.get(position).code; // 記錄目前所選的顏色
                //selected_color = color_list.get(position).getCode();
                Log.d("color=", color_list.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                // 返回 MainActivity
                intent = new Intent(EditMemoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_ok:
                //取得edit資料
                new_memo = edt_memo.getText().toString();
                Log.i("memo=", new_memo);
                currentTime = df.format(new Date(System.currentTimeMillis())); // 取得並格式化目前的日期和時間

                // 判斷要新增還是編輯備忘
                if (bundle.getString("type").equals("EDIT")) {
                    try {
                        //  編輯備忘 （更新資料庫中的資料）
                        dbAdapter.updateMemo(index, currentTime, new_memo, null, selected_color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 返回列表
                        intent = new Intent(EditMemoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    try {
                        // 新增資料
                        dbAdapter.createMemo(currentTime, new_memo, null, selected_color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 返回列表
                        intent = new Intent(EditMemoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}
