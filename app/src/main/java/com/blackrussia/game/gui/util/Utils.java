/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.app.Activity
 *  android.graphics.drawable.Drawable
 *  android.text.Html
 *  android.text.Layout$Alignment
 *  android.text.Spanned
 *  android.text.StaticLayout
 *  android.text.TextPaint
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.TextView
 */
package com.blackrussia.game.gui.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Utils {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static void HideLayout(View view, boolean bl) {
        if (view != null) {
            if (bl) {
                Utils.fadeOut(view);
                return;
            }
            view.setAlpha(0.0f);
            view.setVisibility(8);
        }
    }

    public static void ShowLayout(View view, boolean bl) {
        if (view != null) {
            view.setVisibility(0);
            if (bl) {
                Utils.fadeIn(view);
            } else {
                view.setAlpha(1.0f);
            }
        }
    }

    public static void changeTextViewWidth(TextView textView) {
        textView.post(new Runnable(textView){
            final TextView val$textView;
            {
                this.val$textView = textView;
            }

            @Override
            public void run() {
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(this.val$textView.getTextSize());
                textPaint.setTypeface(this.val$textView.getTypeface());
                textPaint.measureText(this.val$textView.getText().toString());
                textPaint = new StaticLayout(this.val$textView.getText(), textPaint, 10000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                int n = textPaint.getLineCount();
                float f = textPaint.getLineWidth(0);
                for (int i = 0; i < n; ++i) {
                    float f2 = f;
                    if (textPaint.getLineWidth(i) >= f) {
                        f2 = textPaint.getLineWidth(i) + 5.0f;
                    }
                    f = f2;
                }
                textPaint = this.val$textView.getLayoutParams();
                textPaint.width = (int)f;
                this.val$textView.setLayoutParams((ViewGroup.LayoutParams)textPaint);
            }
        });
    }

    private static void fadeIn(View view) {
        if (view != null) {
            view.animate().setDuration(150L).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator animator2) {
                    super.onAnimationEnd(animator2);
                }
            }).alpha(1.0f);
        }
    }

    private static void fadeOut(View view) {
        if (view != null) {
            view.animate().setDuration(150L).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(view){
                final View val$view;
                {
                    this.val$view = view;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$view.setVisibility(8);
                    super.onAnimationEnd(animator2);
                }
            }).alpha(0.0f);
        }
    }

    public static ArrayList<String> fixFieldsForDialog(ArrayList<String> arrayList) {
        int n;
        int n2;
        ArrayList<String> arrayList2 = new ArrayList<String>();
        int n3 = 0;
        for (n2 = 0; n2 < arrayList.size(); ++n2) {
            int n4 = arrayList.get(n2).split("\t").length;
            n = n3;
            if (n4 > n3) {
                n = n4;
            }
            n3 = n;
        }
        for (n2 = 0; n2 < arrayList.size(); ++n2) {
            StringBuilder stringBuilder = new StringBuilder(arrayList.get(n2));
            for (n = arrayList.get(n2).split("\t").length; n != n3; ++n) {
                stringBuilder.append("\\t ");
            }
            arrayList2.add(stringBuilder.toString());
        }
        return arrayList2;
    }

    public static Drawable getRes(Activity activity, int n) {
        return ContextCompat.getDrawable(activity.getApplicationContext(), n);
    }

    public static int getTextLength(TextView textView) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textView.getTextSize());
        textPaint.setTypeface(textView.getTypeface());
        return (int)textPaint.measureText(textView.getText().toString());
    }

    public static void makeAllViewsVisible(ViewGroup viewGroup) {
        viewGroup.setVisibility(0);
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                Utils.makeAllViewsVisible((ViewGroup)viewGroup.getChildAt(i));
                continue;
            }
            viewGroup.getChildAt(i).setVisibility(0);
        }
    }

    public static String randomString(int n) {
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return stringBuilder.toString();
    }

    public static Spanned transfromColors(String object) {
        Object object2;
        int n;
        LinkedList<String> linkedList = new LinkedList<String>();
        int n2 = 0;
        int n3 = 0;
        for (n = 0; n < ((String)object).length(); ++n) {
            object2 = object;
            int n4 = n3;
            if (((String)object).charAt(n) == '{') {
                int n5 = n + 7;
                object2 = object;
                n4 = n3;
                if (n5 < ((String)object).length()) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("#");
                    n4 = n + 1;
                    ((StringBuilder)object2).append(((String)object).substring(n4, n5));
                    linkedList.addLast(((StringBuilder)object2).toString());
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append(((String)object).substring(0, n4));
                    ((StringBuilder)object2).append("repl");
                    ((StringBuilder)object2).append(n3);
                    ((StringBuilder)object2).append(((String)object).substring(n5));
                    object2 = ((StringBuilder)object2).toString();
                    n4 = n3 + 1;
                }
            }
            object = object2;
            n3 = n4;
        }
        object2 = linkedList.iterator();
        n = n2;
        while (object2.hasNext()) {
            StringBuilder stringBuilder;
            CharSequence charSequence;
            String string2 = (String)object2.next();
            if (n == 0) {
                charSequence = new StringBuilder();
                charSequence.append("{repl");
                charSequence.append(n);
                charSequence.append("}");
                charSequence = Pattern.quote(charSequence.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append("<font color='");
                stringBuilder.append(string2);
                stringBuilder.append("'>");
                object = ((String)object).replaceAll((String)charSequence, stringBuilder.toString());
            } else {
                charSequence = new StringBuilder();
                charSequence.append("{repl");
                charSequence.append(n);
                charSequence.append("}");
                charSequence = Pattern.quote(charSequence.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append("</font><font color='");
                stringBuilder.append(string2);
                stringBuilder.append("'>");
                object = ((String)object).replaceAll((String)charSequence, stringBuilder.toString());
            }
            ++n;
        }
        object2 = object;
        if (linkedList.size() >= 1) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append((String)object);
            ((StringBuilder)object2).append("</font>");
            object2 = ((StringBuilder)object2).toString();
        }
        return Html.fromHtml((String)((String)object2).replaceAll(Pattern.quote("\n"), "<br>"));
    }
}

