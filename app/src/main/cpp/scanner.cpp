#include <jni.h>
#include <string>
#include "libexcelreader/excelreader.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_scanner_MainActivity_stringFromJNI(JNIEnv * env, jobject) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}