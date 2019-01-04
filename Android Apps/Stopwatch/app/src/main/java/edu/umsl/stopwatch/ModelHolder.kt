package edu.umsl.stopwatch

import android.util.Log
import java.lang.ref.WeakReference
import java.util.HashMap

class ModelHolder {

    companion object {
        val LAPS_MODEL = "LapsModel"
        val MAIN_MODEL = "MainModel"
        val instance = ModelHolder()
    }

    private val modelData = HashMap<String, WeakReference<Any?>>()

    fun saveModel(modelKey: String, model: Any?) {
        modelData[modelKey] = WeakReference(model)
    }

    fun getModel(modelKey: String): Any? {
        val weakObject = modelData[modelKey]
        Log.e("getModel", "weakObject?.get() is null ${weakObject?.get() == null}")
        return weakObject?.get()
    }
}