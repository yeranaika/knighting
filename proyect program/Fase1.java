    import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
    
    
    public class Fase1 extends World
    {
        public Fase1()
        {
            // tamaño del mapa
            super(900,600,1);
            
            // Crear y agregar la plataforma invisible en la posición de la plataforma naranja visible
            Plataforma plataforma = new Plataforma();
            addObject(plataforma, 630, 300);  // Ajusta estas coordenadas para que coincidan con la plataforma naranja
            
            // Crear y agregar el jugador
            Player player = new Player();
            addObject(player, 400, 250);  // Ajusta la posición inicial del jugador
            
        }
        
    }
