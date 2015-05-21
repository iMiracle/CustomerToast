package com.xuwt.customertoast;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by xuweitao on 2015/5/21.
 */
public class CustomerToast {

    private static final int MESSAGE_TIMEOUT = 1001;
    private static final int DEFAULT_DURATION = 2;

    private Context mContext;
    private WindowManager.LayoutParams mParams;
    private View mView;
    private WindowManager mWM;
    private ToastHandler mHandler;
    private int mDuration;

    int mGravity;
    int mX, mY;

    public CustomerToast(Context context) {
        mContext = context;

        initToast();
    }

    private void initToast() {
        mParams = new WindowManager.LayoutParams();
        mHandler = new ToastHandler();
        mDuration = DEFAULT_DURATION;

        mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mY = UnitUtils.dip2pix(mContext,64);
    }

    public static CustomerToast makeText(Context context, CharSequence text, int duration) {
        CustomerToast result = new CustomerToast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification, null);
        TextView tv = (TextView)v.findViewById(R.id.message);
        tv.setText(text);

        result.mView = v;
        result.mDuration = duration;


        return result;
    }
    public static CustomerToast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public static CustomerToast makeText(Context context, CharSequence text) {
      return makeText(context,text,DEFAULT_DURATION);
    }
    public static CustomerToast makeText(Context context, int resId)
            throws Resources.NotFoundException {
        return makeText(context,resId,DEFAULT_DURATION);
    }
    public void setView(View view) {
        this.mView = view;
    }
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        mGravity = gravity;
        mX = xOffset;
        mY = yOffset;
    }


    public void show() {


        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.custom_toast_anim_view;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        final Configuration config = mView.getContext().getResources().getConfiguration();
        final int gravity = Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
        mParams.gravity = gravity;
        if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
            mParams.horizontalWeight = 1.0f;
        }
        if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
            mParams.verticalWeight = 1.0f;
        }
        mParams.x = mX;
        mParams.y = mY;

        mWM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);

        if (mView.getParent() != null) {
            mWM.removeView(mView);
        }
        mWM.addView(mView, mParams);
        mHandler.sendEmptyMessageDelayed(MESSAGE_TIMEOUT, (long) (mDuration * 1000));
    }

    public void cancel() {
        mWM.removeView(mView);

        if (mView != null) {
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }

            mView = null;
        }
    }

    private class ToastHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MESSAGE_TIMEOUT:
                    cancel();
                    break;
            }
        }
    }
}
