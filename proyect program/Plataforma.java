import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Plataforma extends Actor
{
    public Plataforma() {
        // Crear una plataforma rectangular de 200 píxeles de ancho y 20 píxeles de alto con color visible
        GreenfootImage imagenPlataforma = new GreenfootImage(480, 20);  // Ajusta el tamaño de la plataforma
        imagenPlataforma.setTransparency(0);  // Establecer la transparencia al 100% (0 es completamente transparente)        imagenPlataforma.fillRect(0, 0, 520, 20);  // Dibujar el rectángulo
        
        setImage(imagenPlataforma);  // Asignar la imagen de la plataforma
    }
}
