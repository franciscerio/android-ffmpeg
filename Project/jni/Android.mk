LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)

LOCAL_MODULE  := videokit

FFMPEG_LIBS := $(addprefix ffmpeg/, \
 libavfilter/libavfilter.a \
 libavcodec/libavcodec.a \
  libavformat/libavformat.a \
 libswresample/libswresample.a \
 libswscale/libswscale.a \
 libavutil/libavutil.a \
 libpostproc/libpostproc.a )

LOCAL_CFLAGS += -g -Iffmpeg -Ivideokit -Wno-deprecated-declarations 
LOCAL_LDLIBS += -llog -lz -landroid $(FFMPEG_LIBS) x264/libx264.a 
LOCAL_SRC_FILES := videokit/com_b2bpo_media_VideoBrowser.c videokit/ffmpeg.c videokit/cmdutils.c

include $(BUILD_SHARED_LIBRARY)

# Use to safely invoke ffmpeg multiple times from the same Activity
include $(CLEAR_VARS)

LOCAL_MODULE := ffmpeg
FFMPEG_LIBS := $(addprefix ffmpeg/, \
 libavfilter/libavfilter.a \
 libavcodec/libavcodec.a \
  libavformat/libavformat.a \
 libswresample/libswresample.a \
 libswscale/libswscale.a \
 libavutil/libavutil.a \
 libpostproc/libpostproc.a )

LOCAL_CFLAGS += -g -Iffmpeg -Ivideokit -Wno-deprecated-declarations 
LOCAL_LDLIBS += -llog -lz -landroid $(FFMPEG_LIBS)  x264/libx264.a
LOCAL_SRC_FILES := ffmpeg/ffmpeg.c ffmpeg/cmdutils.c
include $(BUILD_EXECUTABLE)
