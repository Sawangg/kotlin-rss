package com.iut.kotlin_rss.classes

import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import java.lang.Exception
import java.nio.charset.Charset

class Flux(var name : String, var url : String, var category : String) {
    lateinit var channel: Channel;
    var id = 0


    suspend fun read(function: (Channel) -> Unit) {
        val parser = Parser.Builder()
            .charset(Charset.forName("UTF-8"))
            .build();
        try {
            parser.getChannel(url).run {
                channel = this
                function(channel)

            }
        }catch (e : Exception){
            e.printStackTrace();
        }
    }
}