import greenfoot.GreenfootImage;
import greenfoot.Greenfoot;
import greenfoot.Color;
import greenfoot.Actor;
import greenfoot.World;

public class Player extends Entidad {
    // Constantes de movimiento
    public static final int MOV_ARRIBA = -1;
    public static final int MOV_IZQUIERDA = -2;
    public static final int MOV_DERECHA = 2;
    public static final int AGACHARSE = -3;
    private int Movimientoanterior = NO_MOV;
    public static final int NO_MOV = 0;

    // Estados del jugador
    private boolean enAtaque = false;
    public boolean enElAire = false;
    private boolean enSalto = false;

    // Limitar ataques aéreos y enfriamiento
    private int limiteAtaquesAire = 2;
    private int ataquesAireRestantes = limiteAtaquesAire;
    private int cooldownAtaque = 30;
    private int contadorAtaque = 0;

    private int cooldownSalto = 10;
    private int contadorSalto = 0;

    // Variables de gravedad
    private int velocidadY = 0;
    private final int GRAVEDAD = 1;
    private final int FUERZA_SALTO = -16;
    private final int VELOCIDAD_MAXIMA_CAIDA = 10;
    private boolean puedeBajar = false;

    // Spritesheets para animaciones
    private GreenfootImage[] framesCorrerDerecha;
    private GreenfootImage[] framesCorrerIzquierda;
    private GreenfootImage[] framesEstaticoDerecha;
    private GreenfootImage[] framesEstaticoIzquierda;
    private GreenfootImage[] framesJumpDerecha;
    private GreenfootImage[] framesJumpIzquierda;
    private GreenfootImage[] framesAtaqueDerecha;
    private GreenfootImage[] framesAtaqueIzquierda;
    private GreenfootImage[] framesMuerteDerecha;  // **Animación de muerte**
    private GreenfootImage[] framesMuerteIzquierda;  // **Animación de muerte**

    // Hitbox de ataque
    private GreenfootImage hitboxAtaqueDerecha;
    private GreenfootImage hitboxAtaqueIzquierda;

    // Velocidad de animaciones
    private int velocidadAnimAtaque = 4; 
    private int velocidadAnimJump = 4; 
    private int velocidadAnimCorrerIzquierda = 4;  
    private int velocidadAnimCorrerDerecha = 4;    
    private int velocidadAnimEstaticoIzquierda = 10;  
    private int velocidadAnimEstaticoDerecha = 10;
    private int velocidadAnimMuerte = 8;  // **Velocidad de animación de muerte**

    public Player() {
        super(30);  // Vida inicial

        // Cargar los spritesheets
        GreenfootImage spriteSheetCorrerDerecha = new GreenfootImage("player_run_derecha.png");
        GreenfootImage spriteSheetCorrerIzquierda = new GreenfootImage("player_run_izquierda.png");
        GreenfootImage spriteSheetEstaticoDerecha = new GreenfootImage("player_estatico_derecha.png");
        GreenfootImage spriteSheetEstaticoIzquierda = new GreenfootImage("player_estatico_izquierda.png");
        GreenfootImage spriteSheetJumpDerecha = new GreenfootImage("player_jump_derecha.png");
        GreenfootImage spriteSheetJumpIzquierda = new GreenfootImage("player_jump_izquierda.png");
        GreenfootImage spriteSheetAtaqueDerecha = new GreenfootImage("player_ataque_derecha.png");
        GreenfootImage spriteSheetAtaqueIzquierda = new GreenfootImage("player_ataque_izquierda.png");
        GreenfootImage spriteSheetMuerteDerecha = new GreenfootImage("player_dead_derecha.png");  // **Spritesheet de muerte**
        GreenfootImage spriteSheetMuerteIzquierda = new GreenfootImage("player_dead_izquierda.png");  // **Spritesheet de muerte**

        // Cargar animaciones usando métodos de la clase Entidad
        framesCorrerDerecha = cargarFramesDesdeSpriteSheet(spriteSheetCorrerDerecha, 9, 80, 80);
        framesCorrerIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetCorrerIzquierda, 9, 80, 80);
        framesEstaticoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoDerecha, 9, 80, 80);
        framesEstaticoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEstaticoIzquierda, 9, 80, 80);
        framesJumpDerecha = cargarFramesDesdeSpriteSheet(spriteSheetJumpDerecha, 3, 80, 80);
        framesJumpIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetJumpIzquierda, 3, 80, 80);
        framesAtaqueDerecha = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueDerecha, 5, 80, 80);
        framesAtaqueIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueIzquierda, 5, 80, 80);
        framesMuerteDerecha = cargarFramesDesdeSpriteSheet(spriteSheetMuerteDerecha, 10, 80, 80);  // **Cargar frames de muerte**
        framesMuerteIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetMuerteIzquierda, 10, 80, 80);  // **Cargar frames de muerte**

        // Definir las hitboxes de ataque
        hitboxAtaqueDerecha = new GreenfootImage(40, 80);
        hitboxAtaqueIzquierda = new GreenfootImage(40, 80);

        hitboxAtaqueDerecha.setColor(new Color(0, 255, 0, 128));
        hitboxAtaqueDerecha.fillRect(0, 0, 40, 80);

        hitboxAtaqueIzquierda.setColor(new Color(0, 255, 0, 128));
        hitboxAtaqueIzquierda.fillRect(0, 0, 40, 80);

        setImage(framesEstaticoDerecha[0]);
    }

    // Método actEntidad() donde se implementa la lógica del jugador
    @Override
    protected void actEntidad() {
        if (enMuerte) {
            animarMuerte();  // **Animar la muerte si está muriendo**
            return;  // No realizar otras acciones
        }

        if (enAtaque) {
            gestionarAtaque();  // Gestionar la animación de ataque si está atacando
        } else {
            mover();  // Mover solo si no está en ataque
            //mostrarHitbox();  // Mostrar la hitbox de daño si es necesario
        }
    }

    // **Método para animar la muerte**
    @Override
    protected void animarMuerte() {
        if (Movimientoanterior == MOV_DERECHA || Movimientoanterior == NO_MOV) {
            animarUniversal(framesMuerteDerecha, velocidadAnimMuerte);
        } else {
            animarUniversal(framesMuerteIzquierda, velocidadAnimMuerte);
        }

        if (frameActual >= framesMuerteDerecha.length || frameActual >= framesMuerteIzquierda.length) {
            morir();  // Llamar al método morir al finalizar la animación
        }
    }

    // Mostrar la hitbox de ataque durante el ataque
    public void mostrarHitboxAtaque() {
        GreenfootImage imagenActual = new GreenfootImage(getImage());

        if (Movimientoanterior == MOV_DERECHA) {
            imagenActual.drawImage(hitboxAtaqueDerecha, getImage().getWidth(), 0);
        } else {
            imagenActual.drawImage(hitboxAtaqueIzquierda, -40, 0);
        }
        setImage(imagenActual);
    }

    // Detectar colisiones con la hitbox de ataque
    public void detectarColisionAtaque() {
        Actor enemigo = null;

        if (Movimientoanterior == MOV_DERECHA) {
            enemigo = getOneObjectAtOffset(40, 0, Esqueleto.class);
        } else if (Movimientoanterior == MOV_IZQUIERDA) {
            enemigo = getOneObjectAtOffset(-40, 0, Esqueleto.class);
        }

        if (enemigo != null && enemigo instanceof Esqueleto) {
            Esqueleto esqueleto = (Esqueleto) enemigo;
            esqueleto.recibirDaño(20);  // Infligir daño al Esqueleto
        }
    }

    // Realizar el ataque
    public void realizarAtaque() {
        enAtaque = true;
        frameActual = 0;

        if (Movimientoanterior == MOV_IZQUIERDA) {
            animarUniversal(framesAtaqueIzquierda, velocidadAnimAtaque);
        } else {
            animarUniversal(framesAtaqueDerecha, velocidadAnimAtaque);
        }

        mostrarHitboxAtaque();  // Mostrar la hitbox de ataque durante la animación
        detectarColisionAtaque();  // Detectar colisiones con enemigos
    }

    // Gestionar la animación de ataque y su finalización
    public void gestionarAtaque() {
        if (Movimientoanterior == MOV_IZQUIERDA) {
            animarUniversal(framesAtaqueIzquierda, velocidadAnimAtaque);
        } else {
            animarUniversal(framesAtaqueDerecha, velocidadAnimAtaque);
        }

        // Comprobar si la animación ha terminado
        if (frameActual >= framesAtaqueDerecha.length || frameActual >= framesAtaqueIzquierda.length) {
            enAtaque = false;  // Finalizar el estado de ataque
            frameActual = 0;   // Reiniciar el contador de frames
        }
    }

    // Movimiento y control general del jugador
    public void mover() {
        if (contadorAtaque > 0) contadorAtaque--;
        if (contadorSalto > 0) contadorSalto--;

        // Detectar ataque
        if (Greenfoot.isKeyDown("j") && !enAtaque && contadorAtaque == 0) {
            if (!enElSuelo() && ataquesAireRestantes > 0) {
                ataquesAireRestantes--;
                realizarAtaque();
                contadorAtaque = cooldownAtaque;
            } else if (enElSuelo()) {
                realizarAtaque();
                contadorAtaque = cooldownAtaque;
            }
        }

        if (enAtaque) {
            return;  // No permitir movimiento durante el ataque
        }

        int mov = movimientoPlayer();

        if (mov == MOV_ARRIBA && (enElSuelo() || sobrePlataforma()) && contadorSalto == 0) {
            velocidadY = FUERZA_SALTO;
            enSalto = true;
            puedeBajar = false;
            contadorSalto = cooldownSalto;
        }

        if (mov == AGACHARSE && sobrePlataforma()) {
            puedeBajar = true;
        }

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
            ataquesAireRestantes = limiteAtaquesAire;
            velocidadY = 0;
            enSalto = false;
            puedeBajar = false;
        }

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
                } else if (Movimientoanterior == MOV_DERECHA || Movimientoanterior == NO_MOV) {
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

    // **Método para obtener la vida (para el HUD)**
    public int getVida() {
        return vida;
    }

    @Override
    public void morir() {
        World world = getWorld();
        if (world instanceof Fase1) {
            Fase1 fase1 = (Fase1) world;
            fase1.mostrarGameOver();  // Mostrar el menú de Game Over
        }
        getWorld().removeObject(this);
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
}
