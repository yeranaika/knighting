import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class OpcionesMenu extends World {
    // Constructor del mundo de opciones
    public OpcionesMenu() {    
        super(1200, 900, 1);
        
        // Agregar un botón de "Regresar"
        Boton regresarBoton = new Boton("Regresar");
        addObject(regresarBoton, getWidth() / 2, 750);  // Colocar en la parte inferior
    }

    // Método para verificar si el botón de regresar fue clickeado
    public void act() {
        if (Greenfoot.mouseClicked(getObjects(Boton.class).get(0))) {
            Greenfoot.setWorld(new MenuInicio());  // Cambiar al menú principal
        }
    }
}
