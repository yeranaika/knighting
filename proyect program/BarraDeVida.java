import greenfoot.*;

public class BarraDeVida extends Actor {
    private GreenfootImage[] heartFrames;  // Arreglo para almacenar los frames de los corazones
    private int maxHearts = 5;  // Número máximo de corazones
    private int heartWidth = 96;  // Ancho de cada frame (corazón) en el sprite
    private int heartHeight = 96;  // Alto de cada frame
    private int currentHearts;  // Corazones actuales
    private Player player;  // Referencia al jugador

    public BarraDeVida(Player player) {  // Constructor que recibe el jugador
        this.player = player;

        // Cargar los frames desde el sprite
        heartFrames = new GreenfootImage[9];  // 9 frames de corazones en total

        GreenfootImage spriteSheet = new GreenfootImage("BarraDeVida.png");
        for (int i = 0; i < 9; i++) {
            heartFrames[i] = new GreenfootImage(heartWidth, heartHeight);
            heartFrames[i].drawImage(spriteSheet, -i * heartWidth, 0);  // Extraer cada frame del sprite
        }

        currentHearts = maxHearts;  // Vida inicial igual a la cantidad máxima de corazones
        actualizarBarra();  // Mostrar los corazones inicialmente
    }

    // Método para actualizar la barra de vida según la vida del jugador
    public void actualizarBarra() {
        int vida = player.getVida();  // Obtener la vida del jugador
        currentHearts = vida;  // Actualizar los corazones actuales basados en la vida del jugador
        mostrarCorazones();
    }

    // Método para mostrar los corazones en pantalla
    private void mostrarCorazones() {
        GreenfootImage image = new GreenfootImage(maxHearts * heartWidth, heartHeight);  // Crear imagen de la barra de vida
        for (int i = 0; i < maxHearts; i++) {
            if (i < currentHearts) {
                image.drawImage(heartFrames[8], i * heartWidth, 0);  // Corazón lleno
            } else {
                image.drawImage(heartFrames[0], i * heartWidth, 0);  // Corazón vacío
            }
        }
        setImage(image);  // Establecer la imagen actual de la barra de vida
    }
}
