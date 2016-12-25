package org.looa.tabviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
        data.add("Uber");
        data.add("Magic Leap");

        TabView tabView = (TabView) findViewById(R.id.tab_simple);
        tabView.setSmoothShowEdgeSizeOff(100);

        SimpleTabAdapter adapter = new SimpleTabAdapter(getApplicationContext());
        adapter.setData(data);
        adapter.setOnItemClickListener(new SimpleTabAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        tabView.setAdapter(adapter);
        tabView.setAutoFillParent(true);
        tabView.setSmooth(true);
        tabView.setTabCurPosition(1, false);
    }
}
