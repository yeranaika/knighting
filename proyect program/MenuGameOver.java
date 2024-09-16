import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class MenuGameOver extends Actor {
    private Boton botonReiniciar;
    private Boton botonMenuPrincipal;

    public MenuGameOver() {
        // Crear un fondo semitransparente para el menú de Game Over
        GreenfootImage background = new GreenfootImage(800, 600);
        background.setColor(new Color(0, 0, 0, 150));  // Fondo negro semitransparente
        background.fillRect(0, 0, background.getWidth(), background.getHeight());
        
        // Agregar el texto "Has muerto" en el centro
        GreenfootImage texto = new GreenfootImage("¡Has muerto!", 60, Color.WHITE, new Color(0, 0, 0, 0));
        background.drawImage(texto, (background.getWidth() - texto.getWidth()) / 2, background.getHeight() / 2 - 100);
        
        setImage(background);  // Establecer la imagen del actor
    }

    public void addedToWorld(World world) {
        // Agregar el botón de "Reiniciar"
        botonReiniciar = new Boton("Reiniciar");
        world.addObject(botonReiniciar, world.getWidth() / 2, world.getHeight() / 2 + 50);
        
        // Agregar el botón de "Menú Principal"
        botonMenuPrincipal = new Boton("Menú Principal");
        world.addObject(botonMenuPrincipal, world.getWidth() / 2, world.getHeight() / 2 + 140);
    }

    public void removeMenu() {
        World world = getWorld();
        if (world != null) {
            // Remover los botones del mundo
            world.removeObject(botonReiniciar);
            world.removeObject(botonMenuPrincipal);
            // Remover el menú de Game Over
            world.removeObject(this);
        }
    }
}
