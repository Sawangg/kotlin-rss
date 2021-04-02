package com.iut.kotlin_rss

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private val channelID = "channel_kotlin_rss";
    private val notificationId = 101;

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

        createNotificationChannel()

        val buttonAdd : ImageView = findViewById(R.id.add_button);
        buttonAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddFlux::class.java);
            startActivity(intent);
        }

        // call sendNotification(titre, text) pour envoyer une notif
    }
}