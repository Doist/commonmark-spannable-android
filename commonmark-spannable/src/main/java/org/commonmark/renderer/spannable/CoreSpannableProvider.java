package org.commonmark.renderer.spannable;

import org.commonmark.renderer.spannable.text.style.BoldSpan;
import org.commonmark.renderer.spannable.text.style.CodeBlockSpan;
import org.commonmark.renderer.spannable.text.style.HeaderSpan;
import org.commonmark.renderer.spannable.text.style.InlineCodeSpan;
import org.commonmark.renderer.spannable.text.style.ItalicSpan;
import org.commonmark.renderer.spannable.text.style.LineSeparatorSpan;
import org.commonmark.renderer.spannable.text.style.LinkSpan;
import org.commonmark.renderer.spannable.text.style.OrderedListItemSpan;
import org.commonmark.renderer.spannable.text.style.QuoteSpan;
import org.commonmark.renderer.spannable.text.style.UnorderedListItemSpan;

import android.text.style.StrikethroughSpan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The spannable provider that provides all core spans.
 */
class CoreSpannableProvider implements SpannableProvider {
    private final SpannableProviderContext mProviderContext;

    CoreSpannableProvider(SpannableProviderContext providerContext) {
        mProviderContext = providerContext;
    }

    @Override
    public Set<Class<?>> getSpanTypes() {
        return new HashSet<>(Arrays.asList(
                BoldSpan.class,
                CodeBlockSpan.class,
                HeaderSpan.class,
                InlineCodeSpan.class,
                ItalicSpan.class,
                LineSeparatorSpan.class,
                LinkSpan.class,
                OrderedListItemSpan.class,
                QuoteSpan.class,
                StrikethroughSpan.class,
                UnorderedListItemSpan.class)
        );
    }

    @Override
    public Object create(Class<?> spanClass, Object parameter) {
        if (BoldSpan.class.equals(spanClass)) {
            return new BoldSpan();
        }

        if (ItalicSpan.class.equals(spanClass)) {
            return new ItalicSpan();
        }

        if (LinkSpan.class.equals(spanClass)) {
            return new LinkSpan((String) parameter);
        }

        if (HeaderSpan.class.equals(spanClass)) {
            switch ((int) parameter) {
                case 1:
                    return new HeaderSpan(
                            mProviderContext.getHeader1TextSize(),
                            mProviderContext.getHeader1Typeface());
                case 2:
                    return new HeaderSpan(
                            mProviderContext.getHeader2TextSize(),
                            mProviderContext.getHeader2Typeface());
                case 3:
                    return new HeaderSpan(
                            mProviderContext.getHeader3TextSize(),
                            mProviderContext.getHeader3Typeface());
                case 4:
                    return new HeaderSpan(
                            mProviderContext.getHeader4TextSize(),
                            mProviderContext.getHeader4Typeface());
                case 5:
                    return new HeaderSpan(
                            mProviderContext.getHeader5TextSize(),
                            mProviderContext.getHeader5Typeface());
                case 6:
                    return new HeaderSpan(
                            mProviderContext.getHeader6TextSize(),
                            mProviderContext.getHeader6Typeface());
            }
        }

        if (InlineCodeSpan.class.equals(spanClass)) {
            return new InlineCodeSpan(mProviderContext.getCodeBlockColor(), mProviderContext.getCodeTextSize());
        }

        if (CodeBlockSpan.class.equals(spanClass)) {
            return new CodeBlockSpan(
                    mProviderContext.getCodeBlockColor(),
                    mProviderContext.getCodeTextSize(),
                    mProviderContext.getCodeBlockPadding()
            );
        }

        if (OrderedListItemSpan.class.equals(spanClass)) {
            return new OrderedListItemSpan(
                    (OrderedListItemSpan.OrderedListItemData) parameter,
                    mProviderContext.getListItemLeading(),
                    mProviderContext.getListItemExtraHeight(),
                    mProviderContext.getListItemMarkerLeftMargin()
            );
        }

        if (UnorderedListItemSpan.class.equals(spanClass)) {
            return new UnorderedListItemSpan(
                    mProviderContext.getListItemLeading(),
                    mProviderContext.getListItemExtraHeight(),
                    mProviderContext.getListItemMarkerLeftMargin(),
                    mProviderContext.getListItemBulletRadius()
            );
        }

        if (QuoteSpan.class.equals(spanClass)) {
            return new QuoteSpan(
                    mProviderContext.getQuoteStripeColor(),
                    mProviderContext.getQuoteStripeWidth(),
                    mProviderContext.getQuotePadding()
            );
        }

        if (StrikethroughSpan.class.equals(spanClass)) {
            return new StrikethroughSpan();
        }

        throw new IllegalArgumentException("unknown spannable: " + spanClass.toString());
    }
}