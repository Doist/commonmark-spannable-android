package org.commonmark.renderer.spannable.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;

public class QuoteSpan extends ItalicSpan implements LeadingParagraphSpan {
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
}
