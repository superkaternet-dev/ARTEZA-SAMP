/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.path;

import java.io.Closeable;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.io.CloseableKt;
import kotlin.io.path.PathRelativizer;
import kotlin.io.path.PathsKt;
import kotlin.io.path.PathsKt__PathReadWriteKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.text.StringsKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u00b2\u0001\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0011\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a*\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00012\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u001a\"\u00020\u0001H\u0087\b\u00a2\u0006\u0002\u0010\u001b\u001a?\u0010\u001c\u001a\u00020\u00022\b\u0010\u001d\u001a\u0004\u0018\u00010\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0007\u00a2\u0006\u0002\u0010!\u001a6\u0010\u001c\u001a\u00020\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b\u00a2\u0006\u0002\u0010\"\u001aK\u0010#\u001a\u00020\u00022\b\u0010\u001d\u001a\u0004\u0018\u00010\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0007\u00a2\u0006\u0002\u0010%\u001aB\u0010#\u001a\u00020\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b\u00a2\u0006\u0002\u0010&\u001a\u001c\u0010'\u001a\u00020(2\u0006\u0010\u0017\u001a\u00020\u00022\n\u0010)\u001a\u0006\u0012\u0002\b\u00030*H\u0001\u001a\r\u0010+\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010,\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a.\u0010-\u001a\u00020\u0002*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u0002000\u001a\"\u000200H\u0087\b\u00a2\u0006\u0002\u00101\u001a\u001f\u0010-\u001a\u00020\u0002*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u00102\u001a\u000203H\u0087\b\u001a.\u00104\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b\u00a2\u0006\u0002\u00105\u001a.\u00106\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b\u00a2\u0006\u0002\u00105\u001a.\u00107\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b\u00a2\u0006\u0002\u00105\u001a\u0015\u00108\u001a\u00020\u0002*\u00020\u00022\u0006\u0010.\u001a\u00020\u0002H\u0087\b\u001a6\u00109\u001a\u00020\u0002*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b\u00a2\u0006\u0002\u0010:\u001a\r\u0010;\u001a\u00020<*\u00020\u0002H\u0087\b\u001a\r\u0010=\u001a\u000203*\u00020\u0002H\u0087\b\u001a\u0015\u0010>\u001a\u00020\u0002*\u00020\u00022\u0006\u0010?\u001a\u00020\u0002H\u0087\n\u001a\u0015\u0010>\u001a\u00020\u0002*\u00020\u00022\u0006\u0010?\u001a\u00020\u0001H\u0087\n\u001a&\u0010@\u001a\u000203*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010B\u001a2\u0010C\u001a\u0002HD\"\n\b\u0000\u0010D\u0018\u0001*\u00020E*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010F\u001a4\u0010G\u001a\u0004\u0018\u0001HD\"\n\b\u0000\u0010D\u0018\u0001*\u00020E*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010F\u001a\r\u0010H\u001a\u00020I*\u00020\u0002H\u0087\b\u001a\r\u0010J\u001a\u00020K*\u00020\u0002H\u0087\b\u001a.\u0010L\u001a\u00020<*\u00020\u00022\b\b\u0002\u0010M\u001a\u00020\u00012\u0012\u0010N\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020<0OH\u0087\b\u00f8\u0001\u0000\u001a0\u0010P\u001a\u0004\u0018\u00010Q*\u00020\u00022\u0006\u0010R\u001a\u00020\u00012\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010S\u001a&\u0010T\u001a\u00020U*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010V\u001a(\u0010W\u001a\u0004\u0018\u00010X*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010Y\u001a,\u0010Z\u001a\b\u0012\u0004\u0012\u00020\\0[*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010]\u001a&\u0010^\u001a\u000203*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010B\u001a\r\u0010_\u001a\u000203*\u00020\u0002H\u0087\b\u001a\r\u0010`\u001a\u000203*\u00020\u0002H\u0087\b\u001a\r\u0010a\u001a\u000203*\u00020\u0002H\u0087\b\u001a&\u0010b\u001a\u000203*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010B\u001a\u0015\u0010c\u001a\u000203*\u00020\u00022\u0006\u0010?\u001a\u00020\u0002H\u0087\b\u001a\r\u0010d\u001a\u000203*\u00020\u0002H\u0087\b\u001a\r\u0010e\u001a\u000203*\u00020\u0002H\u0087\b\u001a\u001c\u0010f\u001a\b\u0012\u0004\u0012\u00020\u00020g*\u00020\u00022\b\b\u0002\u0010M\u001a\u00020\u0001H\u0007\u001a.\u0010h\u001a\u00020\u0002*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u0002000\u001a\"\u000200H\u0087\b\u00a2\u0006\u0002\u00101\u001a\u001f\u0010h\u001a\u00020\u0002*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u00102\u001a\u000203H\u0087\b\u001a&\u0010i\u001a\u000203*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010B\u001a2\u0010j\u001a\u0002Hk\"\n\b\u0000\u0010k\u0018\u0001*\u00020l*\u00020\u00022\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010m\u001a<\u0010j\u001a\u0010\u0012\u0004\u0012\u00020\u0001\u0012\u0006\u0012\u0004\u0018\u00010Q0n*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00012\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010o\u001a\r\u0010p\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0014\u0010q\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002H\u0007\u001a\u0016\u0010r\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002H\u0007\u001a\u0014\u0010s\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002H\u0007\u001a8\u0010t\u001a\u00020\u0002*\u00020\u00022\u0006\u0010R\u001a\u00020\u00012\b\u0010u\u001a\u0004\u0018\u00010Q2\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020A0\u001a\"\u00020AH\u0087\b\u00a2\u0006\u0002\u0010v\u001a\u0015\u0010w\u001a\u00020\u0002*\u00020\u00022\u0006\u0010u\u001a\u00020UH\u0087\b\u001a\u0015\u0010x\u001a\u00020\u0002*\u00020\u00022\u0006\u0010u\u001a\u00020XH\u0087\b\u001a\u001b\u0010y\u001a\u00020\u0002*\u00020\u00022\f\u0010u\u001a\b\u0012\u0004\u0012\u00020\\0[H\u0087\b\u001a\r\u0010z\u001a\u00020\u0002*\u00020{H\u0087\b\u001a@\u0010|\u001a\u0002H}\"\u0004\b\u0000\u0010}*\u00020\u00022\b\b\u0002\u0010M\u001a\u00020\u00012\u0018\u0010~\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u007f\u0012\u0004\u0012\u0002H}0OH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0003\u0010\u0080\u0001\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001f\u0010\u0007\u001a\u00020\u0001*\u00020\u00028\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\b\u0010\u0004\u001a\u0004\b\t\u0010\u0006\"\u001e\u0010\n\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u000b\u0010\u0004\u001a\u0004\b\f\u0010\u0006\"\u001e\u0010\r\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u000e\u0010\u0004\u001a\u0004\b\u000f\u0010\u0006\"\u001e\u0010\u0010\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0011\u0010\u0004\u001a\u0004\b\u0012\u0010\u0006\"\u001f\u0010\u0013\u001a\u00020\u0001*\u00020\u00028\u00c6\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0014\u0010\u0004\u001a\u0004\b\u0015\u0010\u0006\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0081\u0001"}, d2={"extension", "", "Ljava/nio/file/Path;", "getExtension$annotations", "(Ljava/nio/file/Path;)V", "getExtension", "(Ljava/nio/file/Path;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath$annotations", "getInvariantSeparatorsPath", "invariantSeparatorsPathString", "getInvariantSeparatorsPathString$annotations", "getInvariantSeparatorsPathString", "name", "getName$annotations", "getName", "nameWithoutExtension", "getNameWithoutExtension$annotations", "getNameWithoutExtension", "pathString", "getPathString$annotations", "getPathString", "Path", "path", "base", "subpaths", "", "(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;", "createTempDirectory", "directory", "prefix", "attributes", "Ljava/nio/file/attribute/FileAttribute;", "(Ljava/nio/file/Path;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "(Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "createTempFile", "suffix", "(Ljava/nio/file/Path;Ljava/lang/String;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "(Ljava/lang/String;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "fileAttributeViewNotAvailable", "", "attributeViewClass", "Ljava/lang/Class;", "absolute", "absolutePathString", "copyTo", "target", "options", "Ljava/nio/file/CopyOption;", "(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path;", "overwrite", "", "createDirectories", "(Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "createDirectory", "createFile", "createLinkPointingTo", "createSymbolicLinkPointingTo", "(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "deleteExisting", "", "deleteIfExists", "div", "other", "exists", "Ljava/nio/file/LinkOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z", "fileAttributesView", "V", "Ljava/nio/file/attribute/FileAttributeView;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/FileAttributeView;", "fileAttributesViewOrNull", "fileSize", "", "fileStore", "Ljava/nio/file/FileStore;", "forEachDirectoryEntry", "glob", "action", "Lkotlin/Function1;", "getAttribute", "", "attribute", "(Ljava/nio/file/Path;Ljava/lang/String;[Ljava/nio/file/LinkOption;)Ljava/lang/Object;", "getLastModifiedTime", "Ljava/nio/file/attribute/FileTime;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/FileTime;", "getOwner", "Ljava/nio/file/attribute/UserPrincipal;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/UserPrincipal;", "getPosixFilePermissions", "", "Ljava/nio/file/attribute/PosixFilePermission;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/util/Set;", "isDirectory", "isExecutable", "isHidden", "isReadable", "isRegularFile", "isSameFileAs", "isSymbolicLink", "isWritable", "listDirectoryEntries", "", "moveTo", "notExists", "readAttributes", "A", "Ljava/nio/file/attribute/BasicFileAttributes;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/BasicFileAttributes;", "", "(Ljava/nio/file/Path;Ljava/lang/String;[Ljava/nio/file/LinkOption;)Ljava/util/Map;", "readSymbolicLink", "relativeTo", "relativeToOrNull", "relativeToOrSelf", "setAttribute", "value", "(Ljava/nio/file/Path;Ljava/lang/String;Ljava/lang/Object;[Ljava/nio/file/LinkOption;)Ljava/nio/file/Path;", "setLastModifiedTime", "setOwner", "setPosixFilePermissions", "toPath", "Ljava/net/URI;", "useDirectoryEntries", "T", "block", "Lkotlin/sequences/Sequence;", "(Ljava/nio/file/Path;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib-jdk7"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/io/path/PathsKt")
class PathsKt__PathUtilsKt
extends PathsKt__PathReadWriteKt {
    private static final Path Path(String object) {
        object = Paths.get((String)object, new String[0]);
        Intrinsics.checkNotNullExpressionValue(object, "Paths.get(path)");
        return object;
    }

    private static final Path Path(String object, String ... stringArray) {
        object = Paths.get((String)object, Arrays.copyOf(stringArray, stringArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Paths.get(base, *subpaths)");
        return object;
    }

    private static final Path absolute(Path path) {
        path = path.toAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(path, "toAbsolutePath()");
        return path;
    }

    private static final String absolutePathString(Path path) {
        return ((Object)path.toAbsolutePath()).toString();
    }

    private static final Path copyTo(Path path, Path path2, boolean bl) {
        CopyOption[] copyOptionArray = bl ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
        path = Files.copy(path, path2, Arrays.copyOf(copyOptionArray, copyOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.copy(this, target, *options)");
        return path;
    }

    private static final Path copyTo(Path path, Path path2, CopyOption ... copyOptionArray) {
        path = Files.copy(path, path2, Arrays.copyOf(copyOptionArray, copyOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.copy(this, target, *options)");
        return path;
    }

    static /* synthetic */ Path copyTo$default(Path path, Path path2, boolean bl, int n, Object copyOptionArray) {
        if ((n & 2) != 0) {
            bl = false;
        }
        copyOptionArray = bl ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
        path = Files.copy(path, path2, Arrays.copyOf(copyOptionArray, copyOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.copy(this, target, *options)");
        return path;
    }

    private static final Path createDirectories(Path path, FileAttribute<?> ... fileAttributeArray) {
        path = Files.createDirectories(path, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.createDirectories(this, *attributes)");
        return path;
    }

    private static final Path createDirectory(Path path, FileAttribute<?> ... fileAttributeArray) {
        path = Files.createDirectory(path, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.createDirectory(this, *attributes)");
        return path;
    }

    private static final Path createFile(Path path, FileAttribute<?> ... fileAttributeArray) {
        path = Files.createFile(path, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.createFile(this, *attributes)");
        return path;
    }

    private static final Path createLinkPointingTo(Path path, Path path2) {
        path = Files.createLink(path, path2);
        Intrinsics.checkNotNullExpressionValue(path, "Files.createLink(this, target)");
        return path;
    }

    private static final Path createSymbolicLinkPointingTo(Path path, Path path2, FileAttribute<?> ... fileAttributeArray) {
        path = Files.createSymbolicLink(path, path2, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.createSymbolicLink\u2026his, target, *attributes)");
        return path;
    }

    private static final Path createTempDirectory(String object, FileAttribute<?> ... fileAttributeArray) {
        object = Files.createTempDirectory((String)object, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.createTempDirectory(prefix, *attributes)");
        return object;
    }

    public static final Path createTempDirectory(Path path, String string2, FileAttribute<?> ... fileAttributeArray) {
        Intrinsics.checkNotNullParameter(fileAttributeArray, "attributes");
        if (path != null) {
            path = Files.createTempDirectory(path, string2, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
            Intrinsics.checkNotNullExpressionValue(path, "Files.createTempDirector\u2026ory, prefix, *attributes)");
        } else {
            path = Files.createTempDirectory(string2, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
            Intrinsics.checkNotNullExpressionValue(path, "Files.createTempDirectory(prefix, *attributes)");
        }
        return path;
    }

    static /* synthetic */ Path createTempDirectory$default(String object, FileAttribute[] fileAttributeArray, int n, Object object2) {
        if ((n & 1) != 0) {
            object = null;
        }
        object = Files.createTempDirectory((String)object, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.createTempDirectory(prefix, *attributes)");
        return object;
    }

    public static /* synthetic */ Path createTempDirectory$default(Path path, String string2, FileAttribute[] fileAttributeArray, int n, Object object) {
        if ((n & 2) != 0) {
            string2 = null;
        }
        return PathsKt.createTempDirectory(path, string2, fileAttributeArray);
    }

    private static final Path createTempFile(String object, String string2, FileAttribute<?> ... fileAttributeArray) {
        object = Files.createTempFile((String)object, string2, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.createTempFile(prefix, suffix, *attributes)");
        return object;
    }

    public static final Path createTempFile(Path path, String string2, String string3, FileAttribute<?> ... fileAttributeArray) {
        Intrinsics.checkNotNullParameter(fileAttributeArray, "attributes");
        if (path != null) {
            path = Files.createTempFile(path, string2, string3, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
            Intrinsics.checkNotNullExpressionValue(path, "Files.createTempFile(dir\u2026fix, suffix, *attributes)");
        } else {
            path = Files.createTempFile(string2, string3, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
            Intrinsics.checkNotNullExpressionValue(path, "Files.createTempFile(prefix, suffix, *attributes)");
        }
        return path;
    }

    static /* synthetic */ Path createTempFile$default(String object, String string2, FileAttribute[] fileAttributeArray, int n, Object object2) {
        if ((n & 1) != 0) {
            object = null;
        }
        if ((n & 2) != 0) {
            string2 = null;
        }
        object = Files.createTempFile((String)object, string2, Arrays.copyOf(fileAttributeArray, fileAttributeArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.createTempFile(prefix, suffix, *attributes)");
        return object;
    }

    public static /* synthetic */ Path createTempFile$default(Path path, String string2, String string3, FileAttribute[] fileAttributeArray, int n, Object object) {
        if ((n & 2) != 0) {
            string2 = null;
        }
        if ((n & 4) != 0) {
            string3 = null;
        }
        return PathsKt.createTempFile(path, string2, string3, fileAttributeArray);
    }

    private static final void deleteExisting(Path path) {
        Files.delete(path);
    }

    private static final boolean deleteIfExists(Path path) {
        return Files.deleteIfExists(path);
    }

    private static final Path div(Path path, String string2) {
        Intrinsics.checkNotNullParameter(path, "$this$div");
        path = path.resolve(string2);
        Intrinsics.checkNotNullExpressionValue(path, "this.resolve(other)");
        return path;
    }

    private static final Path div(Path path, Path path2) {
        Intrinsics.checkNotNullParameter(path, "$this$div");
        path = path.resolve(path2);
        Intrinsics.checkNotNullExpressionValue(path, "this.resolve(other)");
        return path;
    }

    private static final boolean exists(Path path, LinkOption ... linkOptionArray) {
        return Files.exists(path, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    public static final Void fileAttributeViewNotAvailable(Path path, Class<?> clazz) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(clazz, "attributeViewClass");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The desired attribute view type ");
        stringBuilder.append(clazz);
        stringBuilder.append(" is not available for the file ");
        stringBuilder.append(path);
        stringBuilder.append('.');
        throw (Throwable)new UnsupportedOperationException(stringBuilder.toString());
    }

    private static final /* synthetic */ <V extends FileAttributeView> V fileAttributesView(Path path, LinkOption ... object) {
        Intrinsics.reifiedOperationMarker(4, "V");
        object = Files.getFileAttributeView(path, FileAttributeView.class, Arrays.copyOf(object, ((LinkOption[])object).length));
        if (object != null) {
            return (V)object;
        }
        Intrinsics.reifiedOperationMarker(4, "V");
        PathsKt.fileAttributeViewNotAvailable(path, FileAttributeView.class);
        throw new KotlinNothingValueException();
    }

    private static final /* synthetic */ <V extends FileAttributeView> V fileAttributesViewOrNull(Path path, LinkOption ... linkOptionArray) {
        Intrinsics.reifiedOperationMarker(4, "V");
        return (V)Files.getFileAttributeView(path, FileAttributeView.class, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    private static final long fileSize(Path path) {
        return Files.size(path);
    }

    private static final FileStore fileStore(Path object) {
        object = Files.getFileStore((Path)object);
        Intrinsics.checkNotNullExpressionValue(object, "Files.getFileStore(this)");
        return object;
    }

    private static final void forEachDirectoryEntry(Path object, String object2, Function1<? super Path, Unit> object3) {
        object = Files.newDirectoryStream((Path)object, (String)object2);
        object2 = null;
        try {
            Object object4 = (DirectoryStream)object;
            Intrinsics.checkNotNullExpressionValue(object4, "it");
            object4 = ((Iterable)object4).iterator();
            while (object4.hasNext()) {
                object3.invoke(object4.next());
            }
            object3 = Unit.INSTANCE;
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable3) {}
                    }
                } else {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                }
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        } else if (object != null) {
            object.close();
        }
        InlineMarker.finallyEnd(1);
    }

    static /* synthetic */ void forEachDirectoryEntry$default(Path object, String object2, Function1 object3, int n, Object iterator2) {
        if ((n & 1) != 0) {
            object2 = "*";
        }
        object = Files.newDirectoryStream((Path)object, (String)object2);
        object2 = null;
        try {
            iterator2 = (DirectoryStream)object;
            Intrinsics.checkNotNullExpressionValue(iterator2, "it");
            iterator2 = ((Iterable)((Object)iterator2)).iterator();
            while (iterator2.hasNext()) {
                object3.invoke(iterator2.next());
            }
            object3 = Unit.INSTANCE;
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable3) {}
                    }
                } else {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                }
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        } else if (object != null) {
            object.close();
        }
        InlineMarker.finallyEnd(1);
    }

    private static final Object getAttribute(Path path, String string2, LinkOption ... linkOptionArray) {
        return Files.getAttribute(path, string2, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    public static final String getExtension(Path object) {
        Intrinsics.checkNotNullParameter(object, "$this$extension");
        Object object2 = object.getFileName();
        String string2 = "";
        object = string2;
        if (object2 != null) {
            object2 = object2.toString();
            object = string2;
            if (object2 != null) {
                object2 = StringsKt.substringAfterLast((String)object2, '.', "");
                object = string2;
                if (object2 != null) {
                    object = object2;
                }
            }
        }
        return object;
    }

    public static /* synthetic */ void getExtension$annotations(Path path) {
    }

    public static final String getInvariantSeparatorsPath(Path path) {
        Intrinsics.checkNotNullParameter(path, "$this$invariantSeparatorsPath");
        return PathsKt.getInvariantSeparatorsPathString(path);
    }

    @Deprecated(message="Use invariantSeparatorsPathString property instead.", replaceWith=@ReplaceWith(expression="invariantSeparatorsPathString", imports={}))
    public static /* synthetic */ void getInvariantSeparatorsPath$annotations(Path path) {
    }

    public static final String getInvariantSeparatorsPathString(Path object) {
        Intrinsics.checkNotNullParameter(object, "$this$invariantSeparatorsPathString");
        Object object2 = object.getFileSystem();
        Intrinsics.checkNotNullExpressionValue(object2, "fileSystem");
        object2 = ((FileSystem)object2).getSeparator();
        if (Intrinsics.areEqual(object2, "/") ^ true) {
            object = object.toString();
            Intrinsics.checkNotNullExpressionValue(object2, "separator");
            object = StringsKt.replace$default((String)object, (String)object2, "/", false, 4, null);
        } else {
            object = object.toString();
        }
        return object;
    }

    public static /* synthetic */ void getInvariantSeparatorsPathString$annotations(Path path) {
    }

    private static final FileTime getLastModifiedTime(Path comparable, LinkOption ... linkOptionArray) {
        comparable = Files.getLastModifiedTime(comparable, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(comparable, "Files.getLastModifiedTime(this, *options)");
        return comparable;
    }

    public static final String getName(Path object) {
        Intrinsics.checkNotNullParameter(object, "$this$name");
        object = object.getFileName();
        object = object != null ? object.toString() : null;
        if (object == null) {
            object = "";
        }
        return object;
    }

    public static /* synthetic */ void getName$annotations(Path path) {
    }

    public static final String getNameWithoutExtension(Path object) {
        Intrinsics.checkNotNullParameter(object, "$this$nameWithoutExtension");
        object = object.getFileName();
        if (object == null || (object = object.toString()) == null || (object = StringsKt.substringBeforeLast$default((String)object, ".", null, 2, null)) == null) {
            object = "";
        }
        return object;
    }

    public static /* synthetic */ void getNameWithoutExtension$annotations(Path path) {
    }

    private static final UserPrincipal getOwner(Path path, LinkOption ... linkOptionArray) {
        return Files.getOwner(path, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    private static final String getPathString(Path path) {
        return ((Object)path).toString();
    }

    public static /* synthetic */ void getPathString$annotations(Path path) {
    }

    private static final Set<PosixFilePermission> getPosixFilePermissions(Path iterable, LinkOption ... linkOptionArray) {
        iterable = Files.getPosixFilePermissions(iterable, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(iterable, "Files.getPosixFilePermissions(this, *options)");
        return iterable;
    }

    private static final boolean isDirectory(Path path, LinkOption ... linkOptionArray) {
        return Files.isDirectory(path, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    private static final boolean isExecutable(Path path) {
        return Files.isExecutable(path);
    }

    private static final boolean isHidden(Path path) {
        return Files.isHidden(path);
    }

    private static final boolean isReadable(Path path) {
        return Files.isReadable(path);
    }

    private static final boolean isRegularFile(Path path, LinkOption ... linkOptionArray) {
        return Files.isRegularFile(path, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    private static final boolean isSameFileAs(Path path, Path path2) {
        return Files.isSameFile(path, path2);
    }

    private static final boolean isSymbolicLink(Path path) {
        return Files.isSymbolicLink(path);
    }

    private static final boolean isWritable(Path path) {
        return Files.isWritable(path);
    }

    public static final List<Path> listDirectoryEntries(Path object, String object2) {
        Iterable iterable;
        Intrinsics.checkNotNullParameter(object, "$this$listDirectoryEntries");
        Intrinsics.checkNotNullParameter(object2, "glob");
        object = Files.newDirectoryStream((Path)object, (String)object2);
        object2 = null;
        try {
            iterable = (DirectoryStream)object;
            Intrinsics.checkNotNullExpressionValue(iterable, "it");
            iterable = CollectionsKt.toList(iterable);
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable2;
            }
        }
        CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        return iterable;
    }

    public static /* synthetic */ List listDirectoryEntries$default(Path path, String string2, int n, Object object) {
        if ((n & 1) != 0) {
            string2 = "*";
        }
        return PathsKt.listDirectoryEntries(path, string2);
    }

    private static final Path moveTo(Path path, Path path2, boolean bl) {
        CopyOption[] copyOptionArray = bl ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
        path = Files.move(path, path2, Arrays.copyOf(copyOptionArray, copyOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.move(this, target, *options)");
        return path;
    }

    private static final Path moveTo(Path path, Path path2, CopyOption ... copyOptionArray) {
        path = Files.move(path, path2, Arrays.copyOf(copyOptionArray, copyOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.move(this, target, *options)");
        return path;
    }

    static /* synthetic */ Path moveTo$default(Path path, Path path2, boolean bl, int n, Object copyOptionArray) {
        if ((n & 2) != 0) {
            bl = false;
        }
        copyOptionArray = bl ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};
        path = Files.move(path, path2, Arrays.copyOf(copyOptionArray, copyOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.move(this, target, *options)");
        return path;
    }

    private static final boolean notExists(Path path, LinkOption ... linkOptionArray) {
        return Files.notExists(path, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
    }

    private static final /* synthetic */ <A extends BasicFileAttributes> A readAttributes(Path object, LinkOption ... linkOptionArray) {
        Intrinsics.reifiedOperationMarker(4, "A");
        object = Files.readAttributes((Path)object, BasicFileAttributes.class, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.readAttributes(thi\u2026 A::class.java, *options)");
        return (A)object;
    }

    private static final Map<String, Object> readAttributes(Path object, String string2, LinkOption ... linkOptionArray) {
        object = Files.readAttributes((Path)object, string2, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(object, "Files.readAttributes(this, attributes, *options)");
        return object;
    }

    private static final Path readSymbolicLink(Path path) {
        path = Files.readSymbolicLink(path);
        Intrinsics.checkNotNullExpressionValue(path, "Files.readSymbolicLink(this)");
        return path;
    }

    public static final Path relativeTo(Path path, Path path2) {
        Intrinsics.checkNotNullParameter(path, "$this$relativeTo");
        Intrinsics.checkNotNullParameter(path2, "base");
        try {
            Path path3 = PathRelativizer.INSTANCE.tryRelativeTo(path, path2);
            return path3;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            String string2 = illegalArgumentException.getMessage();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\nthis path: ");
            stringBuilder.append(path);
            stringBuilder.append("\nbase path: ");
            stringBuilder.append(path2);
            throw (Throwable)new IllegalArgumentException(Intrinsics.stringPlus(string2, stringBuilder.toString()), illegalArgumentException);
        }
    }

    public static final Path relativeToOrNull(Path path, Path path2) {
        Intrinsics.checkNotNullParameter(path, "$this$relativeToOrNull");
        Intrinsics.checkNotNullParameter(path2, "base");
        try {
            path = PathRelativizer.INSTANCE.tryRelativeTo(path, path2);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            path = null;
        }
        return path;
    }

    public static final Path relativeToOrSelf(Path path, Path path2) {
        block0: {
            Intrinsics.checkNotNullParameter(path, "$this$relativeToOrSelf");
            Intrinsics.checkNotNullParameter(path2, "base");
            path2 = PathsKt.relativeToOrNull(path, path2);
            if (path2 == null) break block0;
            path = path2;
        }
        return path;
    }

    private static final Path setAttribute(Path path, String string2, Object object, LinkOption ... linkOptionArray) {
        path = Files.setAttribute(path, string2, object, Arrays.copyOf(linkOptionArray, linkOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path, "Files.setAttribute(this,\u2026tribute, value, *options)");
        return path;
    }

    private static final Path setLastModifiedTime(Path path, FileTime fileTime) {
        path = Files.setLastModifiedTime(path, fileTime);
        Intrinsics.checkNotNullExpressionValue(path, "Files.setLastModifiedTime(this, value)");
        return path;
    }

    private static final Path setOwner(Path path, UserPrincipal userPrincipal) {
        path = Files.setOwner(path, userPrincipal);
        Intrinsics.checkNotNullExpressionValue(path, "Files.setOwner(this, value)");
        return path;
    }

    private static final Path setPosixFilePermissions(Path path, Set<? extends PosixFilePermission> set) {
        path = Files.setPosixFilePermissions(path, set);
        Intrinsics.checkNotNullExpressionValue(path, "Files.setPosixFilePermissions(this, value)");
        return path;
    }

    private static final Path toPath(URI comparable) {
        comparable = Paths.get(comparable);
        Intrinsics.checkNotNullExpressionValue(comparable, "Paths.get(this)");
        return comparable;
    }

    private static final <T> T useDirectoryEntries(Path object, String object2, Function1<? super Sequence<? extends Path>, ? extends T> function1) {
        object = Files.newDirectoryStream((Path)object, (String)object2);
        object2 = null;
        try {
            DirectoryStream directoryStream = (DirectoryStream)object;
            Intrinsics.checkNotNullExpressionValue(directoryStream, "it");
            function1 = function1.invoke(CollectionsKt.asSequence(directoryStream));
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable3) {}
                    }
                } else {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                }
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        } else if (object != null) {
            object.close();
        }
        InlineMarker.finallyEnd(1);
        return (T)function1;
    }

    static /* synthetic */ Object useDirectoryEntries$default(Path object, String object2, Function1 function1, int n, Object object3) {
        if ((n & 1) != 0) {
            object2 = "*";
        }
        object = Files.newDirectoryStream((Path)object, (String)object2);
        object2 = null;
        try {
            object3 = (DirectoryStream)object;
            Intrinsics.checkNotNullExpressionValue(object3, "it");
            function1 = function1.invoke(CollectionsKt.asSequence((Iterable)object3));
        }
        catch (Throwable throwable) {
            try {
                throw throwable;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                if (!PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    if (object != null) {
                        try {
                            object.close();
                        }
                        catch (Throwable throwable3) {}
                    }
                } else {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                }
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
        } else if (object != null) {
            object.close();
        }
        InlineMarker.finallyEnd(1);
        return function1;
    }
}

