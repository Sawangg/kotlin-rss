package com.iut.kotlin_rss.classes

import android.os.Parcel
import android.os.Parcelable
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import java.io.Serializable
import java.lang.Exception
import java.nio.charset.Charset

class Flux(var name : String?, var url : String?, var category : String?) : Parcelable{
    var id = 0
    var channel : Channel = Channel();

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        id = parcel.readInt()
        channel = parcel.readSerializable() as Channel
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

    fun formatAllArticles(listTitle : ArrayList<String>, listDescription : ArrayList<String> ){
        this.channel.articles.forEach {
            if (it.title != null && it.description != null){
                listTitle.add(it.title!!)
                listDescription.add(it.description!!)
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeInt(id)
        parcel.writeSerializable(channel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flux> {
        override fun createFromParcel(parcel: Parcel): Flux {
            return Flux(parcel)
        }

        override fun newArray(size: Int): Array<Flux?> {
            return arrayOfNulls(size)
        }
    }
}