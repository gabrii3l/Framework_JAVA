package interfaceGraph;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import fichier.GestionFichierInterface;
import groupes.GroupeListener;
import groupes.GroupesInterface;
import util.Connectable;
import util.Groupe;
import util.LimitedTextArea;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tchat.TchatInterface;
import tchat.TchatListener;
import util.Utilisateur;

public class TchatGraphique extends VBox {

	protected LimitedTextArea ZoneText;
	protected Button BouttonEnv;
	protected HBox hbTextFButton;
	protected VBox vboxTextTchat;
	protected ScrollPane sp;
	private TchatInterface connex;
	private GestionFichierInterface connexG;
	private GroupesInterface connexGroup;
	private ChoiceBox<Groupe> cbgroupe;
	private List<Groupe> g;
	private Map<Integer ,Tchat> listener = new HashMap<>();
	private Utilisateur util;
	private boolean droit;


	/**
	 * Fonction appeler après appuie sur le bouton envoyer
	 * @param tchat
	 * @throws RemoteException
	 */
	private void execute(TchatInterface tchat) throws RemoteException{ 
		sp.vvalueProperty().bind((ObservableValue<? extends Number>) sp.heightProperty());
		if (!ZoneText.getText().trim().isEmpty()) {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			tchat.envoyerMessage(dateFormat.format(date) + "~" + util.getPrenom() + " " + util.getNom() + "~" + ZoneText.getText() + "|",cbgroupe.getSelectionModel().getSelectedItem().getidGr());
			ZoneText.setText("");
		}
	}

	/**
	 * Constructeur du tchat graphique
	 * @param util l l'utilisateur courant qui va utiliser le tchat, grace a cela on connaitra les droits de l'utilisateur et ses groupes
	 * @throws Exception 
	 */
	public TchatGraphique(Utilisateur util) throws Exception{
		super();
		this.util=util;
		if (util.hasRight("DEC")) droit = true;
		else droit=false;
		connex = new Connectable<TchatInterface>().connexion("Tchat");	
		connexG = new Connectable<GestionFichierInterface>().connexion("Fichier");
		connexGroup = new Connectable<GroupesInterface>().connexion("Groupes");
		g = new ArrayList<>();
		
		genererSousComposant();
		layoutDefaultParametre();
		ecouteurDefaultAction();
		ecouteurChoixGroupe();
		for (Groupe g : connexG.recupererGroupe(util.getLogin())) {
			Tchat l = new Tchat();
			int gr = g.getidGr();
			listener.put(gr,l);
			connex.addTchatListener(l,gr);
			this.g.add(g);
		}
		connexGroup.addGroupeListener(util.getLogin(),(GroupeListener) new GroupeList());
		ajouterMessage(connex.getHistorique(cbgroupe.getSelectionModel().getSelectedItem().getidGr()));		
	}

	/**
	 * genere tout les sous Composant
	 */
	private void genererSousComposant() throws Exception {
		ZoneText= new LimitedTextArea(256);
		BouttonEnv= new Button("Envoyer");
		hbTextFButton= new HBox();
		vboxTextTchat = new VBox();
		sp = new ScrollPane();	
		try {
			cbgroupe = new ChoiceBox<Groupe>(FXCollections.observableArrayList(util.getGroupe()));
		} catch (RemoteException | ClassNotFoundException | NotBoundException | SQLException e) {
			cbgroupe = new ChoiceBox<Groupe>(FXCollections.observableArrayList(new ArrayList<Groupe>()));
		}
		cbgroupe.getSelectionModel().select(0);
	}

	/**
	 * definit le style par defaut
	 */
	private void layoutDefaultParametre() {
		ZoneText.setMinSize(535, 70);
		ZoneText.setWrapText(true);
		vboxTextTchat.setSpacing(5);
		vboxTextTchat.setId("tchat-list");
		sp.setContent(vboxTextTchat);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setMinSize(500, 400);
		sp.setFitToWidth(true);
		sp.setFitToHeight(true);
		if (droit) hbTextFButton.getChildren().addAll(ZoneText,BouttonEnv);
		this.setSpacing(3);
		this.setPadding(new Insets(3, 3, 3, 3));
		this.getChildren().addAll(sp,hbTextFButton,cbgroupe);
	}

	/**
	 * definit tout les Actions-Listeners
	 */
	private void ecouteurDefaultAction() {
		BouttonEnv.setOnAction(event ->{
			try {
				execute(connex);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			ZoneText.setText("");
		});
	}

	/**
	 * ecouteur sur la choice box du choix du groupe
	 */
	private void ecouteurChoixGroupe() {
		cbgroupe.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<? super Number>) (ov, value, new_value) -> {
			try {
				vboxTextTchat.getChildren().removeAll(vboxTextTchat.getChildren());
				cbgroupe.getSelectionModel().select((int) new_value);
				ajouterMessage((connex.getHistorique(cbgroupe.getSelectionModel().getSelectedItem().getidGr())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * ajoute un message au tchat
	 * @param message le messsage a ajouter
	 */
	private void ajouterMessage(String message) {
		String[] str = message.split(Pattern.quote("|"));	
		String[] s;
		for (String u : str) {
			if (!u.equals("")) {
				s = u.split("~");
				vboxTextTchat.getChildren().add(creatMessageTchat(s[0],s[1],s[2]));
			}
		}
	}
	
	private void ajouterGroupe(Groupe g) {
		this.g.add(g);
		this.getChildren().remove(cbgroupe);
		cbgroupe = new ChoiceBox<Groupe>(FXCollections.observableArrayList(this.g));
		this.getChildren().add(cbgroupe);
		cbgroupe.getSelectionModel().select(0);
		ecouteurChoixGroupe();
	}

	/**
	 * Creer un message
	 * @param heure l'heure du message
	 * @param nomATest l'expediteur
	 * @param msg le contenu du message
	 * @return
	 */
	private HBox creatMessageTchat(String heure,String nomATest,String msg){
		VBox v=new VBox();
		String nom;
		HBox h = new HBox();
		if (nomATest.equals(util.getPrenom() + " " + util.getNom())) {
			nom = "Moi";
			v.setId("v2");
			h.setAlignment(Pos.BASELINE_RIGHT);
		}
		else {
			v.setId("v");
			nom = nomATest;
			h.setAlignment(Pos.BASELINE_LEFT);
		}

		Label t1 = new Label("["+ heure + "] "+ nom + " : ");
		t1.setId("tchat-nom");

		Text textMsg = new Text(msg);
		textMsg.getStyleClass().clear();
		textMsg.setId("tchat-msg");
		textMsg.setWrappingWidth(300);

		v.getChildren().addAll(t1,textMsg);
		h.getChildren().add(v);
		v.setMaxWidth(300);
		return h;
	}

	/**
	 * sous classe implementant un listener
	 */
	private class Tchat extends UnicastRemoteObject implements TchatListener {

		private static final long serialVersionUID = 1L;

		protected Tchat() throws RemoteException {
			super();
		}

		@Override
		public void nouveauMessage(String message, Integer groupe) throws RemoteException {
			if (groupe==cbgroupe.getSelectionModel().getSelectedItem().getidGr()) {
				Platform.runLater(
						() -> {
							ajouterMessage(message);
						}
						);
			}			
		}
	}

	/**
	 * sous classe implementant un listener
	 */
	private class GroupeList extends UnicastRemoteObject implements GroupeListener {

		private static final long serialVersionUID = 1L;

		protected GroupeList() throws RemoteException {
			super();
		}

		@Override
		public void nouveauGroupe(Groupe g) {
			Platform.runLater(
					() -> {
						ajouterGroupe(g);
					}
					);			
		}
	}
}
