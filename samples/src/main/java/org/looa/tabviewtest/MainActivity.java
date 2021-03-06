package org.looa.tabviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.looa.tabview.adapter.SimpleTabAdapter;
import org.looa.tabview.widget.OnItemClickListener;
import org.looa.tabview.widget.TabView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabView tabView = (TabView) findViewById(R.id.tab_simple);
        tabView.setTabCurPosition(3, false);
        tabView.setSmoothShowEdgeSizeOff(100);

        SimpleTabAdapter adapter = new SimpleTabAdapter(getApplicationContext());
        final List<String> data = new ArrayList<>();
        data.add("ahh");
        data.add("ahh - Google");
        data.add("ahh - Facebook");
        data.add("ahh");
        data.add("ahh - Ebay");
        data.add("ahh - Magic Leap");
        data.add("ahh");
        data.add("ahh - Alibaba");
        data.add("ahh - Tencent");
        adapter.setData(data);
        tabView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position);
                Log.d(getClass().getName(), "onItemClick current position -> " + position);
            }
        });

        tabView.setAdapter(adapter);
        tabView.setBashLineColor(Color.LTGRAY);
        tabView.setTopLineColor(Color.LTGRAY);

        fragmentList = new ArrayList<>();

        for (String name : data) {
            TestFragment fragment = new TestFragment();
            fragment.setText(name);
            fragmentList.add(fragment);
        }

        viewPager = (ViewPager) findViewById(R.id.vp_content);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(tabView.getCurPosition());
        viewPager.addOnPageChangeListener(tabView);
    }
}
