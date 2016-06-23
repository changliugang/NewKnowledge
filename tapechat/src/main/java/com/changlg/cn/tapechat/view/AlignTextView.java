package com.changlg.cn.tapechat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.changlg.cn.tapechat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Justifyfull text view, you can set the last line on the left, right, center
 */
public class AlignTextView extends TextView {
    private float textHeight;
    private float textLineSpaceExtra = 0; // Line spacing
    private int width; // textView width
    private List<String> lines = new ArrayList<String>(); // Split lines
    private List<Integer> tailLines = new ArrayList<Integer>(); // tailLines
    private Align align = Align.ALIGN_LEFT; // default tail line align left
    private boolean firstCalc = true;  // The first calculation of the logo, to prevent repeated calls, repeated calculation

    private float lineSpacingMultiplier = 1.0f;
    private float lineSpacingAdd = 0.0f;

    private int originalHeight = 0;
    private int originalLineCount = 0;
    private int originalPaddingBottom = 0;
    private boolean setPaddingFromMe = false;

    //  tail line align mode
    public enum Align {
        ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT
    }

    public AlignTextView(Context context) {
        super(context);
        setTextIsSelectable(false);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextIsSelectable(false);

        lineSpacingMultiplier = attrs.getAttributeFloatValue("http://schemas.android" + "" +
                ".com/apk/res/android", "lineSpacingMultiplier", 1.0f);

        int[] attributes = new int[]{android.R.attr.lineSpacingExtra};

        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);

        lineSpacingAdd = arr.getDimensionPixelSize(0, 0);

        originalPaddingBottom = getPaddingBottom();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView);

        int alignStyle = ta.getInt(R.styleable.AlignTextView_align, 0);
        switch (alignStyle) {
            case 1:
                align = Align.ALIGN_CENTER;
                break;
            case 2:
                align = Align.ALIGN_RIGHT;
                break;
            default:
                align = Align.ALIGN_LEFT;
                break;
        }

        ta.recycle();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //First of all, a high degree of adjustment
        if (firstCalc) {
            width = getMeasuredWidth();
            String text = getText().toString();
            TextPaint paint = getPaint();
            lines.clear();
            tailLines.clear();

            //The text contains a newline, handle the split alone.
            String[] items = text.split("\\n");
            for (String item : items) {
                calc(paint, item);
            }

            //Calculate the original height and number of rows by using alternative textview
            measureTextViewHeight(text, paint.getTextSize(), getMeasuredWidth() -
                    getPaddingLeft() - getPaddingRight());

            //get line height
            textHeight = 1.0f * originalHeight / originalLineCount;

            textLineSpaceExtra = textHeight * (lineSpacingMultiplier - 1) + lineSpacingAdd;

            //Calculate the actual height, plus the number of rows of the height (generally reduced)
            int heightGap = (int) ((textLineSpaceExtra + textHeight) * (lines.size() -
                    originalLineCount));

            setPaddingFromMe = true;
            //Adjust paddingBottom of textview to reduce the bottom blank
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                    originalPaddingBottom + heightGap);

            firstCalc = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();

        width = getMeasuredWidth();

        Paint.FontMetrics fm = paint.getFontMetrics();
        float firstHeight = getTextSize() - (fm.bottom - fm.descent + fm.ascent - fm.top);

        int gravity = getGravity();
        if ((gravity & 0x1000) == 0) { // Whether the vertical center
            firstHeight = firstHeight + (textHeight - firstHeight) / 2;
        }

        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        width = width - paddingLeft - paddingRight;

        for (int i = 0; i < lines.size(); i++) {
            float drawY = i * textHeight + firstHeight;
            String line = lines.get(i);
            //Draw the start x coordinates
            float drawSpacingX = paddingLeft;
            float gap = (width - paint.measureText(line));
            float interval = gap / (line.length() - 1);

            // Draw The last line
            if (tailLines.contains(i)) {
                interval = 0;
                if (align == Align.ALIGN_CENTER) {
                    drawSpacingX += gap / 2;
                } else if (align == Align.ALIGN_RIGHT) {
                    drawSpacingX += gap;
                }
            }

            for (int j = 0; j < line.length(); j++) {
                float drawX = paint.measureText(line.substring(0, j)) + interval * j;
                canvas.drawText(line.substring(j, j + 1), drawX + drawSpacingX, drawY +
                        paddingTop + textLineSpaceExtra * i, paint);
            }
        }
    }

    /**
     * setup align mode of the last line
     *
     * @param align align mode
     */
    public void setAlign(Align align) {
        this.align = align;
        invalidate();
    }

    /**
     * Calculate the number of text to be displayed per line
     *
     * @param text text
     */
    private void calc(Paint paint, String text) {
        if (text.length() == 0) {
            lines.add("\n");
            return;
        }
        int startPosition = 0;
        float oneChineseWidth = paint.measureText("AS");
        int ignoreCalcLength = (int) (width / oneChineseWidth); // ignore calculation Length
        StringBuilder sb = new StringBuilder(text.substring(0, Math.min(ignoreCalcLength + 1,
                text.length())));

        for (int i = ignoreCalcLength + 1; i < text.length(); i++) {
            if (paint.measureText(text.substring(startPosition, i + 1)) > width) {
                startPosition = i;
                //Add a string to the list
                lines.add(sb.toString());

                sb = new StringBuilder();

                //Add start to ignore the string, the length of the problem directly to the end, otherwise continue
                if ((text.length() - startPosition) > ignoreCalcLength) {
                    sb.append(text.substring(startPosition, startPosition + ignoreCalcLength));
                } else {
                    lines.add(text.substring(startPosition));
                    break;
                }

                i = i + ignoreCalcLength - 1;
            } else {
                sb.append(text.charAt(i));
            }
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }

        tailLines.add(lines.size() - 1);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        firstCalc = true;
        super.setText(text, type);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (!setPaddingFromMe) {
            originalPaddingBottom = bottom;
        }
        setPaddingFromMe = false;
        super.setPadding(left, top, right, bottom);
    }


    /**
     * Get the actual height of the text, auxiliary for the calculation of the line, the number of rows
     *
     * @param text        text
     * @param textSize    text size
     * @param deviceWidth device width
     */
    private void measureTextViewHeight(String text, float textSize, int deviceWidth) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        originalLineCount = textView.getLineCount();
        originalHeight = textView.getMeasuredHeight();
    }
}