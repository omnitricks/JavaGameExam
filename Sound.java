import javax.sound.sampled.*;

public class Sound {

    private Clip clip;

	//Add File
    public static Sound bgSound = new Sound("Jungle.wav");
	public static Sound atkSound = new Sound("Sword.wav");
	public static Sound magSound = new Sound("Fireball.wav");
	
    public Sound (String fileName) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	//For Animation
    public void play() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//To Stop
    public void stop(){
        if(clip == null) return;
        clip.stop();
    }
	
	//For BG
    public void loop() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public boolean isActive(){
		return clip.isActive();
	}
}