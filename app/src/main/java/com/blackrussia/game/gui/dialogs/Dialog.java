/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.ViewGroup$LayoutParams
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ScrollView
 *  android.widget.TextView
 */
package com.blackrussia.game.gui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda0;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda1;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda2;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda3;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda4;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda5;
import com.blackrussia.game.gui.dialogs.Dialog$$ExternalSyntheticLambda6;
import com.blackrussia.game.gui.dialogs.DialogAdapter;
import com.blackrussia.game.gui.util.CustomRecyclerView;
import com.blackrussia.game.gui.util.Utils;
import com.nvidia.devtech.CustomEditText;
import com.nvidia.devtech.NvEventQueueActivity;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class Dialog {
    private static final int DIALOG_LEFT_BTN_ID = 1;
    private static final int DIALOG_RIGHT_BTN_ID = 0;
    private static final int DIALOG_STYLE_INPUT = 1;
    private static final int DIALOG_STYLE_LIST = 2;
    private static final int DIALOG_STYLE_MSGBOX = 0;
    private static final int DIALOG_STYLE_PASSWORD = 3;
    private static final int DIALOG_STYLE_TABLIST = 4;
    private static final int DIALOG_STYLE_TABLIST_HEADER = 5;
    private final TextView mCaption;
    private final TextView mContent;
    private int mCurrentDialogId = -1;
    private int mCurrentDialogTypeId = -1;
    private String mCurrentInputText = "";
    private int mCurrentListItem = -1;
    private final CustomRecyclerView mCustomRecyclerView;
    private final ArrayList<TextView> mHeadersList;
    private final CustomEditText mInput;
    private final ConstraintLayout mInputLayout;
    private final ConstraintLayout mLeftBtn;
    private final ConstraintLayout mListLayout;
    private final ConstraintLayout mMainLayout;
    private final ScrollView mMsgBoxLayout;
    private final ConstraintLayout mRightBtn;
    private ArrayList<String> mRowsList;

    public Dialog(Activity object) {
        ConstraintLayout constraintLayout;
        ConstraintLayout constraintLayout2;
        this.mMainLayout = (ConstraintLayout)object.findViewById(2131362352);
        this.mCaption = (TextView)object.findViewById(2131362341);
        this.mContent = (TextView)object.findViewById(2131362354);
        this.mLeftBtn = constraintLayout2 = (ConstraintLayout)object.findViewById(2131362338);
        this.mRightBtn = constraintLayout = (ConstraintLayout)object.findViewById(2131362336);
        this.mInputLayout = (ConstraintLayout)object.findViewById(2131362343);
        this.mListLayout = (ConstraintLayout)object.findViewById(2131362350);
        this.mMsgBoxLayout = (ScrollView)object.findViewById(2131362355);
        this.mInput = (CustomEditText)object.findViewById(2131362342);
        this.mCustomRecyclerView = (CustomRecyclerView)object.findViewById(2131362351);
        constraintLayout2.setOnClickListener(new Dialog$$ExternalSyntheticLambda0(this));
        constraintLayout.setOnClickListener(new Dialog$$ExternalSyntheticLambda1(this));
        this.mRowsList = new ArrayList();
        this.mHeadersList = new ArrayList();
        object = (ConstraintLayout)object.findViewById(2131362353);
        for (int i = 0; i < object.getChildCount(); ++i) {
            this.mHeadersList.add((TextView)object.getChildAt(i));
        }
        this.mInput.setOnEditorActionListener(new Dialog$$ExternalSyntheticLambda3(this));
        this.mInput.setOnClickListener(new Dialog$$ExternalSyntheticLambda2(this));
        Utils.HideLayout((View)this.mMainLayout, false);
    }

    private void clearDialogData() {
        this.mInput.setText("");
        this.mCurrentDialogId = -1;
        this.mCurrentDialogTypeId = -1;
        this.mCurrentListItem = -1;
        this.mRowsList.clear();
        for (int i = 0; i < this.mHeadersList.size(); ++i) {
            this.mHeadersList.get(i).setVisibility(8);
        }
    }

    static /* synthetic */ void lambda$show$6(DialogAdapter dialogAdapter) {
        dialogAdapter.updateSizes();
    }

    private void loadTabList(String stringArray) {
        stringArray = stringArray.split("\n");
        for (int i = 0; i < stringArray.length; ++i) {
            if (this.mCurrentDialogTypeId == 5 && i == 0) {
                String[] stringArray2 = stringArray[i].split("\t");
                for (int j = 0; j < stringArray2.length; ++j) {
                    this.mHeadersList.get(j).setText((CharSequence)Utils.transfromColors(stringArray2[j]));
                    this.mHeadersList.get(j).setVisibility(0);
                }
                continue;
            }
            this.mRowsList.add(stringArray[i]);
        }
    }

    public void hideWithoutReset() {
        Utils.HideLayout((View)this.mMainLayout, false);
    }

    public /* synthetic */ void lambda$new$0$com-blackrussia-game-gui-dialogs-Dialog(View view) {
        this.sendDialogResponse(1);
    }

    public /* synthetic */ void lambda$new$1$com-blackrussia-game-gui-dialogs-Dialog(View view) {
        this.sendDialogResponse(0);
    }

    public /* synthetic */ boolean lambda$new$2$com-blackrussia-game-gui-dialogs-Dialog(TextView textView, int n, KeyEvent keyEvent) {
        if (n != 6 && n != 5 || (textView = this.mInput.getText()) == null) {
            return false;
        }
        this.mCurrentInputText = textView.toString();
        return false;
    }

    public /* synthetic */ void lambda$new$3$com-blackrussia-game-gui-dialogs-Dialog(View view) {
        this.mInput.requestFocus();
        ((InputMethodManager)NvEventQueueActivity.getInstance().getSystemService("input_method")).showSoftInput((View)this.mInput, 1);
    }

    public /* synthetic */ void lambda$show$4$com-blackrussia-game-gui-dialogs-Dialog(int n, String string2) {
        this.mCurrentListItem = n;
        this.mCurrentInputText = string2;
    }

    public /* synthetic */ void lambda$show$5$com-blackrussia-game-gui-dialogs-Dialog() {
        this.sendDialogResponse(1);
    }

    public void onHeightChanged(int n) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mMainLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, n);
        this.mMainLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void sendDialogResponse(int n) {
        if (!this.mCurrentInputText.equals(this.mInput.getText().toString())) {
            this.mCurrentInputText = this.mInput.getText().toString();
        }
        ((InputMethodManager)NvEventQueueActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(this.mInput.getWindowToken(), 0);
        try {
            NvEventQueueActivity.getInstance().sendDialogResponse(n, this.mCurrentDialogId, this.mCurrentListItem, this.mCurrentInputText.getBytes("windows-1251"));
            Utils.HideLayout((View)this.mMainLayout, true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
    }

    public void show(int n, int n2, String string2, String string3, String string4, String string5) {
        this.clearDialogData();
        this.mCurrentDialogId = n;
        this.mCurrentDialogTypeId = n2;
        if (n2 == 0) {
            this.mInputLayout.setVisibility(8);
            this.mListLayout.setVisibility(8);
            this.mMsgBoxLayout.setVisibility(0);
        } else if (n2 != 1 && n2 != 3) {
            this.mInputLayout.setVisibility(8);
            this.mMsgBoxLayout.setVisibility(8);
            this.mListLayout.setVisibility(0);
            this.loadTabList(string3);
            Object object = Utils.fixFieldsForDialog(this.mRowsList);
            this.mRowsList = object;
            object = new DialogAdapter((ArrayList<String>)object, this.mHeadersList);
            ((DialogAdapter)object).setOnClickListener(new Dialog$$ExternalSyntheticLambda4(this));
            ((DialogAdapter)object).setOnDoubleClickListener(new Dialog$$ExternalSyntheticLambda5(this));
            this.mCustomRecyclerView.setLayoutManager(new LinearLayoutManager((Context)NvEventQueueActivity.getInstance()));
            this.mCustomRecyclerView.setAdapter((RecyclerView.Adapter)object);
            if (n2 != 2) {
                CustomRecyclerView customRecyclerView = this.mCustomRecyclerView;
                object.getClass();
                customRecyclerView.post(new Dialog$$ExternalSyntheticLambda6((DialogAdapter)object));
            }
        } else {
            this.mInputLayout.setVisibility(0);
            this.mMsgBoxLayout.setVisibility(0);
            this.mListLayout.setVisibility(8);
        }
        this.mCaption.setText((CharSequence)Utils.transfromColors(string2));
        this.mContent.setText((CharSequence)Utils.transfromColors(string3));
        ((TextView)this.mLeftBtn.getChildAt(0)).setText((CharSequence)Utils.transfromColors(string4));
        ((TextView)this.mRightBtn.getChildAt(0)).setText((CharSequence)Utils.transfromColors(string5));
        if (string5.equals("")) {
            this.mRightBtn.setVisibility(8);
        } else {
            this.mRightBtn.setVisibility(0);
        }
        Utils.ShowLayout((View)this.mMainLayout, true);
    }

    public void showWithOldContent() {
        Utils.ShowLayout((View)this.mMainLayout, false);
    }
}

