package FutisrekisteriTietokanta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;

/**
 * Rekisteriluokka, joka v‰litt‰‰ kutsuja pelaajat- ja tilastot luokille.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class Rekisteri {
    private Pelaajat pelaajat = new Pelaajat();
    private Tilastot tilastot = new Tilastot(); 


    /**
     * Palautaa rekisterin jasenmaaran
     * @return jasenmaara
     */
    public int getPelaajia() {
        return pelaajat.getLkm();
    }


    /**
     * Poistaa pelaajista ja tilastoista pelaaja
     * @param pelaaja kenet poistetaan
     * @return montako poistettiin
     * @throws PoikkeusException jos virhe
     */
    public int poista(Pelaaja pelaaja) throws PoikkeusException {
        if (pelaaja == null) return 0;
        tilastot.poistaPelaajanTilastomerkinnat(pelaaja.getTunnusNro()); 
        int ret = pelaajat.poista(pelaaja.getTunnusNro()); 
        return ret;
    }


    /** 
     * Poistaa taman tilaston 
     * @param tilasto poistettava tilasto 
     */ 
    public void poistaTilasto(Tilasto tilasto) { 
        tilastot.poista(tilasto); 
    }

    /**
     * Korvaa pelaajan tietorakenteessa. Jos pelaajaa ei viela ole, luo uuden.
     * @param pelaaja lisattava pelaaja
     * @throws PoikkeusException jos lisaysta ei voida tehda
     */
    public void lisaa(Pelaaja pelaaja) throws PoikkeusException {
        pelaajat.lisaa(pelaaja);
    }
    
    /**
     * List‰‰n uusi harrastus kerhoon
     * @param tilast lisattava tilasto
     * @throws PoikkeusException jos tulee ongelmia
     */

    public void lisaa(Tilasto tilast) throws PoikkeusException {
        tilastot.lisaa(tilast);
    }
  
    /**
     * Korvaa tilastomerkinta tietorakenteessa. Jos tilastoa ei viela ole, luo uuden.
     * @param tilast lisattava tilasto 
     * @throws PoikkeusException jos ei onnistu
     */
    public void korvaaTaiLisaa(Tilasto tilast) throws PoikkeusException {
        tilastot.korvaaTaiLisaa(tilast);
    }

    /** 
     * Korvaa pelaajan tietorakenteessa. Ottaa pelaajan omistukseensa. 
     * Etsit‰‰n samalla tunnusnumerolla oleva pelaaja. Jos ei loydy, niin lisataan uutena pelaajana. 
     * @param pelaaja lisattavan pelaajan viite.
     * @throws PoikkeusException jos tietorakenne on jo taynna 
     */ 
    public void korvaaTaiLisaa(Pelaaja pelaaja) throws PoikkeusException { 
        pelaajat.korvaaTaiLisaa(pelaaja); 
    } 

    /** 
     * Palauttaa taulukossa hakuehtoon vastaavien pelaajien viitteet 
     * @param hakuehto hakuehto  
     * @param i etsittavan kentan indeksi  
     * @return tietorakenteen loytyneista pelaajista 
     * @throws PoikkeusException Jos jotakin menee vaarin
     */ 
    public ArrayList<Pelaaja> etsi(String hakuehto, int i) throws PoikkeusException { 
        return pelaajat.etsi(hakuehto, i);
    }
    
    /**
     * Palauttaa i:n pelaajan
     * @param i monesko pelaaja palautetaan
     * @return viite i:teen pelaajaan
     * @throws IndexOutOfBoundsException jos i v‰‰rin
     */
    public Pelaaja annaPelaaja(int i) throws IndexOutOfBoundsException {
        return pelaajat.annaPelaaja(i);
    }

    /**
     * Haetaan pelaajan tilastomerkinnat
     * @param pelaaja pelaaja jolle tilastomerkintoja etsitaan
     * @return lista jossa viiteet loydetteyihin tilastomerkintoihin
     */
    public List<Tilasto> annaTilastot(Pelaaja pelaaja) {
        return tilastot.annaTilastot(pelaaja.getTunnusNro());
    }
    

    /**   
     * Laitetaan tilastot muuttuneeksi, niin pakotetaan tallentamaan.   
     */   
    public void setTilastotMuutos() {   
        tilastot.setMuutos();   
    }
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if (!nimi.isEmpty()) hakemistonNimi = nimi +"/";
        pelaajat.setTiedostonPerusNimi(hakemistonNimi + "pelaajat");
        tilastot.setTiedostonPerusNimi(hakemistonNimi + "tilastot");
    }

    /**
     * Lukee rekisterin tiedot tiedostosta
     * @param nimi jota kaytetaan lukemisessa
     * @throws PoikkeusException jos lukeminen epaonnistuu
     */
    public void lueTiedostosta(String nimi) throws PoikkeusException {
        pelaajat = new Pelaajat();
        tilastot = new Tilastot();
        setTiedosto(nimi);
        pelaajat.lueTiedostosta();
        tilastot.lueTiedostosta();
    }


    /**
     * Tallettaa rekisterin tiedot tiedostoon
     * @throws PoikkeusException jos tallettamisessa ongelmia
     */
    public void tallenna() throws PoikkeusException {
        String virhe = "";
        try {
            pelaajat.tallenna();
        } catch (PoikkeusException ex) {
            virhe = ex.getMessage();
            Dialogs.showMessageDialog("Virhe on " + virhe); 
        }
        try {
            tilastot.tallenna();
        } catch (PoikkeusException ex) {
            virhe += ex.getMessage();
            Dialogs.showMessageDialog("Virhe on " + virhe);
        }
        if (!"".equals(virhe)) throw new PoikkeusException(virhe);
    }


    /**
     * @param args ei k‰ytˆss‰
     */
    public static void main(String args[]) {
        /*Rekisteri rekisteri = new Rekisteri();
        
        try {
            // rekisteri.lueTiedostosta("futisrekisteri");
            
            Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja();
            aku1.rekisteroi();
            aku1.vastaaMikki(1);
            aku2.rekisteroi();
            aku2.vastaaMikki(2);

            rekisteri.lisaa(aku1);
            rekisteri.lisaa(aku2);
            int id1 = aku1.getTunnusNro();
            int id2 = aku2.getTunnusNro();
            Tilasto til11 = new Tilasto(id1); til11.vastaaMikki(id1); rekisteri.lisaa(til11);
            Tilasto til12 = new Tilasto(id1); til12.vastaaMikki(id1); rekisteri.lisaa(til12);
            Tilasto til21 = new Tilasto(id2); til21.vastaaMikki(id2); rekisteri.lisaa(til21);
            Tilasto til22 = new Tilasto(id2); til22.vastaaMikki(id2); rekisteri.lisaa(til22);
            Tilasto til23 = new Tilasto(id2); til23.vastaaMikki(id2); rekisteri.lisaa(til23);

            System.out.println("============= Rekisterin testi =================");

            for (int i = 0; i < rekisteri.getPelaajia(); i++) {
                Pelaaja pelaaja = rekisteri.annaPelaaja(i);
                System.out.println("Pelaaja paikassa: " + i);
                pelaaja.tulosta(System.out);
                List<Tilasto> loytyneet = rekisteri.annaTilastot(aku1);
                for (Tilasto harrastus : loytyneet)
                    harrastus.tulosta(System.out);
            }
            
        } catch (PoikkeusException p) {
            System.out.println(p.getMessage());
        }*/
    }

}