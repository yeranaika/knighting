import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;

public class Player extends Actor {
    // Constantes de movimiento
    public static final int MOV_ARRIBA = -1;
    public static final int MOV_IZQUIERDA = -2;
    public static final int MOV_DERECHA = 2;
    public static final int AGACHARSE = -3;

    private int Movimientoanterior = NO_MOV;
    
    //valores de estados
    public static final int NO_MOV = 0;
    
    private boolean enAtaque = false;  // Estado para saber si el jugador está atacando
    public boolean enElAire = false;
    private boolean enSalto = false;  // Declaramos la variable 'enSalto'        
    
    // Valores de salto y caída
    private int velocidadY = 0;
    private final int GRAVEDAD = 1;
    private final int FUERZA_SALTO = -18;
    private final int VELOCIDAD_MAXIMA_CAIDA = 10;
    private boolean puedeBajar = false; // Indica si el jugador puede bajar por la plataforma

    // Variables de animación
    private int frameActual = 0;
    private int frameContador = 0;
    
    // Velocidades de animación para cada set
    private int velocidadAnimCorrerDerecha = 4; // Velocidad de correr hacia la derecha
    private int velocidadAnimCorrerIzquierda = 4; // Velocidad de correr hacia la izquierda
    private int velocidadAnimEstaticoDerecha = 10; // Velocidad de animación estática derecha
    private int velocidadAnimEstaticoIzquierda = 10; // Velocidad de animación estática izquierda
    private int velocidadAnimJump = 4;
    
    private int velocidadAnimAtaque = 4;  // Velocidad de la animación de ataque

    // Arrays para diferentes sets de animación
    private GreenfootImage[] framesCorrerDerecha;
    private GreenfootImage[] framesCorrerIzquierda;
    private GreenfootImage[] framesEstaticoDerecha;
    private GreenfootImage[] framesEstaticoIzquierda;
    private GreenfootImage[] framesJumpDerecha;
    private GreenfootImage[] framesJumpIzquierda;
    
    private GreenfootImage[] framesAtaqueDerecha;  // Sprites de ataque hacia la derecha
    private GreenfootImage[] framesAtaqueIzquierda;  // Sprites de ataque hacia la izquierda
    
    public Player() {
        // Cargar los sprite sheets
        GreenfootImage spriteSheetCorrerDerecha = new GreenfootImage("player_run_derecha.png");
        GreenfootImage spriteSheetCorrerIzquierda = new GreenfootImage("player_run_izquierda.png");
        
        GreenfootImage spriteSheetEstaticoDerecha = new GreenfootImage("player_estatico_derecha.png");
        GreenfootImage spriteSheetEstaticoIzquierda = new GreenfootImage("player_estatico_izquierda.png");
        
        GreenfootImage spriteSheetJumpDerecha = new GreenfootImage("player_jump_derecha.png");
        GreenfootImage spriteSheetJumpIzquierda = new GreenfootImage("player_jump_izquierda.png");
        
        GreenfootImage spriteSheetAtaqueDerecha = new GreenfootImage("player_ataque_derecha.png");
        GreenfootImage spriteSheetAtaqueIzquierda = new GreenfootImage("player_ataque_izquierda.png");
        
        
        
        // Inicializar animaciones usando el método genérico
        framesCorrerDerecha = cargarFramesDesdeSpriteSheet(spriteSheetCorrerDerecha, 9, 80, 80);
        framesCorrerIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetCorrerIzquierda, 9, 80, 80);
        
        framesEstaticoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoDerecha, 9, 80, 80);
        framesEstaticoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoIzquierda, 9, 80, 80);
        
        framesJumpDerecha = cargarFramesDesdeSpriteSheet(spriteSheetJumpDerecha, 3, 80, 80);
        framesJumpIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetJumpIzquierda, 3, 80, 80);
        
        framesAtaqueDerecha = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueDerecha, 5, 80, 80);
        framesAtaqueIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueIzquierda, 5, 80, 80);  
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
        // Detectar si el jugador presiona la tecla "J" para atacar
        if (Greenfoot.isKeyDown("j") && !enAtaque) {  // Activar ataque solo si no está ya atacando
            realizarAtaque();
        }
        
        // Verificar si el jugador está atacando
        if (enAtaque) {
            // El ataque en el aire debe permitir moverse en cualquier dirección
            if (Greenfoot.isKeyDown("a")) {
                setLocation(getX() - 4, getY());  // Moverse a la izquierda mientras ataca en el aire
                Movimientoanterior = MOV_IZQUIERDA;
            } 
            if (Greenfoot.isKeyDown("d")) {
                setLocation(getX() + 4, getY());  // Moverse a la derecha mientras ataca en el aire
                Movimientoanterior = MOV_DERECHA;
            }
            if (Greenfoot.isKeyDown("w")) {
                setLocation(getX(), getY() - 4);  // Moverse hacia arriba
            }
            if (Greenfoot.isKeyDown("s")) {
                setLocation(getX(), getY() + 4);  // Moverse hacia abajo
            }
        
            // Animar el ataque según la última dirección
            if (Movimientoanterior == MOV_IZQUIERDA) {
                animarUniversal(framesAtaqueIzquierda, velocidadAnimAtaque);  // Animación de ataque hacia la izquierda
            } else {
                animarUniversal(framesAtaqueDerecha, velocidadAnimAtaque);  // Animación de ataque hacia la derecha
            }
        
            // Detener el ataque después de completar la animación
            if (frameActual == framesAtaqueDerecha.length || frameActual == framesAtaqueIzquierda.length) {
                enAtaque = false;  // El ataque ha terminado
                frameActual = 0;  // Reiniciar el contador de frames
            }
            return;  // No permitir otras acciones mientras está atacando
        }
        
        int mov = movimientoPlayer();  // Obtener el valor de movimiento
        
        // Saltar solo si está en el suelo o sobre una plataforma
        if (mov == MOV_ARRIBA && (enElSuelo() || sobrePlataforma())) {
            velocidadY = FUERZA_SALTO;
            enSalto = true;  // Estado de salto activado
            puedeBajar = false;  // No puede bajar mientras está saltando
        }
        
        // Permitir que el jugador baje al presionar "S" mientras está sobre una plataforma
        if (mov == AGACHARSE && sobrePlataforma()) {
            puedeBajar = true;
        }
        
        // Aplicar la gravedad cuando esté en el aire
        if (enSalto || !enElSuelo() && !sobrePlataforma() || puedeBajar) {
            velocidadY += GRAVEDAD;
            if (velocidadY > VELOCIDAD_MAXIMA_CAIDA) {
                velocidadY = VELOCIDAD_MAXIMA_CAIDA;
            }
            
            // Movimiento vertical
            setLocation(getX(), getY() + velocidadY);
    
            // Normalizar la velocidad cuando se mueve en diagonal
            if (mov == MOV_IZQUIERDA && Greenfoot.isKeyDown("a")) {
                setLocation(getX() - 3, getY());  // Mantener velocidad horizontal normal
                animarUniversal(framesJumpIzquierda, velocidadAnimJump);
                Movimientoanterior = MOV_IZQUIERDA;
            } else if (mov == MOV_DERECHA && Greenfoot.isKeyDown("d")) {
                setLocation(getX() + 3, getY());  // Mantener velocidad horizontal normal
                animarUniversal(framesJumpDerecha, velocidadAnimJump);
                Movimientoanterior = MOV_DERECHA;
            }
        }
        
        // Si el jugador toca el suelo o una plataforma, detener la caída
        if (enElSuelo() || sobrePlataforma()) {
            velocidadY = 0;
            enSalto = false;  // Salto terminado
            puedeBajar = false;  // Restablecer la capacidad de bajar
        }
        
        // Mover horizontalmente si no estamos saltando
        if (!enSalto) {
            if (mov == MOV_IZQUIERDA) {
                setLocation(getX() - 4, getY());
                animarUniversal(framesCorrerIzquierda, velocidadAnimCorrerIzquierda);
                Movimientoanterior = MOV_IZQUIERDA;  // Actualizar la dirección solo si se está moviendo a la izquierda
            } else if (mov == MOV_DERECHA) {
                setLocation(getX() + 4, getY());
                animarUniversal(framesCorrerDerecha, velocidadAnimCorrerDerecha);
                Movimientoanterior = MOV_DERECHA;  // Actualizar la dirección solo si se está moviendo a la derecha
            } else if (mov == NO_MOV) {
                // Si no hay movimiento, regresa a la animación estática
                if (Movimientoanterior == MOV_IZQUIERDA) {
                    animarUniversal(framesEstaticoIzquierda, velocidadAnimEstaticoIzquierda);
                } else if (Movimientoanterior == MOV_DERECHA) {
                    animarUniversal(framesEstaticoDerecha, velocidadAnimEstaticoDerecha);
                }
            }
        }
    }



    public int movimientoPlayer() {
        if (Greenfoot.isKeyDown("w")) {
            if (enElSuelo() || sobrePlataforma()) {
                return MOV_ARRIBA;
            }
        }
        if (Greenfoot.isKeyDown("s")) {
            return AGACHARSE;
        }
        if (Greenfoot.isKeyDown("a")) {
            return MOV_IZQUIERDA;
        }
        if (Greenfoot.isKeyDown("d")) {
            return MOV_DERECHA;
        }
        return NO_MOV;
    }

    public void realizarAtaque() {
    enAtaque = true;  // Activar el estado de ataque
    frameActual = 0;  // Reiniciar el contador de frames
    
        // Mostrar la animación de ataque según la última dirección de movimiento
        if (Movimientoanterior == MOV_IZQUIERDA) {
            animarUniversal(framesAtaqueIzquierda, velocidadAnimAtaque);  // Mostrar ataque a la izquierda
        } else if (Movimientoanterior == MOV_DERECHA) {
            animarUniversal(framesAtaqueDerecha, velocidadAnimAtaque);  // Mostrar ataque a la derecha
        }
    }

    
    // Método que comprueba si el jugador está en el suelo
    public boolean enElSuelo() {
        return getY() >= getWorld().getHeight() - 50;  // Ajusta este valor según el suelo de tu juego
    }

    // Método que detecta si el jugador está sobre una plataforma
    public boolean sobrePlataforma() {
        Actor plataforma = getOneObjectAtOffset(0, getImage().getHeight() / 2 + 5, Plataforma.class);
        if (plataforma != null) {
            // Alinear el jugador con la parte superior de la plataforma solo si no está agachado
            if (!puedeBajar) {
                setLocation(getX(), plataforma.getY() - plataforma.getImage().getHeight() / 2 - getImage().getHeight() / 2);
            }
            return true;
        }
        return false;
    }

    public void act() {
        mover();   // Llamar al método de movimiento y animación
    }
}
