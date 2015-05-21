package com.xuwt.customertoast;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        CustomerToast.makeText(MyActivity.this,"CustomerToast").show();
        showRefreshToast();
    }

    /*
  * 从布局文件中加载布局并且自定义显示Toast
  */
    public void showRefreshToast() {

        LayoutInflater inflater = LayoutInflater.from(MyActivity.this);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView)layout.findViewById(R.id.tvrefresh);
        textView.setText("刷新成功");

        CustomerToast toast = new CustomerToast(MyActivity.this);
        toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0
                , UnitUtils.dip2pix(MyActivity.this,50));
        toast.setView(layout);
        toast.show();
    }
}
