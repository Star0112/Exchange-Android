package com.fxn769;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by redrose on 18/03/19.
 */

public class Numpad extends FrameLayout implements View.OnClickListener {
    private Context context;
    private ArrayList<TextView> num = new ArrayList<>();
    private ArrayList<ImageView> line = new ArrayList<>();
    private String digits = "";
    private int TextLengthLimit = 6;
    private float TextSize = 12;
    private int TextColor = Color.BLACK;
    private int BackgroundResource = R.drawable.numpad_background;
    private int ImageResource = R.drawable.ic_backspace;
    private boolean GridVisible = true;
    private int GridBackgroundColor = Color.GRAY;
    private int GridThicknessHorizontal = 3, GridThicknessVertical = 3;
    private String FontFaceString = "";
    private String CommaString = "";
    private Typeface typeface;
    private ImageView delete;
    private TextView comma;
    private FrameLayout delete_layout;
    private TextGetListener textGetListener;
    private boolean isNumber = false, isDecimal = true;

    public Numpad(@NonNull Context context) {
        super(context);
        initialize(context, null);
    }

    public Numpad(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Numpad(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public void setOnTextChangeListener(TextGetListener textGetListener) {
        this.textGetListener = textGetListener;
        setup();
    }

    public void setBackgroundRes(int BackgroundResource) {
        this.BackgroundResource = BackgroundResource;
        setup();
    }

    public void setImageRes(int ImageResource) {
        this.ImageResource = ImageResource;
        setup();
    }

    public void setFontFace(String FontFaceString) {
        this.FontFaceString = FontFaceString;
        typeface = Typeface.createFromAsset(context.getAssets(), this.FontFaceString);
        setup();
    }

    private void initialize(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Numpad, 0, 0);
        digits = attributes.getString(R.styleable.Numpad_numpad_digits);
        TextLengthLimit = attributes.getInt(R.styleable.Numpad_numpad_text_limit, 6);
        TextSize = attributes.getDimension(R.styleable.Numpad_numpad_text_size, 21.0f);
        TextColor = attributes.getColor(R.styleable.Numpad_numpad_text_color, Color.BLACK);
        BackgroundResource = attributes.getResourceId(R.styleable.Numpad_numpad_background_resource, R.drawable.numpad_background);
        ImageResource = attributes.getResourceId(R.styleable.Numpad_numpad_image_resource, R.drawable.ic_backspace);
        GridVisible = attributes.getBoolean(R.styleable.Numpad_numpad_grid_visible, false);
        GridBackgroundColor = attributes.getColor(R.styleable.Numpad_numpad_grid_background_color, Color.GRAY);
        GridThicknessHorizontal = (int) attributes.getDimension(R.styleable.Numpad_numpad_grid_line_thickness_horizontal, 3);
        GridThicknessVertical = (int) attributes.getDimension(R.styleable.Numpad_numpad_grid_line_thickness_vertical, 3);
        FontFaceString = attributes.getString(R.styleable.Numpad_numpad_fontpath);
        CommaString = attributes.getString(R.styleable.Numpad_numpad_comma);
        isNumber = attributes.getBoolean(R.styleable.Numpad_numpad_is_number, false);
        isDecimal = attributes.getBoolean(R.styleable.Numpad_numpad_is_decimal, true);
        if (digits == null) {
            digits = "";
        }

        final View v = LayoutInflater.from(context).inflate(R.layout.numlock_view, this, false);
        num.add(v.findViewById(R.id.one));
        num.add(v.findViewById(R.id.two));
        num.add(v.findViewById(R.id.three));
        num.add(v.findViewById(R.id.four));
        num.add(v.findViewById(R.id.five));
        num.add(v.findViewById(R.id.six));
        num.add(v.findViewById(R.id.seven));
        num.add(v.findViewById(R.id.eight));
        num.add(v.findViewById(R.id.nine));
        num.add(v.findViewById(R.id.zero));
        line.add(v.findViewById(R.id.line1));
        line.add(v.findViewById(R.id.line2));
        line.add(v.findViewById(R.id.line3));
        line.add(v.findViewById(R.id.line4));
        line.add(v.findViewById(R.id.line5));
        line.add(v.findViewById(R.id.line6));
        line.add(v.findViewById(R.id.line7));
        line.add(v.findViewById(R.id.line8));
        line.add(v.findViewById(R.id.line9));
        line.add(v.findViewById(R.id.line10));
        line.add(v.findViewById(R.id.line11));
        comma = v.findViewById(R.id.comma);
        delete = v.findViewById(R.id.delete);
        delete_layout = v.findViewById(R.id.delete_layout);
        if (FontFaceString == null) {
            typeface = Typeface.DEFAULT;
        } else {
            typeface = Typeface.createFromAsset(context.getAssets(), FontFaceString);
        }
        setup();
        addView(v);
    }

    private void setup() {

        for (TextView textView : num) {
            textView.setOnClickListener(this);
            textView.setTextSize(TextSize);
//            textView.setTextColor(TextColor);
            textView.setBackgroundResource(BackgroundResource);
            textView.setElevation(3);
            textView.setTypeface(typeface);
        }

        if (GridVisible) {
            for (ImageView imageView : line) {
                imageView.setVisibility(VISIBLE);
                imageView.setBackgroundColor(GridBackgroundColor);
            }
            line.get(0).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(1).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(2).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(3).setLayoutParams(new LinearLayout.LayoutParams(GridThicknessHorizontal, LayoutParams.MATCH_PARENT));
            line.get(4).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(5).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(6).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(7).setLayoutParams(new LinearLayout.LayoutParams(GridThicknessHorizontal, LayoutParams.MATCH_PARENT));
            line.get(8).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(9).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
            line.get(10).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThicknessVertical));
        }

        delete_layout.setOnClickListener(this);
        delete_layout.setBackgroundResource(BackgroundResource);
        delete.setImageResource(ImageResource);

        if (CommaString != null) {
            comma.setText(CommaString);
            comma.setOnClickListener(this);
            comma.setTextSize(TextSize);
//            comma.setTextColor(TextColor);
            comma.setBackgroundResource(BackgroundResource);
            comma.setElevation(3);
            comma.setTypeface(typeface);
        }
    }

    public String getDigits() {
        return digits;
    }

    public void clearDigits() {
        digits = "";
        textGetListener.onTextChange(digits, TextLengthLimit);
    }

    public int getTextLengthLimit() {
        return TextLengthLimit;
    }

    public void setTextLengthLimit(int TextLengthLimit) {
        this.TextLengthLimit = TextLengthLimit;
        setup();
    }

    public float getTextSize() {
        return TextSize;
    }

    public void setTextSize(int TextSize) {
        this.TextSize = TextSize;
        setup();
    }

    public int getTextColor() {
        return TextColor;
    }

    public void setTextColor(int TextColor) {
        this.TextColor = TextColor;
        setup();
    }

    public int getBackgroundResource() {
        return BackgroundResource;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public boolean isGridVisible() {
        return GridVisible;
    }

    public void setGridVisible(boolean GridVisible) {
        this.GridVisible = GridVisible;
        setup();
    }

    public int getGridBackgroundColor() {
        return GridBackgroundColor;
    }

    public void setGridBackgroundColor(int GridBackgroundColor) {
        this.GridBackgroundColor = GridBackgroundColor;
        setup();
    }

    public int getGridThicknessHorizontal() {
        return GridThicknessHorizontal;
    }

    public void setGridThicknessHorizontal(int GridThickness) {
        this.GridThicknessHorizontal = GridThickness;
        setup();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public ImageView getImageResourceView() {
        return delete;
    }

    public TextGetListener getTextGetListener() {
        return textGetListener;
    }

    public void setNumber(boolean number) {
        this.isNumber = number;
        if (number) this.TextLengthLimit = 8;
    }

    public void setDecimal(boolean decimal) {
        setNumber(true);
        this.isDecimal = decimal;
        if (decimal) {
            if (digits.isEmpty()) digits = "0";
        }
    }

    public double getValue() {
        if (!isNumber || digits.isEmpty()) return 0;
        try {
            return Double.parseDouble(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setValue(double number) {
        if (!isNumber) {
            setNumber(true);
        }
        if (number == 0) {
            digits = "0";
        } else {
            if (true) {
                if (isDecimal) {
                    digits = String.format(Locale.US, "%f", number);
                } else {
                    digits = String.format(Locale.US, "%d", (int)number);
                }
            } else {
                final NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setRoundingMode(RoundingMode.DOWN);
                digits = nf.format(number).replace(",", "");
            }
        }
        if (digits.length() > TextLengthLimit) {
            digits = digits.substring(0, TextLengthLimit);
        }
        if (textGetListener != null) {
            textGetListener.onTextChange(digits, TextLengthLimit - digits.length());
        }
    }

    @Override
    public void onClick(final View view) {
        if (view instanceof TextView && digits.length() < TextLengthLimit) {
            final CharSequence text = ((TextView)view).getText();
            if (text.equals(".")) {
                if (isNumber && isDecimal && !digits.contains(".")) {
                    if (digits.isEmpty()) digits += "0";
                    digits += text;
                } else {
                    return;
                }
            } else if (digits.equals("0")) {
                if (isNumber) {
                    if (text.equals("0")) {
                        return;
                    } else {
                        digits = text.toString();
                    }
                } else {
                    digits += text;
                }
            } else {
                digits += text;
            }
        } else if (view instanceof FrameLayout && digits.length() >= 1) {
            if (isNumber && digits.length() <= 1) {
                digits = "0";
            } else {
                digits = digits.substring(0, digits.length() - 1);
            }
        }
        if (BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), digits);
        if (textGetListener != null) {
            textGetListener.onTextChange(digits, TextLengthLimit - digits.length());
        }
    }

}
