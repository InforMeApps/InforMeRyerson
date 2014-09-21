/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */


package ca.informeapps.informeryerson.Misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap() ;
        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();


        Bitmap roundBitmap =  createRoundedness(bitmap2,w,h);
        canvas.drawBitmap(roundBitmap, 0,0, null);

    }

    public Bitmap createRoundedness(Bitmap scaleBitmapImage,int w, int h) {

        int Width = w;
        int Height = h;

        Bitmap roundedBitmap = Bitmap.createBitmap(Width, Height,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(roundedBitmap);
        Path path = new Path();
        path.addCircle((((float) Width - 1) / 2), (((float) Height - 1) / 2),
                (Math.min  (((float) Width),  ((float) Height)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
        canvas.drawARGB(0, 0, 0, 0);


        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, Width, Height), paint);
        return roundedBitmap;
    }
}

