/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.widget.ImageView
 */
package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import androidx.core.widget.TintableImageSourceView;

public class ImageViewCompat {
    private ImageViewCompat() {
    }

    public static ColorStateList getImageTintList(ImageView object) {
        if (Build.VERSION.SDK_INT >= 21) {
            return object.getImageTintList();
        }
        object = object instanceof TintableImageSourceView ? ((TintableImageSourceView)object).getSupportImageTintList() : null;
        return object;
    }

    public static PorterDuff.Mode getImageTintMode(ImageView object) {
        if (Build.VERSION.SDK_INT >= 21) {
            return object.getImageTintMode();
        }
        object = object instanceof TintableImageSourceView ? ((TintableImageSourceView)object).getSupportImageTintMode() : null;
        return object;
    }

    public static void setImageTintList(ImageView imageView, ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT >= 21) {
            imageView.setImageTintList(colorStateList);
            if (Build.VERSION.SDK_INT == 21 && (colorStateList = imageView.getDrawable()) != null && imageView.getImageTintList() != null) {
                if (colorStateList.isStateful()) {
                    colorStateList.setState(imageView.getDrawableState());
                }
                imageView.setImageDrawable((Drawable)colorStateList);
            }
        } else if (imageView instanceof TintableImageSourceView) {
            ((TintableImageSourceView)imageView).setSupportImageTintList(colorStateList);
        }
    }

    public static void setImageTintMode(ImageView imageView, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            imageView.setImageTintMode(mode);
            if (Build.VERSION.SDK_INT == 21 && (mode = imageView.getDrawable()) != null && imageView.getImageTintList() != null) {
                if (mode.isStateful()) {
                    mode.setState(imageView.getDrawableState());
                }
                imageView.setImageDrawable((Drawable)mode);
            }
        } else if (imageView instanceof TintableImageSourceView) {
            ((TintableImageSourceView)imageView).setSupportImageTintMode(mode);
        }
    }
}

