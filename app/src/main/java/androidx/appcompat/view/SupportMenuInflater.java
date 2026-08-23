/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.InflateException
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.appcompat.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SupportMenuInflater
extends MenuInflater {
    static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
    static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
    static final String LOG_TAG = "SupportMenuInflater";
    static final int NO_ID = 0;
    private static final String XML_GROUP = "group";
    private static final String XML_ITEM = "item";
    private static final String XML_MENU = "menu";
    final Object[] mActionProviderConstructorArguments;
    final Object[] mActionViewConstructorArguments;
    Context mContext;
    private Object mRealOwner;

    static {
        Class[] classArray = new Class[]{Context.class};
        ACTION_VIEW_CONSTRUCTOR_SIGNATURE = classArray;
        ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = classArray;
    }

    public SupportMenuInflater(Context context) {
        super(context);
        this.mContext = context;
        Object[] objectArray = new Object[]{context};
        this.mActionViewConstructorArguments = objectArray;
        this.mActionProviderConstructorArguments = objectArray;
    }

    private Object findRealOwner(Object object) {
        if (object instanceof Activity) {
            return object;
        }
        if (object instanceof ContextWrapper) {
            return this.findRealOwner(((ContextWrapper)object).getBaseContext());
        }
        return object;
    }

    private void parseMenu(XmlPullParser object, AttributeSet attributeSet, Menu object2) throws XmlPullParserException, IOException {
        int n;
        MenuState menuState = new MenuState(this, (Menu)object2);
        int n2 = object.getEventType();
        int n3 = 0;
        Object object3 = null;
        do {
            if (n2 == 2) {
                object2 = object.getName();
                if (((String)object2).equals(XML_MENU)) {
                    n = object.next();
                    break;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Expecting menu, got ");
                ((StringBuilder)object).append((String)object2);
                throw new RuntimeException(((StringBuilder)object).toString());
            }
            n2 = n = object.next();
        } while (n != 1);
        n2 = 0;
        int n4 = n;
        while (n2 == 0) {
            int n5;
            switch (n4) {
                default: {
                    n = n3;
                    object2 = object3;
                    n5 = n2;
                    break;
                }
                case 3: {
                    String string2 = object.getName();
                    if (n3 != 0 && string2.equals(object3)) {
                        n = 0;
                        object2 = null;
                        n5 = n2;
                        break;
                    }
                    if (string2.equals(XML_GROUP)) {
                        menuState.resetGroup();
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        break;
                    }
                    if (string2.equals(XML_ITEM)) {
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        if (menuState.hasAddedItem()) break;
                        if (menuState.itemActionProvider != null && menuState.itemActionProvider.hasSubMenu()) {
                            menuState.addSubMenuItem();
                            n = n3;
                            object2 = object3;
                            n5 = n2;
                            break;
                        }
                        menuState.addItem();
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        break;
                    }
                    n = n3;
                    object2 = object3;
                    n5 = n2;
                    if (!string2.equals(XML_MENU)) break;
                    n5 = 1;
                    n = n3;
                    object2 = object3;
                    break;
                }
                case 2: {
                    if (n3 != 0) {
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        break;
                    }
                    object2 = object.getName();
                    if (((String)object2).equals(XML_GROUP)) {
                        menuState.readGroup(attributeSet);
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        break;
                    }
                    if (((String)object2).equals(XML_ITEM)) {
                        menuState.readItem(attributeSet);
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        break;
                    }
                    if (((String)object2).equals(XML_MENU)) {
                        this.parseMenu((XmlPullParser)object, attributeSet, (Menu)menuState.addSubMenuItem());
                        n = n3;
                        object2 = object3;
                        n5 = n2;
                        break;
                    }
                    n = 1;
                    n5 = n2;
                    break;
                }
                case 1: {
                    throw new RuntimeException("Unexpected end of document");
                }
            }
            n4 = object.next();
            n3 = n;
            object3 = object2;
            n2 = n5;
        }
    }

    Object getRealOwner() {
        if (this.mRealOwner == null) {
            this.mRealOwner = this.findRealOwner(this.mContext);
        }
        return this.mRealOwner;
    }

    /*
     * Exception decompiling
     */
    public void inflate(int var1_1, Menu var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 2[TRYBLOCK] [6 : 80->84)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static class InflatedOnMenuItemClickListener
    implements MenuItem.OnMenuItemClickListener {
        private static final Class<?>[] PARAM_TYPES = new Class[]{MenuItem.class};
        private Method mMethod;
        private Object mRealOwner;

        public InflatedOnMenuItemClickListener(Object object, String string2) {
            this.mRealOwner = object;
            Class<?> clazz = object.getClass();
            try {
                this.mMethod = clazz.getMethod(string2, PARAM_TYPES);
                return;
            }
            catch (Exception exception) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't resolve menu item onClick handler ");
                stringBuilder.append(string2);
                stringBuilder.append(" in class ");
                stringBuilder.append(clazz.getName());
                string2 = new InflateException(stringBuilder.toString());
                string2.initCause((Throwable)exception);
                throw string2;
            }
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            try {
                if (this.mMethod.getReturnType() == Boolean.TYPE) {
                    return (Boolean)this.mMethod.invoke(this.mRealOwner, menuItem);
                }
                this.mMethod.invoke(this.mRealOwner, menuItem);
                return true;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private class MenuState {
        private static final int defaultGroupId = 0;
        private static final int defaultItemCategory = 0;
        private static final int defaultItemCheckable = 0;
        private static final boolean defaultItemChecked = false;
        private static final boolean defaultItemEnabled = true;
        private static final int defaultItemId = 0;
        private static final int defaultItemOrder = 0;
        private static final boolean defaultItemVisible = true;
        private int groupCategory;
        private int groupCheckable;
        private boolean groupEnabled;
        private int groupId;
        private int groupOrder;
        private boolean groupVisible;
        ActionProvider itemActionProvider;
        private String itemActionProviderClassName;
        private String itemActionViewClassName;
        private int itemActionViewLayout;
        private boolean itemAdded;
        private int itemAlphabeticModifiers;
        private char itemAlphabeticShortcut;
        private int itemCategoryOrder;
        private int itemCheckable;
        private boolean itemChecked;
        private CharSequence itemContentDescription;
        private boolean itemEnabled;
        private int itemIconResId;
        private ColorStateList itemIconTintList;
        private PorterDuff.Mode itemIconTintMode;
        private int itemId;
        private String itemListenerMethodName;
        private int itemNumericModifiers;
        private char itemNumericShortcut;
        private int itemShowAsAction;
        private CharSequence itemTitle;
        private CharSequence itemTitleCondensed;
        private CharSequence itemTooltipText;
        private boolean itemVisible;
        private Menu menu;
        final SupportMenuInflater this$0;

        public MenuState(SupportMenuInflater supportMenuInflater, Menu menu) {
            this.this$0 = supportMenuInflater;
            this.itemIconTintList = null;
            this.itemIconTintMode = null;
            this.menu = menu;
            this.resetGroup();
        }

        private char getShortcut(String string2) {
            if (string2 == null) {
                return '\u0000';
            }
            return string2.charAt(0);
        }

        private <T> T newInstance(String string2, Class<?>[] object, Object[] objectArray) {
            try {
                object = Class.forName(string2, false, this.this$0.mContext.getClassLoader()).getConstructor((Class<?>)object);
                object.setAccessible(true);
                object = object.newInstance(objectArray);
            }
            catch (Exception exception) {
                object = new StringBuilder();
                object.append("Cannot instantiate class: ");
                object.append(string2);
                Log.w((String)SupportMenuInflater.LOG_TAG, (String)object.toString(), (Throwable)exception);
                return null;
            }
            return (T)object;
        }

        private void setItem(MenuItem menuItem) {
            int n;
            Object object = menuItem.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
            boolean bl = this.itemCheckable >= 1;
            object.setCheckable(bl).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
            int n2 = this.itemShowAsAction;
            if (n2 >= 0) {
                menuItem.setShowAsAction(n2);
            }
            if (this.itemListenerMethodName != null) {
                if (!this.this$0.mContext.isRestricted()) {
                    menuItem.setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener)new InflatedOnMenuItemClickListener(this.this$0.getRealOwner(), this.itemListenerMethodName));
                } else {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
            }
            if (this.itemCheckable >= 2) {
                if (menuItem instanceof MenuItemImpl) {
                    ((MenuItemImpl)menuItem).setExclusiveCheckable(true);
                } else if (menuItem instanceof MenuItemWrapperICS) {
                    ((MenuItemWrapperICS)menuItem).setExclusiveCheckable(true);
                }
            }
            n2 = 0;
            object = this.itemActionViewClassName;
            if (object != null) {
                menuItem.setActionView((View)this.newInstance((String)object, ACTION_VIEW_CONSTRUCTOR_SIGNATURE, this.this$0.mActionViewConstructorArguments));
                n2 = 1;
            }
            if ((n = this.itemActionViewLayout) > 0) {
                if (n2 == 0) {
                    menuItem.setActionView(n);
                } else {
                    Log.w((String)SupportMenuInflater.LOG_TAG, (String)"Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                }
            }
            if ((object = this.itemActionProvider) != null) {
                MenuItemCompat.setActionProvider(menuItem, (ActionProvider)object);
            }
            MenuItemCompat.setContentDescription(menuItem, this.itemContentDescription);
            MenuItemCompat.setTooltipText(menuItem, this.itemTooltipText);
            MenuItemCompat.setAlphabeticShortcut(menuItem, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
            MenuItemCompat.setNumericShortcut(menuItem, this.itemNumericShortcut, this.itemNumericModifiers);
            object = this.itemIconTintMode;
            if (object != null) {
                MenuItemCompat.setIconTintMode(menuItem, (PorterDuff.Mode)object);
            }
            if ((object = this.itemIconTintList) != null) {
                MenuItemCompat.setIconTintList(menuItem, (ColorStateList)object);
            }
        }

        public void addItem() {
            this.itemAdded = true;
            this.setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
        }

        public SubMenu addSubMenuItem() {
            this.itemAdded = true;
            SubMenu subMenu = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
            this.setItem(subMenu.getItem());
            return subMenu;
        }

        public boolean hasAddedItem() {
            return this.itemAdded;
        }

        public void readGroup(AttributeSet attributeSet) {
            attributeSet = this.this$0.mContext.obtainStyledAttributes(attributeSet, R.styleable.MenuGroup);
            this.groupId = attributeSet.getResourceId(R.styleable.MenuGroup_android_id, 0);
            this.groupCategory = attributeSet.getInt(R.styleable.MenuGroup_android_menuCategory, 0);
            this.groupOrder = attributeSet.getInt(R.styleable.MenuGroup_android_orderInCategory, 0);
            this.groupCheckable = attributeSet.getInt(R.styleable.MenuGroup_android_checkableBehavior, 0);
            this.groupVisible = attributeSet.getBoolean(R.styleable.MenuGroup_android_visible, true);
            this.groupEnabled = attributeSet.getBoolean(R.styleable.MenuGroup_android_enabled, true);
            attributeSet.recycle();
        }

        public void readItem(AttributeSet object) {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.this$0.mContext, object, R.styleable.MenuItem);
            this.itemId = tintTypedArray.getResourceId(R.styleable.MenuItem_android_id, 0);
            this.itemCategoryOrder = 0xFFFF0000 & tintTypedArray.getInt(R.styleable.MenuItem_android_menuCategory, this.groupCategory) | 0xFFFF & tintTypedArray.getInt(R.styleable.MenuItem_android_orderInCategory, this.groupOrder);
            this.itemTitle = tintTypedArray.getText(R.styleable.MenuItem_android_title);
            this.itemTitleCondensed = tintTypedArray.getText(R.styleable.MenuItem_android_titleCondensed);
            this.itemIconResId = tintTypedArray.getResourceId(R.styleable.MenuItem_android_icon, 0);
            this.itemAlphabeticShortcut = this.getShortcut(tintTypedArray.getString(R.styleable.MenuItem_android_alphabeticShortcut));
            this.itemAlphabeticModifiers = tintTypedArray.getInt(R.styleable.MenuItem_alphabeticModifiers, 4096);
            this.itemNumericShortcut = this.getShortcut(tintTypedArray.getString(R.styleable.MenuItem_android_numericShortcut));
            this.itemNumericModifiers = tintTypedArray.getInt(R.styleable.MenuItem_numericModifiers, 4096);
            this.itemCheckable = tintTypedArray.hasValue(R.styleable.MenuItem_android_checkable) ? (int)(tintTypedArray.getBoolean(R.styleable.MenuItem_android_checkable, false) ? 1 : 0) : this.groupCheckable;
            this.itemChecked = tintTypedArray.getBoolean(R.styleable.MenuItem_android_checked, false);
            this.itemVisible = tintTypedArray.getBoolean(R.styleable.MenuItem_android_visible, this.groupVisible);
            this.itemEnabled = tintTypedArray.getBoolean(R.styleable.MenuItem_android_enabled, this.groupEnabled);
            this.itemShowAsAction = tintTypedArray.getInt(R.styleable.MenuItem_showAsAction, -1);
            this.itemListenerMethodName = tintTypedArray.getString(R.styleable.MenuItem_android_onClick);
            this.itemActionViewLayout = tintTypedArray.getResourceId(R.styleable.MenuItem_actionLayout, 0);
            this.itemActionViewClassName = tintTypedArray.getString(R.styleable.MenuItem_actionViewClass);
            object = tintTypedArray.getString(R.styleable.MenuItem_actionProviderClass);
            this.itemActionProviderClassName = object;
            boolean bl = object != null;
            if (bl && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
                this.itemActionProvider = (ActionProvider)this.newInstance((String)object, ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, this.this$0.mActionProviderConstructorArguments);
            } else {
                if (bl) {
                    Log.w((String)SupportMenuInflater.LOG_TAG, (String)"Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.itemActionProvider = null;
            }
            this.itemContentDescription = tintTypedArray.getText(R.styleable.MenuItem_contentDescription);
            this.itemTooltipText = tintTypedArray.getText(R.styleable.MenuItem_tooltipText);
            this.itemIconTintMode = tintTypedArray.hasValue(R.styleable.MenuItem_iconTintMode) ? DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode) : null;
            this.itemIconTintList = tintTypedArray.hasValue(R.styleable.MenuItem_iconTint) ? tintTypedArray.getColorStateList(R.styleable.MenuItem_iconTint) : null;
            tintTypedArray.recycle();
            this.itemAdded = false;
        }

        public void resetGroup() {
            this.groupId = 0;
            this.groupCategory = 0;
            this.groupOrder = 0;
            this.groupCheckable = 0;
            this.groupVisible = true;
            this.groupEnabled = true;
        }
    }
}

