package parametrage;

import java.rmi.RMISecurityManager;
/**
 * Classe de param�trage de la Java Virtial Machine du Client
 */
@SuppressWarnings("deprecation")
public class SettingClientJVM {
	/**
	 * Localisation des propri�t�s de s�curit� d�finit
	 */
	private final static String SECURITY_POLICY = "ressources/parametre_Client/security.policy";
	/**
	 * Localisation des points classes utilis�e pour l'annuaire
	 */
	private final static String RMI_SEVEUR_CODEBASE = "file:${workspace_loc}/rmiclient/bin/" ;
	
	/**
	 * Constructeur est non instanciable
	 */
	private SettingClientJVM(){}
	
	/**
	 * D�finition des propri�t�s utiles � la JVM
	 * @use java.security.policy
	 * @use java.rmi.server.codebase
	 */
	public static void configureProperty(){
		System.setProperty("java.security.policy",SECURITY_POLICY);
		System.setProperty("java.rmi.server.codebase",RMI_SEVEUR_CODEBASE);
	}
	
	/**
	 * Utilisation du SecurityManager
	 * @use RMISecurityManager
	 */
	public static void useSecurityManager(){
		if (System.getSecurityManager() == null) {
		   System.setSecurityManager(new RMISecurityManager());
	    }
	}
}
