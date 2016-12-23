#TabView

supports vertical and horizontal tab style.


##Requirements
TabView can be included in any Android application.
TabView supports OS on API 15 and above.

include
> * TabView
> * VerticalTabView
> * TabBaseAdapter
> * SimpleTabAdapter
> * SimpleVerticalTabAdapter

##Using TabView in your Application
If you are building with Gradle, simply add the following line to the `dependencies` section of your `build.gradle` file:
```groovy
complie 'com.github.looa:TabView:0.0.4'
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

        SimpleTabAdapter adapter = new SimpleTabAdapter(getApplicationContext());
        adapter.setData(data);
        adapter.setColorSelected(getResources().getColor(R.color.colorPrimary));

        tabView.setAdapter(adapter);
        tabView.setTabCurPosition(0, false);
```
and you can create your adapter to demand your requirements ↓↓↓
```groovy
public class SimpleTabAdapter extends TabBaseAdapter{

  protected View onCreateTabView(ViewGroup parentView, int viewType, int position){
    //create the tab view.
  }

  protected View onCreateCursor(View viewParent) {
    //create the cursor, if you need.
    return null;
  }

  protected void onSelectedTabView(View tabView, int position, boolean isSmooth){
    //the tabView is selected.
  }

  protected void resetTabView(View tabView, int position){
    //init or reset your tab view'values.
  }

}
```
