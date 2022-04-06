package org.looa.tabviewtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by ranxiangwei on 2016/12/26.
 */

public class MainFragment extends Fragment {

    private View view;
    private TextView textView;
    private CharSequence content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test,
                container,
                false);
        initView();
        return view;
    }

    private void initView() {
        textView = (TextView) view.findViewById(R.id.tv_fragment_content);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (content != null) {
            textView.setText(content);
        }
    }

    public void setText(CharSequence charSequence) {
        this.content = charSequence;
        if (textView != null)
            textView.setText(charSequence);
    }
}
