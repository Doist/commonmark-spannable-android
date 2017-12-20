package org.commonmark.renderer.spannable.text.style;

import android.text.style.LeadingMarginSpan;

public interface LeadingParagraphSpan extends LeadingMarginSpan {
    void increaseLeadingMargin(int leadingMargin);
}
