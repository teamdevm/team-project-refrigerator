package com.pmi.kysp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

public class NumericUpDownWidget extends LinearLayout {
    private int Value;
    private ValueChangeListener valueChangeListener;

    public interface ValueChangeListener {
        void onValueChange(int value);
    }

    public void setOnValueChangeListener(ValueChangeListener valueChangeListener)
    {
        this.valueChangeListener = valueChangeListener;
    }

    public NumericUpDownWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public NumericUpDownWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NumericUpDownWidget(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.numeric_updown_widget, this, true);

        AppCompatButton minusBtn = (AppCompatButton)findViewById(R.id.numeric__minus);
        AppCompatButton plusBtn = (AppCompatButton)findViewById(R.id.numeric__plus);

        Value = 1;
        updateTextValue();

        minusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Value == 1) return;
                Value--;
                if (valueChangeListener != null)
                    valueChangeListener.onValueChange(Value);
                updateTextValue();
            }
        });

        plusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Value++;
                if (valueChangeListener != null)
                    valueChangeListener.onValueChange(Value);
                updateTextValue();
            }
        });
    }

    private void updateTextValue()
    {
        TextView textView = (TextView)findViewById(R.id.numeric__number);
        textView.setText(String.valueOf(Value));
    }

    public int getValue()
    {
        return Value;
    }

    public void setValue(int newValue)
    {
        Value = newValue;
        updateTextValue();
    }
}
