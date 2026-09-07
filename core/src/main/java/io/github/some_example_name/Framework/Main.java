package io.github.some_example_name.Framework;

import com.badlogic.gdx.ApplicationAdapter;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    Game game = new Game();

    @Override
    public void create() {
        game = new Game();
        game.initialise();
    }

    @Override
    public void render() {
        game.gameUpdate();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
