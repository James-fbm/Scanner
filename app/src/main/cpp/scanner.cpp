#include <jni.h>
#include <string>
#include "libexcelreader/excelreader.h"


extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_example_scanner_ui_viewmodel_ProjectViewModelKt_readExcelHeader(
        JNIEnv* env, jclass, jstring jFilePath, jstring jFileType) {

    const char *file_path = env->GetStringUTFChars(jFilePath, nullptr);
    const char *file_type = env->GetStringUTFChars(jFileType, nullptr);

    std::vector<std::string> vec_header;
    try {
        if (strcmp(file_type, "xls") == 0) {
            // TODO
        } else if (strcmp(file_type, "xlsx") == 0) {
            // TODO
        } else {
            vec_header = read_csvheader(file_path);
        }
    } catch(std::exception& e) {
        env->ReleaseStringUTFChars(jFilePath, file_path);
        env->ReleaseStringUTFChars(jFileType, file_type);
        return nullptr;
    }

    jobjectArray headerArray = env->NewObjectArray(static_cast<jsize>(vec_header.size()),
                                                   env->FindClass("java/lang/String"), nullptr);
    if (headerArray == nullptr) {
        env->ReleaseStringUTFChars(jFilePath, file_path);
        env->ReleaseStringUTFChars(jFileType, file_type);
        return nullptr;
    }

    for (size_t i = 0; i < vec_header.size(); ++i) {
        jstring headerString = env->NewStringUTF(vec_header[i].c_str());
        env->SetObjectArrayElement(headerArray, static_cast<jsize>(i), headerString);
        env->DeleteLocalRef(headerString);
    }

    env->ReleaseStringUTFChars(jFilePath, file_path);
    env->ReleaseStringUTFChars(jFileType, file_type);

    return headerArray;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_scanner_data_repo_CollectionRepositoryKt_readExcelRecord(
        JNIEnv* env, jclass, jstring jFilePath, jstring jFileType, jintArray indexIdArray) {
    const char *file_path = env->GetStringUTFChars(jFilePath, nullptr);
    const char *file_type = env->GetStringUTFChars(jFileType, nullptr);

    jsize indexIdLength = env->GetArrayLength(indexIdArray);
    std::vector<int> index_id(static_cast<unsigned int>(indexIdLength));

    if (indexIdLength > 0) {
        env->GetIntArrayRegion(indexIdArray, 0, indexIdLength, &index_id[0]);
    }

    IndexRecord index_record;
    try {
        if (strcmp(file_type, "xls") == 0) {
            // TODO
        } else if (strcmp(file_type, "xlsx") == 0) {
            // TODO
        } else {
            index_record = read_csvrecord(file_path, index_id);
        }
    } catch(std::exception& e) {
        env->ReleaseStringUTFChars(jFilePath, file_path);
        env->ReleaseStringUTFChars(jFileType, file_type);

        return nullptr;
    }

    jclass mapClass = env->FindClass("java/util/HashMap");
    if(mapClass == nullptr) {
        env->ReleaseStringUTFChars(jFilePath, file_path);
        env->ReleaseStringUTFChars(jFileType, file_type);
        return nullptr;
    }

    jmethodID mapConstructor = env->GetMethodID(mapClass, "<init>", "()V");
    jobject recordMap = env->NewObject(mapClass, mapConstructor);

    jmethodID mapPutMethod = env->GetMethodID(mapClass, "put",
                                              "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    for (const auto& entry : index_record) {
        jobjectArray keyArray = env->NewObjectArray(entry.first.size(), env->FindClass("java/lang/String"), NULL);
        for (size_t i = 0; i < entry.first.size(); ++i) {
            jstring stringKey = env->NewStringUTF(entry.first[i].c_str());
            env->SetObjectArrayElement(keyArray, i, stringKey);
            env->DeleteLocalRef(stringKey);
        }

        jobjectArray valueArray = env->NewObjectArray(entry.second.size(), env->FindClass("[Ljava/lang/String;"), NULL);
        for (size_t i = 0; i < entry.second.size(); ++i) {
            jobjectArray innerArray = env->NewObjectArray(entry.second[i].size(), env->FindClass("java/lang/String"), NULL);
            for (size_t j = 0; j < entry.second[i].size(); ++j) {
                jstring stringValue = env->NewStringUTF(entry.second[i][j].c_str());
                env->SetObjectArrayElement(innerArray, j, stringValue);
                env->DeleteLocalRef(stringValue);
            }
            env->SetObjectArrayElement(valueArray, i, innerArray);
            env->DeleteLocalRef(innerArray);
        }

        jobject previousValue = env->CallObjectMethod(recordMap, mapPutMethod, keyArray, valueArray);
        env->DeleteLocalRef(previousValue);
        env->DeleteLocalRef(keyArray);
        env->DeleteLocalRef(valueArray);
    }

    env->ReleaseStringUTFChars(jFilePath, file_path);
    env->ReleaseStringUTFChars(jFileType, file_type);
    return recordMap;
}