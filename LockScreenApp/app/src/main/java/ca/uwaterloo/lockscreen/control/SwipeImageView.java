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
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ca.uwaterloo.lockscreen.R;
import ca.uwaterloo.lockscreen.imagelock.ImagePrototype;
import ca.uwaterloo.lockscreen.imagelock.ImageProvider;
import ca.uwaterloo.lockscreen.imagelock.ImageUnlocker;
import ca.uwaterloo.lockscreen.utils.Logger;

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
    private Drawable smile1;
    private Drawable smile2;
    private Drawable sad1;
    private Drawable sad2;
    private GestureDetector gestureDetector;
    private long startTime;


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

        gestureDetector = new GestureDetector(context, new GestureListener());

        smile1 = context.getResources().getDrawable(R.drawable.smile1);
        smile2 = context.getResources().getDrawable(R.drawable.smile2);
        sad1 = context.getResources().getDrawable(R.drawable.sad1);
        sad2 = context.getResources().getDrawable(R.drawable.sad2);

        startTime = -1;
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

        if(image == null) {
            return;
        }

        Point adjustedOffset = new Point();
        adjustedOffset.x = (int)offset.x;
        adjustedOffset.y = (int)offset.y;

        if(Math.abs(offset.y) > 0.28 * canvas.getHeight()){
            adjustedOffset.y = canvas.getHeight() * 4 / 5;
            currentDropArea = ImagePrototype.DROP_AREA.BOTTOM;
            if(offset.y < 0){
                adjustedOffset.y *= -1;
                currentDropArea = ImagePrototype.DROP_AREA.TOP;
            }
            adjustedOffset.x = 0;
        }else if(Math.abs(offset.x) > 0.35 * canvas.getWidth()){
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

        /*if(nextImage != null){
            canvas.drawBitmap(
                    nextImage,
                    new Rect(0,0,nextImage.getWidth(),nextImage.getHeight()),
                    new Rect(0,0,canvas.getWidth(),canvas.getHeight()),
                    paint);
        }*/
        canvas.drawBitmap(
                image,
                new Rect(0,0,image.getWidth(),image.getHeight()),
                new Rect(adjustedOffset.x,adjustedOffset.y,
                        canvas.getWidth() + adjustedOffset.x,canvas.getHeight() + adjustedOffset.y),
                paint);



        int emojiSize = canvas.getWidth() / 5;
        int emojiTop = emojiSize / 4;
        int emojiLeft = canvas.getWidth() / 2 - emojiSize / 2;
        smile2.setBounds(emojiLeft, emojiTop, emojiLeft + emojiSize, emojiTop + emojiSize);
        sad2.setBounds(emojiLeft, canvas.getHeight() - emojiSize - emojiTop, emojiLeft + emojiSize, canvas.getHeight() - emojiTop );

        smile2.draw(canvas);
        sad2.draw(canvas);

        emojiTop = canvas.getHeight() / 2 - emojiSize / 3;
        emojiLeft = emojiSize / 4;
        sad1.setBounds(emojiLeft, emojiTop, emojiLeft + emojiSize, emojiTop + emojiSize);
        smile1.setBounds(canvas.getWidth() - emojiSize - emojiLeft, emojiTop, canvas.getWidth() - emojiLeft, emojiTop + emojiSize);

        smile1.draw(canvas);
        sad1.draw(canvas);


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
        if(startTime < 0){
            startTime = System.currentTimeMillis();
        }

        boolean res = gestureDetector.onTouchEvent(event);

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

            gestureFinished();
        }
        invalidate();

        return res;
    }

    private void gestureFinished() {
        try {
            imgProvider.dropAreaSelected(currentDropArea);
        } catch (ImageUnlocker.ImageUnlockFailedException e) {
            Logger.log(context, "UNLOCK FAILED in: " + getCurrentTime() + ";");
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
            Logger.log(context, "UNLOCKED in: " + getCurrentTime() + ";");
            startTime = -1;
            activity.finish();
        }

        startDragPosition = null;
        offset = new PointF(0,0);
        currentDropArea = ImagePrototype.DROP_AREA.NONE;
    }

    public double getCurrentTime(){
        double difference = System.currentTimeMillis() - startTime;
        return difference / 1000;
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;



        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        currentDropArea = ImagePrototype.DROP_AREA.RIGHT;
        gestureFinished();
    }

    public void onSwipeLeft() {
        currentDropArea = ImagePrototype.DROP_AREA.LEFT;
        gestureFinished();
    }

    public void onSwipeTop() {
        currentDropArea = ImagePrototype.DROP_AREA.TOP;
        gestureFinished();
    }

    public void onSwipeBottom() {
        currentDropArea = ImagePrototype.DROP_AREA.BOTTOM;
        gestureFinished();
    }

}
