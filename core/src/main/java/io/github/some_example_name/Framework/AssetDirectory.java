package io.github.some_example_name.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public final class AssetDirectory {

    public static final class Textures{
        public static final class Player {
            public static final String IDLE = "textures/player/player_idle.png";
            public static final String ROLL = "textures/player/player_roll.png";
        }
        public static final class UI {
            public static final String HEALTH_BAR = "textures/ui/health_bar.png";
            public static final String STAMINA_BAR = "textures/ui/stamina_bar.png";
            public static final String BACKGROUND_BAR = "textures/ui/background_bar.png";
        }
        public static final class Misc {
            public static final String NOT_FOUND = "textures/misc/texture_not_found.png";
            public static final String LOCK_DOT = "textures/misc/lock_dot.png";
        }
        public static final class Entity {
            public static final String DUMMY = "textures/entity/dummy.png";
            public static final class Forlorn {
                public static final String IDLE = "textures/entity/forlorn/forlorn_idle.png";
            }
        }
        public static final class Scene{
            public static final String MUD1 = "textures/scene/mud_1.png";
            public static final String MUD2 = "textures/scene/mud_2.png";
            public static final String MUD3 = "textures/scene/mud_3.png";
            public static final String GRASS1 = "textures/scene/grass_1.png";
            public static final String ROCK = "textures/scene/rock_1.png";
        }
    }
    public static final class Audio{
        public static final class Player {
            public static final Sound ROLL = Gdx.audio.newSound(Gdx.files.internal("audio/player/roll.ogg"));
            public static final Sound HURT = Gdx.audio.newSound(Gdx.files.internal("audio/player/player_hurt.ogg"));
            public static final Sound WALK = Gdx.audio.newSound(Gdx.files.internal("audio/player/player_walk.ogg"));
        }
    }

    public static void dispose(){
        Audio.Player.ROLL.dispose();
        Audio.Player.HURT.dispose();
        Audio.Player.WALK.dispose();
    }
}
