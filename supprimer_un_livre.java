/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotheque;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author BRONSTED
 */
public class supprimer_un_livre {
     Connection con;
    PreparedStatement pst;
    ResultSet rs;
    public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost/bibliothèques","root","");
			System.out.println("connection reussie");
		} catch(Exception e) {
			System.err.println(e); //on affiche l'erreur
		}
       try {
    connect();
    pst = con.prepareStatement("DELETE FROM livres WHERE titre_du_livre = ? AND auteurs = ? AND Categorie = ? AND Nombre_examplaire = ?");
    pst.setString(1, titre.getText());
    pst.setString(2, auteur.getText());
    pst.setString(3, (String) liste1.getSelectedItem());
    pst.setString(4, (String) liste2.getSelectedItem());

    int rowsAffected = pst.executeUpdate();
    con.close();

    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(null, "Livre supprimé avec succès");
    } else {
        JOptionPane.showMessageDialog(null, "Aucun livre trouvé avec ces informations");
    }

} catch (Exception e2) {
    JOptionPane.showMessageDialog(null, "Impossible de supprimer, Vérifiez tous les champs");
    e2.printStackTrace();
}
    
}
