package eu.gebes.tryjump.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import eu.gebes.tryjump.Variables;

public class SoundManager implements Disposable {

    Sound music, won;

    public SoundManager(){

        music = Gdx.audio.newSound(Gdx.files.internal("sounds/music.wav"));
        won = Gdx.audio.newSound(Gdx.files.internal("sounds/won.ogg"));
    }

    public void music() {
        if (Variables.musicVolume != 0) {
            music.setLooping(music.play(Variables.musicVolume), true);
        }
    }

    public void won(){
        won.play();
    }

    @Override
    public void dispose() {
        music.dispose();
        won.dispose();
    }
}
