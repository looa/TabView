package org.looa.tabview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Px;
import android.support.v4.view.ViewPager;
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
 * A TabView is a {@link ScrollView}, meaning you should place one
 * child in it containing the entire contents to scroll. But there's already
 * a holder here. The holder is the only one child, and the parent for other views.
 * The holder is a {@link RelativeLayout},meaning you can add lots of views
 * you want to add. You can use the getHolder() to get the holder;
 * You can use a custom view through the {@link TabBaseAdapter}.
 * You must set a {@link TabBaseAdapter} for a TabView.
 * When all tab total widths are not sufficient to fill the TabView width,
 * the remaining dimensions are assigned to each tab by default.
 * <p>
 * Created by looa on 2016/12/19.
 */

public class TabView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    /**
     * A ScrollView is a {@link FrameLayout}, meaning you should place one child in it
     * containing the entire contents to scroll;
     * holder is the only one child, and the parent for other views.
     */
    private RelativeLayout holder;
    private RelativeLayout.LayoutParams holderParams;


    private View bashLine;
    private int bashLineColor = Color.TRANSPARENT;
    private View topLine;
    private int topLineColor = Color.TRANSPARENT;

    private TabBaseAdapter adapter;
    private List<View> tabViewList;
    private View cursorView;
    private TabUpdatedListener onUpdatedListener = new TabUpdatedListener();
    private OnTabClickedListener onTabClickedListener = new OnTabClickedListener();
    private OnItemClickListener onItemClickListener;

    /**
     * if isSmoothShowEdgeItem == true
     * HorizontalTabView can smoothly scroll the edge items to a friendly vision position.
     */
    private boolean isSmoothShowEdgeItem;
    private int sizeOff = 0;
    private int tabWidth = 0;

    private int scrollSize;

    private int positionBeforeMeasure = 0;
    private boolean positionBeforeMeasureIsSmooth = false;
    private boolean hasMeasure = false;

    private boolean isSmooth;
    /**
     * auto fill the parent view.
     */
    private boolean isAutoFillParent = false;

    /**
     * 是否是点击事件引起的动效
     */
    private boolean isClickScroll = false;
    /**
     * 手指的滑动方向，-1代表在左侧，1代表在右侧，0代表在中心
     */
    private int scrollOrientation = 0;
    /**
     * 移向目标位置的百分比
     */
    private float targetOffset;

    /**
     * 当前位置
     */
    private int position = -1;

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
        setAutoFillParent(false);
    }

    private void initData() {
        initTabList();
    }

    private void initView(Context context) {
        setFillViewport(true);
        holder = new RelativeLayout(context);
        holderParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        addView(holder, holderParams);
        bashLine = new View(context);
        bashLine.setBackgroundColor(bashLineColor);
        RelativeLayout.LayoutParams bashLineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        bashLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bashLine.setLayoutParams(bashLineParams);

        topLine = new View(context);
        topLine.setBackgroundColor(topLineColor);
        RelativeLayout.LayoutParams topLineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        topLineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        topLine.setLayoutParams(topLineParams);
    }

    private void initTabList() {
        int start = 0;
        int tabCount = adapter.getCount();
        if (tabViewList == null) {
            tabViewList = new ArrayList<>(tabCount);
        } else if (tabViewList.size() <= tabCount) {
            start = tabViewList.size();
        } else {
            start = tabCount;
            int end = tabViewList.size();
            for (int i = start; i < end; i++) {
                tabViewList.remove(tabViewList.size() - 1);
            }
        }
        for (int i = start; i < tabCount; i++) {
            tabViewList.add(i, adapter.onCreateTabView(getHolder(), adapter.getItemViewType(i), i));
            tabViewList.get(i).setId(i + 1);
        }
        if (cursorView == null) cursorView = adapter.onCreateCursor(this);
    }

    private void initTabView() {
        if (tabViewList == null || tabViewList.size() == 0) return;
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
        getHolder().addView(bashLine);
        getHolder().addView(topLine);
        if (cursorView != null) {
            getHolder().addView(cursorView);
        }
        tabViewList.get(0).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (hasMeasure) {
                    setTabCurPosition(positionBeforeMeasure, positionBeforeMeasureIsSmooth);
                    removeGlobalLayoutListener(getViewTreeObserver(), this);
                }
            }
        });
    }

    @TargetApi(16)
    private void removeGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);
    }

    /**
     * 获取当前位置，未初始化的时候返回positionBeforeMeasure
     *
     * @return
     */
    public int getCurPosition() {
        return position == -1 ? positionBeforeMeasure : position;
    }

    private int getPosition() {
        return position;
    }

    private void setPosition(int position) {
        this.position = position;
    }

    /**
     * set bash line color.
     * default color is Color.TRANSPARENT.
     *
     * @param color Color.XXX;
     */
    public void setBashLineColor(int color) {
        this.bashLineColor = color;
        if (bashLine != null) bashLine.setBackgroundColor(color);
    }

    /**
     * set bash line height.
     * default height is 1 px.
     *
     * @param height px
     */
    public void setBashLineHeight(@Px int height) {
        if (bashLine != null) bashLine.getLayoutParams().height = height;
    }

    /**
     * set top line color.
     * default color is Color.TRANSPARENT.
     *
     * @param color Color.XXX;
     */
    public void setTopLineColor(int color) {
        this.topLineColor = color;
        if (topLine != null) topLine.setBackgroundColor(color);
    }

    /**
     * set top line height.
     * default height is 1 px.
     *
     * @param height px
     */
    public void setTopLineHeight(@Px int height) {
        if (topLine != null) topLine.getLayoutParams().height = height;
    }

    /**
     * the child can auto fill the parent.
     *
     * @param isAutoFillParent
     */
    public void setAutoFillParent(boolean isAutoFillParent) {
        this.isAutoFillParent = isAutoFillParent;
        setSmooth(!isAutoFillParent);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    ///////onPageChangeListener////

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (positionOffset != 0 && scrollOrientation == 0) {
//            scrollOrientation = positionOffset > 0.5f ? 1 : -1;
//        }
//        if (positionOffset == 0) {
//            isClickScroll = false;
//            scrollOrientation = 0;
//            if (adapter != null)
//                adapter.onTabScrolled(tabViewList.get(position), position, positionOffset, false);
//        } else if (!isClickScroll) {
//            //向右滑动的时候，目标位置等于position，反之等于position + 1
//            int targetPosition = scrollOrientation == 1 ? position : position + 1;
//            float targetOffset = scrollOrientation == 1 ? 1 - positionOffset : positionOffset;
//            this.targetOffset = targetOffset;
//            adapter.onTabScrolled(tabViewList.get(targetPosition), targetPosition, targetOffset, false);
//        }
    }

    @Override
    public void onPageSelected(int position) {
        setTabCurPosition(position, isSmooth());
//        Log.i(getClass().getSimpleName(), "onPageSelected -> " + position);
//        adapter.onTabScrolled(tabViewList.get(position), position, targetOffset, true);
//        setPosition(position);
//        if (isSmoothShowEdgeItem && sizeOff > 0) {
//            View tabView = getTabView(position);
//            if (tabView == null) return;
//            if (tabWidth == 0) tabWidth = tabView.getWidth();
//            float dx = tabView.getX() - scrollSize;
//            if (dx > sizeOff && dx < getWidth() - sizeOff - tabWidth) return;
//            if (isSmooth) {
//                smoothScrollBy(dx <= sizeOff ? (int) (dx - sizeOff) : (int) (dx - getWidth() + sizeOff + tabWidth), 0);
//            } else {
//                scrollBy(dx <= sizeOff ? (int) (dx - sizeOff) : (int) (dx - getWidth() + sizeOff + tabWidth), 0);
//            }
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    ////////////////////////////////

    private class OnTabClickedListener implements OnClickListener {

        /**
         * tabView's id is position+1;
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            isClickScroll = true;
            setTabPosition(v.getId() - 1, isSmooth());
            if (onItemClickListener != null) onItemClickListener.onItemClick(v, v.getId() - 1);
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
     * @param position position
     * @param isSmooth if isSmooth = true, the view will smoothly scroll.
     */
    public void setTabCurPosition(int position, boolean isSmooth) {
        if (!hasMeasure) {
            this.positionBeforeMeasure = position;
            this.positionBeforeMeasureIsSmooth = isSmooth;
            return;
        }
        setTabPosition(position, isSmooth);
    }

    /**
     * set the current position and scroll the view to a right place.
     *
     * @param position view -position
     */
    private void setTabPosition(int position, boolean isSmooth) {
        if (getPosition() == position) return;
        setPosition(position);
        adapter.onSelectedTabView(getTabView(position), position, isSmooth);
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
        int viewWidth = getMeasuredWidth();
        int holderWidth = getHolder().getMeasuredWidth();
        if (getWidth() != 0 && !hasMeasure && tabViewList != null && tabViewList.size() > 0) {
            hasMeasure = true;
            if (isAutoFillParent) {
                int tabWidth = (int) (1f * viewWidth / tabViewList.size());
                for (int i = 0; i < tabViewList.size(); i++) {
                    tabViewList.get(i).getLayoutParams().width = tabWidth;
                }
            }
        }
        int maxWidth = Math.max(viewWidth, holderWidth);
        if (bashLine.getLayoutParams().width != maxWidth) {
            bashLine.getLayoutParams().width = maxWidth;
            topLine.getLayoutParams().width = maxWidth;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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
            hasMeasure = false;
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
