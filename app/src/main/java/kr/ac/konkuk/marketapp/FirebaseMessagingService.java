package kr.ac.konkuk.marketapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

//Firebase FCM 푸시 알림 구현

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
    /**
     * 푸시 알림 정보를 받아서 메소드로 넘긴다
     *
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()); //서버에 이번주엔 어떠한 알림이 있습니다~ 라고 알려주게 되면 remoteMessage를 통해 전달받음 (정확히는 getBody()에)
    }

    // /** 누르고 enter

    /**
     * 푸시 알림 정보를 받아서 앱에서 알림을 띄워준다.
     *
     * @param body
     */
    //위에 설명한 스트링 객체를 sendNotification에서 넘김
    private void sendNotification(String title, String body)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String chId = "test";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //알림이 왔을 때, 사운드.

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, chId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body) // 알림 메세지를 넣음
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* 안드로이드 오레오 버전 대응*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String chName = "ch name";
            NotificationChannel channel = new NotificationChannel(chId, chName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0,notiBuilder.build()); // nofi를 최종적으로 날리는 구문

    }
}
