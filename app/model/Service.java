package model;

import org.joda.time.DateTime;

public class Service {
    public int idService;
    public int idTransporter;
    public int idUser;
    public Transportador transporter;
    public Localization origen;
    public Localization destino;
    public Addressee addressee;
    public DateTime fecha_inicio;
    public String status;

    public Service(){
        this.origen = new Localization();
        this.destino = new Localization();
        this.addressee = new Addressee();
    }

}
