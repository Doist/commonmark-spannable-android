package org.commonmark.renderer.spannable;

import org.commonmark.renderer.spannable.internal.SpannableProviderMap;
import org.commonmark.renderer.spannable.text.style.LeadingParagraphSpan;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import androidx.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public class SpannableWriter {
    private final Map<Class<?>, Object> mSpanTypes = new HashMap<>();

    private final SpannableStringBuilder mBuffer;

    private SpannableProviderMap mProviderMap;

    private char mLastChar = 0;

    private final Deque<Integer> mParagraphStartQueue = new LinkedList<>();

    public SpannableWriter(SpannableStringBuilder out) {
        mBuffer = out;
    }

    public void setProviderMap(SpannableProviderMap providerMap) {
        mProviderMap = providerMap;
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
        line(false);
    }

    public void line(boolean force) {
        if (force || mLastChar != 0 && mLastChar != '\n') {
            mLastChar = '\n';
            mBuffer.append(mLastChar);
        }
    }

    public void start(Class<?> spanClass) {
        start(spanClass, null);
    }

    public void start(Class<?> spanClass, @Nullable Object parameter) {
        if (isParagraphSpan(spanClass)) {
            mParagraphStartQueue.push(mBuffer.length());
            return;
        }

        mSpanTypes.put(spanClass, parameter);
    }

    public void end(Class<?> spanClass) {
        end(spanClass, null);
    }

    public void end(Class<?> spanClass, @Nullable Object parameter) {
        if (isParagraphSpan(spanClass)) {
            int start = mParagraphStartQueue.pop();
            int end = mBuffer.length();

            if (start == end) {
                // Force new line.
                line(true);
                end++;
            }

            // Create span.
            LeadingParagraphSpan span = (LeadingParagraphSpan) mProviderMap.get(spanClass).create(spanClass, parameter);
            // Update leading margin for all "sub items".
            LeadingParagraphSpan[] spans = mBuffer.getSpans(start, end, LeadingParagraphSpan.class);
            for (LeadingParagraphSpan leadingParagraphSpan : spans) {
                leadingParagraphSpan.increaseLeadingMargin(span.getLeadingMargin(false));
            }
            // Set span.
            mBuffer.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return;
        }

        mSpanTypes.remove(spanClass);
    }

    public void write(String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        int length = text.length();
        for (Map.Entry<Class<?>, Object> entry : mSpanTypes.entrySet()) {
            Class<?> spanClass = entry.getKey();
            Object span = mProviderMap.get(spanClass).create(spanClass, entry.getValue());
            ssb.setSpan(span, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

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

    private boolean isParagraphSpan(Class<?> spanClass) {
        return LeadingParagraphSpan.class.isAssignableFrom(spanClass);
    }
}
