import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Player extends Actor
{
    // Constantes de movimiento
    public static final int MOV_ARRIBA = -1;
    public static final int MOV_IZQUIERDA = -2;
    public static final int MOV_DERECHA = 2;
    public static final int NO_MOV = 0;
    public static final int AGACHARSE = -3;

    private int Movimientoanterior = NO_MOV;

    // Valores de salto
    private int velocidadY = 0;
    private final int GRAVEDAD = 1;
    private final int FUERZA_SALTO = -15;
    private final int VELOCIDAD_MAXIMA_CAIDA = 10;

    // Variables de animación
    private int frameActual = 0;
    private int frameVelocidad = 5; // ciclos
    private int frameContador = 0;

    // Arrays para diferentes sets de animación
    private GreenfootImage[] framesCorrerDerecha;
    private GreenfootImage[] framesCorrerIzquierda;
    private GreenfootImage[] framesEstaticoDerecha;
    private GreenfootImage[] framesEstaticoIzquierda;

    public Player()
    {
        // Cargar los sprite sheets
        GreenfootImage spriteSheetCorrerDerecha = new GreenfootImage("player_run_derecha.png");
        GreenfootImage spriteSheetCorrerIzquierda = new GreenfootImage("player_run_izquierda.png");
        GreenfootImage spriteSheetEstaticoDerecha = new GreenfootImage("player_estatico_derecha.png");
        GreenfootImage spriteSheetEstaticoIzquierda = new GreenfootImage("player_estatico_izquierda.png");

        // Inicializar animaciones usando el método genérico
        framesCorrerDerecha = cargarFramesDesdeSpriteSheet(spriteSheetCorrerDerecha, 9, 80, 80);
        framesCorrerIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetCorrerIzquierda, 9, 80, 80);
        framesEstaticoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoDerecha, 5, 80, 80);
        framesEstaticoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoIzquierda, 5, 80, 80);

        setImage(framesEstaticoDerecha[0]);
    }

    // Método genérico para cargar frames de un sprite sheet
    public GreenfootImage[] cargarFramesDesdeSpriteSheet(GreenfootImage sheet, int numFrames, int frameAncho, int frameAlto) {
        GreenfootImage[] frames = new GreenfootImage[numFrames];

        for (int i = 0; i < numFrames; i++) {
            frames[i] = new GreenfootImage(frameAncho, frameAlto);
            frames[i].drawImage(sheet, -i * frameAncho, 0);  // Extraer la porción correspondiente
        }

        return frames;
    }

    public void animar(int mov) {
        frameContador++;

        if (frameContador % frameVelocidad == 0) {
            if (mov == MOV_DERECHA) {
                setImage(framesCorrerDerecha[frameActual]);
            } else if (mov == MOV_IZQUIERDA) {
                setImage(framesCorrerIzquierda[frameActual]);
            }
            frameActual = (frameActual + 1) % framesCorrerDerecha.length;
        }
    }

    public void animarEstatico(int mov) {
        frameContador++;

        if (frameContador % frameVelocidad == 0) {
            if (mov == MOV_DERECHA) {
                setImage(framesEstaticoDerecha[frameActual % framesEstaticoDerecha.length]);
            } else if (mov == MOV_IZQUIERDA) {
                setImage(framesEstaticoIzquierda[frameActual % framesEstaticoIzquierda.length]);
            }
            frameActual = (frameActual + 1) % framesEstaticoDerecha.length;
        }
    }

    public void mover() {
        int mov = movimientoPlayer(); // Obtener el valor de movimiento

        if (mov == MOV_IZQUIERDA) {
            setLocation(getX() - 2, getY());
            animar(MOV_IZQUIERDA); 
        } else if (mov == MOV_DERECHA) {
            setLocation(getX() + 2, getY());
            animar(MOV_DERECHA);
        }

        if (mov == MOV_ARRIBA && (enElSuelo() || sobrePlataforma())) {
            velocidadY = FUERZA_SALTO;
        }

        // Aplicar la gravedad
        velocidadY += GRAVEDAD;
        if (velocidadY > VELOCIDAD_MAXIMA_CAIDA) {
            velocidadY = VELOCIDAD_MAXIMA_CAIDA;
        }

        // Actualizar la posición vertical solo si no está sobre la plataforma
        if (!sobrePlataforma()) {
            setLocation(getX(), getY() + velocidadY);
        }

        if (enElSuelo() || sobrePlataforma()) {
            velocidadY = 0;
        }

        if (mov == NO_MOV) {
            animarEstatico(Movimientoanterior);
        }
    }

    public int movimientoPlayer() {
        if (Greenfoot.isKeyDown("up")) {
            if (enElSuelo() || sobrePlataforma()) {
                Movimientoanterior = MOV_ARRIBA;
                return MOV_ARRIBA;
            }
        }
        if (Greenfoot.isKeyDown("down")) {
            Movimientoanterior = AGACHARSE;
            return AGACHARSE;
        }
        if (Greenfoot.isKeyDown("left")) {
            Movimientoanterior = MOV_IZQUIERDA;
            return MOV_IZQUIERDA;
        }
        if (Greenfoot.isKeyDown("right")) {
            Movimientoanterior = MOV_DERECHA;
            return MOV_DERECHA;
        }
        return NO_MOV;
    }

    // Método que comprueba si el jugador está en el suelo
    public boolean enElSuelo() {
        return getY() >= getWorld().getHeight() - 50;  // Ajusta este valor según el suelo de tu juego
    }

    // Método que detecta si el jugador está sobre una plataforma (parte superior de la plataforma)
    public boolean sobrePlataforma() {
        // Detectar la plataforma en una región un poco más ancha que el jugador
        Actor plataforma = getOneObjectAtOffset(0, getImage().getHeight() / 2, Plataforma.class);
        if (plataforma != null) {
            // Alinear el jugador con la parte superior de la plataforma
            setLocation(getX(), plataforma.getY() - plataforma.getImage().getHeight() / 2 - getImage().getHeight() / 2);
            return true;
        }
        return false;
    }

    public void act() {
        mover();   // Llamar al método de movimiento y animación
    }
}
