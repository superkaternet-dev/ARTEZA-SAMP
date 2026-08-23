/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnLongClickListener
 *  android.view.View$OnTouchListener
 */
package androidx.core.view;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.MotionEventCompat;

public class DragStartHelper {
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View.OnLongClickListener mLongClickListener = new View.OnLongClickListener(this){
        final DragStartHelper this$0;
        {
            this.this$0 = dragStartHelper;
        }

        public boolean onLongClick(View view) {
            return this.this$0.onLongClick(view);
        }
    };
    private final View.OnTouchListener mTouchListener = new View.OnTouchListener(this){
        final DragStartHelper this$0;
        {
            this.this$0 = dragStartHelper;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return this.this$0.onTouch(view, motionEvent);
        }
    };
    private final View mView;

    public DragStartHelper(View view, OnDragStartListener onDragStartListener) {
        this.mView = view;
        this.mListener = onDragStartListener;
    }

    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }

    public void detach() {
        this.mView.setOnLongClickListener(null);
        this.mView.setOnTouchListener(null);
    }

    public void getTouchPosition(Point point) {
        point.set(this.mLastTouchX, this.mLastTouchY);
    }

    public boolean onLongClick(View view) {
        return this.mListener.onDragStart(view, this);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int n = (int)motionEvent.getX();
        int n2 = (int)motionEvent.getY();
        switch (motionEvent.getAction()) {
            default: {
                break;
            }
            case 2: {
                boolean bl;
                if (!MotionEventCompat.isFromSource(motionEvent, 8194) || (motionEvent.getButtonState() & 1) == 0 || this.mDragging || this.mLastTouchX == n && this.mLastTouchY == n2) break;
                this.mLastTouchX = n;
                this.mLastTouchY = n2;
                this.mDragging = bl = this.mListener.onDragStart(view, this);
                return bl;
            }
            case 1: 
            case 3: {
                this.mDragging = false;
                break;
            }
            case 0: {
                this.mLastTouchX = n;
                this.mLastTouchY = n2;
            }
        }
        return false;
    }

    public static interface OnDragStartListener {
        public boolean onDragStart(View var1, DragStartHelper var2);
    }
}

