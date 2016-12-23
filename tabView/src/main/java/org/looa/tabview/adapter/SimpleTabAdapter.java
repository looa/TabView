package org.looa.tabview.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.looa.tabview.widget.TabBaseAdapter;


/**
 * A simple adapter for TabView.
 * Contain a textView and a cursor.
 * <p>
 * Created by looa on 2016/12/20.
 */

public class SimpleTabAdapter extends TabBaseAdapter {
    private View preView;
    private View cursor;

    private ViewGroup viewGroup;

    private OnItemClickListener onItemClickListener;

    private int cursorWidth = 39;//只在初始化的是dp，其他情况都是px
    private int colorSelected;
    private int colorUnSelected;

    public SimpleTabAdapter(Context context) {
        colorSelected = Color.parseColor("#b60909");
        colorUnSelected = Color.parseColor("#333333");
    }

    @Override
    protected View onCreateTabView(ViewGroup parentView, int viewType, int position) {
        if (viewGroup == null) viewGroup = parentView;
        int padding = dip2px(parentView.getContext(), 20);
        TextView textView = new TextView(parentView.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(colorUnSelected);
        textView.setTextSize(13f);
        textView.setPadding(padding, 0, padding, 0);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    protected void onSelectedTabView(View tabView, int position, boolean isSmooth) {
        if (preView != null && preView == tabView) return;

        if (onItemClickListener != null) onItemClickListener.onItemClick(tabView, position);

        cursorWidth = tabView.getWidth() - dip2px(tabView.getContext(), 27);
        int offSize = (tabView.getWidth() - cursor.getWidth()) / 2;

        if (isSmooth) {
            long duration = 250;
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animTranslate = ObjectAnimator.ofFloat(cursor, "translationX", cursor.getX(), tabView.getX() + offSize);
            ObjectAnimator animScale = ObjectAnimator.ofFloat(cursor, "scaleX", 1f, 1f * cursorWidth / cursor.getWidth());
            animatorSet.play(animTranslate).with(animScale);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.setDuration(duration);
            animatorSet.start();
        } else {
            cursor.setTranslationX(tabView.getX() + offSize);
            cursor.setScaleX(1f * cursorWidth / cursor.getWidth());
        }

        ((TextView) tabView).setTextColor(colorSelected);

        if (preView != null && preView != tabView) {
            ((TextView) preView).setTextColor(colorUnSelected);
        }
        preView = tabView;
    }

    @Override
    protected void resetTabView(View tabView, int position) {
        ((TextView) tabView).setText((String) getData(position));
    }

    @Override
    protected View onCreateCursor(View viewParent) {
        int cursorHeight = 2;
        RelativeLayout.LayoutParams paramsCursor = new RelativeLayout.LayoutParams(dip2px(viewParent.getContext(), cursorWidth),
                dip2px(viewParent.getContext(), cursorHeight));
        paramsCursor.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        cursor = new View(viewParent.getContext());
        cursor.setBackgroundColor(colorSelected);
        cursor.setLayoutParams(paramsCursor);
        return cursor;
    }

    public int getColorSelected() {
        return colorSelected;
    }

    /**
     * Color.parseColor()
     *
     * @param colorSelected value
     */
    public void setColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
        if (cursor != null) cursor.setBackgroundColor(colorSelected);
    }

    public int getColorUnSelected() {
        return colorUnSelected;
    }

    /**
     * Color.parseColor()
     *
     * @param colorUnSelected value
     */
    public void setColorUnSelected(int colorUnSelected) {
        this.colorUnSelected = colorUnSelected;
    }

    @Override
    public Object getData(int position) {
        return super.getData(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
