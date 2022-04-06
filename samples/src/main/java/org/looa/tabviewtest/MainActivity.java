package org.looa.tabviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.looa.tabview.adapter.SimpleTabAdapter;
import org.looa.tabview.widget.OnItemClickListener;
import org.looa.tabview.widget.TabView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    private SimpleTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabView tabView = (TabView) findViewById(R.id.tab_simple);

        final List<String> data = new ArrayList<>();
        data.add("No.1");
        data.add("No.2 - Google");
        data.add("No.3 - Facebook");
        data.add("No.4");
        data.add("No.5 - Ebay");
        data.add("No.6 - Magic Leap");
        data.add("No.7");
        data.add("No.8 - Alibaba");
        data.add("No.9 - Tencent");

        adapter = new SimpleTabAdapter(getApplicationContext());
        adapter.setData(data);

        tabView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position);
                Log.d(getClass().getName(),
                        "onItemClick current position -> " + position);
            }
        });

        tabView.setAdapter(adapter);
        tabView.setBashLineColor(Color.LTGRAY);
        tabView.setTopLineColor(Color.LTGRAY);

        tabView.setTabCurPosition(3, false);

        fragmentList = new ArrayList<>();

        for (String name : data) {
            MainFragment fragment = new MainFragment();
            fragment.setText(name);
            fragmentList.add(fragment);
        }

        viewPager = (ViewPager) findViewById(R.id.vp_content);
        final FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(tabView.getCurPosition());
        viewPager.addOnPageChangeListener(tabView);

        findViewById(R.id.btn_reset_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // step 1. 获取当前 tab 的 position
                int position = tabView.getCurPosition();

                // step 2. 重建 tab 数据，重新绘制 tab
                final List<String> data = new ArrayList<>();
                data.add("No.1");
                data.add("No.2 - Google");
                data.add("No.3 - Facebook");
                data.add("No.4");
                data.add("No.5 - Ebay");
                data.add("No.6 - Magic Leap");
                data.add("No.7");
                data.add("No.8 - Alibaba");
                data.add("No.9 - Tencent");

                adapter.setData(data);
                adapter.notifyDataSetChanged();

                // step 3. 恢复 tab 重绘之前的 position
                tabView.setTabCurPosition(position, false);
                viewPager.setCurrentItem(position);
            }
        });
    }
}
