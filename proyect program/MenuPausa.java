import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class MenuPausa extends Actor {
    private Boton botonReanudar;
    private Boton botonSalir;

    public MenuPausa() {
        // Crear un fondo semitransparente para el menú de pausa
        GreenfootImage background = new GreenfootImage(400, 300);
        background.setColor(new Color(0, 0, 0, 150));  // Fondo negro semitransparente
        background.fillRect(0, 0, 400, 300);
        
        // Agregar el texto "PAUSA" en el centro
        GreenfootImage texto = new GreenfootImage("PAUSA", 36, Color.WHITE, new Color(0, 0, 0, 0));
        background.drawImage(texto, (background.getWidth() - texto.getWidth()) / 2, 50);
        
        setImage(background);  // Establecer la imagen del actor
    }

    public void addedToWorld(World world) {
        // Agregar el botón de "Reanudar"
        botonReanudar = new Boton("Reanudar");
        world.addObject(botonReanudar, world.getWidth() / 2, world.getHeight() / 2 - 50);
        
        // Agregar el botón de "Salir"
        botonSalir = new Boton("Salir");
        world.addObject(botonSalir, world.getWidth() / 2, world.getHeight() / 2 + 50);
    }

    public void removeMenu() {
        World world = getWorld();
        if (world != null) {
            // Remover los botones del mundo
            world.removeObject(botonReanudar);
            world.removeObject(botonSalir);
            // Remover el menú de pausa
            world.removeObject(this);
        }
    }
}
