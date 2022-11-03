package com.example.foodapp.ui.customviews;

import static java.lang.Math.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class CustomMotionLayout extends MotionLayout {
    public CustomMotionLayout(@NonNull Context context) {
        super(context);
    }

    public CustomMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean mIsScrolling = false;

    public MotionAbsorptionMode isAbsorptionMode() {
        return absorptionMode;
    }

    public void setAbsorptionMode(MotionAbsorptionMode absorptionMode) {
        this.absorptionMode = absorptionMode;
    }

    private MotionAbsorptionMode absorptionMode = MotionAbsorptionMode.DRAG_DOWN;

    ViewConfiguration vc = ViewConfiguration.get(getContext());

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (absorptionMode) {
            case ALL:
                return true;
            case NONE:
                return false;
            default: {
                final int action = event.getAction();

                if (action != MotionEvent.ACTION_MOVE) {
                    // Release the scroll.
                    mIsScrolling = false;
                    return false; // Do not intercept touch event, let the child handle it
                }

                if (mIsScrolling) {
                    return true;
                }
                final float yDiff = calculateDistanceY(event);
                final float xDiff = calculateDistanceX(event);

                if (yDiff > abs(xDiff)) {
                    mIsScrolling = true;
                    return true;
                }
                return super.onInterceptTouchEvent(event);
            }
        }
    }

    private float calculateDistanceX(MotionEvent event) {
        if (event.getHistorySize() == 0)
            return 0;
        return event.getX() - event.getHistoricalX(0);
    }

    private float calculateDistanceY(MotionEvent event) {
        if (event.getHistorySize() == 0)
            return 0;
        return event.getY() - event.getHistoricalY(0);
    }

    public enum MotionAbsorptionMode {
        ALL,
        DRAG_DOWN,
        NONE
    }
}
