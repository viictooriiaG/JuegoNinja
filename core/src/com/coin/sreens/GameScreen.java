//VICTORIA GONZALEZ IZQUIERDO

package com.coin.sreens;
import static com.coin.extra.Utils.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.coin.MainGame;
import com.coin.actors.Bomb;
import com.coin.actors.Coin;
import com.coin.actors.Ninja;


public class GameScreen extends BaseScreen implements ContactListener {
    //escena que controla los elementos de la pantalla
    private Stage stage;
    private Image background;
    private Ninja ninja;

    //gestiona el mundo fisico
    private World world;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera worldCarmera;
    //MUSICA DEL JUEGO
    private Music music;
    //TIEMPOS MONEDAS
    private final float tiempoAparecerMoneda = 1f;
    private float tiempoCrearMonedas;
    //TIEMPO BOMBAS
    private final float tiempoAparecerBomba = 0.7f;
    private float tiempoCrearBomba;

    //CREAMOS LA OTRA CAMARA PARA Q MUESTRE LA FUENTE
    private OrthographicCamera fontCamera;
    private BitmapFont fuente;

    //CREAMOS EL ATRIBUTO DE MONEDA EN EL ARRAY
    private Array<Coin> arrayCoins;
    //CREAMOS VARIABLE CONTADOR
    private int contador;
    //CREAMOS EL ATRIBUTO DE LA BOMBA EN EL ARRAY
    private Array<Bomb> arrayBomb;





    public GameScreen(MainGame mainGame) {
        super(mainGame);
        //Creamos el mundo y la gravedad, doSleep:
        // mis cuerpos cuando esten en reposo no van a interactuar con el mundo
        this.world = new World(new Vector2(0f, -10f), true);
        //IMPLEMENTA EL TOQUE DE PANTALLA
        this.world.setContactListener(this);
        //Inicializamos el stage creando el viewport
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGTH);
        this.stage = new Stage(fitViewport);
        //INICIALIZAMOS EL ARRAY Y LA VARIABLE PARA ALMACENAR EL TIEMPO DE MONEDAS
        this.arrayCoins = new Array();
        this.tiempoCrearMonedas = 0f;
        //INICIALIZAMOS EL ARRAY Y LA VARIABLE PARA ALMACENAR EL TIEMPO DE LA BOMBA
        this.arrayBomb = new Array();
        this.tiempoCrearBomba = 0f;
        //cargamos la camara
        this.worldCarmera = (OrthographicCamera) this.stage.getCamera();
        //INICIALIZAMOS MUSICA
        this.music = this.mainGame.assetManager.getMusic();
        this.debugRenderer = new Box2DDebugRenderer();
        preparacionFuente();
    }

    @Override
    public void show() {
        addBackground();
        addSuelo();
        addTecho();
        addNinja();
        //REPRODUCCION DE LA MUSICA EN PANTALLA
        //EN BUCLE
        this.music.setLooping(true);
        //VOLUMEN Y PLAY
        this.music.setVolume(0.20f);
        this.music.play();
    }

    private void addNinja() {
        //Metodo que crea el fondo
        Animation<TextureRegion> ninjaSprite = mainGame.assetManager.getNinjaAnimationAtaque();
        //SONIDO
        Sound sonidoNinja = this.mainGame.assetManager.getSound();
        //Posicion donde crea el muñeco y el sonido
        this.ninja = new Ninja(this.world, ninjaSprite, sonidoNinja, new Vector2(2f, 0.50f));
        this.stage.addActor(this.ninja);
    }


    private void preparacionFuente() {
        //CARGAMOS LA FUENTE
        this.contador= 0;
        this.fuente = this.mainGame.assetManager.getFuente();
        this.fuente.getData().scale(0.2f);
        //CREAMOS LA CAMARA EN PIXELES
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        //ACTUALIZAMOS
        this.fontCamera.update();
    }


    private void addCoins(float delta) {
        //CREAMOS TEXTURA DE LA MONEDA
        Animation<TextureRegion> coinTexture = mainGame.assetManager.getCoinsAnimation();
        //HACEMOS LAS COMPROBACIONES NECESARIAS PARA CONTROLAR LOS ESTADOS
        if (ninja.state == Ninja.ESTADO_NORMAL) {
            //ACUMULAMOS DELTA PARA CONTROLAR EL TIEMPO DE LA SIGUIENTE MONEDA
            this.tiempoCrearMonedas += delta;
            //SI EL TIEMPO ACUMULADO ES MAYOR CREAMOS LA MONEDA
            if (this.tiempoCrearMonedas >= tiempoAparecerMoneda) {
                //RESTAMOS EL TIEMPO PARA Q VUELVA A 0
                this.tiempoCrearMonedas -= tiempoAparecerMoneda;
                //CREAMOS LA SIGUIENTE MONEDA
                float posMoneda = MathUtils.random(0.5f, 4f);
                //LA CREAMOS FUERA DE LA PANTALLA
                Coin coin = new Coin(this.world, coinTexture, new Vector2(8f, posMoneda));
                arrayCoins.add(coin);
                this.stage.addActor(coin);
            }
        }
    }

    private void addBomb(float delta) {
        //CREAMOS TEXTURA  DE LA BOMBA
        TextureRegion bombTexture = mainGame.assetManager.getBomb();
        //HACEMOS LAS COMPROBACIONES NECESARIAS PARA CONTROLAR LOS ESTADOS
        if (ninja.state == Ninja.ESTADO_NORMAL) {
            //ACUMULAMOS DELTA PARA CONTROLAR EL TIEMPO DE LA SIGUIENTE BOMBA
            this.tiempoCrearBomba += delta;
            //SI EL TIEMPO ACUMULADO ES MAYOR CREAMOS LA BOMBA
            if (this.tiempoCrearBomba >= tiempoAparecerBomba) {
                //RESTAMOS EL TIEMPO PARA Q VUELVA A 0
                this.tiempoCrearBomba -= tiempoAparecerBomba;
                //CREAMOS LA SIGUIENTE BOMBA
                float posBomba = MathUtils.random(0.5f, 2f);
                //LA CREAMOS FUERA DE LA PANTALLA
                Bomb bomb = new Bomb(this.world, bombTexture, new Vector2(8f, posBomba));
                arrayBomb.add(bomb);
                this.stage.addActor(bomb);
            }
        }
    }

    //ELIMINAMOS LAS MONEDAS
    public void eliminarMonedas() {
        for (Coin coin : this.arrayCoins) {
            //LA FISICA ESTA BLOQUEADA
            if (!world.isLocked()) {
                //COMPROBAMOS SI LA MONEDA QUE HAY FUERA DE PANTALLA
                if (coin.isOutOfScreen()) {
                    //ELIMINAMOS RECURSOS
                    coin.detach();
                    //ELIMINAMOS ESCENARIO
                    coin.remove();
                    //ELIMINAMOS EL ARRAY
                    arrayCoins.removeValue(coin, false);
                }
            }
        }
    }

    //ELIMINAMOS LAS BOMBAS
    public void eliminarBombas() {
        for (Bomb bomb : this.arrayBomb) {
            //LA FISICA ESTA BLOQUEADA
            if (!world.isLocked()) {
                //COMPROBAMOS LA BOMBA QUE HAY FUERA DE PANTALLA
                if (bomb.isOutBomba()) {
                    //ELIMINAMOS RECURSOS
                    bomb.detach();
                    //ELIMINAMOS ESCENARIO
                    bomb.remove();
                    //ELIMINAMOS EL ARRAY
                    arrayBomb.removeValue(bomb, false);
                }
            }
        }
    }


    //CREAMOS EL TECHO CON UNA LINEA
    public void addTecho() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        EdgeShape edge = new EdgeShape();
        edge.set(0, WORLD_HEIGTH, WORLD_WIDTH, WORLD_HEIGTH);
        body.createFixture(edge, 1);
        edge.dispose();
    }

    private void addSuelo() {
        //Colocamos el suelo en su posicion
        BodyDef def = new BodyDef();
        def.position.set(1, 0.3f);
        Body body = world.createBody(def);
        body.setUserData(USER_SUELO);
        //le damos forma
        PolygonShape box = new PolygonShape();
        box.setAsBox(10f, .85f);
        body.createFixture(box, 3);
        box.dispose();
    }

    //Creamos el metodo que configura el fondo
    public void addBackground() {
        this.background = new Image(mainGame.assetManager.getBackground());
        this.background.setPosition(0, 0);
        this.background.setSize(WORLD_WIDTH, WORLD_HEIGTH);
        this.stage.addActor(this.background);
    }


    @Override
    public void render(float delta) {
        //AÑADIMOS MONEDAS EN FUNCION DEL TIEMPO
        int aleatorio = (int) MathUtils.random(0, 6);
        if (aleatorio <= 3) {
            addCoins(delta);
        } else {
            addBomb(delta);
        }
        this.stage.getBatch().setProjectionMatrix(worldCarmera.combined);
        //Ordenamos que lo dibuje y actualizamos la fisica
        this.stage.act();
        this.world.step(delta, 6, 2);
        this.stage.draw();
        //Actualizamos la camara para que realice los cambios internos
        this.worldCarmera.update();
        //se combina el mundo fisico y la camara
        //this.debugRenderer.render(this.world, this.worldCarmera.combined);
        eliminarMonedas();
        eliminarBombas();
        //CARGAMOS LA MATRIZ PARA QUE PROYECTE EL TEXTO CON LAS DIMENSIONES DE PIXELES
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.fuente.draw(this.stage.getBatch(), "POINT: " + contador, SCREEN_WIDTH - 300f, SCREEN_HEIGHT - 15f);
        this.stage.getBatch().end();
    }

    @Override
    public void hide() {
        //Si la pantalla no esta activa quitamos los actores de memoria
        this.ninja.detach();
        this.ninja.remove();
        //PARAMOS LA MUSICA AL QUITAR LOS ACTORES
        this.music.stop();
    }

    @Override
    public void dispose() {
        //Eliminamos recursos
        this.stage.dispose();
        this.world.dispose();
    }

    //------------------COLISIONES--------------------
    //COMPROBAMOS SI OBJETO A CHOCA CON OBJETO B


    @Override
    public void beginContact(Contact contact) {
        //CREAMOS DOS FIXTURAS PARA PODER COMPROBAR CON QUE COLISIONA
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        //SIEMPRE VA A COLISIONAR EL NINJA
        if (a.getBody().getUserData()==USER_NINJA)
            contacto(a, b); // METODO LOGICA COLISION
        else if (b.getBody().getUserData() == USER_NINJA)
            contacto(b, a);
    }
    private void contacto(Fixture ninja, Fixture objeto){
        if (objeto.getUserData()==USER_MONEDA){
            //SI COLISIONAMOS CON LA MONEDA SUMAMOS UNO AL CONTADOR
            this.contador++;
        }
        else if(objeto.getUserData()==USER_BOMB){
            this.stage.addAction(Actions.sequence(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(mainGame.gameOverScreen);
                        }
                    })
            ));
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


}
