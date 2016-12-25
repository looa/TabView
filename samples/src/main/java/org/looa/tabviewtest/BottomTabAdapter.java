package org.looa.tabviewtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.looa.tabview.widget.TabBaseAdapter;

/**
 * image view
 * Created by ran on 2016/12/25.
 */

public class BottomTabAdapter extends TabBaseAdapter<BottomTabAdapter.TabInfo> {

    public static class TabInfo {
        int imgUnpressed;
        int imgPressed;

        public int getImgUnpressed() {
            return imgUnpressed;
        }

        public void setImgUnpressed(int imgUnpressed) {
            this.imgUnpressed = imgUnpressed;
        }

        public int getImgPressed() {
            return imgPressed;
        }

        public void setImgPressed(int imgPressed) {
            this.imgPressed = imgPressed;
        }
    }

    private View preView;

    @Override
    protected View onCreateTabView(ViewGroup parentView, int viewType, int position) {
        ImageView iv = new ImageView(parentView.getContext());
        iv.setTag(position);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setPadding(0, dip2px(parentView.getContext(), 13), 0, dip2px(parentView.getContext(), 13));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(params);
        return iv;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onSelectedTabView(View tabView, int position, boolean isSmooth) {
        if (preView != null) {
            ((ImageView) preView).setImageResource(getData((Integer) preView.getTag()).getImgUnpressed());
        }
        preView = tabView;
        ((ImageView) tabView).setImageResource(getData(position).getImgPressed());
        Toast.makeText(tabView.getContext(), "position -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void resetTabView(View tabView, int position) {
        ((ImageView) tabView).setImageResource(getData(position).getImgUnpressed());
    }
}
