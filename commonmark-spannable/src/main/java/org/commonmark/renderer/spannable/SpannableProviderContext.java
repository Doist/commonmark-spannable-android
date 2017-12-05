package org.commonmark.renderer.spannable;

import android.support.annotation.ColorInt;

public interface SpannableProviderContext {
    /**
     * @return list item marker left margin in px
     */
    int getListItemMarkerLeftMargin();

    /**
     * @return list item extra height in px
     */
    int getListItemExtraHeight();

    /**
     * @return list item line leading in px
     */
    int getListItemLeading();

    /**
     * @return list item bullet radius in px
     */
    int getListItemBulletRadius();

    /**
     * @return header text size in px
     */
    int getHeaderTextSize();

    /**
     * @return code text size in px
     */
    int getCodeTextSize();

    /**
     * @return code block padding in px
     */
    int getCodeBlockPadding();

    /**
     * @return quote padding in px
     */
    int getQuotePadding();

    /**
     * @return quote stripe width in px
     */
    int getQuoteStripeWidth();

    /**
     * @return background color for code
     */
    @ColorInt
    int getCodeBlockColor();

    /**
     * @return quote stripe color
     */
    @ColorInt
    int getQuoteStripeColor();
}