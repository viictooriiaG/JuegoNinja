package com.coin.actors;


import static com.coin.extra.Utils.USER_COUNTER;
import static com.coin.extra.Utils.USER_MONEDA;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Coin extends Actor {
    //TAMANIO MONEDAS
    private static final float COIN_WIDTH =.5f;
    private static final float COIN_HEIGHT =.5f;
    private static final float VELOCIDAD= -2f;
    private static final float SPACE_COIN= 2f;



    //CREAMOS TEXTURA
    private Animation<TextureRegion> coinAnimation;
    private Vector2 position;
    private float stateTime;
    //CREAMOS BODYS
    private Body body;
    //CREAMOSM FIXTURE
    private Fixture fixtureCoin;
    //CREMOS EL MUNDO
    private World world;
    //CREAMOS EL CONTADOR
    private Body bodyContador;
    private Fixture contadorFixture;
    //NUMERO QUE GENERA LA POSICION DE LA MONEDA
    private final int posicionContador = MathUtils.random(0,10);


    //CONSTRUCTOR DE MUNDO, TEXTURA Y POSICION
    public Coin(World world, Animation<TextureRegion> trCoin, Vector2 position){
        this.world = world;
        this.coinAnimation = trCoin;
        createBodyCoin(position);
        createFixture();
    }
    //METODO PARA EL BODY MONEDAS
    private void createBodyCoin(Vector2 position){
        BodyDef def= new BodyDef();
        //POSICION DE LA FISICA IGUAL QUE LA QUE EL DIBUJO
        def.position.set(position.x,position.y);
        //TIPO DE FISICA
        def.type= BodyDef.BodyType.KinematicBody;
        body = this.world.createBody(def);
        body.setLinearVelocity(VELOCIDAD,0f);
    }

    //METODO PARA CREAR LA FIXTURA MONEDAS
    public void createFixture(){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(COIN_WIDTH/2, COIN_HEIGHT/2);
        this.fixtureCoin = this.body.createFixture(shape,8);

        //SENSOR PARA QUE ATRAVIESE LA MONEDA
        this.fixtureCoin.setSensor(true);
        this.fixtureCoin.setUserData(USER_MONEDA);
        shape.dispose();
    }

    //NOS DICE SI ESTA LA MONEDA FUERA DE LA PANTALLA
    public boolean isOutOfScreen(){
        return this.body.getPosition().x <=-2;
    }
    //PARAMOS DE LAS BOMBAS
    public void stopCoins(){
        this.body.setLinearVelocity(0,0);
    }

    //SOBRECARCAMOS METODOS
    @Override
    public void act(float delta){super.act(delta);}


    @Override
    public void draw(Batch batch, float parentAlpha){
        setPosition(body.getPosition().x-.25f, body.getPosition().y-.25f);
        //TAMANIO MONEDA
        batch.draw(this.coinAnimation.getKeyFrame(stateTime, true),getX(),getY(),.5f,.5f);

        stateTime += Gdx.graphics.getDeltaTime();
    }
    //LIBERAR RECURSOS
    public void detach() {
        body.destroyFixture(fixtureCoin);
        world.destroyBody(body);

    }
}
