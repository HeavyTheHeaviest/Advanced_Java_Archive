#include <jni.h>
#include "Sorter.h"
#include <stdlib.h>
#include <string.h>

/* Porównywarki do qsort */
static int cmp_asc(const void *p, const void *q) {
    double a = *(const double*)p, b = *(const double*)q;
    return (a < b ? -1 : a > b ? 1 : 0);
}
static int cmp_desc(const void *p, const void *q) {
    double a = *(const double*)p, b = *(const double*)q;
    return (a > b ? -1 : a < b ? 1 : 0);
}

/*
 * Class:     Sorter
 * Method:    sort01
 * Signature: ([Ljava/lang/Double;Ljava/lang/Boolean;)[Ljava/lang/Double;
 */
JNIEXPORT jobjectArray JNICALL Java_Sorter_sort01
  (JNIEnv *env, jobject obj, jobjectArray arr, jobject orderObj)
{
    // 1) Zamień java.lang.Boolean → jboolean
    jclass clsBool   = (*env)->GetObjectClass(env, orderObj);
    jmethodID mid    = (*env)->GetMethodID(env, clsBool, "booleanValue", "()Z");
    jboolean order   = (*env)->CallBooleanMethod(env, orderObj, mid);

    // 2) Pobierz długość tablicy
    jsize n = (*env)->GetArrayLength(env, arr);

    // 3) Skopiuj elementy do bufora C
    double *buf = malloc(n * sizeof(double));
    jclass clsD       = (*env)->FindClass(env, "java/lang/Double");
    jmethodID midVal  = (*env)->GetMethodID(env, clsD, "doubleValue", "()D");
    for (jsize i = 0; i < n; i++) {
        jobject d = (*env)->GetObjectArrayElement(env, arr, i);
        buf[i] = (*env)->CallDoubleMethod(env, d, midVal);
        (*env)->DeleteLocalRef(env, d);
    }

    // 4) Sortuj bufor
    qsort(buf, n, sizeof(double), order ? cmp_asc : cmp_desc);

    // 5) Utwórz nową tablicę Double[] i wypełnij wynikami
    jobjectArray result = (*env)->NewObjectArray(env, n, clsD, NULL);
    jmethodID ctor      = (*env)->GetMethodID(env, clsD, "<init>", "(D)V");
    for (jsize i = 0; i < n; i++) {
        jobject d = (*env)->NewObject(env, clsD, ctor, buf[i]);
        (*env)->SetObjectArrayElement(env, result, i, d);
        (*env)->DeleteLocalRef(env, d);
    }

    free(buf);
    return result;
}

/*
 * Class:     Sorter
 * Method:    sort02
 * Signature: ([Ljava/lang/Double;)[Ljava/lang/Double;
 */
JNIEXPORT jobjectArray JNICALL Java_Sorter_sort02
  (JNIEnv *env, jobject obj, jobjectArray arr)
{
    // odczytaj this.order (java.lang.Boolean)
    jclass clsThis      = (*env)->GetObjectClass(env, obj);
    jfieldID fidOrder   = (*env)->GetFieldID(env, clsThis, "order", "Ljava/lang/Boolean;");
    jobject orderObj    = (*env)->GetObjectField(env, obj, fidOrder);

    // wywołaj sort01 z odczytaną wartością
    return Java_Sorter_sort01(env, obj, arr, orderObj);
}

/*
 * Class:     Sorter
 * Method:    sort03
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_Sorter_sort03
  (JNIEnv *env, jobject obj)
{
    // 1) Wczytaj ciąg znaków z JOptionPane
    jclass pane = (*env)->FindClass(env, "javax/swing/JOptionPane");
    jmethodID inDlg = (*env)->GetStaticMethodID(env, pane,
        "showInputDialog", "(Ljava/lang/Object;)Ljava/lang/String;");
    jstring prompt = (*env)->NewStringUTF(env,
        "Enter numbers comma-separated:");
    jstring input = (jstring)(*env)->CallStaticObjectMethod(env,
        pane, inDlg, prompt);

    const char *cstr = (*env)->GetStringUTFChars(env, input, NULL);

    // 2) Podziel po przecinkach
    int count = 1;
    for (const char *p = cstr; *p; ++p) {
        if (*p == ',') count++;
    }
    double *vals = malloc(count * sizeof(double));
    char *copy = strdup(cstr);
    char *tok = strtok(copy, ",");
    int idx = 0;
    while (tok && idx < count) {
        vals[idx++] = atof(tok);
        tok = strtok(NULL, ",");
    }
    (*env)->ReleaseStringUTFChars(env, input, cstr);
    free(copy);

    // 3) Zapytaj o kolejność sortowania
    jmethodID confirm = (*env)->GetStaticMethodID(env, pane,
        "showConfirmDialog", "(Ljava/awt/Component;Ljava/lang/Object;)I");
    jstring orderMsg = (*env)->NewStringUTF(env,
        "OK = ascending, Cancel = descending");
    jint choice = (*env)->CallStaticIntMethod(env,
        pane, confirm, NULL, orderMsg);
    jboolean order = (choice == 0) ? JNI_TRUE : JNI_FALSE;

    // 4) Zbuduj tablicę Double[]
    jclass clsD = (*env)->FindClass(env, "java/lang/Double");
    jobjectArray darr = (*env)->NewObjectArray(env, idx, clsD, NULL);
    jmethodID ctorD = (*env)->GetMethodID(env, clsD, "<init>", "(D)V");
    for (int i = 0; i < idx; i++) {
        jobject d = (*env)->NewObject(env, clsD, ctorD, vals[i]);
        (*env)->SetObjectArrayElement(env, darr, i, d);
        (*env)->DeleteLocalRef(env, d);
    }
    free(vals);

    // 5) Przypisz this.a = darr
    jclass clsThis = (*env)->GetObjectClass(env, obj);
    jfieldID fidA = (*env)->GetFieldID(env, clsThis,
        "a", "[Ljava/lang/Double;");
    (*env)->SetObjectField(env, obj, fidA, darr);

    // 6) Przypisz this.order = new Boolean(order)
    jclass clsBool = (*env)->FindClass(env, "java/lang/Boolean");
    jmethodID ctorB = (*env)->GetMethodID(env, clsBool,
        "<init>", "(Z)V");
    jobject boolObj = (*env)->NewObject(env, clsBool, ctorB, order);
    jfieldID fidO = (*env)->GetFieldID(env, clsThis,
        "order", "Ljava/lang/Boolean;");
    (*env)->SetObjectField(env, obj, fidO, boolObj);
}
