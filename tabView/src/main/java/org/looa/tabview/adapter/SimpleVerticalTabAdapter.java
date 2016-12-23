package org.looa.tabview.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.looa.tabview.widget.TabBaseAdapter;


/**
 * A simple adapter for VerticalTabView.
 * Created by looa on 2016/12/20.
 */

public class SimpleVerticalTabAdapter extends TabBaseAdapter {
    private View preView;
    private View cursor;

    private ViewGroup viewGroup;

    private boolean hasAddCursor = false;
    private OnItemClickListener onItemClickListener;

    private int tabHeight = 46;

    private int offY;

    public SimpleVerticalTabAdapter(Context context) {
        offY = dip2px(context, tabHeight) / 2 - dip2px(context, 24) / 2;
        cursor = new View(context);
        cursor.setBackgroundColor(Color.parseColor("#b60909"));
        cursor.setTranslationY(offY);
    }

    @Override
    protected View onCreateTabView(ViewGroup parentView, int viewType, int position) {
        if (viewGroup == null) viewGroup = parentView;
        TextView textView = new TextView(parentView.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dip2px(parentView.getContext(), tabHeight));
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTextSize(14f);
        textView.setPadding(0, 0, 0, 0);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private Animation animReduce, animIncrease;
    private ObjectAnimator animTranslate;
    private long duration = 250;

    @Override
    protected void onSelectedTabView(View tabView, int position, boolean isSmooth) {
        if (!hasAddCursor) {
            RelativeLayout.LayoutParams paramsTab = new RelativeLayout.LayoutParams(dip2px(tabView.getContext(), 3), dip2px(tabView.getContext(), 24));
            viewGroup.addView(cursor, paramsTab);
            hasAddCursor = true;
        }
        if ((animIncrease != null && !animIncrease.hasEnded()) || (animReduce != null && !animReduce.hasEnded()))
            return;
        if (preView != null && preView == tabView) return;

        animIncrease = new ScaleAnimation(1, 1.2f, 1, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animIncrease.setDuration(duration);
        animIncrease.setFillAfter(true);

        animTranslate = ObjectAnimator.ofFloat(cursor, "translationY", cursor.getY(), tabView.getY() + offY);
        animTranslate.setDuration(duration);
        animTranslate.start();

        tabView.startAnimation(animIncrease);
        ((TextView) tabView).setTextColor(Color.parseColor("#b60909"));


        if (onItemClickListener != null) onItemClickListener.onItemClick(tabView, position);

        if (preView != null && preView != tabView) {
            animReduce = new ScaleAnimation(1.2f, 1, 1.2f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animReduce.setDuration(duration);
            animReduce.setFillBefore(true);
            preView.startAnimation(animReduce);
            ((TextView) preView).setTextColor(Color.parseColor("#333333"));
        }
        preView = tabView;
    }

    @Override
    protected void resetTabView(View tabView, int position) {
        ((TextView) tabView).setText((String) getData(position));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static int px2sp(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
