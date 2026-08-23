/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.AbstractIterator;
import kotlin.io.AccessDeniedException;
import kotlin.io.FileTreeWalk$FileTreeWalkIterator$WhenMappings;
import kotlin.io.FileWalkDirection;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u001a\u001b\u001cB\u0019\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u0089\u0001\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00128\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\u0002\u0010\u0015J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0096\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0014J\u001a\u0010\u0007\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bJ \u0010\f\u001a\u00020\u00002\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000b0\rJ\u001a\u0010\n\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R@\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011\u00a2\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lkotlin/io/FileTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/io/File;", "start", "direction", "Lkotlin/io/FileWalkDirection;", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;)V", "onEnter", "Lkotlin/Function1;", "", "onLeave", "", "onFail", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "f", "Ljava/io/IOException;", "e", "maxDepth", "", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;I)V", "iterator", "", "depth", "function", "DirectoryState", "FileTreeWalkIterator", "WalkState", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class FileTreeWalk
implements Sequence<File> {
    private final FileWalkDirection direction;
    private final int maxDepth;
    private final Function1<File, Boolean> onEnter;
    private final Function2<File, IOException, Unit> onFail;
    private final Function1<File, Unit> onLeave;
    private final File start;

    public FileTreeWalk(File file, FileWalkDirection fileWalkDirection) {
        Intrinsics.checkNotNullParameter(file, "start");
        Intrinsics.checkNotNullParameter((Object)fileWalkDirection, "direction");
        this(file, fileWalkDirection, null, null, null, 0, 32, null);
    }

    public /* synthetic */ FileTreeWalk(File file, FileWalkDirection fileWalkDirection, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        this(file, fileWalkDirection);
    }

    private FileTreeWalk(File file, FileWalkDirection fileWalkDirection, Function1<? super File, Boolean> function1, Function1<? super File, Unit> function12, Function2<? super File, ? super IOException, Unit> function2, int n) {
        this.start = file;
        this.direction = fileWalkDirection;
        this.onEnter = function1;
        this.onLeave = function12;
        this.onFail = function2;
        this.maxDepth = n;
    }

    /* synthetic */ FileTreeWalk(File file, FileWalkDirection fileWalkDirection, Function1 function1, Function1 function12, Function2 function2, int n, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 2) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        if ((n2 & 0x20) != 0) {
            n = Integer.MAX_VALUE;
        }
        this(file, fileWalkDirection, function1, function12, function2, n);
    }

    @Override
    public Iterator<File> iterator() {
        return new FileTreeWalkIterator(this);
    }

    public final FileTreeWalk maxDepth(int n) {
        if (n > 0) {
            return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, this.onFail, n);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("depth must be positive, but was ");
        stringBuilder.append(n);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public final FileTreeWalk onEnter(Function1<? super File, Boolean> function1) {
        Intrinsics.checkNotNullParameter(function1, "function");
        return new FileTreeWalk(this.start, this.direction, function1, this.onLeave, this.onFail, this.maxDepth);
    }

    public final FileTreeWalk onFail(Function2<? super File, ? super IOException, Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, function2, this.maxDepth);
    }

    public final FileTreeWalk onLeave(Function1<? super File, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, function1, this.onFail, this.maxDepth);
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2={"Lkotlin/io/FileTreeWalk$DirectoryState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootDir", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static abstract class DirectoryState
    extends WalkState {
        public DirectoryState(File file) {
            Intrinsics.checkNotNullParameter(file, "rootDir");
            super(file);
            if (file.isDirectory()) {
                return;
            }
            throw (Throwable)((Object)new AssertionError((Object)"rootDir must be verified to be directory beforehand."));
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\r\u000e\u000fB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0082\u0010R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;", "Lkotlin/collections/AbstractIterator;", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk;)V", "state", "Ljava/util/ArrayDeque;", "Lkotlin/io/FileTreeWalk$WalkState;", "computeNext", "", "directoryState", "Lkotlin/io/FileTreeWalk$DirectoryState;", "root", "gotoNext", "BottomUpDirectoryState", "SingleFileState", "TopDownDirectoryState", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private final class FileTreeWalkIterator
    extends AbstractIterator<File> {
        private final ArrayDeque<WalkState> state;
        final FileTreeWalk this$0;

        public FileTreeWalkIterator(FileTreeWalk fileTreeWalk) {
            this.this$0 = fileTreeWalk;
            ArrayDeque<WalkState> arrayDeque = new ArrayDeque<WalkState>();
            this.state = arrayDeque;
            if (fileTreeWalk.start.isDirectory()) {
                arrayDeque.push(this.directoryState(fileTreeWalk.start));
            } else if (fileTreeWalk.start.isFile()) {
                arrayDeque.push(new SingleFileState(this, fileTreeWalk.start));
            } else {
                this.done();
            }
        }

        private final DirectoryState directoryState(File object) {
            FileWalkDirection fileWalkDirection = this.this$0.direction;
            switch (FileTreeWalk$FileTreeWalkIterator$WhenMappings.$EnumSwitchMapping$0[fileWalkDirection.ordinal()]) {
                default: {
                    throw new NoWhenBranchMatchedException();
                }
                case 2: {
                    object = new BottomUpDirectoryState(this, (File)object);
                    break;
                }
                case 1: {
                    object = new TopDownDirectoryState(this, (File)object);
                }
            }
            return object;
        }

        private final File gotoNext() {
            WalkState walkState;
            while ((walkState = this.state.peek()) != null) {
                File file = walkState.step();
                if (file == null) {
                    this.state.pop();
                    continue;
                }
                if (!Intrinsics.areEqual(file, walkState.getRoot()) && file.isDirectory() && this.state.size() < this.this$0.maxDepth) {
                    this.state.push(this.directoryState(file));
                    continue;
                }
                return file;
            }
            return null;
        }

        @Override
        protected void computeNext() {
            File file = this.gotoNext();
            if (file != null) {
                this.setNext(file);
            } else {
                this.done();
            }
        }

        @Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\r\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$BottomUpDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "failed", "", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "step", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
        private final class BottomUpDirectoryState
        extends DirectoryState {
            private boolean failed;
            private int fileIndex;
            private File[] fileList;
            private boolean rootVisited;
            final FileTreeWalkIterator this$0;

            public BottomUpDirectoryState(FileTreeWalkIterator fileTreeWalkIterator, File file) {
                Intrinsics.checkNotNullParameter(file, "rootDir");
                this.this$0 = fileTreeWalkIterator;
                super(file);
            }

            @Override
            public File step() {
                Object object;
                if (!this.failed && this.fileList == null) {
                    object = this.this$0.this$0.onEnter;
                    if (object != null && !((Boolean)object.invoke(this.getRoot())).booleanValue()) {
                        return null;
                    }
                    object = this.getRoot().listFiles();
                    this.fileList = object;
                    if (object == null) {
                        object = this.this$0.this$0.onFail;
                        if (object != null) {
                            object = (Unit)object.invoke(this.getRoot(), new AccessDeniedException(this.getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                if ((object = this.fileList) != null) {
                    int n = this.fileIndex;
                    Intrinsics.checkNotNull(object);
                    if (n < ((File[])object).length) {
                        object = this.fileList;
                        Intrinsics.checkNotNull(object);
                        n = this.fileIndex;
                        this.fileIndex = n + 1;
                        return object[n];
                    }
                }
                if (!this.rootVisited) {
                    this.rootVisited = true;
                    return this.getRoot();
                }
                object = this.this$0.this$0.onLeave;
                if (object != null) {
                    object = (Unit)object.invoke(this.getRoot());
                }
                return null;
            }
        }

        @Metadata(bv={1, 0, 3}, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$SingleFileState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootFile", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "visited", "", "step", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
        private final class SingleFileState
        extends WalkState {
            final FileTreeWalkIterator this$0;
            private boolean visited;

            public SingleFileState(FileTreeWalkIterator fileTreeWalkIterator, File file) {
                Intrinsics.checkNotNullParameter(file, "rootFile");
                this.this$0 = fileTreeWalkIterator;
                super(file);
                if (file.isFile()) {
                    return;
                }
                throw (Throwable)((Object)new AssertionError((Object)"rootFile must be verified to be file beforehand."));
            }

            @Override
            public File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return this.getRoot();
            }
        }

        @Metadata(bv={1, 0, 3}, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$TopDownDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "", "step", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
        private final class TopDownDirectoryState
        extends DirectoryState {
            private int fileIndex;
            private File[] fileList;
            private boolean rootVisited;
            final FileTreeWalkIterator this$0;

            public TopDownDirectoryState(FileTreeWalkIterator fileTreeWalkIterator, File file) {
                Intrinsics.checkNotNullParameter(file, "rootDir");
                this.this$0 = fileTreeWalkIterator;
                super(file);
            }

            @Override
            public File step() {
                int n;
                Object object;
                block10: {
                    block11: {
                        if (!this.rootVisited) {
                            Function1 function1 = this.this$0.this$0.onEnter;
                            if (function1 != null && !((Boolean)function1.invoke(this.getRoot())).booleanValue()) {
                                return null;
                            }
                            this.rootVisited = true;
                            return this.getRoot();
                        }
                        object = this.fileList;
                        if (object != null) {
                            n = this.fileIndex;
                            Intrinsics.checkNotNull(object);
                            if (n >= ((File[])object).length) {
                                object = this.this$0.this$0.onLeave;
                                if (object != null) {
                                    object = (Unit)object.invoke(this.getRoot());
                                }
                                return null;
                            }
                        }
                        if (this.fileList != null) break block10;
                        object = this.getRoot().listFiles();
                        this.fileList = object;
                        if (object == null && (object = this.this$0.this$0.onFail) != null) {
                            object = (Unit)object.invoke(this.getRoot(), new AccessDeniedException(this.getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        if ((object = this.fileList) == null) break block11;
                        Intrinsics.checkNotNull(object);
                        if (((File[])object).length != 0) break block10;
                    }
                    if ((object = this.this$0.this$0.onLeave) != null) {
                        object = (Unit)object.invoke(this.getRoot());
                    }
                    return null;
                }
                object = this.fileList;
                Intrinsics.checkNotNull(object);
                n = this.fileIndex;
                this.fileIndex = n + 1;
                return object[n];
            }
        }
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H&R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\b"}, d2={"Lkotlin/io/FileTreeWalk$WalkState;", "", "root", "Ljava/io/File;", "(Ljava/io/File;)V", "getRoot", "()Ljava/io/File;", "step", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static abstract class WalkState {
        private final File root;

        public WalkState(File file) {
            Intrinsics.checkNotNullParameter(file, "root");
            this.root = file;
        }

        public final File getRoot() {
            return this.root;
        }

        public abstract File step();
    }
}

