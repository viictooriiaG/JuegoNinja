package com.coin.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import static com.coin.extra.Utils.USER_BOMB;
import static com.coin.extra.Utils.USER_COUNTER;

public class Bomb extends Actor {
    //TAMANIO BONBAS
    private static final float BOMB_WIDTH =.5f;
    private static final float BOMB_HEIGHT =.5f;
    private static final float VELOCIDAD= -2f;

    private TextureRegion bomb;
    private World world;
    private Body bodyBomb;
    private Fixture fixtureBomb;


    //CONSTRUCTOR
    public Bomb(World world, TextureRegion trBomb, Vector2 position){
        this.world=world;
        this.bomb = trBomb;
        createBodyBomb(position);
        createFixture();
    }
    //CREAMOS TEXTURA
    private void createBodyBomb(Vector2 position){
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.KinematicBody;
        bodyBomb = this.world.createBody(def);
        bodyBomb.setLinearVelocity(VELOCIDAD,0.5f);
    }
    private void createFixture(){
        PolygonShape shape= new PolygonShape();
        shape.setAsBox(BOMB_WIDTH/2,BOMB_HEIGHT/2);
        this.fixtureBomb=this.bodyBomb.createFixture(shape,8);
        this.fixtureBomb.setSensor(true);
        this.fixtureBomb.setUserData(USER_BOMB);
        shape.dispose();
    }
    //COMPROBAMOS QUE LA BOMBA ESTA FUERA DE LA PANTALLA
    public boolean isOutBomba(){return this.bodyBomb.getPosition().x<=-2;}

    //PARAMOS DE LAS BOMBAS
    public void stopBomb(){
        this.bodyBomb.setLinearVelocity(0,0);
    }

    //SOBRECARCAMOS METODOS
    @Override
    public void act(float delta){super.act(delta);}

    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(bodyBomb.getPosition().x-.25f, bodyBomb.getPosition().y-.25f);
        //TAMANIO BOMBA
        batch.draw(this.bomb, getX(), getY(), 0.5f, 0.5f);

    }
    public void detach(){
        bodyBomb.destroyFixture(fixtureBomb);
        world.destroyBody(bodyBomb);
    }

}
