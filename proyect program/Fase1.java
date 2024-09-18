import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

public class Fase1 extends World {
    private boolean juegoPausado = false;  // Controla si el juego está pausado o no
    private MenuPausa menuPausa;  // Referencia al menú de pausa
    private MenuGameOver menuGameOver; // Referencia al menú de Game Over
    private BarraDeVida barraDeVida;  // Instancia de la barra de vida
    private Player player;  // Referencia al jugador
    private TextoPuntaje textoPuntaje;  // Texto para mostrar el puntaje
    private int ronda = 1;  // Controla el número de ronda
    private List<Entidad> enemigosActivos;  // Lista para mantener los enemigos activos
    private List<int[]> posicionesPlataformas;  // Lista para las posiciones de las plataformas

    public Fase1() {
        // Tamaño del mapa
        super(1200, 900, 1);
        enemigosActivos = new ArrayList<>();  // Inicializar la lista de enemigos
        posicionesPlataformas = new ArrayList<>();  // Inicializar la lista de plataformas
        prepararFase();  // Método para configurar el nivel
    }

    private void prepararFase() {
        // Inicializar el puntaje
        textoPuntaje = new TextoPuntaje();
        addObject(textoPuntaje, getWidth() / 2, 50);  // Posicionar el puntaje en la parte superior central

        // PLATAFORMAS FLOTANTES (Agregar solo las plataformas permitidas)
        Plataforma plataformaFLO1 = new Plataforma(485, 20, Color.RED, 1);
        addObject(plataformaFLO1, 625, 300);
        posicionesPlataformas.add(new int[]{625, 300});

        Plataforma plataformaFLO2 = new Plataforma(130, 20, Color.RED, 2);
        addObject(plataformaFLO2, 400, 485);
        posicionesPlataformas.add(new int[]{400, 485});

        Plataforma plataformaFLO3 = new Plataforma(130, 20, Color.RED, 3);
        addObject(plataformaFLO3, 250, 560);
        posicionesPlataformas.add(new int[]{250, 560});

        // Excluir estas plataformas (no agregar a posicionesPlataformas)
        Plataforma plataformaFLO4 = new Plataforma(130, 35, Color.YELLOW, 4);
        addObject(plataformaFLO4, 520, 390);

        Plataforma plataformaFLO5 = new Plataforma(370, 40, Color.YELLOW, 5);
        addObject(plataformaFLO5, 680, 420);

        Plataforma plataformaFLO6 = new Plataforma(280, 90, Color.GREEN, 6);
        addObject(plataformaFLO6, 725, 355);

        // PLATAFORMAS DE PISO
        Plataforma plataforma2 = new Plataforma(873, 20, Color.BLUE, 7);
        addObject(plataforma2, 150, 682);
        posicionesPlataformas.add(new int[]{150, 682});

        Plataforma plataforma3 = new Plataforma(620, 20, Color.BLUE, 8);
        addObject(plataforma3, 890, 701);
        posicionesPlataformas.add(new int[]{890, 701});

        // Crear y agregar el jugador
        player = new Player();
        addObject(player, 20, 600);  // Posicionar el jugador

        // Crear y agregar la barra de vida
        barraDeVida = new BarraDeVida(player);  // Pasar la referencia del jugador
        addObject(barraDeVida, getWidth() - 150, 100);  // Posicionar la barra de vida en la esquina superior derecha

        iniciarRonda();  // Iniciar la primera ronda
    }

    // Método para iniciar una ronda
    private void iniciarRonda() {
        int numeroEnemigos = ronda * 2;  // Aumentar el número de enemigos con cada ronda
        
        // Obtener la altura (y) inicial del jugador
        int alturaJugador = player.getY();
        
        // Colocar enemigos sobre las plataformas válidas
        for (int i = 0; i < numeroEnemigos; i++) {
            // Filtrar plataformas válidas (excluir las plataformas específicas y las plataformas más bajas que el jugador)
            List<int[]> plataformasPermitidas = new ArrayList<>();
            for (int[] plataforma : posicionesPlataformas) {
                // Filtrar plataformas excluidas (FLO4, FLO5, FLO6) y plataformas debajo del jugador
                if ((plataforma[1] <= alturaJugador) &&  // Altura menor o igual a la del jugador
                    !(plataforma[0] == 520 && plataforma[1] == 390) &&  // Excluir plataformaFLO4
                    !(plataforma[0] == 680 && plataforma[1] == 420) &&  // Excluir plataformaFLO5
                    !(plataforma[0] == 725 && plataforma[1] == 355)) {  // Excluir plataformaFLO6
                    plataformasPermitidas.add(plataforma);
                }
            }

            // Si no hay plataformas válidas, no colocar enemigos
            if (plataformasPermitidas.isEmpty()) {
                break;
            }

            // Seleccionar una plataforma aleatoria
            int[] plataforma = plataformasPermitidas.get(Greenfoot.getRandomNumber(plataformasPermitidas.size()));

            int x = plataforma[0];  // Coordenada X de la plataforma
            int y = plataforma[1] - 10;  // Coordenada Y de la plataforma (10 píxeles más alto)

            // Crear el enemigo
            Entidad enemigo;
            if (i % 2 == 0) {  // Alternar entre esqueleto y champiñon
                enemigo = new Esqueleto(x - 100, x + 100);  // Rango de movimiento de 200 píxeles
            } else {
                enemigo = new ChampinonM(x - 100, x + 100);  // Rango de movimiento de 200 píxeles
            }

            // Agregar el enemigo al mundo
            addObject(enemigo, x, y);
            enemigosActivos.add(enemigo);  // Añadir a la lista de enemigos activos
        }
    }

    // Método para eliminar un enemigo cuando muere
    public void eliminarEnemigo(Entidad enemigo) {
        enemigosActivos.remove(enemigo);  // Eliminar el enemigo de la lista
        if (enemigosActivos.isEmpty()) {  // Si no hay más enemigos, comienza una nueva ronda
            ronda++;
            iniciarRonda();
        }
    }

    public void act() {
        if (!juegoPausado) {
            String key = Greenfoot.getKey();
            if (key != null && key.equals("escape")) {
                pausarJuego();
            }
        }
    
        // Actualizar la barra de vida del jugador
        barraDeVida.actualizarBarra();
    }

    // Método para aumentar el puntaje cuando se mata un enemigo
    public void aumentarPuntaje(int puntos) {
        textoPuntaje.aumentarPuntaje(puntos);
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

    // Método para mostrar el mensaje de victoria
    public void mostrarVictoria() {
        showText("¡Has ganado!", getWidth() / 2, getHeight() / 2);  // Mostrar mensaje de victoria en la pantalla
        Greenfoot.stop();  // Detener el juego
    }

    // Método para hacer aparecer la puerta cuando el jugador alcanza el puntaje necesario
    public void aparecerPuerta() {
        PuertaWin puerta = new PuertaWin();  // Crear una nueva puerta
        addObject(puerta, getWidth() / 2, getHeight() - 100);  // Posicionar la puerta en algún lugar del mundo
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
