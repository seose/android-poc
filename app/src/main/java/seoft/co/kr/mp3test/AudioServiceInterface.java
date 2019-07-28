package seoft.co.kr.mp3test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;

public class AudioServiceInterface {
    private ServiceConnection mServiceConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((AudioService.AudioServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceConnection = null;
            mService = null;
        }
    };
    private AudioService mService;

    public AudioServiceInterface(Context context) {

        Intent intent = new Intent(context, AudioService.class);
//        intent.setPackage(context.getPackageName());

        context.bindService( intent , mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void setPlayList(ArrayList<Long> audioIds) {
        if (mService != null) {
            mService.setPlayList(audioIds);
        }
    }

    public void play(int position) {
        if (mService != null) {
            mService.play(position);
        }
    }

    public void play() {
        if (mService != null) {
            mService.play();
        }
    }

    public void pause() {
        if (mService != null) {
            mService.play();
        }
    }

    public void forward() {
        if (mService != null) {
            mService.forward();
        }
    }

    public void rewind() {
        if (mService != null) {
            mService.rewind();
        }
    }

    public void togglePlay() {
        if (isPlaying()) {
            mService.pause();
        } else {
            mService.play();
        }
    }

    public boolean isPlaying() {
        if (mService != null) {
            return mService.isPlaying();
        }
        return false;
    }

    public AudioAdapter.AudioItem getAudioItem() {
        if (mService != null) {
            return mService.getAudioItem();
        }
        return null;
    }
}
