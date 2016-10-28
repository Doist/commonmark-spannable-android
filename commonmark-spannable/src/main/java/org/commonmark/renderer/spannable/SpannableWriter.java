package org.commonmark.renderer.spannable;

import org.commonmark.renderer.spannable.text.style.CountedSpan;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.ArrayList;

public class SpannableWriter {
    private final ArrayList<Object> mSpans = new ArrayList<>();

    private final SpannableStringBuilder mBuffer;

    private int mCount;

    private char mLastChar = 0;

    public SpannableWriter(SpannableStringBuilder out) {
        mBuffer = out;
    }

    public void paragraph(Object span) {
        if (mLastChar != 0) {
            if (mLastChar != '\n') {
                mBuffer.append('\n');
            }

            mBuffer.append(getParagraphSeparator(span));

            mLastChar = 0;
        }
    }

    public void line() {
        if (mLastChar != 0 && mLastChar != '\n') {
            mLastChar = '\n';
            mBuffer.append(mLastChar);
        }
    }

    public void resetCount() {
        resetCountTo(0);
    }

    public void resetCountTo(int startCount) {
        mCount = startCount;
    }

    public void addSpan(Object span) {
        mSpans.add(span);
    }

    public void write(String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        int length = text.length();
        for (Object span : mSpans) {
            if (span instanceof CountedSpan) {
                ((CountedSpan) span).setCount(mCount);
                mCount++;
            }
            ssb.setSpan(span, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mSpans.clear();

        if (length != 0) {
            mLastChar = text.charAt(length - 1);
        }

        mBuffer.append(ssb);
    }

    private CharSequence getParagraphSeparator(Object span) {
        SpannableStringBuilder separator = new SpannableStringBuilder("\n");
        separator.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return separator;
    }
}
