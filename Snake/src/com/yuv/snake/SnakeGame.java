package com.yuv.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SnakeGame extends Game {

	SpriteBatch batch;
	BitmapFont font;
	FPSLogger fps;

	public void create() {
		batch = new SpriteBatch();
		fps = new FPSLogger();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		Assets.load();
		Settings.load();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
		fps.log();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		getScreen().dispose();
	}

	public static void handleBackgroundSound() {

		Settings.soundEnabled = !Settings.soundEnabled;
		if (Settings.soundEnabled) {
			Assets.backgroundMusic.play();
		}else{
			Assets.backgroundMusic.pause();
		}

	}
}
