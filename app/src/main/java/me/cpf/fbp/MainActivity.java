package me.cpf.fbp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    boolean a = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person p = new Person();
                p.activity = MainActivity.this;
                p.getName();

                //test();
            }
        });
    }

    public void test() {
        test1();
        test2();
    }

    public void test1() {
        if (a) {
            return;
        }
    }
    public void test2() {
        Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
    }


    public static void main(String args[]) {
        String str = "com.tr.aa.bb";
        String[] strings = str.split("\\.");
        for(int i = 0; i <strings.length; i++) {
            System.out.println("Hello==" + strings[i]);
        }
        System.out.println("Hello World==" + Matcher.quoteReplacement(File.separator));
    }
}
