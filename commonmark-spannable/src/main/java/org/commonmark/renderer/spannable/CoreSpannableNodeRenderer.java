package org.commonmark.renderer.spannable;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
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
import org.commonmark.renderer.spannable.text.style.CodeBlockSpan;
import org.commonmark.renderer.spannable.text.style.HeaderSpan;
import org.commonmark.renderer.spannable.text.style.InlineCodeSpan;
import org.commonmark.renderer.spannable.text.style.LineSeparatorSpan;
import org.commonmark.renderer.spannable.text.style.LinkSpan;
import org.commonmark.renderer.spannable.text.style.OrderedListItemSpan;
import org.commonmark.renderer.spannable.text.style.QuoteSpan;
import org.commonmark.renderer.spannable.text.style.UnorderedListItemSpan;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The node renderer that renders all the core nodes (comes last in the order of node renderers).
 */
public class CoreSpannableNodeRenderer extends AbstractVisitor implements NodeRenderer {
    private final SpannableNodeRendererContext mRendererContext;
    private final SpannableWriter mSpannableWriter;

    private boolean mOrderedList;
    private boolean mUnorderedList;

    public CoreSpannableNodeRenderer(SpannableNodeRendererContext rendererContext) {
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
                HardLineBreak.class)
        );
    }

    @Override
    public void render(Node node) {
        node.accept(this);
    }

    @Override
    public void visit(BlockQuote blockQuote) {
        mSpannableWriter.addSpan(new QuoteSpan(mRendererContext.getQuoteStripeColor(),
                                               mRendererContext.getQuoteStripeWidth(),
                                               mRendererContext.getQuotePadding()));
        visitChildren(blockQuote);
        addParagraphIfNeeded(blockQuote);
    }

    @Override
    public void visit(BulletList bulletList) {
        mUnorderedList = true;
        if (mOrderedList) {
            mOrderedList = false;
        }

        visitChildren(bulletList);
        addParagraphIfNeeded(bulletList);
    }

    @Override
    public void visit(Code code) {
        mSpannableWriter.addSpan(new InlineCodeSpan(mRendererContext.getCodeBlockColor(),
                                                    mRendererContext.getCodeTextSize()));
        mSpannableWriter.write(code.getLiteral());
    }

    @Override
    public void visit(Emphasis emphasis) {
        mSpannableWriter.addSpans(getInheritedSpans(emphasis));
        mSpannableWriter.addSpan(new StyleSpan(Typeface.ITALIC));
        visitChildren(emphasis);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        if (!TextUtils.isEmpty(fencedCodeBlock.getInfo())) {
            mSpannableWriter.addSpan(new StyleSpan(Typeface.ITALIC));
            mSpannableWriter.addSpan(new StyleSpan(Typeface.BOLD));
            mSpannableWriter.write(fencedCodeBlock.getInfo());
            mSpannableWriter.line();
        }
        mSpannableWriter.addSpan(createCodeBlockSpan());
        mSpannableWriter.write(fencedCodeBlock.getLiteral());
        addParagraphIfNeeded(fencedCodeBlock);
    }

    private CodeBlockSpan createCodeBlockSpan() {
        return new CodeBlockSpan(mRendererContext.getCodeBlockColor(),
                                 mRendererContext.getCodeTextSize(),
                                 mRendererContext.getCodeBlockPadding());
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        mSpannableWriter.line();
        visitChildren(hardLineBreak);
    }

    @Override
    public void visit(Heading header) {
        mSpannableWriter.addSpan(new HeaderSpan(mRendererContext.getHeaderTextSize()));
        visitChildren(header);
        addParagraphIfNeeded(header);
    }

    @Override
    public void visit(ThematicBreak horizontalRule) {
        visitChildren(horizontalRule);
        mSpannableWriter.line();
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        visitChildren(htmlBlock);
        mSpannableWriter.line();
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        mSpannableWriter.addSpan(createCodeBlockSpan());
        mSpannableWriter.write(indentedCodeBlock.getLiteral());
        addParagraphIfNeeded(indentedCodeBlock);
    }

    @Override
    public void visit(Link link) {
        mSpannableWriter.addSpans(getInheritedSpans(link));
        mSpannableWriter.addSpan(new LinkSpan(link.getDestination()));
        visitChildren(link);
    }

    @Override
    public void visit(ListItem listItem) {
        if (mOrderedList) {
            mSpannableWriter.addSpan(new OrderedListItemSpan(mRendererContext.getListItemLeading(),
                                                             mRendererContext.getListItemExtraHeight(),
                                                             mRendererContext.getListItemMarkerLeftMargin()));
        } else if (mUnorderedList) {
            mSpannableWriter.addSpan(new UnorderedListItemSpan(mRendererContext.getListItemLeading(),
                                                               mRendererContext.getListItemExtraHeight(),
                                                               mRendererContext.getListItemMarkerLeftMargin(),
                                                               mRendererContext.getListItemBulletRadius()));
        }

        visitChildren(listItem);
        if (listItem.getNext() != null) {
            mSpannableWriter.line();
        }
    }

    @Override
    public void visit(OrderedList orderedList) {
        mOrderedList = true;
        if (mUnorderedList) {
            mUnorderedList = false;
        }
        if (mRendererContext.shouldKeepOrder()) {
            mSpannableWriter.resetCountTo(orderedList.getStartNumber());
        } else {
            mSpannableWriter.resetCount();
        }

        visitChildren(orderedList);
        addParagraphIfNeeded(orderedList);
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
        mSpannableWriter.addSpans(getInheritedSpans(strongEmphasis));
        mSpannableWriter.addSpan(new StyleSpan(Typeface.BOLD));
        visitChildren(strongEmphasis);
    }

    @Override
    public void visit(Text text) {
        mSpannableWriter.addSpans(getInheritedSpans(text));
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

    private ArrayList<Object> getInheritedSpans(@NonNull Node node) {
        Node parent = node.getParent();
        ArrayList<Object> spans = new ArrayList<>();
        while (parent != null && getInheritedSpanFor(parent) != null) {
            spans.add(getInheritedSpanFor(parent));
            parent = parent.getParent();
        }
        return spans;
    }

    private Object getInheritedSpanFor(Node node) {
        if (node instanceof StrongEmphasis) {
            return new StyleSpan(Typeface.BOLD);
        } else if (node instanceof Emphasis || node instanceof BlockQuote) {
            return new StyleSpan(Typeface.ITALIC);
        } else if (node instanceof Link) {
            return new LinkSpan(((Link) node).getDestination());
        }

        return null;
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
