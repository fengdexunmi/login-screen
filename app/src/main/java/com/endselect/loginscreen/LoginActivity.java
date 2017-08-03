package com.endselect.loginscreen;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attachKeyboardListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (keyboardListenersAttached) {
            detachKeyboardListeners();
        }
    }

    // ========= keyboard visibility event ========

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private int logoHeight = -1;

    /**
     * 显示键盘
     *
     */
    protected void onShowKeyboard() {
        ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(rootLayout, "translationY", -logoHeight);
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    /**
     * 隐藏键盘
     */
    protected void onHideKeyboard() {
        ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(rootLayout, "translationY", 0);
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    /**
     * 开始键盘监听事件
     */
    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = (ViewGroup) findViewById(R.id.layout_login);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        keyboardListenersAttached = true;
    }

    /**
     * 解除键盘监听事件
     */
    protected void detachKeyboardListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
        } else {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }

    /**
     * 键盘布局监听（键盘显示和隐藏的监听）
     */
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            // 最顶层视图的高度（手机屏幕的高度）
            int windowHeight = getActivityRoot(LoginActivity.this).getRootView().getHeight();
            if (logoHeight == -1) {
                logoHeight = findViewById(R.id.layout_logo).getHeight();
            }

            // getWindowVisibleDisplayFrame是从StatusBar和NavigationBar之间的区域
            Rect r = new Rect();
            getActivityRoot(LoginActivity.this).getWindowVisibleDisplayFrame(r);
            int windowVisibleHeight = r.height();

            // 这里我们的差值取100dp（超过StatusBar和NavigationBar的高度和）
            int heightDiff = (int) (getResources().getDisplayMetrics().density * 100);
            if (windowHeight - windowVisibleHeight < heightDiff) {
                onHideKeyboard();
            } else {
                onShowKeyboard();
            }
        }
    };

    /**
     * Activity content视图的第一个子视图
     * @param activity
     * @return
     */
    private View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

}
