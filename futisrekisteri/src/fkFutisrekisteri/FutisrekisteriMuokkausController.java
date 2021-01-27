package fkFutisrekisteri;


import java.net.URL;
import java.util.ResourceBundle;

import FutisrekisteriTietokanta.Pelaaja;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.Initializable;
import fi.jyu.mit.fxgui.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Pelaajan tietojen muokkaksen k‰sittely.
 * @author Sampo Ruohonen
 * @version 16.5.2019
 */
public class FutisrekisteriMuokkausController implements ModalControllerInterface<Pelaaja>, Initializable {
	
	@FXML
	private TextField muokkausSyntymapaiva;
	@FXML
	private TextField muokkausVakuutus;
	@FXML
	private TextField muokkausKausimaksu;
	@FXML
	private TextField muokkausPuhelinnumero;
	@FXML
	private TextField muokkausKotimaa;
	@FXML
	private TextField muokkausPaino;
	@FXML
	private TextField muokkausPituus;
	@FXML
	private TextField muokkausNimi;
	
	@FXML private ScrollPane panelPelaaja;
	@FXML private Label labelVirhe;
	@FXML private GridPane gridPelaaja;
	
	private Pelaaja pelaajaKohdalla;
	private static Pelaaja apu = new Pelaaja();
	private TextField[] muokkaukset;
	private int kentta = 0;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		 alusta();
	}
	
	@FXML
	void muokkausPeruuta(ActionEvent event) {
		pelaajaKohdalla = null;
		ModalController.closeStage(panelPelaaja);
	}

	@FXML
	void muokkausTallenna(ActionEvent event) {
		if (pelaajaKohdalla != null && pelaajaKohdalla.getNimi().trim().equals("") ) {
			System.out.println("Ei saa olla tyhj‰");
			naytaVirhe("Nimi ei saa olla tyhj‰");
			return;
		}
		ModalController.closeStage(labelVirhe);
	}

	/**
	* Tyhjennet‰‰n tekstikentat. 
	* @param muokkaukset taulukko jossa tyhjennettavia tekstikenttia
	*/
	public static void tyhjenna(TextField[] muokkaukset) {
		for (TextField muokkaus : muokkaukset)
			if (muokkaus != null) muokkaus.setText(""); 
	}
	
	/**
	 * Luo tekstikent‰t, joissa on pelaajan tiedot.
	 * @param gridPelaaja mihin luodaan
	 * @return tekstikent‰t listassa
	 */
	public static TextField[] luoKentat(GridPane gridPelaaja) {
		gridPelaaja.getChildren().clear();
        TextField[] muokkaukset = new TextField[apu.getKenttia()];  
        for (int i=0, k = apu.ekaKentta(); k < apu.getKenttia(); k++, i++) {
            Label label = new Label(apu.getKysymys(k));
            gridPelaaja.add(label, 0, i);
            TextField muokkaus = new TextField();
            muokkaukset[k] = muokkaus;
            muokkaus.setId("e"+k);
            gridPelaaja.add(muokkaus, 1, i);
        }
        return muokkaukset;
    }
	
	/**
	 * Tekee tarvittavat muut alustukset.
	 */
	protected void alusta() {
		muokkaukset = luoKentat(gridPelaaja);
		for (TextField muokkaus : muokkaukset) {
			if (muokkaus != null)
				muokkaus.setOnKeyReleased( e -> kasitteleMuutosPelaajaan((TextField)(e.getSource())));
		}
		panelPelaaja.setFitToHeight(true);
	}

	@Override
	public void setDefault(Pelaaja oletus) {
		pelaajaKohdalla = oletus;
		naytaPelaaja(muokkaukset, pelaajaKohdalla);
	}

	@Override
	public Pelaaja getResult() {
		return pelaajaKohdalla;
	}
	
	private void setKentta(int kentta) {
        this.kentta = kentta;
    }
	
	/**
     * Palautetaan komponentin id:st‰ saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mik‰ arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
	
	/**
	 * Fokus kentalle
	 */
	@Override
	public void handleShown() {
		kentta = Math.max(apu.ekaKentta(), Math.min(kentta, apu.getKenttia()-1));
        muokkaukset[kentta].requestFocus();
	}

	/**
     * Kasitellaan pelaajan muutos
     * @param muokkaus muuttunut kentta
     */
	private void kasitteleMuutosPelaajaan(TextField muokkaus) {
		 if (pelaajaKohdalla == null) return;
		 int k = getFieldId(muokkaus, apu.ekaKentta());
	        String s = muokkaus.getText();
	        String virhe = null;
	        virhe = pelaajaKohdalla.aseta(k,s); 
	        if (virhe == null) {
	            Dialogs.setToolTipText(muokkaus,"");
	            muokkaus.getStyleClass().removeAll("virhe");
	            naytaVirhe(virhe);
	        } else {
	            Dialogs.setToolTipText(muokkaus,virhe);
	            muokkaus.getStyleClass().add("virhe");
	            naytaVirhe(virhe);
	        }
    }
	
	/**
     * N‰ytet‰‰n k‰ytt‰j‰lle virheteksti muokkausruudun yll‰
     * @param virhe merkkijono virheviesti
     */
	private void naytaVirhe(String virhe) {
		 if ( virhe == null || virhe.isEmpty() ) {
			 labelVirhe.setText("");
			 labelVirhe.getStyleClass().removeAll("virhe");
			 return;
		 }
		 labelVirhe.setText(virhe);
		 labelVirhe.getStyleClass().add("virhe");
	}

	/**
     * Luodaan pelaajan kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mita dataa naytetaan oletuksena
     * @param kentta mika kentta saa fokuksen kun naytetaan
     * @return null jos painetaan Peruuta, muuten taytetty tietue
     */
	
	public static Pelaaja kysyPelaaja(Stage modalityStage, Pelaaja oletus, int kentta) {
		return ModalController.<Pelaaja, FutisrekisteriMuokkausController>showModal(
				FutisrekisteriMuokkausController.class.getResource("FutisrekisteriMuokkaus.fxml"), "Rekisteri",
				modalityStage, oletus, ctrl -> ctrl.setKentta(kentta));
	}
	/**
     * Naytetaan pelaajan tiedot TextField komponentteihin
     * @param muokkaukset taulukko TextFieldeist‰ johon naytetaan
     * @param pelaaja naytettava pelaaja
     */
	public static void naytaPelaaja(TextField[] muokkaukset, Pelaaja pelaaja) {
		//pelaajaKohdalla = chooserPelaajat.getSelectedObject();
		if (pelaaja == null) return;
		for (int k = pelaaja.ekaKentta(); k < pelaaja.getKenttia(); k++) {
            muokkaukset[k].setText(pelaaja.anna(k));
        }
	}
}
