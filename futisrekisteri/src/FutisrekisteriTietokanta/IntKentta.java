package FutisrekisteriTietokanta;

import javax.swing.SwingConstants;
import fi.jyu.mit.ohj2.Mjonot;
import FutisrekisteriTietokanta.PerusKentta;

/**
 * Kentt‰ kokonaislukujen tallentamiseksi
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class IntKentta extends PerusKentta  {
    private int arvo;

    /**
     * Alustetaan kentt‰ kysymyksell‰.
     * @param kysymys joka n‰ytet‰‰n kentt‰‰ kysytt‰ess‰.
     */
    public IntKentta(String kysymys) { super(kysymys); }
    
    /**
     * @return kent‰n arvo kokonaislukuna
     */
    public int getValue() { return arvo; }
    
    /**
     * Asetetaan kent‰n arvo kokonaislukuna
     * @param value asetettava kokonaislukuarvo
     */
    public void setValue(int value) { arvo = value; }
   
    /** 
     * Kent‰n arvo merkkijonona.
     * @return kentt‰ merkkijonona
     */
    @Override
    public String toString() { return ""+arvo; }

    /**
     * Asetetaan kent‰n arvo merkkijonosta.  Jos jono 
     * kunnollinen, palautetaan null.  Jos jono ei
     * kunnollinen int-syˆte, palautetaan virheilmoitus ja
     * kent‰n alkuper‰inen arvo j‰‰ voimaan.
     * @param jono kent‰‰n asetettava arvo mekrkijonona
     * @return null jos hyv‰ arvo, muuten virhe.  
     */
    @Override
    public String aseta(String jono) {
        StringBuffer sb = new StringBuffer(jono);
        try {
            this.arvo = Mjonot.erotaEx(sb,' ',0);
            return null;
        }
        catch (NumberFormatException ex) {
            return "Ei kokonaisluku (" + jono + ")"; 
        }
    }

    /**
     * Palauttaa kent‰n tiedot vertailtavana merkkijonona
     * @return vertailtava merkkijono kent‰st‰
     */
    @Override
    public String getAvain() { 
        return Mjonot.fmt(arvo, 10); 
    }

    /**
     * @return syv‰kopio oliosta
     */
    @Override
    public IntKentta clone() throws CloneNotSupportedException {
        return (IntKentta)super.clone();
    }

    /**
     * @return vaakasuuntainen sijainti kent‰lle
     */
    @Override
    public int getSijainti() {
        return SwingConstants.RIGHT;        
    }
}