package FutisrekisteriTietokanta;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

import fi.jyu.mit.ohj2.*;

/**
 * Tilastoluokka joka huolehtii yhdest‰ tilastosta.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class Tilasto implements Cloneable, Tietue {

    private int tunnusNro;
    private int tilastoNro;
    private String pelipaikka;
    private int vuosi;
    private int pelienMaara;

    private static int seuraavaNro = 1;


        /**
         * Uusi tilasto
         */
        public Tilasto() {
            //
        }


        /**
         * Alustetaan tietyn pelaajan tilasto.  
         * @param pelaaja pelaajan viitenumero 
         */
        public Tilasto(int pelaaja) {
            this.tunnusNro = pelaaja;
        }

        /**
         * @return tilaston kenttien lukumaara
         */
        @Override
        public int getKenttia() {
            return 5;
        }


        /**
         * @return ensimm‰inen kayttajan syotettavan kentan indeksi
         */
        @Override
        public int ekaKentta() {
            return 2;
        }
        
        /**
         * @param k mink‰ kentan kysymys halutaan
         * @return valitun kentan kysymysteksti
         */
        @Override
        public String getKysymys(int k) {
            switch (k) {
                case 0:
                    return "tunnusnumero";
                case 1:
                    return "tilastonumero";
                case 2:
                    return "pelipaikka";
                case 3:
                    return "vuosi";
                case 4:
                    return "pelien m‰‰r‰";
                default:
                    return "???";
            }
        }


        /**
         * @param k Minka kentan sisalto halutaan
         * @return valitun kentan sisalto
         */
        @Override
        public String anna(int k) {
            switch (k) {
                case 0:
                    return "" + tunnusNro;
                case 1:
                    return "" + tilastoNro;
                case 2:
                    return "" + pelipaikka;
                case 3:
                    return "" + vuosi;
                case 4:
                    return "" + pelienMaara;
                default:
                    return "???";
            }
        }
        
        /**
         * Asetetaan valitun kentan sisalto.  Mikali asettaminen onnistuu,
         * palautetaan null, muutoin virheteksti.
         * @param k minka kentan sisalto asetetaan
         * @param s asetettava sisalto merkkijonona
         * @return null jos ok, muuten virheteksti
         */
        @Override
        public String aseta(int k, String s) {
            String st = s.trim();
            StringBuffer sb = new StringBuffer(st);
            switch (k) {
                case 0:
                    tunnusNro = Mjonot.erota(sb, '|', getTunnusNro());
                    return null;
                case 1:
                    setTilastoNro(Mjonot.erota(sb, '|', getTilastoNro()));
                    return null;
                case 2:
                    pelipaikka = st;
                    return null;
                case 3:
                    vuosi = Mjonot.erotaInt(st, 0);
                    return null;
                case 4:
                    pelienMaara = Mjonot.erotaInt(st, 0);
                    return null;
                default:
                    return "V‰‰r‰ kent‰n indeksi";
            }
        }


        /**
         * Tehd‰‰n identtinen klooni tilastosta
         * @return Object kloonattu tilastomerkinta
         */
        @Override
        public Tilasto clone() throws CloneNotSupportedException { 
            return (Tilasto)super.clone();
        }

        /**
         * Apumetodi, jolla saadaan t‰ytetty‰ testiarvot Tilastomerkinn‰lle.
         * Pelattu pelipaikka, vuosi sek‰ pelien m‰‰r‰ arvotaan.
         * @param nro viite pelaajaan, jonka tilastomerkint‰ on
         */
        public void vastaaMikki(int nro) {
            tunnusNro = nro;
            pelipaikka = "karki";
            vuosi = ThreadLocalRandom.current().nextInt(1990, 2018 + 1);
            pelienMaara = ThreadLocalRandom.current().nextInt(0, 60 + 1);
        }


        /**
         * Tulostetaan tilaston tiedot
         * @param out tietovirta johon tulostetaan
         */
        public void tulosta(PrintStream out) {
            out.println("pelipaikka: " + pelipaikka + " pelien m‰‰r‰: " + pelienMaara + " vuonna: " + vuosi);
        }


        /**
         * Tulostetaan pelaajan tiedot
         * @param os tietovirta johon tulostetaan
         */
        public void tulosta(OutputStream os) {
            tulosta(new PrintStream(os));
        }


        /**
         * Antaa tilastolle seuraavan rekisterˆintinumeron.
         * @return tilaston uusi tunnusnumero
         * @example
         * <pre name="test">
         *   Tilasto til1 = new Tilasto();
         *   til1.getTunnusNro() === 0;
         *   til1.rekisteroi();
         *   Tilasto til2 = new Tilasto();
         *   til2.rekisteroi();
         *   int n1 = til1.getTunnusNro();
         *   int n2 = til2.getTunnusNro();
         *   n1 === n2-1;
         * </pre>
         */
        public int rekisteroi() {
            tilastoNro = seuraavaNro;
            seuraavaNro++;
            return tilastoNro;
        }


        /**
         * Palautetaan pelaajan tunnusnumero
         * @return pelaajan tunnusnumero
         */
        public int getTunnusNro() {
            return tunnusNro;
        }
        

        /**
         * Palautetaan kehen pelaajaan tilasto viittaa
         * @return tilastomerkinn‰n tunnusnumero
         */
        public int getTilastoNro() {
            return tilastoNro;
        }

        /**
         * Asettaa tilastonumeron ja samalla varmistaa etta
         * seuraava numero on aina suurempi kuin t‰h‰n menness‰ suurin.
         * @param nro asetettava tunnusnumero
         */
        private void setTilastoNro(int nro) {
            tilastoNro = nro;
            if (tilastoNro >= seuraavaNro) seuraavaNro = tilastoNro + 1;
        }
        
        /**
         * Palauttaa tilaston tiedot merkkijonona jonka voi tallentaa tiedostoon.
         * @return tilaston tiedot tolppaeroteltuna merkkijonona 
         */
        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("");
            String erotin = "";
            for (int k = 0; k < getKenttia(); k++) {
                sb.append(erotin);
                sb.append(anna(k));
                erotin = "|";
            }
            return sb.toString();
        }
        
        /**
         * Selvittaa tilaston tiedot tolpalla erotellusta merkkijonosta
         * Pitaa huolen etta seuraavaNro on suurempi kuin tuleva tunnusnro
         * @param rivi josta tilaston tiedot otetaan
         * </pre>
         */
        public void parse(String rivi) {
            StringBuffer sb = new StringBuffer(rivi);
            for (int k = 0; k < getKenttia(); k++)
                aseta(k, Mjonot.erota(sb, '|'));           
        }


        @Override
        public boolean equals(Object obj) {
            if ( obj == null ) return false;
            return this.toString().equals(obj.toString());
        }
        

        @Override
        public int hashCode() {
            return tilastoNro;
        }
        
        /**
         * Testiohjelma Harrastukselle.
         * @param args ei k‰ytˆss‰
         */
        public static void main(String[] args) {
            Tilasto til = new Tilasto();
            til.vastaaMikki(2);
            til.tulosta(System.out);
        }

    }


