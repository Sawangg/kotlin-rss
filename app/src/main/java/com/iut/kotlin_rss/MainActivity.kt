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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.iut.kotlin_rss.adapter.ArticleAdapter
import com.iut.kotlin_rss.classes.Flux
import com.iut.kotlin_rss.handler.DatabaseHandler
import java.util.ArrayList
import kotlin.coroutines.startCoroutine

class MainActivity : AppCompatActivity() {

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
    *  - Connecter à une bd
    *  - Finir la listeview en créant l'adaptateur et finissant la classe flux
    *  - Continuer le fragment FilterMenu
    *  - Faire la zone catégorie dans la création de flux
    *  - Permettre l'ajout d'un flux dans le backend
    *  - Faire tout le design de l'edit de flux
    *  - Permettre l'edition de flux dans le backend
    *  - Faire le filtering dans le design et le backend
    *  - Checker si il y a au moins un flux et afficher le message @string/no_flux sinon
    *  - Faire l'envoi de notif maintenant que le handler est fait
    *  - En gros faire des trucs qui marche mnt qu'il y a un début de design
    * */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.lv_flux)
        createNotificationChannel()

        val buttonAdd: ImageView = findViewById(R.id.add_button);
        buttonAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddFlux::class.java);
            startActivity(intent);
        }

        // call sendNotification(titre, text) pour envoyer une notif
    }


    //method for read records from database in ListView
    fun displayArticle(view: View) {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val fluxs: List<Flux> = databaseHandler.viewFlux()
        val listTitle = ArrayList<String>();
        val listContent = ArrayList<String>();
        suspend {
            fluxs.forEach { flux ->
                flux.read {
                    flux.channel.articles.forEach { article ->
                        if (article.title != null)
                            listTitle.add(article.title!!)
                        else
                            listTitle.add("")

                        if (article.content != null)
                            listContent.add(article.content!!)
                        else
                            listContent.add("")
                    }
                    listContent.forEach {
                        print(it)
                    }
                }
            }
        }

    }
}