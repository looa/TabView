package org.looa.tabviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.looa.tabview.adapter.SimpleTabAdapter;
import org.looa.tabview.widget.TabView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> data = new ArrayList<>();

        data.add("Google");
        data.add("Facebook");
        data.add("Alibaba");
        data.add("Tencent");
        data.add("Microsoft");
        data.add("Baidu");
        data.add("Yahoo");
        data.add("Ebay");
        data.add("Amazon");
        data.add("Magic Leap");

        TabView tabView = (TabView) findViewById(R.id.tab_simple);
        tabView.setSmoothShowEdgeSizeOff(100);

        SimpleTabAdapter adapter = new SimpleTabAdapter(getApplicationContext());
        adapter.setData(data);
        adapter.setColorSelected(getResources().getColor(R.color.colorPrimary));

        tabView.setAdapter(adapter);
        tabView.setTabCurPosition(0, false);
    }
}
