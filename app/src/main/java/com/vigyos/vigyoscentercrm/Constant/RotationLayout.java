package com.vigyos.vigyoscentercrm.Constant;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RotationLayout extends RelativeLayout {

    private static final int ROTATION_ANGLE = -90;

    public RotationLayout(Context context) {
        super(context);
        init();
    }

    public RotationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotationLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(ROTATION_ANGLE, getWidth() * 0.5f, getHeight() * 0.5f);
        super.onDraw(canvas);
    }
}
