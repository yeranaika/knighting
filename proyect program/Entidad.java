import greenfoot.*;
import java.util.List;

public abstract class Entidad extends Actor {
    protected int vida;  // Vida de la entidad

    // Variables de animación
    protected int frameActual = 0;
    protected int frameContador = 0;

    // Movimiento y física
    protected int velocidadY = 0;  // Velocidad vertical inicial
    protected int velocidadX = 2;  // Velocidad horizontal
    protected int rangoDeteccion = 200;  // Rango en el que detecta al jugador
    protected int distanciaAtaque = 50;  // Distancia para atacar
    protected int distanciaSegura = 45;  // Distancia mínima para detenerse
    protected boolean moviendoDerecha = true;  // Dirección inicial del enemigo

    // Patrullaje
    protected int puntoInicialX;
    protected int puntoFinalX;
    protected boolean enPatrullaje = true;

    // Ataque
    protected boolean enAtaque = false;
    protected int cooldownAtaque = 50;
    protected int contadorAtaque = 0;

    // Definir el tamaño de la hitbox
    protected int hitboxWidth = 30;
    protected int hitboxHeight = 50;
    protected GreenfootImage hitboxImage;

    // Variable para el estado de muerte
    protected boolean enMuerte = false;

    public Entidad(int vidaInicial, int puntoInicialX, int puntoFinalX) {
        vida = vidaInicial;
        this.puntoInicialX = puntoInicialX;
        this.puntoFinalX = puntoFinalX;

        // Inicializar la hitbox
        hitboxImage = new GreenfootImage(hitboxWidth, hitboxHeight);
        hitboxImage.setColor(new Color(0, 255, 0, 128));  // Verde semi-transparente para visualización
        hitboxImage.fillRect(0, 0, hitboxWidth, hitboxHeight);
    }

    @Override
    public void act() {
        Fase1 mundo = (Fase1) getWorld();
        if (mundo != null && mundo.isJuegoPausado() && !enMuerte) {
            return;  
        }
        actEntidad();  
    }

    protected abstract void actEntidad();

    // Método genérico para cargar frames de un sprite sheet
    public GreenfootImage[] cargarFramesDesdeSpriteSheet(GreenfootImage sheet, int numFrames, int frameAncho, int frameAlto) {
        GreenfootImage[] frames = new GreenfootImage[numFrames];
        for (int i = 0; i < numFrames; i++) {
            frames[i] = new GreenfootImage(frameAncho, frameAlto);
            frames[i].drawImage(sheet, -i * frameAncho, 0);  // Extraer la porción correspondiente
        }
        return frames;
    }

    // Método para animar
    public void animarUniversal(GreenfootImage[] frames, int velocidadAnimacion) {
        frameContador++;
        if (frameContador % velocidadAnimacion == 0) {
            if (frameActual < frames.length) {
                setImage(frames[frameActual]);
                frameActual++;
            } else {
                frameActual = 0;
            }
        }
    }

    // Aplicar gravedad
    public void aplicarGravedad() {
        if (!enElSuelo() && !sobrePlataforma()) {
            velocidadY += 1;  // Incremento debido a la gravedad
            if (velocidadY > 8) {
                velocidadY = 8;  // Limitar la velocidad de caída
            }
            setLocation(getX(), getY() + velocidadY);
        } else {
            velocidadY = 0;  // Resetear la velocidad cuando está en el suelo
        }
    }

    // Método para mostrar la hitbox
    public void mostrarHitbox() {
        GreenfootImage imagenConHitbox = new GreenfootImage(getImage());
        imagenConHitbox.drawImage(hitboxImage, (getImage().getWidth() - hitboxWidth) / 2, (getImage().getHeight() - hitboxHeight) / 2 + 14);
        setImage(imagenConHitbox);
    }

    // Método para detectar colisiones
    public void detectarColisiones(Class<? extends Entidad> claseEnemigo, int daño) {
        Actor enemigo = getOneIntersectingObject(claseEnemigo);  
        if (enemigo != null) {
            recibirDaño(daño);  // Restar vida en caso de colisión
        }
    }

    // Patrullaje
    public void moverEnemigo(GreenfootImage[] framesIzquierda, GreenfootImage[] framesDerecha) {
        enPatrullaje = true;
        if (moviendoDerecha) {
            if (getX() < puntoFinalX) {
                setLocation(getX() + velocidadX, getY());
                animarUniversal(framesDerecha, 5);
            } else {
                moviendoDerecha = false;
            }
        } else {
            if (getX() > puntoInicialX) {
                setLocation(getX() - velocidadX, getY());
                animarUniversal(framesIzquierda, 5);
            } else {
                moviendoDerecha = true;
            }
        }
    }

    // Seguir al jugador
    public void seguirJugador(Player jugador, GreenfootImage[] framesIzquierda, GreenfootImage[] framesDerecha) {
        int distancia = calcularDistanciaAlJugador(jugador);

        if (distancia <= rangoDeteccion && distancia > distanciaAtaque) {
            enPatrullaje = false;
            if (jugador.getX() > getX()) {
                moviendoDerecha = true;
                setLocation(getX() + velocidadX, getY());
                animarUniversal(framesDerecha, 5);
            } else {
                moviendoDerecha = false;
                setLocation(getX() - velocidadX, getY());
                animarUniversal(framesIzquierda, 5);
            }
        } else if (distancia <= distanciaAtaque) {
            atacarJugador(jugador);
        } else {
            moverEnemigo(framesIzquierda, framesDerecha);
        }
    }

    // Método para calcular la distancia al jugador
    public int calcularDistanciaAlJugador(Player jugador) {
        int dx = getX() - jugador.getX();
        int dy = getY() - jugador.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);  // Distancia euclidiana
    }
    // Atacar al jugador
    public void atacarJugador(Player jugador) {
            if (contadorAtaque > 0 || enAtaque || enMuerte) {
                return;
            }
    
            enAtaque = true;
            contadorAtaque = cooldownAtaque;
            frameActual = 0;
    }
    
    public void morir() {
        World world = getWorld();
        if (world instanceof Fase1) {
            Fase1 fase1 = (Fase1) world;
            fase1.eliminarEnemigo(this);  
        }
        getWorld().removeObject(this);  
    }


    // Recibir daño
    public void recibirDaño(int cantidad) {
        if (enMuerte) return;

        vida -= cantidad;
        if (vida <= 0) {
            enMuerte = true;
            frameActual = 0;
        }
    }

    protected abstract void animarMuerte();

    public boolean enElSuelo() {
        return getY() >= getWorld().getHeight() - 50; 
    }

    public boolean sobrePlataforma() {
        Actor plataforma = getOneObjectAtOffset(0, getImage().getHeight() / 2 + 5, Plataforma.class);
        if (plataforma != null) {
            setLocation(getX(), plataforma.getY() - plataforma.getImage().getHeight() / 2 - getImage().getHeight() / 2);
            return true;
        }
        return false;
    }
}
