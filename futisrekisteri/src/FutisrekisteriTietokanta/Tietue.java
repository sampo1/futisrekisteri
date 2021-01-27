package FutisrekisteriTietokanta;

/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa "attribuutit".
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public interface Tietue {

    
    /**
     * @return tietueen kenttien lukum��r�
     */
    public abstract int getKenttia();


    /**
     * @return ensimm�inen k�ytt�j�n sy�tett�v�n kent�n indeksi
     */
    public abstract int ekaKentta();


    /**
     * @param k mink� kent�n kysymys halutaan
     * @return valitun kent�n kysymysteksti
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Mink� kent�n sis�lt� halutaan
     * @return valitun kent�n sis�lt�
     */
    public abstract String anna(int k);

    
    /**
     * Asetetaan valitun kent�n sis�lt�.  Mik�li asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k mink� kent�n sis�lt� asetetaan
     * @param s asetettava sis�lt� merkkijonona
     * @return null jos ok, muuten virheteksti
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehd��n identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona 
     */
    @Override
    public abstract String toString();

}
