package org.looa.tabview.widget;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by looa on 2016/12/22.
 */

public abstract class TabBaseAdapter<T> {
    private TabUpdateListener listener;
    private List<T> list;
    private int sizePre;

    public void setOnUpdateListener(TabUpdateListener listener) {
        this.listener = listener;
    }

    public void setData(List<T> data) {
        this.list = data;
        sizePre = getCount();
    }

    /**
     * notice the VerticalTabView to update view.
     */
    public void notifyDataSetChanged() {
        sizePre = getCount();
        if (listener != null) listener.replace();
    }

    /**
     * after change data, you should notice the VerticalTabView to update view.
     *
     * @param position
     */
    public void notifyDataSetChanged(int position) {
        if (listener != null)
            if (sizePre == getCount())
                listener.update(position);
            else {
                listener.replace();
                sizePre = getCount();
            }
    }

    public T getData(int position) {
        if (position < 0 || list == null || list.size() < position + 1) return null;
        return list.get(position);
    }

    public int getCount() {
        return list.size();
    }

    public int getViewTypeCount() {
        return 1;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * create a tabView
     *
     * @param parentView
     * @param viewType
     * @return
     */
    protected abstract View onCreateTabView(ViewGroup parentView, int viewType, int position);


    /**
     * if you add a cursor for the TabView, you should init the cursor in the method.
     *
     * @param viewParent
     * @return
     */
    protected View onCreateCursor(View viewParent) {
        return null;
    }

    /**
     * Update the view corresponding to the position.
     *
     * @param tabView
     * @param position
     */
    protected abstract void onSelectedTabView(View tabView, int position, boolean isSmooth);


    /**
     * Scrolls tab view to target position;
     *
     * @param tabView        target view.
     * @param position       target position.
     * @param positionOffset Value from [0, 1) indicating the offset to the page(target) at position
     * @param isPageSelected is page selected
     */
    protected void onTabScrolled(View tabView, int position, float positionOffset, boolean isPageSelected) {

    }

    /**
     * Reset the view corresponding to the position.
     * You can initialize a tabView or update a tabView.
     *
     * @param tabView
     * @param position
     */
    protected abstract void resetTabView(View tabView, int position);

}
