import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Plataforma extends Actor {
    public Plataforma(int ancho, int alto, Color color) {
        // Crear una plataforma rectangular con las dimensiones proporcionadas
        GreenfootImage imagenPlataforma = new GreenfootImage(ancho, alto);
        
        // Establecer el color de la plataforma
        imagenPlataforma.setColor(color);
        
        // Dibujar el rectángulo de la plataforma
        imagenPlataforma.fillRect(0, 0, ancho, alto);
        
        // Ajustar la transparencia de la plataforma (0 es completamente transparente, 255 es opaco)
        imagenPlataforma.setTransparency(0);  // Ajusta el nivel de transparencia (por ejemplo, 128 es semi-transparente)
        
        // Asignar la imagen de la plataforma
        setImage(imagenPlataforma);
    }
}
