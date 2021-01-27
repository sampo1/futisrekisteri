package fkFutisrekisteri;

import fi.jyu.mit.fxgui.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Luokka jossa luodaan Seurannimen kysymisdialogi.
 * @author Sampo Ruohonen
 * @version 12.4.2018
 */
public class FutisrekisteriGUIController implements ModalControllerInterface<String> {
	
	@FXML private TextField textVastaus;
	private String vastaus = null;
	
    @FXML
    private void handleOK() {
        vastaus = textVastaus.getText();
    	ModalController.closeStage(textVastaus);	
    }

    @FXML
    private void handlePeruuta() {
    	ModalController.closeStage(textVastaus);
    }
    
    @Override
    public String getResult() {
        return vastaus;
    }
   
        
    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
    }
	
    /**
    * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
    * @param modalityStage mille ollaan modaalisia, null = sovellukselle
    * @param oletus mit‰ nime‰ n‰ytet‰‰n oletuksena
    * @return null jos painetaan Cancel, muuten kirjoitettu nimi
    */
    public static String kysyNimi(Stage modalityStage, String oletus) {
         return ModalController.showModal(
              FutisrekisteriGUIController.class.getResource("FutisrekisteriGUIView.fxml"),
              "Futisrekisteri",
    	       modalityStage, oletus);
         }

	@Override
	public void handleShown() {
		textVastaus.requestFocus();
	}
    
}
