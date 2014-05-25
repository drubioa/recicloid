package es.uca.recicloid.adapters;

public class Title
{
    private String titulo;
    private String subtitulo;
 
    public Title(String tit, String sub){
        titulo = tit;
        subtitulo = sub;
    }
 
    public String getTitulo(){
        return titulo;
    }
 
    public String getSubtitulo(){
        return subtitulo;
    }
}
