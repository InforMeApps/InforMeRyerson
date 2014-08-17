/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Shahar on 2014-08-17.
 */
public class CircleImageView extends ImageView {

    public CircleImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    //must have constructor... causes crash
    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        int width = getWidth();
        int height = getHeight();

        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap() ;
        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888,true);



        Bitmap circleImage =  makeCircle(bitmap2, width,height);
        canvas.drawBitmap(circleImage, 0,0, null);
    }

    public static Bitmap makeCircle(Bitmap bitmap, int width, int height) {
        Bitmap vectorBitmap;
        if(bitmap.getWidth() != width|| bitmap.getHeight() != height) {
            vectorBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }
        else {
            vectorBitmap = bitmap;
        }


        Bitmap finalBitmap = Bitmap.createBitmap(vectorBitmap.getWidth(),vectorBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, vectorBitmap.getWidth(), vectorBitmap.getHeight());

        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setAntiAlias(true);//may cause lag
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);

        canvas.drawCircle(vectorBitmap.getWidth() / 2, vectorBitmap.getHeight() / 2, vectorBitmap.getWidth() / 2, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(vectorBitmap, rect, rect, paint);


        return finalBitmap;
    }

}