package cn.wzbos.android.widget.linked;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

class Utils {

    static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null)
            return 0;
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    static int dp2px(Context context, float dpValue) {
        if (dpValue == 0)
            return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
