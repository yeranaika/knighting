import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Fase1 extends World
{
    public Fase1()
    {
        // Tamaño del mapa
        super(1200, 900, 1);
        
        //PLATAFORMA FLOTANTE
        //PLATAFORMA 1
        Plataforma plataformaFLO1 = new Plataforma(485, 20, Color.RED);
        addObject(plataformaFLO1, 625, 300);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO2 = new Plataforma(130, 20, Color.RED);
        addObject(plataformaFLO2, 400, 485);  // Ajusta estas coordenadas para la primera plataforma
        
        Plataforma plataformaFLO3 = new Plataforma(130, 20, Color.RED);
        addObject(plataformaFLO3, 250, 560);  // Ajusta estas coordenadas para la primera plataforma
        
        //PLATAFORMA PISO
        //PLATAFORMA 1
        Plataforma plataforma2 = new Plataforma(873, 20, Color.BLUE);
        addObject(plataforma2, 150, 682);  
        //PLATAFORMA 2
        Plataforma plataforma3 = new Plataforma(620, 20, Color.BLUE);
        addObject(plataforma3, 890, 701); 
        
        // Crear y agregar el jugador
        Player player = new Player();
        addObject(player, 400, 250);  // Ajusta la posición inicial del jugador
        
        Esqueleto esqueleto = new Esqueleto();
        addObject(esqueleto, 1000, 245);  // Ajusta la posición inicial del enemigo
        
        Esqueleto esqueleto2 = new Esqueleto();
        addObject(esqueleto2, 600, 245);  // Ajusta la posición inicial del enemigo
    }
}
