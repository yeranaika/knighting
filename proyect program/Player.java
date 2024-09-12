    import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
    import greenfoot.GreenfootImage;
    
    public class Player extends Actor{
        // Constantes de movimiento
        public static final int MOV_ARRIBA = -1;
        public static final int MOV_IZQUIERDA = -2;
        public static final int MOV_DERECHA = 2;
        public static final int AGACHARSE = -3;
    
        private int Movimientoanterior = NO_MOV;
        
        //valores de estados
        public static final int NO_MOV = 0;
        
        public boolean enElAire = false;
        public boolean enReposo = false;
        
    
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
        
        private int velocidadAnimJump = 4;
    
    
        // Arrays para diferentes sets de animación
        private GreenfootImage[] framesCorrerDerecha;
        private GreenfootImage[] framesCorrerIzquierda;
        private GreenfootImage[] framesEstaticoDerecha;
        private GreenfootImage[] framesEstaticoIzquierda;
        
        private GreenfootImage[] framesJumpDerecha;
        private GreenfootImage[] framesJumpIzquierda;
        
    public Player()
    {
        // Cargar los sprite sheets
        GreenfootImage spriteSheetCorrerDerecha = new GreenfootImage("player_run_derecha.png");
        GreenfootImage spriteSheetCorrerIzquierda = new GreenfootImage("player_run_izquierda.png");
        GreenfootImage spriteSheetEstaticoDerecha = new GreenfootImage("player_estatico_derecha.png");
        GreenfootImage spriteSheetEstaticoIzquierda = new GreenfootImage("player_estatico_izquierda.png");
        
        GreenfootImage spriteSheetJumpDerecha = new GreenfootImage("player_jump_derecha.png");
        GreenfootImage spriteSheetJumpIzquierda = new GreenfootImage("player_jump_izquierda.png");
        
        // Inicializar animaciones usando el método genérico
        framesCorrerDerecha = cargarFramesDesdeSpriteSheet(spriteSheetCorrerDerecha, 9, 80, 80);
        framesCorrerIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetCorrerIzquierda, 9, 80, 80);
        
        framesEstaticoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoDerecha, 9, 80, 80);
        framesEstaticoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoIzquierda, 9, 80, 80);
        
        framesJumpDerecha = cargarFramesDesdeSpriteSheet(spriteSheetJumpDerecha, 3, 80, 80);
        framesJumpIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetJumpIzquierda, 3, 80, 80);

        
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
    
        // Saltar solo si está en el suelo o sobre una plataforma
        if (mov == MOV_ARRIBA && (enElSuelo() || sobrePlataforma())) {
            velocidadY = FUERZA_SALTO;
            enElAire = true;  // El personaje está en el aire
        }
    
        // Aplicar la gravedad si estamos en el aire
        if (enElAire) {
            velocidadY += GRAVEDAD;
            if (velocidadY > VELOCIDAD_MAXIMA_CAIDA) {
                velocidadY = VELOCIDAD_MAXIMA_CAIDA;
            }
            setLocation(getX(), getY() + velocidadY);
    
            // Asegurar que siempre se muestre la animación de salto mientras el personaje esté en el aire
            if (Movimientoanterior == MOV_IZQUIERDA || mov == MOV_IZQUIERDA) {
                animarUniversal(framesJumpIzquierda, velocidadAnimJump);
            } else if (Movimientoanterior == MOV_DERECHA || mov == MOV_DERECHA) {
                animarUniversal(framesJumpDerecha, velocidadAnimJump);
            }
    
            // Permitir movimiento horizontal en el aire, pero no cambiar la animación de salto
            if (mov == MOV_IZQUIERDA) {
                setLocation(getX() - 4, getY());  // Moverse a la izquierda en el aire
            } else if (mov == MOV_DERECHA) {
                setLocation(getX() + 4, getY());  // Moverse a la derecha en el aire
            }
        }
    
        // Si el jugador toca el suelo o una plataforma
        if (enElSuelo() || sobrePlataforma()) {
            velocidadY = 0;
            enElAire = false;  // El personaje ya no está en el aire
    
            // Si no se está moviendo, mostrar la animación estática
            if (mov == NO_MOV) {
                if (Movimientoanterior == MOV_IZQUIERDA) {
                    animarUniversal(framesEstaticoIzquierda, velocidadAnimEstaticoIzquierda);
                } else if (Movimientoanterior == MOV_DERECHA) {
                    animarUniversal(framesEstaticoDerecha, velocidadAnimEstaticoDerecha);
                }
            } else {
                // Si el jugador se mueve horizontalmente en el suelo
                if (mov == MOV_IZQUIERDA) {
                    setLocation(getX() - 4, getY());
                    animarUniversal(framesCorrerIzquierda, velocidadAnimCorrerIzquierda);
                } else if (mov == MOV_DERECHA) {
                    setLocation(getX() + 4, getY());
                    animarUniversal(framesCorrerDerecha, velocidadAnimCorrerDerecha);
                }
            }
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
