package interfaceGraph.sondage;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sondage.SondageObj;
import util.Utilisateur;

public class ResultatSondage extends Composition
{
	//private itemResultat i;//un label, une barre et le nombre de vote associ�
	private VBox l_item;
	private HBox infos;
	private Label question;
	private Label nbVote;
	private Label jourRestant;
	//other
	private Utilisateur user;
	SondageObj s;


	public ResultatSondage(Utilisateur user, SondageObj s){
		this.user = user;
		this.s=s;

		genererSousComposant();	
		layoutDefaultParametre();
	}

	/**
	 * @return l'id du sondage
	 */
	public int idSondage() {
		return s.getId();
	}

	/**
	 * @return le sondageobj
	 */
	public SondageObj getSondageObj() {
		return s;
	}

	/**
	 * Modifie les r�sultats
	 * @param resultat la string contenant les resultats
	 */
	public void modifResultat(String resultat) {
		s.setReponses(resultat);
		s.totalPlusUn();
		l_item.getChildren().clear();
		int max = s.getReponses().size();
		//initialisation de notre liste d'item		
		for(int j=0, i=1 ; j<max ; j+=2, i+=2){ 
			l_item.getChildren().addAll(new Label(s.getReponses().get(j)+" : ["+s.getReponses().get(i)+"]"), new ProgressBar(Integer.parseInt(s.getReponses().get(i))/(double)s.getTotal()));
		}
		this.nbVote = new Label("votes total : "+s.getTotal());
		this.jourRestant = new Label("date cloture : "+s.getDate());
	}

	@Override
	protected void genererSousComposant() {
		this.infos = new HBox();
		this.comp = new VBox();
		this.l_item= new VBox();
		int max = s.getReponses().size();
		//initialisation de notre liste d'item		
		for(int j=0, i=1 ; j<max ; j+=2, i+=2){ 
			l_item.getChildren().addAll(new Label(s.getReponses().get(j)+" : ["+s.getReponses().get(i)+"]"), new ProgressBar(Integer.parseInt(s.getReponses().get(i))/(double)s.getTotal()));
		}
		question = new Label(s.getQuestion());
		this.nbVote = new Label("votes total : "+s.getTotal());
		this.jourRestant = new Label("date cloture : "+s.getDate());

	}

	@Override
	protected void ecouteurDefaultAction() {
	}

	@Override
	protected void layoutDefaultParametre() {
		infos.getChildren().addAll(nbVote,jourRestant);
		infos.setSpacing(15);
		infos.setAlignment(Pos.CENTER);
		question.setAlignment(Pos.CENTER);
		comp.getChildren().addAll(question,l_item,infos);
		comp.setSpacing(20);
		comp.setAlignment(Pos.CENTER);
		l_item.setAlignment(Pos.CENTER);
		this.getChildren().add(this.comp);
	}
}
