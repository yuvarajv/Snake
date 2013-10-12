package com.yuv.snake;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen {
	SnakeGame game;
	OrthographicCamera camera;
	Rectangle food;
	Rectangle snake;
	int score;

	List<Rectangle> tail;

	boolean firstTail = true;
	boolean actionTriggred = false;

	Rectangle prevHead;
	List<Rectangle> prevTail;
	long lastChangeTime;
	short direction;
	Texture foodImg;

	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;

	short gameStatus;

	public GameScreen(SnakeGame game) {
		this.game = game;
		gameStatus = GAME_READY;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		food = new Rectangle();
		food.height = 64;
		food.width = 64;

		snake = new Rectangle();
		snake.x = 800 / 2 - Assets.snakeBodyImage.getWidth() / 2;
		snake.y = 0;
		snake.height = Assets.snakeBodyImage.getHeight();
		snake.width = Assets.snakeBodyImage.getWidth();

		addFood();
		tail = new ArrayList<Rectangle>();

		prevHead = new Rectangle();

		lastChangeTime = System.currentTimeMillis();
	}

	public void selectFood() {
		switch (MathUtils.random(0, 3)) {
		case 0:
			foodImg = Assets.foodImage;
			break;
		case 1:
			foodImg = Assets.foodImage2;
			break;
		case 2:
			foodImg = Assets.foodImage3;
			break;
		case 3:
			foodImg = Assets.foodImage4;
			break;

		default:
			foodImg = Assets.foodImage;
			break;
		}
	}

	private void addFood() {
		selectFood();

		/*
		int rand = MathUtils.random(0, 800 - Assets.foodImage.getWidth());
		food.x = Math.round(rand / 32) * 32;
		rand = MathUtils.random(0, 480 - Assets.foodImage.getWidth());
		food.y = Math.round(rand / 32) * 32;
		
		*/
		int rand = MathUtils.random(0, 23);
		food.x = rand * 32;
		
		rand = MathUtils.random(0, 13);
		food.y = rand * 32;
		
		//Save Score
		Settings.addScore(score);
		Settings.save();
	}

	private void updateRunning(float delta) {
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = 0;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction = 1;
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			direction = 2;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			direction = 3;
		}

		if ((System.currentTimeMillis() - lastChangeTime) >= 100) {
			lastChangeTime = System.currentTimeMillis();
			switch (direction) {
			case 0:
				onAction(-(Assets.snakeBodyImage.getWidth()), 0);
				break;

			case 1:
				onAction(Assets.snakeBodyImage.getWidth(), 0);
				break;

			case 2:
				onAction(0, Assets.snakeBodyImage.getHeight());
				break;
			case 3:
				onAction(0, -(Assets.snakeBodyImage.getHeight()));
				break;

			default:
				break;
			}
		}
		if (snake.x < 0) {
			snake.x = 0;
		}
		if (snake.x > (800 - snake.getWidth())) {
			snake.x = 800 - snake.getWidth();
		}
		if (snake.y < 0) {
			snake.y = 0;
		}
		if (snake.y > (480 - snake.getHeight())) {
			snake.y = 480 - snake.getHeight();
		}

		// checkforCollision();

		if (food.overlaps(snake)) {
			Assets.eatingSound.play();
			addFood();
			score++;
			addTail();
		}

	}

	private void ready() {
		if (Gdx.input.justTouched()) {
			gameStatus = GAME_RUNNING;
		}
	}

	private void paused() {
		if (Gdx.input.justTouched()) {
			gameStatus = GAME_RUNNING;
		}
	}

	private void over() {
		game.setScreen(new MainMenuScreen(game));
		this.dispose();
	}

	private void running(float delta) {
		if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) &&  Gdx.input.isKeyPressed(Keys.SPACE)) {
			System.out.println("Pressed space!!");
			gameStatus = GAME_PAUSED;
		} else if (Gdx.input.isKeyPressed(Keys.BACKSPACE)){
			System.out.println("Pressed backspace!!");
			gameStatus = GAME_OVER;
		}else {
			updateRunning(delta);
		}
	}

	private void levelEnd(float delta) {

	}

	private void update(float delta) {

		if (Gdx.input.isKeyPressed(Keys.X)) {
			SnakeGame.handleBackgroundSound();
		}

		switch (gameStatus) {
		case GAME_READY:
			ready();
			break;
		case GAME_PAUSED:
			paused();
			break;
		case GAME_RUNNING:
			running(delta);
			break;
		case GAME_OVER:
			over();
			break;
		case GAME_LEVEL_END:
			levelEnd(delta);
			break;

		default:
			break;
		}
	}

	private void drawRunning() {
		game.batch.draw(Assets.snakeHeadImage, snake.x, snake.y);
		for (Rectangle newTail : tail) {
			/*
			 * System.out.println("Tail:" + tail.size() + firstTail +
			 * actionTriggred); System.out.println("Prev:" + prevHead.x + "," +
			 * prevHead.y); System.out.println("Snake:" + snake.x + "," +
			 * snake.y); System.out.println("newTail:" + newTail.x + "," +
			 * newTail.y);
			 */
			game.batch.draw(Assets.snakeBodyImage, newTail.x, newTail.y);
		}

		game.batch.draw(foodImg, food.x, food.y);

		prevTail = tail;
		prevHead.x = snake.x;
		prevHead.y = snake.y;
	}

	private void draw(float delta) {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "Score : " + score, 0, 480);

		switch (gameStatus) {
		case GAME_READY:
			game.font.draw(game.batch, "Press any key to start!!", 200, 240);
			break;
		case GAME_RUNNING:
			drawRunning();
			break;
		case GAME_PAUSED:
			game.font.draw(game.batch, "Press any key to continue!!", 200, 240);
			break;
		case GAME_OVER:
			game.font.draw(game.batch, "Game over!! Score: "+score, 200, 240);
			break;	
		default:
			break;
		}
		game.batch.end();

	}

	@Override
	public void render(float delta) {

		update(delta);
		draw(delta);

	}

	public void checkforCollision() {
		for (Rectangle newTail : tail) {
			if (snake.overlaps(newTail)) {
				game.batch.begin();
				game.font.draw(game.batch,
						"Collision happend.. Game should end", 400, 480);
				game.batch.end();
			}
		}
	}

	public void onAction(float x, float y) {
		// System.out.println("snake:oldx:" + snake.x + " newSnakex:" + x);
		// System.out.println("snake:oldy:" + snake.y + " newSnakey:" + y);

		prevHead.x = snake.x;
		prevHead.y = snake.y;

		Rectangle tailTemp = new Rectangle();

		tailTemp.x = snake.x;
		tailTemp.y = snake.y;

		snake.x = snake.x + x;
		snake.y = snake.y + y;
		int i = 0;

		float tempX;
		float tempY;

		for (Rectangle rect : tail) {

			tempX = rect.x;
			tempY = rect.y;

			rect.x = tailTemp.x;
			rect.y = tailTemp.y;

			tailTemp.x = tempX;
			tailTemp.y = tempY;

		}
		/*
		 * if (!firstTail) { actionTriggred = true; } int cntr = 0;
		 * 
		 * Rectangle temp = new Rectangle();
		 * 
		 * for (Rectangle newTail : tail) { if (firstTail && !actionTriggred) {
		 * firstTail = false; } else { temp.x = newTail.x; temp.y = newTail.y;
		 * 
		 * if(cntr==0) { //newTail.x = newTail.x + x; //newTail.y = newTail.y +
		 * y; newTail.x = prevHead.x; newTail.y = prevHead.y; }else{ newTail.x =
		 * temp.x; newTail.y = temp.y; }
		 * 
		 * }
		 */
		// }
	}

	private void addTail() {

		// System.out.println("Adding tail");
		Rectangle newTail = new Rectangle();
		newTail.height = Assets.snakeBodyImage.getHeight();
		newTail.width = Assets.snakeBodyImage.getWidth();

		Rectangle tailRect;
		if (tail.size() > 0) {
			tailRect = tail.get(tail.size() - 1);
		} else {
			tailRect = snake;
		}

		switch (direction) {
		case 0:
			newTail.x = tailRect.x - 32;
			newTail.y = tailRect.y;
			break;
		case 1:
			newTail.x = tailRect.x + 32;
			newTail.y = tailRect.y;
			break;
		case 2:
			newTail.x = tailRect.x;
			newTail.y = tailRect.y + 32;
			break;
		case 3:
			newTail.x = tailRect.x;
			newTail.y = tailRect.y - 32;
			break;

		default:
			break;
		}

		// newTail.y = tailRect.y - 32;
		// newTail.x = tailRect.x;
		tail.add(newTail);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {

		if (gameStatus == GAME_RUNNING) {
			gameStatus = GAME_PAUSED;
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
