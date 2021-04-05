package com.iut.kotlin_rss

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.iut.kotlin_rss.adapter.ArticleAdapter
import com.iut.kotlin_rss.classes.Flux
import com.iut.kotlin_rss.handler.DatabaseHandler
import kotlinx.android.synthetic.main.category.*
import kotlinx.android.synthetic.main.filtermenu.*
import kotlinx.coroutines.*
import java.util.ArrayList
import android.app.UiModeManager;
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private val channelID = "channel_kotlin_rss";
    private val notificationId = 101;
    lateinit var listView: ListView;
    var fluxs: ArrayList<Flux> = ArrayList()
    var category = ""
    var favori = false


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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        listView = findViewById(R.id.lv_flux)
        createNotificationChannel()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
            val dialog = FilterMenu(category, favori)
            dialog.show(supportFragmentManager, "TAG")
            dialog.setOnDismiss {
                favori = it.filter_dof_switch.isChecked
                category = ""
                if (it.category_wrapper_group.selectedButtons.size > 0) {
                    val btn = it.category_wrapper_group.selectedButtons[0]
                    category = btn.text
                }
                displayArticles()
            }
        }
        this.loadArticles()
    }

    private fun loadArticles() {
        val databaseHandler = DatabaseHandler(this)
        val fluxs1: List<Flux> = databaseHandler.viewFlux()
        if (fluxs1.isEmpty()) return;
        val tv: TextView = findViewById(R.id.no_flux)
        tv.visibility = View.INVISIBLE
        GlobalScope.launch(Dispatchers.Default) {
            fluxs1.forEach { flux ->
                flux.read()
            }
            withContext(Dispatchers.Main) {
                fluxs = fluxs1 as ArrayList<Flux>
                category = ""
                favori = false
                displayArticles()
            }
        }
    }

    private fun displayArticles() {
        val arrTitle = ArrayList<String>()
        val arrDesc = ArrayList<String>()
        val arrLink = ArrayList<String>()
        val arrDate = ArrayList<String>()
        var fluxs1 = fluxs

        if (category != "")
            fluxs1 = fluxs1.filter { it.category == category } as ArrayList<Flux>

        fluxs1.forEach {
            if (favori) {
                if (it.fav) {
                    it.formatAllArticles(arrTitle, arrDesc, arrLink, arrDate)
                }
            } else {
                it.formatAllArticles(arrTitle, arrDesc, arrLink, arrDate)
            }
        }

        val tv: TextView = findViewById(R.id.no_result)

        if (arrTitle.isEmpty())
            tv.visibility = View.VISIBLE
        else
            tv.visibility = View.INVISIBLE

        listView.adapter = ArticleAdapter(this, arrTitle, arrDesc, arrLink, arrDate)
    }

    override fun onBackPressed() {
        return;
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