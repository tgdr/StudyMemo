package buu.njj.studymemo;

import android.content.Context;

public class DensityUtils {

    /**
     * 将dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context,float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     * @param context
     * @param pxValue
     * @return
     */

   
    public static int px2dp(Context context, float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
