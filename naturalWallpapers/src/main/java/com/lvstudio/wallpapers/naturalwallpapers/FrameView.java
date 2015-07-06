package com.lvstudio.wallpapers.naturalwallpapers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by apple on 7/2/15.
 */
public class FrameView extends View implements View.OnTouchListener
{

    private Paint bmPaint = new Paint();
    private Paint drawPaint = new Paint();
    private Path path = new Path();
    private Canvas cv = null;

    private Bitmap bm = null;
    private boolean firstTimeThru = true;
    int left =0;
    int top=0;

    public FrameView(Context context)
    {
        super(context);
        init();
    }

    public FrameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void init()
    {
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {

            // Set the starting position of a new line:
            case MotionEvent.ACTION_DOWN:
                //path.moveTo(xPos, yPos);
                left = (int)xPos;
                top = (int)yPos;
                return true;

            // Draw a line to the ending position:
            case MotionEvent.ACTION_MOVE:
                //path.lineTo(xPos, yPos);
                left = (int)xPos;
                top = (int)yPos;
                break;

            default:
                return false;
        }

        // Call onDraw() to redraw the whole view:
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        // Just quickly fill the view with a black mask:
        //canvas.drawColor(Color.BLACK);

        // Create a new bitmap and canvas and fill it with a black mask:
        //bm = Bitmap.createBitmap(canvas.getWidth()/2, canvas.getHeight()/2, Bitmap.Config.ARGB_8888);
        //cv = new Canvas();
        //cv.setBitmap(bm);
        //cv.drawColor(Color.CYAN);
        //cv.drawRect(left, top, left + bm.getWidth() , top+bm.getHeight(), bmPaint);

        // Specify that painting will be with fat strokes:
        //drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //drawPaint.setStrokeWidth(canvas.getWidth() / 15);

        // Specify that painting will clear the pixels instead of paining new ones:
        //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


        //cv.drawPath(path, drawPaint);
        //canvas.drawBitmap(bm, left/2, top/2, bmPaint);
        canvas.drawRect(left, top, left+100, top + 200, bmPaint);

        super.onDraw(canvas);
    }
}
