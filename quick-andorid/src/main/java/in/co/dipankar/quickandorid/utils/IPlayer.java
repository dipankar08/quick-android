package in.co.dipankar.quickandorid.utils;

public interface IPlayer {
    void play(String title, String url);
    void stop();
    boolean isPlaying();
    void pause();
    void resume();
    void restart();
    void mute();
    void unmute();
    boolean isPaused();
}
