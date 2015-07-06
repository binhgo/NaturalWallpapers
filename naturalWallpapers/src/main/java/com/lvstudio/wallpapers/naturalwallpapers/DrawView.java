package com.lvstudio.wallpapers.naturalwallpapers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener
{

    private Paint bmPaint = new Paint();
    private Paint drawPaint = new Paint();
    private Path path = new Path();

    private Canvas cv = null;
    private Bitmap bm = null;
    private boolean firstTimeThru = true;


    public DrawView(Context context)
    {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs)
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
    public void onDraw(Canvas canvas)
    {
        // Set everything up the first time anything gets drawn:
        if (firstTimeThru)
        {
            firstTimeThru = false;

            // Just quickly fill the view with a black mask:
            canvas.drawColor(Color.BLACK);

            // Create a new bitmap and canvas and fill it with a black mask:
            bm = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
            cv = new Canvas();
            cv.setBitmap(bm);
            cv.drawColor(Color.BLACK);

            // Specify that painting will be with fat strokes:
            drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            drawPaint.setStrokeWidth(canvas.getWidth() / 15);

            // Specify that painting will clear the pixels instead of paining new ones:
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        cv.drawPath(path, drawPaint);
        canvas.drawBitmap(bm, 0, 0, bmPaint);

        super.onDraw(canvas);
    }

    public boolean onTouch(View view, MotionEvent event)
    {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {

            // Set the starting position of a new line:
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos, yPos);
                return true;

            // Draw a line to the ending position:
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;

            default:
                return false;
        }

        // Call onDraw() to redraw the whole view:
        invalidate();
        return true;
    }
}
