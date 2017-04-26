package com.maibo.lvyongsheng.xianhui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.maibo.lvyongsheng.xianhui.R;
import com.maibo.lvyongsheng.xianhui.myinterface.OnLetterTouchListener;


/**
 * side bar like WeChat
 * Created by zz on 2016/5/12.
 */
public class ZzLetterSideBar extends View {

    public static final String TAG = ZzLetterSideBar.class.getSimpleName();

    private String[] letters;

    private OnLetterTouchListener letterTouchListener;
    private ListView lv;
    private ZzRecyclerView rv;
    private TextView tvDialog;

    private float itemHeight = -1;

    private Paint paint;

    public static String[] b = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private Bitmap letterBitmap;

    public ZzLetterSideBar(Context context) {
        super(context);

        init(context);
    }

    public ZzLetterSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public ZzLetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        setShowString(b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (letters == null) {
            return;
        }

        if (itemHeight == -1) {
            itemHeight = getHeight() / letters.length;
        }

        if (paint == null) {
            //准备画笔
            paint = new Paint();
            paint.setTextSize(itemHeight - 4);
            paint.setColor(Color.rgb(84, 84, 84));
//            Paint.FILTER_BITMAP_FLAG是使位图过滤的位掩码标志
//            Paint.ANTI_ALIAS_FLAG是使位图抗锯齿的标志
//            Paint.DITHER_FLAG是使位图进行有利的抖动的位掩码标志
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            //准备画布
            Canvas mCanvas = new Canvas();
            letterBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mCanvas.setBitmap(letterBitmap);
            float widthCenter = getMeasuredWidth() / 2.0f;
            for (int i = 0; i < letters.length; i++) {
                mCanvas.drawText(letters[i], widthCenter - paint.measureText(letters[i]) / 2, itemHeight * i + itemHeight, paint);
            }
        }
        if (letterBitmap != null) {
            canvas.drawBitmap(letterBitmap, 0, 0, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (letterTouchListener == null || letters == null) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //set background gray
                setBackgroundResource(R.drawable.side_bar_bg);
            case MotionEvent.ACTION_MOVE:
                int position = (int) (event.getY() / itemHeight);
                if (position >= 0 && position < letters.length) {
                    if (tvDialog != null) {
                        tvDialog.setVisibility(View.VISIBLE);
                        tvDialog.setText(letters[position]);
                    }
                    //interface callback
                    letterTouchListener.onLetterTouch(letters[position], position);
                }
                return true;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                //set background transparent
                setBackgroundResource(android.R.color.transparent);
                //make dialog invisible
                if (tvDialog != null) {
                    tvDialog.setVisibility(View.GONE);
                }
                //interface callback
                letterTouchListener.onActionUp();
                return true;
        }
        return false;
    }


    public void setShowString(String[] letters) {
        this.letters = letters;
    }

    public void setLetterTouchListener(OnLetterTouchListener letterTouchListener) {
        this.letterTouchListener = letterTouchListener;
    }
}
