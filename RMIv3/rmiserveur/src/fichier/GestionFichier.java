package fichier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.remote.rmi.RMIServer;

import BaseDeDonnee.sgbd.SGBD;
import parametrage.PropertiesServeur;
import util.Groupe;


public class GestionFichier extends UnicastRemoteObject implements GestionFichierInterface {
	private static final long serialVersionUID = 5481399837776043628L;
	private SGBD sgbd;
	private String chemin = PropertiesServeur.getStockageFichiers() +"/";
	private  Map<Integer ,Vector <FichierListener>> list = new HashMap<>();

	public GestionFichier (SGBD sgbd) throws RemoteException, ClassNotFoundException, SQLException {  
		super();
		this.sgbd=sgbd;
		for (Groupe s : sgbd.getGroupes()) {
			list.put(s.getidGr(), new Vector <FichierListener> ());
		}
	}

	@Override
	public boolean upload(String nom, byte[] contenu, String l, int id) throws RemoteException, ClassNotFoundException, SQLException {
		int i = -1;
		try{
			Files.write(Paths.get(chemin+nom),contenu);
			i = sgbd.ajouterFichier(nom, l, id);
		} catch (IOException ex) {
			Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
		notifyListeners(new Fichier(i,nom,chemin),id);
		return true;
	}

	@Override
	public byte[] download(int id) throws ClassNotFoundException, SQLException, IOException {
		Fichier f = sgbd.getUrlFichier(id);
		return Files.readAllBytes(Paths.get(f.getFileLink()+"/"+f.getNom())); 
	}

	@Override
	public List<Fichier> recupererTousFichiers() throws RemoteException, ClassNotFoundException, SQLException {
		return sgbd.getFichiers();
	}

	@Override
	public List<Fichier> recupererFichierGroupe(int id) throws ClassNotFoundException, RemoteException, SQLException {
		return sgbd.getFichiersGroupe(id);
	}

	@Override
	public List<Groupe> recupererGroupe(String l) throws ClassNotFoundException, RemoteException, SQLException {
		return sgbd.getGroupeUtilisateur(l);
	}

	@Override
	public synchronized void addGroupListener (FichierListener listener, Integer groupe) throws java.rmi.RemoteException {
		Vector<FichierListener> newlistener = list.get(groupe);
		newlistener.add(listener);
		list.put(groupe,newlistener);
	}

	@Override
	public synchronized void removeGroupListener (FichierListener listener, Integer groupe) throws java.rmi.RemoteException {
		list.get(groupe).remove(listener);
	}
	
	@Override
	public void ajouterGroupeFichier(Integer idGr) throws java.rmi.RemoteException {
		list.put(idGr, new Vector <FichierListener> ());
	}
	
	/**
	 * Notifie les listeners d'un nouveau fichier
	 * @param f le nouveau fichier
	 * @param groupe le groupe auquel est destine le fichier
	 */
	private void notifyListeners(Fichier f, Integer groupe) {	
		Vector <FichierListener> v = list.get(groupe);
		for (Enumeration<FichierListener> e = v.elements(); e.hasMoreElements();) { 
			FichierListener listener = (FichierListener) e.nextElement();
			try {
				listener.nouveauFichier(f,groupe);
			} catch(Exception re) {
				re.printStackTrace();
				v.remove(listener); 
			} 
		} 
	}
}
