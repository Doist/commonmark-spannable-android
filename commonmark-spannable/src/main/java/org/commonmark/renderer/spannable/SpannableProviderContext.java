package org.commonmark.renderer.spannable;

import android.graphics.Typeface;

import androidx.annotation.ColorInt;

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
     * @return header1 text size in px
     */
    float getHeader1TextSize();

    /**
     * @return header1 typeface
     */
    Typeface getHeader1Typeface();

    /**
     * @return header2 text size in px
     */
    float getHeader2TextSize();

    /**
     * @return header2 typeface
     */
    Typeface getHeader2Typeface();

    /**
     * @return header3 text size in px
     */
    float getHeader3TextSize();

    /**
     * @return header3 typeface
     */
    Typeface getHeader3Typeface();

    /**
     * @return header4 text size in px
     */
    float getHeader4TextSize();

    /**
     * @return header4 typeface
     */
    Typeface getHeader4Typeface();

    /**
     * @return header5 text size in px
     */
    float getHeader5TextSize();

    /**
     * @return header5 typeface
     */
    Typeface getHeader5Typeface();

    /**
     * @return header6 text size in px
     */
    float getHeader6TextSize();

    /**
     * @return header6 typeface
     */
    Typeface getHeader6Typeface();

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