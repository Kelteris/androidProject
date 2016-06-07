package com.cs246.rmgroup.rmplanner;

import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.content.Context;

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
        CLOSED, OPEN
    };

    protected int currentContentOffset = 0;
    protected menuState menuCurrentState = menuState.CLOSED;

    public FlyOutContainer(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
    public FlyOutContainer(Context context, AttributeSet attrs){
        super(context, attrs);
    }
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

    public void toggleMenu(){
        switch(this.menuCurrentState) {
            case CLOSED:
                this.menu.setVisibility(View.VISIBLE);
                this.currentContentOffset = this.getMenuWidth();
                this.content.offsetLeftAndRight(currentContentOffset);
                this.menuCurrentState = menuState.OPEN;
                break;
            case OPEN:
                this.content.offsetLeftAndRight(-currentContentOffset);
                this.currentContentOffset = 0;
                this.menuCurrentState = menuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
        }
        this.invalidate();
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
}
