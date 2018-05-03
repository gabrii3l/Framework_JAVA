package interfaceGraph;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public abstract class Formulaire extends VBox{
	
	/**
	 * Le formulaire
	 */
	protected VBox form;
	
	/**
	 * genere tout les sous Composant du formulaire
	 */
	protected abstract void genererSousComposant();
	
	/**
	 * definit tout les Actions-Listeners du formulaire
	 */
	protected abstract void ecouteurDefaultAction();

	/**
	 * definit le style par defaut du formulaire
	 */
	protected abstract void layoutDefaultParametre();
	
	/**
	 * Ajoute le noeud n (Button,Label,etc..) au formulaire
	 * @param n le noeud (Button,Label,etc..) � ajouter
	 */
	public void addElementForm(Node n) {
		form.getChildren().add(n);
	}
	
	/**
	 * Ajoute le noeud n (Button,Label,etc..) � l'index du formulaire
	 * @param index l'index o� l'on ajoute
	 * @param n le noeud (Button,Label,etc..) � ajouter
	 */
	public void addElementForm(int index, Node n) {
		form.getChildren().add(index, n);
	}
	
	/**
	 * Supprime le noeud � l'indice index du formulaire
	 * @param index indice du noeud � supprimer
	 */
	public void removeElement(int index) {
		form.getChildren().remove(index);
	}
	
	/**
	 * Supprime le noeud n du formulaire
	 * @param n le noeud � supprimer
	 */
	public void removeElement(Node n) {
		form.getChildren().remove(n);
	}
	
	protected static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}
	
}
