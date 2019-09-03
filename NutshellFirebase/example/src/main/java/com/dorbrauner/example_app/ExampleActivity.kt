package com.dorbrauner.example_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dorbrauner.nutshellfirebase.R
import com.dorbrauner.nutshellfirebase.LocalMessagesNotifier
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.*
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage


class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity_layout)
    }

    override fun onResume() {
        super.onResume()
        LocalMessagesNotifier.notifyDismiss("Action 4")
    }


    override fun onPause() {
        super.onPause()
        LocalMessagesNotifier.notify(
            NotificationMessage(
                "Action 4",
                NotificationType.FOREGROUND_NOTIFICATION
            ))
    }

}