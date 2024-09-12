import greenfoot.*;

public class Esqueleto extends Player {
    // Propiedades específicas del Esqueleto
    private int velocidadX;  // Velocidad de movimiento horizontal del esqueleto
    private int velocidadY;  // Velocidad de caída personalizada para el esqueleto
    private int gravedadEsqueleto;  // Gravedad personalizada del esqueleto
    private int fuerzaSaltoEsqueleto;
    private int velocidadMaximaCaidaEsqueleto;
    private int frameActualEsqueleto = 0;
    private int frameContadorEsqueleto = 0;

    // Velocidades de animación para el esqueleto
    private int velocidadAnimEsqueletoDerecha = 5;
    private int velocidadAnimEsqueletoIzquierda = 5;

    // Arrays para las animaciones del esqueleto
    private GreenfootImage[] framesEsqueletoDerecha;
    private GreenfootImage[] framesEsqueletoIzquierda;

    // Propiedades para el sistema de rondas
    private int tiempoMovimiento = 100; // Tiempo que caminará en una dirección
    private int contadorMovimiento = 0; // Contador para medir el tiempo de movimiento
    private boolean moviendoDerecha = true; // Controla la dirección del movimiento

    public Esqueleto() {
        // Asignar valores propios del Esqueleto
        this.velocidadX = 2;  // Velocidad horizontal del esqueleto
        this.velocidadY = 0;  // Inicialmente la velocidad en Y es 0
        this.gravedadEsqueleto = 1;  // Gravedad personalizada
        this.fuerzaSaltoEsqueleto = -15;  // Fuerza de salto personalizada
        this.velocidadMaximaCaidaEsqueleto = 8;  // Velocidad máxima de caída personalizada

        // Cargar las animaciones del esqueleto
        GreenfootImage spriteSheetEsqueletoDerecha = new GreenfootImage("esqueleto_run_derecha.png");
        GreenfootImage spriteSheetEsqueletoIzquierda = new GreenfootImage("esqueleto_run_izquierda.png");

        // Suponiendo 4 frames de 80x80
        framesEsqueletoDerecha = cargarFramesDesdeSpriteSheet(spriteSheetEsqueletoDerecha, 4, 80, 80);
        framesEsqueletoIzquierda = cargarFramesDesdeSpriteSheet(spriteSheetEsqueletoIzquierda, 4, 80, 80);
        
        setImage(framesEsqueletoDerecha[0]);  // Imagen inicial del esqueleto
    }

    @Override
    public void act() {
        moverEnemigo();  // Llamada a la lógica de movimiento del esqueleto
    }

    // Método para mover al esqueleto
    public void moverEnemigo() {
        // Incrementar el contador de movimiento
        contadorMovimiento++;

        // Movimiento del esqueleto en rondas
        if (contadorMovimiento <= tiempoMovimiento) {
            if (moviendoDerecha) {
                setLocation(getX() + velocidadX, getY());
                animarEsqueleto(framesEsqueletoDerecha, velocidadAnimEsqueletoDerecha);
            } else {
                setLocation(getX() - velocidadX, getY());
                animarEsqueleto(framesEsqueletoIzquierda, velocidadAnimEsqueletoIzquierda);
            }
        } else {
            // Cambiar de dirección cuando el tiempo se acabe
            moviendoDerecha = !moviendoDerecha;
            contadorMovimiento = 0; // Reiniciar el contador para la nueva dirección
        }

        // Aplicar la gravedad del esqueleto si no está en el suelo o sobre una plataforma
        if (!enElSuelo() && !sobrePlataforma()) {
            velocidadY += gravedadEsqueleto;
            if (velocidadY > velocidadMaximaCaidaEsqueleto) {
                velocidadY = velocidadMaximaCaidaEsqueleto;
            }
            setLocation(getX(), getY() + velocidadY);  // Movimiento vertical debido a la gravedad
        }

        // Restablecer la velocidad de caída si está en el suelo o sobre una plataforma
        if (enElSuelo() || sobrePlataforma()) {
            velocidadY = 0;
        }
    }

    // Animar el esqueleto
    public void animarEsqueleto(GreenfootImage[] frames, int velocidadAnimacion) {
        frameContadorEsqueleto++;
        if (frameContadorEsqueleto % velocidadAnimacion == 0) {
            if (frameActualEsqueleto < frames.length) {
                setImage(frames[frameActualEsqueleto]);
                frameActualEsqueleto++;
            } else {
                frameActualEsqueleto = 0;  // Reiniciar la animación
            }
        }
    }

    // Método para cargar los frames desde un sprite sheet (heredado de Player)
    @Override
    public GreenfootImage[] cargarFramesDesdeSpriteSheet(GreenfootImage sheet, int numFrames, int frameAncho, int frameAlto) {
        return super.cargarFramesDesdeSpriteSheet(sheet, numFrames, frameAncho, frameAlto);
    }
}
