package com.coin.sreens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.coin.MainGame;
import com.coin.extra.Utils;
import static com.coin.extra.Utils.*;
public class GetReadyScreen extends BaseScreen{
    private final Stage stage;
    private final Music music;

    public GetReadyScreen(MainGame mainGame)
    {
        super(mainGame);
        FillViewport vista = new FillViewport(WORLD_WIDTH, WORLD_HEIGTH);
        this.stage=new Stage(vista);
        this.stage.addActor(mainGame.assetManager.addFondo());
        this.music = mainGame.assetManager.getMusic();

    }
    //IMAGEN PLAY
    public void addImagenPlay(){
        Image play = new Image(mainGame.assetManager.getPlay());
        play.setSize(3f,2f);
        play.setPosition((WORLD_WIDTH/2)-(play.getWidth()/2),WORLD_HEIGTH - (WORLD_HEIGTH/1.5f));
        this.stage.addActor(play);
    }

    @Override
    public void show()
    {
        addImagenPlay();
        this.music.setLooping(true);
        this.music.play();
    }
    @Override
    public void hide() {
        this.music.stop();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.stage.draw();
        //SI TOCAMOS CUALQUIER PARTE DE LA PANTALLA NOS LANZA EL JUEGO (GAME SCREEN)
        if (Gdx.input.justTouched()) {
            this.music.stop();
            mainGame.setScreen(new GameScreen(mainGame));
        }
    }
    @Override
    public void dispose(){
        super.dispose();
        this.stage.dispose();
    }

}

