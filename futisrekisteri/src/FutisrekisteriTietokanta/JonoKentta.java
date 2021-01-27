package FutisrekisteriTietokanta;

/**
 * Luokka merkkijonojen tallentamiseksi.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class JonoKentta extends PerusKentta {
    private String jono = "";

    /**
     * Alustetaan kenttä kysymyksen tiedoilla.
     * @param kysymys joka esitetään kenttää kysyttäessä.
     */
    public JonoKentta(String kysymys) { super(kysymys); }


    /**
     * Alustetaan kysymyksellä ja tarkistajalla.
     * @param kysymys joka esitetään kenttää kysyttäessä.
     * @param tarkistaja tarkistajaluokka joka tarkistaa syötön oikeellisuuden
     */
    public JonoKentta(String kysymys,Tarkistaja tarkistaja) {
        super(kysymys,tarkistaja);
    }

    /**
     * @return Palauetaan kentän sisältö
     */
    @Override
    public String toString() { return jono; }

    /** 
     * @param s merkkijono joka asetetaan kentän arvoksi
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
