#TabView

supports vertical and horizontal tab style.


##Requirements
TabView can be included in any Android application.
TabView supports OS on API 15 and above.

include
> * TabView
> * VerticalTabView
> * TabBashAdapter
> * SimpleTabAdapter
> * SimpleVerticalTabAdapter

##Using TabView in your Application
If you are building with Gradle, simply add the following line to the `dependencies` section of your `build.gradle` file:
```groovy
complie 'com.github.looa:TabView:0.0.1'
```

##Simple
![screenshots](https://raw.githubusercontent.com/looa/TabView/master/REDEME/show_001.gif)
###xml
```groovy
<org.looa.tabview.widget.TabView
        android:id="@+id/tab_simple"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:background="#efeff0"
        android:overScrollMode="never"
        android:scrollbars="none" />
```
###java
```groovy
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

        TabBaseAdapter<String> adapter = new SimpleTabAdapter(getApplicationContext());
        adapter.setData(data);

        tabView.setAdapter(adapter);
        tabView.setTabCurPosition(0, false);
```
