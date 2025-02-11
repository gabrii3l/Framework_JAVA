package interfaceGraph;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fichier.GestionFichierInterface;
import groupes.GroupesInterface;
import util.Connectable;
import util.Fenetre;
import util.Groupe;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.TransformerFichier;
import util.Utilisateur;

public class PoseFichier extends VBox {
	protected VBox form;
	protected FileChooser filechooser;
	protected Button button;
	protected Label label;
	private Stage stage;
	private Utilisateur u;
	protected ChoiceBox<Groupe> cbgroupe;
	private GestionFichierInterface connex;
	
	public PoseFichier(Utilisateur u) throws Exception {
		this.u=u;	
		connex = new Connectable<GestionFichierInterface>().connexion("Fichier");
		genererSousComposant();
		ecouteurDefaultAction();
		layoutDefaultParametre();
	}

	/**
	 * genere tout les sous Composant
	 */
	protected void genererSousComposant() throws Exception {
		button = new Button("upload");
		label = new Label();
		try {
			cbgroupe = new ChoiceBox<Groupe>(FXCollections.observableArrayList(u.getGroupe()));
		} catch (RemoteException | ClassNotFoundException | NotBoundException | SQLException e) {
			cbgroupe = new ChoiceBox<Groupe>(FXCollections.observableArrayList(new ArrayList<Groupe>()));
		}
		form = new VBox();	
	}

	/**
	 * definit tout les Actions-Listeners
	 */
	protected void ecouteurDefaultAction() {
		button.setOnAction(event -> {
			filechooser = new FileChooser();
			File chosenFile = filechooser.showOpenDialog(stage);
			if(chosenFile != null){
				label.setText(chosenFile.getAbsolutePath());
            }else {
                label.setText("please choose a file!");
            }
			if(chosenFile.length()>20000000) {
				Fenetre.creatAlert(AlertType.ERROR, "Information Dialog", "Le fichier doit faire moins de 20Mo");
			}
			else {
				try {				
					byte[] b = TransformerFichier.fileToByte(chosenFile.getAbsolutePath());
					boolean bo = connex.upload(chosenFile.getName(),b,u.getLogin(),cbgroupe.getSelectionModel().getSelectedItem().getidGr());
					if (bo) label.setText("Fichier correctement envoye");
					else label.setText("Erreur lors de l'envoie du fichier");
				} catch (Exception e) {
					Fenetre.creatAlert(AlertType.ERROR, "Information Dialog", "Erreur");
				}
			}
			
		});
	}

	/**
	 * definit le style par defaut
	 */
	protected void layoutDefaultParametre() {
		cbgroupe.getSelectionModel().select(0);
		this.form.getChildren().addAll(cbgroupe,button, label);
		form.setAlignment(Pos.CENTER);
		this.getChildren().add(form);
	}
	
	public VBox getStyleForm(){
		return this.form;
	}
	
}
