package com.example.cyberpay_android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.EditText;

import com.example.cyberpay_android.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardNumberEditText extends TextInputEditText {

    private SparseArray<Pattern> mCCPatterns =  null;
    private int mCurrentDrawableResId = 0;
    private Drawable mCurrentDrawable;

    public CardNumberEditText(Context context) {
        super(context);
        init();
    }

    public CardNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        if (mCCPatterns == null) {
            mCCPatterns = new SparseArray<>();
            // With spaces for credit card masking
            mCCPatterns.put(R.drawable.ic_visa_logo, Pattern.compile(
                    "^4[0-9]{2,12}(?:[0-9]{3})?$"));
            mCCPatterns.put(R.drawable.ic_mastercard_icon, Pattern.compile(
                    "^5[1-5][0-9]{1,14}$"));

        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter){
        if (mCCPatterns == null) {
            init();
        }
        int mDrawableResId = 0;
        for (int i = 0; i < mCCPatterns.size(); i++) {
            int key = mCCPatterns.keyAt(i);
            // get the object by the key.
            Pattern p = mCCPatterns.get(key);
            Matcher m = p.matcher(text);
            if (m.find()) {
                mDrawableResId = key;
                break;
            }
        }
        if (mDrawableResId > 0 && mDrawableResId !=
                mCurrentDrawableResId) {
            mCurrentDrawableResId = mDrawableResId;
        } else if (mDrawableResId == 0) {
            int mDefaultDrawableResId = R.drawable.ic_credit_card;
            mCurrentDrawableResId = mDefaultDrawableResId;
        }
        mCurrentDrawable = getResources()
                .getDrawable(mCurrentDrawableResId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentDrawable == null) {
            return;
        }
        // right offset for showing errors in the EditText
        int rightOffset = 0;
        if (getError() != null && getError().length() > 0) {
            rightOffset = (int) getResources().getDisplayMetrics()
                    .density * 32;
        }



        int right = getWidth() - getPaddingRight() - rightOffset;

        int top = getPaddingTop();
        int bottom = getHeight() - getPaddingBottom();
        float ratio = (float) mCurrentDrawable.getIntrinsicWidth() /
                (float) mCurrentDrawable.getIntrinsicHeight();
        //if images are the correct size.
        //int left = right - mCurrentDrawable.getIntrinsicWidth();
        //scale image depending on height available.
        int left = (int) (right - ((bottom - top) * ratio));
        mCurrentDrawable.setBounds(left, top, right, bottom);

        mCurrentDrawable.draw(canvas);

    }



}
