package com.iut.kotlin_rss.classes

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import java.io.Serializable
import java.lang.Exception
import java.nio.charset.Charset

class Flux(var name : String?, var url : String?, var category : String?) : Parcelable{
    var id = 0
    var channel : Channel = Channel()
    var fav : Boolean = false

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        id = parcel.readInt()
        channel = parcel.readSerializable() as Channel
        fav = parcel.readBoolean()
    }


    suspend fun read() {
        val parser = Parser.Builder()
            .charset(Charset.forName("UTF-8"))
            .build();
        try {
            parser.getChannel(url!!).run {
                channel = this
            }
        }catch (e : Exception){
            e.printStackTrace();
        }
    }

    fun formatAllArticles(listTitle : ArrayList<String>, listDescription : ArrayList<String>, listLink : ArrayList<String>, listDate : ArrayList<String>  ){
        this.channel.articles.forEach {
            if (it.title != null && it.description != null && it.link != null){
                listTitle.add(it.title!!)
                listDescription.add(it.description!!)
                listLink.add(it.link!!)
                if(it.pubDate != null){
                    listDate.add(it.pubDate!!)
//                    Log.e("t",it.pubDate.);
                }else {
                    listDate.add("")
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeInt(id)
        parcel.writeSerializable(channel)
        parcel.writeBoolean(fav)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flux> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Flux {
            return Flux(parcel)
        }

        override fun newArray(size: Int): Array<Flux?> {
            return arrayOfNulls(size)
        }
    }
}