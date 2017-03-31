package com.zixuanz.plaintextreader.style;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by Zixuan Zhao on 3/30/17.
 */

public class BottomBarBehavior extends CoordinatorLayout.Behavior {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.abs(dependency.getTop());

        child.setTranslationY(translationY);
        return true;
    }
}
