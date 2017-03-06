package com.example.usuario.memoria;
import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by micastillo on 27/2/2017.
 */

public final class MyPlayer {
    private Context context;
    public MyPlayer(Context context){
        this.context=context;
    }
    public void play(String audio){
        MediaPlayer mp=this.create(audio);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();mp.release();mp = null;
            }
        });
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }

    public MediaPlayer create(String audio){
        return MediaPlayer.create(context.getApplicationContext(),context.getResources().getIdentifier(audio,"raw", context.getApplicationContext().getPackageName()));
    }
}
