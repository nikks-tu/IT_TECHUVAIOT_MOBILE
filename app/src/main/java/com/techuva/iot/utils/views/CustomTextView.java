package com.techuva.iot.utils.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextView extends AppCompatTextView {

        public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public CustomTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public CustomTextView(Context context) {
            super(context);
            init();
        }

        private void init() {
            if (!isInEditMode()) {
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font.ttf");
                setTypeface(tf);
            }
        }

    }

