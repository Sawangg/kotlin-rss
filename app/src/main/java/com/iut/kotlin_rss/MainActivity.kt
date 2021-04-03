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

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private val channelID = "channel_kotlin_rss";
    private val notificationId = 101;
    lateinit var listView: ListView;
    var fluxs: ArrayList<Flux> = ArrayList()


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
            val dialog = FilterMenu({ displayArticles("", false) }) {
                if (it.category_wrapper_group.selectedButtons.size > 0) {
                    val btn = it.category_wrapper_group.selectedButtons[0]
                    val fav = it.filter_dof_switch.isChecked
                    displayArticles(btn.text, fav)
                }
            }
            dialog.show(supportFragmentManager, "TAG")
        }

        if (intent.getParcelableArrayListExtra<Flux>("Flux") != null) {
            fluxs = intent.getParcelableArrayListExtra("Flux")!!
            displayArticles("", false)
            val tv: TextView = findViewById(R.id.no_flux)
            tv.visibility = View.INVISIBLE
        } else {
            this.loadArticles()
        }
    }

    private fun loadArticles() {
        val databaseHandler = DatabaseHandler(this)
        val fluxs: List<Flux> = databaseHandler.viewFlux()
        if (fluxs.isEmpty()) return;

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

    private fun displayArticles(category: String, fav: Boolean) {
        val arrTitle = ArrayList<String>()
        val arrDesc = ArrayList<String>()
        val arrLink = ArrayList<String>()
        val arrDate = ArrayList<String>()
        if (category == "") {
            fluxs.forEach {
                if (fav) {
                    if (it.fav) {
                        it.formatAllArticles(arrTitle, arrDesc, arrLink, arrDate)
                    }
                } else {
                    it.formatAllArticles(arrTitle, arrDesc, arrLink, arrDate)
                }

            }
        } else {
            fluxs.forEach {
                if (it.category == category) {
                    if (fav) {
                        if (it.fav) {
                            it.formatAllArticles(arrTitle, arrDesc, arrLink, arrDate)
                        }
                    } else {
                        it.formatAllArticles(arrTitle, arrDesc, arrLink, arrDate)
                    }
                }
            }
        }
        if (arrTitle.isEmpty()) {
            val tv: TextView = findViewById(R.id.no_result)
            tv.visibility = View.VISIBLE
        } else {
            val tv: TextView = findViewById(R.id.no_result)
            tv.visibility = View.INVISIBLE
        }
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