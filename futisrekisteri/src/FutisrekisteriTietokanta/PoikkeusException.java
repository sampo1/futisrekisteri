package FutisrekisteriTietokanta;

/**
* Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
* @author Sampo Ruohonen
* @version 15.5.2019
*/
public class PoikkeusException extends Exception{

    private static final long serialVersionUID = 1L;


    /**
    * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
    * käytettävä viesti
    * @param viesti Poikkeuksen viesti
    */
    public PoikkeusException(String viesti) {
        super(viesti);
    }

}
