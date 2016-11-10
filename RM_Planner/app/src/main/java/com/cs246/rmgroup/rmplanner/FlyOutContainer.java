package com.cs246.rmgroup.rmplanner;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.Scroller;

/**
 * Created by Chris on 6/7/2016.
 */

public class FlyOutContainer extends LinearLayout {
    //references to views
    private View menu;
    private View content;

    //constants
    protected static final int menuMargin = 175;

    public enum menuState{
        CLOSED, OPEN, CLOSING, OPENING
    };

    protected int currentContentOffset = 0;
    protected menuState menuCurrentState = menuState.CLOSED;

    //animation objects
    protected Scroller menuAnimationScroller = new Scroller(this.getContext(),
            new SmoothInterpolator());
    protected Runnable menuAnimationRunnable = new AnimationRunnable();
    protected Handler menuAnimationHandler = new Handler();

    //animation constants
    private static final int menuAnimationDuration = 300;
    private static final int menuAnimationPollingInterval = 16;

    /**
     * Constructor
     * @author Chris Simmons
     * @param context
     * @param attrs
     * @param defStyle
     */
    public FlyOutContainer(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    /**
     * Construtor
     * @author Chris Simmons
     * @param context
     * @param attrs
     */
    public FlyOutContainer(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    /**
     * Constructor
     * @author
     * @param context
     */
    public FlyOutContainer(Context context){
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);

        this.menu.setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed)
            this.calculateChildDimensions();

        this.menu.layout(left, top, right - menuMargin, bottom);

        this.content.layout(left+this.currentContentOffset, top,
                right + this.currentContentOffset, bottom);
    }

    /**
     * switches the arrows from left facing and right facing
     * @author Chris Simmons
     */
    public void toggleMenu(){
        ImageButton ib = (ImageButton) findViewById(R.id.menuButton);
        switch(this.menuCurrentState) {
            case CLOSED:
                ib.setBackgroundResource(R.drawable.arrow_right2);
                this.menuCurrentState = menuState.OPENING;
                this.menu.setVisibility(View.VISIBLE);
                this.menuAnimationScroller.startScroll(0, 0, this.getMenuWidth(),
                        0, menuAnimationDuration);
                Log.d("OPENING", "Changing menuState to OPENING");
                break;
            case OPEN:
                ib.setBackgroundResource(R.drawable.arrow_left2);
                this.menuCurrentState = menuState.CLOSING;
                this.menuAnimationScroller.startScroll(this.currentContentOffset,
                        0, -this.currentContentOffset, 0, menuAnimationDuration);
                Log.d("CLOSING", "Changing menuState to CLOSING");
                break;
            default:
                Log.i("INPROCESS", "TRYING TO CHANGE DURING PROCESS");
                return;
        }

        this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable,
                menuAnimationPollingInterval);
    }

    private int getMenuWidth(){
        return this.menu.getLayoutParams().width;
    }

    private void calculateChildDimensions(){
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();

        this.menu.getLayoutParams().width = this.getWidth() - menuMargin;
        this.menu.getLayoutParams().height = this.getHeight();
    }
    private void adjustContentPosition(boolean isAnimationOngoing) {
        int scrollerOffset = this.menuAnimationScroller.getCurrX();

        this.content.offsetLeftAndRight(scrollerOffset
                - this.currentContentOffset);

        this.currentContentOffset = scrollerOffset;

        this.invalidate();

        if (isAnimationOngoing)
            this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable,
                    menuAnimationPollingInterval);
        else
            this.onMenuTransitionComplete();
    }

    private void onMenuTransitionComplete() {
        switch (this.menuCurrentState) {
            case OPENING:
                this.menuCurrentState = menuState.OPEN;
                break;
            case CLOSING:
                this.menuCurrentState = menuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
            default:
                return;
        }
    }

    protected class SmoothInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float t) {
            return (float)Math.pow(t-1, 5) + 1;
        }

    }

    protected class AnimationRunnable implements Runnable {

        @Override
        public void run() {
            FlyOutContainer.this
                    .adjustContentPosition(FlyOutContainer.this.menuAnimationScroller
                            .computeScrollOffset());
        }

    }
}
