import greenfoot.*;

public class BarraDeVida extends Actor {
    private GreenfootImage[] heartFrames;  
    private int heartWidth = 96;  
    private int heartHeight = 96;  
    private Player player;  
    private int numFrames = 9;  

    public BarraDeVida(Player player) {
        this.player = player;  // Asignar el jugador

        // Cargar los frames desde el sprite
        heartFrames = new GreenfootImage[numFrames];
        GreenfootImage spriteSheet = new GreenfootImage("BarraDeVida.png");

        // Extraer cada frame del sprite y agrandar el tamaño
        for (int i = 0; i < numFrames; i++) {
            heartFrames[i] = new GreenfootImage(heartWidth, heartHeight);
            heartFrames[i].drawImage(spriteSheet, -i * heartWidth, 0);  // Extraer cada frame
            heartFrames[i].scale(heartWidth * 5/3, heartHeight * 5/3);  // Escalar para agrandar el corazón
        }

        mostrarCorazon(player.getVida());  // Inicialmente mostrar el corazón completo
    }

    // Método para actualizar la barra de vida según la vida del jugador
    public void actualizarBarra() {
        int vida = player.getVida();  
        mostrarCorazon(vida);  
    }

    // Mostrar el corazón animado en pantalla según la vida del jugador
    private void mostrarCorazon(int vida) {
        int vidaTotal = 30;  
        int frameIndex = (vida * (numFrames - 1)) / vidaTotal;  
        setImage(heartFrames[frameIndex]);  
    }
}
