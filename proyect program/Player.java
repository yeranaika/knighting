import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;

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
    private int frameContador = 0;
    
    // Velocidades de animación para cada set
    private int velocidadAnimCorrerDerecha = 5; // Velocidad de correr hacia la derecha
    private int velocidadAnimCorrerIzquierda = 5; // Velocidad de correr hacia la izquierda
    private int velocidadAnimEstaticoDerecha = 10; // Velocidad de animación estática derecha
    private int velocidadAnimEstaticoIzquierda = 10; // Velocidad de animación estática izquierda
    
    private int velocidadAnimJump = 8;


    // Arrays para diferentes sets de animación
    private GreenfootImage[] framesCorrerDerecha;
    private GreenfootImage[] framesCorrerIzquierda;
    private GreenfootImage[] framesEstaticoDerecha;
    private GreenfootImage[] framesEstaticoIzquierda;
    
    private GreenfootImage[] framesJump;

    public Player()
    {
        // Cargar los sprite sheets
        GreenfootImage spriteSheetCorrerDerecha = new GreenfootImage("player_run_derecha.png");
        GreenfootImage spriteSheetCorrerIzquierda = new GreenfootImage("player_run_izquierda.png");
        GreenfootImage spriteSheetEstaticoDerecha = new GreenfootImage("player_estatico_derecha.png");
        GreenfootImage spriteSheetEstaticoIzquierda = new GreenfootImage("player_estatico_izquierda.png");
        
        GreenfootImage spriteSheetJump = new GreenfootImage("player_jump.png");
        
        // Inicializar animaciones usando el método genérico
        framesCorrerDerecha = cargarFramesDesdeSpriteSheet(spriteSheetCorrerDerecha, 9, 80, 80);
        framesCorrerIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetCorrerIzquierda, 9, 80, 80);
        framesEstaticoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoDerecha, 9, 80, 80);
        framesEstaticoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoIzquierda, 9, 80, 80);
        framesJump = cargarFramesDesdeSpriteSheet(spriteSheetJump, 3, 80, 80);
        
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

    public void animarUniversal(GreenfootImage[] frames, int velocidadAnimacion) {
        frameContador++;
    
        // Cambiar el frame cada 'velocidadAnimacion' ciclos
        if (frameContador % velocidadAnimacion == 0) {
            if (frameActual < frames.length) {
                setImage(frames[frameActual]);
                frameActual++;
            } else {
                frameActual = 0;  // Reinicia el ciclo de frames
            }
        }
    }


    public void mover() {
        int mov = movimientoPlayer(); // Obtener el valor de movimiento
    
        if (mov == MOV_IZQUIERDA) {
            setLocation(getX() - 4, getY());  // Aumentamos la velocidad para moverse más rápido
            animarUniversal(framesCorrerIzquierda, velocidadAnimCorrerIzquierda); // Animación para correr a la izquierda
        } else if (mov == MOV_DERECHA) {
            setLocation(getX() + 4, getY());  // Aumentamos la velocidad para moverse más rápido
            animarUniversal(framesCorrerDerecha, velocidadAnimCorrerDerecha); // Animación para correr a la derecha
        }
    
        // Saltar solo si está en el suelo o sobre una plataforma
        if (mov == MOV_ARRIBA && (enElSuelo() || sobrePlataforma())) {
            velocidadY = FUERZA_SALTO;
        }
    
        // Aplicar la gravedad
        velocidadY += GRAVEDAD;
        if (velocidadY > VELOCIDAD_MAXIMA_CAIDA) {
            velocidadY = VELOCIDAD_MAXIMA_CAIDA;
        }
    
        // Actualizar la posición vertical
        setLocation(getX(), getY() + velocidadY);
    
        // Si el jugador está en el aire (ni en el suelo ni sobre una plataforma), animar el salto
        if (!enElSuelo() && !sobrePlataforma()) {
            animarUniversal(framesJump, velocidadAnimJump);
        } else if (mov == NO_MOV) {
            // Si el jugador ha terminado el salto, regresa a la animación estática
            if (Movimientoanterior == MOV_IZQUIERDA) {
                animarUniversal(framesEstaticoIzquierda, velocidadAnimEstaticoIzquierda); // Animación estática izquierda
            } else if (Movimientoanterior == MOV_DERECHA) {
                animarUniversal(framesEstaticoDerecha, velocidadAnimEstaticoDerecha); // Animación estática derecha
            }
        }
    
        // Si el jugador está en el suelo o sobre una plataforma, se detiene la caída
        if (enElSuelo() || sobrePlataforma()) {
            velocidadY = 0;
        }
    }



    public int movimientoPlayer() {
        if (Greenfoot.isKeyDown("w")) {
            if (enElSuelo() || sobrePlataforma()) {
                Movimientoanterior = MOV_ARRIBA;
                return MOV_ARRIBA;
            }
        }
        if (Greenfoot.isKeyDown("s")) {
            Movimientoanterior = AGACHARSE;
            return AGACHARSE;
        }
        if (Greenfoot.isKeyDown("a")) {
            Movimientoanterior = MOV_IZQUIERDA;
            return MOV_IZQUIERDA;
        }
        if (Greenfoot.isKeyDown("d")) {
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
        // Detectar la plataforma directamente debajo del jugador
        Actor plataforma = getOneObjectAtOffset(0, getImage().getHeight() / 2 + 5, Plataforma.class);
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
