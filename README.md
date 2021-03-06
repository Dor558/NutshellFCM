# ![alt tag](https://i.imgur.com/8WhCFvw.jpg) ![alt tag](https://i.imgur.com/1dVlVDC.png)  ![alt tag](https://i.imgur.com/UNTyNoE.png) 

NutshellFirebase allows you to quickly integrate firebase notifications into your project, saving you a lot of boilerplate code that is usually required in order to have them run.
NutshellFirebase allows you to focus on what to do with your notification data instead of how and when to retrieve the payload. moreover, it suggests a solid solution to how an activity should process the notification and whether or not notification should be displayed to the user.

*Please note that NutshellFirebase intent to work with data notification only.

# Highlights
- Allow to have custom android notifications in a nutshell.
- Allow to use custom silent notifications.
- Allow to start foreground services via push.

# How-to
1) Add the depnedency to you project
### Gradle
```
compile 'com.dorbrauner.nutshellfirebase:framework:0.1'
```

2) Use the firebase wizard to integrate with firebase and generate the firebase services json file
-Tools-> Firebase -> Cloud messaging -> set up Cloud Messgeing ->  Connect to Firebase && Add FCM to your App
![alt tag](https://i.imgur.com/yMWh5zB.png)
![alt tag](https://i.imgur.com/ze0gq47.png)
![alt tag](https://i.imgur.com/pPBTYmQ.png)

3) Add the below lines to your application onCreate method
```    
      NutshellFirebaseEngine.start(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider(),
                            ExampleForegroundServicesBinder())
```

4) Add the below lines to your manifest
```
         <service android:name="com.dorbrauner.nutshellfirebase.NutshellFirebaseMessagingService">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>

```

5) Create your android notification factory
```
class ExampleNotificationFactory(private val application: Application) : NutshellFirebaseContract.AndroidNotificationsFactory {

    override fun create(notificationMessage: NotificationMessage): NutshellFirebaseContract.AndroidNotification {
        return when (notificationMessage.actionId) {
            "Some Action Id" -> {
                NutshellFirebaseContract.AndroidNotification(
                  [Fill your custom notification]
                )
            }
            
            else -> {
                throw Throwable("Unsupported notification id")
            }
        }
    }
}
```
6) Optional, Create your cases provider to handle app notifications use cases
```
class ExampleCaseProvider : NutshellFirebaseContract.NotificationsHandling.CasesProvider {

    override val cases: List<NutshellFirebaseContract.NotificationsHandling.Case> =
        listOf(Action1ExampleCase(), Action2Action3ExampleCase(), Action4ExampleCase())
}
```
7) Optional, Create your foreground services binder to start foreground services via firebase push.
```
class ExampleForegroundServicesBinder : NotificationsFrameworkContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? {
        return when (actionId) {
            "Some action id to start foreground service" -> ExampleForegroundService::class.java
            else -> null
        }
    }
}
```
8) Optional, You can extend from android notification builder to build your own notifications style.

## Usage

Send notification remotely:
```
{  
   "to": "[Firebase token]",
   "data":{  
      "action_id":"[Notification action id as it is defined in your android notifications factory]",
      "type": "[choose between = [notification|foreground|silent]]"
      ..//add your notification payload here...
   }
}
```

Send notification locally:
```
LocalMessagesNotifier.notify(
            NotificationMessage(
               [Notification action id as it is defined in your android notifications factory],
               [choose between = [NOTIFICATION|FOREGROUND_NOTIFICATION|SILENT_NOTIFICATION]
                  ..//add your notification payload here...
            )
        )
```

