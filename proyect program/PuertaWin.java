import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class PuertaWin extends Actor {
    
    public PuertaWin() {
        // Establecer la imagen de la puerta (cargar la imagen de la puerta desde tus recursos)
        setImage("puerta.png");  
    }

    public void act() {
        // Verificar si el jugador está tocando la puerta
        if (isTouching(Player.class)) {
            // Cambiar de nivel o mostrar mensaje de victoria
            ((Fase1) getWorld()).mostrarVictoria();  
        }
    }
}
