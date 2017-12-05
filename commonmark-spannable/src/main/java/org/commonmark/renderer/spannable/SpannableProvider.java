package org.commonmark.renderer.spannable;

import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Provider for a set of spans.
 */
public interface SpannableProvider {
    /**
     * @return the types of spans that this provider handles.
     */
    Set<Class<?>> getSpanTypes();

    /**
     * Creates span object for specified class.
     *
     * @param spanClass span class for creating object {@link #getSpanTypes()}
     * @param parameter parameter passed in writer {@link SpannableWriter#start(java.lang.Class, java.lang.Object) }
     * @return spannable object.
     */
    Object create(Class<?> spanClass, @Nullable Object parameter);
}