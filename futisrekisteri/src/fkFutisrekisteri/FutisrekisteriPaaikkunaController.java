package fkFutisrekisteri;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import FutisrekisteriTietokanta.Pelaaja;
import FutisrekisteriTietokanta.PoikkeusException;
import FutisrekisteriTietokanta.Rekisteri;
import FutisrekisteriTietokanta.Tilasto;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;


/**
 * P‰‰ikkunan tapahtumat ovat t‰ss‰ luokassa.
 * @author Sampo Ruohonen
 * @version 16.5.2019
 */
public class FutisrekisteriPaaikkunaController implements Initializable {
	
    @FXML private TextField hakuehto;
	@FXML private Label labelVirhe;
	@FXML private ComboBoxChooser<String> cbKentat;
	@FXML private ScrollPane panelPelaaja;
	@FXML private ListChooser<Pelaaja> chooserPelaajat;	
	@FXML private TextField muokkausNimi;
    @FXML private TextField muokkausSyntymapaiva;
	@FXML private StringGrid<Tilasto> tableTilastot; 
	@FXML private GridPane gridPelaaja;
	
	private Rekisteri rekisteri;
	private String seurannimi = "Barcelona";
	private Pelaaja pelaajaKohdalla;
	private TextField muokkaukset[];
	//private int kentta = 0;
	private static Tilasto aputilasto = new Tilasto();
	private static Pelaaja apupelaaja = new Pelaaja();
	
	@Override
    public void initialize(URL url, ResourceBundle bundle) {
	    alusta();
    }

	/**
	 * Asetetaan pelipaikkojen leveys
	 */
	public void pelipaikat(){
	    tableTilastot.setColumnWidth(1, 40);
	}
	
	
	@FXML private void handleHakuehto() {
		hae(0);
	}
	
	@FXML
    void handleLisaaTilastotieto() {
	    uusiTilasto();
    }

    @FXML
    void handleLisaaUusiPelaaja() {
    	uusiPelaaja();
    }
    	
	@FXML
    void handleMuokkaaTilastoa(ActionEvent event) {
	    muokkaaTilastoa();
    }
	
	@FXML
    void handleAvaa() {
	    avaa();
    }

    @FXML
    void handleLopeta() {
        tallenna();
        Platform.exit();
    }

    @FXML
    void handlePoistaPelaaja() throws PoikkeusException {
    	poistaPelaaja();
    }

    @FXML
    void handlePoistaTilastotieto() {
    	poistaTilasto();
    }

    @FXML
    void handleTallenna() {
    	tallenna();
    }

    @FXML
    void handleTallennaMenu() {
        tallenna();    
    }
    
    @FXML
    void handleUusiPelaaja() {
    	uusiPelaaja();
    }

    /**
     * Kysyt‰‰n tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
     public boolean avaa() {
         String uusinimi = FutisrekisteriGUIController.kysyNimi(null, seurannimi);
         if (uusinimi == null) {
             System.out.println("uusinimi on null");
             return false;
         }
         lueTiedosto(uusinimi);
         return true;
     }
    
     /**
      * Tekee tarvittavat muut alustukset. Luodaan pelaajataulu ja tilastotaulu. 
      * Alustetaan n‰ille kuuntelijat.
      */
     protected void alusta() {
         chooserPelaajat.clear();
         chooserPelaajat.addSelectionListener(e -> naytaPelaaja());
         cbKentat.clear();
         for (int k = apupelaaja.ekaKentta(); k < apupelaaja.getKenttia(); k++)  
             cbKentat.add(apupelaaja.getKysymys(k), null);
         cbKentat.getSelectionModel().select(0);
         muokkaukset = FutisrekisteriMuokkausController.luoKentat(gridPelaaja); 
         for (TextField muokkaus : muokkaukset) 
             if ( muokkaus != null ) { 
                 muokkaus.setEditable(false); 
                 muokkaus.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(FutisrekisteriMuokkausController.getFieldId(e.getSource(),0)); }); 
                 //muokkaus.focusedProperty().addListener((a,o,n) -> kentta = FutisrekisteriMuokkausController.getFieldId(muokkaus,0)); 
             }
         String otsikot[] = new String[aputilasto.getKenttia()-aputilasto.ekaKentta()];
         for (int i=0, k=aputilasto.ekaKentta(); k < aputilasto.getKenttia(); i++, k++)
             otsikot[i] = aputilasto.getKysymys(k);
         tableTilastot.initTable(otsikot); 
         tableTilastot.setPlaceholder(new Label("Ei viel‰ tilastoja")); 
         tableTilastot.setEditable(true);
         tableTilastot.setOnCellString( (g, tilasto, defValue, r, c) -> tilasto.anna(c+tilasto.ekaKentta()) );
         tableTilastot.setOnGridLiveEdit((g, tilasto, defValue, r, c, edit) -> {
             String virhe = tilasto.aseta(c+tilasto.ekaKentta(), defValue);
             if ( virhe == null ) {
                 edit.setStyle(null);
                 rekisteri.setTilastotMuutos();
                 Dialogs.setToolTipText(edit,"");
             } else {
                 edit.setStyle("-fx-background-color: red");
                 Dialogs.setToolTipText(edit,virhe);
             }
             return defValue;
         });
     }

     /**
      * Naytetaan tilastot taulukkoon
      * @param pelaaja kenen tilastot naytetaan
      */
     private void naytaTilastot(Pelaaja pelaaja) {
     	tableTilastot.clear();
         if ( pelaaja == null ) return;
         try {
             List<Tilasto> tilastot = rekisteri.annaTilastot(pelaaja);
             if ( tilastot.size() == 0 ) return;
             tableTilastot.add(tilastot);
         } catch (Exception e) {
         	Dialogs.showMessageDialog("Virhe on " + e);
         } 
     }  
    
    /**
  	* Alustaa kerhon lukemalla sen valitun nimisest‰ tiedostosta
  	* @param nimi tiedosto josta kerhon tiedot luetaan
    * @return null jos luku onnistuu muuten virhe tekstina
    */
   	protected String lueTiedosto(String nimi) {
	    try {
	        rekisteri.lueTiedostosta(nimi);
	        hae(0);
	        return null;
	    } catch (PoikkeusException ex) {
	        hae(0);
	        String virhe = ex.getMessage(); 
	        if ( virhe != null ) Dialogs.showMessageDialog("Virhe on " + virhe);
	        return virhe;
	    }
   	}

  	/**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstina
     */
    private String tallenna() {
        try {
            rekisteri.tallenna();
            System.out.println("Tallennus onnistui");
            return null;
        } catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
  	
    private void muokkaa(int k) { 
    	 if (pelaajaKohdalla == null) return; 
    	 try { 
    		 Pelaaja pelaaja; 
    		 pelaaja = FutisrekisteriMuokkausController.kysyPelaaja(null, pelaajaKohdalla.clone(),0); //FutisrekisteriMuokkausController.kysyPelaaja(null, pelaajaKohdalla.clone(), k); 
    		 if (pelaaja == null) return; 
    		 rekisteri.korvaaTaiLisaa(pelaaja);
    		 hae(pelaaja.getTunnusNro()); 
    	 } catch (CloneNotSupportedException e) { 
    		 Dialogs.showMessageDialog(e.getMessage());
    	 } catch (PoikkeusException e) { 
    		 Dialogs.showMessageDialog(e.getMessage()); 
    	 } 
    }     
    
    /**
     * Poistetaan tilastoista valittu tilasto
     */
    private void poistaTilasto() {
        int rivi = tableTilastot.getRowNr();
        if ( rivi < 0 ) return;
        Tilasto tilasto = tableTilastot.getObject();
        if ( tilasto == null ) return;
        rekisteri.poistaTilasto(tilasto);
        naytaTilastot(pelaajaKohdalla);
        int tilastoja = tableTilastot.getItems().size(); 
        if ( rivi >= tilastoja ) rivi = tilastoja -1;
        tableTilastot.getFocusModel().focus(rivi);
        tableTilastot.getSelectionModel().select(rivi);
    }


    /**
     * Poistetaan listalta valittu j‰sen
     * @throws PoikkeusException 
     */
    private void poistaPelaaja() throws PoikkeusException {
        Pelaaja pelaaja = pelaajaKohdalla;
        if (pelaaja == null) return;
        if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko pelaaja: " + pelaaja.getNimi(), "Kyll‰", "Ei"))
            return;
        rekisteri.poista(pelaaja);
        int index = chooserPelaajat.getSelectedIndex();
        hae(0);
        chooserPelaajat.setSelectedIndex(index);
    }
    
  	/**
  	* N‰ytt‰‰ listasta valitun pelaajan tiedot
  	*/
  	protected void naytaPelaaja() {
  	    pelaajaKohdalla = chooserPelaajat.getSelectedObject();
  	    if (pelaajaKohdalla == null) return;
  	    FutisrekisteriMuokkausController.naytaPelaaja(muokkaukset, pelaajaKohdalla);
  	    naytaTilastot(pelaajaKohdalla);
  	    gridPelaaja.setVisible(pelaajaKohdalla != null);
  	}

  	/**
     * Hakee pelaajien tiedot listaan
     * @param jnro pelaajan tunnusnro numero, joka aktivoidaan haun jalkeen
     */
    protected void hae(int jnr) {
        int jnro = jnr;
        if ( jnro <= 0 ) {
            Pelaaja kohdalla = pelaajaKohdalla;
            if ( kohdalla != null ) jnro = kohdalla.getTunnusNro();           
        }
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apupelaaja.ekaKentta();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        chooserPelaajat.clear();
        int index = 0;
        ArrayList<Pelaaja> pelaajat;
        try {
            pelaajat = rekisteri.etsi(ehto, k);
            int i = 0;
            for (Pelaaja pelaaja:pelaajat) {
                if (pelaaja.getTunnusNro() == jnro) index = i;
                chooserPelaajat.add(pelaaja.getNimi(), pelaaja);
                i++;
            }
        } catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Pelaajan hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserPelaajat.getSelectionModel().select(index);
    }
    
    /**
     * Luo uuden pelaajan jota aletaan editoimaan 
     */
    protected void uusiPelaaja() {
        try {
        	Pelaaja uusi = new Pelaaja();
        	uusi = FutisrekisteriMuokkausController.kysyPelaaja(null, uusi,0);
        	if (uusi == null) return;
        	uusi.rekisteroi();
        	rekisteri.lisaa(uusi);
        	hae(uusi.getTunnusNro());
        } catch (PoikkeusException poikkeus) {
            Dialogs.showMessageDialog("Ongelmia uuden pelaajan luomisessa " + poikkeus.getMessage());
            return;
        }
        
    }
    
    /**
     * Tekee uuden tyhjan tilastomerkinnan editointia varten
     */
     public void uusiTilasto() {
        if (pelaajaKohdalla == null ) return; 
        try {
            Tilasto uusi = new Tilasto();
            uusi.rekisteroi();
            uusi.vastaaMikki(pelaajaKohdalla.getTunnusNro());
            rekisteri.lisaa(uusi);
        } catch (PoikkeusException e) {
            Dialogs.showMessageDialog("Ongelmia lis‰‰misess‰! " + e.getMessage());
        }
        hae(pelaajaKohdalla.getTunnusNro());   
    }
    
    /**
     * Muokkaa valittua tilastomerkintaa
     */
    private void muokkaaTilastoa() {
        int r = tableTilastot.getRowNr();
        if ( r < 0 ) return;
        Tilasto til = tableTilastot.getObject();
        if (til == null) return;
        try {
            til = tableTilastot.getObject(r).clone();
            if (til == null) return;
            rekisteri.korvaaTaiLisaa(til); 
            naytaTilastot(pelaajaKohdalla); 
            tableTilastot.selectRow(r);
        } catch (CloneNotSupportedException  e) {//
        } catch (PoikkeusException e) {
            Dialogs.showMessageDialog("Ongelmia lis‰‰misess‰: " + e.getMessage());
        }
    }
    
    /**
     * @param rekisteri rekisteri jota kaytetaan tassa kayttoliittymassa
     */
    public void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
        naytaPelaaja();
    }

}