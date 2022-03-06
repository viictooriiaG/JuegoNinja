package com.coin.sreens;
import static com.coin.extra.Utils.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.coin.MainGame;

public class GameOverScreen extends BaseScreen{
    private final Stage stage;
    public GameOverScreen(MainGame mainGame) {
        super(mainGame);
        FillViewport fillViewport = new FillViewport(WORLD_WIDTH,WORLD_HEIGTH);
        this.stage = new Stage(fillViewport);
        this.stage.addActor(mainGame.assetManager.addFondo());
    }
    //IMAGEN GAME OVER
    public void addGameOver(){
        Image gameOver = new Image(mainGame.assetManager.getGameOver());
        gameOver.setSize(5f,3f);
        gameOver.setPosition((WORLD_WIDTH/2)-(gameOver.getWidth()/2),WORLD_HEIGTH - (WORLD_HEIGTH/1.4f));
        this.stage.addActor(gameOver);
    }
    @Override
    public void show(){
        addGameOver();
    }
    @Override
    public void hide(){
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        this.stage.draw();

        //SI EL USUARIO TOCA LA PANTALLA
        if (Gdx.input.justTouched()){
            mainGame.setScreen(mainGame.getReadyScreen);
        }
    }
    @Override
    public void dispose(){
        super.dispose();
        this.stage.dispose();
    }
}
