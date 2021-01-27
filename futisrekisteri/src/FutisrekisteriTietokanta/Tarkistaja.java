package FutisrekisteriTietokanta;

/**
 * Rajapinta yleiselle tarkistajalle.
 * Tarkistajan tehtävä on tutkia onko annettu
 * merkkijono kelvollinen kentän sisällöksi ja jos on.
 * palautetaan null.
 * Virhetapauksessa palautetaan virhettä mahdollisimman
 * hyvin kuvaava merkkijono.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public interface Tarkistaja {
    /**
     * Tutkitaan käykö annettu merkkijono kentän sisällöksi.
     * @param jono tutkittava merkkijono
     * @return null jos jono oikein, muuten virheilmoitusta vastaava merkkijono
     */
    String tarkista(String jono);
}