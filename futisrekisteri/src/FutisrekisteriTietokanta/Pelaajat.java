package FutisrekisteriTietokanta;
import java.util.*;

import fi.jyu.mit.ohj2.WildChars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Luokka kaikkien pelaajien hoitamiseksi. Osaa tallentaa pelaajat, hakea pelaajia, avaa pelaajat tiedostosta.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class Pelaajat implements Iterable<Pelaaja> {

    private String kokoNimi = "";
    private boolean muuttunutTieto = false;
    private String tiedostonPerusNimi = "pelaajat";
    private int lkm = 0;
        /**
         *  Taulukko Pelaajista 
         */
        private final ArrayList<Pelaaja> alkiot = new ArrayList<Pelaaja>();
        
        /**
         * Pelaajien alustaminen
         */
        public Pelaajat() {
            // 
        }


        /**
         * Lisaa uuden Pelaajan tietorakenteeseen.  Ottaa Pelaajan omistukseensa.
         * @param pelaaja Lisattava Pelaaja. Tietorakenne muuttuu omistajaksi.
         */
        public void lisaa(Pelaaja pelaaja) {
            alkiot.add(pelaaja);
            lkm++;
            muuttunutTieto = true;
        }


        /**
         * Lukee pelaajat tiedostosta.
         * @param nimi tiedoston perusnimi
         * @throws PoikkeusException kun lukeminen epaonnistuu
		 * @example
		 * <pre name="test">
		 * #THROWS PoikeeusException 
		 * #import java.io.File;
		 *  Pelaajat pel = new Pelaajat();
		 *  Pelaaja aku1 = new Pelaajat(), aku2 = new Pelaajat();
		 *  aa1.vastaaMikki(1);
		 *  aa2.vastaaMikki(2);
		 *  String hakemisto = "testipelaajat";
		 *  String tiedNimi = hakemisto+"/nimet";
  	  	 *  File ftied = new File(tiedNimi+".dat");
  	     *  File dir = new File(hakemisto);
  	     *  dir.mkdir();
     	 *  ftied.delete();
         *  pel.lueTiedostosta(tiedNimi); #THROWS PoikkeusException
         *  pel.lisaa(aku1);
         *  pel.lisaa(aku2);
         *  pel.tallenna();
         *  pel = new Pelaajat();
         *  pel.lueTiedostosta(tiedNimi);
         *  Iterator<Pelaaja> i = pel.iterator();
         *  i.next() === aa1;
     	 *  i.next() === aa2;
     	 *  i.hasNext() === false;
     	 *  pel.lisaa(aa2);
      	 *  pel.tallenna();
     	 *  ftied.delete() === true;
     	 *  File fbak = new File(tiedNimi+".bak");
     	 *  fbak.delete() === true;
     	 *  dir.delete() === true;
     	 * </pre>
         */
        public void lueTiedostosta(String nimi) throws PoikkeusException {        
            setTiedostonPerusNimi(nimi);
            try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
                String rivi = fi.readLine();
                while ((rivi = fi.readLine()) != null) {
                    rivi = rivi.trim();
                    if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                    Pelaaja pelaaja = new Pelaaja();
                    pelaaja.parse(rivi);
                    lisaa(pelaaja);
                }
                muuttunutTieto = false;
            } catch (FileNotFoundException e) {
                throw new PoikkeusException("Tiedosto " + getTiedostonNimi() + " ei aukea");
            } catch (IOException e) {
                throw new PoikkeusException("Ongelmia tiedoston kanssa: " + e.getMessage());
            }
        }
        
        /**
         * Korvaa pelaajan tietorakenteessa. Ottaa pelaajan omistukseensa.
         * Etsitaan samalla tunnusnumerolla oleva pelaaja.  Jos ei loydy,
         * niin lisat‰‰n uutena pelaajana.
         * @param pelaaja lisattava pelaaja
         * @throws PoikkeusException jos jokin menee pieleen
         */
        public void korvaaTaiLisaa(Pelaaja pelaaja) throws PoikkeusException {
            int id = pelaaja.getTunnusNro();
            for (int i = 0; i < alkiot.size(); i++) {
                if (alkiot.get(i).getTunnusNro() == id) {
                    alkiot.set(i, pelaaja);
                    muuttunutTieto = true;
                    return;
                }
            }
            lisaa(pelaaja);
        }
        
        /**
         * Luetaan aikaisemmin annetun nimisest‰ tiedostosta.
         * @throws PoikkeusException 
         */
        public void lueTiedostosta() throws PoikkeusException{
        	lueTiedostosta(getTiedostonPerusNimi());
        }
        
        /** 
         * Poistaa pelaajan jolla on valittu tunnusnumero  
         * @param id poistettavan pelaajan tunnusnumero 
         * @return 1 jos poistettiin, 0 jos ei lˆydy   
         */ 
        public int poista(int id) { 
            Pelaaja pelaaja = annaId(id);
            alkiot.remove(pelaaja);
            muuttunutTieto = true;
            return 1;
        } 
        
        /**
         * Tallentaa pelaajat tiedostoon.  
         * @throws PoikkeusException jos talletus epaonnistuu
         */
        public void tallenna() throws PoikkeusException {
            if (!muuttunutTieto) return;
            File fbak = new File(getBakNimi());
            File ftied = new File(getTiedostonNimi());
            fbak.delete();
            ftied.renameTo(fbak);
            try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
                //fo.println(getKokoNimi());
                fo.println(alkiot.size());
                for (Pelaaja pelaaja : this) {
                    fo.println(pelaaja.toString());
                }
            } catch ( FileNotFoundException ex ) {
                throw new PoikkeusException("Tiedosto " + ftied.getName() + " ei aukea");
            } catch ( IOException ex ) {
                throw new PoikkeusException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
            }
            muuttunutTieto = false;
        }


        /**
         * Palauttaa Pelaajamerkintojen lukumaaran
         * @return harrastusten lukumaara
         */
        public int getLkm() {
            return alkiot.size();
        }


        /**
         * Iteraattori kaikkien pelaajamerkintojen lapikaymiseen
         * @return Pelaajaiteraattori
         * @example
         * <pre name="test">
         * #PACKAGEIMPORT
         * #import java.util.*;
         *  Pelaajat Pelaajao = new Pelaajat();
         *  Pelaaja merkintaa1 = new Pelaaja(2); Pelaajao.lisaa(merkintaa1);
         *  Pelaaja merkintaa2 = new Pelaaja(1); Pelaajao.lisaa(merkintaa2);
         *  Pelaaja merkintaa3 = new Pelaaja(2); Pelaajao.lisaa(merkintaa3);
         *  Pelaaja merkintaa4 = new Pelaaja(1); Pelaajao.lisaa(merkintaa4);
         *  Pelaaja merkintaa5 = new Pelaaja(2); Pelaajao.lisaa(merkintaa5);
         *  Iterator<Pelaaja> i2=Pelaajao.iterator();
         *  i2.next() === merkintaa1;
         *  i2.next() === merkintaa2;
         *  i2.next() === merkintaa3;
         *  i2.next() === merkintaa4;
         *  i2.next() === merkintaa5;
         *  int n = 0;
         *  int jnrot[] = {2,1,2,1,2};
         *  for ( Pelaaja til : Pelaajao ) { 
         *    til.getPelaaja() === pelaajat[n]; n++;  
         *  }
         *  n === 5;
         *  
         * </pre>
         */
        @Override
        public Iterator<Pelaaja> iterator() {
            return alkiot.iterator();
        }

        /**
         * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
         * @return tallennustiedoston nimi
         */
        public String getTiedostonPerusNimi() {
            return tiedostonPerusNimi;
        }


        /**
         * Palauttaa seuran koko nimen
         * @return seuran koko nimi merkkijononna
         */
        public String getKokoNimi() {
            return kokoNimi;
        }

        /**
         * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
         * @return tallennustiedoston nimi
         */
        public String getTiedostonNimi() {
            return getTiedostonPerusNimi() + ".dat";
        }

        /**
         * Asettaa tiedoston perusnimen ilman tarkenninta
         * @param nimi tallennustiedoston perusnimi
         */
        public void setTiedostonPerusNimi(String nimi) {
            tiedostonPerusNimi = nimi;
        }
        
        /**
         * Palauttaa varakopiotiedoston nimen
         * @return varakopiotiedoston nimi
         */
        public String getBakNimi() {
            return tiedostonPerusNimi + ".bak";
        }


        
        /**
         * Haetaan indeksin paikalta pelaajaa
         * @param i indeksi josta haetaan
         * @return annetun indeksin paikalta pelaaja
         * @throws IndexOutOfBoundsException jos i ei sallitulla alueella
         */
        public Pelaaja annaPelaaja(int i) throws IndexOutOfBoundsException {
            if (i < 0 || lkm <= i)
                throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
            return alkiot.get(i);
        }


        /** 
         * Palauttaa taulukossa hakuehtoon vastaavien pelaajiin viitteet 
         * @param hakuehto hakuehto 
         * @param i etsitt‰v‰n kent‰n indeksi  
         * @return tietorakenteen lˆytyneist‰ pelaajista 
         *   // TODO: toistaiseksi palauttaa kaikki j‰senet 
         */ 
        public ArrayList<Pelaaja> etsi(String hakuehto, int i) { 
        	String ehto = "*"; 
        	if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        	ArrayList<Pelaaja> loytyneet = new ArrayList<Pelaaja>(); 
        	int hk = i;
        	if (hk < 0) hk = 1;
            for (Pelaaja pelaaja : this) { 
            	if (WildChars.onkoSamat(pelaaja.anna(hk), ehto)) loytyneet.add(pelaaja);   
            } 
            Collections.sort(loytyneet, new Pelaaja.Vertailija(hk));
            return loytyneet; 
        }
        
        
        /** 
         * Etsii pelaajan id:n perusteella 
         * @param id etsittava id
         * @return pelaajan id
         */
        public Pelaaja annaId(int id) { 
            for (Pelaaja pelaaja : this) { 
                if (id == pelaaja.getTunnusNro()) return pelaaja; 
            } 
            return null; 
        }
        
        /** 
         * Etsii pelaajan id:n perusteella 
         * @param id etsittava id
         * @return pelaajan id
         */
        public int etsiId(int id) { 
            for (int i = 0; i < alkiot.size(); i++) 
                if (id == alkiot.get(i).getTunnusNro()) return i; 
            return -1; 
        }
        
        /**
         * Testiohjelma Pelaajamerkinnalle
         * @param args ei kaytossa
         */
        public static void main(String[] args) {
            Pelaajat t = new Pelaajat();
            Pelaaja merkinta1 = new Pelaaja();
            merkinta1.vastaaMikki(2);
            Pelaaja merkinta2 = new Pelaaja();
            merkinta2.vastaaMikki(1);
            Pelaaja merkinta3 = new Pelaaja();
            merkinta3.vastaaMikki(2);
            Pelaaja merkinta4 = new Pelaaja();
            merkinta4.vastaaMikki(2);
            Pelaaja merkinta5 = new Pelaaja();
            merkinta5.vastaaMikki(2);

            t.lisaa(merkinta1);
            t.lisaa(merkinta2);
            t.lisaa(merkinta3);
            t.lisaa(merkinta4);
            t.lisaa(merkinta5);

            System.out.println("===============Pelaajat===================");

        //    Pelaaja pel = t.annaPelaaja(2);

            for (Pelaaja pelaaja : t) {
                System.out.print(pelaaja.getPelaaja() + " ");
                pelaaja.tulosta(System.out);
            }

        }

    }

