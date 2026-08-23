/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.view.View
 *  android.view.ViewPropertyAnimator
 */
package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import java.util.ArrayList;
import java.util.List;

public class DefaultItemAnimator
extends SimpleItemAnimator {
    private static final boolean DEBUG = false;
    private static TimeInterpolator sDefaultInterpolator;
    ArrayList<RecyclerView.ViewHolder> mAddAnimations;
    ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList;
    ArrayList<RecyclerView.ViewHolder> mChangeAnimations;
    ArrayList<ArrayList<ChangeInfo>> mChangesList;
    ArrayList<RecyclerView.ViewHolder> mMoveAnimations;
    ArrayList<ArrayList<MoveInfo>> mMovesList;
    private ArrayList<RecyclerView.ViewHolder> mPendingAdditions;
    private ArrayList<ChangeInfo> mPendingChanges;
    private ArrayList<MoveInfo> mPendingMoves;
    private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList();
    ArrayList<RecyclerView.ViewHolder> mRemoveAnimations;

    public DefaultItemAnimator() {
        this.mPendingAdditions = new ArrayList();
        this.mPendingMoves = new ArrayList();
        this.mPendingChanges = new ArrayList();
        this.mAdditionsList = new ArrayList();
        this.mMovesList = new ArrayList();
        this.mChangesList = new ArrayList();
        this.mAddAnimations = new ArrayList();
        this.mMoveAnimations = new ArrayList();
        this.mRemoveAnimations = new ArrayList();
        this.mChangeAnimations = new ArrayList();
    }

    private void animateRemoveImpl(RecyclerView.ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        ViewPropertyAnimator viewPropertyAnimator = view.animate();
        this.mRemoveAnimations.add(viewHolder);
        viewPropertyAnimator.setDuration(this.getRemoveDuration()).alpha(0.0f).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewHolder, viewPropertyAnimator, view){
            final DefaultItemAnimator this$0;
            final ViewPropertyAnimator val$animation;
            final RecyclerView.ViewHolder val$holder;
            final View val$view;
            {
                this.this$0 = defaultItemAnimator;
                this.val$holder = viewHolder;
                this.val$animation = viewPropertyAnimator;
                this.val$view = view;
            }

            public void onAnimationEnd(Animator animator2) {
                this.val$animation.setListener(null);
                this.val$view.setAlpha(1.0f);
                this.this$0.dispatchRemoveFinished(this.val$holder);
                this.this$0.mRemoveAnimations.remove(this.val$holder);
                this.this$0.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator animator2) {
                this.this$0.dispatchRemoveStarting(this.val$holder);
            }
        }).start();
    }

    private void endChangeAnimation(List<ChangeInfo> list, RecyclerView.ViewHolder viewHolder) {
        for (int i = list.size() - 1; i >= 0; --i) {
            ChangeInfo changeInfo = list.get(i);
            if (!this.endChangeAnimationIfNecessary(changeInfo, viewHolder) || changeInfo.oldHolder != null || changeInfo.newHolder != null) continue;
            list.remove(changeInfo);
        }
    }

    private void endChangeAnimationIfNecessary(ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }

    private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, RecyclerView.ViewHolder viewHolder) {
        block4: {
            boolean bl;
            block3: {
                block2: {
                    bl = false;
                    if (changeInfo.newHolder != viewHolder) break block2;
                    changeInfo.newHolder = null;
                    break block3;
                }
                if (changeInfo.oldHolder != viewHolder) break block4;
                changeInfo.oldHolder = null;
                bl = true;
            }
            viewHolder.itemView.setAlpha(1.0f);
            viewHolder.itemView.setTranslationX(0.0f);
            viewHolder.itemView.setTranslationY(0.0f);
            this.dispatchChangeFinished(viewHolder, bl);
            return true;
        }
        return false;
    }

    private void resetAnimation(RecyclerView.ViewHolder viewHolder) {
        if (sDefaultInterpolator == null) {
            sDefaultInterpolator = new ValueAnimator().getInterpolator();
        }
        viewHolder.itemView.animate().setInterpolator(sDefaultInterpolator);
        this.endAnimation(viewHolder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        this.resetAnimation(viewHolder);
        viewHolder.itemView.setAlpha(0.0f);
        this.mPendingAdditions.add(viewHolder);
        return true;
    }

    void animateAddImpl(RecyclerView.ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        ViewPropertyAnimator viewPropertyAnimator = view.animate();
        this.mAddAnimations.add(viewHolder);
        viewPropertyAnimator.alpha(1.0f).setDuration(this.getAddDuration()).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewHolder, view, viewPropertyAnimator){
            final DefaultItemAnimator this$0;
            final ViewPropertyAnimator val$animation;
            final RecyclerView.ViewHolder val$holder;
            final View val$view;
            {
                this.this$0 = defaultItemAnimator;
                this.val$holder = viewHolder;
                this.val$view = view;
                this.val$animation = viewPropertyAnimator;
            }

            public void onAnimationCancel(Animator animator2) {
                this.val$view.setAlpha(1.0f);
            }

            public void onAnimationEnd(Animator animator2) {
                this.val$animation.setListener(null);
                this.this$0.dispatchAddFinished(this.val$holder);
                this.this$0.mAddAnimations.remove(this.val$holder);
                this.this$0.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator animator2) {
                this.this$0.dispatchAddStarting(this.val$holder);
            }
        }).start();
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int n, int n2, int n3, int n4) {
        if (viewHolder == viewHolder2) {
            return this.animateMove(viewHolder, n, n2, n3, n4);
        }
        float f = viewHolder.itemView.getTranslationX();
        float f2 = viewHolder.itemView.getTranslationY();
        float f3 = viewHolder.itemView.getAlpha();
        this.resetAnimation(viewHolder);
        int n5 = (int)((float)(n3 - n) - f);
        int n6 = (int)((float)(n4 - n2) - f2);
        viewHolder.itemView.setTranslationX(f);
        viewHolder.itemView.setTranslationY(f2);
        viewHolder.itemView.setAlpha(f3);
        if (viewHolder2 != null) {
            this.resetAnimation(viewHolder2);
            viewHolder2.itemView.setTranslationX((float)(-n5));
            viewHolder2.itemView.setTranslationY((float)(-n6));
            viewHolder2.itemView.setAlpha(0.0f);
        }
        this.mPendingChanges.add(new ChangeInfo(viewHolder, viewHolder2, n, n2, n3, n4));
        return true;
    }

    void animateChangeImpl(ChangeInfo changeInfo) {
        RecyclerView.ViewHolder viewHolder = changeInfo.oldHolder;
        View view = null;
        viewHolder = viewHolder == null ? null : viewHolder.itemView;
        RecyclerView.ViewHolder viewHolder2 = changeInfo.newHolder;
        if (viewHolder2 != null) {
            view = viewHolder2.itemView;
        }
        if (viewHolder != null) {
            viewHolder2 = viewHolder.animate().setDuration(this.getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            viewHolder2.translationX(changeInfo.toX - changeInfo.fromX);
            viewHolder2.translationY(changeInfo.toY - changeInfo.fromY);
            viewHolder2.alpha(0.0f).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, changeInfo, (ViewPropertyAnimator)viewHolder2, (View)viewHolder){
                final DefaultItemAnimator this$0;
                final ChangeInfo val$changeInfo;
                final ViewPropertyAnimator val$oldViewAnim;
                final View val$view;
                {
                    this.this$0 = defaultItemAnimator;
                    this.val$changeInfo = changeInfo;
                    this.val$oldViewAnim = viewPropertyAnimator;
                    this.val$view = view;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$oldViewAnim.setListener(null);
                    this.val$view.setAlpha(1.0f);
                    this.val$view.setTranslationX(0.0f);
                    this.val$view.setTranslationY(0.0f);
                    this.this$0.dispatchChangeFinished(this.val$changeInfo.oldHolder, true);
                    this.this$0.mChangeAnimations.remove(this.val$changeInfo.oldHolder);
                    this.this$0.dispatchFinishedWhenDone();
                }

                public void onAnimationStart(Animator animator2) {
                    this.this$0.dispatchChangeStarting(this.val$changeInfo.oldHolder, true);
                }
            }).start();
        }
        if (view != null) {
            viewHolder = view.animate();
            this.mChangeAnimations.add(changeInfo.newHolder);
            viewHolder.translationX(0.0f).translationY(0.0f).setDuration(this.getChangeDuration()).alpha(1.0f).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, changeInfo, (ViewPropertyAnimator)viewHolder, view){
                final DefaultItemAnimator this$0;
                final ChangeInfo val$changeInfo;
                final View val$newView;
                final ViewPropertyAnimator val$newViewAnimation;
                {
                    this.this$0 = defaultItemAnimator;
                    this.val$changeInfo = changeInfo;
                    this.val$newViewAnimation = viewPropertyAnimator;
                    this.val$newView = view;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$newViewAnimation.setListener(null);
                    this.val$newView.setAlpha(1.0f);
                    this.val$newView.setTranslationX(0.0f);
                    this.val$newView.setTranslationY(0.0f);
                    this.this$0.dispatchChangeFinished(this.val$changeInfo.newHolder, false);
                    this.this$0.mChangeAnimations.remove(this.val$changeInfo.newHolder);
                    this.this$0.dispatchFinishedWhenDone();
                }

                public void onAnimationStart(Animator animator2) {
                    this.this$0.dispatchChangeStarting(this.val$changeInfo.newHolder, false);
                }
            }).start();
        }
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, int n, int n2, int n3, int n4) {
        View view = viewHolder.itemView;
        int n5 = n2 + (int)viewHolder.itemView.getTranslationY();
        this.resetAnimation(viewHolder);
        int n6 = n3 - (n += (int)viewHolder.itemView.getTranslationX());
        n2 = n4 - n5;
        if (n6 == 0 && n2 == 0) {
            this.dispatchMoveFinished(viewHolder);
            return false;
        }
        if (n6 != 0) {
            view.setTranslationX((float)(-n6));
        }
        if (n2 != 0) {
            view.setTranslationY((float)(-n2));
        }
        this.mPendingMoves.add(new MoveInfo(viewHolder, n, n5, n3, n4));
        return true;
    }

    void animateMoveImpl(RecyclerView.ViewHolder viewHolder, int n, int n2, int n3, int n4) {
        View view = viewHolder.itemView;
        n = n3 - n;
        n2 = n4 - n2;
        if (n != 0) {
            view.animate().translationX(0.0f);
        }
        if (n2 != 0) {
            view.animate().translationY(0.0f);
        }
        ViewPropertyAnimator viewPropertyAnimator = view.animate();
        this.mMoveAnimations.add(viewHolder);
        viewPropertyAnimator.setDuration(this.getMoveDuration()).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewHolder, n, view, n2, viewPropertyAnimator){
            final DefaultItemAnimator this$0;
            final ViewPropertyAnimator val$animation;
            final int val$deltaX;
            final int val$deltaY;
            final RecyclerView.ViewHolder val$holder;
            final View val$view;
            {
                this.this$0 = defaultItemAnimator;
                this.val$holder = viewHolder;
                this.val$deltaX = n;
                this.val$view = view;
                this.val$deltaY = n2;
                this.val$animation = viewPropertyAnimator;
            }

            public void onAnimationCancel(Animator animator2) {
                if (this.val$deltaX != 0) {
                    this.val$view.setTranslationX(0.0f);
                }
                if (this.val$deltaY != 0) {
                    this.val$view.setTranslationY(0.0f);
                }
            }

            public void onAnimationEnd(Animator animator2) {
                this.val$animation.setListener(null);
                this.this$0.dispatchMoveFinished(this.val$holder);
                this.this$0.mMoveAnimations.remove(this.val$holder);
                this.this$0.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator animator2) {
                this.this$0.dispatchMoveStarting(this.val$holder);
            }
        }).start();
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        this.resetAnimation(viewHolder);
        this.mPendingRemovals.add(viewHolder);
        return true;
    }

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder, List<Object> list) {
        boolean bl = !list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list);
        return bl;
    }

    void cancelAll(List<RecyclerView.ViewHolder> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            list.get((int)i).itemView.animate().cancel();
        }
    }

    void dispatchFinishedWhenDone() {
        if (!this.isRunning()) {
            this.dispatchAnimationsFinished();
        }
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        ArrayList<Object> arrayList;
        int n;
        View view = viewHolder.itemView;
        view.animate().cancel();
        for (n = this.mPendingMoves.size() - 1; n >= 0; --n) {
            if (this.mPendingMoves.get((int)n).holder != viewHolder) continue;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            this.dispatchMoveFinished(viewHolder);
            this.mPendingMoves.remove(n);
        }
        this.endChangeAnimation(this.mPendingChanges, viewHolder);
        if (this.mPendingRemovals.remove(viewHolder)) {
            view.setAlpha(1.0f);
            this.dispatchRemoveFinished(viewHolder);
        }
        if (this.mPendingAdditions.remove(viewHolder)) {
            view.setAlpha(1.0f);
            this.dispatchAddFinished(viewHolder);
        }
        for (n = this.mChangesList.size() - 1; n >= 0; --n) {
            arrayList = this.mChangesList.get(n);
            this.endChangeAnimation(arrayList, viewHolder);
            if (!arrayList.isEmpty()) continue;
            this.mChangesList.remove(n);
        }
        block2: for (n = this.mMovesList.size() - 1; n >= 0; --n) {
            arrayList = this.mMovesList.get(n);
            for (int i = arrayList.size() - 1; i >= 0; --i) {
                if (((MoveInfo)arrayList.get((int)i)).holder != viewHolder) continue;
                view.setTranslationY(0.0f);
                view.setTranslationX(0.0f);
                this.dispatchMoveFinished(viewHolder);
                arrayList.remove(i);
                if (!arrayList.isEmpty()) continue block2;
                this.mMovesList.remove(n);
                continue block2;
            }
        }
        for (n = this.mAdditionsList.size() - 1; n >= 0; --n) {
            arrayList = this.mAdditionsList.get(n);
            if (!arrayList.remove(viewHolder)) continue;
            view.setAlpha(1.0f);
            this.dispatchAddFinished(viewHolder);
            if (!arrayList.isEmpty()) continue;
            this.mAdditionsList.remove(n);
        }
        this.mRemoveAnimations.remove(viewHolder);
        this.mAddAnimations.remove(viewHolder);
        this.mChangeAnimations.remove(viewHolder);
        this.mMoveAnimations.remove(viewHolder);
        this.dispatchFinishedWhenDone();
    }

    @Override
    public void endAnimations() {
        int n;
        Object object;
        Object object2;
        int n2;
        for (n2 = this.mPendingMoves.size() - 1; n2 >= 0; --n2) {
            object2 = this.mPendingMoves.get(n2);
            object = ((MoveInfo)object2).holder.itemView;
            object.setTranslationY(0.0f);
            object.setTranslationX(0.0f);
            this.dispatchMoveFinished(((MoveInfo)object2).holder);
            this.mPendingMoves.remove(n2);
        }
        for (n2 = this.mPendingRemovals.size() - 1; n2 >= 0; --n2) {
            this.dispatchRemoveFinished(this.mPendingRemovals.get(n2));
            this.mPendingRemovals.remove(n2);
        }
        for (n2 = this.mPendingAdditions.size() - 1; n2 >= 0; --n2) {
            object = this.mPendingAdditions.get(n2);
            ((RecyclerView.ViewHolder)object).itemView.setAlpha(1.0f);
            this.dispatchAddFinished((RecyclerView.ViewHolder)object);
            this.mPendingAdditions.remove(n2);
        }
        for (n2 = this.mPendingChanges.size() - 1; n2 >= 0; --n2) {
            this.endChangeAnimationIfNecessary(this.mPendingChanges.get(n2));
        }
        this.mPendingChanges.clear();
        if (!this.isRunning()) {
            return;
        }
        for (n2 = this.mMovesList.size() - 1; n2 >= 0; --n2) {
            object = this.mMovesList.get(n2);
            for (n = ((ArrayList)object).size() - 1; n >= 0; --n) {
                MoveInfo moveInfo = (MoveInfo)((ArrayList)object).get(n);
                object2 = moveInfo.holder.itemView;
                object2.setTranslationY(0.0f);
                object2.setTranslationX(0.0f);
                this.dispatchMoveFinished(moveInfo.holder);
                ((ArrayList)object).remove(n);
                if (!((ArrayList)object).isEmpty()) continue;
                this.mMovesList.remove(object);
            }
        }
        for (n2 = this.mAdditionsList.size() - 1; n2 >= 0; --n2) {
            object = this.mAdditionsList.get(n2);
            for (n = ((ArrayList)object).size() - 1; n >= 0; --n) {
                object2 = (RecyclerView.ViewHolder)((ArrayList)object).get(n);
                ((RecyclerView.ViewHolder)object2).itemView.setAlpha(1.0f);
                this.dispatchAddFinished((RecyclerView.ViewHolder)object2);
                ((ArrayList)object).remove(n);
                if (!((ArrayList)object).isEmpty()) continue;
                this.mAdditionsList.remove(object);
            }
        }
        for (n2 = this.mChangesList.size() - 1; n2 >= 0; --n2) {
            object = this.mChangesList.get(n2);
            for (n = ((ArrayList)object).size() - 1; n >= 0; --n) {
                this.endChangeAnimationIfNecessary((ChangeInfo)((ArrayList)object).get(n));
                if (!((ArrayList)object).isEmpty()) continue;
                this.mChangesList.remove(object);
            }
        }
        this.cancelAll(this.mRemoveAnimations);
        this.cancelAll(this.mMoveAnimations);
        this.cancelAll(this.mAddAnimations);
        this.cancelAll(this.mChangeAnimations);
        this.dispatchAnimationsFinished();
    }

    @Override
    public boolean isRunning() {
        boolean bl = !(this.mPendingAdditions.isEmpty() && this.mPendingChanges.isEmpty() && this.mPendingMoves.isEmpty() && this.mPendingRemovals.isEmpty() && this.mMoveAnimations.isEmpty() && this.mRemoveAnimations.isEmpty() && this.mAddAnimations.isEmpty() && this.mChangeAnimations.isEmpty() && this.mMovesList.isEmpty() && this.mAdditionsList.isEmpty() && this.mChangesList.isEmpty());
        return bl;
    }

    @Override
    public void runPendingAnimations() {
        Object object;
        boolean bl = this.mPendingRemovals.isEmpty() ^ true;
        boolean bl2 = this.mPendingMoves.isEmpty() ^ true;
        boolean bl3 = this.mPendingChanges.isEmpty() ^ true;
        boolean bl4 = this.mPendingAdditions.isEmpty() ^ true;
        if (!(bl || bl2 || bl4 || bl3)) {
            return;
        }
        Object object2 = this.mPendingRemovals.iterator();
        while (object2.hasNext()) {
            this.animateRemoveImpl(object2.next());
        }
        this.mPendingRemovals.clear();
        if (bl2) {
            object = new ArrayList<Object>();
            ((ArrayList)object).addAll(this.mPendingMoves);
            this.mMovesList.add((ArrayList<MoveInfo>)object);
            this.mPendingMoves.clear();
            object2 = new Runnable(this, (ArrayList)object){
                final DefaultItemAnimator this$0;
                final ArrayList val$moves;
                {
                    this.this$0 = defaultItemAnimator;
                    this.val$moves = arrayList;
                }

                @Override
                public void run() {
                    for (MoveInfo moveInfo : this.val$moves) {
                        this.this$0.animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY);
                    }
                    this.val$moves.clear();
                    this.this$0.mMovesList.remove(this.val$moves);
                }
            };
            if (bl) {
                ViewCompat.postOnAnimationDelayed(((MoveInfo)((ArrayList)object).get((int)0)).holder.itemView, (Runnable)object2, this.getRemoveDuration());
            } else {
                object2.run();
            }
        }
        if (bl3) {
            object2 = new ArrayList();
            ((ArrayList)object2).addAll(this.mPendingChanges);
            this.mChangesList.add((ArrayList<ChangeInfo>)object2);
            this.mPendingChanges.clear();
            object = new Runnable(this, (ArrayList)object2){
                final DefaultItemAnimator this$0;
                final ArrayList val$changes;
                {
                    this.this$0 = defaultItemAnimator;
                    this.val$changes = arrayList;
                }

                @Override
                public void run() {
                    for (ChangeInfo changeInfo : this.val$changes) {
                        this.this$0.animateChangeImpl(changeInfo);
                    }
                    this.val$changes.clear();
                    this.this$0.mChangesList.remove(this.val$changes);
                }
            };
            if (bl) {
                ViewCompat.postOnAnimationDelayed(((ChangeInfo)((ArrayList)object2).get((int)0)).oldHolder.itemView, (Runnable)object, this.getRemoveDuration());
            } else {
                object.run();
            }
        }
        if (bl4) {
            object = new ArrayList();
            ((ArrayList)object).addAll(this.mPendingAdditions);
            this.mAdditionsList.add((ArrayList<RecyclerView.ViewHolder>)object);
            this.mPendingAdditions.clear();
            object2 = new Runnable(this, (ArrayList)object){
                final DefaultItemAnimator this$0;
                final ArrayList val$additions;
                {
                    this.this$0 = defaultItemAnimator;
                    this.val$additions = arrayList;
                }

                @Override
                public void run() {
                    for (RecyclerView.ViewHolder viewHolder : this.val$additions) {
                        this.this$0.animateAddImpl(viewHolder);
                    }
                    this.val$additions.clear();
                    this.this$0.mAdditionsList.remove(this.val$additions);
                }
            };
            if (!(bl || bl2 || bl3)) {
                object2.run();
            } else {
                long l = 0L;
                long l2 = bl ? this.getRemoveDuration() : 0L;
                long l3 = bl2 ? this.getMoveDuration() : 0L;
                if (bl3) {
                    l = this.getChangeDuration();
                }
                l3 = Math.max(l3, l);
                ViewCompat.postOnAnimationDelayed(((RecyclerView.ViewHolder)((ArrayList)object).get((int)0)).itemView, (Runnable)object2, l3 + l2);
            }
        }
    }

    private static class ChangeInfo {
        public int fromX;
        public int fromY;
        public RecyclerView.ViewHolder newHolder;
        public RecyclerView.ViewHolder oldHolder;
        public int toX;
        public int toY;

        private ChangeInfo(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            this.oldHolder = viewHolder;
            this.newHolder = viewHolder2;
        }

        ChangeInfo(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int n, int n2, int n3, int n4) {
            this(viewHolder, viewHolder2);
            this.fromX = n;
            this.fromY = n2;
            this.toX = n3;
            this.toY = n4;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ChangeInfo{oldHolder=");
            stringBuilder.append(this.oldHolder);
            stringBuilder.append(", newHolder=");
            stringBuilder.append(this.newHolder);
            stringBuilder.append(", fromX=");
            stringBuilder.append(this.fromX);
            stringBuilder.append(", fromY=");
            stringBuilder.append(this.fromY);
            stringBuilder.append(", toX=");
            stringBuilder.append(this.toX);
            stringBuilder.append(", toY=");
            stringBuilder.append(this.toY);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    private static class MoveInfo {
        public int fromX;
        public int fromY;
        public RecyclerView.ViewHolder holder;
        public int toX;
        public int toY;

        MoveInfo(RecyclerView.ViewHolder viewHolder, int n, int n2, int n3, int n4) {
            this.holder = viewHolder;
            this.fromX = n;
            this.fromY = n2;
            this.toX = n3;
            this.toY = n4;
        }
    }
}

