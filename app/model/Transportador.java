package model;


public class Transportador {
    public String name;
    public Double calificacion;
    public CarDetail carDetail;
    public Localization pos;

    public Transportador(){
        this.carDetail= new CarDetail();
        this.pos = new Localization();
    }

}
