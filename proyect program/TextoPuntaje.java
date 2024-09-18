import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Font;

public class TextoPuntaje extends Actor {
    private int puntaje;
    private Font fuentePixel;

    public TextoPuntaje() {
        puntaje = 0;

        // Fuente estilo pixel art
        fuentePixel = new Font("Monospaced", Font.PLAIN, 40);  // Puedes cambiar esto a una fuente pixel art si tienes una
        actualizarTexto();
    }

    public void aumentarPuntaje(int puntos) {
        puntaje += puntos;
        actualizarTexto();
    }

    public void actualizarTexto() {
        GreenfootImage imagenTexto = new GreenfootImage("Puntaje: " + puntaje, 40, Color.WHITE, new Color(0, 0, 0, 0));
        setImage(imagenTexto);
    }
    
    public int getPuntaje() {
        return puntaje;
    }
}
