package com.atilsamancioglu.survivorbirdstarter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture backGround;

    Texture hero;
    float birdX;
    float birdy;
    float gameStarted = 0;
    float velocity = 0;
    float gravity = 0.4f;

    Texture bee1;
    Texture bee2;
    Texture bee3;
    Random random;
    int numberOfEnemies = 4;
    float[] enemyX = new float[numberOfEnemies];
    float[] enemyOffset = new float[numberOfEnemies];
    float[] enemyOffset2 = new float[numberOfEnemies];
    float[] enemyOffset3 = new float[numberOfEnemies];
    float distance;
    float enemyVelocity = 2;
    int score = 0;
    int scoredEnemy = 0;

    BitmapFont font;
    BitmapFont font2;

    Circle birdCircle;

    Circle[] enemyCircles1;
    Circle[] enemyCircles2;
    Circle[] enemyCircles3;

    @Override
    public void create() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);

        font2 = new BitmapFont();
        font2.setColor(Color.WHITE);
        font2.getData().setScale(6);

        batch = new SpriteBatch();
        backGround = new Texture("land.png");
        hero = new Texture("samurai.png");

        birdX = Gdx.graphics.getWidth() / 9;
        birdy = Gdx.graphics.getHeight() / 2;

        birdCircle = new Circle();
        enemyCircles1 = new Circle[numberOfEnemies];
        enemyCircles2 = new Circle[numberOfEnemies];
        enemyCircles3 = new Circle[numberOfEnemies];

        bee1 = new Texture("beess.png");
        bee2 = new Texture("beess.png");
        bee3 = new Texture("beess.png");
        distance = Gdx.graphics.getWidth() / 2;

        random = new Random();

        for (int i = 0; i < numberOfEnemies; i++) {
            enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;
            enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

            enemyCircles1[i] = new Circle();
            enemyCircles2[i] = new Circle();
            enemyCircles3[i] = new Circle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(hero, birdX, birdy, Gdx.graphics.getWidth() / 7, Gdx.graphics.getHeight() / 6);

        if (gameStarted == 1) {
            if (enemyX[scoredEnemy] < Gdx.graphics.getWidth() / 2 - hero.getHeight() / 2) {
                score++;
                scoredEnemy = (scoredEnemy + 1) % numberOfEnemies;
            }

            if (Gdx.input.justTouched()) {
                velocity = -14;
            }

            for (int i = 0; i < numberOfEnemies; i++) {
                if (enemyX[i] < 0) {
                    enemyX[i] += numberOfEnemies * distance;
                    enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                } else {
                    enemyX[i] -= enemyVelocity;
                }

                batch.draw(bee1, enemyX[i], Gdx.graphics.getHeight() / 3 + enemyOffset[i], Gdx.graphics.getWidth() / 7, Gdx.graphics.getHeight() / 5);
                batch.draw(bee2, enemyX[i], Gdx.graphics.getHeight() / 3 + enemyOffset2[i], Gdx.graphics.getWidth() / 7, Gdx.graphics.getHeight() / 5);
                batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 3 + enemyOffset3[i], Gdx.graphics.getWidth() / 7, Gdx.graphics.getHeight() / 5);

                enemyCircles1[i].set(enemyX[i] + Gdx.graphics.getWidth() / 14, Gdx.graphics.getHeight() / 2 + enemyOffset[i], Gdx.graphics.getWidth() / 30);
                enemyCircles2[i].set(enemyX[i] + Gdx.graphics.getWidth() / 14, Gdx.graphics.getHeight() / 2 + enemyOffset2[i], Gdx.graphics.getWidth() / 30);
                enemyCircles3[i].set(enemyX[i] + Gdx.graphics.getWidth() / 14, Gdx.graphics.getHeight() / 2 + enemyOffset3[i], Gdx.graphics.getWidth() / 30);

                if (Intersector.overlaps(birdCircle, enemyCircles1[i]) ||
                        Intersector.overlaps(birdCircle, enemyCircles2[i]) ||
                        Intersector.overlaps(birdCircle, enemyCircles3[i])) {
                    gameStarted = 2; //oyun bittiyor burda
                }
            }

            if (birdy > 0) {
                velocity += gravity;
                birdy -= velocity;
            } else {
                gameStarted = 2; //oyun bitti
            }
        } else if (gameStarted == 0) {
            if (Gdx.input.justTouched()) {
                gameStarted = 1;
            }
        } else if (gameStarted == 2) {
            font2.draw(batch, "Game Over! Tap To Play Again!", 100, Gdx.graphics.getHeight() / 2);

            if (Gdx.input.justTouched()) {
                gameStarted = 1;
                birdy = Gdx.graphics.getHeight() / 2;

                for (int i = 0; i < numberOfEnemies; i++) {
                    enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;
                    enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                    enemyCircles1[i] = new Circle();
                    enemyCircles2[i] = new Circle();
                    enemyCircles3[i] = new Circle();
                }

                velocity = 0;
                scoredEnemy = 0;
                score = 0;
            }
        }

        batch.end();

        birdCircle.set(birdX + Gdx.graphics.getWidth() / 13, birdy + Gdx.graphics.getHeight() / 12, Gdx.graphics.getWidth() / 30);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backGround.dispose();
        hero.dispose();
        bee1.dispose();
        bee2.dispose();
        bee3.dispose();
        font.dispose();
        font2.dispose();
    }
}
