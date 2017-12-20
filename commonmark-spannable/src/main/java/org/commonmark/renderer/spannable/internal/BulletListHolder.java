package org.commonmark.renderer.spannable.internal;

import org.commonmark.renderer.spannable.text.style.UnorderedListItemSpan;

import android.support.annotation.Nullable;

public class BulletListHolder extends ListHolder {
    public BulletListHolder(ListHolder parent) {
        super(parent);
    }

    @Override
    public Class getSpanClass() {
        return UnorderedListItemSpan.class;
    }

    @Nullable
    @Override
    public Object getSpanParameter() {
        return null;
    }
}
