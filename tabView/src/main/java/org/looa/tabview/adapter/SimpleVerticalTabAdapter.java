package org.looa.tabview.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.looa.tabview.widget.TabBaseAdapter;


/**
 * A simple adapter for VerticalTabView.
 * Created by looa on 2016/12/20.
 */

public class SimpleVerticalTabAdapter extends TabBaseAdapter<String> {
    private View preView;
    private final View cursor;

    private ViewGroup viewGroup;

    private boolean hasAddCursor = false;
    private OnItemClickListener onItemClickListener;

    private final int tabHeight = 50;

    private final int offY;

    private ObjectAnimator animTranslate;
    private ObjectAnimator animReduceX, animReduceY;
    private ObjectAnimator animIncreaseX, animIncreaseY;
    private AnimatorSet animSet;
    private final AnimListener animListener = new AnimListener();
    private long duration = 250;
    private boolean isFirstSelected = true;


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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                dip2px(parentView.getContext(), tabHeight));
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(13f);
        textView.setPadding(0, 0, 0, 0);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    protected void onSelectedTabView(View tabView, int position, boolean isSmooth) {
        if (!hasAddCursor) {
            RelativeLayout.LayoutParams paramsTab = new RelativeLayout.LayoutParams(dip2px(tabView.getContext(), 3),
                    dip2px(tabView.getContext(), 24));
            viewGroup.addView(cursor, paramsTab);
            hasAddCursor = true;
        }
        if ((animIncreaseX != null && animSet.isRunning()) || (animReduceX != null && animSet.isRunning()))
            return;
        if (preView != null && preView == tabView) {
            return;
        }

        animSet = new AnimatorSet();
        animIncreaseX = ObjectAnimator.ofFloat(tabView,
                "scaleX",
                1 / 1.1f,
                1f);
        animIncreaseY = ObjectAnimator.ofFloat(tabView,
                "scaleY",
                1 / 1.1f,
                1f);

        animTranslate = ObjectAnimator.ofFloat(cursor,
                "translationY",
                cursor.getY(),
                tabView.getY() + offY);

        ((TextView) tabView).setTextColor(Color.parseColor("#b60909"));

        if (onItemClickListener != null && !isFirstSelected) {
            onItemClickListener.onItemClick(tabView, position);
        }
        isFirstSelected = false;
        if (preView != null && preView != tabView) {
            ((TextView) preView).setTextColor(Color.parseColor("#000000"));
            animReduceX = ObjectAnimator.ofFloat(preView, "scaleX", 1f, 1 / 1.1f);
            animReduceY = ObjectAnimator.ofFloat(preView, "scaleY", 1f, 1 / 1.1f);
            animSet.play(animIncreaseX).with(animIncreaseY).with(animTranslate).with(animReduceX).with(animReduceY);
            animListener.setPreView((TextView) preView);
        } else {
            animSet.play(animIncreaseX).with(animIncreaseY).with(animTranslate);
        }
        animSet.setDuration(duration);
        animSet.addListener(animListener);
        animListener.setView((TextView) tabView);
        animSet.start();
        preView = tabView;
    }

    private static class AnimListener implements Animator.AnimatorListener {

        private TextView view;
        private TextView preView;

        public void setView(TextView view) {
            this.view = view;
        }

        public void setPreView(TextView preView) {
            this.preView = preView;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            view.setTextSize(14f);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (preView != null) {
                preView.setTextSize(13f);
                preView.setScaleX(1f);
                preView.setScaleY(1f);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
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
