package org.commonmark.renderer.spannable.internal;

import org.commonmark.renderer.spannable.SpannableProvider;

import java.util.HashMap;
import java.util.Map;

public class SpannableProviderMap {
    private final Map<Class<?>, SpannableProvider> map = new HashMap<>(16);

    public void add(SpannableProvider provider) {
        for (Class<?> spanClass : provider.getSpanTypes()) {
            // Overwrite existing provider.
            map.put(spanClass, provider);
        }
    }

    public SpannableProvider get(Class<?> spanClass) {
        SpannableProvider provider = map.get(spanClass);
        if (provider == null) {
            throw new IllegalStateException("provider for '" + spanClass + "' not found");
        }
        return provider;
    }
}
