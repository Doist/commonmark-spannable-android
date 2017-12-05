package org.commonmark.renderer.spannable;

import org.commonmark.renderer.spannable.internal.SpannableProviderMap;
import org.commonmark.renderer.spannable.text.style.CountedSpan;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class SpannableWriter {
    private final Map<Class<?>, Object> mSpanTypes = new HashMap<>();

    private final SpannableStringBuilder mBuffer;

    private SpannableProviderMap mProviderMap;

    private int mCount;

    private char mLastChar = 0;

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

    public void start(Class<?> spanClass) {
        start(spanClass, null);
    }

    public void start(Class<?> spanClass, @Nullable Object parameter) {
        mSpanTypes.put(spanClass, parameter);
    }

    public void end(Class<?> spanClass) {
        mSpanTypes.remove(spanClass);
    }

    public void write(String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        int length = text.length();
        for (Map.Entry<Class<?>, Object> entry : mSpanTypes.entrySet()) {
            Class<?> spanClass = entry.getKey();
            Object span = mProviderMap.get(spanClass).create(spanClass, entry.getValue());
            if (span instanceof CountedSpan) {
                ((CountedSpan) span).setCount(mCount);
                mCount++;
            }
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
}
