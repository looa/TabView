package org.looa.tabview.widget;

/**
 * Created by looa on 2016/12/22.
 */

/**
 * used to notice adapter to update or replace.
 */
interface TabUpdateListener {
    void replace();

    void update(int position);
}
