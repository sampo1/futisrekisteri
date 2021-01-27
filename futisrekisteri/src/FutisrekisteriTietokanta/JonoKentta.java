package FutisrekisteriTietokanta;

/**
 * Luokka merkkijonojen tallentamiseksi.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class JonoKentta extends PerusKentta {
    private String jono = "";

    /**
     * Alustetaan kentt� kysymyksen tiedoilla.
     * @param kysymys joka esitet��n kentt�� kysytt�ess�.
     */
    public JonoKentta(String kysymys) { super(kysymys); }


    /**
     * Alustetaan kysymyksell� ja tarkistajalla.
     * @param kysymys joka esitet��n kentt�� kysytt�ess�.
     * @param tarkistaja tarkistajaluokka joka tarkistaa sy�t�n oikeellisuuden
     */
    public JonoKentta(String kysymys,Tarkistaja tarkistaja) {
        super(kysymys,tarkistaja);
    }

    /**
     * @return Palauetaan kent�n sis�lt�
     */
    @Override
    public String toString() { return jono; }

    /** 
     * @param s merkkijono joka asetetaan kent�n arvoksi
     */
    @Override
    public String aseta(String s) {
        if ( tarkistaja == null ) {
            this.jono = s; 
            return null;
        }

        String virhe = tarkistaja.tarkista(s);
        if ( virhe == null ) {
            this.jono = s; 
            return null;
        }
        return virhe;
    }

}
