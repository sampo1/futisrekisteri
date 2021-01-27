package FutisrekisteriTietokanta;

import java.io.*;
import java.util.Comparator;
//import java.util.Scanner;

import fi.jyu.mit.ohj2.*;

/**
 * Pelaajaluokka. K‰sittelee yhden pelaajan.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class Pelaaja implements Cloneable {
    
    private Kentta kentat[] = {
            new IntKentta("tunnusNro"),
            new JonoKentta("nimi"),
            new JonoKentta("syntymapaiva"),
            new IntKentta("pituus"),
            new IntKentta("paino"),
            new JonoKentta("kotimaa"),
            new IntKentta("puhelinnumero"),
            new JonoKentta("kausimaksu"),
            new JonoKentta("vakuutus")
    }; 
    
    private int tunnusNro;
    private String nimi = "";
    private String syntymapaiva ="";
    private int pituus;
    private int paino;
    private String kotimaa ="";
    private String puhelinnumero="";
    private String kausimaksu="";
    private String vakuutus="";
  
    private static int seuraavaNro = 1;


    /**
     * Luokka joka vertaa kahta pelaajaa kesken‰‰n 
     */
    
    public static class Vertailija implements Comparator<Pelaaja> {

        private final int kenttaNro;


        /**
         * Alustetaan vertailija vertailemaan tietyn kent‰n perusteella
         * @param k vertailtavan kent‰n indeksi.
         */
        
        public Vertailija(int k) {
            this.kenttaNro = k;
        }
        
        /**
         * Verrataan kahta pelaajaa keskenaan.
         * @param p1 1. verrattava pelaaja
         * @param p2 2. verrattava pelaaja
         * @return <0 jos p1 < p2, 0 jos p1 == p2 ja muuten >0
         */
        @Override
        public int compare(Pelaaja p1, Pelaaja p2) {
            String s1 = p1.getAvain(kenttaNro);
            String s2 = p2.getAvain(kenttaNro);
            return s1.compareTo(s2);
        }
    }       
     
    /**
     * Uusi Pelaaja
     */
    public Pelaaja() {
        //
    }


    /**
     * Alustetaan tietyn pelaajan Pelaaja.  
     * @param pelaaja pelaajan viitenumero 
     */
    public Pelaaja(int pelaaja) {
        this.tunnusNro = pelaaja;
    }
    

    /**
     * Apumetodi, jolla saadaan t‰ytetty‰ testiarvot Pelaajalle.
     * @param nro viite pelaajaan, jonka pelaaja on
     */
    public void vastaaMikki(int nro) {
        tunnusNro = nro;
        nimi = "testiNimi";
        pituus = 156;
        paino = 100;
        syntymapaiva = "1.1.1980";
    }


    /**
     * Tulostetaan pelaajan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("nimi " + nimi + " paino: " + paino + " pituus: " + pituus
                + "syntym‰p‰iv‰: " + syntymapaiva + "pelipaikka: ");
    }


    /**
     * Tulostetaan pelaajan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa Pelaajalle seuraavan rekisterointinumeron.
     * @return pelaajan uusi tunnusnumero
     * @example
     * <pre name="test">
     *   Pelaaja pel1 = new Pelaaja();
     *   pel1.getTunnusNro() === 0;
     *   pel1.rekisteroi();
     *   Pelaaja pel2 = new Pelaaja();
     *   pel2.rekisteroi();
     *   int n1 = pel1.getTunnusNro();
     *   int n2 = pel2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }

    /**
     * @return kuinka monta kenttaa on
     */
    public int getKenttia() {
    	return 9;
	}
    
    /**
     * @return eka naytettava kentta
     */
    public int ekaKentta() {
    	return 1;
    }
    
    /**
     * Antaa k:n kentan sisallon avain-merkkijonona
     * jonka perusteella voi lajitella
     * @param k  sfd
     * @return sdf 
     */
   public String getAvain(int k) {
        try {
            return kentat[k].getAvain();
        } catch (Exception ex) {
            return "";
        }
    }
   
    
    
    /**
     * Palauttaa k:tta pelaajan kenttaa vastaavan kysymyksen
     * @param k kuinka monennen kentan kysymys palautetaan (0-alkuinen)
     * @return k:nnetta kenttaa vastaava kysymys
     */
    public String getKysymys(int k) {
    	switch ( k ) {
        case 0: return "Tunnus nro";
        case 1: return "nimi";
        case 2: return "syntym‰p‰iv‰";
        case 3: return "pituus";
        case 4: return "paino";
        case 5: return "kotimaa";
        case 6: return "puhelinnumero";
        case 7: return "kausimaksu";
        case 8: return "vakuutus";
        default: return "ƒ‰liˆ";
        }
    }
    
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
        	syntymapaiva = tjono;
            return null;
        case 3:
            pituus = Mjonot.erotaInt(tjono, 0);
            return null;
        case 4:
            paino = Mjonot.erotaInt(tjono, 0);
            return null;
        case 5:
            kotimaa = tjono;
            return null;
        case 6:
            puhelinnumero = tjono;
            return null;
        case 7:
            kausimaksu = tjono;
            return null;
        case 8:
            vakuutus = tjono;
            return null;
        default:
            return "ƒ‰liˆ";
        }
    }
    
    /**
     * Antaa k:n kent‰n sis‰llˆn merkkijonona
     * @param k monenenko kent‰n sis‰ltˆ palautetaan
     * @return kent‰n sis‰ltˆ merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + syntymapaiva;
        case 3: return "" + pituus;
        case 4: return "" + paino;
        case 5: return "" + kotimaa;
        case 6: return "" + puhelinnumero;
        case 7: return "" + kausimaksu;
        case 8: return "" + vakuutus;
        default: return "ƒ‰‰liˆ";
        }
    }


    
    /**
     * Palautetaan pelaajan tunnusnumero
     * @return Pelaajan tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Palautetaan pelaajan nimi
     * @return pelaajan nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa ett‰
     * seuraava numero on aina suurempi kuin tahan menness‰ suurin.
     * @param nro asetettava tunnusnumero
     */
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }

    /**
     * Palautetaan kehen pelaajaan Pelaaja viittaa
     * @return pelaajan tunnusnumero
     */
    public int getPelaaja() {
        return tunnusNro;
    }

    /**
     * Palauttaa pelaajan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return pelaajan tiedot tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("2|Lionel Messi|171");
     *   pelaaja.toString().startsWith("2|Lionel Messi|171") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                nimi + "|" +
                syntymapaiva + "|" +
                pituus + "|" +
                paino + "|" +     
                kotimaa + "|" +
                puhelinnumero + "|" +
                kausimaksu + "|" +
                vakuutus;
    }

    /**
     * Selvittaa pelaajan tiedot | erotellusta merkkijonosta
     * Pitaa huolen ett‰ seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta jasenen tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("2|Lionel Messi|171");
     *   pelaaja.getTunnusNro() === 2;
     *   pelaaja.toString().startsWith("2|Lionel Messi|171") === true;
     *   pelaaja.rekisteroi();
     *   int n = pelaaja.getTunnusNro();
     *   pelaaja.parse(""+(n+3));
     *   pelaaja.rekisteroi();
     *   pelaaja.getTunnusNro() === n+3+1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    
    /**
     * Tutkii onko parametrina annettu merkkijono
     * @param s annettu merkkijono
     * 
     */
    /*
    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }
    */
    /**
     * Tutkii onko parametrina annetun pelaajan tiedot samat kuin pelaajan tiedot
     * @param pelaaja vertailtava pelaaja
     * @return tosi jos ovat samat
     */
    public boolean equals(Pelaaja pelaaja) {
        if ( pelaaja == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(pelaaja.anna(k)) ) return false;
        return true;
    }

    @Override
    public boolean equals(Object pelaaja) {
        if ( pelaaja instanceof Pelaaja ) return equals((Pelaaja)pelaaja);
        return false;
    }


    @Override
    public int hashCode() {
        return getTunnusNro();
    }
    
    /**
     * Tehd‰‰n identtinen klooni pelaajasta
     */
    @Override
    public Pelaaja clone() throws CloneNotSupportedException {
    	 Pelaaja uusi;
    	 uusi = (Pelaaja) super.clone();
    	 return uusi;
    }
    
    /**
     * Testiohjelma pelaajalle.
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Pelaaja pel = new Pelaaja();
        pel.vastaaMikki(2);
        pel.tulosta(System.out);
    }

}
