package fkFutisrekisteri;
	
import FutisrekisteriTietokanta.Rekisteri;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * P��luokka, josta laitetaan p��ikkuna ja seurannimen kysymisikkuna k�yntiin.
 * @author Sampo Ruohonen
 * @version 12.4.2018
 * Laitetaan k�yntiin
 */
public class FutisrekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final FXMLLoader ldr = new FXMLLoader(getClass().getResource("FutisrekisteriPaaikkuna.fxml"));
			final Pane root = (Pane)ldr.load();
			final FutisrekisteriPaaikkunaController rekisteriCtrl = (FutisrekisteriPaaikkunaController)ldr.getController();
			final Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("futisrekisteri.css").toExternalForm());
			Rekisteri rekisteri = new Rekisteri();
			primaryStage.setTitle("Futisrekisteri");
			rekisteriCtrl.setRekisteri(rekisteri);
			primaryStage.setScene(scene);
			primaryStage.show();
			Application.Parameters params = getParameters();
			if ( params.getRaw().size() > 0 )
			    rekisteriCtrl.lueTiedosto(params.getRaw().get(0));
			/*
			else { 
			    rekisteriCtrl.lueTiedosto("Barcelona"); }
			*/
			/**/else
			    if (!rekisteriCtrl.avaa()) Platform.exit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * K�ynnistet��n k�ytt�liittym� 
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
