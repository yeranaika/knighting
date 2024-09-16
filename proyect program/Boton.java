import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Boton extends Actor {
    private String texto;
    private GreenfootImage imgNormal;  // Imagen normal
    private GreenfootImage imgGrande;  // Imagen más grande para hover
    private boolean mouseOver = false;  // Estado del ratón sobre el botón

    // Constructor del botón
    public Boton(String texto) {
        this.texto = texto;

        // Crear imagen normal
        imgNormal = crearImagenBoton(300, 80, texto, 40, Color.WHITE, Color.BLACK);
        setImage(imgNormal);

        // Crear imagen más grande para hover
        imgGrande = crearImagenBoton(320, 90, texto, 45, Color.WHITE, Color.GRAY);
    }

    // Método para manejar la animación y eventos del clic
    public void act() {
        // Los botones deben seguir funcionando incluso si el juego está pausado,
        // por lo que no verificamos el estado de pausa aquí.

        if (Greenfoot.mouseClicked(this)) {
            // Realizar acción dependiendo del texto del botón
            World mundo = getWorld();
            if (texto.equals("Reanudar")) {
                // Reanudar el juego
                if (mundo instanceof Fase1) {
                    ((Fase1) mundo).reanudarJuego();
                }
            } else if (texto.equals("Salir")) {
                // Cambiar al menú principal
                Greenfoot.setWorld(new MenuInicio());
            } else if (texto.equals("Iniciar")) {
                // Iniciar el juego desde el menú principal
                Greenfoot.setWorld(new Fase1());
            } else if (texto.equals("Opciones")) {
                // Ir al menú de opciones
                Greenfoot.setWorld(new OpcionesMenu());
            } else if (texto.equals("Reiniciar")) {
                // Reiniciar el juego
                if (mundo instanceof Fase1) {
                    ((Fase1) mundo).reiniciarJuego();
                }
            } else if (texto.equals("Menú Principal")) {
                // Ir al menú principal
                Greenfoot.setWorld(new MenuInicio());
            }
            // Puedes agregar más acciones según sea necesario
        }

        // Animación de hover
        if (Greenfoot.mouseMoved(this)) {
            setImage(imgGrande);  // Cambiar a imagen más grande cuando el ratón esté sobre el botón
            mouseOver = true;
        }
        if (mouseOver && Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
            setImage(imgNormal);  // Volver a la imagen normal cuando el ratón se mueva fuera
            mouseOver = false;
        }
    }

    // Método para crear una imagen de botón con los parámetros dados
    private GreenfootImage crearImagenBoton(int ancho, int alto, String texto, int tamañoTexto, Color colorTexto, Color colorFondo) {
        GreenfootImage img = new GreenfootImage(ancho, alto);
        img.setColor(colorFondo);  // Establecer color de fondo
        img.fillRect(0, 0, img.getWidth(), img.getHeight());  // Dibujar el rectángulo de fondo
        GreenfootImage textoImg = new GreenfootImage(texto, tamañoTexto, colorTexto, new Color(0, 0, 0, 0));  // Crear imagen del texto
        int x = (img.getWidth() - textoImg.getWidth()) / 2;  // Calcular posición X para centrar el texto
        int y = (img.getHeight() - textoImg.getHeight()) / 2;  // Calcular posición Y para centrar el texto
        img.drawImage(textoImg, x, y);  // Dibujar el texto
        return img;
    }
}
