package org.commonmark.renderer.spannable.internal;

import org.commonmark.node.OrderedList;
import org.commonmark.renderer.spannable.text.style.OrderedListItemSpan;

import androidx.annotation.Nullable;

public class OrderedListHolder extends ListHolder {
    private int counter;

    public OrderedListHolder(ListHolder parent, OrderedList list, boolean shouldKeepOrder) {
        super(parent);

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
    public Object getSpanParameter() {
        return counter;
    }
}
