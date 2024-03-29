package org.commonmark.renderer.spannable;

import org.commonmark.Extension;
import org.commonmark.internal.renderer.NodeRendererMap;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.spannable.internal.SpannableProviderMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;

/**
 * Renders a tree of nodes to SpannableString.
 */
public class SpannableRenderer implements Renderer {
    private final List<SpannableNodeRendererFactory> mNodeRendererFactories;
    private final List<SpannableProviderFactory> mProviderFactories;

    private final boolean mKeepOrder;
    private final int mListItemMarkerLeftMargin;
    private final int mListItemExtraHeight;
    private final int mListItemLeading;
    private final int mListItemBulletRadius;
    private final float mHeader1TextSize;
    private final Typeface mHeader1Typeface;
    private final float mHeader2TextSize;
    private final Typeface mHeader2Typeface;
    private final float mHeader3TextSize;
    private final Typeface mHeader3Typeface;
    private final float mHeader4TextSize;
    private final Typeface mHeader4Typeface;
    private final float mHeader5TextSize;
    private final Typeface mHeader5Typeface;
    private final float mHeader6TextSize;
    private final Typeface mHeader6Typeface;
    private final int mCodeTextSize;
    private final int mCodeBlockPadding;
    private final int mParagraphPadding;
    private final int mQuotePadding;
    private final int mQuoteStripeWidth;
    private final int mCodeBlockColor;
    private final int mQuoteStripeColor;

    private SpannableRenderer(Builder builder) {
        Resources resources = builder.mResources;

        mKeepOrder = builder.mKeepOrder;

        mListItemMarkerLeftMargin = getDimen(resources, builder.mListItemMarkerLeftMargin,
                                             builder.mListItemMarkerLeftMarginResId,
                                             R.dimen.commonmark_list_item_marker_left_margin);

        mListItemExtraHeight = getDimen(resources, builder.mListItemExtraHeight, builder.mListItemExtraHeightResId,
                                        R.dimen.commonmark_list_item_extra_height);

        mListItemLeading = getDimen(resources, builder.mListItemLeading, builder.mListItemLeadingResId,
                                    R.dimen.commonmark_list_item_leading);

        mListItemBulletRadius = getDimen(resources, builder.mListItemBulletRadius, builder.mListItemBulletRadiusResId,
                                         R.dimen.commonmark_list_item_bullet_radius);

        mHeader1TextSize = getDimenFloat(
                resources,
                builder.mHeader1TextSize,
                builder.mHeader1TextSizeResId,
                R.dimen.commonmark_header_text_size);

        mHeader1Typeface = getTypeface(builder.mHeader1Typeface, Typeface.DEFAULT_BOLD);

        mHeader2TextSize = getDimenFloat(
                resources,
                builder.mHeader2TextSize,
                builder.mHeader2TextSizeResId,
                R.dimen.commonmark_header_text_size);

        mHeader2Typeface = getTypeface(builder.mHeader2Typeface, Typeface.DEFAULT_BOLD);

        mHeader3TextSize = getDimenFloat(
                resources,
                builder.mHeader3TextSize,
                builder.mHeader3TextSizeResId,
                R.dimen.commonmark_header_text_size);

        mHeader3Typeface = getTypeface(builder.mHeader3Typeface, Typeface.DEFAULT_BOLD);

        mHeader4TextSize = getDimenFloat(
                resources,
                builder.mHeader4TextSize,
                builder.mHeader4TextSizeResId,
                R.dimen.commonmark_header_text_size);

        mHeader4Typeface = getTypeface(builder.mHeader4Typeface, Typeface.DEFAULT_BOLD);

        mHeader5TextSize = getDimenFloat(
                resources,
                builder.mHeader5TextSize,
                builder.mHeader5TextSizeResId,
                R.dimen.commonmark_header_text_size);

        mHeader5Typeface = getTypeface(builder.mHeader5Typeface, Typeface.DEFAULT_BOLD);

        mHeader6TextSize = getDimenFloat(
                resources,
                builder.mHeader6TextSize,
                builder.mHeader6TextSizeResId,
                R.dimen.commonmark_header_text_size);

        mHeader6Typeface = getTypeface(builder.mHeader6Typeface, Typeface.DEFAULT_BOLD);

        mCodeTextSize = getDimen(resources, builder.mCodeTextSize, builder.mCodeTextSizeResId,
                                 R.dimen.commonmark_code_text_size);

        mCodeBlockPadding = getDimen(resources, builder.mCodeBlockPadding, builder.mCodeBlockPaddingResId,
                                     R.dimen.commonmark_code_block_padding);

        mParagraphPadding = getDimen(resources, builder.mParagraphPadding, builder.mParagraphPaddingResId,
                                     R.dimen.commonmark_paragraph_padding);

        mQuotePadding = getDimen(resources, builder.mQuotePadding, builder.mQuotePaddingResId,
                                 R.dimen.commonmark_quote_padding);

        mQuoteStripeWidth = getDimen(resources, builder.mQuoteStripeWidth, builder.mQuoteStripeWidthResId,
                                     R.dimen.commonmark_quote_stripe_width);

        mCodeBlockColor = getColor(resources, builder.mCodeBlockColor, builder.mCodeBlockColorResId,
                                   R.color.commonmark_code_block_color);

        mQuoteStripeColor = getColor(resources, builder.mQuoteStripeColor, builder.mQuoteStripeColorResId,
                                     R.color.commonmark_quote_stripe_color);

        mNodeRendererFactories = createFactoryList(builder.mNodeRendererFactories, new SpannableNodeRendererFactory() {
            @Override
            public NodeRenderer create(SpannableNodeRendererContext context) {
                return new CoreSpannableNodeRenderer(context);
            }
        });

        mProviderFactories = createFactoryList(builder.mProviderFactories, new SpannableProviderFactory() {
            @Override
            public SpannableProvider create(SpannableProviderContext context) {
                return new CoreSpannableProvider(context);
            }
        });
    }

    @Deprecated
    public static Builder builder(Context context) {
        return builder(context.getResources());
    }

    /**
     * Create a new builder for configuring an {@link SpannableRenderer}.
     *
     * @return a builder
     */
    @SuppressWarnings("WeakerAccess")
    public static Builder builder(Resources resources) {
        return new Builder(resources);
    }

    @Override
    public void render(Node node, Appendable output) {
        if (!(output instanceof SpannableStringBuilder)) {
            throw new IllegalArgumentException("output must be instance of SpannableStringBuilder");
        }

        RendererContext context = new RendererContext(new SpannableWriter((SpannableStringBuilder) output));
        context.render(node);
    }

    @Override
    public String render(Node node) {
        throw new IllegalStateException("don't use this method");
    }

    private int getDimen(Resources res, Integer value, Integer resId, int defaultResId) {
        if (value != null) {
            return value;
        } else if (resId != null) {
            return res.getDimensionPixelSize(resId);
        } else {
            return res.getDimensionPixelSize(defaultResId);
        }
    }

    private float getDimenFloat(Resources res, Float value, Integer resId, int defaultResId) {
        if (value != null) {
            return value;
        } else if (resId != null) {
            return res.getDimensionPixelSize(resId);
        } else {
            return res.getDimensionPixelSize(defaultResId);
        }
    }

    private Typeface getTypeface(Typeface value, Typeface defaultTypeface) {
        if (value != null) {
            return value;
        }  else {
            return defaultTypeface;
        }
    }

    private int getColor(Resources res, Integer value, Integer resId, int defaultResId) {
        if (value != null) {
            return value;
        } else if (resId != null) {
            return getColor(res, resId);
        } else {
            return getColor(res, defaultResId);
        }
    }

    @SuppressWarnings("deprecation")
    private int getColor(Resources res, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return res.getColor(resId, null);
        } else {
            return res.getColor(resId);
        }
    }

    private <T> List<T> createFactoryList(List<T> custom, T coreFactory) {
        List<T> result = new ArrayList<>(custom.size() + 1);
        result.addAll(custom);
        // Add as last. This means clients can override the creating of core span if they want.
        result.add(coreFactory);
        return result;
    }

    /**
     * Builder for configuring an {@link SpannableRenderer}.
     */
    public static final class Builder {
        private final List<SpannableNodeRendererFactory> mNodeRendererFactories = new ArrayList<>();
        private final List<SpannableProviderFactory> mProviderFactories = new ArrayList<>();

        private final Resources mResources;

        private boolean mKeepOrder = true;

        private Integer mListItemMarkerLeftMargin;
        private Integer mListItemMarkerLeftMarginResId;
        private Integer mListItemExtraHeight;
        private Integer mListItemExtraHeightResId;
        private Integer mListItemLeading;
        private Integer mListItemLeadingResId;
        private Integer mListItemBulletRadius;
        private Integer mListItemBulletRadiusResId;
        private Float mHeader1TextSize;
        private Integer mHeader1TextSizeResId;
        private Typeface mHeader1Typeface;
        private Float mHeader2TextSize;
        private Integer mHeader2TextSizeResId;
        private Typeface mHeader2Typeface;
        private Float mHeader3TextSize;
        private Integer mHeader3TextSizeResId;
        private Typeface mHeader3Typeface;
        private Float mHeader4TextSize;
        private Integer mHeader4TextSizeResId;
        private Typeface mHeader4Typeface;
        private Float mHeader5TextSize;
        private Integer mHeader5TextSizeResId;
        private Typeface mHeader5Typeface;
        private Float mHeader6TextSize;
        private Integer mHeader6TextSizeResId;
        private Typeface mHeader6Typeface;
        private Integer mCodeTextSize;
        private Integer mCodeTextSizeResId;
        private Integer mCodeBlockPadding;
        private Integer mCodeBlockPaddingResId;
        private Integer mParagraphPadding;
        private Integer mParagraphPaddingResId;
        private Integer mQuotePadding;
        private Integer mQuotePaddingResId;
        private Integer mQuoteStripeWidth;
        private Integer mQuoteStripeWidthResId;
        private Integer mCodeBlockColor;
        private Integer mCodeBlockColorResId;
        private Integer mQuoteStripeColor;
        private Integer mQuoteStripeColorResId;

        private Builder(Resources resources) {
            mResources = resources;
        }

        /**
         * @return the configured {@link SpannableRenderer}
         */
        public SpannableRenderer build() {
            return new SpannableRenderer(this);
        }

        /**
         * This param defines how the ordered list would be numerated.
         *
         * @param keepOrder true - the numeration will be started from the first number provided by user
         *                  false - the numeration will be started from the 1
         * @return {@code this}
         */
        public Builder keepOrder(boolean keepOrder) {
            mKeepOrder = keepOrder;
            return this;
        }

        /**
         * This param defines the left margin of list item markers.
         *
         * @param margin list item marker left margin in px
         * @return {@code this}
         */
        public Builder listItemMarkerLeftMargin(int margin) {
            mListItemMarkerLeftMargin = margin;
            return this;
        }

        /**
         * This param defines the left margin of list item markers.
         *
         * @param resId list item marker left margin resource id
         * @return {@code this}
         */
        public Builder listItemMarkerLeftMarginResId(@DimenRes int resId) {
            mListItemMarkerLeftMarginResId = resId;
            return this;
        }

        /**
         * This param defines extra height for list item.
         *
         * @param extraHeight list item extra height in px
         * @return {@code this}
         */
        public Builder listItemExtraHeight(int extraHeight) {
            mListItemExtraHeight = extraHeight;
            return this;
        }

        /**
         * This param defines extra height for list item.
         *
         * @param resId list item extra height resources id
         * @return {@code this}
         */
        public Builder listItemExtraHeightResId(@DimenRes int resId) {
            mListItemExtraHeightResId = resId;
            return this;
        }

        /**
         * This param defines line leading for list item.
         *
         * @param leading list item line leading in px
         * @return {@code this}
         */
        public Builder listItemLeading(int leading) {
            mListItemLeading = leading;
            return this;
        }

        /**
         * This param defines line leading for list item.
         *
         * @param resId list item line leading resources id
         * @return {@code this}
         */
        public Builder listItemLeadingResId(@DimenRes int resId) {
            mListItemLeadingResId = resId;
            return this;
        }

        /**
         * This param defines bullet radius for list item.
         *
         * @param radius list item bullet radius in px
         * @return {@code this}
         */
        public Builder listItemBulletRadius(int radius) {
            mListItemBulletRadius = radius;
            return this;
        }

        /**
         * This param defines bullet radius for list item.
         *
         * @param resId list item bullet radius resources id
         * @return {@code this}
         */
        public Builder listItemBulletRadiusResId(@DimenRes int resId) {
            mListItemBulletRadiusResId = resId;
            return this;
        }

        /**
         * This param defines text size for header1.
         *
         * @param textSize header text size in px
         * @return {@code this}
         */
        public Builder header1TextSize(float textSize) {
            mHeader1TextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for header1.
         *
         * @param resId header text size resources id
         * @return {@code this}
         */
        public Builder header1TextSizeResId(@DimenRes int resId) {
            mHeader1TextSizeResId = resId;
            return this;
        }

        /**
         * This param defines typeface for header1.
         *
         * @param typeface header typeface
         * @return {@code this}
         */
        public Builder header1Typeface(Typeface typeface) {
            mHeader1Typeface = typeface;
            return this;
        }

        /**
         * This param defines text size for header2.
         *
         * @param textSize header text size in px
         * @return {@code this}
         */
        public Builder header2TextSize(float textSize) {
            mHeader2TextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for header2.
         *
         * @param resId header text size resources id
         * @return {@code this}
         */
        public Builder header2TextSizeResId(@DimenRes int resId) {
            mHeader2TextSizeResId = resId;
            return this;
        }

        /**
         * This param defines typeface for header2.
         *
         * @param typeface header typeface
         * @return {@code this}
         */
        public Builder header2Typeface(Typeface typeface) {
            mHeader2Typeface = typeface;
            return this;
        }

        /**
         * This param defines text size for header3.
         *
         * @param textSize header text size in px
         * @return {@code this}
         */
        public Builder header3TextSize(float textSize) {
            mHeader3TextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for header3.
         *
         * @param resId header text size resources id
         * @return {@code this}
         */
        public Builder header3TextSizeResId(@DimenRes int resId) {
            mHeader3TextSizeResId = resId;
            return this;
        }

        /**
         * This param defines typeface for header3.
         *
         * @param typeface header typeface
         * @return {@code this}
         */
        public Builder header3Typeface(Typeface typeface) {
            mHeader3Typeface = typeface;
            return this;
        }

        /**
         * This param defines text size for header4.
         *
         * @param textSize header text size in px
         * @return {@code this}
         */
        public Builder header4TextSize(float textSize) {
            mHeader4TextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for header4.
         *
         * @param resId header text size resources id
         * @return {@code this}
         */
        public Builder header4TextSizeResId(@DimenRes int resId) {
            mHeader4TextSizeResId = resId;
            return this;
        }

        /**
         * This param defines typeface for header4.
         *
         * @param typeface header typeface
         * @return {@code this}
         */
        public Builder header4Typeface(Typeface typeface) {
            mHeader4Typeface = typeface;
            return this;
        }

        /**
         * This param defines text size for header5.
         *
         * @param textSize header text size in px
         * @return {@code this}
         */
        public Builder header5TextSize(float textSize) {
            mHeader5TextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for header5.
         *
         * @param resId header text size resources id
         * @return {@code this}
         */
        public Builder header5TextSizeResId(@DimenRes int resId) {
            mHeader5TextSizeResId = resId;
            return this;
        }

        /**
         * This param defines typeface for header5.
         *
         * @param typeface header typeface
         * @return {@code this}
         */
        public Builder header5Typeface(Typeface typeface) {
            mHeader5Typeface = typeface;
            return this;
        }

        /**
         * This param defines text size for header6.
         *
         * @param textSize header text size in px
         * @return {@code this}
         */
        public Builder header6TextSize(float textSize) {
            mHeader6TextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for header6.
         *
         * @param resId header text size resources id
         * @return {@code this}
         */
        public Builder header6TextSizeResId(@DimenRes int resId) {
            mHeader6TextSizeResId = resId;
            return this;
        }

        /**
         * This param defines typeface for header6.
         *
         * @param typeface header typeface
         * @return {@code this}
         */
        public Builder header6Typeface(Typeface typeface) {
            mHeader6Typeface = typeface;
            return this;
        }

        /**
         * This param defines text size for inline code and code block.
         *
         * @param textSize code text size in px
         * @return {@code this}
         */
        public Builder codeTextSize(int textSize) {
            mCodeTextSize = textSize;
            return this;
        }

        /**
         * This param defines text size for inline code and code block.
         *
         * @param resId code text size resources id
         * @return {@code this}
         */
        public Builder codeTextSizeResId(@DimenRes int resId) {
            mCodeTextSizeResId = resId;
            return this;
        }

        /**
         * This param defines padding for code block.
         *
         * @param padding code block padding in px
         * @return {@code this}
         */
        public Builder codeBlockPadding(int padding) {
            mCodeBlockPadding = padding;
            return this;
        }

        /**
         * This param defines padding for code block.
         *
         * @param resId code block padding resources id
         * @return {@code this}
         */
        public Builder codeBlockPaddingResId(@DimenRes int resId) {
            mCodeBlockPaddingResId = resId;
            return this;
        }

        /**
         * This param defines padding for paragraph.
         *
         * @param padding paragraph padding in px
         * @return {@code this}
         */
        public Builder paragraphPadding(int padding) {
            mParagraphPadding = padding;
            return this;
        }

        /**
         * This param defines padding for paragraph.
         *
         * @param resId paragraph padding resources id
         * @return {@code this}
         */
        public Builder paragraphPaddingResId(@DimenRes int resId) {
            mParagraphPaddingResId = resId;
            return this;
        }

        /**
         * This param defines padding for quote.
         *
         * @param padding quote padding in px
         * @return {@code this}
         */
        public Builder quotePadding(int padding) {
            mQuotePadding = padding;
            return this;
        }

        /**
         * This param defines padding for quote.
         *
         * @param resId quote padding resources id
         * @return {@code this}
         */
        public Builder quotePaddingResId(@DimenRes int resId) {
            mQuotePaddingResId = resId;
            return this;
        }

        /**
         * This param defines stripe width for quote.
         *
         * @param width quote stripe width in px
         * @return {@code this}
         */
        public Builder quoteStripeWidth(int width) {
            mQuoteStripeWidth = width;
            return this;
        }

        /**
         * This param defines stripe width for quote.
         *
         * @param resId quote stripe width resources id
         * @return {@code this}
         */
        public Builder quoteStripeWidthResId(@DimenRes int resId) {
            mQuoteStripeWidthResId = resId;
            return this;
        }

        /**
         * This param defines background color for inline code and code block.
         *
         * @param color background color for code
         * @return {@code this}
         */
        public Builder codeBlockColor(@ColorInt int color) {
            mCodeBlockColor = color;
            return this;
        }

        /**
         * This param defines background color for inline code and code block.
         *
         * @param resId background color resources id for code
         * @return {@code this}
         */
        public Builder codeBlockColorResId(@ColorRes int resId) {
            mCodeBlockColorResId = resId;
            return this;
        }

        /**
         * This param defines stripe color for quote.
         *
         * @param color quote stripe color
         * @return {@code this}
         */
        public Builder quoteStripeColor(@ColorInt int color) {
            mQuoteStripeColor = color;
            return this;
        }

        /**
         * This param defines stripe color for quote.
         *
         * @param resId quote stripe color resources id
         * @return {@code this}
         */
        public Builder quoteStripeColorResId(@ColorRes int resId) {
            mQuoteStripeColorResId = resId;
            return this;
        }

        /**
         * Add a factory for instantiating a node renderer (done when rendering). This allows to override the rendering
         * of node types or define rendering for custom node types.
         * <p>
         * If multiple node renderers for the same node type are created, the one from the factory that was added first
         * "wins". (This is how the rendering for core node types can be overridden; the default rendering comes last.)
         *
         * @param nodeRendererFactory the factory for creating a node renderer
         * @return {@code this}
         */
        public Builder nodeRendererFactory(SpannableNodeRendererFactory nodeRendererFactory) {
            this.mNodeRendererFactories.add(nodeRendererFactory);
            return this;
        }

        /**
         * Add a factory for instantiating spannable provider. This allows to override the span or provide custom span.
         * <p>
         * If multiple spannable provider for the same span are created, the one from the factory that was added first
         * "wins".
         *
         * @param providerFactory the factory for creating a spannable provider
         * @return {@code this}
         */
        public Builder providerFactory(SpannableProviderFactory providerFactory) {
            mProviderFactories.add(providerFactory);
            return this;
        }

        /**
         * @param extensions extensions to use on this spannable renderer
         * @return {@code this}
         */
        public Builder extensions(Iterable<? extends Extension> extensions) {
            for (Extension extension : extensions) {
                if (extension instanceof SpannableRendererExtension) {
                    SpannableRendererExtension spannableExtension = (SpannableRendererExtension) extension;
                    spannableExtension.extend(this);
                }
            }
            return this;
        }
    }

    /**
     * Extension for {@link SpannableRenderer}.
     */
    public interface SpannableRendererExtension extends Extension {
        void extend(SpannableRenderer.Builder rendererBuilder);
    }

    private class RendererContext implements SpannableNodeRendererContext, SpannableProviderContext {
        private final SpannableWriter mSpannableWriter;
        private final NodeRendererMap mNodeRendererMap = new NodeRendererMap();

        private RendererContext(SpannableWriter spannableWriter) {
            SpannableProviderMap providerMap = new SpannableProviderMap();
            // The first spannable provider for a span "wins".
            for (int i = mProviderFactories.size() - 1; i >= 0; i--) {
                SpannableProviderFactory providerFactory = mProviderFactories.get(i);
                SpannableProvider provider = providerFactory.create(this);
                providerMap.add(provider);
            }
            spannableWriter.setProviderMap(providerMap);
            mSpannableWriter = spannableWriter;

            // The first node renderer for a node type "wins".
            for (int i = mNodeRendererFactories.size() - 1; i >= 0; i--) {
                SpannableNodeRendererFactory nodeRendererFactory = mNodeRendererFactories.get(i);
                NodeRenderer nodeRenderer = nodeRendererFactory.create(this);
                mNodeRendererMap.add(nodeRenderer);
            }
        }

        @Override
        public boolean shouldKeepOrder() {
            return mKeepOrder;
        }

        @Override
        public int getListItemMarkerLeftMargin() {
            return mListItemMarkerLeftMargin;
        }

        @Override
        public int getListItemExtraHeight() {
            return mListItemExtraHeight;
        }

        @Override
        public int getListItemLeading() {
            return mListItemLeading;
        }

        @Override
        public int getListItemBulletRadius() {
            return mListItemBulletRadius;
        }

        @Override
        public float getHeader1TextSize() {
            return mHeader1TextSize;
        }

        @Override
        public Typeface getHeader1Typeface() {
            return mHeader1Typeface;
        }

        @Override
        public float getHeader2TextSize() {
            return mHeader2TextSize;
        }

        @Override
        public Typeface getHeader2Typeface() {
            return mHeader2Typeface;
        }

        @Override
        public float getHeader3TextSize() {
            return mHeader3TextSize;
        }

        @Override
        public Typeface getHeader3Typeface() {
            return mHeader3Typeface;
        }

        @Override
        public float getHeader4TextSize() {
            return mHeader4TextSize;
        }

        @Override
        public Typeface getHeader4Typeface() {
            return mHeader4Typeface;
        }

        @Override
        public float getHeader5TextSize() {
            return mHeader5TextSize;
        }

        @Override
        public Typeface getHeader5Typeface() {
            return mHeader5Typeface;
        }

        @Override
        public float getHeader6TextSize() {
            return mHeader6TextSize;
        }

        @Override
        public Typeface getHeader6Typeface() {
            return mHeader6Typeface;
        }

        @Override
        public int getCodeTextSize() {
            return mCodeTextSize;
        }

        @Override
        public int getCodeBlockPadding() {
            return mCodeBlockPadding;
        }

        @Override
        public int getParagraphPadding() {
            return mParagraphPadding;
        }

        @Override
        public int getQuotePadding() {
            return mQuotePadding;
        }

        @Override
        public int getQuoteStripeWidth() {
            return mQuoteStripeWidth;
        }

        @Override
        public int getCodeBlockColor() {
            return mCodeBlockColor;
        }

        @Override
        public int getQuoteStripeColor() {
            return mQuoteStripeColor;
        }

        @Override
        public SpannableWriter getWriter() {
            return mSpannableWriter;
        }

        @Override
        public void render(Node node) {
            mNodeRendererMap.render(node);
        }
    }
}
