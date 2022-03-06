package com.coin.actors;
import static com.coin.extra.Utils.USER_NINJA;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ninja extends Actor {
    //Creamos el atributo con la animacion de las texturas
    private Animation<TextureRegion> ninjaAnimation;
    private TextureRegion ninjaTo;
    private Vector2 position;
    private float stateTime;
    private World world;
    private Body body;
    private Fixture fixture;

    private static final float NINJA_WIDTH = 2.0f;
    private static final float NINJA_HEIGHT = 1.9f;
    private static final float FISICA_WIDTH = .85f;
    private static final float FISICA_HEIGHT = 1.40f;
    //ESTADOS
    private static boolean PUEDE_SALTAR = false;
    public int state;
    public static final int ESTADO_NORMAL = 0;
    public static final int ESTADO_MUERTO = 1;
    public static final float SALTO_VELOCIDAD= 5f;


    //SONIDO MOVIMIENTO
    private Sound movimientoNinja;


    public Ninja(World world, Animation<TextureRegion> animation, Sound movimientoNinja, Vector2 position){
        this.ninjaAnimation=animation;
        this.position=position;
        this.world= world;
        this.movimientoNinja=movimientoNinja;
        stateTime=0f;
        state = ESTADO_NORMAL;
        createBody();
        createFixture();
    }

    //Metodo para crear el cuerpo del ninja
    public void createBody(){
        //Creamos el cuerpo
        BodyDef bodyDef = new BodyDef();
        //pasamos la position
        bodyDef.position.set(position);
        //tipo de cuerpo dinamico/activo
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        //cuerpo del mundo
        this.body = this.world.createBody(bodyDef);
        //this.body = this.world.createBody(bodyDef);
    }


    //Metodo para crear la forma del ninja
    public void createFixture(){
        //Es un rectangulo
       PolygonShape rectangle = new PolygonShape();
       //tamanio
       rectangle.setAsBox(FISICA_WIDTH/2,FISICA_HEIGHT/2);
       //Crea la figura con densidad
        this.fixture = body.createFixture(rectangle,3);
        //Identificar el cuerpo
        //this.fixture.setUserData(USER_NINJA);
        //ASGINAMOS UN NOMBRE PARA HACER LAS COLISIONES
        this.body.setUserData(USER_NINJA);
        rectangle.dispose();
    }
    //METODO PARA CAMBIAR EL ESTADO DEL NINJA CUANDO COLISIONE
    public void derrotado(){
        this.state=ESTADO_MUERTO;
        this.stateTime=0;
    }


    @Override
    public void act(float delta) {
        //CONTROLAMOS EL TOQUE DE PANTALLA
        boolean salto = Gdx.input.justTouched();
        //COMPROBAMOS EL ESTADO: SI EL ESTADO ESTA EN NORMAL, SE LE DA UN IMPUSO EN VERTICAL SIN ANDAR(vX0)
        if(salto && this.state == ESTADO_NORMAL){
            this.movimientoNinja.play();
            this.body.setLinearVelocity(0, SALTO_VELOCIDAD);
        }
    }
    //Sobrecargamos draw
    @Override
    public void draw(Batch batch, float parentAlpha){
        //Posicion
        setPosition(body.getPosition().x-0.45f, body.getPosition().y-.75f);
        //tamanio muñeco
        batch.draw(this.ninjaAnimation.getKeyFrame(stateTime,true),getX(),getY(),1.7f,1.5f);
        stateTime += Gdx.graphics.getDeltaTime();
    }
    //liberar recursos
    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }
}
