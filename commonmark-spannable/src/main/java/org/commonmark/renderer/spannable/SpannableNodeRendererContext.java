package org.commonmark.renderer.spannable;

import org.commonmark.node.Node;

import android.support.annotation.ColorInt;

public interface SpannableNodeRendererContext {
    /**
     * @return true if ordered list should start numeration provided by user
     */
    boolean shouldKeepOrder();

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
     * @return paragraph padding in px
     */
    int getParagraphPadding();

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

    /**
     * @return the writer to use
     */
    SpannableWriter getWriter();

    /**
     * Render the specified node and its children using the configured renderers. This should be used to render child
     * nodes. Be careful not to pass the node that is being rendered, that would result in an endless loop.
     *
     * @param node the node to render
     */
    void render(Node node);
}
