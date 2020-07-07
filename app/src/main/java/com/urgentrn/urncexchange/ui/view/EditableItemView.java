package com.urgentrn.urncexchange.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;

public class EditableItemView extends LinearLayout implements View.OnClickListener {

    private ImageView imgCircle;
    private TextView txtButton;
    private EditText editValue;

    private String mTitle, mAddText, mDeleteText, mHintText;
    private int mnAddIcon, mDeleteIcon;
    private boolean mEditable;

    public EditableItemView(Context context) {
        super(context);
        init();
    }

    public EditableItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditableItemView, 0, 0);
        mTitle = attributes.getString(R.styleable.EditableItemView_title);
        mAddText = attributes.getString(R.styleable.EditableItemView_textAdd);
        mDeleteText = attributes.getString(R.styleable.EditableItemView_textDelete);
        mHintText = attributes.getString(R.styleable.EditableItemView_textHint);
        mnAddIcon = attributes.getResourceId(R.styleable.EditableItemView_iconAdd, R.mipmap.ic_add_green);
        mDeleteIcon = attributes.getResourceId(R.styleable.EditableItemView_iconDelete, R.mipmap.ic_launcher);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_editable, this);

        setBackgroundColor(getResources().getColor(R.color.colorWhite));
        setOnClickListener(this);
        imgCircle = findViewById(R.id.imgCircle);
        txtButton = findViewById(R.id.txtButton);
        editValue = findViewById(R.id.editValue);

        updateView();
    }

    @Override
    public void onClick(View v) {
        setEditable(!mEditable);
    }
    
    private void updateView() {
        if (mEditable) {
            imgCircle.setImageResource(mDeleteIcon);
            txtButton.setText(mDeleteText != null ? mDeleteText : getResources().getString(R.string.button_delete));
            editValue.setVisibility(VISIBLE);
            if (editValue.getText().toString().isEmpty()) {
                editValue.requestFocus();
            }
        } else {
            imgCircle.setImageResource(mnAddIcon);
            txtButton.setText(mAddText != null ? mAddText : mTitle);
            editValue.setHint(mHintText != null ? mHintText : mTitle);
            editValue.setText(null);
            editValue.setVisibility(GONE);
        }
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
        updateView();
    }

    public void setInputType(int type) {
        editValue.setInputType(type);
    }
    
    public void setTitle(String title) {
        mTitle = title;
        updateView();
    }

    public String getValue() {
        return editValue.getText().toString().trim();
    }

    public void setValue(String value) {
        mEditable = value != null && !value.isEmpty();
        editValue.setText(value);
        updateView();
    }
}
