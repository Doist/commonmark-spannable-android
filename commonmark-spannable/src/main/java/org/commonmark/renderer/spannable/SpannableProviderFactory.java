package org.commonmark.renderer.spannable;

/**
 * Factory for instantiating new spannable provider.
 */
public interface SpannableProviderFactory {
    /**
     * Creates a new spannable provider for the specified provider context.
     *
     * @param context the context for provider (normally passed on to the provider)
     * @return a spannable provider
     */
    SpannableProvider create(SpannableProviderContext context);
}