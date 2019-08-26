# firebase-notifications

Notifications framework allow you to quickly integrate firebase notifications into your project, saving you alot of boilplate code 
required in order to have them run.

Notifications framework allow you to focus on what to do with notification data instead of how to get the notification data.
moreover, its suggest a solid solution to how an activity should process the notification and whether or not notification 
should be displayed to the user.

## Gradle
```
compile 'com.dorbrauner.notifications.framework:notifications-framework:1.4'
```

## Usage
1) Make your activity implement NotificationContract.NotificationsListener 
2) Receive the notifications data via onNotificationReceived
3) Consume the notification by calling consume
![alt tag](https://i.imgur.com/hctuZib.png)

4) Bonus, you can build foreground notification via androidNotificaitonBuilder
   calling buildForeground.
![alt tag](https://i.imgur.com/5wrma4N.png)

# How-to
1) Add the depnedency to you project
2) Use the firebase wizard to integrate with firebase and generate the firebase services json file
-Tools-> Firebase -> Cloud messaging -> set up Cloud Messgeing ->  Connect to Firebase && Add FCM to your App
![alt tag](https://i.imgur.com/yMWh5zB.png)
![alt tag](https://i.imgur.com/ze0gq47.png)
![alt tag](https://i.imgur.com/pPBTYmQ.png)


3) Add the below lines to your manifest
```
<service android:name="com.dorbrauner.notifications.framework.FirebaseService"/>
<service android:name="com.dorbrauner.notifications.framework.NotificationBuilderService"/>
<service android:name="com.dorbrauner.notifications.framework.MessageToNotificationService"/>
<receiver android:name="com.dorbrauner.notifications.framework.NotificationApplicationReceiver"/>
```
4) Create your android notification creator
![alt tag](https://i.imgur.com/hdkOMsP.png)
5) Call NotificationFramework.Build your application class and init it like the follow
![alt tag](https://i.imgur.com/dou35ux.png)
6) Optional, You can extend from android notification builder to build your own notifications style if required
