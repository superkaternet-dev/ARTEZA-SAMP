/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.File;
import kotlin.Metadata;
import kotlin.io.FileTreeWalk;
import kotlin.io.FileWalkDirection;
import kotlin.io.FilesKt;
import kotlin.io.FilesKt__FileReadWriteKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u00a8\u0006\u0007"}, d2={"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/io/FilesKt")
class FilesKt__FileTreeWalkKt
extends FilesKt__FileReadWriteKt {
    public static final FileTreeWalk walk(File file, FileWalkDirection fileWalkDirection) {
        Intrinsics.checkNotNullParameter(file, "$this$walk");
        Intrinsics.checkNotNullParameter((Object)fileWalkDirection, "direction");
        return new FileTreeWalk(file, fileWalkDirection);
    }

    public static /* synthetic */ FileTreeWalk walk$default(File file, FileWalkDirection fileWalkDirection, int n, Object object) {
        if ((n & 1) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        return FilesKt.walk(file, fileWalkDirection);
    }

    public static final FileTreeWalk walkBottomUp(File file) {
        Intrinsics.checkNotNullParameter(file, "$this$walkBottomUp");
        return FilesKt.walk(file, FileWalkDirection.BOTTOM_UP);
    }

    public static final FileTreeWalk walkTopDown(File file) {
        Intrinsics.checkNotNullParameter(file, "$this$walkTopDown");
        return FilesKt.walk(file, FileWalkDirection.TOP_DOWN);
    }
}

