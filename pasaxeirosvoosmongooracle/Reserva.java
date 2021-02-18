/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pasaxeirosvoosmongooracle;

/**
 *
 * @author oracle
 */
public class Reserva {
    private int codigo;
    private String DNI;
    private int idvueloIda;
    private int idvueloVuelta;

    public Reserva(int codigo, String DNI, int idvueloIda, int idvueloVuelta) {
        this.codigo = codigo;
        this.DNI = DNI;
        this.idvueloIda = idvueloIda;
        this.idvueloVuelta = idvueloVuelta;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public int getIdvueloIda() {
        return idvueloIda;
    }

    public void setIdvueloIda(int idvueloIda) {
        this.idvueloIda = idvueloIda;
    }

    public int getIdvueloVuelta() {
        return idvueloVuelta;
    }

    public void setIdvueloVuelta(int idvueloVuelta) {
        this.idvueloVuelta = idvueloVuelta;
    }
    
    public String toString(){
        return "Codr:" + codigo + " DNI:" + DNI + " vueloIda:" + idvueloIda + " idvueloVuelta:" + idvueloVuelta;
    }
    
}
