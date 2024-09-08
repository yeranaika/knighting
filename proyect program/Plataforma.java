import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Plataforma extends Actor
{
    public Plataforma() {
        // Crear una imagen invisible de 200 píxeles de ancho y 20 píxeles de alto
        GreenfootImage imagenPlataforma = new GreenfootImage(200, 20);  // Ajusta el tamaño de la plataforma
        imagenPlataforma.setTransparency(0);  // Hacer la imagen completamente invisible
    }
}
