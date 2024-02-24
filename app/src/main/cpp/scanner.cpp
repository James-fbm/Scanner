#include <jni.h>
#include <string>
#include "libexcelreader/excelreader.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_scanner_MainActivity_stringFromJNI(JNIEnv * env, jobject) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_example_scanner_ui_viewmodel_ProjectViewModelKt_readExcelHeader(
        JNIEnv* env, jclass, jstring jFilePath, jstring jFileType) {

    const char *file_path = env->GetStringUTFChars(jFilePath, nullptr);
    const char *file_type = env->GetStringUTFChars(jFileType, nullptr);

    std::vector<std::string> vec_header;
    if (strcmp(file_type, "xls") == 0) {
        // TODO
    } else if (strcmp(file_type, "xlsx") == 0) {
        // TODO
    } else {
        vec_header = read_csvheader(file_path);
    }

    jobjectArray headerArray = env->NewObjectArray(vec_header.size(),
                                                   env->FindClass("java/lang/String"), nullptr);

    for (size_t i = 0; i < vec_header.size(); ++i) {
        env->SetObjectArrayElement(headerArray, i, env->NewStringUTF(vec_header[i].c_str()));
    }

    env->ReleaseStringUTFChars(jFilePath, file_path);
    env->ReleaseStringUTFChars(jFileType, file_type);

    return headerArray;
}