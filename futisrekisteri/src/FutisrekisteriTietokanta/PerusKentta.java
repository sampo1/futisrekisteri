package FutisrekisteriTietokanta;

import javax.swing.SwingConstants;

/**
 * Peruskentt‰ joka implementoi kysymyksen k‰sittelyn ja tarkistajan k‰sittelyn.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public abstract class PerusKentta implements Kentta {
    private final String kysymys;

    /**
     * Yleisen tarkistajan viite
     */
    protected Tarkistaja tarkistaja = null;

    /**
     * Alustetaan kentt‰ kysymyksen tiedoilla.
     * @param kysymys joka esitet‰‰n kentt‰‰ kysytt‰ess‰.
     */
    public PerusKentta(String kysymys)  { this.kysymys = kysymys; }

    /**
     * Alustetaan kysymyksell‰ ja tarkistajalla.
     * @param kysymys joka esitet‰‰n kentt‰‰ kysytt‰ess‰.
     * @param tarkistaja tarkistajaluokka joka tarkistaa syˆt‰n oikeellisuuden
     */
    public PerusKentta(String kysymys, Tarkistaja tarkistaja) {
        this.kysymys = kysymys;
        this.tarkistaja = tarkistaja;
    }

    /**
     * @return kent‰n arvo merkkijonona
     */
    @Override
    public abstract String toString();

    /**
     * @return Kentt‰‰ vastaava kysymys
     */
    @Override
    public String getKysymys() {
        return kysymys;
    }

    /**
     * @param jono josta otetaan kent‰n arvo
     */
    @Override
    public abstract String aseta(String jono);

    /**
     * Palauttaa kent‰n tiedot veratiltavana merkkijonona
     * @return vertailtava merkkijono kent‰st‰
     */
    @Override
    public String getAvain() {
        return toString().toUpperCase();
    }


    /**
     * @return syv‰kopio oliosta
     */
    @Override
    public Kentta clone() throws CloneNotSupportedException {
        return (Kentta)super.clone();
    }


    /**
     * @return vertailee kahta oliota kesken‰‰n
     */
    @Override
    public int compareTo(Kentta k) {
            return getAvain().compareTo(k.getAvain());
    }

    
    /**
     * @return vaakasuuntainen sijainti kent‰lle
     */
    @Override
    public int getSijainti() {
        return SwingConstants.LEFT;        
    }
}
