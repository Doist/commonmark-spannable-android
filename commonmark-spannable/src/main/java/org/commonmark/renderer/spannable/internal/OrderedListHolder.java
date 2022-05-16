package org.commonmark.renderer.spannable.internal;

import org.commonmark.node.OrderedList;
import org.commonmark.renderer.spannable.text.style.OrderedListItemSpan;

import androidx.annotation.Nullable;

public class OrderedListHolder extends ListHolder {
    private final float leadingMultiplier;

    private int counter;

    public OrderedListHolder(ListHolder parent, OrderedList list, boolean shouldKeepOrder) {
        super(parent);

        int digits = String.valueOf(list.getStartNumber()).length();
        if (!shouldKeepOrder) {
            leadingMultiplier = 1;
        } else if (digits >= 10) {
            leadingMultiplier = 3.33f;
        } else if (digits == 9) {
            leadingMultiplier = 3;
        } else if (digits == 8) {
            leadingMultiplier = 2.63f;
        } else if (digits == 7) {
            leadingMultiplier = 2.33f;
        } else if (digits == 6) {
            leadingMultiplier = 2f;
        } else if (digits == 5) {
            leadingMultiplier = 1.63f;
        } else if (digits == 4) {
            leadingMultiplier = 1.33f;
        } else {
            leadingMultiplier = 1;
        }

        counter = shouldKeepOrder ? list.getStartNumber() : 0;
    }

    public void increaseCounter() {
        counter++;
    }

    @Override
    public Class getSpanClass() {
        return OrderedListItemSpan.class;
    }

    @Nullable
    @Override
    public OrderedListItemSpan.OrderedListItemData getSpanParameter() {
        return new OrderedListItemSpan.OrderedListItemData(leadingMultiplier, counter);
    }
}
