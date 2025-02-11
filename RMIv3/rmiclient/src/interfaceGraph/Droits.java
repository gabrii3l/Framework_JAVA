package interfaceGraph;


import java.util.List;

import BaseDeDonnee.gestionUtilisateur.GestionTypeInterface;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Connectable;
import util.Fenetre;
import util.Type;
import util.Utilisateur;

public class Droits extends Formulaire {

	private ListView<Type> typeComboBox;
	private HBox formCreate;
	private HBox formSelect;
	private VBox form;
	private Button buttonCreate;
	private Button buttonModifier;

	public Droits(Utilisateur user) {
		genererSousComposant();
		ecouteurDefaultAction();
		layoutDefaultParametre();
	}

	@Override
	protected void layoutDefaultParametre() {
		formCreate.getChildren().addAll(buttonCreate);
		formCreate.setAlignment(Pos.CENTER);
		formSelect.getChildren().addAll(typeComboBox);
		formSelect.setAlignment(Pos.CENTER);
		form.getChildren().addAll(formCreate,formSelect,buttonModifier);
		form.setAlignment(Pos.CENTER);
		this.getChildren().add(form);
	}

	
	public VBox getStyleForm(){
		return this.form;
	}

	@Override
	protected void ecouteurDefaultAction() {
		buttonCreate.setOnAction(event -> {
			//apelle fonction cr�ation type
			CreerType a = null;
			Stage nouveauStage = new Stage();
			try {
				a = new CreerType();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			a.setPostAdd(e -> {
				refreshList();
				nouveauStage.close();
				
			});
			ScrollPane sp = new ScrollPane();
			sp.setContent(a);
			
			a.setAlignment(Pos.CENTER);
			sp.setFitToWidth(true);
			sp.setFitToHeight(true);
			Scene scene = new Scene(sp, 550, 550);
			Fenetre.paramStage(nouveauStage,scene);
			nouveauStage.show();

		});
		buttonModifier.setOnAction(event -> {
			CreerType a = null;
			Stage nouveauStage = new Stage();
			try {
				a = new ListDroitsType(typeComboBox.getSelectionModel().getSelectedItem());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			ScrollPane sp = new ScrollPane();
			sp.setContent(a);
			
			a.setAlignment(Pos.CENTER);
			sp.setFitToWidth(true);
			sp.setFitToHeight(true);
			Scene scene = new Scene(sp, 550, 550);
			Fenetre.paramStage(nouveauStage,scene);
			nouveauStage.show();
		});

	}

	@Override
	protected void genererSousComposant() {
		// TODO Auto-generated method stub
		//requete pour recup�rer tout les type d�j� existant dans une liste
		//parcourir la liste pour integrer a chaque fois l'element a la combo box
		/*List l = new List();
				l.add("option 1");
				l.add("option 2");
				l.add("option 3");
				for(String str:l)
				{
					typeComboBox.getItems().add(str);
				}*/



		buttonCreate = new Button("Creer nouveau type");
		buttonModifier = new Button("modifier le type selectionne");
		form = new VBox();
		formCreate = new HBox();
		formSelect = new HBox();
		typeComboBox = new ListView<>();
		refreshChoiceBox();
	}

	/**
	 * refresh la choice box 
	 */
	public void refreshChoiceBox() {
		GestionTypeInterface connex;
		try {
			connex = new Connectable<GestionTypeInterface>().connexion("GestionTypes");
			List<Type> lesTypes = connex.getAllType();
			typeComboBox.setItems(FXCollections.observableArrayList(lesTypes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * refresh la liste des types
	 */
	public void refreshList() {
		try {
			GestionTypeInterface connex = new Connectable<GestionTypeInterface>().connexion("GestionTypes");
			List<Type> type = connex.getAllType();
			typeComboBox.setItems(FXCollections.observableArrayList(type));
		} catch (Exception e) {
			Fenetre.creatAlert(AlertType.ERROR, "Erreur", "Impossible de rafrechir les groupes");
		}	
	}
}
