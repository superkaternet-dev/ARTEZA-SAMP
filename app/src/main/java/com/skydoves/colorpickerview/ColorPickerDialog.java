/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.content.DialogInterface$OnKeyListener
 *  android.content.DialogInterface$OnMultiChoiceClickListener
 *  android.database.Cursor
 *  android.graphics.drawable.Drawable
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.ListAdapter
 */
package com.skydoves.colorpickerview;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import androidx.appcompat.app.AlertDialog;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.SizeUtils;
import com.skydoves.colorpickerview.databinding.DialogColorpickerBinding;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import com.skydoves.colorpickerview.sliders.AbstractSlider;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class ColorPickerDialog
extends AlertDialog {
    private ColorPickerView colorPickerView;

    public ColorPickerDialog(Context context) {
        super(context);
    }

    public static class Builder
    extends AlertDialog.Builder {
        private int bottomSpace = SizeUtils.dp2Px(this.getContext(), 10);
        private ColorPickerView colorPickerView;
        private DialogColorpickerBinding dialogBinding;
        private boolean shouldAttachAlphaSlideBar = true;
        private boolean shouldAttachBrightnessSlideBar = true;

        public Builder(Context context) {
            super(context);
            this.onCreate();
        }

        public Builder(Context context, int n) {
            super(context, n);
            this.onCreate();
        }

        private DialogInterface.OnClickListener getOnClickListener(ColorPickerViewListener colorPickerViewListener) {
            return new DialogInterface.OnClickListener(this, colorPickerViewListener){
                final Builder this$0;
                final ColorPickerViewListener val$colorListener;
                {
                    this.this$0 = builder;
                    this.val$colorListener = colorPickerViewListener;
                }

                public void onClick(DialogInterface object, int n) {
                    object = this.val$colorListener;
                    if (object instanceof ColorListener) {
                        ((ColorListener)object).onColorSelected(this.this$0.getColorPickerView().getColor(), true);
                    } else if (object instanceof ColorEnvelopeListener) {
                        ((ColorEnvelopeListener)object).onColorSelected(this.this$0.getColorPickerView().getColorEnvelope(), true);
                    }
                    if (this.this$0.getColorPickerView() != null) {
                        ColorPickerPreferenceManager.getInstance(this.this$0.getContext()).saveColorPickerData(this.this$0.getColorPickerView());
                    }
                }
            };
        }

        private void onCreate() {
            Object object = DialogColorpickerBinding.inflate(LayoutInflater.from((Context)this.getContext()), null, false);
            this.dialogBinding = object;
            this.colorPickerView = object = ((DialogColorpickerBinding)object).colorPickerView;
            ((ColorPickerView)object).attachAlphaSlider(this.dialogBinding.alphaSlideBar);
            this.colorPickerView.attachBrightnessSlider(this.dialogBinding.brightnessSlideBar);
            this.colorPickerView.setColorListener(new ColorEnvelopeListener(this){
                final Builder this$0;
                {
                    this.this$0 = builder;
                }

                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope, boolean bl) {
                }
            });
            super.setView((View)this.dialogBinding.getRoot());
        }

        public Builder attachAlphaSlideBar(boolean bl) {
            this.shouldAttachAlphaSlideBar = bl;
            return this;
        }

        public Builder attachBrightnessSlideBar(boolean bl) {
            this.shouldAttachBrightnessSlideBar = bl;
            return this;
        }

        @Override
        public AlertDialog create() {
            if (this.getColorPickerView() != null) {
                this.dialogBinding.colorPickerViewFrame.removeAllViews();
                this.dialogBinding.colorPickerViewFrame.addView((View)this.getColorPickerView());
                AbstractSlider abstractSlider = this.getColorPickerView().getAlphaSlideBar();
                boolean bl = this.shouldAttachAlphaSlideBar;
                if (bl && abstractSlider != null) {
                    this.dialogBinding.alphaSlideBarFrame.removeAllViews();
                    this.dialogBinding.alphaSlideBarFrame.addView((View)abstractSlider);
                    this.getColorPickerView().attachAlphaSlider((AlphaSlideBar)abstractSlider);
                } else if (!bl) {
                    this.dialogBinding.alphaSlideBarFrame.removeAllViews();
                }
                abstractSlider = this.getColorPickerView().getBrightnessSlider();
                bl = this.shouldAttachBrightnessSlideBar;
                if (bl && abstractSlider != null) {
                    this.dialogBinding.brightnessSlideBarFrame.removeAllViews();
                    this.dialogBinding.brightnessSlideBarFrame.addView((View)abstractSlider);
                    this.getColorPickerView().attachBrightnessSlider((BrightnessSlideBar)abstractSlider);
                } else if (!bl) {
                    this.dialogBinding.brightnessSlideBarFrame.removeAllViews();
                }
                if (!this.shouldAttachAlphaSlideBar && !this.shouldAttachBrightnessSlideBar) {
                    this.dialogBinding.spaceBottom.setVisibility(8);
                } else {
                    this.dialogBinding.spaceBottom.setVisibility(0);
                    this.dialogBinding.spaceBottom.getLayoutParams().height = this.bottomSpace;
                }
            }
            super.setView((View)this.dialogBinding.getRoot());
            return super.create();
        }

        public ColorPickerView getColorPickerView() {
            return this.colorPickerView;
        }

        @Override
        public Builder setAdapter(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
            super.setAdapter(listAdapter, onClickListener);
            return this;
        }

        public Builder setBottomSpace(int n) {
            this.bottomSpace = SizeUtils.dp2Px(this.getContext(), n);
            return this;
        }

        @Override
        public Builder setCancelable(boolean bl) {
            super.setCancelable(bl);
            return this;
        }

        public Builder setColorPickerView(ColorPickerView colorPickerView) {
            this.dialogBinding.colorPickerViewFrame.removeAllViews();
            this.dialogBinding.colorPickerViewFrame.addView((View)colorPickerView);
            return this;
        }

        @Override
        public Builder setCursor(Cursor cursor, DialogInterface.OnClickListener onClickListener, String string2) {
            super.setCursor(cursor, onClickListener, string2);
            return this;
        }

        @Override
        public Builder setCustomTitle(View view) {
            super.setCustomTitle(view);
            return this;
        }

        @Override
        public Builder setIcon(int n) {
            super.setIcon(n);
            return this;
        }

        @Override
        public Builder setIcon(Drawable drawable2) {
            super.setIcon(drawable2);
            return this;
        }

        @Override
        public Builder setIconAttribute(int n) {
            super.setIconAttribute(n);
            return this;
        }

        @Override
        public Builder setItems(int n, DialogInterface.OnClickListener onClickListener) {
            super.setItems(n, onClickListener);
            return this;
        }

        @Override
        public Builder setItems(CharSequence[] charSequenceArray, DialogInterface.OnClickListener onClickListener) {
            super.setItems(charSequenceArray, onClickListener);
            return this;
        }

        @Override
        public Builder setMessage(int n) {
            super.setMessage(this.getContext().getString(n));
            return this;
        }

        @Override
        public Builder setMessage(CharSequence charSequence) {
            super.setMessage(charSequence);
            return this;
        }

        @Override
        public Builder setMultiChoiceItems(int n, boolean[] blArray, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            super.setMultiChoiceItems(n, blArray, onMultiChoiceClickListener);
            return this;
        }

        @Override
        public Builder setMultiChoiceItems(Cursor cursor, String string2, String string3, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            super.setMultiChoiceItems(cursor, string2, string3, onMultiChoiceClickListener);
            return this;
        }

        @Override
        public Builder setMultiChoiceItems(CharSequence[] charSequenceArray, boolean[] blArray, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            super.setMultiChoiceItems(charSequenceArray, blArray, onMultiChoiceClickListener);
            return this;
        }

        @Override
        public Builder setNegativeButton(int n, DialogInterface.OnClickListener onClickListener) {
            super.setNegativeButton(n, onClickListener);
            return this;
        }

        @Override
        public Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            super.setNegativeButton(charSequence, onClickListener);
            return this;
        }

        @Override
        public Builder setNeutralButton(int n, DialogInterface.OnClickListener onClickListener) {
            super.setNeutralButton(n, onClickListener);
            return this;
        }

        @Override
        public Builder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            super.setNeutralButton(charSequence, onClickListener);
            return this;
        }

        @Override
        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            super.setOnCancelListener(onCancelListener);
            return this;
        }

        @Override
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            super.setOnDismissListener(onDismissListener);
            return this;
        }

        @Override
        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
            super.setOnItemSelectedListener(onItemSelectedListener);
            return this;
        }

        @Override
        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            super.setOnKeyListener(onKeyListener);
            return this;
        }

        @Override
        public Builder setPositiveButton(int n, DialogInterface.OnClickListener onClickListener) {
            super.setPositiveButton(n, onClickListener);
            return this;
        }

        public Builder setPositiveButton(int n, ColorPickerViewListener colorPickerViewListener) {
            super.setPositiveButton(n, this.getOnClickListener(colorPickerViewListener));
            return this;
        }

        @Override
        public Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            super.setPositiveButton(charSequence, onClickListener);
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, ColorPickerViewListener colorPickerViewListener) {
            super.setPositiveButton(charSequence, this.getOnClickListener(colorPickerViewListener));
            return this;
        }

        public Builder setPreferenceName(String string2) {
            if (this.getColorPickerView() != null) {
                this.getColorPickerView().setPreferenceName(string2);
            }
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(int n, int n2, DialogInterface.OnClickListener onClickListener) {
            super.setSingleChoiceItems(n, n2, onClickListener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(Cursor cursor, int n, String string2, DialogInterface.OnClickListener onClickListener) {
            super.setSingleChoiceItems(cursor, n, string2, onClickListener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(ListAdapter listAdapter, int n, DialogInterface.OnClickListener onClickListener) {
            super.setSingleChoiceItems(listAdapter, n, onClickListener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(CharSequence[] charSequenceArray, int n, DialogInterface.OnClickListener onClickListener) {
            super.setSingleChoiceItems(charSequenceArray, n, onClickListener);
            return this;
        }

        @Override
        public Builder setTitle(int n) {
            super.setTitle(n);
            return this;
        }

        @Override
        public Builder setTitle(CharSequence charSequence) {
            super.setTitle(charSequence);
            return this;
        }

        @Override
        public Builder setView(int n) {
            super.setView(n);
            return this;
        }

        @Override
        public Builder setView(View view) {
            super.setView(view);
            return this;
        }
    }
}

