LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := junit-hierarchicalcontextrunner

LOCAL_SRC_FILES := $(call all-java-files-under, src/main/java)

LOCAL_MODULE_TAGS := tests

LOCAL_JAVA_LANGUAGE_VERSION := 1.7

LOCAL_JAVA_LIBRARIES := \
	junit \

include $(BUILD_STATIC_JAVA_LIBRARY)
