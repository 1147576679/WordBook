package com.example.zjy.wordbook;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjy.mydatabase.Mydatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Button buttonAdd;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private View view;
    private EditText etword,etfanyi;
    private Mydatabase mydatabase;
    private SQLiteDatabase sqLiteDatabase;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> arrayAdapter;

//    private List<WordEntity> wordEntities;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
        queryDatas();
    }

    public void init(){
        buttonAdd = (Button) findViewById(R.id.btn_add);
        buttonAdd.setOnClickListener(this);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.actv);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.addTextChangedListener(this);
        textView = (TextView) findViewById(R.id.textview);
        view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,null);
        etword = (EditText) view.findViewById(R.id.etword);
        etfanyi = (EditText) view.findViewById(R.id.etfanyi);
        mydatabase = new Mydatabase(this);
        sqLiteDatabase = mydatabase.getReadableDatabase();
        builder = new AlertDialog.Builder(this);
        alertDialog = builder.setIcon(R.mipmap.yqg)
                .setTitle("添加新词")
                .setMessage("单词库")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newword = etword.getText().toString();
                        String chinese = etfanyi.getText().toString();

                        if(TextUtils.isEmpty(newword)||TextUtils.isEmpty(chinese)){
                            Toast.makeText(MainActivity.this, "单词或者翻译不能为空", Toast.LENGTH_SHORT).show();
                        }else{
                            long id = saveDatatoDatabase(newword, chinese);
                            if(id > 0){
                                Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                arrayAdapter.add(newword);
                                arrayAdapter.notifyDataSetChanged();
                                etword.setText("");
                                etfanyi.setText("");

                                dialog.dismiss();
                            }else{
                                Toast.makeText(MainActivity.this, "添加失败，我哪知道发生了什么", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    /**
     * 添加单词的按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        alertDialog.show();
    }

    /**
     * 保存数据到数据库
     */
    public long saveDatatoDatabase(String newword,String chinese){
        ContentValues contentValues = new ContentValues();
        contentValues.put("newword",newword);
        contentValues.put("chinese",chinese);
        return sqLiteDatabase.insert("word",null,contentValues);
    }

    public void queryDatas(){
//        wordEntities = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("word", new String[]{"_id", "newword", "chinese"}, null, null, null, null, null);
        while(cursor.moveToNext()){
//            WordEntity wordEntity = new WordEntity();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String newword = cursor.getString(cursor.getColumnIndex("newword"));
            String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
//            wordEntity.setId(id);
//            wordEntity.setWord(newword);
//            wordEntity.setChinese(chinese);
//            wordEntities.add(wordEntity);
            arrayAdapter.add(newword);
            arrayAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Cursor cursor = sqLiteDatabase.query("word", new String[]{"chinese"}, "newword = ?", new String[]{s + ""}, null, null, null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
                textView.setText(chinese);
            }
        }else{
            textView.setText("");
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sqLiteDatabase != null){
            sqLiteDatabase.close();
        }
    }
}
