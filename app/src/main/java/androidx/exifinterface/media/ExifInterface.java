/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetManager$AssetInputStream
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.location.Location
 *  android.media.MediaDataSource
 *  android.media.MediaMetadataRetriever
 *  android.os.Build$VERSION
 *  android.system.Os
 *  android.system.OsConstants
 *  android.util.Log
 *  android.util.Pair
 */
package androidx.exifinterface.media;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class ExifInterface {
    public static final short ALTITUDE_ABOVE_SEA_LEVEL = 0;
    public static final short ALTITUDE_BELOW_SEA_LEVEL = 1;
    static final Charset ASCII;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2;
    public static final int[] BITS_PER_SAMPLE_RGB;
    static final short BYTE_ALIGN_II = 18761;
    static final short BYTE_ALIGN_MM = 19789;
    public static final int COLOR_SPACE_S_RGB = 1;
    public static final int COLOR_SPACE_UNCALIBRATED = 65535;
    public static final short CONTRAST_HARD = 2;
    public static final short CONTRAST_NORMAL = 0;
    public static final short CONTRAST_SOFT = 1;
    public static final int DATA_DEFLATE_ZIP = 8;
    public static final int DATA_HUFFMAN_COMPRESSED = 2;
    public static final int DATA_JPEG = 6;
    public static final int DATA_JPEG_COMPRESSED = 7;
    public static final int DATA_LOSSY_JPEG = 34892;
    public static final int DATA_PACK_BITS_COMPRESSED = 32773;
    public static final int DATA_UNCOMPRESSED = 1;
    private static final boolean DEBUG;
    static final byte[] EXIF_ASCII_PREFIX;
    private static final ExifTag[] EXIF_POINTER_TAGS;
    static final ExifTag[][] EXIF_TAGS;
    public static final short EXPOSURE_MODE_AUTO = 0;
    public static final short EXPOSURE_MODE_AUTO_BRACKET = 2;
    public static final short EXPOSURE_MODE_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_ACTION = 6;
    public static final short EXPOSURE_PROGRAM_APERTURE_PRIORITY = 3;
    public static final short EXPOSURE_PROGRAM_CREATIVE = 5;
    public static final short EXPOSURE_PROGRAM_LANDSCAPE_MODE = 8;
    public static final short EXPOSURE_PROGRAM_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_NORMAL = 2;
    public static final short EXPOSURE_PROGRAM_NOT_DEFINED = 0;
    public static final short EXPOSURE_PROGRAM_PORTRAIT_MODE = 7;
    public static final short EXPOSURE_PROGRAM_SHUTTER_PRIORITY = 4;
    public static final short FILE_SOURCE_DSC = 3;
    public static final short FILE_SOURCE_OTHER = 0;
    public static final short FILE_SOURCE_REFLEX_SCANNER = 2;
    public static final short FILE_SOURCE_TRANSPARENT_SCANNER = 1;
    public static final short FLAG_FLASH_FIRED = 1;
    public static final short FLAG_FLASH_MODE_AUTO = 24;
    public static final short FLAG_FLASH_MODE_COMPULSORY_FIRING = 8;
    public static final short FLAG_FLASH_MODE_COMPULSORY_SUPPRESSION = 16;
    public static final short FLAG_FLASH_NO_FLASH_FUNCTION = 32;
    public static final short FLAG_FLASH_RED_EYE_SUPPORTED = 64;
    public static final short FLAG_FLASH_RETURN_LIGHT_DETECTED = 6;
    public static final short FLAG_FLASH_RETURN_LIGHT_NOT_DETECTED = 4;
    private static final List<Integer> FLIPPED_ROTATION_ORDER;
    public static final short FORMAT_CHUNKY = 1;
    public static final short FORMAT_PLANAR = 2;
    public static final short GAIN_CONTROL_HIGH_GAIN_DOWN = 4;
    public static final short GAIN_CONTROL_HIGH_GAIN_UP = 2;
    public static final short GAIN_CONTROL_LOW_GAIN_DOWN = 3;
    public static final short GAIN_CONTROL_LOW_GAIN_UP = 1;
    public static final short GAIN_CONTROL_NONE = 0;
    public static final String GPS_DIRECTION_MAGNETIC = "M";
    public static final String GPS_DIRECTION_TRUE = "T";
    public static final String GPS_DISTANCE_KILOMETERS = "K";
    public static final String GPS_DISTANCE_MILES = "M";
    public static final String GPS_DISTANCE_NAUTICAL_MILES = "N";
    public static final String GPS_MEASUREMENT_2D = "2";
    public static final String GPS_MEASUREMENT_3D = "3";
    public static final short GPS_MEASUREMENT_DIFFERENTIAL_CORRECTED = 1;
    public static final String GPS_MEASUREMENT_INTERRUPTED = "V";
    public static final String GPS_MEASUREMENT_IN_PROGRESS = "A";
    public static final short GPS_MEASUREMENT_NO_DIFFERENTIAL = 0;
    public static final String GPS_SPEED_KILOMETERS_PER_HOUR = "K";
    public static final String GPS_SPEED_KNOTS = "N";
    public static final String GPS_SPEED_MILES_PER_HOUR = "M";
    private static final byte[] HEIF_BRAND_HEIC;
    private static final byte[] HEIF_BRAND_MIF1;
    private static final byte[] HEIF_TYPE_FTYP;
    static final byte[] IDENTIFIER_EXIF_APP1;
    private static final byte[] IDENTIFIER_XMP_APP1;
    private static final ExifTag[] IFD_EXIF_TAGS;
    private static final int IFD_FORMAT_BYTE = 1;
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT;
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final int IFD_FORMAT_IFD = 13;
    static final String[] IFD_FORMAT_NAMES;
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final ExifTag[] IFD_GPS_TAGS;
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS;
    private static final int IFD_OFFSET = 8;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS;
    private static final ExifTag[] IFD_TIFF_TAGS;
    private static final int IFD_TYPE_EXIF = 1;
    private static final int IFD_TYPE_GPS = 2;
    private static final int IFD_TYPE_INTEROPERABILITY = 3;
    private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
    private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
    private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
    private static final int IFD_TYPE_PEF = 9;
    static final int IFD_TYPE_PREVIEW = 5;
    static final int IFD_TYPE_PRIMARY = 0;
    static final int IFD_TYPE_THUMBNAIL = 4;
    private static final int IMAGE_TYPE_ARW = 1;
    private static final int IMAGE_TYPE_CR2 = 2;
    private static final int IMAGE_TYPE_DNG = 3;
    private static final int IMAGE_TYPE_HEIF = 12;
    private static final int IMAGE_TYPE_JPEG = 4;
    private static final int IMAGE_TYPE_NEF = 5;
    private static final int IMAGE_TYPE_NRW = 6;
    private static final int IMAGE_TYPE_ORF = 7;
    private static final int IMAGE_TYPE_PEF = 8;
    private static final int IMAGE_TYPE_PNG = 13;
    private static final int IMAGE_TYPE_RAF = 9;
    private static final int IMAGE_TYPE_RW2 = 10;
    private static final int IMAGE_TYPE_SRW = 11;
    private static final int IMAGE_TYPE_UNKNOWN = 0;
    private static final int IMAGE_TYPE_WEBP = 14;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
    static final byte[] JPEG_SIGNATURE;
    public static final String LATITUDE_NORTH = "N";
    public static final String LATITUDE_SOUTH = "S";
    public static final short LIGHT_SOURCE_CLOUDY_WEATHER = 10;
    public static final short LIGHT_SOURCE_COOL_WHITE_FLUORESCENT = 14;
    public static final short LIGHT_SOURCE_D50 = 23;
    public static final short LIGHT_SOURCE_D55 = 20;
    public static final short LIGHT_SOURCE_D65 = 21;
    public static final short LIGHT_SOURCE_D75 = 22;
    public static final short LIGHT_SOURCE_DAYLIGHT = 1;
    public static final short LIGHT_SOURCE_DAYLIGHT_FLUORESCENT = 12;
    public static final short LIGHT_SOURCE_DAY_WHITE_FLUORESCENT = 13;
    public static final short LIGHT_SOURCE_FINE_WEATHER = 9;
    public static final short LIGHT_SOURCE_FLASH = 4;
    public static final short LIGHT_SOURCE_FLUORESCENT = 2;
    public static final short LIGHT_SOURCE_ISO_STUDIO_TUNGSTEN = 24;
    public static final short LIGHT_SOURCE_OTHER = 255;
    public static final short LIGHT_SOURCE_SHADE = 11;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_A = 17;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_B = 18;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_C = 19;
    public static final short LIGHT_SOURCE_TUNGSTEN = 3;
    public static final short LIGHT_SOURCE_UNKNOWN = 0;
    public static final short LIGHT_SOURCE_WARM_WHITE_FLUORESCENT = 16;
    public static final short LIGHT_SOURCE_WHITE_FLUORESCENT = 15;
    public static final String LONGITUDE_EAST = "E";
    public static final String LONGITUDE_WEST = "W";
    static final byte MARKER = -1;
    static final byte MARKER_APP1 = -31;
    private static final byte MARKER_COM = -2;
    static final byte MARKER_EOI = -39;
    private static final byte MARKER_SOF0 = -64;
    private static final byte MARKER_SOF1 = -63;
    private static final byte MARKER_SOF10 = -54;
    private static final byte MARKER_SOF11 = -53;
    private static final byte MARKER_SOF13 = -51;
    private static final byte MARKER_SOF14 = -50;
    private static final byte MARKER_SOF15 = -49;
    private static final byte MARKER_SOF2 = -62;
    private static final byte MARKER_SOF3 = -61;
    private static final byte MARKER_SOF5 = -59;
    private static final byte MARKER_SOF6 = -58;
    private static final byte MARKER_SOF7 = -57;
    private static final byte MARKER_SOF9 = -55;
    private static final byte MARKER_SOI = -40;
    private static final byte MARKER_SOS = -38;
    private static final int MAX_THUMBNAIL_SIZE = 512;
    public static final short METERING_MODE_AVERAGE = 1;
    public static final short METERING_MODE_CENTER_WEIGHT_AVERAGE = 2;
    public static final short METERING_MODE_MULTI_SPOT = 4;
    public static final short METERING_MODE_OTHER = 255;
    public static final short METERING_MODE_PARTIAL = 6;
    public static final short METERING_MODE_PATTERN = 5;
    public static final short METERING_MODE_SPOT = 3;
    public static final short METERING_MODE_UNKNOWN = 0;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
    private static final byte[] ORF_MAKER_NOTE_HEADER_1;
    private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2;
    private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS;
    private static final short ORF_SIGNATURE_1 = 20306;
    private static final short ORF_SIGNATURE_2 = 21330;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIGINAL_RESOLUTION_IMAGE = 0;
    private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
    private static final String PEF_SIGNATURE = "PENTAX";
    private static final ExifTag[] PEF_TAGS;
    public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
    public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
    public static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
    private static final int PNG_CHUNK_CRC_BYTE_LENGTH = 4;
    private static final int PNG_CHUNK_TYPE_BYTE_LENGTH = 4;
    private static final byte[] PNG_CHUNK_TYPE_EXIF;
    private static final byte[] PNG_CHUNK_TYPE_IEND;
    private static final byte[] PNG_CHUNK_TYPE_IHDR;
    private static final byte[] PNG_SIGNATURE;
    private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
    private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
    private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
    public static final int REDUCED_RESOLUTION_IMAGE = 1;
    public static final short RENDERED_PROCESS_CUSTOM = 1;
    public static final short RENDERED_PROCESS_NORMAL = 0;
    public static final short RESOLUTION_UNIT_CENTIMETERS = 3;
    public static final short RESOLUTION_UNIT_INCHES = 2;
    private static final List<Integer> ROTATION_ORDER;
    private static final short RW2_SIGNATURE = 85;
    public static final short SATURATION_HIGH = 0;
    public static final short SATURATION_LOW = 0;
    public static final short SATURATION_NORMAL = 0;
    public static final short SCENE_CAPTURE_TYPE_LANDSCAPE = 1;
    public static final short SCENE_CAPTURE_TYPE_NIGHT = 3;
    public static final short SCENE_CAPTURE_TYPE_PORTRAIT = 2;
    public static final short SCENE_CAPTURE_TYPE_STANDARD = 0;
    public static final short SCENE_TYPE_DIRECTLY_PHOTOGRAPHED = 1;
    public static final short SENSITIVITY_TYPE_ISO_SPEED = 3;
    public static final short SENSITIVITY_TYPE_REI = 2;
    public static final short SENSITIVITY_TYPE_REI_AND_ISO = 6;
    public static final short SENSITIVITY_TYPE_SOS = 1;
    public static final short SENSITIVITY_TYPE_SOS_AND_ISO = 5;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI = 4;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI_AND_ISO = 7;
    public static final short SENSITIVITY_TYPE_UNKNOWN = 0;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL = 5;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL_LINEAR = 8;
    public static final short SENSOR_TYPE_NOT_DEFINED = 1;
    public static final short SENSOR_TYPE_ONE_CHIP = 2;
    public static final short SENSOR_TYPE_THREE_CHIP = 4;
    public static final short SENSOR_TYPE_TRILINEAR = 7;
    public static final short SENSOR_TYPE_TWO_CHIP = 3;
    public static final short SHARPNESS_HARD = 2;
    public static final short SHARPNESS_NORMAL = 0;
    public static final short SHARPNESS_SOFT = 1;
    private static final int SIGNATURE_CHECK_SIZE = 5000;
    static final byte START_CODE = 42;
    public static final int STREAM_TYPE_EXIF_DATA_ONLY = 1;
    public static final int STREAM_TYPE_FULL_IMAGE_DATA = 0;
    public static final short SUBJECT_DISTANCE_RANGE_CLOSE_VIEW = 2;
    public static final short SUBJECT_DISTANCE_RANGE_DISTANT_VIEW = 3;
    public static final short SUBJECT_DISTANCE_RANGE_MACRO = 1;
    public static final short SUBJECT_DISTANCE_RANGE_UNKNOWN = 0;
    private static final String TAG = "ExifInterface";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BODY_SERIAL_NUMBER = "BodySerialNumber";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
    @Deprecated
    public static final String TAG_CAMARA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CAMERA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    public static final String TAG_DNG_VERSION = "DNGVersion";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_F_NUMBER = "FNumber";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GAMMA = "Gamma";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_H_POSITIONING_ERROR = "GPSHPositioningError";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    public static final String TAG_ISO_SPEED = "ISOSpeed";
    public static final String TAG_ISO_SPEED_LATITUDE_YYY = "ISOSpeedLatitudeyyy";
    public static final String TAG_ISO_SPEED_LATITUDE_ZZZ = "ISOSpeedLatitudezzz";
    @Deprecated
    public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LENS_MAKE = "LensMake";
    public static final String TAG_LENS_MODEL = "LensModel";
    public static final String TAG_LENS_SERIAL_NUMBER = "LensSerialNumber";
    public static final String TAG_LENS_SPECIFICATION = "LensSpecification";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_OFFSET_TIME = "OffsetTime";
    public static final String TAG_OFFSET_TIME_DIGITIZED = "OffsetTimeDigitized";
    public static final String TAG_OFFSET_TIME_ORIGINAL = "OffsetTimeOriginal";
    public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
    private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
    private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
    public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
    public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
    public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOGRAPHIC_SENSITIVITY = "PhotographicSensitivity";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    private static final ExifTag TAG_RAF_IMAGE_SIZE;
    public static final String TAG_RECOMMENDED_EXPOSURE_INDEX = "RecommendedExposureIndex";
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_RW2_ISO = "ISO";
    public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
    public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
    public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
    public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
    public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SENSITIVITY_TYPE = "SensitivityType";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STANDARD_OUTPUT_SENSITIVITY = "StandardOutputSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBFILE_TYPE = "SubfileType";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
    private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    public static final String TAG_THUMBNAIL_ORIENTATION = "ThumbnailOrientation";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_XMP = "Xmp";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    private static final int WEBP_CHUNK_SIZE_BYTE_LENGTH = 4;
    private static final byte[] WEBP_CHUNK_TYPE_ANIM;
    private static final byte[] WEBP_CHUNK_TYPE_ANMF;
    private static final int WEBP_CHUNK_TYPE_BYTE_LENGTH = 4;
    private static final byte[] WEBP_CHUNK_TYPE_EXIF;
    private static final byte[] WEBP_CHUNK_TYPE_VP8;
    private static final byte[] WEBP_CHUNK_TYPE_VP8L;
    private static final byte[] WEBP_CHUNK_TYPE_VP8X;
    private static final int WEBP_CHUNK_TYPE_VP8X_DEFAULT_LENGTH = 10;
    private static final byte[] WEBP_CHUNK_TYPE_XMP;
    private static final int WEBP_FILE_SIZE_BYTE_LENGTH = 4;
    private static final byte[] WEBP_SIGNATURE_1;
    private static final byte[] WEBP_SIGNATURE_2;
    private static final byte WEBP_VP8L_SIGNATURE = 47;
    private static final byte[] WEBP_VP8_SIGNATURE;
    @Deprecated
    public static final int WHITEBALANCE_AUTO = 0;
    @Deprecated
    public static final int WHITEBALANCE_MANUAL = 1;
    public static final short WHITE_BALANCE_AUTO = 0;
    public static final short WHITE_BALANCE_MANUAL = 1;
    public static final short Y_CB_CR_POSITIONING_CENTERED = 1;
    public static final short Y_CB_CR_POSITIONING_CO_SITED = 2;
    private static final HashMap<Integer, Integer> sExifPointerTagMap;
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
    private static SimpleDateFormat sFormatter;
    private static final Pattern sGpsTimestampPattern;
    private static final Pattern sNonZeroTimePattern;
    private static final HashSet<String> sTagSetForCompatibility;
    private boolean mAreThumbnailStripsConsecutive;
    private AssetManager.AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes;
    private Set<Integer> mAttributesOffsets;
    private ByteOrder mExifByteOrder;
    private int mExifOffset;
    private String mFilename;
    private boolean mHasThumbnail;
    private boolean mHasThumbnailStrips;
    private boolean mIsExifDataOnly;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private boolean mModified;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private FileDescriptor mSeekableFileDescriptor;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;
    private boolean mXmpIsFromSeparateMarker;

    static {
        Integer n = 3;
        DEBUG = Log.isLoggable((String)TAG, (int)3);
        Integer n2 = 1;
        Integer n3 = 2;
        Integer n4 = 8;
        ROTATION_ORDER = Arrays.asList(n2, 6, n, n4);
        Integer n5 = 7;
        Integer n6 = 5;
        FLIPPED_ROTATION_ORDER = Arrays.asList(n3, n5, 4, n6);
        BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
        BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
        BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
        JPEG_SIGNATURE = new byte[]{-1, -40, -1};
        HEIF_TYPE_FTYP = new byte[]{102, 116, 121, 112};
        HEIF_BRAND_MIF1 = new byte[]{109, 105, 102, 49};
        HEIF_BRAND_HEIC = new byte[]{104, 101, 105, 99};
        ORF_MAKER_NOTE_HEADER_1 = new byte[]{79, 76, 89, 77, 80, 0};
        ORF_MAKER_NOTE_HEADER_2 = new byte[]{79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
        PNG_SIGNATURE = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
        PNG_CHUNK_TYPE_EXIF = new byte[]{101, 88, 73, 102};
        PNG_CHUNK_TYPE_IHDR = new byte[]{73, 72, 68, 82};
        PNG_CHUNK_TYPE_IEND = new byte[]{73, 69, 78, 68};
        WEBP_SIGNATURE_1 = new byte[]{82, 73, 70, 70};
        WEBP_SIGNATURE_2 = new byte[]{87, 69, 66, 80};
        WEBP_CHUNK_TYPE_EXIF = new byte[]{69, 88, 73, 70};
        WEBP_VP8_SIGNATURE = new byte[]{-99, 1, 42};
        WEBP_CHUNK_TYPE_VP8X = "VP8X".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_VP8L = "VP8L".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_VP8 = "VP8 ".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_ANIM = "ANIM".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_ANMF = "ANMF".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_XMP = "XMP ".getBytes(Charset.defaultCharset());
        IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};
        IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
        EXIF_ASCII_PREFIX = new byte[]{65, 83, 67, 73, 73, 0, 0, 0};
        ExifTag[] exifTagArray = new ExifTag[]{new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, 272, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, 283, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, 306, 2), new ExifTag(TAG_ARTIST, 315, 2), new ExifTag(TAG_WHITE_POINT, 318, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_RW2_SENSOR_TOP_BORDER, 4, 4), new ExifTag(TAG_RW2_SENSOR_LEFT_BORDER, 5, 4), new ExifTag(TAG_RW2_SENSOR_BOTTOM_BORDER, 6, 4), new ExifTag(TAG_RW2_SENSOR_RIGHT_BORDER, 7, 4), new ExifTag(TAG_RW2_ISO, 23, 3), new ExifTag(TAG_RW2_JPG_FROM_RAW, 46, 7), new ExifTag(TAG_XMP, 700, 1)};
        IFD_TIFF_TAGS = exifTagArray;
        ExifTag[] exifTagArray2 = new ExifTag[]{new ExifTag(TAG_EXPOSURE_TIME, 33434, 5), new ExifTag(TAG_F_NUMBER, 33437, 5), new ExifTag(TAG_EXPOSURE_PROGRAM, 34850, 3), new ExifTag(TAG_SPECTRAL_SENSITIVITY, 34852, 2), new ExifTag(TAG_PHOTOGRAPHIC_SENSITIVITY, 34855, 3), new ExifTag(TAG_OECF, 34856, 7), new ExifTag(TAG_SENSITIVITY_TYPE, 34864, 3), new ExifTag(TAG_STANDARD_OUTPUT_SENSITIVITY, 34865, 4), new ExifTag(TAG_RECOMMENDED_EXPOSURE_INDEX, 34866, 4), new ExifTag(TAG_ISO_SPEED, 34867, 4), new ExifTag(TAG_ISO_SPEED_LATITUDE_YYY, 34868, 4), new ExifTag(TAG_ISO_SPEED_LATITUDE_ZZZ, 34869, 4), new ExifTag(TAG_EXIF_VERSION, 36864, 2), new ExifTag(TAG_DATETIME_ORIGINAL, 36867, 2), new ExifTag(TAG_DATETIME_DIGITIZED, 36868, 2), new ExifTag(TAG_OFFSET_TIME, 36880, 2), new ExifTag(TAG_OFFSET_TIME_ORIGINAL, 36881, 2), new ExifTag(TAG_OFFSET_TIME_DIGITIZED, 36882, 2), new ExifTag(TAG_COMPONENTS_CONFIGURATION, 37121, 7), new ExifTag(TAG_COMPRESSED_BITS_PER_PIXEL, 37122, 5), new ExifTag(TAG_SHUTTER_SPEED_VALUE, 37377, 10), new ExifTag(TAG_APERTURE_VALUE, 37378, 5), new ExifTag(TAG_BRIGHTNESS_VALUE, 37379, 10), new ExifTag(TAG_EXPOSURE_BIAS_VALUE, 37380, 10), new ExifTag(TAG_MAX_APERTURE_VALUE, 37381, 5), new ExifTag(TAG_SUBJECT_DISTANCE, 37382, 5), new ExifTag(TAG_METERING_MODE, 37383, 3), new ExifTag(TAG_LIGHT_SOURCE, 37384, 3), new ExifTag(TAG_FLASH, 37385, 3), new ExifTag(TAG_FOCAL_LENGTH, 37386, 5), new ExifTag(TAG_SUBJECT_AREA, 37396, 3), new ExifTag(TAG_MAKER_NOTE, 37500, 7), new ExifTag(TAG_USER_COMMENT, 37510, 7), new ExifTag(TAG_SUBSEC_TIME, 37520, 2), new ExifTag(TAG_SUBSEC_TIME_ORIGINAL, 37521, 2), new ExifTag(TAG_SUBSEC_TIME_DIGITIZED, 37522, 2), new ExifTag(TAG_FLASHPIX_VERSION, 40960, 7), new ExifTag(TAG_COLOR_SPACE, 40961, 3), new ExifTag(TAG_PIXEL_X_DIMENSION, 40962, 3, 4), new ExifTag(TAG_PIXEL_Y_DIMENSION, 40963, 3, 4), new ExifTag(TAG_RELATED_SOUND_FILE, 40964, 2), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_FLASH_ENERGY, 41483, 5), new ExifTag(TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, 7), new ExifTag(TAG_FOCAL_PLANE_X_RESOLUTION, 41486, 5), new ExifTag(TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, 5), new ExifTag(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, 3), new ExifTag(TAG_SUBJECT_LOCATION, 41492, 3), new ExifTag(TAG_EXPOSURE_INDEX, 41493, 5), new ExifTag(TAG_SENSING_METHOD, 41495, 3), new ExifTag(TAG_FILE_SOURCE, 41728, 7), new ExifTag(TAG_SCENE_TYPE, 41729, 7), new ExifTag(TAG_CFA_PATTERN, 41730, 7), new ExifTag(TAG_CUSTOM_RENDERED, 41985, 3), new ExifTag(TAG_EXPOSURE_MODE, 41986, 3), new ExifTag(TAG_WHITE_BALANCE, 41987, 3), new ExifTag(TAG_DIGITAL_ZOOM_RATIO, 41988, 5), new ExifTag(TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, 3), new ExifTag(TAG_SCENE_CAPTURE_TYPE, 41990, 3), new ExifTag(TAG_GAIN_CONTROL, 41991, 3), new ExifTag(TAG_CONTRAST, 41992, 3), new ExifTag(TAG_SATURATION, 41993, 3), new ExifTag(TAG_SHARPNESS, 41994, 3), new ExifTag(TAG_DEVICE_SETTING_DESCRIPTION, 41995, 7), new ExifTag(TAG_SUBJECT_DISTANCE_RANGE, 41996, 3), new ExifTag(TAG_IMAGE_UNIQUE_ID, 42016, 2), new ExifTag("CameraOwnerName", 42032, 2), new ExifTag(TAG_BODY_SERIAL_NUMBER, 42033, 2), new ExifTag(TAG_LENS_SPECIFICATION, 42034, 5), new ExifTag(TAG_LENS_MAKE, 42035, 2), new ExifTag(TAG_LENS_MODEL, 42036, 2), new ExifTag(TAG_GAMMA, 42240, 5), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
        IFD_EXIF_TAGS = exifTagArray2;
        Object object = new ExifTag[]{new ExifTag(TAG_GPS_VERSION_ID, 0, 1), new ExifTag(TAG_GPS_LATITUDE_REF, 1, 2), new ExifTag(TAG_GPS_LATITUDE, 2, 5), new ExifTag(TAG_GPS_LONGITUDE_REF, 3, 2), new ExifTag(TAG_GPS_LONGITUDE, 4, 5), new ExifTag(TAG_GPS_ALTITUDE_REF, 5, 1), new ExifTag(TAG_GPS_ALTITUDE, 6, 5), new ExifTag(TAG_GPS_TIMESTAMP, 7, 5), new ExifTag(TAG_GPS_SATELLITES, 8, 2), new ExifTag(TAG_GPS_STATUS, 9, 2), new ExifTag(TAG_GPS_MEASURE_MODE, 10, 2), new ExifTag(TAG_GPS_DOP, 11, 5), new ExifTag(TAG_GPS_SPEED_REF, 12, 2), new ExifTag(TAG_GPS_SPEED, 13, 5), new ExifTag(TAG_GPS_TRACK_REF, 14, 2), new ExifTag(TAG_GPS_TRACK, 15, 5), new ExifTag(TAG_GPS_IMG_DIRECTION_REF, 16, 2), new ExifTag(TAG_GPS_IMG_DIRECTION, 17, 5), new ExifTag(TAG_GPS_MAP_DATUM, 18, 2), new ExifTag(TAG_GPS_DEST_LATITUDE_REF, 19, 2), new ExifTag(TAG_GPS_DEST_LATITUDE, 20, 5), new ExifTag(TAG_GPS_DEST_LONGITUDE_REF, 21, 2), new ExifTag(TAG_GPS_DEST_LONGITUDE, 22, 5), new ExifTag(TAG_GPS_DEST_BEARING_REF, 23, 2), new ExifTag(TAG_GPS_DEST_BEARING, 24, 5), new ExifTag(TAG_GPS_DEST_DISTANCE_REF, 25, 2), new ExifTag(TAG_GPS_DEST_DISTANCE, 26, 5), new ExifTag(TAG_GPS_PROCESSING_METHOD, 27, 7), new ExifTag(TAG_GPS_AREA_INFORMATION, 28, 7), new ExifTag(TAG_GPS_DATESTAMP, 29, 2), new ExifTag(TAG_GPS_DIFFERENTIAL, 30, 3), new ExifTag(TAG_GPS_H_POSITIONING_ERROR, 31, 5)};
        IFD_GPS_TAGS = object;
        Object object2 = new ExifTag[]{new ExifTag(TAG_INTEROPERABILITY_INDEX, 1, 2)};
        IFD_INTEROPERABILITY_TAGS = object2;
        ExifTag[] exifTagArray3 = new ExifTag[]{new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, 272, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_THUMBNAIL_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, 283, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, 306, 2), new ExifTag(TAG_ARTIST, 315, 2), new ExifTag(TAG_WHITE_POINT, 318, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
        IFD_THUMBNAIL_TAGS = exifTagArray3;
        TAG_RAF_IMAGE_SIZE = new ExifTag(TAG_STRIP_OFFSETS, 273, 3);
        ExifTag[] exifTagArray4 = new ExifTag[]{new ExifTag(TAG_ORF_THUMBNAIL_IMAGE, 256, 7), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 4), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 4)};
        ORF_MAKER_NOTE_TAGS = exifTagArray4;
        ExifTag[] exifTagArray5 = new ExifTag[]{new ExifTag(TAG_ORF_PREVIEW_IMAGE_START, 257, 4), new ExifTag(TAG_ORF_PREVIEW_IMAGE_LENGTH, 258, 4)};
        ORF_CAMERA_SETTINGS_TAGS = exifTagArray5;
        ExifTag[] exifTagArray6 = new ExifTag[]{new ExifTag(TAG_ORF_ASPECT_FRAME, 4371, 3)};
        ORF_IMAGE_PROCESSING_TAGS = exifTagArray6;
        ExifTag[] exifTagArray7 = new ExifTag[]{new ExifTag(TAG_COLOR_SPACE, 55, 3)};
        PEF_TAGS = exifTagArray7;
        ExifTag[][] exifTagArrayArray = new ExifTag[][]{exifTagArray, exifTagArray2, object, object2, exifTagArray3, exifTagArray, exifTagArray4, exifTagArray5, exifTagArray6, exifTagArray7};
        EXIF_TAGS = exifTagArrayArray;
        EXIF_POINTER_TAGS = new ExifTag[]{new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 1), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 1)};
        JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, 513, 4);
        JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4);
        sExifTagMapsForReading = new HashMap[exifTagArrayArray.length];
        sExifTagMapsForWriting = new HashMap[exifTagArrayArray.length];
        sTagSetForCompatibility = new HashSet<String>(Arrays.asList(TAG_F_NUMBER, TAG_DIGITAL_ZOOM_RATIO, TAG_EXPOSURE_TIME, TAG_SUBJECT_DISTANCE, TAG_GPS_TIMESTAMP));
        sExifPointerTagMap = new HashMap();
        object = Charset.forName("US-ASCII");
        ASCII = object;
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes((Charset)object);
        IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes((Charset)object);
        sFormatter = object = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        ((DateFormat)object).setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < ((Object)(object = EXIF_TAGS)).length; ++i) {
            ExifInterface.sExifTagMapsForReading[i] = new HashMap();
            ExifInterface.sExifTagMapsForWriting[i] = new HashMap();
            object = object[i];
            int n7 = ((Object)object).length;
            for (int j = 0; j < n7; ++j) {
                object2 = object[j];
                sExifTagMapsForReading[i].put(object2.number, (ExifTag)object2);
                sExifTagMapsForWriting[i].put(object2.name, (ExifTag)object2);
            }
        }
        object = sExifPointerTagMap;
        object2 = EXIF_POINTER_TAGS;
        ((HashMap)object).put(object2[0].number, n6);
        ((HashMap)object).put(object2[1].number, n2);
        ((HashMap)object).put(object2[2].number, n3);
        ((HashMap)object).put(object2[3].number, n);
        ((HashMap)object).put(object2[4].number, n5);
        ((HashMap)object).put(object2[5].number, n4);
        sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
        sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    }

    public ExifInterface(File file) throws IOException {
        ExifTag[][] exifTagArray = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArray.length];
        this.mAttributesOffsets = new HashSet<Integer>(exifTagArray.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (file != null) {
            this.initForFilename(file.getAbsolutePath());
            return;
        }
        throw new NullPointerException("file cannot be null");
    }

    public ExifInterface(FileDescriptor fileDescriptor) throws IOException {
        Object object = EXIF_TAGS;
        this.mAttributes = new HashMap[((ExifTag[][])object).length];
        this.mAttributesOffsets = new HashSet<Integer>(((ExifTag[][])object).length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (fileDescriptor != null) {
            this.mAssetInputStream = null;
            this.mFilename = null;
            boolean bl = false;
            if (Build.VERSION.SDK_INT >= 21 && ExifInterface.isSeekableFD(fileDescriptor)) {
                this.mSeekableFileDescriptor = fileDescriptor;
                try {
                    fileDescriptor = Os.dup((FileDescriptor)fileDescriptor);
                    bl = true;
                }
                catch (Exception exception) {
                    throw new IOException("Failed to duplicate file descriptor", exception);
                }
            } else {
                this.mSeekableFileDescriptor = null;
            }
            FileInputStream fileInputStream = null;
            object = fileInputStream;
            object = fileInputStream;
            try {
                FileInputStream fileInputStream2;
                fileInputStream = fileInputStream2 = new FileInputStream(fileDescriptor);
                object = fileInputStream;
            }
            catch (Throwable throwable) {
                ExifInterface.closeQuietly((Closeable)object);
                if (bl) {
                    ExifInterface.closeFileDescriptor(fileDescriptor);
                }
                throw throwable;
            }
            this.loadAttributes(fileInputStream);
            ExifInterface.closeQuietly(fileInputStream);
            if (bl) {
                ExifInterface.closeFileDescriptor(fileDescriptor);
            }
            return;
        }
        throw new NullPointerException("fileDescriptor cannot be null");
    }

    public ExifInterface(InputStream inputStream) throws IOException {
        this(inputStream, false);
    }

    public ExifInterface(InputStream inputStream, int n) throws IOException {
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        this(inputStream, bl);
    }

    private ExifInterface(InputStream inputStream, boolean bl) throws IOException {
        ExifTag[][] exifTagArray = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArray.length];
        this.mAttributesOffsets = new HashSet<Integer>(exifTagArray.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (inputStream != null) {
            this.mFilename = null;
            if (bl) {
                if (!ExifInterface.isExifDataOnly((BufferedInputStream)(inputStream = new BufferedInputStream(inputStream, 5000)))) {
                    Log.w((String)TAG, (String)"Given data does not follow the structure of an Exif-only data.");
                    return;
                }
                this.mIsExifDataOnly = true;
                this.mAssetInputStream = null;
                this.mSeekableFileDescriptor = null;
            } else if (inputStream instanceof AssetManager.AssetInputStream) {
                this.mAssetInputStream = (AssetManager.AssetInputStream)inputStream;
                this.mSeekableFileDescriptor = null;
            } else if (inputStream instanceof FileInputStream && ExifInterface.isSeekableFD(((FileInputStream)inputStream).getFD())) {
                this.mAssetInputStream = null;
                this.mSeekableFileDescriptor = ((FileInputStream)inputStream).getFD();
            } else {
                this.mAssetInputStream = null;
                this.mSeekableFileDescriptor = null;
            }
            this.loadAttributes(inputStream);
            return;
        }
        throw new NullPointerException("inputStream cannot be null");
    }

    public ExifInterface(String string2) throws IOException {
        ExifTag[][] exifTagArray = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArray.length];
        this.mAttributesOffsets = new HashSet<Integer>(exifTagArray.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (string2 != null) {
            this.initForFilename(string2);
            return;
        }
        throw new NullPointerException("filename cannot be null");
    }

    private void addDefaultValuesForCompatibility() {
        String string2 = this.getAttribute(TAG_DATETIME_ORIGINAL);
        if (string2 != null && this.getAttribute(TAG_DATETIME) == null) {
            this.mAttributes[0].put(TAG_DATETIME, ExifAttribute.createString(string2));
        }
        if (this.getAttribute(TAG_IMAGE_WIDTH) == null) {
            this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute(TAG_IMAGE_LENGTH) == null) {
            this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute(TAG_ORIENTATION) == null) {
            this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute(TAG_LIGHT_SOURCE) == null) {
            this.mAttributes[1].put(TAG_LIGHT_SOURCE, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
    }

    private static String byteArrayToHexString(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder(byArray.length * 2);
        for (int i = 0; i < byArray.length; ++i) {
            stringBuilder.append(String.format("%02x", byArray[i]));
        }
        return stringBuilder.toString();
    }

    private static void closeFileDescriptor(FileDescriptor fileDescriptor) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Os.close((FileDescriptor)fileDescriptor);
            }
            catch (Exception exception) {
                Log.e((String)TAG, (String)"Error closing fd.");
            }
        } else {
            Log.e((String)TAG, (String)"closeFileDescriptor is called in API < 21, which must be wrong.");
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception exception) {
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    private String convertDecimalDegree(double d) {
        long l = (long)d;
        double d2 = l;
        Double.isNaN(d2);
        long l2 = (long)((d - d2) * 60.0);
        d2 = l;
        Double.isNaN(d2);
        double d3 = l2;
        Double.isNaN(d3);
        long l3 = Math.round((d - d2 - d3 / 60.0) * 3600.0 * 1.0E7);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(l);
        stringBuilder.append("/1,");
        stringBuilder.append(l2);
        stringBuilder.append("/1,");
        stringBuilder.append(l3);
        stringBuilder.append("/10000000");
        return stringBuilder.toString();
    }

    private static double convertRationalLatLonToDouble(String object, String string2) {
        block6: {
            double d;
            block5: {
                object = object.split(",", -1);
                String[] stringArray = object[0].split("/", -1);
                d = Double.parseDouble(stringArray[0].trim()) / Double.parseDouble(stringArray[1].trim());
                stringArray = object[1].split("/", -1);
                double d2 = Double.parseDouble(stringArray[0].trim()) / Double.parseDouble(stringArray[1].trim());
                object = object[2].split("/", -1);
                double d3 = Double.parseDouble(object[0].trim()) / Double.parseDouble(object[1].trim());
                d = d2 / 60.0 + d + d3 / 3600.0;
                try {
                    if (string2.equals(LATITUDE_SOUTH) || string2.equals(LONGITUDE_WEST)) break block5;
                    if (!string2.equals("N") && !string2.equals(LONGITUDE_EAST)) {
                        object = new IllegalArgumentException();
                        throw object;
                    }
                    return d;
                }
                catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    break block6;
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            return -d;
        }
        throw new IllegalArgumentException();
    }

    private static long[] convertToLongArray(Object object) {
        if (object instanceof int[]) {
            int[] nArray = (int[])object;
            object = new long[nArray.length];
            for (int i = 0; i < nArray.length; ++i) {
                object[i] = (long)nArray[i];
            }
            return object;
        }
        if (object instanceof long[]) {
            return (long[])object;
        }
        return null;
    }

    private static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int n;
        int n2 = 0;
        byte[] byArray = new byte[8192];
        while ((n = inputStream.read(byArray)) != -1) {
            n2 += n;
            outputStream.write(byArray, 0, n);
        }
        return n2;
    }

    private static void copy(InputStream inputStream, OutputStream outputStream, int n) throws IOException {
        byte[] byArray = new byte[8192];
        while (n > 0) {
            int n2 = Math.min(n, 8192);
            int n3 = inputStream.read(byArray, 0, n2);
            if (n3 == n2) {
                n -= n3;
                outputStream.write(byArray, 0, n3);
                continue;
            }
            throw new IOException("Failed to copy the given amount of bytes from the inputstream to the output stream.");
        }
    }

    private void copyChunksUpToGivenChunkType(ByteOrderedDataInputStream object, ByteOrderedDataOutputStream object2, byte[] object3, byte[] byArray) throws IOException {
        byte[] byArray2;
        do {
            if (((InputStream)object).read(byArray2 = new byte[4]) != byArray2.length) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Encountered invalid length while copying WebP chunks up tochunk type ");
                object = ASCII;
                ((StringBuilder)object2).append(new String((byte[])object3, (Charset)object));
                if (byArray == null) {
                    object = "";
                } else {
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append(" or ");
                    ((StringBuilder)object3).append(new String(byArray, (Charset)object));
                    object = ((StringBuilder)object3).toString();
                }
                ((StringBuilder)object2).append((String)object);
                throw new IOException(((StringBuilder)object2).toString());
            }
            this.copyWebPChunk((ByteOrderedDataInputStream)object, (ByteOrderedDataOutputStream)object2, byArray2);
        } while (!Arrays.equals(byArray2, (byte[])object3) && (byArray == null || !Arrays.equals(byArray2, byArray)));
    }

    private void copyWebPChunk(ByteOrderedDataInputStream byteOrderedDataInputStream, ByteOrderedDataOutputStream byteOrderedDataOutputStream, byte[] byArray) throws IOException {
        int n = byteOrderedDataInputStream.readInt();
        byteOrderedDataOutputStream.write(byArray);
        byteOrderedDataOutputStream.writeInt(n);
        if (n % 2 == 1) {
            ++n;
        }
        ExifInterface.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, n);
    }

    private ExifAttribute getExifAttribute(String object) {
        if (object != null) {
            String string2 = object;
            if (TAG_ISO_SPEED_RATINGS.equals(object)) {
                if (DEBUG) {
                    Log.d((String)TAG, (String)"getExifAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
                }
                string2 = TAG_PHOTOGRAPHIC_SENSITIVITY;
            }
            for (int i = 0; i < EXIF_TAGS.length; ++i) {
                object = this.mAttributes[i].get(string2);
                if (object == null) continue;
                return object;
            }
            return null;
        }
        object = new NullPointerException("tag shouldn't be null");
        throw object;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private void getHeifAttributes(ByteOrderedDataInputStream var1_1) throws IOException {
        block32: {
            block26: {
                block29: {
                    block28: {
                        block31: {
                            block27: {
                                block30: {
                                    var10_6 = new MediaMetadataRetriever();
                                    if (Build.VERSION.SDK_INT < 23) break block30;
                                    var7_7 /* !! */  = new MediaDataSource(this, (ByteOrderedDataInputStream)var1_1){
                                        long mPosition;
                                        final ExifInterface this$0;
                                        final ByteOrderedDataInputStream val$in;
                                        {
                                            this.this$0 = exifInterface;
                                            this.val$in = byteOrderedDataInputStream;
                                        }

                                        public void close() throws IOException {
                                        }

                                        public long getSize() throws IOException {
                                            return -1L;
                                        }

                                        public int readAt(long l, byte[] byArray, int n, int n2) throws IOException {
                                            block11: {
                                                block9: {
                                                    block10: {
                                                        if (n2 == 0) {
                                                            return 0;
                                                        }
                                                        if (l < 0L) {
                                                            return -1;
                                                        }
                                                        long l2 = this.mPosition;
                                                        if (l2 == l) break block9;
                                                        if (l2 < 0L) break block10;
                                                        if (l < l2 + (long)this.val$in.available()) break block10;
                                                        return -1;
                                                    }
                                                    this.val$in.seek(l);
                                                    this.mPosition = l;
                                                }
                                                int n3 = n2;
                                                if (n2 > this.val$in.available()) {
                                                    n3 = this.val$in.available();
                                                }
                                                if ((n = this.val$in.read(byArray, n, n3)) < 0) break block11;
                                                try {
                                                    this.mPosition += (long)n;
                                                    return n;
                                                }
                                                catch (IOException iOException) {
                                                    // empty catch block
                                                }
                                            }
                                            this.mPosition = -1L;
                                            return -1;
                                        }
                                    };
                                    var10_6.setDataSource((MediaDataSource)var7_7 /* !! */ );
                                    ** GOTO lbl17
                                }
                                var7_7 /* !! */  = this.mSeekableFileDescriptor;
                                if (var7_7 /* !! */  == null) ** GOTO lbl13
                                var10_6.setDataSource((FileDescriptor)var7_7 /* !! */ );
                                ** GOTO lbl17
lbl13:
                                // 1 sources

                                var7_7 /* !! */  = this.mFilename;
                                if (var7_7 /* !! */  == null) break block26;
                                var10_6.setDataSource((String)var7_7 /* !! */ );
lbl17:
                                // 3 sources

                                var11_8 = var10_6.extractMetadata(33);
                                var12_9 = var10_6.extractMetadata(34);
                                var14_10 = var10_6.extractMetadata(26);
                                var13_11 = var10_6.extractMetadata(17);
                                var7_7 /* !! */  = null;
                                var8_12 = null;
                                var9_13 = null;
                                if ("yes".equals(var14_10)) {
                                    var7_7 /* !! */  = var10_6.extractMetadata(29);
                                    var8_12 = var10_6.extractMetadata(30);
                                    var9_13 = var10_6.extractMetadata(31);
                                } else if ("yes".equals(var13_11)) {
                                    var7_7 /* !! */  = var10_6.extractMetadata(18);
                                    var8_12 = var10_6.extractMetadata(19);
                                    var9_13 = var10_6.extractMetadata(24);
                                }
                                if (var7_7 /* !! */  == null) break block27;
                                this.mAttributes[0].put("ImageWidth", ExifAttribute.createUShort(Integer.parseInt((String)var7_7 /* !! */ ), this.mExifByteOrder));
                            }
                            if (var8_12 != null) {
                                this.mAttributes[0].put("ImageLength", ExifAttribute.createUShort(Integer.parseInt(var8_12), this.mExifByteOrder));
                            }
                            if (var9_13 != null) {
                                var2_14 = 1;
                                switch (Integer.parseInt(var9_13)) {
                                    default: {
                                        break;
                                    }
                                    case 270: {
                                        var2_14 = 8;
                                        break;
                                    }
                                    case 180: {
                                        var2_14 = 3;
                                        break;
                                    }
                                    case 90: {
                                        var2_14 = 6;
                                    }
                                }
                                this.mAttributes[0].put("Orientation", ExifAttribute.createUShort(var2_14, this.mExifByteOrder));
                            }
                            if (var11_8 == null || var12_9 == null) ** GOTO lbl94
                            var2_14 = Integer.parseInt((String)var11_8);
                            var3_15 = Integer.parseInt(var12_9);
                            if (var3_15 <= 6) break block28;
                            var5_16 = var2_14;
                            try {
                                var1_1.seek(var5_16);
                                var11_8 = new byte[6];
                                if (var1_1.read((byte[])var11_8) != 6) ** GOTO lbl83
                                var3_15 -= 6;
                            }
                            catch (Throwable var1_2) {}
                            if (!Arrays.equals((byte[])var11_8, ExifInterface.IDENTIFIER_EXIF_APP1)) ** GOTO lbl81
                            var11_8 = new byte[var3_15];
                            var4_17 = var1_1.read((byte[])var11_8);
                            if (var4_17 != var3_15) ** GOTO lbl79
                            break block31;
lbl79:
                            // 1 sources

                            var1_1 = new IOException("Can't read exif");
                            throw var1_1;
lbl81:
                            // 1 sources

                            var1_1 = new IOException("Invalid identifier");
                            throw var1_1;
lbl83:
                            // 1 sources

                            var1_1 = new IOException("Can't read identifier");
                            throw var1_1;
                        }
                        this.mExifOffset = var2_14 + 6;
                        this.readExifSegment((byte[])var11_8, 0);
                        ** GOTO lbl94
                        break block32;
                    }
                    try {
                        var1_1 = new IOException("Invalid exif length");
                        throw var1_1;
lbl94:
                        // 2 sources

                        if (!ExifInterface.DEBUG) break block29;
                        var1_1 = new StringBuilder();
                        var1_1.append("Heif meta: ");
                        var1_1.append((String)var7_7 /* !! */ );
                        var1_1.append("x");
                        var1_1.append(var8_12);
                        var1_1.append(", rotation ");
                        var1_1.append(var9_13);
                        Log.d((String)"ExifInterface", (String)var1_1.toString());
                    }
                    catch (Throwable var1_3) {}
                }
                var10_6.release();
                return;
                break block32;
            }
            var10_6.release();
            return;
            catch (Throwable var1_4) {
                // empty catch block
            }
        }
        var10_6.release();
        throw var1_5;
    }

    private void getJpegAttributes(ByteOrderedDataInputStream object, int n, int n2) throws IOException {
        Object object2;
        boolean bl = DEBUG;
        String string2 = TAG;
        if (bl) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("getJpegAttributes starting with: ");
            ((StringBuilder)object2).append(object);
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        ((ByteOrderedDataInputStream)object).setByteOrder(ByteOrder.BIG_ENDIAN);
        ((ByteOrderedDataInputStream)object).seek(n);
        int n3 = ((ByteOrderedDataInputStream)object).readByte();
        if (n3 == -1) {
            if (((ByteOrderedDataInputStream)object).readByte() == -40) {
                n = n + 1 + 1;
                while ((n3 = ((ByteOrderedDataInputStream)object).readByte()) == -1) {
                    int n4 = ((ByteOrderedDataInputStream)object).readByte();
                    bl = DEBUG;
                    if (bl) {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Found JPEG segment indicator: ");
                        ((StringBuilder)object2).append(Integer.toHexString(n4 & 0xFF));
                        Log.d((String)string2, (String)((StringBuilder)object2).toString());
                    }
                    if (n4 != -39 && n4 != -38) {
                        int n5 = ((ByteOrderedDataInputStream)object).readUnsignedShort() - 2;
                        n3 = n + 1 + 1 + 2;
                        if (bl) {
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("JPEG segment: ");
                            ((StringBuilder)object2).append(Integer.toHexString(n4 & 0xFF));
                            ((StringBuilder)object2).append(" (length: ");
                            ((StringBuilder)object2).append(n5 + 2);
                            ((StringBuilder)object2).append(")");
                            Log.d((String)string2, (String)((StringBuilder)object2).toString());
                        }
                        if (n5 >= 0) {
                            switch (n4) {
                                default: {
                                    n = n5;
                                    break;
                                }
                                case -2: {
                                    object2 = new byte[n5];
                                    if (((InputStream)object).read((byte[])object2) == n5) {
                                        n = 0;
                                        if (this.getAttribute(TAG_USER_COMMENT) != null) break;
                                        this.mAttributes[1].put(TAG_USER_COMMENT, ExifAttribute.createString(new String((byte[])object2, ASCII)));
                                        break;
                                    }
                                    throw new IOException("Invalid exif");
                                }
                                case -31: {
                                    object2 = new byte[n5];
                                    ((ByteOrderedDataInputStream)object).readFully((byte[])object2);
                                    n5 = n3 + n5;
                                    n = 0;
                                    byte[] byArray = IDENTIFIER_EXIF_APP1;
                                    if (ExifInterface.startsWith((byte[])object2, byArray)) {
                                        n4 = byArray.length;
                                        object2 = Arrays.copyOfRange((byte[])object2, byArray.length, ((Object)object2).length);
                                        this.mExifOffset = n4 + n3;
                                        this.readExifSegment((byte[])object2, n2);
                                    } else {
                                        byArray = IDENTIFIER_XMP_APP1;
                                        if (ExifInterface.startsWith((byte[])object2, byArray)) {
                                            n4 = byArray.length;
                                            object2 = Arrays.copyOfRange((byte[])object2, byArray.length, ((Object)object2).length);
                                            if (this.getAttribute(TAG_XMP) == null) {
                                                this.mAttributes[0].put(TAG_XMP, new ExifAttribute(1, ((Object)object2).length, n4 + n3, (byte[])object2));
                                                this.mXmpIsFromSeparateMarker = true;
                                            }
                                        }
                                    }
                                    n3 = n5;
                                    break;
                                }
                                case -64: 
                                case -63: 
                                case -62: 
                                case -61: 
                                case -59: 
                                case -58: 
                                case -57: 
                                case -55: 
                                case -54: 
                                case -53: 
                                case -51: 
                                case -50: 
                                case -49: {
                                    if (((ByteOrderedDataInputStream)object).skipBytes(1) == 1) {
                                        this.mAttributes[n2].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong(((ByteOrderedDataInputStream)object).readUnsignedShort(), this.mExifByteOrder));
                                        this.mAttributes[n2].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong(((ByteOrderedDataInputStream)object).readUnsignedShort(), this.mExifByteOrder));
                                        n = n5 - 5;
                                        break;
                                    }
                                    throw new IOException("Invalid SOFx");
                                }
                            }
                            if (n >= 0) {
                                if (((ByteOrderedDataInputStream)object).skipBytes(n) == n) {
                                    n = n3 + n;
                                    continue;
                                }
                                throw new IOException("Invalid JPEG segment");
                            }
                            throw new IOException("Invalid length");
                        }
                        throw new IOException("Invalid length");
                    }
                    ((ByteOrderedDataInputStream)object).setByteOrder(this.mExifByteOrder);
                    return;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Invalid marker:");
                ((StringBuilder)object).append(Integer.toHexString(n3 & 0xFF));
                throw new IOException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Invalid marker: ");
            ((StringBuilder)object).append(Integer.toHexString(n3 & 0xFF));
            throw new IOException(((StringBuilder)object).toString());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Invalid marker: ");
        ((StringBuilder)object).append(Integer.toHexString(n3 & 0xFF));
        object = new IOException(((StringBuilder)object).toString());
        throw object;
    }

    private int getMimeType(BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(5000);
        byte[] byArray = new byte[5000];
        bufferedInputStream.read(byArray);
        bufferedInputStream.reset();
        if (ExifInterface.isJpegFormat(byArray)) {
            return 4;
        }
        if (this.isRafFormat(byArray)) {
            return 9;
        }
        if (this.isHeifFormat(byArray)) {
            return 12;
        }
        if (this.isOrfFormat(byArray)) {
            return 7;
        }
        if (this.isRw2Format(byArray)) {
            return 10;
        }
        if (this.isPngFormat(byArray)) {
            return 13;
        }
        if (this.isWebpFormat(byArray)) {
            return 14;
        }
        return 0;
    }

    private void getOrfAttributes(ByteOrderedDataInputStream object) throws IOException {
        this.getRawAttributes((ByteOrderedDataInputStream)object);
        object = this.mAttributes[1].get(TAG_MAKER_NOTE);
        if (object != null) {
            object = new ByteOrderedDataInputStream(((ExifAttribute)object).bytes);
            ((ByteOrderedDataInputStream)object).setByteOrder(this.mExifByteOrder);
            Object object2 = ORF_MAKER_NOTE_HEADER_1;
            byte[] byArray = new byte[((byte[])object2).length];
            ((ByteOrderedDataInputStream)object).readFully(byArray);
            ((ByteOrderedDataInputStream)object).seek(0L);
            byte[] byArray2 = ORF_MAKER_NOTE_HEADER_2;
            byte[] byArray3 = new byte[byArray2.length];
            ((ByteOrderedDataInputStream)object).readFully(byArray3);
            if (Arrays.equals(byArray, (byte[])object2)) {
                ((ByteOrderedDataInputStream)object).seek(8L);
            } else if (Arrays.equals(byArray3, byArray2)) {
                ((ByteOrderedDataInputStream)object).seek(12L);
            }
            this.readImageFileDirectory((ByteOrderedDataInputStream)object, 6);
            object2 = this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_START);
            object = this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_LENGTH);
            if (object2 != null && object != null) {
                this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT, (ExifAttribute)object2);
                this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, (ExifAttribute)object);
            }
            if ((object = this.mAttributes[8].get(TAG_ORF_ASPECT_FRAME)) != null) {
                if ((object = (Object)((int[])((ExifAttribute)object).getValue(this.mExifByteOrder))) != null && ((Object)object).length == 4) {
                    if (object[2] > object[0] && object[3] > object[1]) {
                        reference var3_10;
                        reference var2_12;
                        reference var5_6 = object[2] - object[0] + true;
                        reference var4_7 = object[3] - object[1] + true;
                        reference var3_8 = var5_6;
                        reference var2_11 = var4_7;
                        if (var5_6 < var4_7) {
                            reference var3_9 = var5_6 + var4_7;
                            var2_12 = var3_9 - var4_7;
                            var3_10 = var3_9 - var2_12;
                        }
                        object2 = ExifAttribute.createUShort((int)var3_10, this.mExifByteOrder);
                        object = ExifAttribute.createUShort((int)var2_12, this.mExifByteOrder);
                        this.mAttributes[0].put(TAG_IMAGE_WIDTH, (ExifAttribute)object2);
                        this.mAttributes[0].put(TAG_IMAGE_LENGTH, (ExifAttribute)object);
                    }
                } else {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Invalid aspect frame values. frame=");
                    ((StringBuilder)object2).append(Arrays.toString((int[])object));
                    Log.w((String)TAG, (String)((StringBuilder)object2).toString());
                    return;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void getPngAttributes(ByteOrderedDataInputStream object) throws IOException {
        Object object2;
        if (DEBUG) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("getPngAttributes starting with: ");
            ((StringBuilder)object2).append(object);
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        ((ByteOrderedDataInputStream)object).setByteOrder(ByteOrder.BIG_ENDIAN);
        object2 = PNG_SIGNATURE;
        ((ByteOrderedDataInputStream)object).skipBytes(((Object)object2).length);
        int n = 0 + ((Object)object2).length;
        try {
            while (true) {
                int n2 = ((ByteOrderedDataInputStream)object).readInt();
                object2 = new byte[4];
                if (((InputStream)object).read((byte[])object2) != ((Object)object2).length) {
                    object = new IOException("Encountered invalid length while parsing PNG chunktype");
                    throw object;
                }
                if ((n = n + 4 + 4) == 16 && !Arrays.equals((byte[])object2, PNG_CHUNK_TYPE_IHDR)) {
                    object = new IOException("Encountered invalid PNG file--IHDR chunk should appearas the first chunk");
                    throw object;
                }
                if (Arrays.equals((byte[])object2, PNG_CHUNK_TYPE_IEND)) {
                    return;
                }
                if (Arrays.equals((byte[])object2, PNG_CHUNK_TYPE_EXIF)) {
                    byte[] byArray = new byte[n2];
                    if (((InputStream)object).read(byArray) != n2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to read given length for given PNG chunk type: ");
                        stringBuilder.append(ExifInterface.byteArrayToHexString((byte[])object2));
                        object = new IOException(stringBuilder.toString());
                        throw object;
                    }
                    n2 = ((ByteOrderedDataInputStream)object).readInt();
                    object = new CRC32();
                    object.update((byte[])object2);
                    object.update(byArray);
                    if ((int)((CRC32)object).getValue() == n2) {
                        this.mExifOffset = n;
                        this.readExifSegment(byArray, 0);
                        this.validateImages();
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Encountered invalid CRC value for PNG-EXIF chunk.\n recorded CRC value: ");
                    stringBuilder.append(n2);
                    stringBuilder.append(", calculated CRC value: ");
                    stringBuilder.append(((CRC32)object).getValue());
                    object2 = new IOException(stringBuilder.toString());
                    throw object2;
                }
                ((ByteOrderedDataInputStream)object).skipBytes(n2 + 4);
                n += n2 + 4;
            }
        }
        catch (EOFException eOFException) {
            IOException iOException = new IOException("Encountered corrupt PNG file.");
            throw iOException;
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream object) throws IOException {
        ((ByteOrderedDataInputStream)object).skipBytes(84);
        byte[] byArray = new byte[4];
        Object object2 = new byte[4];
        ((InputStream)object).read(byArray);
        ((ByteOrderedDataInputStream)object).skipBytes(4);
        ((InputStream)object).read((byte[])object2);
        int n = ByteBuffer.wrap(byArray).getInt();
        int n2 = ByteBuffer.wrap((byte[])object2).getInt();
        this.getJpegAttributes((ByteOrderedDataInputStream)object, n, 5);
        ((ByteOrderedDataInputStream)object).seek(n2);
        ((ByteOrderedDataInputStream)object).setByteOrder(ByteOrder.BIG_ENDIAN);
        n2 = ((ByteOrderedDataInputStream)object).readInt();
        if (DEBUG) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("numberOfDirectoryEntry: ");
            ((StringBuilder)object2).append(n2);
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        for (n = 0; n < n2; ++n) {
            int n3 = ((ByteOrderedDataInputStream)object).readUnsignedShort();
            int n4 = ((ByteOrderedDataInputStream)object).readUnsignedShort();
            if (n3 == ExifInterface.TAG_RAF_IMAGE_SIZE.number) {
                n2 = ((ByteOrderedDataInputStream)object).readShort();
                n = ((ByteOrderedDataInputStream)object).readShort();
                object = ExifAttribute.createUShort(n2, this.mExifByteOrder);
                object2 = ExifAttribute.createUShort(n, this.mExifByteOrder);
                this.mAttributes[0].put(TAG_IMAGE_LENGTH, (ExifAttribute)object);
                this.mAttributes[0].put(TAG_IMAGE_WIDTH, (ExifAttribute)object2);
                if (DEBUG) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Updated to length: ");
                    ((StringBuilder)object).append(n2);
                    ((StringBuilder)object).append(", width: ");
                    ((StringBuilder)object).append(n);
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                }
                return;
            }
            ((ByteOrderedDataInputStream)object).skipBytes(n4);
        }
    }

    private void getRawAttributes(ByteOrderedDataInputStream object) throws IOException {
        this.parseTiffHeaders((ByteOrderedDataInputStream)object, ((ByteOrderedDataInputStream)object).available());
        this.readImageFileDirectory((ByteOrderedDataInputStream)object, 0);
        this.updateImageSizeValues((ByteOrderedDataInputStream)object, 0);
        this.updateImageSizeValues((ByteOrderedDataInputStream)object, 5);
        this.updateImageSizeValues((ByteOrderedDataInputStream)object, 4);
        this.validateImages();
        if (this.mMimeType == 8 && (object = this.mAttributes[1].get(TAG_MAKER_NOTE)) != null) {
            object = new ByteOrderedDataInputStream(((ExifAttribute)object).bytes);
            ((ByteOrderedDataInputStream)object).setByteOrder(this.mExifByteOrder);
            ((ByteOrderedDataInputStream)object).seek(6L);
            this.readImageFileDirectory((ByteOrderedDataInputStream)object, 9);
            object = this.mAttributes[9].get(TAG_COLOR_SPACE);
            if (object != null) {
                this.mAttributes[1].put(TAG_COLOR_SPACE, (ExifAttribute)object);
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream object) throws IOException {
        this.getRawAttributes((ByteOrderedDataInputStream)object);
        if (this.mAttributes[0].get(TAG_RW2_JPG_FROM_RAW) != null) {
            this.getJpegAttributes((ByteOrderedDataInputStream)object, this.mRw2JpgFromRawOffset, 5);
        }
        object = this.mAttributes[0].get(TAG_RW2_ISO);
        ExifAttribute exifAttribute = this.mAttributes[1].get(TAG_PHOTOGRAPHIC_SENSITIVITY);
        if (object != null && exifAttribute == null) {
            this.mAttributes[1].put(TAG_PHOTOGRAPHIC_SENSITIVITY, (ExifAttribute)object);
        }
    }

    private void getStandaloneAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        byte[] byArray = IDENTIFIER_EXIF_APP1;
        byteOrderedDataInputStream.skipBytes(byArray.length);
        byte[] byArray2 = new byte[byteOrderedDataInputStream.available()];
        byteOrderedDataInputStream.readFully(byArray2);
        this.mExifOffset = byArray.length;
        this.readExifSegment(byArray2, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void getWebpAttributes(ByteOrderedDataInputStream object) throws IOException {
        Object object2;
        if (DEBUG) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("getWebpAttributes starting with: ");
            ((StringBuilder)object2).append(object);
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        ((ByteOrderedDataInputStream)object).setByteOrder(ByteOrder.LITTLE_ENDIAN);
        ((ByteOrderedDataInputStream)object).skipBytes(WEBP_SIGNATURE_1.length);
        int n = ((ByteOrderedDataInputStream)object).readInt() + 8;
        int n2 = 8 + ((ByteOrderedDataInputStream)object).skipBytes(WEBP_SIGNATURE_2.length);
        try {
            while (((InputStream)object).read((byte[])(object2 = (Object)new byte[4])) == ((Object)object2).length) {
                int n3 = ((ByteOrderedDataInputStream)object).readInt();
                int n4 = n2 + 4 + 4;
                if (Arrays.equals(WEBP_CHUNK_TYPE_EXIF, (byte[])object2)) {
                    byte[] byArray = new byte[n3];
                    if (((InputStream)object).read(byArray) == n3) {
                        this.mExifOffset = n4;
                        this.readExifSegment(byArray, 0);
                        this.mExifOffset = n4;
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to read given length for given PNG chunk type: ");
                    stringBuilder.append(ExifInterface.byteArrayToHexString((byte[])object2));
                    object = new IOException(stringBuilder.toString());
                    throw object;
                }
                n2 = n3 % 2 == 1 ? n3 + 1 : n3;
                if (n4 + n2 == n) {
                    return;
                }
                if (n4 + n2 <= n) {
                    n3 = ((ByteOrderedDataInputStream)object).skipBytes(n2);
                    if (n3 != n2) {
                        object = new IOException("Encountered WebP file with invalid chunk size");
                        throw object;
                    }
                    n2 = n4 + n3;
                    continue;
                }
                object = new IOException("Encountered WebP file with invalid chunk size");
                throw object;
            }
            object = new IOException("Encountered invalid length while parsing WebP chunktype");
            throw object;
        }
        catch (EOFException eOFException) {
            IOException iOException = new IOException("Encountered corrupt WebP file.");
            throw iOException;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Pair<Integer, Integer> guessDataFormat(String pair) {
        Integer n;
        Integer n2;
        block19: {
            boolean bl = pair.contains(",");
            n2 = 2;
            n = -1;
            if (!bl) break block19;
            String[] stringArray = pair.split(",", -1);
            pair = ExifInterface.guessDataFormat(stringArray[0]);
            if ((Integer)pair.first == 2) {
                return pair;
            }
            int n3 = 1;
            while (n3 < stringArray.length) {
                int n4;
                int n5;
                block20: {
                    block21: {
                        Pair<Integer, Integer> pair2 = ExifInterface.guessDataFormat(stringArray[n3]);
                        n5 = -1;
                        int n6 = -1;
                        if (((Integer)pair2.first).equals(pair.first) || ((Integer)pair2.second).equals(pair.first)) {
                            n5 = (Integer)pair.first;
                        }
                        n4 = n6;
                        if ((Integer)pair.second == -1) break block20;
                        if (((Integer)pair2.first).equals(pair.second)) break block21;
                        n4 = n6;
                        if (!((Integer)pair2.second).equals(pair.second)) break block20;
                    }
                    n4 = (Integer)pair.second;
                }
                if (n5 == -1 && n4 == -1) {
                    return new Pair((Object)n2, (Object)n);
                }
                if (n5 == -1) {
                    pair = new Pair((Object)n4, (Object)n);
                } else if (n4 == -1) {
                    pair = new Pair((Object)n5, (Object)n);
                }
                ++n3;
            }
            return pair;
        }
        if (pair.contains("/")) {
            if (((Pair<Integer, Integer>)(pair = pair.split("/", -1))).length != 2) return new Pair((Object)n2, (Object)n);
            long l = (long)Double.parseDouble(pair[0]);
            long l2 = (long)Double.parseDouble((String)pair[1]);
            if (l < 0L) return new Pair((Object)10, (Object)n);
            if (l2 < 0L) return new Pair((Object)10, (Object)n);
            if (l > Integer.MAX_VALUE) return new Pair((Object)5, (Object)n);
            if (l2 > Integer.MAX_VALUE) return new Pair((Object)5, (Object)n);
            try {
                return new Pair((Object)10, (Object)5);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            return new Pair((Object)n2, (Object)n);
        }
        try {
            Long l = Long.parseLong((String)pair);
            if (l >= 0L && l <= 65535L) {
                return new Pair((Object)3, (Object)4);
            }
            if (l >= 0L) return new Pair((Object)4, (Object)n);
            return new Pair((Object)9, (Object)n);
        }
        catch (NumberFormatException numberFormatException) {
            try {
                Double.parseDouble(pair);
                return new Pair((Object)12, (Object)n);
            }
            catch (NumberFormatException numberFormatException2) {
                return new Pair((Object)n2, (Object)n);
            }
        }
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream object, HashMap object2) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute)((HashMap)object2).get(TAG_JPEG_INTERCHANGE_FORMAT);
        object2 = (ExifAttribute)((HashMap)object2).get(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (exifAttribute != null && object2 != null) {
            int n = exifAttribute.getIntValue(this.mExifByteOrder);
            int n2 = ((ExifAttribute)object2).getIntValue(this.mExifByteOrder);
            int n3 = n;
            if (this.mMimeType == 7) {
                n3 = n + this.mOrfMakerNoteOffset;
            }
            n2 = Math.min(n2, ((ByteOrderedDataInputStream)object).getLength() - n3);
            if (n3 > 0 && n2 > 0) {
                this.mHasThumbnail = true;
                this.mThumbnailOffset = n = this.mExifOffset + n3;
                this.mThumbnailLength = n2;
                if (this.mFilename == null && this.mAssetInputStream == null && this.mSeekableFileDescriptor == null) {
                    object2 = new byte[n2];
                    ((ByteOrderedDataInputStream)object).seek(n);
                    ((ByteOrderedDataInputStream)object).readFully((byte[])object2);
                    this.mThumbnailBytes = (byte[])object2;
                }
            }
            if (DEBUG) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Setting thumbnail attributes with offset: ");
                ((StringBuilder)object).append(n3);
                ((StringBuilder)object).append(", length: ");
                ((StringBuilder)object).append(n2);
                Log.d((String)TAG, (String)((StringBuilder)object).toString());
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void handleThumbnailFromStrips(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap object) throws IOException {
        Object object2 = (ExifAttribute)((HashMap)object).get(TAG_STRIP_OFFSETS);
        object = (ExifAttribute)((HashMap)object).get(TAG_STRIP_BYTE_COUNTS);
        if (object2 == null) return;
        if (object == null) return;
        long[] lArray = ExifInterface.convertToLongArray(((ExifAttribute)object2).getValue(this.mExifByteOrder));
        long[] lArray2 = ExifInterface.convertToLongArray(((ExifAttribute)object).getValue(this.mExifByteOrder));
        if (lArray != null && lArray.length != 0) {
            if (lArray2 != null && lArray2.length != 0) {
                int n;
                if (lArray.length != lArray2.length) {
                    Log.w((String)TAG, (String)"stripOffsets and stripByteCounts should have same length.");
                    return;
                }
                long l = 0L;
                int n2 = lArray2.length;
                for (n = 0; n < n2; l += lArray2[n], ++n) {
                }
                byte[] byArray = new byte[(int)l];
                int n3 = 0;
                n2 = 0;
                this.mAreThumbnailStripsConsecutive = true;
                this.mHasThumbnailStrips = true;
                this.mHasThumbnail = true;
                n = 0;
                object = object2;
                while (true) {
                    if (n >= lArray.length) {
                        this.mThumbnailBytes = byArray;
                        if (!this.mAreThumbnailStripsConsecutive) return;
                        this.mThumbnailOffset = (int)lArray[0] + this.mExifOffset;
                        this.mThumbnailLength = byArray.length;
                        return;
                    }
                    int n4 = (int)lArray[n];
                    int n5 = (int)lArray2[n];
                    if (n < lArray.length - 1 && (long)(n4 + n5) != lArray[n + 1]) {
                        this.mAreThumbnailStripsConsecutive = false;
                    }
                    if ((n4 -= n3) < 0) {
                        Log.d((String)TAG, (String)"Invalid strip offset value");
                    }
                    byteOrderedDataInputStream.seek(n4);
                    object2 = new byte[n5];
                    byteOrderedDataInputStream.read((byte[])object2);
                    n3 = n3 + n4 + n5;
                    System.arraycopy(object2, 0, byArray, n2, ((Object)object2).length);
                    n2 += ((Object)object2).length;
                    ++n;
                }
            }
            Log.w((String)TAG, (String)"stripByteCounts should not be null or have zero length.");
            return;
        }
        Log.w((String)TAG, (String)"stripOffsets should not be null or have zero length.");
    }

    private void initForFilename(String object) throws IOException {
        if (object != null) {
            Object object2;
            block8: {
                Object object3 = null;
                this.mAssetInputStream = null;
                this.mFilename = object;
                object2 = object3;
                object2 = object3;
                try {
                    FileInputStream fileInputStream = new FileInputStream((String)object);
                    object2 = object = fileInputStream;
                }
                catch (Throwable throwable) {
                    ExifInterface.closeQuietly(object2);
                    throw throwable;
                }
                if (!ExifInterface.isSeekableFD(((FileInputStream)object).getFD())) break block8;
                object2 = object;
                this.mSeekableFileDescriptor = ((FileInputStream)object).getFD();
            }
            object2 = object;
            this.mSeekableFileDescriptor = null;
            object2 = object;
            this.loadAttributes((InputStream)object);
            ExifInterface.closeQuietly((Closeable)object);
            return;
        }
        throw new NullPointerException("filename cannot be null");
    }

    private static boolean isExifDataOnly(BufferedInputStream object) throws IOException {
        byte[] byArray = IDENTIFIER_EXIF_APP1;
        ((BufferedInputStream)object).mark(byArray.length);
        byArray = new byte[byArray.length];
        ((FilterInputStream)object).read(byArray);
        ((BufferedInputStream)object).reset();
        for (int i = 0; i < ((Object)(object = (Object)IDENTIFIER_EXIF_APP1)).length; ++i) {
            if (byArray[i] == object[i]) continue;
            return false;
        }
        return true;
    }

    /*
     * Exception decompiling
     */
    private boolean isHeifFormat(byte[] var1_1) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 15[TRYBLOCK] [30 : 427->433)] java.lang.Throwable
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

    private static boolean isJpegFormat(byte[] byArray) throws IOException {
        byte[] byArray2;
        for (int i = 0; i < (byArray2 = JPEG_SIGNATURE).length; ++i) {
            if (byArray[i] == byArray2[i]) continue;
            return false;
        }
        return true;
    }

    private boolean isOrfFormat(byte[] object) throws IOException {
        boolean bl;
        block10: {
            InputStream inputStream = null;
            InputStream inputStream2 = null;
            bl = false;
            Object object2 = inputStream2;
            Object object3 = inputStream;
            object2 = inputStream2;
            object3 = inputStream;
            Object object4 = new ByteOrderedDataInputStream((byte[])object);
            object = object4;
            object2 = object;
            object3 = object;
            object4 = this.readByteOrder((ByteOrderedDataInputStream)object);
            object2 = object;
            object3 = object;
            this.mExifByteOrder = object4;
            object2 = object;
            object3 = object;
            ((ByteOrderedDataInputStream)object).setByteOrder((ByteOrder)object4);
            object2 = object;
            object3 = object;
            try {
                short s = ((ByteOrderedDataInputStream)object).readShort();
                if (s != 20306 && s != 21330) break block10;
                bl = true;
            }
            catch (Throwable throwable) {
                if (object2 != null) {
                    ((InputStream)object2).close();
                }
                throw throwable;
            }
            catch (Exception exception) {
                if (object3 != null) {
                    ((InputStream)object3).close();
                }
                return false;
            }
        }
        ((InputStream)object).close();
        return bl;
    }

    private boolean isPngFormat(byte[] byArray) throws IOException {
        byte[] byArray2;
        for (int i = 0; i < (byArray2 = PNG_SIGNATURE).length; ++i) {
            if (byArray[i] == byArray2[i]) continue;
            return false;
        }
        return true;
    }

    private boolean isRafFormat(byte[] byArray) throws IOException {
        byte[] byArray2 = RAF_SIGNATURE.getBytes(Charset.defaultCharset());
        for (int i = 0; i < byArray2.length; ++i) {
            if (byArray[i] == byArray2[i]) continue;
            return false;
        }
        return true;
    }

    private boolean isRw2Format(byte[] object) throws IOException {
        boolean bl;
        block10: {
            InputStream inputStream = null;
            InputStream inputStream2 = null;
            bl = false;
            Object object2 = inputStream2;
            Object object3 = inputStream;
            object2 = inputStream2;
            object3 = inputStream;
            Object object4 = new ByteOrderedDataInputStream((byte[])object);
            object = object4;
            object2 = object;
            object3 = object;
            object4 = this.readByteOrder((ByteOrderedDataInputStream)object);
            object2 = object;
            object3 = object;
            this.mExifByteOrder = object4;
            object2 = object;
            object3 = object;
            ((ByteOrderedDataInputStream)object).setByteOrder((ByteOrder)object4);
            object2 = object;
            object3 = object;
            try {
                short s = ((ByteOrderedDataInputStream)object).readShort();
                if (s != 85) break block10;
                bl = true;
            }
            catch (Throwable throwable) {
                if (object2 != null) {
                    ((InputStream)object2).close();
                }
                throw throwable;
            }
            catch (Exception exception) {
                if (object3 != null) {
                    ((InputStream)object3).close();
                }
                return false;
            }
        }
        ((InputStream)object).close();
        return bl;
    }

    private static boolean isSeekableFD(FileDescriptor fileDescriptor) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Os.lseek((FileDescriptor)fileDescriptor, (long)0L, (int)OsConstants.SEEK_CUR);
                return true;
            }
            catch (Exception exception) {
                if (DEBUG) {
                    Log.d((String)TAG, (String)"The file descriptor for the given input is not seekable");
                }
                return false;
            }
        }
        return false;
    }

    private boolean isSupportedDataType(HashMap object) throws IOException {
        Object object2 = (ExifAttribute)((HashMap)object).get(TAG_BITS_PER_SAMPLE);
        if (object2 != null) {
            int n;
            int[] nArray = BITS_PER_SAMPLE_RGB;
            if (Arrays.equals(nArray, (int[])(object2 = (Object)((int[])((ExifAttribute)object2).getValue(this.mExifByteOrder))))) {
                return true;
            }
            if (this.mMimeType == 3 && (object = (ExifAttribute)((HashMap)object).get(TAG_PHOTOMETRIC_INTERPRETATION)) != null && ((n = ((ExifAttribute)object).getIntValue(this.mExifByteOrder)) == 1 && Arrays.equals((int[])object2, BITS_PER_SAMPLE_GREYSCALE_2) || n == 6 && Arrays.equals((int[])object2, nArray))) {
                return true;
            }
        }
        if (DEBUG) {
            Log.d((String)TAG, (String)"Unsupported data type value");
        }
        return false;
    }

    private boolean isSupportedFormatForSavingAttributes() {
        int n;
        return this.mIsSupportedFile && ((n = this.mMimeType) == 4 || n == 13 || n == 14);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isSupportedMimeType(String object) {
        if (object == null) {
            object = new NullPointerException("mimeType shouldn't be null");
            throw object;
        }
        object = ((String)object).toLowerCase(Locale.ROOT);
        int n = -1;
        switch (((String)object).hashCode()) {
            case 2111234748: {
                if (!((String)object).equals("image/x-canon-cr2")) break;
                n = 2;
                break;
            }
            case 2099152524: {
                if (!((String)object).equals("image/x-nikon-nrw")) break;
                n = 4;
                break;
            }
            case 2099152104: {
                if (!((String)object).equals("image/x-nikon-nef")) break;
                n = 3;
                break;
            }
            case 1378106698: {
                if (!((String)object).equals("image/x-olympus-orf")) break;
                n = 7;
                break;
            }
            case -332763809: {
                if (!((String)object).equals("image/x-pentax-pef")) break;
                n = 8;
                break;
            }
            case -879258763: {
                if (!((String)object).equals("image/png")) break;
                n = 13;
                break;
            }
            case -985160897: {
                if (!((String)object).equals("image/x-panasonic-rw2")) break;
                n = 6;
                break;
            }
            case -1423313290: {
                if (!((String)object).equals("image/x-adobe-dng")) break;
                n = 1;
                break;
            }
            case -1487018032: {
                if (!((String)object).equals("image/webp")) break;
                n = 14;
                break;
            }
            case -1487394660: {
                if (!((String)object).equals("image/jpeg")) break;
                n = 0;
                break;
            }
            case -1487464690: {
                if (!((String)object).equals("image/heif")) break;
                n = 12;
                break;
            }
            case -1487464693: {
                if (!((String)object).equals("image/heic")) break;
                n = 11;
                break;
            }
            case -1594371159: {
                if (!((String)object).equals("image/x-sony-arw")) break;
                n = 5;
                break;
            }
            case -1635437028: {
                if (!((String)object).equals("image/x-samsung-srw")) break;
                n = 9;
                break;
            }
            case -1875291391: {
                if (!((String)object).equals("image/x-fuji-raf")) break;
                n = 10;
            }
        }
        switch (n) {
            default: {
                return false;
            }
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
        }
        return true;
    }

    private boolean isThumbnail(HashMap object) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute)((HashMap)object).get(TAG_IMAGE_LENGTH);
        object = (ExifAttribute)((HashMap)object).get(TAG_IMAGE_WIDTH);
        if (exifAttribute != null && object != null) {
            int n = exifAttribute.getIntValue(this.mExifByteOrder);
            int n2 = ((ExifAttribute)object).getIntValue(this.mExifByteOrder);
            if (n <= 512 && n2 <= 512) {
                return true;
            }
        }
        return false;
    }

    private boolean isWebpFormat(byte[] byArray) throws IOException {
        byte[] byArray2;
        int n;
        for (n = 0; n < (byArray2 = WEBP_SIGNATURE_1).length; ++n) {
            if (byArray[n] == byArray2[n]) continue;
            return false;
        }
        for (n = 0; n < (byArray2 = WEBP_SIGNATURE_2).length; ++n) {
            if (byArray[WEBP_SIGNATURE_1.length + n + 4] == byArray2[n]) continue;
            return false;
        }
        return true;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void loadAttributes(InputStream var1_1) {
        block21: {
            if (var1_1 == null) {
                var1_1 = new NullPointerException("inputstream shouldn't be null");
                throw var1_1;
            }
            for (var2_4 = 0; var2_4 < ExifInterface.EXIF_TAGS.length; ++var2_4) {
                this.mAttributes[var2_4] = new HashMap<K, V>();
            }
            var4_5 = var1_1;
            {
                catch (Throwable var1_2) {
                    break block21;
                }
                catch (IOException var1_3) {}
                {
                    this.mIsSupportedFile = false;
                    var3_6 = ExifInterface.DEBUG;
                    if (var3_6) {
                        Log.w((String)"ExifInterface", (String)"Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface.", (Throwable)var1_3);
                    }
                    this.addDefaultValuesForCompatibility();
                    if (var3_6 == false) return;
                    ** GOTO lbl59
                }
            }
            {
                if (!this.mIsExifDataOnly) {
                    var4_5 = new BufferedInputStream((InputStream)var1_1, 5000);
                    this.mMimeType = this.getMimeType((BufferedInputStream)var4_5);
                }
                var1_1 = new ByteOrderedDataInputStream((InputStream)var4_5);
                if (!this.mIsExifDataOnly) {
                    switch (this.mMimeType) {
                        default: {
                            break;
                        }
                        case 14: {
                            this.getWebpAttributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                        case 13: {
                            this.getPngAttributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                        case 12: {
                            this.getHeifAttributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                        case 10: {
                            this.getRw2Attributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                        case 9: {
                            this.getRafAttributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                        case 7: {
                            this.getOrfAttributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                        case 4: {
                            this.getJpegAttributes((ByteOrderedDataInputStream)var1_1, 0, 0);
                            break;
                        }
                        case 0: 
                        case 1: 
                        case 2: 
                        case 3: 
                        case 5: 
                        case 6: 
                        case 8: 
                        case 11: {
                            this.getRawAttributes((ByteOrderedDataInputStream)var1_1);
                            break;
                        }
                    }
                } else {
                    this.getStandaloneAttributes((ByteOrderedDataInputStream)var1_1);
                }
                this.setThumbnailData((ByteOrderedDataInputStream)var1_1);
                this.mIsSupportedFile = true;
                this.addDefaultValuesForCompatibility();
                if (ExifInterface.DEBUG == false) return;
lbl59:
                // 2 sources

                this.printAttributes();
                return;
            }
        }
        this.addDefaultValuesForCompatibility();
        if (ExifInterface.DEBUG == false) throw var1_2;
        this.printAttributes();
        throw var1_2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static long parseDateTime(String object, String string2) {
        long l;
        block7: {
            if (object == null) return -1L;
            if (!sNonZeroTimePattern.matcher((CharSequence)object).matches()) return -1L;
            ParsePosition parsePosition = new ParsePosition(0);
            object = sFormatter.parse((String)object, parsePosition);
            if (object != null) break block7;
            return -1L;
        }
        long l2 = l = ((Date)object).getTime();
        if (string2 == null) return l2;
        try {
            l2 = Long.parseLong(string2);
        }
        catch (NumberFormatException numberFormatException) {
            return l;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return -1L;
        }
        while (l2 > 1000L) {
            l2 /= 10L;
        }
        return l + l2;
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream object, int n) throws IOException {
        ByteOrder byteOrder;
        this.mExifByteOrder = byteOrder = this.readByteOrder((ByteOrderedDataInputStream)object);
        ((ByteOrderedDataInputStream)object).setByteOrder(byteOrder);
        int n2 = ((ByteOrderedDataInputStream)object).readUnsignedShort();
        int n3 = this.mMimeType;
        if (n3 != 7 && n3 != 10 && n2 != 42) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Invalid start code: ");
            ((StringBuilder)object).append(Integer.toHexString(n2));
            throw new IOException(((StringBuilder)object).toString());
        }
        n3 = ((ByteOrderedDataInputStream)object).readInt();
        if (n3 >= 8 && n3 < n) {
            n = n3 - 8;
            if (n > 0 && ((ByteOrderedDataInputStream)object).skipBytes(n) != n) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Couldn't jump to first Ifd: ");
                ((StringBuilder)object).append(n);
                throw new IOException(((StringBuilder)object).toString());
            }
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Invalid first Ifd offset: ");
        ((StringBuilder)object).append(n3);
        throw new IOException(((StringBuilder)object).toString());
    }

    private void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The size of tag group[");
            stringBuilder.append(i);
            stringBuilder.append("]: ");
            stringBuilder.append(this.mAttributes[i].size());
            Log.d((String)TAG, (String)stringBuilder.toString());
            for (Map.Entry<String, ExifAttribute> entry : this.mAttributes[i].entrySet()) {
                ExifAttribute exifAttribute = entry.getValue();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("tagName: ");
                stringBuilder2.append(entry.getKey());
                stringBuilder2.append(", tagType: ");
                stringBuilder2.append(exifAttribute.toString());
                stringBuilder2.append(", tagValue: '");
                stringBuilder2.append(exifAttribute.getStringValue(this.mExifByteOrder));
                stringBuilder2.append("'");
                Log.d((String)TAG, (String)stringBuilder2.toString());
            }
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream object) throws IOException {
        short s = ((ByteOrderedDataInputStream)object).readShort();
        switch (s) {
            default: {
                object = new StringBuilder();
                ((StringBuilder)object).append("Invalid byte order: ");
                ((StringBuilder)object).append(Integer.toHexString(s));
                throw new IOException(((StringBuilder)object).toString());
            }
            case 19789: {
                if (DEBUG) {
                    Log.d((String)TAG, (String)"readExifSegment: Byte Align MM");
                }
                return ByteOrder.BIG_ENDIAN;
            }
            case 18761: 
        }
        if (DEBUG) {
            Log.d((String)TAG, (String)"readExifSegment: Byte Align II");
        }
        return ByteOrder.LITTLE_ENDIAN;
    }

    private void readExifSegment(byte[] byArray, int n) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(byArray);
        this.parseTiffHeaders(byteOrderedDataInputStream, byArray.length);
        this.readImageFileDirectory(byteOrderedDataInputStream, n);
    }

    private void readImageFileDirectory(ByteOrderedDataInputStream object, int n) throws IOException {
        block51: {
            boolean bl;
            Object object2;
            this.mAttributesOffsets.add(((ByteOrderedDataInputStream)object).mPosition);
            if (((ByteOrderedDataInputStream)object).mPosition + 2 > ((ByteOrderedDataInputStream)object).mLength) {
                return;
            }
            short s = ((ByteOrderedDataInputStream)object).readShort();
            if (DEBUG) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("numberOfDirectoryEntry: ");
                ((StringBuilder)object2).append(s);
                Log.d((String)TAG, (String)((StringBuilder)object2).toString());
            }
            if (((ByteOrderedDataInputStream)object).mPosition + s * 12 > ((ByteOrderedDataInputStream)object).mLength || s <= 0) break block51;
            for (short s2 = 0; s2 < s; s2 = (short)(s2 + 1)) {
                Object object3;
                long l;
                int n2;
                int n3;
                Object object4;
                long l2;
                int n4;
                int n5;
                int n6;
                block56: {
                    block53: {
                        block54: {
                            block55: {
                                block52: {
                                    n6 = ((ByteOrderedDataInputStream)object).readUnsignedShort();
                                    n5 = ((ByteOrderedDataInputStream)object).readUnsignedShort();
                                    n4 = ((ByteOrderedDataInputStream)object).readInt();
                                    l2 = (long)((ByteOrderedDataInputStream)object).peek() + 4L;
                                    object4 = sExifTagMapsForReading[n].get(n6);
                                    bl = DEBUG;
                                    if (bl) {
                                        object2 = object4 != null ? ((ExifTag)object4).name : null;
                                        Log.d((String)TAG, (String)String.format("ifdType: %d, tagNumber: %d, tagName: %s, dataFormat: %d, numberOfComponents: %d", n, n6, object2, n5, n4));
                                    }
                                    n3 = 0;
                                    if (object4 != null) break block52;
                                    if (bl) {
                                        object2 = new StringBuilder();
                                        ((StringBuilder)object2).append("Skip the tag entry since tag number is not defined: ");
                                        ((StringBuilder)object2).append(n6);
                                        Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                                    }
                                    break block53;
                                }
                                if (n5 <= 0 || n5 >= ((Object)(object2 = (Object)IFD_FORMAT_BYTES_PER_FORMAT)).length) break block54;
                                if (((ExifTag)object4).isFormatCompatible(n5)) break block55;
                                if (bl) {
                                    object2 = new StringBuilder();
                                    ((StringBuilder)object2).append("Skip the tag entry since data format (");
                                    ((StringBuilder)object2).append(IFD_FORMAT_NAMES[n5]);
                                    ((StringBuilder)object2).append(") is unexpected for tag: ");
                                    ((StringBuilder)object2).append(((ExifTag)object4).name);
                                    Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                                }
                                break block53;
                            }
                            n2 = n5;
                            if (n5 == 7) {
                                n2 = ((ExifTag)object4).primaryFormat;
                            }
                            if ((l = (long)n4 * (long)object2[n2]) >= 0L && l <= Integer.MAX_VALUE) {
                                n3 = 1;
                            } else if (bl) {
                                object2 = new StringBuilder();
                                ((StringBuilder)object2).append("Skip the tag entry since the number of components is invalid: ");
                                ((StringBuilder)object2).append(n4);
                                Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                            }
                            break block56;
                        }
                        if (bl) {
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("Skip the tag entry since data format is invalid: ");
                            ((StringBuilder)object2).append(n5);
                            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                        }
                    }
                    l = 0L;
                    n2 = n5;
                }
                if (n3 == 0) {
                    ((ByteOrderedDataInputStream)object).seek(l2);
                    continue;
                }
                if (l > 4L) {
                    n5 = ((ByteOrderedDataInputStream)object).readInt();
                    if (bl) {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("seek to data offset: ");
                        ((StringBuilder)object2).append(n5);
                        Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                    }
                    if ((n3 = this.mMimeType) == 7) {
                        if (TAG_MAKER_NOTE.equals(((ExifTag)object4).name)) {
                            this.mOrfMakerNoteOffset = n5;
                        } else if (n == 6 && TAG_ORF_THUMBNAIL_IMAGE.equals(((ExifTag)object4).name)) {
                            this.mOrfThumbnailOffset = n5;
                            this.mOrfThumbnailLength = n4;
                            object3 = ExifAttribute.createUShort(6, this.mExifByteOrder);
                            object2 = ExifAttribute.createULong(this.mOrfThumbnailOffset, this.mExifByteOrder);
                            ExifAttribute exifAttribute = ExifAttribute.createULong(this.mOrfThumbnailLength, this.mExifByteOrder);
                            this.mAttributes[4].put(TAG_COMPRESSION, (ExifAttribute)object3);
                            this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT, (ExifAttribute)object2);
                            this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, exifAttribute);
                        }
                    } else if (n3 == 10 && TAG_RW2_JPG_FROM_RAW.equals(((ExifTag)object4).name)) {
                        this.mRw2JpgFromRawOffset = n5;
                    }
                    if ((long)n5 + l <= (long)((ByteOrderedDataInputStream)object).mLength) {
                        ((ByteOrderedDataInputStream)object).seek(n5);
                    } else {
                        if (bl) {
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("Skip the tag entry since data offset is invalid: ");
                            ((StringBuilder)object2).append(n5);
                            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                        }
                        ((ByteOrderedDataInputStream)object).seek(l2);
                        continue;
                    }
                }
                object2 = sExifPointerTagMap.get(n6);
                if (bl) {
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("nextIfdType: ");
                    ((StringBuilder)object3).append(object2);
                    ((StringBuilder)object3).append(" byteCount: ");
                    ((StringBuilder)object3).append(l);
                    Log.d((String)TAG, (String)((StringBuilder)object3).toString());
                }
                if (object2 != null) {
                    l = -1L;
                    switch (n2) {
                        default: {
                            break;
                        }
                        case 9: 
                        case 13: {
                            l = ((ByteOrderedDataInputStream)object).readInt();
                            break;
                        }
                        case 8: {
                            l = ((ByteOrderedDataInputStream)object).readShort();
                            break;
                        }
                        case 4: {
                            l = ((ByteOrderedDataInputStream)object).readUnsignedInt();
                            break;
                        }
                        case 3: {
                            l = ((ByteOrderedDataInputStream)object).readUnsignedShort();
                        }
                    }
                    if (bl) {
                        Log.d((String)TAG, (String)String.format("Offset: %d, tagName: %s", l, ((ExifTag)object4).name));
                    }
                    if (l > 0L && l < (long)((ByteOrderedDataInputStream)object).mLength) {
                        if (!this.mAttributesOffsets.contains((int)l)) {
                            ((ByteOrderedDataInputStream)object).seek(l);
                            this.readImageFileDirectory((ByteOrderedDataInputStream)object, (Integer)object2);
                        } else if (bl) {
                            object4 = new StringBuilder();
                            ((StringBuilder)object4).append("Skip jump into the IFD since it has already been read: IfdType ");
                            ((StringBuilder)object4).append(object2);
                            ((StringBuilder)object4).append(" (at ");
                            ((StringBuilder)object4).append(l);
                            ((StringBuilder)object4).append(")");
                            Log.d((String)TAG, (String)((StringBuilder)object4).toString());
                        }
                    } else if (bl) {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Skip jump into the IFD since its offset is invalid: ");
                        ((StringBuilder)object2).append(l);
                        Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                    }
                    ((ByteOrderedDataInputStream)object).seek(l2);
                    continue;
                }
                n3 = ((ByteOrderedDataInputStream)object).peek();
                n5 = this.mExifOffset;
                object2 = new byte[(int)l];
                ((ByteOrderedDataInputStream)object).readFully((byte[])object2);
                object2 = new ExifAttribute(n2, n4, n3 + n5, (byte[])object2);
                this.mAttributes[n].put(((ExifTag)object4).name, (ExifAttribute)object2);
                if (TAG_DNG_VERSION.equals(((ExifTag)object4).name)) {
                    this.mMimeType = 3;
                }
                if ((TAG_MAKE.equals(((ExifTag)object4).name) || TAG_MODEL.equals(((ExifTag)object4).name)) && ((ExifAttribute)object2).getStringValue(this.mExifByteOrder).contains(PEF_SIGNATURE) || TAG_COMPRESSION.equals(((ExifTag)object4).name) && ((ExifAttribute)object2).getIntValue(this.mExifByteOrder) == 65535) {
                    this.mMimeType = 8;
                }
                if ((long)((ByteOrderedDataInputStream)object).peek() == l2) continue;
                ((ByteOrderedDataInputStream)object).seek(l2);
            }
            if (((ByteOrderedDataInputStream)object).peek() + 4 <= ((ByteOrderedDataInputStream)object).mLength) {
                n = ((ByteOrderedDataInputStream)object).readInt();
                bl = DEBUG;
                if (bl) {
                    Log.d((String)TAG, (String)String.format("nextIfdOffset: %d", n));
                }
                if ((long)n > 0L && n < ((ByteOrderedDataInputStream)object).mLength) {
                    if (!this.mAttributesOffsets.contains(n)) {
                        ((ByteOrderedDataInputStream)object).seek(n);
                        if (this.mAttributes[4].isEmpty()) {
                            this.readImageFileDirectory((ByteOrderedDataInputStream)object, 4);
                        } else if (this.mAttributes[5].isEmpty()) {
                            this.readImageFileDirectory((ByteOrderedDataInputStream)object, 5);
                        }
                    } else if (bl) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Stop reading file since re-reading an IFD may cause an infinite loop: ");
                        ((StringBuilder)object).append(n);
                        Log.d((String)TAG, (String)((StringBuilder)object).toString());
                    }
                } else if (bl) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Stop reading file since a wrong offset may cause an infinite loop: ");
                    ((StringBuilder)object).append(n);
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                }
            }
            return;
        }
    }

    private void removeAttribute(String string2) {
        for (int i = 0; i < EXIF_TAGS.length; ++i) {
            this.mAttributes[i].remove(string2);
        }
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream byteOrderedDataInputStream, int n) throws IOException {
        ExifAttribute exifAttribute = this.mAttributes[n].get(TAG_IMAGE_LENGTH);
        ExifAttribute exifAttribute2 = this.mAttributes[n].get(TAG_IMAGE_WIDTH);
        if ((exifAttribute == null || exifAttribute2 == null) && (exifAttribute2 = this.mAttributes[n].get(TAG_JPEG_INTERCHANGE_FORMAT)) != null) {
            this.getJpegAttributes(byteOrderedDataInputStream, exifAttribute2.getIntValue(this.mExifByteOrder), n);
        }
    }

    private void saveJpegAttributes(InputStream object, OutputStream object2) throws IOException {
        block18: {
            block19: {
                Object object3;
                if (DEBUG) {
                    object3 = new StringBuilder();
                    ((StringBuilder)object3).append("saveJpegAttributes starting with (inputStream: ");
                    ((StringBuilder)object3).append(object);
                    ((StringBuilder)object3).append(", outputStream: ");
                    ((StringBuilder)object3).append(object2);
                    ((StringBuilder)object3).append(")");
                    Log.d((String)TAG, (String)((StringBuilder)object3).toString());
                }
                object3 = new DataInputStream((InputStream)object);
                ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream((OutputStream)object2, ByteOrder.BIG_ENDIAN);
                if (((DataInputStream)object3).readByte() != -1) break block18;
                byteOrderedDataOutputStream.writeByte(-1);
                if (((DataInputStream)object3).readByte() != -40) break block19;
                byteOrderedDataOutputStream.writeByte(-40);
                object = object2 = null;
                if (this.getAttribute(TAG_XMP) != null) {
                    object = object2;
                    if (this.mXmpIsFromSeparateMarker) {
                        object = this.mAttributes[0].remove(TAG_XMP);
                    }
                }
                byteOrderedDataOutputStream.writeByte(-1);
                byteOrderedDataOutputStream.writeByte(-31);
                this.writeExifSegment(byteOrderedDataOutputStream);
                if (object != null) {
                    this.mAttributes[0].put(TAG_XMP, (ExifAttribute)object);
                }
                object = new byte[4096];
                block4: while (((DataInputStream)object3).readByte() == -1) {
                    block20: {
                        int n;
                        int n2 = ((DataInputStream)object3).readByte();
                        switch (n2) {
                            default: {
                                byteOrderedDataOutputStream.writeByte(-1);
                                byteOrderedDataOutputStream.writeByte(n2);
                                n2 = ((DataInputStream)object3).readUnsignedShort();
                                byteOrderedDataOutputStream.writeUnsignedShort(n2);
                                if ((n2 -= 2) >= 0) {
                                    break;
                                }
                                break block20;
                            }
                            case -31: {
                                n = ((DataInputStream)object3).readUnsignedShort() - 2;
                                if (n >= 0) {
                                    object2 = new byte[6];
                                    if (n >= 6) {
                                        if (((DataInputStream)object3).read((byte[])object2) == 6) {
                                            if (Arrays.equals((byte[])object2, IDENTIFIER_EXIF_APP1)) {
                                                if (((DataInputStream)object3).skipBytes(n - 6) == n - 6) continue block4;
                                                throw new IOException("Invalid length");
                                            }
                                        } else {
                                            throw new IOException("Invalid exif");
                                        }
                                    }
                                    byteOrderedDataOutputStream.writeByte(-1);
                                    byteOrderedDataOutputStream.writeByte(n2);
                                    byteOrderedDataOutputStream.writeUnsignedShort(n + 2);
                                    n2 = n;
                                    if (n >= 6) {
                                        n2 = n - 6;
                                        byteOrderedDataOutputStream.write((byte[])object2);
                                    }
                                    while (n2 > 0 && (n = ((DataInputStream)object3).read((byte[])object, 0, Math.min(n2, ((Object)object).length))) >= 0) {
                                        byteOrderedDataOutputStream.write((byte[])object, 0, n);
                                        n2 -= n;
                                    }
                                    continue block4;
                                }
                                throw new IOException("Invalid length");
                            }
                            case -39: 
                            case -38: {
                                byteOrderedDataOutputStream.writeByte(-1);
                                byteOrderedDataOutputStream.writeByte(n2);
                                ExifInterface.copy((InputStream)object3, byteOrderedDataOutputStream);
                                return;
                            }
                        }
                        while (n2 > 0 && (n = ((DataInputStream)object3).read((byte[])object, 0, Math.min(n2, ((Object)object).length))) >= 0) {
                            byteOrderedDataOutputStream.write((byte[])object, 0, n);
                            n2 -= n;
                        }
                        continue;
                    }
                    throw new IOException("Invalid length");
                }
                throw new IOException("Invalid marker");
            }
            throw new IOException("Invalid marker");
        }
        object = new IOException("Invalid marker");
        throw object;
    }

    private void savePngAttributes(InputStream object, OutputStream outputStream) throws IOException {
        Object object2;
        if (DEBUG) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("savePngAttributes starting with (inputStream: ");
            ((StringBuilder)object2).append(object);
            ((StringBuilder)object2).append(", outputStream: ");
            ((StringBuilder)object2).append(outputStream);
            ((StringBuilder)object2).append(")");
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
        DataInputStream dataInputStream = new DataInputStream((InputStream)object);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        object = PNG_SIGNATURE;
        ExifInterface.copy(dataInputStream, byteOrderedDataOutputStream, ((Object)object).length);
        int n = this.mExifOffset;
        if (n == 0) {
            n = dataInputStream.readInt();
            byteOrderedDataOutputStream.writeInt(n);
            ExifInterface.copy(dataInputStream, byteOrderedDataOutputStream, n + 4 + 4);
        } else {
            ExifInterface.copy(dataInputStream, byteOrderedDataOutputStream, n - ((Object)object).length - 4 - 4);
            dataInputStream.skipBytes(dataInputStream.readInt() + 4 + 4);
        }
        object = object2 = null;
        object = object2;
        try {
            outputStream = new ByteArrayOutputStream();
            object = outputStream;
            object = outputStream;
        }
        catch (Throwable throwable) {
            ExifInterface.closeQuietly((Closeable)object);
            throw throwable;
        }
        object2 = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        object = outputStream;
        this.writeExifSegment((ByteOrderedDataOutputStream)object2);
        object = outputStream;
        object2 = ((ByteArrayOutputStream)((ByteOrderedDataOutputStream)object2).mOutputStream).toByteArray();
        object = outputStream;
        byteOrderedDataOutputStream.write((byte[])object2);
        object = outputStream;
        object = outputStream;
        CRC32 cRC32 = new CRC32();
        object = outputStream;
        cRC32.update((byte[])object2, 4, ((Object)object2).length - 4);
        object = outputStream;
        byteOrderedDataOutputStream.writeInt((int)cRC32.getValue());
        ExifInterface.closeQuietly(outputStream);
        ExifInterface.copy(dataInputStream, byteOrderedDataOutputStream);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void saveWebpAttributes(InputStream var1_1, OutputStream var2_4) throws IOException {
        if (ExifInterface.DEBUG) {
            var6_5 = new StringBuilder();
            var6_5.append("saveWebpAttributes starting with (inputStream: ");
            var6_5.append(var1_1);
            var6_5.append(", outputStream: ");
            var6_5.append(var2_4);
            var6_5.append(")");
            Log.d((String)"ExifInterface", (String)var6_5.toString());
        }
        var10_6 = new ByteOrderedDataInputStream((InputStream)var1_1, ByteOrder.LITTLE_ENDIAN);
        var11_7 = new ByteOrderedDataOutputStream((OutputStream)var2_4, ByteOrder.LITTLE_ENDIAN);
        var13_8 = ExifInterface.WEBP_SIGNATURE_1;
        ExifInterface.copy(var10_6, var11_7, var13_8.length);
        var12_9 = ExifInterface.WEBP_SIGNATURE_2;
        var10_6.skipBytes(var12_9.length + 4);
        var8_10 = null;
        var2_4 = var7_11 = null;
        var6_5 = var8_10;
        try {
            block13: {
                var2_4 = var7_11;
                var6_5 = var8_10;
                var2_4 = var7_11 = (var9_12 = new ByteArrayOutputStream());
                var6_5 = var7_11;
                var2_4 = var7_11;
                var6_5 = var7_11;
                var8_10 = new ByteOrderedDataOutputStream((OutputStream)var7_11, ByteOrder.LITTLE_ENDIAN);
                var2_4 = var7_11;
                var6_5 = var7_11;
                var3_13 = this.mExifOffset;
                if (var3_13 == 0) break block13;
                var2_4 = var7_11;
                var6_5 = var7_11;
                ExifInterface.copy(var10_6, var8_10, var3_13 - (var13_8.length + 4 + var12_9.length) - 4 - 4);
                var2_4 = var7_11;
                var6_5 = var7_11;
                var10_6.skipBytes(4);
                var2_4 = var7_11;
                var6_5 = var7_11;
                var10_6.skipBytes(var10_6.readInt());
                var2_4 = var7_11;
                var6_5 = var7_11;
                this.writeExifSegment(var8_10);
                ** GOTO lbl136
            }
            var2_4 = var7_11;
            var6_5 = var7_11;
            var12_9 = new byte[4];
            var2_4 = var7_11;
            var6_5 = var7_11;
            if (var10_6.read(var12_9) != var12_9.length) ** GOTO lbl167
            var2_4 = var7_11;
            var6_5 = var7_11;
            var9_12 = ExifInterface.WEBP_CHUNK_TYPE_VP8X;
            var2_4 = var7_11;
            var6_5 = var7_11;
            if (!Arrays.equals(var12_9, (byte[])var9_12)) ** GOTO lbl130
            var2_4 = var7_11;
            var6_5 = var7_11;
            var4_14 = var10_6.readInt();
            var5_15 = 1;
            var3_13 = var4_14 % 2 == 1 ? var4_14 + 1 : var4_14;
            var2_4 = var7_11;
            var6_5 = var7_11;
            ** GOTO lbl81
        }
        catch (Exception var1_3) {
            block12: {
                var2_4 = var6_5;
                var2_4 = var6_5;
                var7_11 = new IOException("Failed to save WebP file", var1_3);
                var2_4 = var6_5;
                throw var7_11;
lbl81:
                // 2 sources

                var12_9 = new byte[var3_13];
                var2_4 = var7_11;
                var6_5 = var7_11;
                var10_6.read(var12_9);
                var12_9[0] = (byte)(var12_9[0] | 8);
                var3_13 = (var12_9[0] >> 1 & 1) == 1 ? var5_15 : 0;
                var2_4 = var7_11;
                var6_5 = var7_11;
                try {
                    block11: {
                        var8_10.write((byte[])var9_12);
                        var2_4 = var7_11;
                        var6_5 = var7_11;
                        var8_10.writeInt(var4_14);
                        var2_4 = var7_11;
                        var6_5 = var7_11;
                        var8_10.write(var12_9);
                        if (var3_13 != 0) {
                            var2_4 = var7_11;
                            var6_5 = var7_11;
                            this.copyChunksUpToGivenChunkType(var10_6, var8_10, ExifInterface.WEBP_CHUNK_TYPE_ANIM, null);
                            while (true) {
                                var2_4 = var7_11;
                                var6_5 = var7_11;
                                var9_12 = new byte[4];
                                var2_4 = var7_11;
                                var6_5 = var7_11;
                                var1_1.read((byte[])var9_12);
                                var2_4 = var7_11;
                                var6_5 = var7_11;
                                if (!Arrays.equals((byte[])var9_12, ExifInterface.WEBP_CHUNK_TYPE_ANMF)) {
                                    var2_4 = var7_11;
                                    var6_5 = var7_11;
                                    this.writeExifSegment(var8_10);
                                    break block11;
                                }
                                var2_4 = var7_11;
                                var6_5 = var7_11;
                                this.copyWebPChunk(var10_6, var8_10, (byte[])var9_12);
                            }
                        }
                        var2_4 = var7_11;
                        var6_5 = var7_11;
                        this.copyChunksUpToGivenChunkType(var10_6, var8_10, ExifInterface.WEBP_CHUNK_TYPE_VP8, ExifInterface.WEBP_CHUNK_TYPE_VP8L);
                        var2_4 = var7_11;
                        var6_5 = var7_11;
                        this.writeExifSegment(var8_10);
                        break block11;
lbl130:
                        // 1 sources

                        var2_4 = var7_11;
                        var6_5 = var7_11;
                        if (Arrays.equals(var12_9, ExifInterface.WEBP_CHUNK_TYPE_VP8)) break block12;
                        var2_4 = var7_11;
                        var6_5 = var7_11;
                        if (Arrays.equals(var12_9, ExifInterface.WEBP_CHUNK_TYPE_VP8L)) break block12;
                    }
                    var2_4 = var7_11;
                    var6_5 = var7_11;
                    ExifInterface.copy(var10_6, var8_10);
                    var2_4 = var7_11;
                    var6_5 = var7_11;
                    var3_13 = var7_11.size();
                    var2_4 = var7_11;
                    var6_5 = var7_11;
                    var1_1 = ExifInterface.WEBP_SIGNATURE_2;
                    var2_4 = var7_11;
                    var6_5 = var7_11;
                    var11_7.writeInt(var3_13 + ((Object)var1_1).length);
                    var2_4 = var7_11;
                    var6_5 = var7_11;
                    var11_7.write((byte[])var1_1);
                    var2_4 = var7_11;
                    var6_5 = var7_11;
                    var7_11.writeTo(var11_7);
                }
                catch (Throwable var1_2) {}
                ExifInterface.closeQuietly((Closeable)var7_11);
                return;
            }
            var2_4 = var7_11;
            var6_5 = var7_11;
            {
                var2_4 = var7_11;
                var6_5 = var7_11;
                var1_1 = new IOException("WebP files with only VP8 or VP8L chunks are currently not supported");
                var2_4 = var7_11;
                var6_5 = var7_11;
                throw var1_1;
lbl167:
                // 1 sources

                var2_4 = var7_11;
                var6_5 = var7_11;
                var2_4 = var7_11;
                var6_5 = var7_11;
                var1_1 = new IOException("Encountered invalid length while parsing WebP chunk type");
                var2_4 = var7_11;
                var6_5 = var7_11;
                throw var1_1;
            }
        }
        ExifInterface.closeQuietly((Closeable)var2_4);
        throw var1_2;
    }

    private void setThumbnailData(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        HashMap<String, ExifAttribute> hashMap = this.mAttributes[4];
        ExifAttribute exifAttribute = hashMap.get(TAG_COMPRESSION);
        if (exifAttribute != null) {
            int n;
            this.mThumbnailCompression = n = exifAttribute.getIntValue(this.mExifByteOrder);
            switch (n) {
                default: {
                    break;
                }
                case 6: {
                    this.handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
                    break;
                }
                case 1: 
                case 7: {
                    if (this.isSupportedDataType(hashMap)) {
                        this.handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } else {
            this.mThumbnailCompression = 6;
            this.handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
        }
    }

    private static boolean startsWith(byte[] byArray, byte[] byArray2) {
        if (byArray != null && byArray2 != null) {
            if (byArray.length < byArray2.length) {
                return false;
            }
            for (int i = 0; i < byArray2.length; ++i) {
                if (byArray[i] == byArray2[i]) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private void swapBasedOnImageSize(int n, int n2) throws IOException {
        if (!this.mAttributes[n].isEmpty() && !this.mAttributes[n2].isEmpty()) {
            ExifAttribute exifAttribute = this.mAttributes[n].get(TAG_IMAGE_LENGTH);
            Object object = this.mAttributes[n].get(TAG_IMAGE_WIDTH);
            HashMap<String, ExifAttribute>[] hashMapArray = this.mAttributes[n2].get(TAG_IMAGE_LENGTH);
            ExifAttribute exifAttribute2 = this.mAttributes[n2].get(TAG_IMAGE_WIDTH);
            if (exifAttribute != null && object != null) {
                if (hashMapArray != null && exifAttribute2 != null) {
                    int n3 = exifAttribute.getIntValue(this.mExifByteOrder);
                    int n4 = ((ExifAttribute)object).getIntValue(this.mExifByteOrder);
                    int n5 = hashMapArray.getIntValue(this.mExifByteOrder);
                    int n6 = exifAttribute2.getIntValue(this.mExifByteOrder);
                    if (n3 < n5 && n4 < n6) {
                        hashMapArray = this.mAttributes;
                        object = hashMapArray[n];
                        hashMapArray[n] = hashMapArray[n2];
                        hashMapArray[n2] = object;
                    }
                } else if (DEBUG) {
                    Log.d((String)TAG, (String)"Second image does not contain valid size information");
                }
            } else if (DEBUG) {
                Log.d((String)TAG, (String)"First image does not contain valid size information");
            }
            return;
        }
        if (DEBUG) {
            Log.d((String)TAG, (String)"Cannot perform swap since only one image data exists");
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     */
    private void updateImageSizeValues(ByteOrderedDataInputStream object, int n) throws IOException {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        ExifAttribute exifAttribute3;
        ExifAttribute exifAttribute4;
        block9: {
            int[] nArray;
            block10: {
                void var7_10;
                block8: {
                    ExifAttribute exifAttribute5 = this.mAttributes[n].get(TAG_DEFAULT_CROP_SIZE);
                    exifAttribute4 = this.mAttributes[n].get(TAG_RW2_SENSOR_TOP_BORDER);
                    exifAttribute3 = this.mAttributes[n].get(TAG_RW2_SENSOR_LEFT_BORDER);
                    exifAttribute2 = this.mAttributes[n].get(TAG_RW2_SENSOR_BOTTOM_BORDER);
                    exifAttribute = this.mAttributes[n].get(TAG_RW2_SENSOR_RIGHT_BORDER);
                    if (exifAttribute5 == null) break block9;
                    if (exifAttribute5.format == 5) {
                        Object[] objectArray = (Rational[])exifAttribute5.getValue(this.mExifByteOrder);
                        if (objectArray != null && objectArray.length == 2) {
                            object = ExifAttribute.createURational(objectArray[0], this.mExifByteOrder);
                            ExifAttribute exifAttribute6 = ExifAttribute.createURational((Rational)objectArray[1], this.mExifByteOrder);
                            break block8;
                        } else {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Invalid crop size values. cropSize=");
                            ((StringBuilder)object).append(Arrays.toString(objectArray));
                            Log.w((String)TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                    }
                    nArray = (int[])exifAttribute5.getValue(this.mExifByteOrder);
                    if (nArray == null || nArray.length != 2) break block10;
                    object = ExifAttribute.createUShort(nArray[0], this.mExifByteOrder);
                    ExifAttribute exifAttribute7 = ExifAttribute.createUShort(nArray[1], this.mExifByteOrder);
                }
                this.mAttributes[n].put(TAG_IMAGE_WIDTH, (ExifAttribute)object);
                this.mAttributes[n].put(TAG_IMAGE_LENGTH, (ExifAttribute)var7_10);
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Invalid crop size values. cropSize=");
            ((StringBuilder)object).append(Arrays.toString(nArray));
            Log.w((String)TAG, (String)((StringBuilder)object).toString());
            return;
        }
        if (exifAttribute4 != null && exifAttribute3 != null && exifAttribute2 != null && exifAttribute != null) {
            int n2 = exifAttribute4.getIntValue(this.mExifByteOrder);
            int n3 = exifAttribute2.getIntValue(this.mExifByteOrder);
            int n4 = exifAttribute.getIntValue(this.mExifByteOrder);
            int n5 = exifAttribute3.getIntValue(this.mExifByteOrder);
            if (n3 <= n2) return;
            if (n4 <= n5) return;
            object = ExifAttribute.createUShort(n3 - n2, this.mExifByteOrder);
            ExifAttribute exifAttribute8 = ExifAttribute.createUShort(n4 - n5, this.mExifByteOrder);
            this.mAttributes[n].put(TAG_IMAGE_LENGTH, (ExifAttribute)object);
            this.mAttributes[n].put(TAG_IMAGE_WIDTH, exifAttribute8);
            return;
        }
        this.retrieveJpegImageSize((ByteOrderedDataInputStream)object, n);
    }

    private void validateImages() throws IOException {
        this.swapBasedOnImageSize(0, 5);
        this.swapBasedOnImageSize(0, 4);
        this.swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute = this.mAttributes[1].get(TAG_PIXEL_X_DIMENSION);
        HashMap<String, ExifAttribute>[] hashMapArray = this.mAttributes[1].get(TAG_PIXEL_Y_DIMENSION);
        if (exifAttribute != null && hashMapArray != null) {
            this.mAttributes[0].put(TAG_IMAGE_WIDTH, exifAttribute);
            this.mAttributes[0].put(TAG_IMAGE_LENGTH, (ExifAttribute)hashMapArray);
        }
        if (this.mAttributes[4].isEmpty() && this.isThumbnail(this.mAttributes[5])) {
            hashMapArray = this.mAttributes;
            hashMapArray[4] = hashMapArray[5];
            hashMapArray[5] = new HashMap();
        }
        if (!this.isThumbnail(this.mAttributes[4])) {
            Log.d((String)TAG, (String)"No image meets the size requirements of a thumbnail image.");
        }
    }

    private int writeExifSegment(ByteOrderedDataOutputStream byteOrderedDataOutputStream) throws IOException {
        int n;
        int n2;
        int n3;
        Object object = EXIF_TAGS;
        int[] nArray = new int[((ExifTag[][])object).length];
        object = new int[((ExifTag[][])object).length];
        ExifTag[] object22 = EXIF_POINTER_TAGS;
        int n4 = object22.length;
        for (n3 = 0; n3 < n4; ++n3) {
            this.removeAttribute(object22[n3].name);
        }
        this.removeAttribute(ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name);
        this.removeAttribute(ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        for (n3 = 0; n3 < EXIF_TAGS.length; ++n3) {
            Object[] objectArray = this.mAttributes[n3].entrySet().toArray();
            n2 = objectArray.length;
            for (n4 = 0; n4 < n2; ++n4) {
                Map.Entry entry = (Map.Entry)objectArray[n4];
                if (entry.getValue() != null) continue;
                this.mAttributes[n3].remove(entry.getKey());
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(ExifInterface.EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0L, this.mExifByteOrder));
            this.mAttributes[4].put(ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong(this.mThumbnailLength, this.mExifByteOrder));
        }
        for (n3 = 0; n3 < EXIF_TAGS.length; ++n3) {
            n2 = 0;
            Iterator<Map.Entry<String, ExifAttribute>> iterator2 = this.mAttributes[n3].entrySet().iterator();
            while (iterator2.hasNext()) {
                n = iterator2.next().getValue().size();
                n4 = n2;
                if (n > 4) {
                    n4 = n2 + n;
                }
                n2 = n4;
            }
            object[n3] = object[n3] + n2;
        }
        n3 = 8;
        for (n4 = 0; n4 < EXIF_TAGS.length; ++n4) {
            n2 = n3;
            if (!this.mAttributes[n4].isEmpty()) {
                nArray[n4] = n3;
                n2 = n3 + (this.mAttributes[n4].size() * 12 + 2 + 4 + object[n4]);
            }
            n3 = n2;
        }
        n4 = n3;
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(n3, this.mExifByteOrder));
            this.mThumbnailOffset = this.mExifOffset + n3;
            n4 = n3 + this.mThumbnailLength;
        }
        n3 = n4;
        if (this.mMimeType == 4) {
            n3 = n4 + 8;
        }
        if (DEBUG) {
            for (n4 = 0; n4 < EXIF_TAGS.length; ++n4) {
                Log.d((String)TAG, (String)String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d, total size: %d", n4, nArray[n4], this.mAttributes[n4].size(), (int)object[n4], n3));
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(nArray[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(nArray[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(ExifInterface.EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(nArray[3], this.mExifByteOrder));
        }
        switch (this.mMimeType) {
            default: {
                break;
            }
            case 14: {
                byteOrderedDataOutputStream.write(WEBP_CHUNK_TYPE_EXIF);
                byteOrderedDataOutputStream.writeInt(n3);
                break;
            }
            case 13: {
                byteOrderedDataOutputStream.writeInt(n3);
                byteOrderedDataOutputStream.write(PNG_CHUNK_TYPE_EXIF);
                break;
            }
            case 4: {
                byteOrderedDataOutputStream.writeUnsignedShort(n3);
                byteOrderedDataOutputStream.write(IDENTIFIER_EXIF_APP1);
            }
        }
        short s = this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? (short)19789 : 18761;
        byteOrderedDataOutputStream.writeShort(s);
        byteOrderedDataOutputStream.setByteOrder(this.mExifByteOrder);
        byteOrderedDataOutputStream.writeUnsignedShort(42);
        byteOrderedDataOutputStream.writeUnsignedInt(8L);
        for (n4 = 0; n4 < EXIF_TAGS.length; ++n4) {
            if (this.mAttributes[n4].isEmpty()) continue;
            byteOrderedDataOutputStream.writeUnsignedShort(this.mAttributes[n4].size());
            n2 = nArray[n4] + 2 + this.mAttributes[n4].size() * 12 + 4;
            for (Map.Entry entry : this.mAttributes[n4].entrySet()) {
                n = ExifInterface.sExifTagMapsForWriting[n4].get(entry.getKey()).number;
                ExifAttribute exifAttribute = (ExifAttribute)entry.getValue();
                int n5 = exifAttribute.size();
                byteOrderedDataOutputStream.writeUnsignedShort(n);
                byteOrderedDataOutputStream.writeUnsignedShort(exifAttribute.format);
                byteOrderedDataOutputStream.writeInt(exifAttribute.numberOfComponents);
                if (n5 > 4) {
                    byteOrderedDataOutputStream.writeUnsignedInt(n2);
                    n = n2 + n5;
                } else {
                    byteOrderedDataOutputStream.write(exifAttribute.bytes);
                    n = n2;
                    if (n5 < 4) {
                        while (true) {
                            n = n2;
                            if (n5 >= 4) break;
                            byteOrderedDataOutputStream.writeByte(0);
                            ++n5;
                        }
                    }
                }
                n2 = n;
            }
            if (n4 == 0 && !this.mAttributes[4].isEmpty()) {
                byteOrderedDataOutputStream.writeUnsignedInt(nArray[4]);
            } else {
                byteOrderedDataOutputStream.writeUnsignedInt(0L);
            }
            object = this.mAttributes[n4].entrySet().iterator();
            while (object.hasNext()) {
                ExifAttribute exifAttribute = (ExifAttribute)((Map.Entry)object.next()).getValue();
                if (exifAttribute.bytes.length <= 4) continue;
                byteOrderedDataOutputStream.write(exifAttribute.bytes, 0, exifAttribute.bytes.length);
            }
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream.write(this.getThumbnailBytes());
        }
        if (this.mMimeType == 14 && n3 % 2 == 1) {
            byteOrderedDataOutputStream.writeByte(0);
        }
        byteOrderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        return n3;
    }

    public void flipHorizontally() {
        int n;
        switch (this.getAttributeInt(TAG_ORIENTATION, 1)) {
            default: {
                n = 0;
                break;
            }
            case 8: {
                n = 7;
                break;
            }
            case 7: {
                n = 8;
                break;
            }
            case 6: {
                n = 5;
                break;
            }
            case 5: {
                n = 6;
                break;
            }
            case 4: {
                n = 3;
                break;
            }
            case 3: {
                n = 4;
                break;
            }
            case 2: {
                n = 1;
                break;
            }
            case 1: {
                n = 2;
            }
        }
        this.setAttribute(TAG_ORIENTATION, Integer.toString(n));
    }

    public void flipVertically() {
        int n;
        switch (this.getAttributeInt(TAG_ORIENTATION, 1)) {
            default: {
                n = 0;
                break;
            }
            case 8: {
                n = 5;
                break;
            }
            case 7: {
                n = 6;
                break;
            }
            case 6: {
                n = 7;
                break;
            }
            case 5: {
                n = 8;
                break;
            }
            case 4: {
                n = 1;
                break;
            }
            case 3: {
                n = 2;
                break;
            }
            case 2: {
                n = 3;
                break;
            }
            case 1: {
                n = 4;
            }
        }
        this.setAttribute(TAG_ORIENTATION, Integer.toString(n));
    }

    public double getAltitude(double d) {
        double d2 = this.getAttributeDouble(TAG_GPS_ALTITUDE, -1.0);
        int n = -1;
        int n2 = this.getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
        if (d2 >= 0.0 && n2 >= 0) {
            if (n2 != 1) {
                n = 1;
            }
            d = n;
            Double.isNaN(d);
            return d * d2;
        }
        return d;
    }

    public String getAttribute(String charSequence) {
        if (charSequence != null) {
            Object[] objectArray = this.getExifAttribute((String)charSequence);
            if (objectArray != null) {
                if (!sTagSetForCompatibility.contains(charSequence)) {
                    return objectArray.getStringValue(this.mExifByteOrder);
                }
                if (((String)charSequence).equals(TAG_GPS_TIMESTAMP)) {
                    if (objectArray.format != 5 && objectArray.format != 10) {
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append("GPS Timestamp format is not rational. format=");
                        ((StringBuilder)charSequence).append(objectArray.format);
                        Log.w((String)TAG, (String)((StringBuilder)charSequence).toString());
                        return null;
                    }
                    if ((objectArray = (Rational[])objectArray.getValue(this.mExifByteOrder)) != null && objectArray.length == 3) {
                        return String.format("%02d:%02d:%02d", (int)((float)((Rational)objectArray[0]).numerator / (float)((Rational)objectArray[0]).denominator), (int)((float)((Rational)objectArray[1]).numerator / (float)((Rational)objectArray[1]).denominator), (int)((float)((Rational)objectArray[2]).numerator / (float)((Rational)objectArray[2]).denominator));
                    }
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append("Invalid GPS Timestamp array. array=");
                    ((StringBuilder)charSequence).append(Arrays.toString(objectArray));
                    Log.w((String)TAG, (String)((StringBuilder)charSequence).toString());
                    return null;
                }
                try {
                    charSequence = Double.toString(objectArray.getDoubleValue(this.mExifByteOrder));
                    return charSequence;
                }
                catch (NumberFormatException numberFormatException) {
                    return null;
                }
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public byte[] getAttributeBytes(String object) {
        if (object != null) {
            if ((object = this.getExifAttribute((String)object)) != null) {
                return ((ExifAttribute)object).bytes;
            }
            return null;
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public double getAttributeDouble(String object, double d) {
        if (object != null) {
            if ((object = this.getExifAttribute((String)object)) == null) {
                return d;
            }
            try {
                double d2 = ((ExifAttribute)object).getDoubleValue(this.mExifByteOrder);
                return d2;
            }
            catch (NumberFormatException numberFormatException) {
                return d;
            }
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public int getAttributeInt(String object, int n) {
        if (object != null) {
            if ((object = this.getExifAttribute((String)object)) == null) {
                return n;
            }
            try {
                int n2 = ((ExifAttribute)object).getIntValue(this.mExifByteOrder);
                return n2;
            }
            catch (NumberFormatException numberFormatException) {
                return n;
            }
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public long[] getAttributeRange(String object) {
        if (object != null) {
            if (!this.mModified) {
                if ((object = this.getExifAttribute((String)object)) != null) {
                    return new long[]{((ExifAttribute)object).bytesOffset, ((ExifAttribute)object).bytes.length};
                }
                return null;
            }
            throw new IllegalStateException("The underlying file has been modified since being parsed");
        }
        throw new NullPointerException("tag shouldn't be null");
    }

    public long getDateTime() {
        return ExifInterface.parseDateTime(this.getAttribute(TAG_DATETIME), this.getAttribute(TAG_SUBSEC_TIME));
    }

    public long getDateTimeDigitized() {
        return ExifInterface.parseDateTime(this.getAttribute(TAG_DATETIME_DIGITIZED), this.getAttribute(TAG_SUBSEC_TIME_DIGITIZED));
    }

    public long getDateTimeOriginal() {
        return ExifInterface.parseDateTime(this.getAttribute(TAG_DATETIME_ORIGINAL), this.getAttribute(TAG_SUBSEC_TIME_ORIGINAL));
    }

    public long getGpsDateTime() {
        Serializable serializable;
        Object object = this.getAttribute(TAG_GPS_DATESTAMP);
        Object object2 = this.getAttribute(TAG_GPS_TIMESTAMP);
        if (object != null && object2 != null && (((Pattern)(serializable = sNonZeroTimePattern)).matcher((CharSequence)object).matches() || ((Pattern)serializable).matcher((CharSequence)object2).matches())) {
            block4: {
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append((String)object);
                ((StringBuilder)serializable).append(' ');
                ((StringBuilder)serializable).append((String)object2);
                object = ((StringBuilder)serializable).toString();
                object2 = new ParsePosition(0);
                try {
                    object = sFormatter.parse((String)object, (ParsePosition)object2);
                    if (object != null) break block4;
                    return -1L;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    return -1L;
                }
            }
            long l = ((Date)object).getTime();
            return l;
        }
        return -1L;
    }

    @Deprecated
    public boolean getLatLong(float[] fArray) {
        double[] dArray = this.getLatLong();
        if (dArray == null) {
            return false;
        }
        fArray[0] = (float)dArray[0];
        fArray[1] = (float)dArray[1];
        return true;
    }

    public double[] getLatLong() {
        String string2 = this.getAttribute(TAG_GPS_LATITUDE);
        String string3 = this.getAttribute(TAG_GPS_LATITUDE_REF);
        String string4 = this.getAttribute(TAG_GPS_LONGITUDE);
        String string5 = this.getAttribute(TAG_GPS_LONGITUDE_REF);
        if (string2 != null && string3 != null && string4 != null && string5 != null) {
            double d;
            double d2;
            try {
                d2 = ExifInterface.convertRationalLatLonToDouble(string2, string3);
                d = ExifInterface.convertRationalLatLonToDouble(string4, string5);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Latitude/longitude values are not parsable. ");
                stringBuilder.append(String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", string2, string3, string4, string5));
                Log.w((String)TAG, (String)stringBuilder.toString());
            }
            return new double[]{d2, d};
        }
        return null;
    }

    public int getRotationDegrees() {
        switch (this.getAttributeInt(TAG_ORIENTATION, 1)) {
            default: {
                return 0;
            }
            case 6: 
            case 7: {
                return 90;
            }
            case 5: 
            case 8: {
                return 270;
            }
            case 3: 
            case 4: 
        }
        return 180;
    }

    public byte[] getThumbnail() {
        int n = this.mThumbnailCompression;
        if (n != 6 && n != 7) {
            return null;
        }
        return this.getThumbnailBytes();
    }

    public Bitmap getThumbnailBitmap() {
        int n;
        if (!this.mHasThumbnail) {
            return null;
        }
        if (this.mThumbnailBytes == null) {
            this.mThumbnailBytes = this.getThumbnailBytes();
        }
        if ((n = this.mThumbnailCompression) != 6 && n != 7) {
            if (n == 1) {
                Object object;
                int[] nArray = new int[this.mThumbnailBytes.length / 3];
                for (n = 0; n < nArray.length; ++n) {
                    object = this.mThumbnailBytes;
                    nArray[n] = (object[n * 3] << 16) + 0 + (object[n * 3 + 1] << 8) + object[n * 3 + 2];
                }
                object = this.mAttributes[4].get(TAG_IMAGE_LENGTH);
                ExifAttribute exifAttribute = this.mAttributes[4].get(TAG_IMAGE_WIDTH);
                if (object != null && exifAttribute != null) {
                    n = ((ExifAttribute)object).getIntValue(this.mExifByteOrder);
                    return Bitmap.createBitmap((int[])nArray, (int)exifAttribute.getIntValue(this.mExifByteOrder), (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                }
            }
            return null;
        }
        return BitmapFactory.decodeByteArray((byte[])this.mThumbnailBytes, (int)0, (int)this.mThumbnailLength);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getThumbnailBytes() {
        if (!this.mHasThumbnail) {
            return null;
        }
        var4_1 /* !! */  = this.mThumbnailBytes;
        if (var4_1 /* !! */  != null) {
            return var4_1 /* !! */ ;
        }
        var11_4 = null;
        var12_5 = null;
        var15_6 = null;
        var13_7 = null;
        var14_8 = null;
        var10_9 = null;
        var6_10 = var11_4;
        var5_11 = var13_7;
        var8_12 = var12_5;
        var7_13 = var14_8;
        try {
            block16: {
                var4_1 /* !! */  = (byte[])this.mAssetInputStream;
                if (var4_1 /* !! */  == null) ** GOTO lbl50
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var13_7;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var14_8;
                if (!var4_1 /* !! */ .markSupported()) break block16;
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var13_7;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var14_8;
                var4_1 /* !! */ .reset();
                var9_14 = var10_9;
                ** GOTO lbl94
            }
            var6_10 = var4_1 /* !! */ ;
            var5_11 = var13_7;
            var8_12 = var4_1 /* !! */ ;
            var7_13 = var14_8;
            Log.d((String)"ExifInterface", (String)"Cannot read thumbnail from inputstream without mark/reset support");
            ** GOTO lbl48
        }
        catch (Exception var4_3) {
            block15: {
                var6_10 = var8_12;
                var5_11 = var7_13;
                Log.d((String)"ExifInterface", (String)"Encountered exception while getting thumbnail", (Throwable)var4_3);
                ExifInterface.closeQuietly((Closeable)var8_12);
                if (var7_13 != null) {
                    ExifInterface.closeFileDescriptor(var7_13);
                }
                return null;
lbl48:
                // 1 sources

                ExifInterface.closeQuietly((Closeable)var4_1 /* !! */ );
                return null;
lbl50:
                // 1 sources

                var6_10 = var11_4;
                var5_11 = var13_7;
                var8_12 = var12_5;
                var7_13 = var14_8;
                try {
                    if (this.mFilename != null) {
                        var6_10 = var11_4;
                        var5_11 = var13_7;
                        var8_12 = var12_5;
                        var7_13 = var14_8;
                        var4_1 /* !! */  = new FileInputStream(this.mFilename);
                        var9_14 = var10_9;
                    } else {
                        var4_1 /* !! */  = var15_6;
                        var9_14 = var10_9;
                        var6_10 = var11_4;
                        var5_11 = var13_7;
                        var8_12 = var12_5;
                        var7_13 = var14_8;
                        if (Build.VERSION.SDK_INT >= 21) {
                            var6_10 = var11_4;
                            var5_11 = var13_7;
                            var8_12 = var12_5;
                            var7_13 = var14_8;
                            var16_15 = this.mSeekableFileDescriptor;
                            var4_1 /* !! */  = var15_6;
                            var9_14 = var10_9;
                            if (var16_15 != null) {
                                var6_10 = var11_4;
                                var5_11 = var13_7;
                                var8_12 = var12_5;
                                var7_13 = var14_8;
                                var9_14 = Os.dup((FileDescriptor)var16_15);
                                var6_10 = var11_4;
                                var5_11 = var9_14;
                                var8_12 = var12_5;
                                var7_13 = var9_14;
                                Os.lseek((FileDescriptor)var9_14, (long)0L, (int)OsConstants.SEEK_SET);
                                var6_10 = var11_4;
                                var5_11 = var9_14;
                                var8_12 = var12_5;
                                var7_13 = var9_14;
                                var4_1 /* !! */  = new FileInputStream((FileDescriptor)var9_14);
                            }
                        }
                    }
lbl94:
                    // 7 sources

                    if (var4_1 /* !! */  == null) ** GOTO lbl155
                    var6_10 = var4_1 /* !! */ ;
                    var5_11 = var9_14;
                    var8_12 = var4_1 /* !! */ ;
                    var7_13 = var9_14;
                    var2_16 = var4_1 /* !! */ .skip(this.mThumbnailOffset);
                    var6_10 = var4_1 /* !! */ ;
                    var5_11 = var9_14;
                    var8_12 = var4_1 /* !! */ ;
                    var7_13 = var9_14;
                    var1_17 = this.mThumbnailOffset;
                    if (var2_16 != (long)var1_17) ** GOTO lbl141
                    var6_10 = var4_1 /* !! */ ;
                    var5_11 = var9_14;
                    var8_12 = var4_1 /* !! */ ;
                    var7_13 = var9_14;
                    var10_9 = new byte[this.mThumbnailLength];
                    var6_10 = var4_1 /* !! */ ;
                    var5_11 = var9_14;
                    var8_12 = var4_1 /* !! */ ;
                    var7_13 = var9_14;
                    if (var4_1 /* !! */ .read((byte[])var10_9) != this.mThumbnailLength) break block15;
                    var6_10 = var4_1 /* !! */ ;
                    var5_11 = var9_14;
                    var8_12 = var4_1 /* !! */ ;
                    var7_13 = var9_14;
                    this.mThumbnailBytes = (byte[])var10_9;
                }
                catch (Throwable var4_2) {}
                ExifInterface.closeQuietly((Closeable)var4_1 /* !! */ );
                if (var9_14 != null) {
                    ExifInterface.closeFileDescriptor((FileDescriptor)var9_14);
                }
                return var10_9;
            }
            var6_10 = var4_1 /* !! */ ;
            var5_11 = var9_14;
            var8_12 = var4_1 /* !! */ ;
            var7_13 = var9_14;
            {
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                var10_9 = new IOException("Corrupted image");
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                throw var10_9;
lbl141:
                // 1 sources

                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                var10_9 = new IOException("Corrupted image");
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                throw var10_9;
lbl155:
                // 1 sources

                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                var10_9 = new FileNotFoundException();
                var6_10 = var4_1 /* !! */ ;
                var5_11 = var9_14;
                var8_12 = var4_1 /* !! */ ;
                var7_13 = var9_14;
                throw var10_9;
            }
        }
        ExifInterface.closeQuietly(var6_10);
        if (var5_11 != null) {
            ExifInterface.closeFileDescriptor(var5_11);
        }
        throw var4_2;
    }

    public long[] getThumbnailRange() {
        if (!this.mModified) {
            if (this.mHasThumbnail) {
                if (this.mHasThumbnailStrips && !this.mAreThumbnailStripsConsecutive) {
                    return null;
                }
                return new long[]{this.mThumbnailOffset, this.mThumbnailLength};
            }
            return null;
        }
        throw new IllegalStateException("The underlying file has been modified since being parsed");
    }

    public boolean hasAttribute(String string2) {
        boolean bl = this.getExifAttribute(string2) != null;
        return bl;
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    public boolean isFlipped() {
        switch (this.getAttributeInt(TAG_ORIENTATION, 1)) {
            default: {
                return false;
            }
            case 2: 
            case 4: 
            case 5: 
            case 7: 
        }
        return true;
    }

    public boolean isThumbnailCompressed() {
        if (!this.mHasThumbnail) {
            return false;
        }
        int n = this.mThumbnailCompression;
        return n == 6 || n == 7;
        {
        }
    }

    public void resetOrientation() {
        this.setAttribute(TAG_ORIENTATION, Integer.toString(1));
    }

    public void rotate(int n) {
        if (n % 90 == 0) {
            int n2 = this.getAttributeInt(TAG_ORIENTATION, 1);
            List<Integer> list = ROTATION_ORDER;
            boolean bl = list.contains(n2);
            int n3 = 0;
            int n4 = 0;
            if (bl) {
                n3 = list.indexOf(n2);
                n3 = (n / 90 + n3) % 4;
                n = n4;
                if (n3 < 0) {
                    n = 4;
                }
                n = list.get(n3 + n);
            } else {
                list = FLIPPED_ROTATION_ORDER;
                if (list.contains(n2)) {
                    n4 = list.indexOf(n2);
                    n4 = (n / 90 + n4) % 4;
                    n = n3;
                    if (n4 < 0) {
                        n = 4;
                    }
                    n = list.get(n4 + n);
                } else {
                    n = 0;
                }
            }
            this.setAttribute(TAG_ORIENTATION, Integer.toString(n));
            return;
        }
        throw new IllegalArgumentException("degree should be a multiple of 90");
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void saveAttributes() throws IOException {
        Throwable throwable3222222;
        Object object;
        Object object2;
        block24: {
            Throwable throwable22222222;
            Object object3;
            File file;
            Object object4;
            block26: {
                Object object5;
                Object object6;
                block25: {
                    File file2;
                    FileInputStream fileInputStream;
                    FileInputStream fileInputStream2;
                    FileInputStream fileInputStream3;
                    Object object7;
                    InputStream inputStream;
                    Closeable closeable;
                    block23: {
                        if (!this.isSupportedFormatForSavingAttributes()) {
                            throw new IOException("ExifInterface only supports saving attributes on JPEG, PNG, or WebP formats.");
                        }
                        if (this.mSeekableFileDescriptor == null && this.mFilename == null) {
                            throw new IOException("ExifInterface does not support saving attributes for the current input.");
                        }
                        this.mModified = true;
                        this.mThumbnailBytes = this.getThumbnail();
                        closeable = null;
                        inputStream = null;
                        object7 = null;
                        fileInputStream3 = null;
                        fileInputStream2 = null;
                        fileInputStream = null;
                        file2 = null;
                        if (this.mFilename != null) {
                            file2 = new File(this.mFilename);
                        }
                        File file3 = null;
                        object2 = closeable;
                        object = fileInputStream3;
                        object6 = inputStream;
                        object5 = fileInputStream2;
                        if (this.mFilename != null) {
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            object4 = new StringBuilder();
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            ((StringBuilder)object4).append(this.mFilename);
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            ((StringBuilder)object4).append(".tmp");
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            file = new File(((StringBuilder)object4).toString());
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            if (!file2.renameTo(file)) {
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                object4 = new StringBuilder();
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                ((StringBuilder)object4).append("Couldn't rename to ");
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                ((StringBuilder)object4).append(file.getAbsolutePath());
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                IOException iOException = new IOException(((StringBuilder)object4).toString());
                                object2 = closeable;
                                object = fileInputStream3;
                                object6 = inputStream;
                                object5 = fileInputStream2;
                                throw iOException;
                            }
                            object4 = object7;
                            object3 = fileInputStream;
                            break block23;
                        }
                        object4 = object7;
                        object3 = fileInputStream;
                        file = file3;
                        object2 = closeable;
                        object = fileInputStream3;
                        object6 = inputStream;
                        object5 = fileInputStream2;
                        if (Build.VERSION.SDK_INT < 21) break block23;
                        object4 = object7;
                        object3 = fileInputStream;
                        file = file3;
                        object2 = closeable;
                        object = fileInputStream3;
                        object6 = inputStream;
                        object5 = fileInputStream2;
                        if (this.mSeekableFileDescriptor != null) {
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            file = File.createTempFile("temp", "tmp");
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            Os.lseek((FileDescriptor)this.mSeekableFileDescriptor, (long)0L, (int)OsConstants.SEEK_SET);
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            object2 = closeable;
                            object = fileInputStream3;
                            object6 = inputStream;
                            object5 = fileInputStream2;
                            object2 = object4 = new FileInputStream(this.mSeekableFileDescriptor);
                            object = fileInputStream3;
                            object6 = object4;
                            object5 = fileInputStream2;
                            object2 = object4;
                            object = fileInputStream3;
                            object6 = object4;
                            object5 = fileInputStream2;
                            object3 = new FileOutputStream(file);
                            object2 = object4;
                            object = object3;
                            object6 = object4;
                            object5 = object3;
                            ExifInterface.copy((InputStream)object4, (OutputStream)object3);
                        }
                        {
                            catch (Throwable throwable3222222) {
                                break block24;
                            }
                            catch (Exception exception) {}
                            object2 = object6;
                            object = object5;
                            {
                                object2 = object6;
                                object = object5;
                                IOException iOException = new IOException("Failed to copy original file to temp file", exception);
                                object2 = object6;
                                object = object5;
                                throw iOException;
                            }
                        }
                    }
                    ExifInterface.closeQuietly(object4);
                    ExifInterface.closeQuietly(object3);
                    inputStream = null;
                    object5 = null;
                    closeable = null;
                    fileInputStream3 = null;
                    fileInputStream2 = null;
                    object4 = closeable;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    object4 = closeable;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    fileInputStream = new FileInputStream(file);
                    object4 = closeable;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    if (this.mFilename != null) {
                        object4 = closeable;
                        object3 = fileInputStream2;
                        object = object5;
                        object2 = fileInputStream3;
                        object4 = closeable;
                        object3 = fileInputStream2;
                        object = object5;
                        object2 = fileInputStream3;
                        object6 = new FileOutputStream(this.mFilename);
                    } else {
                        object6 = inputStream;
                        object4 = closeable;
                        object3 = fileInputStream2;
                        object = object5;
                        object2 = fileInputStream3;
                        if (Build.VERSION.SDK_INT >= 21) {
                            object4 = closeable;
                            object3 = fileInputStream2;
                            object = object5;
                            object2 = fileInputStream3;
                            object7 = this.mSeekableFileDescriptor;
                            object6 = inputStream;
                            if (object7 != null) {
                                object4 = closeable;
                                object3 = fileInputStream2;
                                object = object5;
                                object2 = fileInputStream3;
                                Os.lseek((FileDescriptor)object7, (long)0L, (int)OsConstants.SEEK_SET);
                                object4 = closeable;
                                object3 = fileInputStream2;
                                object = object5;
                                object2 = fileInputStream3;
                                object6 = new FileOutputStream(this.mSeekableFileDescriptor);
                            }
                        }
                    }
                    object4 = closeable;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    object4 = closeable;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    inputStream = new BufferedInputStream(fileInputStream);
                    object4 = object5 = inputStream;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    object4 = object5;
                    object3 = fileInputStream2;
                    object = object5;
                    object2 = fileInputStream3;
                    closeable = new BufferedOutputStream((OutputStream)object6);
                    object6 = closeable;
                    object4 = object5;
                    object3 = object6;
                    object = object5;
                    object2 = object6;
                    int n = this.mMimeType;
                    if (n == 4) {
                        object4 = object5;
                        object3 = object6;
                        object = object5;
                        object2 = object6;
                        this.saveJpegAttributes((InputStream)object5, (OutputStream)object6);
                        break block25;
                    }
                    if (n == 13) {
                        object4 = object5;
                        object3 = object6;
                        object = object5;
                        object2 = object6;
                        this.savePngAttributes((InputStream)object5, (OutputStream)object6);
                        break block25;
                    }
                    if (n == 14) {
                        object4 = object5;
                        object3 = object6;
                        object = object5;
                        object2 = object6;
                        this.saveWebpAttributes((InputStream)object5, (OutputStream)object6);
                    }
                    {
                        catch (Throwable throwable22222222) {
                            break block26;
                        }
                        catch (Exception exception) {}
                        object4 = object;
                        object3 = object2;
                        {
                            if (this.mFilename != null) {
                                object4 = object;
                                object3 = object2;
                                if (!file.renameTo(file2)) {
                                    object4 = object;
                                    object3 = object2;
                                    object4 = object;
                                    object3 = object2;
                                    object4 = object;
                                    object3 = object2;
                                    object5 = new StringBuilder();
                                    object4 = object;
                                    object3 = object2;
                                    ((StringBuilder)object5).append("Couldn't restore original file: ");
                                    object4 = object;
                                    object3 = object2;
                                    ((StringBuilder)object5).append(file2.getAbsolutePath());
                                    object4 = object;
                                    object3 = object2;
                                    IOException iOException = new IOException(((StringBuilder)object5).toString());
                                    object4 = object;
                                    object3 = object2;
                                    throw iOException;
                                }
                            }
                            object4 = object;
                            object3 = object2;
                            object4 = object;
                            object3 = object2;
                            object5 = new IOException("Failed to save new file", exception);
                            object4 = object;
                            object3 = object2;
                            throw object5;
                        }
                    }
                }
                ExifInterface.closeQuietly((Closeable)object5);
                ExifInterface.closeQuietly((Closeable)object6);
                file.delete();
                this.mThumbnailBytes = null;
                return;
            }
            ExifInterface.closeQuietly((Closeable)object4);
            ExifInterface.closeQuietly((Closeable)object3);
            file.delete();
            throw throwable22222222;
        }
        ExifInterface.closeQuietly(object2);
        ExifInterface.closeQuietly(object);
        throw throwable3222222;
    }

    public void setAltitude(double d) {
        String string2 = d >= 0.0 ? "0" : "1";
        this.setAttribute(TAG_GPS_ALTITUDE, new Rational(Math.abs(d)).toString());
        this.setAttribute(TAG_GPS_ALTITUDE_REF, string2);
    }

    public void setAttribute(String object, String object2) {
        Object[] objectArray = object2;
        if (object != null) {
            String string2;
            if (TAG_ISO_SPEED_RATINGS.equals(object)) {
                if (DEBUG) {
                    Log.d((String)TAG, (String)"setAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
                }
                string2 = TAG_PHOTOGRAPHIC_SENSITIVITY;
            } else {
                string2 = object;
            }
            object = objectArray;
            if (objectArray != null) {
                object = objectArray;
                if (sTagSetForCompatibility.contains(string2)) {
                    if (string2.equals(TAG_GPS_TIMESTAMP)) {
                        object = sGpsTimestampPattern.matcher((CharSequence)objectArray);
                        if (!((Matcher)object).find()) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Invalid value for ");
                            ((StringBuilder)object).append(string2);
                            ((StringBuilder)object).append(" : ");
                            ((StringBuilder)object).append((String)objectArray);
                            Log.w((String)TAG, (String)((StringBuilder)object).toString());
                            return;
                        }
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append(Integer.parseInt(((Matcher)object).group(1)));
                        ((StringBuilder)object2).append("/1,");
                        ((StringBuilder)object2).append(Integer.parseInt(((Matcher)object).group(2)));
                        ((StringBuilder)object2).append("/1,");
                        ((StringBuilder)object2).append(Integer.parseInt(((Matcher)object).group(3)));
                        ((StringBuilder)object2).append("/1");
                        object = ((StringBuilder)object2).toString();
                    } else {
                        try {
                            double d = Double.parseDouble((String)object2);
                            object = new Rational(d);
                            object = ((Rational)object).toString();
                        }
                        catch (NumberFormatException numberFormatException) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid value for ");
                            stringBuilder.append(string2);
                            stringBuilder.append(" : ");
                            stringBuilder.append((String)objectArray);
                            Log.w((String)TAG, (String)stringBuilder.toString());
                            return;
                        }
                    }
                }
            }
            block12: for (int i = 0; i < EXIF_TAGS.length; ++i) {
                Object object3;
                Object object4;
                int n;
                String[] stringArray;
                if (i == 4 && !this.mHasThumbnail || (stringArray = sExifTagMapsForWriting[i].get(string2)) == null) continue;
                if (object == null) {
                    this.mAttributes[i].remove(string2);
                    continue;
                }
                objectArray = ExifInterface.guessDataFormat((String)object);
                if (stringArray.primaryFormat != (Integer)objectArray.first && stringArray.primaryFormat != (Integer)objectArray.second) {
                    if (stringArray.secondaryFormat != -1 && (stringArray.secondaryFormat == (Integer)objectArray.first || stringArray.secondaryFormat == (Integer)objectArray.second)) {
                        n = stringArray.secondaryFormat;
                    } else {
                        if (stringArray.primaryFormat != 1 && stringArray.primaryFormat != 7 && stringArray.primaryFormat != 2) {
                            if (!DEBUG) continue;
                            object4 = new StringBuilder();
                            ((StringBuilder)object4).append("Given tag (");
                            ((StringBuilder)object4).append(string2);
                            ((StringBuilder)object4).append(") value didn't match with one of expected formats: ");
                            String[] stringArray2 = IFD_FORMAT_NAMES;
                            ((StringBuilder)object4).append(stringArray2[stringArray.primaryFormat]);
                            n = stringArray.secondaryFormat;
                            object3 = "";
                            if (n == -1) {
                                object2 = "";
                            } else {
                                object2 = new StringBuilder();
                                ((StringBuilder)object2).append(", ");
                                ((StringBuilder)object2).append(stringArray2[stringArray.secondaryFormat]);
                                object2 = ((StringBuilder)object2).toString();
                            }
                            ((StringBuilder)object4).append((String)object2);
                            ((StringBuilder)object4).append(" (guess: ");
                            ((StringBuilder)object4).append(stringArray2[(Integer)objectArray.first]);
                            if ((Integer)objectArray.second == -1) {
                                object2 = object3;
                            } else {
                                object2 = new StringBuilder();
                                ((StringBuilder)object2).append(", ");
                                ((StringBuilder)object2).append(stringArray2[(Integer)objectArray.second]);
                                object2 = ((StringBuilder)object2).toString();
                            }
                            ((StringBuilder)object4).append((String)object2);
                            ((StringBuilder)object4).append(")");
                            Log.d((String)TAG, (String)((StringBuilder)object4).toString());
                            continue;
                        }
                        n = stringArray.primaryFormat;
                    }
                } else {
                    n = stringArray.primaryFormat;
                }
                switch (n) {
                    default: {
                        int n2;
                        i = n2 = i;
                        if (!DEBUG) continue block12;
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Data format isn't one of expected formats: ");
                        ((StringBuilder)object2).append(n);
                        Log.d((String)TAG, (String)((StringBuilder)object2).toString());
                        i = n2;
                        continue block12;
                    }
                    case 12: {
                        object2 = ((String)object).split(",", -1);
                        objectArray = new double[((Object)object2).length];
                        for (n = 0; n < ((Object)object2).length; ++n) {
                            objectArray[n] = (String)Double.parseDouble((String)object2[n]);
                        }
                        this.mAttributes[i].put(string2, ExifAttribute.createDouble((double[])objectArray, this.mExifByteOrder));
                        continue block12;
                    }
                    case 10: {
                        object2 = ((String)object).split(",", -1);
                        objectArray = new Rational[((Object)object2).length];
                        for (n = 0; n < ((Object)object2).length; ++n) {
                            object3 = ((String)object2[n]).split("/", -1);
                            objectArray[n] = new Rational((long)Double.parseDouble((String)object3[0]), (long)Double.parseDouble((String)object3[1]));
                        }
                        this.mAttributes[i].put(string2, ExifAttribute.createSRational((Rational[])objectArray, this.mExifByteOrder));
                        continue block12;
                    }
                    case 9: {
                        object2 = ((String)object).split(",", -1);
                        objectArray = new int[((Object)object2).length];
                        for (n = 0; n < ((Object)object2).length; ++n) {
                            objectArray[n] = (String)Integer.parseInt((String)object2[n]);
                        }
                        this.mAttributes[i].put(string2, ExifAttribute.createSLong((int[])objectArray, this.mExifByteOrder));
                        continue block12;
                    }
                    case 5: {
                        object4 = ((String)object).split(",", -1);
                        object3 = new Rational[((String[])object4).length];
                        object2 = objectArray;
                        objectArray = stringArray;
                        for (n = 0; n < ((String[])object4).length; ++n) {
                            stringArray = object4[n].split("/", -1);
                            object3[n] = new Rational((long)Double.parseDouble(stringArray[0]), (long)Double.parseDouble(stringArray[1]));
                        }
                        this.mAttributes[i].put(string2, ExifAttribute.createURational((Rational[])object3, this.mExifByteOrder));
                        continue block12;
                    }
                    case 4: {
                        objectArray = ((String)object).split(",", -1);
                        object2 = new long[objectArray.length];
                        for (n = 0; n < objectArray.length; ++n) {
                            object2[n] = Long.parseLong(objectArray[n]);
                        }
                        this.mAttributes[i].put(string2, ExifAttribute.createULong((long[])object2, this.mExifByteOrder));
                        continue block12;
                    }
                    case 3: {
                        object2 = ((String)object).split(",", -1);
                        objectArray = new int[((Object)object2).length];
                        for (n = 0; n < ((Object)object2).length; ++n) {
                            objectArray[n] = (String)Integer.parseInt((String)object2[n]);
                        }
                        this.mAttributes[i].put(string2, ExifAttribute.createUShort((int[])objectArray, this.mExifByteOrder));
                        continue block12;
                    }
                    case 2: 
                    case 7: {
                        this.mAttributes[i].put(string2, ExifAttribute.createString((String)object));
                        continue block12;
                    }
                    case 1: {
                        this.mAttributes[i].put(string2, ExifAttribute.createByte((String)object));
                        continue block12;
                    }
                }
            }
            return;
        }
        object = new NullPointerException("tag shouldn't be null");
        throw object;
    }

    public void setDateTime(long l) {
        this.setAttribute(TAG_DATETIME, sFormatter.format(new Date(l)));
        this.setAttribute(TAG_SUBSEC_TIME, Long.toString(l % 1000L));
    }

    public void setGpsInfo(Location stringArray) {
        if (stringArray == null) {
            return;
        }
        this.setAttribute(TAG_GPS_PROCESSING_METHOD, stringArray.getProvider());
        this.setLatLong(stringArray.getLatitude(), stringArray.getLongitude());
        this.setAltitude(stringArray.getAltitude());
        this.setAttribute(TAG_GPS_SPEED_REF, "K");
        this.setAttribute(TAG_GPS_SPEED, new Rational(stringArray.getSpeed() * (float)TimeUnit.HOURS.toSeconds(1L) / 1000.0f).toString());
        stringArray = sFormatter.format(new Date(stringArray.getTime())).split("\\s+", -1);
        this.setAttribute(TAG_GPS_DATESTAMP, stringArray[0]);
        this.setAttribute(TAG_GPS_TIMESTAMP, stringArray[1]);
    }

    public void setLatLong(double d, double d2) {
        if (!(d < -90.0 || d > 90.0 || Double.isNaN(d))) {
            if (!(d2 < -180.0 || d2 > 180.0 || Double.isNaN(d2))) {
                String string2 = d >= 0.0 ? "N" : LATITUDE_SOUTH;
                this.setAttribute(TAG_GPS_LATITUDE_REF, string2);
                this.setAttribute(TAG_GPS_LATITUDE, this.convertDecimalDegree(Math.abs(d)));
                string2 = d2 >= 0.0 ? LONGITUDE_EAST : LONGITUDE_WEST;
                this.setAttribute(TAG_GPS_LONGITUDE_REF, string2);
                this.setAttribute(TAG_GPS_LONGITUDE, this.convertDecimalDegree(Math.abs(d2)));
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Longitude value ");
            stringBuilder.append(d2);
            stringBuilder.append(" is not valid.");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Latitude value ");
        stringBuilder.append(d);
        stringBuilder.append(" is not valid.");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static class ByteOrderedDataInputStream
    extends InputStream
    implements DataInput {
        private static final ByteOrder BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN;
        private ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
        private DataInputStream mDataInputStream;
        final int mLength;
        int mPosition;

        static {
            LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
            BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        }

        public ByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            this(inputStream, ByteOrder.BIG_ENDIAN);
        }

        ByteOrderedDataInputStream(InputStream inputStream, ByteOrder byteOrder) throws IOException {
            int n;
            inputStream = new DataInputStream(inputStream);
            this.mDataInputStream = inputStream;
            this.mLength = n = ((FilterInputStream)inputStream).available();
            this.mPosition = 0;
            this.mDataInputStream.mark(n);
            this.mByteOrder = byteOrder;
        }

        public ByteOrderedDataInputStream(byte[] byArray) throws IOException {
            this(new ByteArrayInputStream(byArray));
        }

        @Override
        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int getLength() {
            return this.mLength;
        }

        public int peek() {
            return this.mPosition;
        }

        @Override
        public int read() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.read();
        }

        @Override
        public int read(byte[] byArray, int n, int n2) throws IOException {
            n = this.mDataInputStream.read(byArray, n, n2);
            this.mPosition += n;
            return n;
        }

        @Override
        public boolean readBoolean() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.readBoolean();
        }

        @Override
        public byte readByte() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 1;
            if (n <= this.mLength) {
                n = this.mDataInputStream.read();
                if (n >= 0) {
                    return (byte)n;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        @Override
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(this.readLong());
        }

        @Override
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(this.readInt());
        }

        @Override
        public void readFully(byte[] byArray) throws IOException {
            int n;
            this.mPosition = n = this.mPosition + byArray.length;
            if (n <= this.mLength) {
                if (this.mDataInputStream.read(byArray, 0, byArray.length) == byArray.length) {
                    return;
                }
                throw new IOException("Couldn't read up to the length of buffer");
            }
            throw new EOFException();
        }

        @Override
        public void readFully(byte[] byArray, int n, int n2) throws IOException {
            int n3;
            this.mPosition = n3 = this.mPosition + n2;
            if (n3 <= this.mLength) {
                if (this.mDataInputStream.read(byArray, n, n2) == n2) {
                    return;
                }
                throw new IOException("Couldn't read up to the length of buffer");
            }
            throw new EOFException();
        }

        @Override
        public int readInt() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 4;
            if (n <= this.mLength) {
                int n2;
                int n3;
                int n4;
                n = this.mDataInputStream.read();
                if ((n | (n4 = this.mDataInputStream.read()) | (n3 = this.mDataInputStream.read()) | (n2 = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return (n2 << 24) + (n3 << 16) + (n4 << 8) + n;
                    }
                    if (object == BIG_ENDIAN) {
                        return (n << 24) + (n4 << 16) + (n3 << 8) + n2;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Invalid byte order: ");
                    ((StringBuilder)object).append(this.mByteOrder);
                    throw new IOException(((StringBuilder)object).toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public String readLine() throws IOException {
            Log.d((String)ExifInterface.TAG, (String)"Currently unsupported");
            return null;
        }

        @Override
        public long readLong() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 8;
            if (n <= this.mLength) {
                int n2;
                int n3;
                int n4;
                int n5;
                int n6;
                int n7;
                int n8 = this.mDataInputStream.read();
                if ((n8 | (n7 = this.mDataInputStream.read()) | (n6 = this.mDataInputStream.read()) | (n5 = this.mDataInputStream.read()) | (n4 = this.mDataInputStream.read()) | (n3 = this.mDataInputStream.read()) | (n2 = this.mDataInputStream.read()) | (n = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return ((long)n << 56) + ((long)n2 << 48) + ((long)n3 << 40) + ((long)n4 << 32) + ((long)n5 << 24) + ((long)n6 << 16) + ((long)n7 << 8) + (long)n8;
                    }
                    if (object == BIG_ENDIAN) {
                        return ((long)n8 << 56) + ((long)n7 << 48) + ((long)n6 << 40) + ((long)n5 << 32) + ((long)n4 << 24) + ((long)n3 << 16) + ((long)n2 << 8) + (long)n;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Invalid byte order: ");
                    ((StringBuilder)object).append(this.mByteOrder);
                    throw new IOException(((StringBuilder)object).toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public short readShort() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 2;
            if (n <= this.mLength) {
                int n2;
                n = this.mDataInputStream.read();
                if ((n | (n2 = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return (short)((n2 << 8) + n);
                    }
                    if (object == BIG_ENDIAN) {
                        return (short)((n << 8) + n2);
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Invalid byte order: ");
                    ((StringBuilder)object).append(this.mByteOrder);
                    throw new IOException(((StringBuilder)object).toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        @Override
        public int readUnsignedByte() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.readUnsignedByte();
        }

        public long readUnsignedInt() throws IOException {
            return (long)this.readInt() & 0xFFFFFFFFL;
        }

        @Override
        public int readUnsignedShort() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 2;
            if (n <= this.mLength) {
                int n2 = this.mDataInputStream.read();
                if ((n2 | (n = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return (n << 8) + n2;
                    }
                    if (object == BIG_ENDIAN) {
                        return (n2 << 8) + n;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Invalid byte order: ");
                    ((StringBuilder)object).append(this.mByteOrder);
                    throw new IOException(((StringBuilder)object).toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public void seek(long l) throws IOException {
            int n = this.mPosition;
            if ((long)n > l) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                l -= (long)n;
            }
            if (this.skipBytes((int)l) == (int)l) {
                return;
            }
            throw new IOException("Couldn't seek up to the byteCount");
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        @Override
        public int skipBytes(int n) throws IOException {
            int n2 = Math.min(n, this.mLength - this.mPosition);
            for (n = 0; n < n2; n += this.mDataInputStream.skipBytes(n2 - n)) {
            }
            this.mPosition += n;
            return n;
        }
    }

    private static class ByteOrderedDataOutputStream
    extends FilterOutputStream {
        private ByteOrder mByteOrder;
        final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.mOutputStream = outputStream;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        @Override
        public void write(byte[] byArray) throws IOException {
            this.mOutputStream.write(byArray);
        }

        @Override
        public void write(byte[] byArray, int n, int n2) throws IOException {
            this.mOutputStream.write(byArray, n, n2);
        }

        public void writeByte(int n) throws IOException {
            this.mOutputStream.write(n);
        }

        public void writeInt(int n) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write(n >>> 0 & 0xFF);
                this.mOutputStream.write(n >>> 8 & 0xFF);
                this.mOutputStream.write(n >>> 16 & 0xFF);
                this.mOutputStream.write(n >>> 24 & 0xFF);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write(n >>> 24 & 0xFF);
                this.mOutputStream.write(n >>> 16 & 0xFF);
                this.mOutputStream.write(n >>> 8 & 0xFF);
                this.mOutputStream.write(n >>> 0 & 0xFF);
            }
        }

        public void writeShort(short s) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write(s >>> 0 & 0xFF);
                this.mOutputStream.write(s >>> 8 & 0xFF);
            } else if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write(s >>> 8 & 0xFF);
                this.mOutputStream.write(s >>> 0 & 0xFF);
            }
        }

        public void writeUnsignedInt(long l) throws IOException {
            this.writeInt((int)l);
        }

        public void writeUnsignedShort(int n) throws IOException {
            this.writeShort((short)n);
        }
    }

    private static class ExifAttribute {
        public static final long BYTES_OFFSET_UNKNOWN = -1L;
        public final byte[] bytes;
        public final long bytesOffset;
        public final int format;
        public final int numberOfComponents;

        ExifAttribute(int n, int n2, long l, byte[] byArray) {
            this.format = n;
            this.numberOfComponents = n2;
            this.bytesOffset = l;
            this.bytes = byArray;
        }

        ExifAttribute(int n, int n2, byte[] byArray) {
            this(n, n2, -1L, byArray);
        }

        public static ExifAttribute createByte(String object) {
            if (((String)object).length() == 1 && ((String)object).charAt(0) >= '0' && ((String)object).charAt(0) <= '1') {
                byte[] byArray = new byte[]{(byte)(((String)object).charAt(0) - 48)};
                return new ExifAttribute(1, byArray.length, byArray);
            }
            object = ((String)object).getBytes(ASCII);
            return new ExifAttribute(1, ((Object)object).length, (byte[])object);
        }

        public static ExifAttribute createDouble(double d, ByteOrder byteOrder) {
            return ExifAttribute.createDouble(new double[]{d}, byteOrder);
        }

        public static ExifAttribute createDouble(double[] dArray, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[12] * dArray.length]);
            byteBuffer.order(byteOrder);
            int n = dArray.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putDouble(dArray[i]);
            }
            return new ExifAttribute(12, dArray.length, byteBuffer.array());
        }

        public static ExifAttribute createSLong(int n, ByteOrder byteOrder) {
            return ExifAttribute.createSLong(new int[]{n}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] nArray, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[9] * nArray.length]);
            byteBuffer.order(byteOrder);
            int n = nArray.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putInt(nArray[i]);
            }
            return new ExifAttribute(9, nArray.length, byteBuffer.array());
        }

        public static ExifAttribute createSRational(Rational rational, ByteOrder byteOrder) {
            return ExifAttribute.createSRational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] rationalArray, ByteOrder object) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[10] * rationalArray.length]);
            byteBuffer.order((ByteOrder)object);
            int n = rationalArray.length;
            for (int i = 0; i < n; ++i) {
                object = rationalArray[i];
                byteBuffer.putInt((int)((Rational)object).numerator);
                byteBuffer.putInt((int)((Rational)object).denominator);
            }
            return new ExifAttribute(10, rationalArray.length, byteBuffer.array());
        }

        public static ExifAttribute createString(String object) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append('\u0000');
            object = stringBuilder.toString().getBytes(ASCII);
            return new ExifAttribute(2, ((Object)object).length, (byte[])object);
        }

        public static ExifAttribute createULong(long l, ByteOrder byteOrder) {
            return ExifAttribute.createULong(new long[]{l}, byteOrder);
        }

        public static ExifAttribute createULong(long[] lArray, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[4] * lArray.length]);
            byteBuffer.order(byteOrder);
            int n = lArray.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putInt((int)lArray[i]);
            }
            return new ExifAttribute(4, lArray.length, byteBuffer.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            return ExifAttribute.createURational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createURational(Rational[] rationalArray, ByteOrder object) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[5] * rationalArray.length]);
            byteBuffer.order((ByteOrder)object);
            int n = rationalArray.length;
            for (int i = 0; i < n; ++i) {
                object = rationalArray[i];
                byteBuffer.putInt((int)((Rational)object).numerator);
                byteBuffer.putInt((int)((Rational)object).denominator);
            }
            return new ExifAttribute(5, rationalArray.length, byteBuffer.array());
        }

        public static ExifAttribute createUShort(int n, ByteOrder byteOrder) {
            return ExifAttribute.createUShort(new int[]{n}, byteOrder);
        }

        public static ExifAttribute createUShort(int[] nArray, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[3] * nArray.length]);
            byteBuffer.order(byteOrder);
            int n = nArray.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putShort((short)nArray[i]);
            }
            return new ExifAttribute(3, nArray.length, byteBuffer.array());
        }

        public double getDoubleValue(ByteOrder rationalArray) {
            if ((rationalArray = this.getValue((ByteOrder)rationalArray)) != null) {
                if (rationalArray instanceof String) {
                    return Double.parseDouble((String)rationalArray);
                }
                if (rationalArray instanceof long[]) {
                    if ((rationalArray = (Rational[])((long[])rationalArray)).length == 1) {
                        return (double)rationalArray[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (rationalArray instanceof int[]) {
                    if ((rationalArray = (Rational[])((int[])rationalArray)).length == 1) {
                        return (double)rationalArray[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (rationalArray instanceof double[]) {
                    if ((rationalArray = (Rational[])((double[])rationalArray)).length == 1) {
                        return (double)rationalArray[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (rationalArray instanceof Rational[]) {
                    if ((rationalArray = (Rational[])rationalArray).length == 1) {
                        return rationalArray[0].calculate();
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                throw new NumberFormatException("Couldn't find a double value");
            }
            throw new NumberFormatException("NULL can't be converted to a double value");
        }

        public int getIntValue(ByteOrder object) {
            if ((object = this.getValue((ByteOrder)object)) != null) {
                if (object instanceof String) {
                    return Integer.parseInt((String)object);
                }
                if (object instanceof long[]) {
                    if (((Object)(object = (Object)((long[])object))).length == 1) {
                        return (int)object[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (object instanceof int[]) {
                    if (((Object)(object = (Object)((int[])object))).length == 1) {
                        return (int)object[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                throw new NumberFormatException("Couldn't find a integer value");
            }
            throw new NumberFormatException("NULL can't be converted to a integer value");
        }

        public String getStringValue(ByteOrder object) {
            Object object2 = this.getValue((ByteOrder)object);
            if (object2 == null) {
                return null;
            }
            if (object2 instanceof String) {
                return (String)object2;
            }
            object = new StringBuilder();
            if (object2 instanceof long[]) {
                object2 = (long[])object2;
                for (int i = 0; i < ((Object)object2).length; ++i) {
                    ((StringBuilder)object).append((long)object2[i]);
                    if (i + 1 == ((Rational[])object2).length) continue;
                    ((StringBuilder)object).append(",");
                }
                return ((StringBuilder)object).toString();
            }
            if (object2 instanceof int[]) {
                object2 = (int[])object2;
                for (int i = 0; i < ((Object)object2).length; ++i) {
                    ((StringBuilder)object).append((int)object2[i]);
                    if (i + 1 == ((Rational[])object2).length) continue;
                    ((StringBuilder)object).append(",");
                }
                return ((StringBuilder)object).toString();
            }
            if (object2 instanceof double[]) {
                object2 = (double[])object2;
                for (int i = 0; i < ((Rational[])object2).length; ++i) {
                    ((StringBuilder)object).append((double)object2[i]);
                    if (i + 1 == ((Rational[])object2).length) continue;
                    ((StringBuilder)object).append(",");
                }
                return ((StringBuilder)object).toString();
            }
            if (object2 instanceof Rational[]) {
                object2 = object2;
                for (int i = 0; i < ((Rational[])object2).length; ++i) {
                    ((StringBuilder)object).append(object2[i].numerator);
                    ((StringBuilder)object).append('/');
                    ((StringBuilder)object).append(object2[i].denominator);
                    if (i + 1 == ((Rational[])object2).length) continue;
                    ((StringBuilder)object).append(",");
                }
                return ((StringBuilder)object).toString();
            }
            return null;
        }

        /*
         * Exception decompiling
         */
        Object getValue(ByteOrder var1_1) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 55[TRYBLOCK] [98 : 1182->1191)] java.lang.Throwable
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }

        public int size() {
            return IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(IFD_FORMAT_NAMES[this.format]);
            stringBuilder.append(", data length:");
            stringBuilder.append(this.bytes.length);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ExifStreamType {
    }

    static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        ExifTag(String string2, int n, int n2) {
            this.name = string2;
            this.number = n;
            this.primaryFormat = n2;
            this.secondaryFormat = -1;
        }

        ExifTag(String string2, int n, int n2, int n3) {
            this.name = string2;
            this.number = n;
            this.primaryFormat = n2;
            this.secondaryFormat = n3;
        }

        boolean isFormatCompatible(int n) {
            int n2 = this.primaryFormat;
            if (n2 != 7 && n != 7) {
                int n3;
                if (n2 != n && (n3 = this.secondaryFormat) != n) {
                    if ((n2 == 4 || n3 == 4) && n == 3) {
                        return true;
                    }
                    if ((n2 == 9 || n3 == 9) && n == 8) {
                        return true;
                    }
                    return (n2 == 12 || n3 == 12) && n == 11;
                }
                return true;
            }
            return true;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface IfdType {
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        Rational(double d) {
            this((long)(10000.0 * d), 10000L);
        }

        Rational(long l, long l2) {
            if (l2 == 0L) {
                this.numerator = 0L;
                this.denominator = 1L;
                return;
            }
            this.numerator = l;
            this.denominator = l2;
        }

        public double calculate() {
            double d = this.numerator;
            double d2 = this.denominator;
            Double.isNaN(d);
            Double.isNaN(d2);
            return d / d2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.numerator);
            stringBuilder.append("/");
            stringBuilder.append(this.denominator);
            return stringBuilder.toString();
        }
    }
}

