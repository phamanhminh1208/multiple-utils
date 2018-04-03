package com.that2u.android.app.utils.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by mohamedzakaria on 8/7/16.
 */
public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size;

        //If the layout_width or layout_width of this view is set as match_parent or any exact dimension, then this view will use that dimension
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ^ MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)
                size = width;
            else
                size = height;
        }
        //If the layout_width and layout_width of this view are not set or both set as match_parent or any exact dimension,
        // then this view will use the minimum dimension
        else
            size = Math.min(width, height);

        setMeasuredDimension(size, size);
    }
}
