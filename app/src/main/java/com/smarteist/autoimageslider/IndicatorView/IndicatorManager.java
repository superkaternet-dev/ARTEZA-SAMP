/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView;

import com.smarteist.autoimageslider.IndicatorView.animation.AnimationManager;
import com.smarteist.autoimageslider.IndicatorView.animation.controller.ValueController;
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;
import com.smarteist.autoimageslider.IndicatorView.draw.DrawManager;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;

public class IndicatorManager
implements ValueController.UpdateListener {
    private AnimationManager animationManager;
    private DrawManager drawManager;
    private Listener listener;

    IndicatorManager(Listener object) {
        this.listener = object;
        this.drawManager = object = new DrawManager();
        this.animationManager = new AnimationManager(((DrawManager)object).indicator(), this);
    }

    public AnimationManager animate() {
        return this.animationManager;
    }

    public DrawManager drawer() {
        return this.drawManager;
    }

    public Indicator indicator() {
        return this.drawManager.indicator();
    }

    @Override
    public void onValueUpdated(Value object) {
        this.drawManager.updateValue((Value)object);
        object = this.listener;
        if (object != null) {
            object.onIndicatorUpdated();
        }
    }

    static interface Listener {
        public void onIndicatorUpdated();
    }
}

