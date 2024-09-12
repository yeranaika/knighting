import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Player extends Actor {
    // Constantes de movimiento
    public static final int MOV_ARRIBA = -1;
    public static final int MOV_IZQUIERDA = -2;
    public static final int MOV_DERECHA = 2;
    public static final int AGACHARSE = -3;

    private int Movimientoanterior = NO_MOV;

    // Valores de estados
    public static final int NO_MOV = 0;

    private boolean enAtaque = false;  // Estado para saber si el jugador está atacando
    public boolean enElAire = false;
    private boolean enSalto = false;  // Declaramos la variable 'enSalto'

    // Limitar ataques aéreos y enfriamiento
    private int limiteAtaquesAire = 2;  // Número de ataques permitidos en el aire
    private int ataquesAireRestantes = limiteAtaquesAire;
    private int cooldownAtaque = 30;  // Duración del enfriamiento del ataque
    private int contadorAtaque = 0;   // Contador para gestionar el enfriamiento del ataque

    // Enfriamiento de salto
    private int cooldownSalto = 10;  // Enfriamiento del salto
    private int contadorSalto = 0;  // Contador para gestionar el enfriamiento del salto

    // Valores de salto y caída
    private int velocidadY = 0;
    private final int GRAVEDAD = 1;
    private final int FUERZA_SALTO = -16;
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
        if (contadorAtaque > 0) contadorAtaque--;  // Reducir el contador de enfriamiento de ataque
        if (contadorSalto > 0) contadorSalto--;    // Reducir el contador de enfriamiento de salto

        // Detectar si el jugador presiona la tecla "J" para atacar
        if (Greenfoot.isKeyDown("j") && !enAtaque && contadorAtaque == 0) {  // Solo atacar si no está en enfriamiento
            if (!enElSuelo() && ataquesAireRestantes > 0) {  // Ataque en el aire limitado
                ataquesAireRestantes--;
                realizarAtaque();
                contadorAtaque = cooldownAtaque;  // Iniciar enfriamiento de ataque
            } else if (enElSuelo()) {  // Ataque en el suelo
                realizarAtaque();
                contadorAtaque = cooldownAtaque;  // Iniciar enfriamiento de ataque
            }
        }

        // Verificar si el jugador está atacando
        if (enAtaque) {
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
        if (mov == MOV_ARRIBA && (enElSuelo() || sobrePlataforma()) && contadorSalto == 0) {
            velocidadY = FUERZA_SALTO;
            enSalto = true;  // Estado de salto activado
            puedeBajar = false;  // No puede bajar mientras está saltando
            contadorSalto = cooldownSalto;  // Iniciar enfriamiento del salto
        }

        // Permitir que el jugador baje al presionar "S" mientras está sobre una plataforma
        if (mov == AGACHARSE && sobrePlataforma()) {
            puedeBajar = true;
        }

        // Aplicar la gravedad cuando esté en el aire
        if (enSalto || (!enElSuelo() && !sobrePlataforma()) || puedeBajar) {
            velocidadY += GRAVEDAD;
            if (velocidadY > VELOCIDAD_MAXIMA_CAIDA) {
                velocidadY = VELOCIDAD_MAXIMA_CAIDA;
            }
            setLocation(getX(), getY() + velocidadY);

            if (mov == MOV_IZQUIERDA) {
                setLocation(getX() - 4, getY());
                animarUniversal(framesJumpIzquierda, velocidadAnimJump);
                Movimientoanterior = MOV_IZQUIERDA;
            } else if (mov == MOV_DERECHA) {
                setLocation(getX() + 4, getY());
                animarUniversal(framesJumpDerecha, velocidadAnimJump);
                Movimientoanterior = MOV_DERECHA;
            }
        }

        if (enElSuelo() || sobrePlataforma()) {
            ataquesAireRestantes = limiteAtaquesAire;  // Restablecer el límite de ataques aéreos
            velocidadY = 0;
            enSalto = false;  // Salto terminado
            puedeBajar = false;  // Restablecer la capacidad de bajar
        }

        // Mover horizontalmente si no estamos saltando
        if (!enSalto) {
            if (mov == MOV_IZQUIERDA) {
                setLocation(getX() - 4, getY());
                animarUniversal(framesCorrerIzquierda, velocidadAnimCorrerIzquierda);
                Movimientoanterior = MOV_IZQUIERDA;
            } else if (mov == MOV_DERECHA) {
                setLocation(getX() + 4, getY());
                animarUniversal(framesCorrerDerecha, velocidadAnimCorrerDerecha);
                Movimientoanterior = MOV_DERECHA;
            } else if (mov == NO_MOV) {
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
        enAtaque = true;
        frameActual = 0;

        if (Movimientoanterior == MOV_IZQUIERDA) {
            animarUniversal(framesAtaqueIzquierda, velocidadAnimAtaque);
        } else if (Movimientoanterior == MOV_DERECHA) {
            animarUniversal(framesAtaqueDerecha, velocidadAnimAtaque);
        }
    }

    public boolean enElSuelo() {
        return getY() >= getWorld().getHeight() - 50;
    }

    public boolean sobrePlataforma() {
        Actor plataforma = getOneObjectAtOffset(0, getImage().getHeight() / 2 + 5, Plataforma.class);
        if (plataforma != null) {
            if (!puedeBajar) {
                setLocation(getX(), plataforma.getY() - plataforma.getImage().getHeight() / 2 - getImage().getHeight() / 2);
            }
            return true;
        }
        return false;
    }

    public void act() {
        mover();
    }
}
