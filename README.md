# login-screen
登录界面-键盘弹起的时候，隐藏logo，输入框上移

# 键盘布局监听的关键代码

```java
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
```

# 效果如下

![键盘布局监听效果](https://github.com/fengdexunmi/login-screen/raw/master/screenshots/ezgif-1-bc51ccb71a.gif)