package org.commonmark.renderer.spannable.text.style;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class HeaderSpan extends MetricAffectingSpan {
    private final float mTextSize;
    private final Typeface mTypeface;

    public HeaderSpan(float textSize, Typeface typeface) {
        mTextSize = textSize;
        mTypeface = typeface;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        apply(tp);
    }

    @Override
    public void updateMeasureState(TextPaint tp) {
        apply(tp);
    }

    private void apply(TextPaint tp) {
        tp.setTextSize(mTextSize);
        tp.setTypeface(mTypeface);
    }
}
