package com.yuv.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

	public static Texture foodImage;
	public static Texture foodImage2;
	public static Texture foodImage3;
	public static Texture foodImage4;
	public static Texture snakeHeadImage;
	public static Texture snakeBodyImage;
	public static Sound eatingSound;
	public static Music backgroundMusic;

	public static void load() {
		// load the images for the droplet and the bucket, 64x64 pixels each
		//foodImage = new Texture(Gdx.files.internal("data/droplet.png"));
		foodImage = new Texture(Gdx.files.internal("data/food.png"));
		foodImage2 = new Texture(Gdx.files.internal("data/apple.png"));
		foodImage3 = new Texture(Gdx.files.internal("data/strawberry.png"));
		foodImage4 = new Texture(Gdx.files.internal("data/banana.png"));
		//snakeImage = new Texture(Gdx.files.internal("data/bucket.png"));
		//snakeImage = new Texture(Gdx.files.internal("data/block.png"));
		snakeBodyImage = new Texture(Gdx.files.internal("data/button-white.png"));
		
		snakeHeadImage = new Texture(Gdx.files.internal("data/head.png"));

		// load the drop sound effect and the rain background "music"
		eatingSound = Gdx.audio.newSound(Gdx.files.internal("data/mumble.mp3"));
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("data/background.wav"));
		backgroundMusic.setLooping(true);

	}
	
	public static void dispose(){
		foodImage.dispose();
		snakeBodyImage.dispose();
		eatingSound.dispose();
		backgroundMusic.dispose();
		snakeHeadImage.dispose();
	}
}
