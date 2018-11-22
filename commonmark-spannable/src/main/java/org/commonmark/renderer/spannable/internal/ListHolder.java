package org.commonmark.renderer.spannable.internal;

import androidx.annotation.Nullable;

public abstract class ListHolder {
    private final ListHolder parent;

    ListHolder(ListHolder parent) {
        this.parent = parent;
    }

    public ListHolder getParent() {
        return parent;
    }

    public abstract Class getSpanClass();

    @Nullable
    public abstract Object getSpanParameter();
}
