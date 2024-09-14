import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Plataforma extends Actor {
    public Plataforma(int ancho, int alto, Color color, int numero) {
        // Crear una plataforma rectangular con las dimensiones proporcionadas
        GreenfootImage imagenPlataforma = new GreenfootImage(ancho, alto);
        
        // Establecer el color de la plataforma
        //imagenPlataforma.setColor(color);
        
        // Dibujar el rectángulo de la plataforma
        imagenPlataforma.fillRect(0, 0, ancho, alto);
        
        // Ajustar la transparencia de la plataforma (0 es completamente transparente, 255 es opaco)
        imagenPlataforma.setTransparency(0);  // Ajusta el nivel de transparencia (por ejemplo, 128 es semi-transparente)
        
        // Establecer el color del texto y el tamaño de la fuente
        int tamañoTexto = 36;  // Tamaño del texto, puedes ajustar este valor para hacerlo más grande o más pequeño
        GreenfootImage textoTemporal = new GreenfootImage("" + numero, tamañoTexto, Color.WHITE, new Color(0, 0, 0, 0));
        int textoAncho = textoTemporal.getWidth();
        int textoAlto = textoTemporal.getHeight();
        
        // Dibujar el número en el centro de la plataforma
        imagenPlataforma.drawImage(textoTemporal, (ancho - textoAncho) / 2, (alto - textoAlto) / 2);
        
        // Asignar la imagen de la plataforma
        setImage(imagenPlataforma);
    }
}
