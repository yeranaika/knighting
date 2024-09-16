import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class MenuInicio extends World
{
    // Constructor del menú de inicio
    public MenuInicio()
    {    
        // Crear el mundo con un tamaño específico
        super(1200, 900, 1); 
        
        // Llamar a un método para preparar el menú de inicio
        prepararMenu();
    }
    
    // Método para preparar el menú
    private void prepararMenu() {
        // Crear y agregar el botón de "Iniciar"
        Boton botonIniciar = new Boton("Iniciar");
        addObject(botonIniciar, getWidth() / 2, getHeight() / 2 - 50);
        
        // Crear y agregar el botón de "Opciones"
        Boton botonOpciones = new Boton("Opciones");
        addObject(botonOpciones, getWidth() / 2, getHeight() / 2 + 50);
    }
    
    // Método que verifica la selección de los botones
    public void act() {
        // Si el botón de "Iniciar" es presionado
        if (Greenfoot.mouseClicked(getObjects(Boton.class).get(0))) {
            Greenfoot.setWorld(new Fase1());  // Cambiar a la fase 1 del juego
        }
        
        // Si el botón de "Opciones" es presionado
        if (Greenfoot.mouseClicked(getObjects(Boton.class).get(1))) {
            Greenfoot.setWorld(new OpcionesMenu());  // Cambiar al menú de opciones
        }
    }
}
