package org.commonmark.renderer.spannable.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class QuoteSpan extends MetricAffectingSpan implements LeadingParagraphSpan {
    private final int mStripeColor;
    private final int mStripeSize;
    private final int mPadding;

    private int mParentLeadingMargin;

    public QuoteSpan(int stripeColor, int stripeSize, int padding) {
        mStripeColor = stripeColor;
        mStripeSize = stripeSize;
        mPadding = padding;
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint);
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        apply(paint);
    }

    @Override
    public void increaseLeadingMargin(int leadingMargin) {
        mParentLeadingMargin += leadingMargin;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mStripeSize + mPadding;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom,
                                  CharSequence text, int start, int end, boolean first, Layout l) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(mStripeColor);

        c.drawRect(mParentLeadingMargin, top, mParentLeadingMargin + dir * mStripeSize, bottom, p);

        p.setColor(color);
        p.setStyle(style);
    }

    private void apply(TextPaint paint) {
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
    }
}
