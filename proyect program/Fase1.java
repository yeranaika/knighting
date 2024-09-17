import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Fase1 extends World {
    private boolean juegoPausado = false;  // Controla si el juego está pausado o no
    private MenuPausa menuPausa;  // Referencia al menú de pausa
    private MenuGameOver menuGameOver; // Referencia al menú de Game Over
    private BarraDeVida barraDeVida;  // Instancia de la barra de vida
    private Player player;  // Referencia al jugador

    public Fase1() {
        // Tamaño del mapa
        super(1200, 900, 1);
        prepararFase();  // Método para configurar el nivel
    }

    private void prepararFase() {
        // PLATAFORMAS FLOTANTES
        Plataforma plataformaFLO1 = new Plataforma(485, 20, Color.RED, 1);
        addObject(plataformaFLO1, 625, 300);

        Plataforma plataformaFLO2 = new Plataforma(130, 20, Color.RED, 2);
        addObject(plataformaFLO2, 400, 485);

        Plataforma plataformaFLO3 = new Plataforma(130, 20, Color.RED, 3);
        addObject(plataformaFLO3, 250, 560);

        Plataforma plataformaFLO4 = new Plataforma(130, 35, Color.YELLOW, 4);
        addObject(plataformaFLO4, 520, 390);

        Plataforma plataformaFLO5 = new Plataforma(370, 40, Color.YELLOW, 5);
        addObject(plataformaFLO5, 680, 420);

        Plataforma plataformaFLO6 = new Plataforma(280, 90, Color.GREEN, 6);
        addObject(plataformaFLO6, 725, 355);

        // PLATAFORMAS DE PISO
        Plataforma plataforma2 = new Plataforma(873, 20, Color.BLUE, 7);
        addObject(plataforma2, 150, 682);  

        Plataforma plataforma3 = new Plataforma(620, 20, Color.BLUE, 8);
        addObject(plataforma3, 890, 701);

        // Crear y agregar el jugador
        player = new Player();
        addObject(player, 400, 250);

        // Crear y agregar la barra de vida
        barraDeVida = new BarraDeVida(player);  // Pasar la referencia del jugador
        addObject(barraDeVida, getWidth() - 150, 50);  // Posicionar la barra de vida en la esquina superior derecha

        // Agregar enemigos
        Esqueleto esqueleto = new Esqueleto(100, 200);
        addObject(esqueleto, 100, 250);

        Esqueleto esqueleto2 = new Esqueleto(800, 900);
        addObject(esqueleto2, 600, 250);
    }

    public void act() {
        if (!juegoPausado) {
            String key = Greenfoot.getKey();
            if (key != null && key.equals("escape")) {
                pausarJuego();
            }
        }
    
        // Actualizar la barra de vida del jugador
        barraDeVida.actualizarBarra();  // Sin argumentos
    }


    // Método para pausar el juego
    private void pausarJuego() {
        juegoPausado = true;  // Cambiar el estado del juego a pausado
        menuPausa = new MenuPausa();  // Crear el menú de pausa
        addObject(menuPausa, getWidth() / 2, getHeight() / 2);  // Agregar el menú de pausa al centro del mundo
    }

    // Método para reanudar el juego desde el menú de pausa
    public void reanudarJuego() {
        juegoPausado = false;  // Cambiar el estado del juego a no pausado
        menuPausa.removeMenu();  // Remover el menú y los botones
    }

    // Método para mostrar el menú de Game Over
    public void mostrarGameOver() {
        juegoPausado = true;  // Pausar el juego
        menuGameOver = new MenuGameOver();  // Crear el menú de Game Over
        addObject(menuGameOver, getWidth() / 2, getHeight() / 2);  // Agregar el menú de Game Over
    }

    // Método para reiniciar el juego
    public void reiniciarJuego() {
        Greenfoot.setWorld(new Fase1());  // Reiniciar el nivel actual
    }

    // Método para que los actores puedan verificar si el juego está pausado
    public boolean isJuegoPausado() {
        return juegoPausado;
    }
}
