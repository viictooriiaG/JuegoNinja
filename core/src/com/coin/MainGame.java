package com.coin;
import com.badlogic.gdx.Game;
import com.coin.extra.AssetMan;
import com.coin.sreens.GameOverScreen;
import com.coin.sreens.GameScreen;
import com.coin.sreens.GetReadyScreen;

public class MainGame extends Game{
    public GameScreen gameScreen;
    public AssetMan assetManager;
    public GameOverScreen gameOverScreen;
    public GetReadyScreen getReadyScreen;
    @Override
    public void create() {
        this.assetManager = new AssetMan();
        this.gameScreen = new GameScreen(this);
        this.gameOverScreen = new GameOverScreen(this);
        this.getReadyScreen = new GetReadyScreen(this);
        //Manejamos las instancias de las diferentes pantallas
        setScreen(this.getReadyScreen);

    }
}
