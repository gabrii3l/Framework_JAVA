package interfaceGraph;

import org.mindrot.jbcrypt.BCrypt;
import BaseDeDonnee.gestionUtilisateur.OperationUtilisateurInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.Connectable;
import util.Fenetre;
import util.Utilisateur;

public class ModifierMotDePasse extends Formulaire {
	
	private Utilisateur utilisateur;
	private Label l_mdpNew1;
	private Label l_mdpNew2;
	private PasswordField t_mdpNew1;
	private PasswordField t_mdpNew2;
	private Button b_valider;
	private Button b_annuler;
	private HBox hb_validerAnnuler;

	public ModifierMotDePasse(Utilisateur _utilisateur) {
		super();
		utilisateur = _utilisateur;
		if (utilisateur != null) {
			genererSousComposant();
			ecouteurDefaultAction();
			layoutDefaultParametre();
			this.setAlignment(Pos.CENTER);
		}
	}
	
	@Override
	protected void genererSousComposant() {
		form = new VBox();
		//l_mdpOld = new Label("Ancien mot de passe");
		l_mdpNew1 = new Label("Nouveau mot de passe");
		l_mdpNew2 = new Label("Confirmation");
		t_mdpNew1 = new PasswordField();
		//t_mdpOld = new PasswordField();
		t_mdpNew2 = new PasswordField();
		
		b_valider = new Button("Valider");
		b_annuler = new Button("Annuler");
		hb_validerAnnuler = new HBox(b_annuler, b_valider);
		hb_validerAnnuler.setAlignment(Pos.CENTER);
		
	}

	@Override
	protected void ecouteurDefaultAction() {
		b_valider.addEventHandler(ActionEvent.ACTION, event -> {
			if (t_mdpNew2.getText() != "") {
				if (t_mdpNew1.getText().equals(t_mdpNew2.getText())) {
					String mdp = BCrypt.hashpw(t_mdpNew2.getText(), BCrypt.gensalt());
					try {
						OperationUtilisateurInterface connex = new Connectable<OperationUtilisateurInterface>().connexion("OperationUtilisateur");
						connex.ModifierMdpUtilisateur(utilisateur.getLogin(), mdp);
					} catch (Exception e) {
						Fenetre.creatAlert(AlertType.ERROR, "Information Dialog", "Erreur");
					}
					
				} else {
					Fenetre.creatAlert(AlertType.WARNING, "Attention !", "Les mots de passe doivent etre identique");
				}
			} else {
				Fenetre.creatAlert(AlertType.WARNING, "Attention !", "Les mots de passe ne peut pas etre vide");
			}
		});
		
	}
	
	/**
	 * Ajout d'un evenement apres la modification du mot de passe
	 * @param value
	 */
	public void setPostValidEvent(EventHandler<ActionEvent> value) {
		this.b_valider.addEventHandler(ActionEvent.ACTION, value);
	}
	
	/**
	 * set un evenement quqnd l'utilisateur clique sur annuler
	 * @param value
	 */
	public void setAnnulerEvent(EventHandler<ActionEvent> value) {
		b_annuler.addEventHandler(ActionEvent.ACTION, value);
	}

	@Override
	protected void layoutDefaultParametre() {
		form.getChildren().addAll(l_mdpNew1, t_mdpNew1, l_mdpNew2, t_mdpNew2, hb_validerAnnuler);
		form.setMaxSize(200, 200);
		form.setSpacing(3);
		form.setAlignment(Pos.CENTER);
		this.getChildren().add(form);
	}
	
	

}
