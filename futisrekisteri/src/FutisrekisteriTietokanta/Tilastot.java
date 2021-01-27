package FutisrekisteriTietokanta;
import java.io.*;
import java.util.*;

/**
 * Tilastot-luokka joka hoitaa tilastotiedot. Osaa tallentaa ja avata tilastot, poistaa tilastoista.
 * @author Sampo Ruohonen
 * @version 15.5.2019
 */
public class Tilastot implements Iterable<Tilasto>{
    
    private static int TilastojaEnintaan = 5;
    private int tilastoja = 0;
    private boolean muutettuTieto = false;
    private String tiedostonPerusNimi = "tilastot";
    private Tilasto merkinnat[] = new Tilasto[TilastojaEnintaan];

        /**
         * Tilastojen alustaminen
         */
        public Tilastot() {
            // 
        }

        /**
         * Lisää uuden tilaston tietorakenteeseen. Ottaa tilaston omistukseensa.
         * @param tilasto Lisattava tilasto. Tietorakenne muuttuu omistajaksi.
         * @throws PoikkeusException jos liikaa alkioita
         */
        public void lisaa(Tilasto tilasto) throws PoikkeusException{
            if (tilastoja >= merkinnat.length) {
                TilastojaEnintaan = TilastojaEnintaan * 2;
                Tilasto valiaik[] = new Tilasto[TilastojaEnintaan];
                for (int i = 0; i < merkinnat.length; i++) {
                    valiaik[i] = merkinnat[i];          
                }
                merkinnat = valiaik;
            }
            merkinnat[tilastoja] = tilasto;
            tilastoja++;
            muutettuTieto = true;
        }
        
        /**
         * Korvaa tilaston tietorakenteeseen. Ottaa tilaston omistukseensa. Jos ei löydy,
         * niin lisätään uutena tilastona.
         * @param tilasto Lisattava tilasto. Tietorakenne muuttuu omistajaksi.
         * @throws PoikkeusException jos liikaa alkioita
         */
        public void korvaaTaiLisaa(Tilasto tilasto) throws PoikkeusException{
            int id = tilasto.getTilastoNro();
            for (int i = 0; i < tilastoja; i++) {
                if (merkinnat[i].getTunnusNro() == id) {
                    merkinnat[i] = tilasto;
                    return;
                }
            }  
            if (tilastoja >= merkinnat.length) {
                TilastojaEnintaan = TilastojaEnintaan * 2;
                Tilasto valiaik[] = new Tilasto[TilastojaEnintaan];
                for (int i = 0; i < merkinnat.length; i++) {
                    valiaik[i] = merkinnat[i];          
                }
                merkinnat = valiaik;
            }
            merkinnat[tilastoja] = tilasto;
            tilastoja++;
            muutettuTieto = true;
        }
       
        /**   
         * Laitetaan muutos, jolloin pakotetaan tallentamaan.     
         */   
        public void setMuutos() {   
            muutettuTieto = true;   
        }
        
        /**
         * Poistaa valitun tilastomerkinnän
         * @param tilasto poistettava tilasto
         */
        public void poista(Tilasto tilasto) {
        	int a = tilasto.getTilastoNro();
        	tilastoja--;
        	for (int i = a; i < tilastoja; i++) {
        		merkinnat[i] = merkinnat[i + 1];
        	}
        	merkinnat[tilastoja] = null;
        	muutettuTieto = true;
        }

        /**
         * Poistaa kaikki tietyn tietyn pelaajan tilastomerkinnat
         * @param tunnusNro viite pelaajaan, mihin liittyvät tilastomerkinnat poistetaan
         * @return montako poistettiin
         */
        public int poistaPelaajanTilastomerkinnat(int tunnusNro) {
            int n = 0;
            int[] poistettavat = new int[TilastojaEnintaan];
            for (int i = 0; i< tilastoja; i++) {
                if (merkinnat[i].getTunnusNro() == tunnusNro) {
                    poistettavat[n] = i;
                	n++;
                }
            }
                      
            for (int j = n; j > 0; j--) {
            	poista(merkinnat[poistettavat[n]]);
            }
            return n;
        }
        
        /**
         * Lukee tilastot tiedostosta.
         * @param nimi tiedoston nimi
         * @throws PoikkeusException kun lukeminen epäonnistuu
         * @example
         * <pre name="test">
         * #THROWS PoikkeusException 
         * #import java.io.File;
         *  Tilastot til = new Tilastot();
         *  Tilasto aa1 = new Tilasto(); aa1.vastaaMikki(2);
         *  Tilasto aa2 = new Tilasto(); aa2.vastaaMikki(1);
         *  Tilasto aa3 = new Tilasto(); aa3.vastaaMikki(4); 
     	 *  Tilasto aa4 = new Tilasto(); aa4.vastaaMikki(3); 
     	 *  Tilasto aa5 = new Tilasto(); aa5.vastaaMikki(5); 
     	 *  String tiedNimi = "testitilastot";
       	 *  File ftied = new File(tiedNimi+".dat");
     	 *  ftied.delete();
     	 *  til.lueTiedostosta(tiedNimi); #THROWS PoikkeusException
     	 *  til.lisaa(aa1);
     	 *  til.lisaa(aa2);
     	 *  til.lisaa(aa3);
     	 *  til.lisaa(aa4);
     	 *  til.lisaa(aa5);
     	 *  til.tallenna();
     	 *  til = new Tilastot();
     	 *  til.lueTiedostosta(tiedNimi);
     	 *  Iterator<Tilasto> i = til.iterator();
     	 *  i.next().toString() === aa1.toString();
      	 *  i.next().toString() === aa2.toString();
     	 *  i.next().toString() === aa3.toString();
     	 *  i.next().toString() === aa4.toString();
     	 *  i.next().toString() === aa5.toString();
     	 *  i.hasNext() === false;
     	 *  til.lisaa(aa4);
     	 *  til.tallenna();
     	 *  ftied.delete() === true;
     	 *  File fbak = new File(tiedNimi+".bak");
     	 *  fbak.delete() === true;
     	 * </pre>
     	 */
        public void lueTiedostosta(String nimi) throws PoikkeusException {
            setTiedostonPerusNimi(nimi);
            try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
            	String rivi = fi.readLine();
                while ((rivi = fi.readLine()) != null) {
                    rivi = rivi.trim();
                    if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                    Tilasto til = new Tilasto();
                    til.parse(rivi);
                    lisaa(til);
                }
                muutettuTieto = false;
            } catch (FileNotFoundException e) {
                throw new PoikkeusException("Tiedosto " + getTiedostonNimi() + " ei aukea");
            } catch (IOException e) {
                throw new PoikkeusException("Ongelmia tiedoston kanssa: " + e.getMessage());
            }
        }
        
        /**
         * Luetaan aikaisemmin annetun nimisestä tiedostosta
         * @throws PoikkeusException 
         */
        public void lueTiedostosta() throws PoikkeusException {
        	lueTiedostosta(getTiedostonPerusNimi());
        }

        /**
         * Tallentaa pelaajat tiedostoon.  
         * @throws PoikkeusException jos talletus epäonnistuu
         */
        public void tallenna() throws PoikkeusException {
            if (muutettuTieto == false) return;
            File fbak = new File(getBakNimi());
            File ftied = new File(getTiedostonNimi());
            fbak.delete();
            ftied.renameTo(fbak); 
            try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            	fo.println(getTiedostonNimi());
            	//fo.println("#" + tilastoja);//voisi lisätä mutta ei saa toimimaan
                
            	/*for (Tilasto tilasto : this) {
                    fo.println(tilasto.toString());
                }*/	
            	for (int i = 0; i < tilastoja; i++) {
                    fo.println(merkinnat[i].toString());
                }
            } catch (FileNotFoundException ex) {
                throw new PoikkeusException("Tiedosto " + ftied.getName() + " ei aukea");
            } catch (IOException ex) {
                throw new PoikkeusException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
            }
            muutettuTieto = false;
        }
        

		/**
         * Palauttaa tilastomerkintojen lukumaaran
         * @return tilastomerkintojen lukumaara
         */
        public int getLkm() {
            return tilastoja;
        }

        /**
         * Asettaa tiedoston perusnimen ilan tarkenninta
         * @param tied tallennustiedoston perusnimi
         */
        public void setTiedostonPerusNimi(String tied) {
            tiedostonPerusNimi = tied;
        }

        /**
         * Palauttaa tiedoston nimen, jota käytetään tallennukseen
         * @return tallennustiedoston nimi
         */
        public String getTiedostonPerusNimi() {
            return tiedostonPerusNimi;
        }

        /**
         * Palauttaa tiedoston nimen, jota käytetään tallennukseen
         * @return tallennustiedoston nimi
         */
        public String getTiedostonNimi() {
            return tiedostonPerusNimi + ".dat";
        }

        /**
         * Palauttaa varakopiotiedoston nimen
         * @return varakopiotiedoston nimi
         */
        public String getBakNimi() {
            return tiedostonPerusNimi + ".bak";
        }
         
        /**
         * Luokka tilastojen iteroimiseksi.
         */
        
        public class TilastotIterator implements Iterator<Tilasto> {
            private int kohdalla = 0;

            /**
             * Onko olemassa vielä seuraavaa tilastoa
             * @see java.util.Iterator#hasNext()
             * @return true jos on vielä jäseniä
             */
            @Override
            public boolean hasNext() {
                return kohdalla < getLkm();
            }

            /**
             * Annetaan seuraava tilasto
             * @return seuraava tilasto
             * @throws NoSuchElementException jos seuraava alkiota ei enää ole
             * @see java.util.Iterator#next()
             */
            @Override
            public Tilasto next() throws NoSuchElementException {
                if (!hasNext()) throw new NoSuchElementException("Ei ole");
                return anna(kohdalla++);
            }


            /**
             * Tuhoamista ei ole toteutettu
             * @throws UnsupportedOperationException aina
             * @see java.util.Iterator#remove()
             */
            @Override
            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Ei poisteta");
            }
        }

        /**
         * Palautetaan iteraattori tilastoihin.
         * @return tilasto iteraattori
         */
        @Override
        public Iterator<Tilasto> iterator() {
            return new TilastotIterator();
        }
        
        
        /**
         * Palauttaa pelaajan kaikki tilastomerkinnat
         * @param tunnusNro tilastomerkinta halutaan
         * @return viite tilastomerkintaan, jonka indeksi i
         * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
         */
        public List<Tilasto> annaTilastot(int tunnusNro) throws IndexOutOfBoundsException {
            List<Tilasto> loydetyt = new ArrayList<Tilasto>();
            for (int j = 0; j < tilastoja; j++)
                 if (merkinnat[j].getTunnusNro() == tunnusNro) loydetyt.add(merkinnat[j]);
            return loydetyt;
        }
        
        /**
         * Palauttaa viitteen i:teen tilastoon.
         * @param i monennenko tilaston viite halutaan
         * @return viite tilastoon, jonka indeksi on i
         * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
         */
        protected Tilasto anna(int i) throws IndexOutOfBoundsException {
            if (i < 0 || tilastoja <= i)
                throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
            return merkinnat[i];
        }

        /**
         * Testiohjelma tilastomerkinnöille
         * @param args ei käytössä
         */
        public static void main(String[] args) {
            /*
            Tilastot t = new Tilastot();
            Tilasto merkinta1 = new Tilasto();
            merkinta1.vastaaMikki(2);
            Tilasto merkinta2 = new Tilasto();
            merkinta2.vastaaMikki(1);
            Tilasto merkinta3 = new Tilasto();
            merkinta3.vastaaMikki(2);
            Tilasto merkinta4 = new Tilasto();
            merkinta4.vastaaMikki(2);
            Tilasto merkinta5 = new Tilasto();
            merkinta5.vastaaMikki(2);
            
            try {
                t.lisaa(merkinta1);
                t.lisaa(merkinta2);
                t.lisaa(merkinta3);
                t.lisaa(merkinta4);
                t.lisaa(merkinta5);
            } catch (PoikkeusException e) {
                e.printStackTrace();
            }
            */

            System.out.println("===============Tilastot===================");

          /* TODO:   List<Tilasto> tilast = t.annaTilastot();

            for (Tilasto til : tilast) {
                System.out.print(til.getPelaaja() + " ");
                til.tulosta(System.out);
            } */

        }

    }

