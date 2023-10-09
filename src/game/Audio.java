package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class Audio {
    Map<String, Clip> sounds;

    public Audio() {
        sounds = new java.util.HashMap<>();
    }

    public void load(String path, String name) throws Exception {
        InputStream stream = new FileInputStream(path);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        sounds.put(name, clip);
    }

    public void play(String name) {
        Clip clip = sounds.get(name);
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void loopPlayForever(String name, float volume) {
        Clip clip = sounds.get(name);

        // set volume
        float gain = volume;
        if (gain < 0f || gain > 1f) {
            throw new IllegalArgumentException("Volume not valid: " + gain);
        }

        // set volume
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }
}
