package org.commonmark.renderer.spannable.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;

public class OrderedListItemSpan extends ListItemSpan {
    private final int mMarkerNumber;

    public OrderedListItemSpan(int order, int leading, int extraHeight, int leftMargin) {
        super(leading, extraHeight, leftMargin);
        mMarkerNumber = order;
    }

    @Override
    protected void drawMarker(Canvas c, Paint p, int x, int baseline, int top, int bottom, int lineLeading,
                              int markerLeftMargin, int lineExtraSpace) {
        String text = mMarkerNumber + ". ";
        float textSize = p.measureText(text);
        c.drawText(text, x + (lineLeading - textSize) / 2, baseline, p);
    }
}
