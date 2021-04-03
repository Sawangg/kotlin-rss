package com.iut.kotlin_rss

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.iut.kotlin_rss.adapter.ListAdapter
import com.iut.kotlin_rss.classes.Flux
import com.iut.kotlin_rss.handler.DatabaseHandler
import kotlinx.coroutines.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private val channelID = "channel_kotlin_rss";
    private val notificationId = 101;
    lateinit var listView: ListView;


    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(titre: String, text: String) {
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titre)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    /*
    * TODO:
    *  - Faire le filtering dans le design et le backend
    *  - Faire l'envoi de notif maintenant que le handler est fait
    *  - Favori
    *  - Date
    * */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        listView = findViewById(R.id.lv_flux)
        createNotificationChannel()

        val buttonAdd: ImageView = findViewById(R.id.add_button);
        buttonAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddFlux::class.java);
            startActivity(intent);
            finish()
        }

        val buttonEdit: TextView = findViewById(R.id.nav_flux_edit)
        buttonEdit.setOnClickListener {
            val intent = Intent(this@MainActivity, EditActivity::class.java);
            startActivity(intent);
            finish()
        }
        // Fragment pour l'ouverture de filter
        val filterButton: TextView = findViewById(R.id.category_title);
        filterButton.setOnClickListener {
            val dialog = FilterMenu();
            dialog.show(supportFragmentManager, "TAG")
        }

        if (intent.getParcelableArrayListExtra<Flux>("Flux") != null) {
            val fluxs = intent.getParcelableArrayListExtra<Flux>("Flux")!!
            val arrTitle = ArrayList<String>()
            val arrDesc = ArrayList<String>()
            fluxs.forEach {
                it.formatAllArticles(arrTitle, arrDesc)
            }
            listView.adapter = ListAdapter(this, arrTitle, arrDesc)

            val tv: TextView = findViewById(R.id.no_flux)
            tv.visibility = View.INVISIBLE
        } else {
            this.displayArticle()
        }


    }


    //method for read records from database in ListView

    fun displayArticle() {
        val databaseHandler = DatabaseHandler(this)
        val fluxs: List<Flux> = databaseHandler.viewFlux()
        if (fluxs.isEmpty()) return

        GlobalScope.launch(Dispatchers.Default) {
            fluxs.forEach { flux ->
                flux.read()
            }
        }.invokeOnCompletion {
            val intent = Intent(this@MainActivity, MainActivity::class.java);
            intent.putParcelableArrayListExtra("Flux", ArrayList(fluxs))
            startActivity(intent);
            finish()
        }
    }

    override fun onBackPressed() {
        return
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        sendNotification("Kotlin-RSS", "See you next time !")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
//        sendNotification("Kotlin-RSS", "See you next time !")
    }



}