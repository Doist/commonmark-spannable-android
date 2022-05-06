package org.commonmark.renderer.spannable;

import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Link;
import org.commonmark.node.ListBlock;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.node.ThematicBreak;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.spannable.internal.BulletListHolder;
import org.commonmark.renderer.spannable.internal.ListHolder;
import org.commonmark.renderer.spannable.internal.OrderedListHolder;
import org.commonmark.renderer.spannable.text.style.BoldSpan;
import org.commonmark.renderer.spannable.text.style.CodeBlockSpan;
import org.commonmark.renderer.spannable.text.style.HeaderSpan;
import org.commonmark.renderer.spannable.text.style.InlineCodeSpan;
import org.commonmark.renderer.spannable.text.style.ItalicSpan;
import org.commonmark.renderer.spannable.text.style.LineSeparatorSpan;
import org.commonmark.renderer.spannable.text.style.LinkSpan;
import org.commonmark.renderer.spannable.text.style.QuoteSpan;

import android.text.TextUtils;
import android.text.style.StrikethroughSpan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The node renderer that renders all the core nodes (comes last in the order of node renderers).
 */
public class CoreSpannableNodeRenderer extends AbstractVisitor implements NodeRenderer {
    private final SpannableNodeRendererContext mRendererContext;
    private final SpannableWriter mSpannableWriter;

    private ListHolder listHolder;

    CoreSpannableNodeRenderer(SpannableNodeRendererContext rendererContext) {
        mRendererContext = rendererContext;
        mSpannableWriter = mRendererContext.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return new HashSet<>(Arrays.asList(
                Document.class,
                Heading.class,
                Paragraph.class,
                BlockQuote.class,
                BulletList.class,
                FencedCodeBlock.class,
                HtmlBlock.class,
                ThematicBreak.class,
                IndentedCodeBlock.class,
                Link.class,
                ListItem.class,
                OrderedList.class,
                Image.class,
                Emphasis.class,
                StrongEmphasis.class,
                Text.class,
                Code.class,
                HtmlInline.class,
                SoftLineBreak.class,
                HardLineBreak.class,
                Strikethrough.class)
        );
    }

    @Override
    public void render(Node node) {
        node.accept(this);
    }

    @Override
    public void visit(BlockQuote blockQuote) {
        mSpannableWriter.start(QuoteSpan.class);
        visitChildren(blockQuote);
        mSpannableWriter.end(QuoteSpan.class);

        addParagraphIfNeeded(blockQuote);
    }

    @Override
    public void visit(BulletList bulletList) {
        if (listHolder != null) {
            mSpannableWriter.line();
        }
        listHolder = new BulletListHolder(listHolder);
        visitChildren(bulletList);
        addParagraphIfNeeded(bulletList);
        if (listHolder.getParent() != null) {
            listHolder = listHolder.getParent();
        } else {
            listHolder = null;
        }
    }

    @Override
    public void visit(Code code) {
        mSpannableWriter.start(InlineCodeSpan.class);
        mSpannableWriter.write(code.getLiteral());
        mSpannableWriter.end(InlineCodeSpan.class);
    }

    @Override
    public void visit(Emphasis emphasis) {
        mSpannableWriter.start(ItalicSpan.class);
        visitChildren(emphasis);
        mSpannableWriter.end(ItalicSpan.class);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        if (!TextUtils.isEmpty(fencedCodeBlock.getInfo())) {
            mSpannableWriter.start(ItalicSpan.class);
            mSpannableWriter.start(BoldSpan.class);
            mSpannableWriter.write(fencedCodeBlock.getInfo());
            mSpannableWriter.end(BoldSpan.class);
            mSpannableWriter.end(ItalicSpan.class);
            mSpannableWriter.line();
        }

        mSpannableWriter.start(CodeBlockSpan.class);
        mSpannableWriter.write(fencedCodeBlock.getLiteral());
        mSpannableWriter.end(CodeBlockSpan.class);

        addParagraphIfNeeded(fencedCodeBlock);
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        mSpannableWriter.line();
        visitChildren(hardLineBreak);
    }

    @Override
    public void visit(CustomNode customNode) {
        if (customNode instanceof Strikethrough) {
            mSpannableWriter.start(StrikethroughSpan.class);
            visitChildren(customNode);
            mSpannableWriter.end(StrikethroughSpan.class);
        } else {
            visitChildren(customNode);
        }
    }

    @Override
    public void visit(Heading header) {
        mSpannableWriter.start(HeaderSpan.class);
        visitChildren(header);
        mSpannableWriter.end(HeaderSpan.class);

        addParagraphIfNeeded(header);
    }

    @Override
    public void visit(ThematicBreak horizontalRule) {
        visitChildren(horizontalRule);
        mSpannableWriter.line();
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        mSpannableWriter.start(CodeBlockSpan.class);
        mSpannableWriter.write(indentedCodeBlock.getLiteral());
        mSpannableWriter.end(CodeBlockSpan.class);

        addParagraphIfNeeded(indentedCodeBlock);
    }

    @Override
    public void visit(Link link) {
        mSpannableWriter.start(LinkSpan.class, link.getDestination());
        visitChildren(link);
        mSpannableWriter.end(LinkSpan.class);
    }

    @Override
    public void visit(ListItem listItem) {
        if (listHolder == null) {
            return;
        }

        mSpannableWriter.start(listHolder.getSpanClass());
        visitChildren(listItem);
        mSpannableWriter.end(listHolder.getSpanClass(), listHolder.getSpanParameter());

        if (listItem.getNext() != null) {
            mSpannableWriter.line();
        }

        if (listHolder instanceof OrderedListHolder) {
            ((OrderedListHolder) listHolder).increaseCounter();
        }
    }

    @Override
    public void visit(OrderedList orderedList) {
        if (listHolder != null) {
            mSpannableWriter.line();
        }
        listHolder = new OrderedListHolder(listHolder, orderedList, mRendererContext.shouldKeepOrder());
        visitChildren(orderedList);
        addParagraphIfNeeded(orderedList);
        if (listHolder.getParent() != null) {
            listHolder = listHolder.getParent();
        } else {
            listHolder = null;
        }
    }

    @Override
    public void visit(Paragraph paragraph) {
        visitChildren(paragraph);
        if (!isInTightList(paragraph)) {
            addParagraphIfNeeded(paragraph);
        }
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
        mSpannableWriter.line();
        visitChildren(softLineBreak);
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        mSpannableWriter.start(BoldSpan.class);
        visitChildren(strongEmphasis);
        mSpannableWriter.end(BoldSpan.class);
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        mSpannableWriter.write(htmlBlock.getLiteral());
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        mSpannableWriter.write(htmlInline.getLiteral());
    }

    @Override
    public void visit(Text text) {
        mSpannableWriter.write(text.getLiteral());
    }

    @Override
    protected void visitChildren(Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            mRendererContext.render(node);
            node = next;
        }
    }

    private void addParagraphIfNeeded(Node node) {
        if (node.getNext() != null) {
            mSpannableWriter.paragraph(new LineSeparatorSpan(mRendererContext.getParagraphPadding()));
        }
    }

    private boolean isInTightList(Paragraph paragraph) {
        Node parent = paragraph.getParent();
        if (parent != null) {
            Node gramps = parent.getParent();
            if (gramps != null && gramps instanceof ListBlock) {
                ListBlock list = (ListBlock) gramps;
                return list.isTight();
            }
        }
        return false;
    }
}
