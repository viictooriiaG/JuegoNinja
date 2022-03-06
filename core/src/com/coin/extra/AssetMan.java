package com.coin.extra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import static com.coin.extra.Utils.TODONINJA;
import static com.coin.extra.Utils.BACKGROUND_IMAGE;
import static com.coin.extra.Utils.*;


//Centralizaremos la gestion de los recursos graficos
public class AssetMan {
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;


    public AssetMan(){
        this.assetManager = new AssetManager();
            assetManager.load(TODONINJA, TextureAtlas.class);
            assetManager.finishLoading();
            textureAtlas = assetManager.get(TODONINJA);
            //SONIDO MOVIMIENTO
            assetManager.load(MOVIMIENTO,Sound.class);
            //SONIDO MUSICA
            assetManager.load(MUSIC, Music.class);
            assetManager.finishLoading();
            textureAtlas = assetManager.get(TODONINJA);
    }
    //Metodo que devuelve la parte de imagen que corresponde al fondo
    public TextureRegion getBackground()
    {return this.textureAtlas.findRegion(BACKGROUND_IMAGE);}

    public TextureRegion getGameOver(){
        return this.textureAtlas.findRegion(GAME_OVER);
    }
    public TextureRegion getPlay(){
        return this.textureAtlas.findRegion(PLAY);
    }

    public TextureRegion getBomb()
    {return this.textureAtlas.findRegion(USER_BOMB);}

    public Animation<TextureRegion> getNinjaAnimation(){
        return new Animation<TextureRegion>(0.12f,
                textureAtlas.findRegion(WALK2),
                textureAtlas.findRegion(WALK3),
                textureAtlas.findRegion(WALK4),
                textureAtlas.findRegion(WALK5),
                textureAtlas.findRegion(WALK6),
                textureAtlas.findRegion(WALK7),
                textureAtlas.findRegion(WALK8),
                textureAtlas.findRegion(WALK9));
    }
    public Animation<TextureRegion> getCoinsAnimation(){
        return new Animation<TextureRegion>(0.15f,
        textureAtlas.findRegion(MONEDA1),
        textureAtlas.findRegion(MONEDA2),
        textureAtlas.findRegion(MONEDA3),
        textureAtlas.findRegion(MONEDA4),
        textureAtlas.findRegion(MONEDA5),
        textureAtlas.findRegion(MONEDA6));
    }


    public Animation<TextureRegion> getNinjaAnimationAtaque(){
        return new Animation<TextureRegion>(0.25f,
                textureAtlas.findRegion(ATAQUE1),
                textureAtlas.findRegion(ATAQUE2),
                textureAtlas.findRegion(ATAQUE3),
                textureAtlas.findRegion(ATAQUE4));
    }

    public Music getMusic(){
        return this.assetManager.get(MUSIC);
    }
    public Sound getSound(){
        return this.assetManager.get(MOVIMIENTO);
    }
    public TextureRegion fondoReady(){return this.textureAtlas.findRegion(BACKGROUND_IMAGE);}
    public TextureRegion addPlay(){return this.textureAtlas.findRegion(IMAGENPLAY);}



    public Image addFondo(){
        Image fondo;
        fondo = new Image(fondoReady());
        fondo.setPosition(0,0);
        //MEDIDAS DEL MUNDO
        fondo.setSize(WORLD_WIDTH,WORLD_HEIGTH);
        return fondo;
    }
    //FUENTE
    public BitmapFont getFuente(){
        return new BitmapFont(Gdx.files.internal(FONT_TXT),Gdx.files.internal(FONT_PNG), false);
    }


}
