import greenfoot.*;

public class Entidad extends Actor {
    protected int vida;  // Vida de la entidad

    // Variables de animación
    protected int frameActual = 0;
    protected int frameContador = 0;
    
    protected int velocidadY = 0;  // Velocidad vertical inicial

    // Definir el tamaño de la hitbox
    protected int hitboxWidth = 30;
    protected int hitboxHeight = 50;
    protected GreenfootImage hitboxImage;

    public Entidad(int vidaInicial) {
        vida = vidaInicial;

        // Inicializar la hitbox
        hitboxImage = new GreenfootImage(hitboxWidth, hitboxHeight);
        hitboxImage.setColor(new Color(0, 255, 0, 128));  // Verde semi-transparente para visualización
        hitboxImage.fillRect(0, 0, hitboxWidth, hitboxHeight);
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
        Actor enemigo = getOneIntersectingObject(claseEnemigo);  // Verificar colisión con la clase pasada
        if (enemigo != null) {
            recibirDaño(daño);  // Restar vida en caso de colisión
        }
    }

    // Recibir daño
    public void recibirDaño(int cantidad) {
        vida -= cantidad;
        if (vida <= 0) {
            morir();
        }
    }

    // Método morir
    public void morir() {
        getWorld().removeObject(this);
    }
}
