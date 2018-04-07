package com.that2u.android.app.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.that2u.android.app.utils.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by phama on 2017-03-10.
 */

public class CountDownGiftCoinView extends LinearLayout {
    private static final int SECOND_TO_MILLISECOND = 1000;

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private static final int NOT_SET_VALUE = -1;

    private TextView mGiftNameTextView;
    private TextView mCountdownTextView;
    private View mGiftView;

    private final long mDefaultCountdownTime;

    private SimpleDateFormat mTimeFormat;
    private Calendar mCalendar;
    private long mCountdownTime;
    private CountDownTimer mTimer;

    private OnReceiveGiftListener mListener;

    public CountDownGiftCoinView(Context context) {
        this(context, null);
    }

    public CountDownGiftCoinView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CountDownGiftCoinView, 0, 0);
        String giftName = a.getString(R.styleable.CountDownGiftCoinView_giftName);
        mDefaultCountdownTime = a.getInt(R.styleable.CountDownGiftCoinView_countdownTime, 0);
        String timeFormat = a.getString(R.styleable.CountDownGiftCoinView_timeFormat);
        int layoutId = a.getResourceId(R.styleable.CountDownGiftCoinView_giftLayout, -1);
        float textSize = a.getDimension(R.styleable.CountDownGiftCoinView_textSize, NOT_SET_VALUE);
        int textColor = a.getColor(R.styleable.CountDownGiftCoinView_textColor, NOT_SET_VALUE);
        Drawable iconDrawable = a.getDrawable(R.styleable.CountDownGiftCoinView_icon);

        a.recycle();

        if (Strings.isNullOrEmpty(timeFormat)) {
            timeFormat = DEFAULT_TIME_FORMAT;
        }

        inflate(context, R.layout.gift_coin_layout, this);

        mGiftNameTextView = (TextView) findViewById(R.id.gift_name);

        mCountdownTextView = (TextView) findViewById(R.id.countdown_text);

        Log.d("PAM", "textSize: " + textSize);

        if (textSize != NOT_SET_VALUE) {
            mGiftNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mCountdownTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        if (textColor != NOT_SET_VALUE) {
            mGiftNameTextView.setTextColor(textColor);
            mCountdownTextView.setTextColor(textColor);
        }

        if (iconDrawable != null) {
//            ImageView giftIconView = (ImageView) findViewById(R.id.gift_icon_view);
            mGiftNameTextView.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);
//            giftIconView.setImageDrawable(iconDrawable);
        }

        ViewStub viewStub = (ViewStub) findViewById(R.id.viewstub);
        if (layoutId > 0) {
            viewStub.setLayoutResource(layoutId);
            mGiftView = viewStub.inflate();
            mGiftView.setVisibility(GONE);
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onReceiveGift(CountDownGiftCoinView.this);
                }
            }
        });
        this.setClickable(false);

        mGiftNameTextView.setText(giftName);

        mTimeFormat = new SimpleDateFormat(timeFormat);
        if (!isInEditMode()) {
            mTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        mCalendar = Calendar.getInstance();

        if (context instanceof OnReceiveGiftListener) {
            mListener = (OnReceiveGiftListener) context;
        }

        setTime(mDefaultCountdownTime);
    }

    public void setGiftName(String giftName) {
        if (mGiftNameTextView != null) {
            mGiftNameTextView.setText(giftName);
        }
    }

    public View getGiftView() {
        return mGiftView;
    }

    public void setOnReceiveGiftListener(OnReceiveGiftListener listener) {
        mListener = listener;
    }

    public void setTime(long millisecond) {
        cancelTimer();
        mCountdownTime = millisecond;
        if (millisecond > 0) {
            setGiftViewVisible(false);
            updateCountdownText(mCountdownTime);
        } else {
            setGiftViewVisible(true);
        }
    }

    public void startCountDown() {
        cancelTimer();

        mTimer = new CountDownTimer(mCountdownTime, SECOND_TO_MILLISECOND) {
            @Override
            public void onTick(long l) {
                updateCountdownText(l);
            }

            @Override
            public void onFinish() {
                setGiftViewVisible(true);
            }
        };
        mTimer.start();
    }

    private void setGiftViewVisible(boolean isVisible) {
        mGiftView.setVisibility(isVisible ? VISIBLE : GONE);
        mCountdownTextView.setVisibility(isVisible ? GONE : VISIBLE);
        this.setClickable(isVisible);
    }

    public void restartCountdown() {
        setGiftViewVisible(false);

        setTime(mDefaultCountdownTime);
        startCountDown();
    }

    private void updateCountdownText(long millisecond) {
        mCalendar.setTimeInMillis(millisecond);
        mCountdownTextView.setText(mTimeFormat.format(mCalendar.getTime()));
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startCountDown();

        if (getContext() instanceof OnReceiveGiftListener) {
            mListener = (OnReceiveGiftListener) getContext();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelTimer();
        mListener = null;
        super.onDetachedFromWindow();
    }

    public interface OnReceiveGiftListener {
        void onReceiveGift(View view);
    }
}
