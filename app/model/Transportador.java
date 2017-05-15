package model;


public class Transportador {
    public int id;
    public String name;
    public Double calificacion;
    public String estado;
    public CarDetail carDetail;
    public Localization pos;

    public Transportador(){
        this.carDetail= new CarDetail();
        this.pos = new Localization();
    }

}
