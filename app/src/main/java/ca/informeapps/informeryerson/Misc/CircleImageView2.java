/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */


package ca.informeapps.informeryerson.Misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by Shahar on 2014-09-04.
 */


public class CircleImageView2 extends ImageView {


    public CircleImageView2(Context context) {
        super(context);
    }

    public CircleImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = 90.0f; // angle of round corners
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);

        super.onDraw(canvas);
    }
}

