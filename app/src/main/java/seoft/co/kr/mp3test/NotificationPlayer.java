package seoft.co.kr.mp3test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;


public class NotificationPlayer {
    private final static int NOTIFICATION_PLAYER_ID = 0x342;
    private AudioService mService;
    private NotificationManager mNotificationManager;
//    private NotificationManagerBuilder mNotificationManagerBuilder;
    private boolean isForeground;

    NotificationManagerBuilder2 mNotificationManagerBuilder2;

    public NotificationPlayer(AudioService service) {
        mService = service;
        mNotificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void updateNotificationPlayer() {
        cancel();
//        mNotificationManagerBuilder = new NotificationManagerBuilder();
//        mNotificationManagerBuilder.execute();

        mNotificationManagerBuilder2 = new NotificationManagerBuilder2();
    }

    public void removeNotificationPlayer() {
        cancel();
        mService.stopForeground(true);
        isForeground = false;
    }

    private void cancel() {
//        if (mNotificationManagerBuilder != null) {
//            mNotificationManagerBuilder.cancel(true);
//            mNotificationManagerBuilder = null;
//        }

        if(mNotificationManagerBuilder2!=null) {
            mNotificationManagerBuilder2  = null;
        }
    }

//    private class NotificationManagerBuilder extends AsyncTask<Void, Void, Notification> {
//        private RemoteViews mRemoteViews;
//        private Notification.Builder mNotificationBuilder;
//        private PendingIntent mMainPendingIntent;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Intent mainActivity = new Intent(mService, MainActivity.class);
//            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0);
//            mRemoteViews = createRemoteView(R.layout.notification_player);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                mNotificationBuilder = new Notification.Builder(mService,SC.channelId);
//            } else {
//                mNotificationBuilder = new Notification.Builder(mService);
//            }
//
//            mNotificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
//                    .setOngoing(true)
//                    .setContentIntent(mMainPendingIntent)
//                    .setCustomContentView(mRemoteViews);
//
//            Notification notification = mNotificationBuilder.build();
//            notification.priority = Notification.PRIORITY_MAX;
//            notification.contentIntent = mMainPendingIntent;
//            if (!isForeground) {
//                isForeground = true;
//                // 서비스를 Foreground 상태로 만든다
//                mService.startForeground(NOTIFICATION_PLAYER_ID, notification);
//            }
//        }
//
//        @Override
//        protected Notification doInBackground(Void... params) {
//            mNotificationBuilder.setCustomContentView(mRemoteViews);
//            mNotificationBuilder.setContentIntent(mMainPendingIntent);
//            mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
//            Notification notification = mNotificationBuilder.build();
//            updateRemoteView(mRemoteViews, notification);
//            return notification;
//        }
//
//        @Override
//        protected void onPostExecute(Notification notification) {
//            super.onPostExecute(notification);
//            try {
//                mNotificationManager.notify(NOTIFICATION_PLAYER_ID, notification);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        private RemoteViews createRemoteView(int layoutId) {
//            RemoteViews remoteView = new RemoteViews(mService.getPackageName(), layoutId);
//            Intent actionTogglePlay = new Intent(CommandActions.TOGGLE_PLAY);
//            Intent actionForward = new Intent(CommandActions.FORWARD);
//            Intent actionRewind = new Intent(CommandActions.REWIND);
//            Intent actionClose = new Intent(CommandActions.CLOSE);
//            PendingIntent togglePlay = PendingIntent.getService(mService, 0, actionTogglePlay, 0);
//            PendingIntent forward = PendingIntent.getService(mService, 0, actionForward, 0);
//            PendingIntent rewind = PendingIntent.getService(mService, 0, actionRewind, 0);
//            PendingIntent close = PendingIntent.getService(mService, 0, actionClose, 0);
//
//            remoteView.setOnClickPendingIntent(R.id.btn_play_pause, togglePlay);
//            remoteView.setOnClickPendingIntent(R.id.btn_forward, forward);
//            remoteView.setOnClickPendingIntent(R.id.btn_rewind, rewind);
//            remoteView.setOnClickPendingIntent(R.id.btn_close, close);
//            return remoteView;
//        }
//
//        private void updateRemoteView(RemoteViews remoteViews, Notification notification) {
//            if (mService.isPlaying()) {
//                remoteViews.setImageViewResource(R.id.btn_play_pause, R.drawable.pause);
//            } else {
//                remoteViews.setImageViewResource(R.id.btn_play_pause, R.drawable.play);
//            }
//
//            String title = mService.getAudioItem().mTitle;
//            remoteViews.setTextViewText(R.id.txt_title, title);
//            Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), mService.getAudioItem().mAlbumId);
//            Picasso.get().load(albumArtUri).error(R.drawable.empty).into(remoteViews, R.id.img_albumart, NOTIFICATION_PLAYER_ID, notification);
//        }
//    }


    private class NotificationManagerBuilder2 {
        private RemoteViews mRemoteViews;
        private Notification.Builder mNotificationBuilder;
        private PendingIntent mMainPendingIntent;

        NotificationManagerBuilder2(){
            Intent mainActivity = new Intent(mService, MainActivity.class);
            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0);
            mRemoteViews = createRemoteView(R.layout.notification_player);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationBuilder = new Notification.Builder(mService,SC.channelId);
            } else {
                mNotificationBuilder = new Notification.Builder(mService);
            }

            mNotificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setCustomContentView(mRemoteViews)
                    .setPriority(Notification.PRIORITY_MAX);

            Notification notification = mNotificationBuilder.build();
//            notification.priority = Notification.PRIORITY_MAX;
//            notification.contentIntent = mMainPendingIntent;
            if (!isForeground) {
                isForeground = true;
                // 서비스를 Foreground 상태로 만든다
                mService.startForeground(NOTIFICATION_PLAYER_ID, notification);
            }

//            mNotificationBuilder.setCustomContentView(mRemoteViews);
//            mNotificationBuilder.setContentIntent(mMainPendingIntent);
//            mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
//            Notification notification = mNotificationBuilder.build();
            updateRemoteView(mRemoteViews, notification);

            mNotificationManager.notify(NOTIFICATION_PLAYER_ID, notification);
        }

        private RemoteViews createRemoteView(int layoutId) {
            RemoteViews remoteView = new RemoteViews(mService.getPackageName(), layoutId);
            Intent actionTogglePlay = new Intent(CommandActions.TOGGLE_PLAY);
            Intent actionForward = new Intent(CommandActions.FORWARD);
            Intent actionRewind = new Intent(CommandActions.REWIND);
            Intent actionClose = new Intent(CommandActions.CLOSE);
            PendingIntent togglePlay = PendingIntent.getService(mService, 0, actionTogglePlay, 0);
            PendingIntent forward = PendingIntent.getService(mService, 0, actionForward, 0);
            PendingIntent rewind = PendingIntent.getService(mService, 0, actionRewind, 0);
            PendingIntent close = PendingIntent.getService(mService, 0, actionClose, 0);

            remoteView.setOnClickPendingIntent(R.id.btn_play_pause, togglePlay);
            remoteView.setOnClickPendingIntent(R.id.btn_forward, forward);
            remoteView.setOnClickPendingIntent(R.id.btn_rewind, rewind);
            remoteView.setOnClickPendingIntent(R.id.btn_close, close);
            return remoteView;
        }

        private void updateRemoteView(RemoteViews remoteViews, Notification notification) {
            Log.i("TAG","updateRemoteView");
            if (mService.isPlaying()) {
                remoteViews.setImageViewResource(R.id.btn_play_pause, R.drawable.pause);
            } else {
                remoteViews.setImageViewResource(R.id.btn_play_pause, R.drawable.play);
            }

            String title = mService.getAudioItem().mTitle;
            remoteViews.setTextViewText(R.id.txt_title, title);
            Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), mService.getAudioItem().mAlbumId);
            Picasso.get().load(albumArtUri).error(R.drawable.empty).into(remoteViews, R.id.img_albumart, NOTIFICATION_PLAYER_ID, notification);
        }
    }
}