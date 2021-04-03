package com.iut.kotlin_rss.handler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast
import com.iut.kotlin_rss.classes.Flux
import kotlinx.coroutines.*
import kotlin.system.*

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "FluxDB"
        private val TABLE_CONTACTS = "Flux"
        private val FLUX_ID = "id"
        private val KEY_URL = "url"
        private val KEY_CATEGORY = "category"
        private val FLUX_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_CONTACTS ( $FLUX_ID INTEGER PRIMARY KEY AUTOINCREMENT,$FLUX_NAME TEXT, $KEY_URL TEXT, $KEY_CATEGORY TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }


    //method to insert data
    fun addFlux(flux: Flux): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_URL, flux.url)
        contentValues.put(KEY_CATEGORY, flux.category)
        contentValues.put(FLUX_NAME, flux.name)

        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection

        return success
    }

    //method to read data
    fun viewFlux(): List<Flux> {
        val listFlux: ArrayList<Flux> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        var fluxId: Int
        var fluxUrl: String
        var fluxCategory: String
        var fluxName: String
        if (cursor.moveToFirst()) {
            do {
                fluxId =  cursor.getInt( cursor.getColumnIndex(FLUX_ID))
                fluxName = cursor.getString(cursor.getColumnIndex(FLUX_NAME))
                fluxUrl = cursor.getString(cursor.getColumnIndex(KEY_URL))
                fluxCategory = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY))
                val flux = Flux(fluxName, fluxUrl, fluxCategory)
                flux.id = fluxId
                listFlux.add(flux)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listFlux
    }

    //method to update data
    fun updateFlux(flux: Flux): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_URL + "=" + flux.url, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to delete data
    fun deleteFlux(flux: Flux): Int {
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_URL + "=" + flux.url, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}