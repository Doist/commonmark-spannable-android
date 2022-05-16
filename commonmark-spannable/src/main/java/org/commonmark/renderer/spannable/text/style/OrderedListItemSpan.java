package org.commonmark.renderer.spannable.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;

public class OrderedListItemSpan extends ListItemSpan {
    private final int mMarkerNumber;

    public OrderedListItemSpan(OrderedListItemData data, int leading, int extraHeight, int leftMargin) {
        super((int) (data.mLeadingMultiplier * leading), extraHeight, leftMargin);
        mMarkerNumber = data.mMarkerNumber;
    }

    @Override
    protected void drawMarker(Canvas c, Paint p, int x, int baseline, int top, int bottom, int lineLeading,
                              int markerLeftMargin, int lineExtraSpace) {
        String text = mMarkerNumber + ". ";
        float textSize = p.measureText(text);
        c.drawText(text, x + (lineLeading - textSize) / 2, baseline, p);
    }

    public static class OrderedListItemData {
        final float mLeadingMultiplier;
        final int mMarkerNumber;

        public OrderedListItemData(float leadingMultiplier, int markerNumber) {
            mLeadingMultiplier = leadingMultiplier;
            mMarkerNumber = markerNumber;
        }
    }
}
