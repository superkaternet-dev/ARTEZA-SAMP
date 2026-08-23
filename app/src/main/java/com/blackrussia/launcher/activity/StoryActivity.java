/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.CountDownTimer
 *  android.os.Handler
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.animation.AnimationUtils
 *  android.widget.ImageView
 */
package com.blackrussia.launcher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.blackrussia.launcher.activity.MainActivity;
import com.blackrussia.launcher.activity.StoryActivity$$ExternalSyntheticLambda0;
import com.blackrussia.launcher.activity.StoryActivity$$ExternalSyntheticLambda1;
import com.blackrussia.launcher.adapter.SliderStoriesAdapter;
import com.blackrussia.launcher.other.Lists;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class StoryActivity
extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private long progress;
    private RoundCornerProgressBar progressStory;
    private SliderStoriesAdapter sliderStoriesAdapter;
    private SliderView sliderView;

    static /* synthetic */ long access$002(StoryActivity storyActivity, long l) {
        storyActivity.progress = l;
        return l;
    }

    private void startTimer() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.progress = 0L;
            this.countDownTimer = null;
        }
        this.countDownTimer = new CountDownTimer(this, 5000L, 1L){
            final StoryActivity this$0;
            {
                this.this$0 = storyActivity;
                super(l, l2);
            }

            public void onFinish() {
                if (this.this$0.sliderView.getCurrentPagePosition() + 1 == this.this$0.sliderStoriesAdapter.getCount()) {
                    this.this$0.closeStory();
                } else {
                    this.this$0.sliderView.setCurrentPagePosition(this.this$0.sliderView.getCurrentPagePosition() + 1);
                }
            }

            public void onTick(long l) {
                StoryActivity.access$002(this.this$0, 5000L - l);
                this.this$0.progressStory.setProgress(this.this$0.progress);
            }
        }.start();
    }

    public void closeStory() {
        this.countDownTimer.cancel();
        this.countDownTimer = null;
        this.progress = 0L;
        this.startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
    }

    public /* synthetic */ void lambda$lambda$onCreate$0$StoryActivity$1$com-blackrussia-launcher-activity-StoryActivity() {
        this.closeStory();
    }

    public void lambda$onCreate$0$StoryActivity(View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this, (int)2130771992));
        new Handler().postDelayed((Runnable)new StoryActivity$$ExternalSyntheticLambda1(this), 200L);
    }

    public /* synthetic */ void lambda$onCreate$0$com-blackrussia-launcher-activity-StoryActivity(int n) {
        this.startTimer();
    }

    @Override
    protected void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131558433);
        this.sliderView = (SliderView)this.findViewById(2131362387);
        this.progressStory = (RoundCornerProgressBar)((Object)this.findViewById(2131362296));
        ((ImageView)this.findViewById(2131361959)).setOnClickListener(new View.OnClickListener(this){
            final StoryActivity this$0;
            {
                this.this$0 = storyActivity;
            }

            public void onClick(View view) {
                this.this$0.lambda$onCreate$0$StoryActivity(view);
            }
        });
        object = new SliderStoriesAdapter((Context)this);
        this.sliderStoriesAdapter = object;
        this.sliderView.setSliderAdapter((SliderViewAdapter)object);
        this.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        this.sliderStoriesAdapter.addItems(Lists.nlist);
        this.sliderView.setCurrentPageListener(new StoryActivity$$ExternalSyntheticLambda0(this));
        int n = this.getIntent().getIntExtra("position", 0);
        this.sliderView.getSliderPager().setCurrentItem(n, false);
        this.sliderView.getPagerIndicator().setSelection(n);
        this.progressStory.setProgress(0.0f);
        this.startTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.countDownTimer.cancel();
        this.progress = 0L;
        this.countDownTimer = null;
    }
}

