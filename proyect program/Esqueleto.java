import greenfoot.*;
import java.util.List;

public class Esqueleto extends Entidad {
    private int velocidadX = 2;  // Velocidad horizontal del esqueleto
    private int velocidadY = 0;  // Velocidad vertical para manejar la gravedad

    private boolean enAtaque = false;  // Estado de ataque

    private GreenfootImage[] framesEsqueletoDerecha;
    private GreenfootImage[] framesEsqueletoIzquierda;
    private GreenfootImage[] framesAtaqueDerecha;
    private GreenfootImage[] framesAtaqueIzquierda;
    private GreenfootImage[] framesMuerteDerecha;
    private GreenfootImage[] framesMuerteIzquierda;

    private int rangoDeteccion = 200;  // Rango en el que el esqueleto detecta al jugador
    private int distanciaAtaque = 50;  // Distancia mínima para que el esqueleto ataque
    private int distanciaSegura = 45;  // Distancia mínima para que el esqueleto se detenga antes de atacar

    private int contadorAtaque = 0;  // Contador para gestionar el enfriamiento de ataques
    private int cooldownAtaque = 50; // Tiempo de espera entre ataques

    private final int GRAVEDAD = 1;  // Gravedad que afecta al esqueleto
    private final int VELOCIDAD_MAXIMA_CAIDA = 10;  // Velocidad máxima de caída

    // Variables para patrullaje
    private int puntoInicialX;  // Punto de inicio del patrullaje
    private int puntoFinalX;    // Punto final del patrullaje
    private boolean enPatrullaje = false; // Estado de patrullaje
    private boolean moviendoDerecha = true;

    // Variable para ajustar la velocidad de animación de muerte
    private int velocidadAnimMuerte = 10;  // Puedes ajustar este valor

    // Constructor
    public Esqueleto(int puntoInicialX, int puntoFinalX) {
        super(20);  // Vida inicial

        // Guardar los puntos de patrullaje
        this.puntoInicialX = puntoInicialX;
        this.puntoFinalX = puntoFinalX;

        // Cargar las animaciones
        GreenfootImage spriteSheetEsqueletoDerecha = new GreenfootImage("esqueleto_run_derecha.png");
        GreenfootImage spriteSheetEsqueletoIzquierda = new GreenfootImage("esqueleto_run_izquierda.png");
        GreenfootImage spriteSheetAtaqueDerecha = new GreenfootImage("esqueleto_ataque_derecha.png");
        GreenfootImage spriteSheetAtaqueIzquierda = new GreenfootImage("esqueleto_ataque_izquierda.png");
        GreenfootImage spriteSheetMuerteDerecha = new GreenfootImage("esqueleto_muerte_derecha.png");
        GreenfootImage spriteSheetMuerteIzquierda = new GreenfootImage("esqueleto_muerte_izquierda.png");

        // Cargar frames de animación
        framesEsqueletoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEsqueletoDerecha, 4, 80, 80); // Caminar tiene 4 frames
        framesEsqueletoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEsqueletoIzquierda, 4, 80, 80);
        framesAtaqueDerecha = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueDerecha, 8, 80, 80); // Ataque tiene 8 frames
        framesAtaqueIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetAtaqueIzquierda, 8, 80, 80);
        framesMuerteDerecha = cargarFramesDesdeSpriteSheet(spriteSheetMuerteDerecha, 4, 80, 80); // Muerte tiene 4 frames
        framesMuerteIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetMuerteIzquierda, 4, 80, 80);

        setImage(framesEsqueletoDerecha[0]);
    }

    @Override
    protected void actEntidad() {
        if (enMuerte) {
            animarMuerte();  // Animar la muerte si está muriendo
            return;
        }

        if (contadorAtaque > 0) contadorAtaque--;  // Cooldown de ataque

        aplicarGravedad();  // Aplicar gravedad

        // Obtener la lista de jugadores
        List<Player> jugadores = getWorld().getObjects(Player.class);
        if (!jugadores.isEmpty()) {
            Player jugador = jugadores.get(0);  // Obtener al jugador

            if (!enAtaque) {
                seguirJugador(jugador);  // El esqueleto seguirá al jugador si está en rango
            } else {
                animarAtaque();  // Realizar la animación de ataque
            }
        } else {
            // Si no hay jugador, el esqueleto puede patrullar o quedarse quieto
            moverEsqueleto();  // Continuar patrullando
        }
    }

    // Resto de métodos sin cambios...

    // Método para calcular la distancia al jugador
    private int calcularDistanciaAlJugador(Player jugador) {
        int dx = getX() - jugador.getX();
        int dy = getY() - jugador.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);  // Distancia euclidiana
    }

    // Método para que el Esqueleto siga al jugador si está en el rango
    public void seguirJugador(Player jugador) {
        int distancia = calcularDistanciaAlJugador(jugador);

        if (distancia <= rangoDeteccion && distancia > distanciaAtaque) {
            enPatrullaje = false; // Desactivar patrullaje si detecta al jugador

            if (jugador.getX() > getX()) {
                moviendoDerecha = true;
                setLocation(getX() + velocidadX, getY());  // Mover hacia la derecha
                animarUniversal(framesEsqueletoDerecha, 5);
            } else {
                moviendoDerecha = false;
                setLocation(getX() - velocidadX, getY());  // Mover hacia la izquierda
                animarUniversal(framesEsqueletoIzquierda, 5);
            }
        } else if (distancia <= distanciaAtaque) {
            atacarJugador(jugador);  // Intentar atacar al jugador si está en distancia de ataque
        } else {
            moverEsqueleto();  // Comportamiento de patrullaje si el jugador está fuera del rango
        }
    }

    // Método para mover al esqueleto de manera predeterminada (patrullaje)
    public void moverEsqueleto() {
        enPatrullaje = true;  // Activar patrullaje si no detecta al jugador

        if (moviendoDerecha) {
            if (getX() < puntoFinalX) {
                setLocation(getX() + velocidadX, getY());
                animarUniversal(framesEsqueletoDerecha, 5);
            } else {
                moviendoDerecha = false;  // Cambiar dirección al llegar al extremo
            }
        } else {
            if (getX() > puntoInicialX) {
                setLocation(getX() - velocidadX, getY());
                animarUniversal(framesEsqueletoIzquierda, 5);
            } else {
                moviendoDerecha = true;  // Cambiar dirección al llegar al extremo
            }
        }
    }

    // Método para atacar al jugador
    public void atacarJugador(Player jugador) {
        if (contadorAtaque > 0 || enAtaque || enMuerte) {
            return;  // Si el ataque está en cooldown o está muriendo, no hacer nada
        }

        enAtaque = true;  // El esqueleto está atacando
        contadorAtaque = cooldownAtaque;  // Iniciar cooldown de ataque
        frameActual = 0;  // Reiniciar animación de ataque
    }

    // Método para realizar la animación de ataque
    public void animarAtaque() {
        if (enMuerte) return;  // No atacar si está muriendo

        if (moviendoDerecha) {
            animarUniversal(framesAtaqueDerecha, 5);
        } else {
            animarUniversal(framesAtaqueIzquierda, 5);
        }

        if (frameActual >= framesAtaqueDerecha.length || frameActual >= framesAtaqueIzquierda.length) {
            // Fin de la animación de ataque, aplicar daño si está cerca del jugador
            Player jugador = (Player) getOneIntersectingObject(Player.class);
            if (jugador != null) {
                jugador.recibirDaño(10);  // El esqueleto inflige daño al jugador
            }

            enAtaque = false;  // Termina el ataque
            frameActual = 0;  // Reiniciar el contador de frames
        }
    }

    // Método para realizar la animación de muerte
    @Override
    protected void animarMuerte() {
        if (moviendoDerecha) {
            animarUniversal(framesMuerteDerecha, velocidadAnimMuerte);
        } else {
            animarUniversal(framesMuerteIzquierda, velocidadAnimMuerte);
        }

        if (frameActual >= framesMuerteDerecha.length || frameActual >= framesMuerteIzquierda.length) {
            morir();  // Llamar al método morir al finalizar la animación
        }
    }

    // Método para aplicar gravedad al esqueleto
    public void aplicarGravedad() {
        if (!enElSuelo() && !sobrePlataforma()) {
            velocidadY += GRAVEDAD;
            if (velocidadY > VELOCIDAD_MAXIMA_CAIDA) {
                velocidadY = VELOCIDAD_MAXIMA_CAIDA;
            }
            setLocation(getX(), getY() + velocidadY);
        }

        // Restablecer la velocidad vertical si está en el suelo o sobre una plataforma
        if (enElSuelo() || sobrePlataforma()) {
            velocidadY = 0;
        }
    }

    // Sobrescribir el método recibirDaño para manejar la muerte del esqueleto
    @Override
    public void recibirDaño(int cantidad) {
        if (enMuerte) return;

        vida -= cantidad;
        if (vida <= 0 && !enMuerte) {
            enMuerte = true;
            frameActual = 0;  // Reiniciar animación de muerte
        }
    }
}
