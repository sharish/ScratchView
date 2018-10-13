/**
 *
 * Copyright 2016 Harish Sridharan

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cooltechworks.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import com.cooltechworks.utils.BitmapUtils;

/**
 * Created by Harish on 25/03/16.
 */
public class ScratchTextView extends AppCompatTextView {


    public interface IRevealListener {
        void onRevealed(ScratchTextView tv);
        void onRevealPercentChangedListener(ScratchTextView stv, float percent);
    }

    public static final float STROKE_WIDTH = 12f;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    /**
     * Bitmap holding the scratch region.
     */
    private Bitmap mScratchBitmap;

    /**
     * Drawable canvas area through which the scratchable area is drawn.
     */
    private Canvas mCanvas;

    /**
     * Path holding the erasing path done by the user.
     */
    private Path mErasePath;

    /**
     * Path to indicate where the user have touched.
     */
    private Path mTouchPath;

    /**
     * Paint properties for drawing the scratch area.
     */
    private Paint mBitmapPaint;

    /**
     * Paint properties for erasing the scratch region.
     */
    private Paint mErasePaint;

    /**
     * Gradient paint properties that lies as a background for scratch region.
     */
    private Paint mGradientBgPaint;

    /**
     * Sample Drawable bitmap having the scratch pattern.
     */
    private BitmapDrawable mDrawable;


    /**
     * Listener object callback reference to send back the callback when the text has been revealed.
     */
    private IRevealListener mRevealListener;

    /**
     * Reveal percent value.
     */
    private float mRevealPercent;

    /**
     * Thread Count
     */
    private int mThreadCount = 0;



    public ScratchTextView(Context context) {
        super(context);
        init();

    }

    public ScratchTextView(Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    public ScratchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Set the strokes width based on the parameter multiplier.
     * @param multiplier can be 1,2,3 and so on to set the stroke width of the paint.
     */
    public void setStrokeWidth(int multiplier) {
        mErasePaint.setStrokeWidth(multiplier * STROKE_WIDTH);
    }

    /**
     * Initialises the paint drawing elements.
     */
    private void init() {


        mTouchPath = new Path();

        mErasePaint = new Paint();
        mErasePaint.setAntiAlias(true);
        mErasePaint.setDither(true);
        mErasePaint.setColor(0xFFFF0000);
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeJoin(Paint.Join.BEVEL);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);
        mErasePaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
        setStrokeWidth(6);

        mGradientBgPaint = new Paint();

        mErasePath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);


        Bitmap scratchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gold);
        mDrawable = new BitmapDrawable(getResources(), scratchBitmap);
        mDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScratchBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mScratchBitmap);

        Rect rect = new Rect(0, 0, mScratchBitmap.getWidth(), mScratchBitmap.getHeight());
        mDrawable.setBounds(rect);

        int startGradientColor = ContextCompat.getColor(getContext(), R.color.scratch_start_gradient);
        int endGradientColor = ContextCompat.getColor(getContext(), R.color.scratch_end_gradient);


        mGradientBgPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), startGradientColor, endGradientColor, Shader.TileMode.MIRROR));

        mCanvas.drawRect(rect, mGradientBgPaint);
        mDrawable.draw(mCanvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawBitmap(mScratchBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mErasePath, mErasePaint);

    }

    private void touch_start(float x, float y) {

        checkRevealed();

        mErasePath.reset();
        mErasePath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    private void touch_move(float x, float y) {

        checkRevealed();

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mErasePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            drawPath();
        }

        mTouchPath.reset();
        mTouchPath.addCircle(mX, mY, 30, Path.Direction.CW);

    }

    private void drawPath() {
        mErasePath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mErasePath, mErasePaint);
        // kill this so we don't double draw
        mTouchPath.reset();
        mErasePath.reset();
        mErasePath.moveTo(mX, mY);

        checkRevealed();
    }

    /**
     * Reveals the hidden text by erasing the scratch area.
     */
    public void reveal() {

        int[] bounds = getTextBounds(1.5f);
        int left = bounds[0];
        int top = bounds[1];
        int right = bounds[2];
        int bottom = bounds[3];

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));

        mCanvas.drawRect(left, top, right, bottom, paint);
        checkRevealed();
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                drawPath();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public int getColor() {
        return mErasePaint.getColor();
    }


    public void setRevealListener(IRevealListener listener) {
        this.mRevealListener = listener;
    }

    public boolean isRevealed() {
        return mRevealPercent == 1;
    }

    private void checkRevealed() {

        if(! isRevealed() && mRevealListener != null) {

            int[] bounds = getTextBounds();
            int left = bounds[0];
            int top = bounds[1];
            int width = bounds[2] - left;
            int height = bounds[3] - top;


            // Do not create multiple calls to compare.
            if(mThreadCount > 1) {
                return;
            }

            mThreadCount++;

            new AsyncTask<Integer, Void, Float>() {

                @Override
                protected Float doInBackground(Integer... params) {

                    try {
                        int left = params[0];
                        int top = params[1];
                        int width = params[2];
                        int height = params[3];

                        Bitmap croppedBitmap = Bitmap.createBitmap(mScratchBitmap, left, top, width, height);

                        return BitmapUtils.getTransparentPixelPercent(croppedBitmap);
                    } finally {
                        mThreadCount--;

                    }
                }

                public void onPostExecute(Float percentRevealed) {

                    // check if not revealed before.
                    if( ! isRevealed()) {

                        float oldValue = mRevealPercent;
                        mRevealPercent = percentRevealed;

                        if(oldValue != percentRevealed) {
                            mRevealListener.onRevealPercentChangedListener(ScratchTextView.this, percentRevealed);
                        }

                        // if now revealed.
                        if( isRevealed()) {
                            mRevealListener.onRevealed(ScratchTextView.this);
                        }
                    }
                }
            }.execute(left, top, width, height);

        }
    }


    private static int[] getTextDimens(String text, Paint paint) {

        int end = text.length();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, end, bounds);
        int width = bounds.left + bounds.width();
        int height = bounds.bottom + bounds.height();

        return new int[] { width, height};
    }

    private int[] getTextBounds() {
        return getTextBounds(1f);
    }

    private int[] getTextBounds(float scale) {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int vwidth = getWidth();
        int vheight = getHeight();

        int centerX = vwidth/2;
        int centerY = vheight/2;


        TextPaint paint = getPaint();

        String text = getText().toString();

        int[] dimens = getTextDimens(text, paint);
        int width = dimens[0];
        int height = dimens[1];

        int lines = getLineCount();
        height = height * lines;
        width = width / lines;


        int left = 0;
        int top = 0;

        if(height > vheight) {
            height = vheight - ( paddingBottom + paddingTop);
        }
        else {
            height = (int) (height * scale);
        }

        if(width > vwidth) {
            width = vwidth - (paddingLeft + paddingRight);
        }
        else {
            width = (int) (width * scale);
        }

        int gravity = getGravity();

        if((gravity & Gravity.START) == Gravity.START) {
            left = paddingLeft;
        }

        else if((gravity & Gravity.END) == Gravity.END) {
            left = (vwidth - paddingRight) - width;
        }
        else if((gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            left = centerX - width / 2;
        }

        if((gravity & Gravity.TOP) == Gravity.TOP) {
            top = paddingTop;
        }
        else if((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            top = (vheight - paddingBottom) - height;
        }

        else if((gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
            top = centerY - height / 2;
        }

        return new int[] {left, top, left + width, top + height};
    }


}
