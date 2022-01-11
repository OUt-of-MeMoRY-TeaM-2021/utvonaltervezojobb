/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package out_of_memory.Terkep;

/**
 *
 * @author zacco
 */
public class Rakomanyadat {
    private String tipus;
    private float suly;
    private String cim;

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public void setSuly(float suly) {
        this.suly = suly;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getTipus() {
        return tipus;
    }

    public float getSuly() {
        return suly;
    }

    public String getCim() {
        return cim;
    }

    public Rakomanyadat(String tipus, float suly, String cim) {
        this.tipus = tipus;
        this.suly = suly;
        this.cim = cim;
    }
    
    @Override
    public String toString(){
        return "tipus: "+tipus+" suly: "+suly+" cim "+cim;
    }
}
