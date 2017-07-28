package com.endselect.loginscreen;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

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
    private boolean isFirst = true;

    /**
     * 显示键盘
     * @param keyboardHeight
     */
    protected void onShowKeyboard(int keyboardHeight) {
        Toast.makeText(this, "onShowKeyboard", Toast.LENGTH_SHORT).show();
        ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(rootLayout, "translationY", -logoHeight);
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    /**
     * 隐藏键盘
     */
    protected void onHideKeyboard() {
        Toast.makeText(this, "onHideKeyboard", Toast.LENGTH_SHORT).show();
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
            int heightDiff = rootLayout.getRootView().getHeight();
//            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            if (logoHeight == -1) {
                logoHeight = findViewById(R.id.layout_logo).getHeight();
            }
            Rect r = new Rect();
            rootLayout.getWindowVisibleDisplayFrame(r);
            int contentViewTop = r.bottom - r.top;

            if (heightDiff - contentViewTop < 100) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    onHideKeyboard();
                }
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);
            }
        }
    };

}
