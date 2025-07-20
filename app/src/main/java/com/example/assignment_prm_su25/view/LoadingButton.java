package com.example.assignment_prm_su25.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.example.assignment_prm_su25.R;
import com.google.android.material.button.MaterialButton;

public class LoadingButton extends MaterialButton {

    private ButtonState buttonState = ButtonState.Normal;
    private float sweepAngle = 0f;
    private float progressWidth;
    private int progressColor;

    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF bounds = new RectF();
    private CharSequence originalText;

    private final ValueAnimator animator;

    public enum ButtonState {
        Normal, Loading
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, com.google.android.material.R.attr.materialButtonStyle);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LoadingButton,
                defStyleAttr,
                R.style.Widget_App_Button
        );

        try {
            progressWidth = a.getDimension(
                    R.styleable.LoadingButton_progressWidth,
                    getResources().getDimension(R.dimen.progress_stroke_width)
            );
            progressColor = a.getColor(
                    R.styleable.LoadingButton_progressColor,
                    ContextCompat.getColor(context, R.color.white)
            );
            if (a.getBoolean(R.styleable.LoadingButton_loading, false)) {
                setButtonState(ButtonState.Loading);
            }
        } finally {
            a.recycle();
        }

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);

        originalText = getText();

        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(1500);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateProgressBounds();
    }

    private void updateProgressBounds() {
        float size = Math.min(getHeight() * 0.4f, getWidth() * 0.4f);
        float padding = (getHeight() - size) / 2;
        bounds.set(
                getWidth() - size - padding,
                padding,
                getWidth() - padding,
                getHeight() - padding
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (buttonState == ButtonState.Loading) {
            canvas.drawArc(bounds, -90f, sweepAngle, false, progressPaint);
        }
    }

    public void setButtonState(ButtonState state) {
        if (state == buttonState) return;

        buttonState = state;
        if (state == ButtonState.Loading) {
            setClickable(false);
            setFocusable(false);
            originalText = getText();
            setText("");
            animator.start();
        } else {
            setClickable(true);
            setFocusable(true);
            setText(originalText);
            animator.cancel();
        }
        invalidate();
    }

    public void setProgressColor(@ColorInt int color) {
        progressColor = color;
        progressPaint.setColor(color);
        invalidate();
    }

    public void setProgressWidth(float width) {
        progressWidth = width;
        progressPaint.setStrokeWidth(width);
        updateProgressBounds();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.cancel();
    }
}