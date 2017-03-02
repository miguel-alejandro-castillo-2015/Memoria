package com.example.usuario.memoria;

/**
 * Created by micastillo on 1/3/2017.
 */

public interface ICountDownTimer {
    public void start();
    public void pause();
    public void resume();
    public void stop();
    public boolean isActive();
}
