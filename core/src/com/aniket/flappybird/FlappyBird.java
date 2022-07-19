package com.aniket.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

//	INITIALIZATION

	// Sprite Batch
	SpriteBatch batch;

	// Textures
	Texture background;
	Texture[] bird;
	Texture gameover;
	Texture toptube;
	Texture bottomtube;

	// Shapes
	Circle birdcircle;
	Rectangle[] toptuberectangle;
	Rectangle[] bottomtuberectangle;

	// Random Generator
	Random randomgenerator;

	// Bitmap Fonts
	BitmapFont font;
	BitmapFont[] fonts;

	// Extra Variables
	int flapstate = 0;
	float birdy = 0;
	float velocity = 0;
	float gap = 225;
	float[] tubeoffset = new float[2];
	float[] tubex = new float[2];
	int tubenumber = 0;
	float tubevelocity = 8;
	int gamestate = 0;
	int tubestate = 0;
	int score = 0;

	// create function
	@Override
	public void create () {

		//	DECLARATION

		// Sprite Batch
		batch = new SpriteBatch();

		// Textures
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		bird = new Texture[2];
		bird[0] = new Texture("bird.png");
		bird[1] = new Texture("bird2.png");
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");

		// Random Generator
		randomgenerator = new Random();

		// Bitmap Fonts
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		fonts = new BitmapFont[2];
		fonts[0] = new BitmapFont();
		fonts[1] = new BitmapFont();
		fonts[0].setColor(Color.WHITE);
		fonts[0].getData().setScale(11);
		fonts[1].setColor(Color.RED);
		fonts[1].getData().setScale(7);

		// Extra variables and Shapes
		startgame();
	}

	// startgame function
	public void startgame(){

		// Extra Variables
		birdy = 0;
		velocity = 0;
		tubenumber = 0;
		gamestate = 0;
		tubestate = 0;
		score = 0;
		flapstate = 0;
		gap = 225;
		tubevelocity = 8;
		birdy = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2;
		tubex[0] = Gdx.graphics.getWidth()+1;
		tubex[1] = Gdx.graphics.getWidth()+1;
		tubeoffset[tubenumber] = (randomgenerator.nextFloat()* (0 - ((Gdx.graphics.getHeight() - toptube.getHeight() - 2*gap) - bottomtube.getHeight())) + ((Gdx.graphics.getHeight() - toptube.getHeight() - 2*gap) - bottomtube.getHeight()));

		// Shapes
		toptuberectangle = new Rectangle[2];
		toptuberectangle[0] = new Rectangle();
		toptuberectangle[1] = new Rectangle();
		bottomtuberectangle = new Rectangle[2];
		bottomtuberectangle[0] = new Rectangle();
		bottomtuberectangle[1] = new Rectangle();
		birdcircle = new Circle();
	}

	// render function
	@Override
	public void render () {

		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate == 2){

			batch.draw(toptube, tubex[tubenumber], tubeoffset[tubenumber] + bottomtube.getHeight() + 2 * gap);
			batch.draw(bottomtube, tubex[tubenumber], tubeoffset[tubenumber]);
			batch.draw(toptube, tubex[tubenumber ^ 1], tubeoffset[tubenumber ^ 1] + bottomtube.getHeight() + 2 * gap);
			batch.draw(bottomtube, tubex[tubenumber ^ 1], tubeoffset[tubenumber ^ 1]);
			batch.draw(bird[flapstate], Gdx.graphics.getWidth() / 2 - bird[flapstate].getWidth() / 2, birdy);

			fonts[0].draw(batch, "SCORE", Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() - 325);
			fonts[1].draw(batch, "Tap to Restart", 200, 200);
			font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - 45 , Gdx.graphics.getHeight() - 500);

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - 350, Gdx.graphics.getHeight() / 2 - 250, 700, 500);

			if(Gdx.input.justTouched()) {
				startgame();
			}

		}
		else {

			if (gamestate == 1) {

				if (Gdx.input.justTouched()) {

					velocity = -25;
					flapstate = 1;

				}

				batch.draw(toptube, tubex[tubenumber], tubeoffset[tubenumber] + bottomtube.getHeight() + 2 * gap - 3);
				batch.draw(bottomtube, tubex[tubenumber], tubeoffset[tubenumber] + 4);

				toptuberectangle[tubenumber] = new Rectangle(tubex[tubenumber], tubeoffset[tubenumber] + bottomtube.getHeight() + 2 * gap + 3, toptube.getWidth(), toptube.getHeight());
				bottomtuberectangle[tubenumber] = new Rectangle(tubex[tubenumber], tubeoffset[tubenumber] - 4, bottomtube.getWidth(), bottomtube.getHeight());

				if (tubestate == 1) {

					if (tubex[tubenumber ^ 1] <= -toptube.getWidth()) {
						tubex[tubenumber ^ 1] = Gdx.graphics.getWidth() + 1;
						tubestate = 0;

					}

					tubex[tubenumber ^ 1] = tubex[tubenumber ^ 1] - tubevelocity;

				}
				if (tubex[tubenumber] <= Gdx.graphics.getWidth() / 2 - toptube.getWidth()) {

					score++;
					tubestate = 1;
					tubenumber = tubenumber ^ 1;
					tubeoffset[tubenumber] = (randomgenerator.nextFloat() * (0 - ((Gdx.graphics.getHeight() - toptube.getHeight() - 2 * gap) - bottomtube.getHeight())) + ((Gdx.graphics.getHeight() - toptube.getHeight() - 2 * gap) - bottomtube.getHeight()));

				}

				tubex[tubenumber] = tubex[tubenumber] - tubevelocity;

				batch.draw(toptube, tubex[tubenumber ^ 1], tubeoffset[tubenumber ^ 1] + bottomtube.getHeight() + 2 * gap - 3);
				batch.draw(bottomtube, tubex[tubenumber ^ 1], tubeoffset[tubenumber ^ 1] + 4);

				toptuberectangle[tubenumber^1] = new Rectangle(tubex[tubenumber ^ 1], tubeoffset[tubenumber ^ 1] + bottomtube.getHeight() + 2 * gap + 3, toptube.getWidth(), toptube.getHeight());
				bottomtuberectangle[tubenumber^1] = new Rectangle(tubex[tubenumber ^ 1], tubeoffset[tubenumber ^ 1] - 4, bottomtube.getWidth(), bottomtube.getHeight());

				if (birdy > 0) {

					if (birdy > Gdx.graphics.getHeight() - bird[flapstate].getHeight())
						birdy = Gdx.graphics.getHeight() - bird[flapstate].getHeight();

					velocity++;
					birdy -= velocity;

					if (velocity == 0)
						flapstate = 0;

				}
				if (birdy < 0)
					gamestate = 2;

				batch.draw(bird[flapstate], Gdx.graphics.getWidth() / 2 - bird[flapstate].getWidth() / 2, birdy);

				font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 175);

				if (Intersector.overlaps(birdcircle, toptuberectangle[tubenumber]) || Intersector.overlaps(birdcircle, bottomtuberectangle[tubenumber]))
					gamestate = 2;

			}
			else {

				if (Gdx.input.justTouched()) {

					gamestate = 1;
					flapstate = 0;

				}

			}

			batch.draw(bird[flapstate], Gdx.graphics.getWidth() / 2 - bird[flapstate].getWidth() / 2, birdy);
			font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 175);
			birdcircle.set(Gdx.graphics.getWidth() / 2, birdy + bird[flapstate].getHeight() / 2, bird[flapstate].getHeight() / 2);

		}

		batch.end();
	}

	// dispose function
	@Override
	public void dispose () {
		batch.dispose();
	}
}
