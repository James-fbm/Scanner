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

    const char *filePath = env->GetStringUTFChars(jFilePath, nullptr);
    std::string file_path(filePath);
    env->ReleaseStringUTFChars(jFilePath, filePath);

    std::vector<std::string> vec_header = read_csvheader(file_path.c_str());

    jobjectArray retArray = env->NewObjectArray(vec_header.size(), env->FindClass("java/lang/String"), nullptr);

    for (size_t i = 0; i < vec_header.size(); ++i) {
        env->SetObjectArrayElement(retArray, i, env->NewStringUTF(vec_header[i].c_str()));
    }
    return retArray;
}