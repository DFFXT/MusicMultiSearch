//
// Created by qinfei on 2019/9/5.
//
#include <string>
#include <jni.h>
#include <zconf.h>
#include "CAddAdd.h"

char* jstringTostring(JNIEnv* env, jstring jstr)
{
    char* rtn = nullptr;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    auto barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0)
    {
        rtn = (char*)malloc(static_cast<size_t>(alen + 1));

        memcpy(rtn, ba, static_cast<size_t>(alen));
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}

const int aa=10;

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_simple_module_cNative_CNative_main(JNIEnv* env,jobject,jstring a){

    char* f=jstringTostring(env,a);
    FILE* file=fopen("/storage/emulated/0/0/cache/download/音乐api.txt","rwe");
    char * len=(char *)malloc(static_cast<size_t >(aa));
    fread(len, static_cast<size_t >(aa),static_cast<size_t>(aa-1),file);
    fclose(file);
    jsize s=aa;
    jbyteArray array=env->NewByteArray(s);
    env->SetByteArrayRegion(array, 0, 1, reinterpret_cast<const jbyte *>(len));

    return array;
}
class F{
public:int get(){
        return 1;
    }
};

/*extern "C" JNIEXPORT int JNICALL
Java_com_simple_module_cNative_CNative_main(JNIEnv *env){
    CAddAdd cAddAdd=CAddAdd();
    cAddAdd.getValue();
    delete(&cAddAdd);
    return 1;
}*/



