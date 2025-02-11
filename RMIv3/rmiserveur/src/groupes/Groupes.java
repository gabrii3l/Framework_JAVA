package groupes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import BaseDeDonnee.MethodeServeur;
import BaseDeDonnee.sgbd.SGBD;
import tchat.TchatListener;
import util.Groupe;

public class Groupes extends UnicastRemoteObject  implements GroupesInterface {

	private SGBD sgbd;
	private Vector<GroupeListener> listeners = new Vector<>();
	private  Map<String ,GroupeListener> list = new HashMap<>();
	
	public Groupes(SGBD _sgbd) throws RemoteException {
		super();
		sgbd = _sgbd;
	}
	
	@Override
	public List<Groupe> getAllGroupes() throws RemoteException, ClassNotFoundException, SQLException {
		return sgbd.getGroupes();
	}
	
	@Override
	public List<Groupe> getGroupeLogin(String login) throws ClassNotFoundException, RemoteException, SQLException {
		return sgbd.getGroupeUtilisateur(login);
	}

	@Override
	public void suprimerGroupe(int idGr) throws RemoteException, ClassNotFoundException, SQLException {
		sgbd.suprimerGroupe(idGr);
	}

	@Override
	public int ajouterGroupe(String groupe, List<String> lstUser) throws RemoteException, ClassNotFoundException, SQLException {
		int idGr = sgbd.ajouterGroupe(groupe, lstUser);
		notifyListeners(new Groupe(idGr,groupe),lstUser);
		return idGr;
	}

	@Override
	public List<String> getAllLogin(int idGr) throws RemoteException, ClassNotFoundException, SQLException {
		return sgbd.getAllLoginGroupe(idGr);
	}

	@Override
	public List<String> getAllLoginNotInGroupe(int idGr) throws RemoteException, ClassNotFoundException, SQLException {
		return sgbd.getAllLoginNotInGroupe(idGr);
	}

	@Override
	public void ajouterUtilisateur(int idGr, String login)
			throws RemoteException, ClassNotFoundException, SQLException {
		sgbd.ajouterUtilisateur(idGr, login);
	}

	@Override
	public void supprimerUtilisateur(int idGr, String login)
			throws RemoteException, ClassNotFoundException, SQLException {
		sgbd.supprimerUtilisateur(idGr, login);
		
	}
	
	@Override
	public synchronized void addGroupeListener (String login, GroupeListener listener) throws java.rmi.RemoteException {
		list.put(login,listener);
	}

	@Override
	public synchronized void removeGroupeListener (String login, GroupeListener listener) throws java.rmi.RemoteException {
		list.remove(login);
	}
	
	/**
	 * Methode de notification aux listener
	 * @param message le nouveau message
	 * @param groupe le groupe auquel le message est destine
	 */
	private void notifyListeners(Groupe groupe,List<String> lstUser)  {	
		for (Entry<String, GroupeListener> t : list.entrySet()) {
			if (lstUser.contains(t.getKey())) {
				try {
					t.getValue().nouveauGroupe(groupe);
				} catch(Exception re) {
					re.printStackTrace();
					list.remove(t.getKey()); 
				} 
			}
		} 
	}

}
