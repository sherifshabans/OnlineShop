package com.elsharif.onlineshop.Helper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import com.elsharif.onlineshop.model.ItemModel
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays

class TinyDB2(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var DEFAULT_APP_IMAGEDATA_DIRECTORY: String? = null
    private var lastImagePath = ""

    companion object {
        /**
         * Check if external storage is writable or not
         *
         * @return true if writable, false otherwise
         */
        fun isExternalStorageWritable(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        }

        /**
         * Check if external storage is readable or not
         *
         * @return true if readable, false otherwise
         */
        fun isExternalStorageReadable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }
    }

    /**
     * Decodes the Bitmap from 'path' and returns it
     *
     * @param path image path
     * @return the Bitmap from 'path'
     */
    fun getImage(path: String?): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Returns the String path of the last saved image
     *
     * @return string path of the last saved image
     */
    fun getSavedImagePath(): String {
        return lastImagePath
    }

    /**
     * Saves 'theBitmap' into folder 'theFolder' with the name 'theImageName'
     *
     * @param theFolder    the folder path dir you want to save it to e.g "DropBox/WorkImages"
     * @param theImageName the name you want to assign to the image file e.g "MeAtLunch.png"
     * @param theBitmap    the image you want to save as a Bitmap
     * @return returns the full path(file system address) of the saved image
     */
    fun putImage(theFolder: String?, theImageName: String?, theBitmap: Bitmap?): String? {
        if (theFolder == null || theImageName == null || theBitmap == null) return null
        this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder
        val mFullPath = setupFullPath(theImageName)
        if (mFullPath.isNotEmpty()) {
            lastImagePath = mFullPath
            saveBitmap(mFullPath, theBitmap)
        }
        return mFullPath
    }

    /**
     * Saves 'theBitmap' into 'fullPath'
     *
     * @param fullPath  full path of the image file e.g. "Images/MeAtLunch.png"
     * @param theBitmap the image you want to save as a Bitmap
     * @return true if image was saved, false otherwise
     */
    fun putImageWithFullPath(fullPath: String?, theBitmap: Bitmap?): Boolean {
        return !(fullPath == null || theBitmap == null) && saveBitmap(fullPath, theBitmap)
    }

    /**
     * Creates the path for the image with name 'imageName' in DEFAULT_APP.. directory
     *
     * @param imageName name of the image
     * @return the full path of the image. If it failed to create directory, return empty string
     */
    private fun setupFullPath(imageName: String): String {
        val mFolder = File(Environment.getExternalStorageDirectory(), DEFAULT_APP_IMAGEDATA_DIRECTORY)
        if (isExternalStorageReadable() && isExternalStorageWritable() && !mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("ERROR", "Failed to setup folder")
                return ""
            }
        }
        return mFolder.path + '/' + imageName
    }

    /**
     * Saves the Bitmap as a PNG file at path 'fullPath'
     *
     * @param fullPath path of the image file
     * @param bitmap   the image as a Bitmap
     * @return true if it successfully saved, false otherwise
     */
    private fun saveBitmap(fullPath: String?, bitmap: Bitmap?): Boolean {
        if (fullPath == null || bitmap == null) return false

        var fileCreated = false
        var bitmapCompressed = false
        var streamClosed = false

        val imageFile = File(fullPath)
        if (imageFile.exists() && !imageFile.delete()) return false

        try {
            fileCreated = imageFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(imageFile)
            bitmapCompressed = bitmap.compress(CompressFormat.PNG, 100, out)
        } catch (e: Exception) {
            e.printStackTrace()
            bitmapCompressed = false
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                    streamClosed = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    streamClosed = false
                }
            }
        }
        return fileCreated && bitmapCompressed && streamClosed
    }

    /**
     * Get int value from SharedPreferences at 'key'. If key not found, return 0
     *
     * @param key SharedPreferences key
     * @return int value at 'key' or 0 if key not found
     */
    fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Integers
     */
    fun getListInt(key: String): ArrayList<Int> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Int>()
        for (item in arrayToList) newList.add(Integer.parseInt(item))
        return newList
    }

    /**
     * Get long value from SharedPreferences at 'key'. If key not found, return 0
     *
     * @param key SharedPreferences key
     * @return long value at 'key' or 0 if key not found
     */
    fun getLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    /**
     * Get float value from SharedPreferences at 'key'. If key not found, return 0
     *
     * @param key SharedPreferences key
     * @return float value at 'key' or 0 if key not found
     */
    fun getFloat(key: String): Float {
        return preferences.getFloat(key, 0f)
    }

    /**
     * Get double value from SharedPreferences at 'key'. If exception thrown, return 0
     *
     * @param key SharedPreferences key
     * @return double value at 'key' or 0 if exception is thrown
     */
    fun getDouble(key: String): Double {
        val number = getString(key)
        return try {
            number.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    /**
     * Get parsed ArrayList of Double from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Double
     */
    fun getListDouble(key: String): ArrayList<Double> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Double>()
        for (item in arrayToList) newList.add(item.toDouble())
        return newList
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of Longs
     */
    fun getListLong(key: String): ArrayList<Long> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Long>()
        for (item in arrayToList) newList.add(item.toLong())
        return newList
    }

    /**
     * Get String value from SharedPreferences at 'key'. If key not found, return ""
     *
     * @param key SharedPreferences key
     * @return String value at 'key' or "" (empty String) if key not found
     */
    fun getString(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    fun getListString(key: String): ArrayList<String> {
        return ArrayList(Arrays.asList(*TextUtils.split(preferences.getString(key, ""), "‚‗‚")))
    }

    /**
     * Put int value into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param value int value to be added
     */
    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    /**
     * Put ArrayList of Integer into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param intList ArrayList of Integer to be added
     */
    fun putListInt(key: String, intList: ArrayList<Int>) {
        val myIntList = arrayOfNulls<String>(intList.size)
        for (i in intList.indices) myIntList[i] = intList[i].toString()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply()
    }

    /**
     * Put long value into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param value long value to be added
     */
    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    /**
     * Put ArrayList of Long into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param longList ArrayList of Long to be added
     */
    fun putListLong(key: String, longList: ArrayList<Long>) {
        val myLongList = arrayOfNulls<String>(longList.size)
        for (i in longList.indices) myLongList[i] = longList[i].toString()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply()
    }

    /**
     * Put float value into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param value float value to be added
     */
    fun putFloat(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    /**
     * Put ArrayList of Float into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param floatList ArrayList of Float to be added
     */
    fun putListFloat(key: String, floatList: ArrayList<Float>) {
        val myFloatList = arrayOfNulls<String>(floatList.size)
        for (i in floatList.indices) myFloatList[i] = floatList[i].toString()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myFloatList)).apply()
    }

    /**
     * Put double value into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param value double value to be added
     */
    fun putDouble(key: String, value: Double) {
        putString(key, value.toString())
    }

    /**
     * Put ArrayList of Double into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param doubleList ArrayList of Double to be added
     */
    fun putListDouble(key: String, doubleList: ArrayList<Double>) {
        val myDoubleList = arrayOfNulls<String>(doubleList.size)
        for (i in doubleList.indices) myDoubleList[i] = doubleList[i].toString()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply()
    }

    /**
     * Put String value into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param value String value to be added
     */
    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key'
     *
     * @param key   SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    fun putListString(key: String, stringList: ArrayList<String>) {
        val myStringList = arrayOfNulls<String>(stringList.size)
        for (i in stringList.indices) myStringList[i] = stringList[i]
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    /**
     * Remove SharedPreferences item with 'key'
     *
     * @param key SharedPreferences key
     */
    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    /**
     * Clear SharedPreferences (remove all)
     */
    fun clear() {
        preferences.edit().clear().apply()
    }

    /**
     * Retrieve all SharedPreferences key-value pairs
     *
     * @return a Map containing all key-value pairs in SharedPreferences
     */
    fun getAll(): Map<String, *> {
        return preferences.all
    }

    /**
     * Check if SharedPreferences contains 'key'
     *
     * @param key SharedPreferences key
     * @return true if 'key' exists, false otherwise
     */
    operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    /**
     * Store Object in SharedPreferences at 'key'
     *
     * @param key   SharedPreferences key
     * @param obj Object to be stored
     */
    fun <T> putObject(key: String, obj: T) {
        val gson = Gson()
        putString(key, gson.toJson(obj))
    }

    /**
     * Retrieve Object from SharedPreferences at 'key'
     *
     * @param key   SharedPreferences key
     * @param classOfT Class type of object
     * @return Object of type T stored in SharedPreferences at 'key'
     */
    fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val gson = Gson()
        return gson.fromJson(getString(key), classOfT)
    }

    /**
     * Store ArrayList of Object in SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @param objList ArrayList of Object to be stored
     */
    fun <T> putListObject(key: String, objList: ArrayList<T>) {
        val gson = Gson()
        putString(key, gson.toJson(objList))
    }

    /**
     * Retrieve ArrayList of Object from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @param classOfT Class type of object
     * @return ArrayList of Object of type T stored in SharedPreferences at 'key'
     */
    fun getListObject(key: String): ArrayList<ItemModel> {
        val gson = Gson()
        val objStrings = getListString(key)
        val itemList = ArrayList<ItemModel>()

        for (jObjString in objStrings) {
            val item = gson.fromJson(jObjString, ItemModel::class.java)
            itemList.add(item)
        }
        return itemList
    }
}
