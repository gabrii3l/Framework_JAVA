package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ManipulationFichier {

	/**
	 * charge un fichier
	 * @param fichier l'url du fichier a charger
	 */
	public static StringBuffer chargerFichierTchat(String fichier) {
		StringBuffer s = new StringBuffer("");
		try {
			BufferedReader br = new BufferedReader(new FileReader(fichier));
			String line;
			while ((line = br.readLine()) != null) {
			  s.append(line);
			}
			br.close();          
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * sauve un fihcier
	 * @param fichier l'url du fichier de sauvegarde
	 */
	public static void sauverFichier(String fichier, String texte) {
		try {
			FileWriter fw = new FileWriter(new File(fichier),true);
			fw.write(texte);	
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
