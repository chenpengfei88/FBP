package me.cpf.fbp;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/7/5.
 */
public class Test {

    public static void da(Activity activity, String name, String value) {
        Toast.makeText(activity, name + '=' + value, Toast.LENGTH_SHORT).show();
    }

    public static void da(String name, String value) {
    }
}
