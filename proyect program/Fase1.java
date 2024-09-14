import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Fase1 extends World
{
    public Fase1()
    {
        // Tamaño del mapa
        super(1200, 900, 1);
        
        //PLATAFORMA FLOTANTE
        //PLATAFORMA 1
        Plataforma plataformaFLO1 = new Plataforma(485, 20, Color.RED, 1);
        addObject(plataformaFLO1, 625, 300);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO2 = new Plataforma(130, 20, Color.RED, 2);
        addObject(plataformaFLO2, 400, 485);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO3 = new Plataforma(130, 20, Color.RED,3);
        addObject(plataformaFLO3, 250, 560);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO4 = new Plataforma(130, 35, Color.YELLOW, 4);
        addObject(plataformaFLO4, 520, 390);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO5 = new Plataforma(370, 40, Color.YELLOW, 5);
        addObject(plataformaFLO5, 680, 420);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO6 = new Plataforma(280, 90, Color.GREEN, 6);
        addObject(plataformaFLO6, 725, 355);  // Ajusta estas coordenadas para la primera plataforma
        
        
        //PLATAFORMA PISO
        //PLATAFORMA 1
        Plataforma plataforma2 = new Plataforma(873, 20, Color.BLUE, 7);
        addObject(plataforma2, 150, 682);  
        //PLATAFORMA 2
        Plataforma plataforma3 = new Plataforma(620, 20, Color.BLUE, 8);
        addObject(plataforma3, 890, 701); 
        
        
        // Crear y agregar el jugador
        Player player = new Player();
        addObject(player, 400, 250);  // Ajusta la posición inicial del jugadoren
        
        Esqueleto esqueleto = new Esqueleto(100, 200); // Los valores 100 y 500 son ejemplos para los puntos inicial y final de patrullaje
        addObject(esqueleto, 100, 250);  // Ajusta la posición inicial del esqueleto en el mundo
        
        Esqueleto esqueleto2 = new Esqueleto(800, 900); // Para otro esqueleto con diferentes puntos de patrullaje
        addObject(esqueleto2, 600, 250);  // Ajusta la posición inicial del segundo esqueleto

    }
}
