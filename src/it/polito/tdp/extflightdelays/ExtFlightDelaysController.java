

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Rotta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;
	Integer distanzaMin = null;
	Airport aeroporto = null;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroVoliTxtInput"
    private TextField numeroVoliTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	try {
    		distanzaMin = Integer.parseInt(distanzaMinima.getText());
    	}catch(NumberFormatException e ) {
    		txtResult.setText("Inserire numero intero!");
    		return;
    	}
    	
    	model.creaGrafo(distanzaMin);
    	txtResult.setText("Grafo creato\n");
    	txtResult.appendText("Archi "+ model.getGrafoArchi());

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	txtResult.clear();
    	aeroporto = cmbBoxAeroportoPartenza.getValue();
    	if(aeroporto== null) {
    		txtResult.setText("Selezionare un aeroporto");
    		return;
    	}
    	txtResult.clear();
    	List<Rotta> connessi = model.getConnessi(aeroporto);
    	if(connessi.size() == 0) {
    		txtResult.setText("Aeroporto non collegato ad altri aeroporti tramite voli con distanza di almeno "+distanzaMin);
    		return;
    	}
    	for(Rotta r : connessi) {
    		txtResult.appendText(r.getDestination().toString()+" "+ r.getDistanza()+"\n");
    	}
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	txtResult.clear();
    	Double migliaMax = null;
    	try {
    		migliaMax = Double.parseDouble(numeroVoliTxtInput.getText());
    	}catch(NumberFormatException e ) {
    		txtResult.setText("Inserire numero intero!");
    		return;
    	}
    	
    	txtResult.clear();
    	List<Airport> percorso = model.getPercorso(migliaMax, aeroporto);
    	for(Airport a : percorso) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	txtResult.appendText("Distanza percorsa "+ model.getDistanzaPercorsa());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
		cmbBoxAeroportoPartenza.getItems().addAll(model.getAeroporti());
	}
}

