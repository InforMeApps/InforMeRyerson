/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.Misc;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;

/**
 * Created by Shahar on 2014-07-31.
 */

public class ExpandAnimation extends Animation {


    private View AnimationView;
    private LayoutParams layoutPrams;


    private int intStartPos;
    private int intEndPos;

    private boolean blnVisible = false;
    private boolean blnAnimationDone = false;


    public ExpandAnimation(View view, int duration) {

        setDuration(duration);
        AnimationView = view;
        layoutPrams = (LayoutParams) view.getLayoutParams();


        blnVisible = (view.getVisibility() == View.VISIBLE); //checks visibility of current view

        //gets the positions of the layouts
        intStartPos = layoutPrams.bottomMargin;
        intEndPos = (intStartPos == 0 ? (0 - view.getHeight()) : 0);

        view.setVisibility(View.VISIBLE); //sets current view to be visible
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);


        if (interpolatedTime < 1.0f) {
            layoutPrams.bottomMargin = intStartPos + (int) ((intEndPos - intStartPos) * interpolatedTime);//calculating the height
            AnimationView.requestLayout();
        } else if (!blnAnimationDone) {
            layoutPrams.bottomMargin = intEndPos;
            AnimationView.requestLayout();

            if (blnVisible) {
                AnimationView.setVisibility(View.GONE);
            }
            blnAnimationDone = true;
        }
    }

}
