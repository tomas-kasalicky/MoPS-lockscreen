package ca.uwaterloo.lockscreen.control;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import ca.uwaterloo.lockscreen.LockScreenActivity;
import ca.uwaterloo.lockscreen.imagelock.ImagePrototype;
import ca.uwaterloo.lockscreen.imagelock.ImageProvider;
import ca.uwaterloo.lockscreen.imagelock.ImageUnlocker;

/**
 * Created by kasal on 07.11.2016.
 */

public class SwipeImageView extends ImageView {



    private ImagePrototype.DROP_AREA currentDropArea = ImagePrototype.DROP_AREA.NONE;

    private Time showErrorTime = new Time();

    private Context context;
    private PointF startDragPosition;
    private PointF offset;
    private ImageProvider imgProvider;
    private Activity activity;

    public SwipeImageView(Context context) {
        super(context);
        init(context);
    }

    public SwipeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
/*
    public SwipeImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
*/
    private void init(Context context){
        this.context = context;
        offset = new PointF(0,0);
        showErrorTime.set(0);
    }

    public void setImageProvider(ImageProvider unlocker){
        this.imgProvider = unlocker;
        invalidate();
    }

    public void setActivity(Activity act){
        this.activity = act;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(imgProvider == null) {
            return;
        }

        Bitmap image = imgProvider.getCurrentImage();
        Bitmap nextImage = imgProvider.getNextImage();

        Point adjustedOffset = new Point();
        adjustedOffset.x = (int)offset.x;
        adjustedOffset.y = (int)offset.y;

        if(Math.abs(offset.y) > 0.45 * canvas.getHeight()){
            adjustedOffset.y = canvas.getHeight() * 4 / 5;
            currentDropArea = ImagePrototype.DROP_AREA.BOTTOM;
            if(offset.y < 0){
                adjustedOffset.y *= -1;
                currentDropArea = ImagePrototype.DROP_AREA.TOP;
            }
            adjustedOffset.x = 0;
        }else if(Math.abs(offset.x) > 0.4 * canvas.getWidth()){
            adjustedOffset.x = canvas.getWidth() * 4 / 5;
            currentDropArea = ImagePrototype.DROP_AREA.RIGHT;
            if(offset.x < 0){
                adjustedOffset.x *= -1;
                currentDropArea = ImagePrototype.DROP_AREA.LEFT;
            }
            adjustedOffset.y = 0;
        }else{
            currentDropArea = ImagePrototype.DROP_AREA.NONE;
        }

        Paint paint = new Paint();

        if(nextImage != null){
            canvas.drawBitmap(
                    nextImage,
                    new Rect(0,0,nextImage.getWidth(),nextImage.getHeight()),
                    new Rect(0,0,canvas.getWidth(),canvas.getHeight()),
                    paint);
        }
        canvas.drawBitmap(
                image,
                new Rect(0,0,image.getWidth(),image.getHeight()),
                new Rect(adjustedOffset.x,adjustedOffset.y,
                        canvas.getWidth() + adjustedOffset.x,canvas.getHeight() + adjustedOffset.y),
                paint);



        Time currTime = new Time();
        currTime.setToNow();
        if((currTime.toMillis(true) - showErrorTime.toMillis(true)) < Toast.LENGTH_LONG) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            String errorMessage = "Incorrect sequence, try again.";
            float textWidth = paint.measureText(errorMessage);

            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.BLACK);
            canvas.drawRoundRect(
                    new RectF(
                            canvas.getWidth() / 2 - textWidth / 2 - 20,
                            canvas.getHeight() * 3 / 4 - 60,
                            canvas.getWidth() / 2 + textWidth / 2 + 20,
                            canvas.getHeight() * 3 / 4 + 30),
                    10,
                    10,
                    rectPaint);

            canvas.drawText(errorMessage, canvas.getWidth() / 2 - textWidth / 2, canvas.getHeight() * 3 / 4, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            startDragPosition = new PointF(event.getX(),event.getY());
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            PointF currentPosition = new PointF(event.getX(),event.getY());
            if(startDragPosition != null){
                offset.x = currentPosition.x - startDragPosition.x;
                offset.y = currentPosition.y - startDragPosition.y;
            }else{
                startDragPosition = currentPosition;
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){

            try {
                imgProvider.dropAreaSelected(currentDropArea);
            } catch (ImageUnlocker.ImageUnlockFailedException e) {
                showErrorTime.setToNow();
                new CountDownTimer(Toast.LENGTH_LONG + 1000, Toast.LENGTH_LONG + 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        invalidate();
                    }
                }.start();

            }

            if(imgProvider.isFinished()){
                activity.finish();
            }

            startDragPosition = null;
            offset = new PointF(0,0);
            currentDropArea = ImagePrototype.DROP_AREA.NONE;
        }
        invalidate();
        return true;
    }

}
