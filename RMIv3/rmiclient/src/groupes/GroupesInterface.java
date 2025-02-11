package groupes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import util.Groupe;

public interface GroupesInterface  extends Remote {
	
	/**
	 * recuperer tous les groupes
	 * @return une liste de groupe
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<Groupe> getAllGroupes()  throws RemoteException, ClassNotFoundException, SQLException;
	
	
	/**
	 * Recuperer les groupes d'un utilisateur
	 * @param login le login de l'utilisateur
	 * @return une liste de groupe
	 * @throws ClassNotFoundException
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public List<Groupe> getGroupeLogin(String login) throws ClassNotFoundException, RemoteException, SQLException;
	
	
	/**
	 * Supprimer un groupe
	 * @param idGr l'id du groupe
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void suprimerGroupe(int idGr) throws RemoteException, ClassNotFoundException, SQLException;
	
	
	/**
	 * Ajouter un groupe
	 * @param groupe le nom du groupe a ajouter
	 * @param lstUser la liste des utilisateurs du groupe
	 * @return int 
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int ajouterGroupe(String groupe, List<String> lstUser) throws RemoteException, ClassNotFoundException, SQLException;
	
	
	/**
	 * Recuperer tous les utilisateurs d'un groupe
	 * @param idGr l'id du groupe
	 * @return une liste de login
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<String> getAllLogin(int idGr) throws RemoteException, ClassNotFoundException, SQLException;
	
	
	/**
	 * Recuperer tous les utilisateurs qui ne sont pas dans le groupe
	 * @param idGr l'id du groupe
	 * @return une liste de login utilisateur
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<String> getAllLoginNotInGroupe(int idGr) throws RemoteException, ClassNotFoundException, SQLException;
	
	
	/**
	 * Ajouter un utilisateur a un groupe
	 * @param idGr l'id du groupe
	 * @param login le login de l'utilisateur
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void ajouterUtilisateur(int idGr, String login) throws RemoteException, ClassNotFoundException, SQLException;
	
	
	/**
	 * Supprimer un utilisateur d'un groupe
	 * @param idGr l'id du groupe
	 * @param login le login de l'utilisateur
	 * @throws RemoteException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void supprimerUtilisateur(int idGr, String login) throws RemoteException, ClassNotFoundException, SQLException;
	
	void addGroupeListener(String login, GroupeListener listener) throws RemoteException;
	void removeGroupeListener(String login, GroupeListener listener) throws RemoteException;
}
