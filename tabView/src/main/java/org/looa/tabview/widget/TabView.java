package org.looa.tabview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * A HorizontalScrollView is a {@link ScrollView}, meaning you should place one
 * child in it containing the entire contents to scroll. But there's already
 * a holder here. The holder is the only one child, and the parent for other views.
 * The holder is a {@link RelativeLayout},meaning you can add lots of views
 * you want to add. You can use the getHolder() to get the holder;
 * You can use a custom view through the {@link TabBaseAdapter}.
 * You must set a {@link TabBaseAdapter} for a HorizontalScrollView.
 * When all tab total widths are not sufficient to fill the HorizontalTabView width,
 * the remaining dimensions are assigned to each tab by default.
 * TODO 当所有的tab总宽度不足以填满整个控件宽时，默认把剩余尺寸平均分给每个tab（这一版不需要这样）
 * <p>
 * Created by looa on 2016/12/19.
 */

public class TabView extends HorizontalScrollView {

    /**
     * A ScrollView is a {@link FrameLayout}, meaning you should place one child in it
     * containing the entire contents to scroll;
     * holder is the only one child, and the parent for other views.
     */
    private RelativeLayout holder;
    private RelativeLayout.LayoutParams holderParams;

    private TabBaseAdapter adapter;
    private List<View> tabViewList;
    private TabUpdatedListener onUpdatedListener = new TabUpdatedListener();
    private OnTabClickedListener onTabClickedListener = new OnTabClickedListener();

    /**
     * if isSmoothShowEdgeItem == true
     * HorizontalTabView can smoothly scroll the edge items to a friendly vision position.
     */
    private boolean isSmoothShowEdgeItem;
    private int sizeOff = 0;
    private int tabWidth = 0;

    private int scrollSize;

    private int positionBeforeMeasure = -1;
    private boolean positionBeforeMeasureIsSmooth = false;
    private boolean hasMeasure = false;

    private boolean isSmooth;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setSmoothShowEdgeItemEnable(true);
        setSmoothShowEdgeSizeOff(72);
        setSmooth(true);
    }

    private void initData() {
        initTabList();
    }

    private void initView(Context context) {
        holder = new RelativeLayout(context);
        holderParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        addView(holder, holderParams);
    }

    private void initTabList() {
        int tabCount = adapter.getCount();
        if (tabViewList == null)
            tabViewList = new ArrayList<>(tabCount);
        else tabViewList.clear();
        for (int i = 0; i < tabCount; i++) {
            tabViewList.add(i, adapter.onCreateTabView(getHolder(), adapter.getItemViewType(i), i));
            tabViewList.get(i).setId(i + 1);
        }
    }

    private void initTabView() {
        if (tabViewList == null) return;
        getHolder().removeAllViews();
        for (int i = 0; i < tabViewList.size(); i++) {
            if (i != 0) {
                ((RelativeLayout.LayoutParams) tabViewList.get(i).getLayoutParams()).addRule(RelativeLayout.RIGHT_OF,
                        tabViewList.get(i - 1).getId());
            }
            getHolder().addView(tabViewList.get(i));

            tabViewList.get(i).setOnClickListener(onTabClickedListener);
            onUpdatedListener.update(i);
        }
        final View view;
        if ((view = adapter.onCreateCursor(this)) != null) {
            getHolder().addView(view);
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    hasMeasure = true;
                    if (positionBeforeMeasure != -1)
                        setTabCurPosition(positionBeforeMeasure, positionBeforeMeasureIsSmooth);
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            if (positionBeforeMeasure != -1)
                setTabCurPosition(positionBeforeMeasure, positionBeforeMeasureIsSmooth);
        }
    }

    /**
     * HorizontalTabView can smoothly scroll the edge items to a friendly vision position.
     *
     * @param isSmoothShowEdgeItem if isSmoothShowEdgeItem == true
     */
    public void setSmoothShowEdgeItemEnable(boolean isSmoothShowEdgeItem) {
        this.isSmoothShowEdgeItem = isSmoothShowEdgeItem;
    }

    /**
     * HorizontalTabView can smoothly scroll the edge items to a friendly vision position.
     * {the sizeOff position}
     *
     * @param sizeOff dp
     */
    public void setSmoothShowEdgeSizeOff(int sizeOff) {
        this.sizeOff = dip2px(getContext(), sizeOff);
    }


    private class OnTabClickedListener implements OnClickListener {

        /**
         * tabView's id is position+1;
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            setTabPosition(v, v.getId() - 1, isSmooth());
        }
    }

    public boolean isSmooth() {
        return isSmooth;
    }

    public void setSmooth(boolean smooth) {
        isSmooth = smooth;
    }

    /**
     * set the current position and scroll the view to a right place.
     *
     * @param position
     * @param isSmooth if isSmooth = true, the view will smoothly scroll.
     */
    public void setTabCurPosition(int position, boolean isSmooth) {
        if (!hasMeasure) {
            this.positionBeforeMeasure = position;
            this.positionBeforeMeasureIsSmooth = isSmooth;
            return;
        }
        setTabPosition(tabViewList.get(position), position, isSmooth);
    }

    /**
     * set the current position and scroll the view to a right place.
     *
     * @param view     view - position
     * @param position view -position
     */
    private void setTabPosition(View view, int position, boolean isSmooth) {
        adapter.onSelectedTabView(view, position, isSmooth);
        if (isSmoothShowEdgeItem && sizeOff > 0) {
            View tabView = getTabView(position);
            if (tabView == null) return;
            if (tabWidth == 0) tabWidth = tabView.getWidth();
            float dx = tabView.getX() - scrollSize;
            if (dx > sizeOff && dx < getWidth() - sizeOff - tabWidth) return;
            if (isSmooth) {
                smoothScrollBy(dx <= sizeOff ? (int) (dx - sizeOff) : (int) (dx - getWidth() + sizeOff + tabWidth), 0);
            } else {
                scrollBy(dx <= sizeOff ? (int) (dx - sizeOff) : (int) (dx - getWidth() + sizeOff + tabWidth), 0);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * if you want to change or add something (view) that will be included in the holder,
     * you should use this method to get the holder.
     *
     * @return the holder
     */
    public RelativeLayout getHolder() {
        return holder;
    }

    /**
     * It has set a onClickListener for each View.But you can get the tab view, then
     * you can use setLongClickListener for every view or do anything you want to do;
     *
     * @return tabView
     */
    public View getTabView(int position) {
        return tabViewList.get(position);
    }

    public void setAdapter(TabBaseAdapter adapter) {
        this.adapter = adapter;
        this.adapter.setOnUpdateListener(onUpdatedListener);
        initData();
        adapter.notifyDataSetChanged();
    }

    private class TabUpdatedListener implements TabUpdateListener {

        @Override
        public void replace() {
            initTabList();
            initTabView();
        }

        @Override
        public void update(int position) {
            adapter.resetTabView(tabViewList.get(position), position);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        this.scrollSize = l;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
