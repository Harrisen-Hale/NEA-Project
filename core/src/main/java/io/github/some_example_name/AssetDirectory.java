package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public final class AssetDirectory {
    public static final class Textures{
        public static final class Player {
            public static final String IDLE = "textures/player/player_idle.png";
            public static final String ROLL = "textures/player/player_roll.png";
        }
    }
    public static final class Audio{
        public static final class Player {
            public static final Sound FAH = Gdx.audio.newSound(Gdx.files.internal("audio/player/fah.ogg"));
            public static final Sound ROLL = Gdx.audio.newSound(Gdx.files.internal("audio/player/ROLL_SFX.ogg"));
        }
    }

    public static final class Scene{
        public static final String MUD = "textures/scene/mud_1.png";
    }
}
