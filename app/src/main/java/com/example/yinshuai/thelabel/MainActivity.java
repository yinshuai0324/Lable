package com.example.yinshuai.thelabel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Lable lable;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lable= (Lable) findViewById(R.id.lable);
        lable.setDataList(getData());

        lable.setOnItemSelectClickListener(new Lable.OnItemSelectClickListener() { //设置item选中监听
            @Override
            public void selectclick(String text, int position) {
                Toast.makeText(MainActivity.this,"选中了:"+text+"---位置:"+position,Toast.LENGTH_SHORT).show();
            }
        });

        lable.setOnCancelSelectClickListener(new Lable.OnCancelSelectClickListener() {//设置取消item选中监听
            @Override
            public void cancelselectclick(String text, int position) {
                Toast.makeText(MainActivity.this,"取消选中:"+text+"---位置:"+position,Toast.LENGTH_SHORT).show();
            }
        });

        lable.setOnCancelAllSelectListener(new Lable.OnCancelAllSelectListener() {
            @Override
            public void cancelalllistener() {
                Toast.makeText(MainActivity.this,"取消全部选中",Toast.LENGTH_SHORT).show();
            }
        });

        textView= (TextView) findViewById(R.id.content);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("选中的内容:"+lable.getSelectContent().toString()); //获取选中的全部内容
            }
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lable.clearSelect(); //取消全部选中
            }
        });
    }


    /**
     * 获取模拟数据
     * @return
     */
    public List<String> getData() {
        List<String> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            switch (random.nextInt(10)) {
                case 0:
                    list.add("干净卫生");
                    break;
                case 1:
                    list.add("食材新鲜");
                    break;
                case 2:
                    list.add("分量足");
                    break;
                case 3:
                    list.add("味道好");
                    break;
                case 4:
                    list.add("包装精美");
                    break;
                case 5:
                    list.add("很实惠");
                    break;
                case 6:
                    list.add("配送快");
                    break;
                case 7:
                    list.add("态度很好");
                    break;
                case 8:
                    list.add("主动联系");
                    break;
                case 9:
                    list.add("餐品保存完好");
                    break;
                default:
                    list.add("送货上门");
                    break;
            }
        }
        return list;
    }
}
