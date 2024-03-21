#include <jni.h>
#include <thread>
#include <syslog.h>
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


void thread_copy_record(JavaVM* jvm, jobject recordMap, jmethodID mapPutMethod,
                        IndexRecord::const_iterator start, IndexRecord::const_iterator end) {

    JNIEnv* env = nullptr;
    jvm->AttachCurrentThread(&env, nullptr);


    for (auto entry = start ; entry != end ; ++entry) {
        jobjectArray keyArray = env->NewObjectArray(entry->first.size(), env->FindClass("java/lang/String"), nullptr);
        for (size_t i = 0; i < entry->first.size(); ++i) {
            jstring stringKey = env->NewStringUTF(entry->first[i].c_str());
            env->SetObjectArrayElement(keyArray, i, stringKey);
            env->DeleteLocalRef(stringKey);
        }

        jobjectArray valueArray = env->NewObjectArray(entry->second.size(), env->FindClass("java/lang/String"), nullptr);
        for (size_t i = 0; i < entry->second.size(); ++i) {
            jstring stringValue = env->NewStringUTF(entry->second[i].c_str());
            env->SetObjectArrayElement(valueArray, i, stringValue);
            env->DeleteLocalRef(stringValue);
        }

        env->CallObjectMethod(recordMap, mapPutMethod, keyArray, valueArray);
        env->DeleteLocalRef(keyArray);
        env->DeleteLocalRef(valueArray);
    }

    jvm->DetachCurrentThread();
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

    jclass mapClass = env->FindClass("java/util/concurrent/ConcurrentHashMap");
    if(mapClass == nullptr) {
        env->ReleaseStringUTFChars(jFilePath, file_path);
        env->ReleaseStringUTFChars(jFileType, file_type);
        return nullptr;
    }
    jmethodID mapConstructor = env->GetMethodID(mapClass, "<init>", "()V");
    jobject recordMap = env->NewObject(mapClass, mapConstructor);
    // global reference shared between threads
    jobject globalRecordMap = env->NewGlobalRef(recordMap);
    jmethodID mapPutMethod = env->GetMethodID(mapClass, "put",
                                              "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    int record_size = index_record.size();
    int max_thread_count = std::thread::hardware_concurrency();
    // regulate thread count according to data size
    int thread_count = std::min((int)(record_size / 100) + 1, max_thread_count);
    int batch_size = index_record.size() / thread_count;
    std::vector<std::thread> threads;

    JavaVM* jvm = nullptr;
    env->GetJavaVM(&jvm);

    auto it = index_record.cbegin();
    for (int i = 0 ; i < thread_count ; ++i) {
        auto it_start = it;
        std::advance(it, batch_size);
        auto it_end = (i == thread_count - 1) ? index_record.cend() : it;

        threads.emplace_back(thread_copy_record, jvm, globalRecordMap, mapPutMethod, it_start, it_end);
    }

    for (auto& t: threads) {
        t.join();
    }

    env->ReleaseStringUTFChars(jFilePath, file_path);
    env->ReleaseStringUTFChars(jFileType, file_type);

    env->DeleteGlobalRef(globalRecordMap);
    return recordMap;
}