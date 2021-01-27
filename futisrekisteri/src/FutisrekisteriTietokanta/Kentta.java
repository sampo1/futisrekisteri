package FutisrekisteriTietokanta;

/**
 * Rajapinta yhdelle kentalle.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public interface Kentta extends Cloneable, Comparable<Kentta>{

    /**
     * Kentan arvo merkkijonona.
     * @return kenttä merkkkijonona
     */
    @Override
    String toString();

    /**
     * Palauttaa kenttaan liittyvan kysymyksen.
     * @return kenttaan liittyvä kysymys.
     */
    String getKysymys();

    /**
     * Asettaa kentan sisallon ottamalla tiedot
     * merkkijonosta.
     * @param jono jono josta tiedot otetaan.
     * @return null jos sisalto on hyvä, muuten merkkijonona virhetieto
     */
    String aseta(String jono);


    /**
     * Palauttaa kentan tiedot veratiltavana merkkijonona
     * @return vertailtava merkkijono kentasta
     */
    String getAvain();


    /**
     * @return syvakopio kentasta, tehtava jokaiseen luokkaan toimivaksi
     * @throws CloneNotSupportedException
     */
    Kentta clone() throws CloneNotSupportedException ;

    
    /**
     * @return vaakasuuntainen sijainti kentalle
     */
    int getSijainti();
    
}
