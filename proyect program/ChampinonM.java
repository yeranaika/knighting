import greenfoot.GreenfootImage;

public class ChampinonM extends Entidad {
    private GreenfootImage[] framesDerecha;
    private GreenfootImage[] framesIzquierda;
    private GreenfootImage[] framesAtaqueDerecha;
    private GreenfootImage[] framesAtaqueIzquierda;
    private GreenfootImage[] framesMuerteDerecha;
    private GreenfootImage[] framesMuerteIzquierda;

    public ChampinonM(int puntoInicialX, int puntoFinalX) {
        super(30, puntoInicialX, puntoFinalX);  // Vida inicial y puntos de patrullaje

        // Cargar los spritesheets
        GreenfootImage spriteSheetDerecha = new GreenfootImage("champinonM_run_derecha.png");
        GreenfootImage spriteSheetIzquierda = new GreenfootImage("champinonM_run_izquierda.png");
        GreenfootImage spriteSheetAtaqueDerecha = new GreenfootImage("champinonM_ataque_derecha.png");
        GreenfootImage spriteSheetAtaqueIzquierda = new GreenfootImage("champinonM_ataque_izquierda.png");
        GreenfootImage spriteSheetMuerteDerecha = new GreenfootImage("champinonM_muerte_derecha.png");
        GreenfootImage spriteSheetMuerteIzquierda = new GreenfootImage("champinonM_muerte_izquierda.png");

        // Dividir los sprites en frames
        framesDerecha = cargarFramesDesdeSpriteSheet(spriteSheetDerecha, 8, 80, 80);
        framesIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetIzquierda, 8, 80, 80);
        framesAtaqueDerecha = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueDerecha, 8, 80, 80);
        framesAtaqueIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueIzquierda, 8, 80, 80);
        framesMuerteDerecha = cargarFramesDesdeSpriteSheet(spriteSheetMuerteDerecha, 4, 80, 80);
        framesMuerteIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetMuerteIzquierda, 4, 80, 80);

        // Configurar la imagen inicial
        setImage(framesDerecha[0]);
    }

    @Override
    protected void actEntidad() {
        if (enMuerte) {
            animarMuerte();  // Animar la muerte si el enemigo está en estado de muerte
            return;
        }
    
        // Aplicar la gravedad
        aplicarGravedad();
    
        // Cooldown de ataque
        if (contadorAtaque > 0) {
            contadorAtaque--;
        }
    
        // Obtener al jugador
        Player jugador = (Player) getWorld().getObjects(Player.class).get(0);
    
        // Si está en ataque, realizar la animación de ataque
        if (enAtaque) {
            animarAtaque(jugador);
        } else {
            seguirJugador(jugador, framesIzquierda, framesDerecha);  // Seguir al jugador si no está atacando
        }
    }

    // Método para realizar el ataque
    @Override
    public void atacarJugador(Player jugador) {
        if (contadorAtaque > 0 || enAtaque || enMuerte) {
            return;  
        }

        enAtaque = true;  
        contadorAtaque = cooldownAtaque;  
        frameActual = 0;  
    }

    // Método para animar el ataque
    public void animarAtaque(Player jugador) {
        // Animar según la dirección
        if (moviendoDerecha) {
            animarUniversal(framesAtaqueDerecha, 5);  
        } else {
            animarUniversal(framesAtaqueIzquierda, 5);
        }

        // Comprobar si la animación ha llegado al final
        if (frameActual >= framesAtaqueDerecha.length || frameActual >= framesAtaqueIzquierda.length) {
            // Fin de la animación de ataque, aplicar daño si está cerca del jugador
            if (calcularDistanciaAlJugador(jugador) <= distanciaAtaque) {
                jugador.recibirDaño(10);  
            }

            enAtaque = false;  
            frameActual = 0; 
        }
    }

    @Override
    protected void animarMuerte() {
        // Animar la muerte del champiñón
        if (moviendoDerecha) {
            animarUniversal(framesMuerteDerecha, 8);  // Velocidad de animación de muerte
        } else {
            animarUniversal(framesMuerteIzquierda, 8);
        }

        // Una vez que la animación de muerte ha terminado
        if (frameActual >= framesMuerteDerecha.length || frameActual >= framesMuerteIzquierda.length) {
            morir();  
        }
    }
    
    @Override
    public void recibirDaño(int cantidad) {
        if (enMuerte) return;  // No recibir daño si ya está en estado de muerte
    
        vida -= cantidad;  
        if (vida <= 0) {
            enMuerte = true;  
            frameActual = 0;  
    
            // Otorgar puntos al jugador cuando el ChampiñonM muere
            Fase1 mundo = (Fase1) getWorld();
            if (mundo != null) {
                mundo.aumentarPuntaje(50);  
            }
        }
    }
}
