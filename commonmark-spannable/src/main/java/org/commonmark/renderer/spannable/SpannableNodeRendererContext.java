package org.commonmark.renderer.spannable;

import org.commonmark.node.Node;

public interface SpannableNodeRendererContext {
    /**
     * @return true if ordered list should start numeration provided by user
     */
    boolean shouldKeepOrder();

    /**
     * @return paragraph padding in px
     */
    int getParagraphPadding();

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
