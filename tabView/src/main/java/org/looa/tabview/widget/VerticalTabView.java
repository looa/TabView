package org.looa.tabview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * A VerticalTabView is a {@link ScrollView}, meaning you should place one
 * child in it containing the entire contents to scroll. But there's already
 * a holder here. The holder is the only one child, and the parent for other views.
 * The holder is a {@link RelativeLayout},meaning you can add lots of views
 * you want to add. You can use the getHolder() to get the holder;
 * You can use a custom view through the {@link TabBaseAdapter}.
 * You must set a {@link TabBaseAdapter} for a VerticalTabView.
 * <p>
 * Created by looa on 2016/12/19.
 */

public class VerticalTabView extends ScrollView {

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
     * VerticalTabView can smoothly scroll the edge items to a friendly vision position.
     */
    private boolean isSmoothShowEdgeItem;
    private int sizeOff = 0;
    private int tabHeight = 0;

    private int scrollSize;

    public VerticalTabView(Context context) {
        this(context, null);
    }

    public VerticalTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setSmoothShowEdgeItemEnable(true);
        setSmoothShowEdgeSizeOff(120);

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
                ((RelativeLayout.LayoutParams) tabViewList.get(i).getLayoutParams()).addRule(RelativeLayout.BELOW,
                        tabViewList.get(i - 1).getId());
            }
            getHolder().addView(tabViewList.get(i));

            tabViewList.get(i).setOnClickListener(onTabClickedListener);
            onUpdatedListener.update(i);
        }
    }

    /**
     * VerticalTabView can smoothly scroll the edge items to a friendly vision position.
     *
     * @param isSmoothShowEdgeItem if isSmoothShowEdgeItem == true
     */
    public void setSmoothShowEdgeItemEnable(boolean isSmoothShowEdgeItem) {
        this.isSmoothShowEdgeItem = isSmoothShowEdgeItem;
    }

    /**
     * VerticalTabView can smoothly scroll the edge items to a friendly vision position.
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
            setTabPosition(v, v.getId() - 1);
        }
    }


    /**
     * set the current position and scroll the view to a right place.
     *
     * @param position
     */
    public void setTabCurPosition(int position) {
        setTabPosition(tabViewList.get(position), position);
    }

    /**
     * set the current position and scroll the view to a right place.
     *
     * @param view
     * @param position
     */
    private void setTabPosition(View view, int position) {
        adapter.onSelectedTabView(view, position, true);
        if (isSmoothShowEdgeItem && sizeOff > 0) {
            View tabView = getTabView(position);
            if (tabView == null) return;
            if (tabHeight == 0) tabHeight = tabView.getHeight();
            float dy = tabView.getY() - scrollSize;
            if (dy > sizeOff && dy < getHeight() - sizeOff - tabHeight) return;
            smoothScrollBy(0, dy <= sizeOff ? (int) (dy - sizeOff) : (int) (dy - getHeight() + sizeOff + tabHeight));
        }
    }

    /**
     * if you want to change or add something (view) that will be included in the holder,
     * you should use this method to get the holder.
     *
     * @return
     */
    public RelativeLayout getHolder() {
        return holder;
    }

    /**
     * It has set a onClickListener for each View.But you can get the tab view, then
     * you can use setLongClickListener for every view or do anything you want to do;
     *
     * @return
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
        this.scrollSize = t;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
