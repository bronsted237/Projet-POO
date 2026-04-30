package bibliotheque;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

/**
 * Gestion des Emprunts — Bibliothèque
 * Interface complète : emprunter, retourner, affichage table + connexion MySQL
 *
 * @author Développeur Senior
 * @version 2.0
 */
public class emprunterlivre extends javax.swing.JFrame {

    // =========================================================
    //  CONNEXION BASE DE DONNÉES
    //  Adaptez les valeurs selon votre configuration MySQL
    // =========================================================
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    // =========================================================
    //  COMPOSANTS UI — Sidebar
    // =========================================================
    private JPanel     jPanel1;       // Sidebar bleue
    private JButton    jButton1;      // Livres
    private JButton    jButton2;      // Membres
    private JButton    jButton3;      // Emprunts
    private JButton    jButton4;      // Déconnexion
    private JLabel     jLabel4;       // Titre app
    private JLabel     jLabel8;       // Icône livre
    private JLabel     jLabel9;       // Icône membres
    private JLabel     jLabel10;      // Spacer
    private JLabel     jLabel11;      // Icône admin
    private JLabel     jLabel12;      // "Administrateur"
    private JLabel     jLabel13;      // Icône emprunt

    // =========================================================
    //  COMPOSANTS UI — Zone principale
    // =========================================================
    private JPanel        jPanel3;      // Zone blanche principale
    private JPanel        jPanel4;      // Formulaire emprunt
    private JLabel        jLabel1;      // "Gestion des emprunts"
    private JLabel        jLabel14;     // Label Membre
    private JLabel        jLabel15;     // Label Livre
    private JLabel        jLabel16;     // Label Date_retour
    private JLabel        jLabel17;     // "Nouvel emprunt / Retour"
    private JLabel        jLabel3;      // Label Date_emprunt
    private TextField     textField4;   // Champ ID Membre
    private TextField     textField5;   // Champ ID Livre
    private JDateChooser  jDateChooser1; // Date emprunt
    private JDateChooser  jDateChooser2; // Date retour
    private Button        button2;      // Bouton Emprunter
    private Button        button3;      // Bouton Retourner
    private JScrollPane   jScrollPane1;
    private JTable        jTable1;

    // Modèle de table réutilisable
    private DefaultTableModel tableModel;

    // =========================================================
    //  CONSTRUCTEUR
    // =========================================================
    public emprunterlivre() {
        initComponents();
        chargerEmprunts();           // Charger la liste au démarrage
        configurerNavigationBoutons();
    }

    // =========================================================
    //  INITIALISATION DES COMPOSANTS
    // =========================================================
    private void initComponents() {
        // --- Sidebar ---
        jPanel1   = new JPanel();
        jButton1  = new JButton("Livres");
        jButton2  = new JButton("Membres");
        jButton3  = new JButton("Emprunts");
        jButton4  = new JButton("Déconnexion");
        jLabel4   = new JLabel();
        jLabel8   = new JLabel();
        jLabel9   = new JLabel();
        jLabel10  = new JLabel();
        jLabel11  = new JLabel();
        jLabel12  = new JLabel("Administrateur");
        jLabel13  = new JLabel();

        // --- Zone principale ---
        jPanel3       = new JPanel();
        jPanel4       = new JPanel();
        jLabel1       = new JLabel("Gestion des emprunts");
        jLabel17      = new JLabel("Nouvel emprunt / Retour");
        jLabel14      = new JLabel("Membre");
        jLabel15      = new JLabel("Livre");
        jLabel3       = new JLabel("Date_emprunt");
        jLabel16      = new JLabel("Date_retour");
        textField4    = new TextField();
        textField5    = new TextField();
        jDateChooser1 = new JDateChooser();
        jDateChooser2 = new JDateChooser();
        button2       = new Button("Emprunter");
        button3       = new Button("Retourner");
        jScrollPane1  = new JScrollPane();

        // --- Table avec colonnes métier ---
        String[] colonnes = {"ID", "Membre", "Livre", "Date Emprunt", "Date Retour prévue", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Table en lecture seule
            }
        };
        jTable1 = new JTable(tableModel);
        jTable1.setRowHeight(28);
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        jTable1.getTableHeader().setBackground(new Color(0, 0, 102));
        jTable1.getTableHeader().setForeground(Color.WHITE);
        jTable1.setSelectionBackground(new Color(173, 216, 230));
        jScrollPane1.setViewportView(jTable1);

        // Sélection dans la table → remplir les champs
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                int row = jTable1.getSelectedRow();
                textField4.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                textField5.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        // =====================================================
        //  STYLE — Sidebar
        // =====================================================
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Gestion de la Bibliothèque");
        setSize(900, 650);
        setLocationRelativeTo(null);

        jPanel1.setBackground(new Color(0, 0, 102));

        styliserBoutonSidebar(jButton1);
        styliserBoutonSidebar(jButton2);
        styliserBoutonSidebar(jButton3);

        jButton4.setBackground(new Color(0, 0, 102));
        jButton4.setForeground(new Color(204, 0, 0));
        jButton4.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jButton4.setBorder(BorderFactory.createEtchedBorder());
        jButton4.setFocusPainted(false);

        jLabel12.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabel12.setForeground(Color.WHITE);
        jLabel12.setHorizontalAlignment(SwingConstants.CENTER);

        // Icône admin simulée (Unicode)
        jLabel11.setText("👤");
        jLabel11.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        jLabel11.setForeground(Color.WHITE);
        jLabel11.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel8.setText("📖");
        jLabel8.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        jLabel8.setForeground(Color.WHITE);

        jLabel9.setText("👥");
        jLabel9.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        jLabel9.setForeground(Color.WHITE);

        jLabel13.setText("📚");
        jLabel13.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        jLabel13.setForeground(Color.WHITE);

        // =====================================================
        //  LAYOUT — Sidebar (BoxLayout vertical)
        // =====================================================
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
        jPanel1.setPreferredSize(new Dimension(170, 0));

        jPanel1.add(Box.createVerticalStrut(20));
        ajouterAuSidebar(jPanel1, jLabel11);
        ajouterAuSidebar(jPanel1, jLabel12);
        jPanel1.add(Box.createVerticalStrut(50));

        // Ligne Livres
        JPanel ligneLivres = creerLigneSidebar(jLabel8, jButton1);
        jPanel1.add(ligneLivres);
        jPanel1.add(Box.createVerticalStrut(20));

        // Ligne Membres
        JPanel ligneMembres = creerLigneSidebar(jLabel9, jButton2);
        jPanel1.add(ligneMembres);
        jPanel1.add(Box.createVerticalStrut(20));

        // Ligne Emprunts
        JPanel ligneEmprunts = creerLigneSidebar(jLabel13, jButton3);
        jPanel1.add(ligneEmprunts);

        jPanel1.add(Box.createVerticalGlue());
        ajouterAuSidebar(jPanel1, jButton4);
        jPanel1.add(Box.createVerticalStrut(20));

        // =====================================================
        //  STYLE — Zone principale
        // =====================================================
        jPanel3.setBackground(Color.WHITE);

        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jLabel1.setForeground(new Color(0, 0, 102));

        jLabel17.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel17.setHorizontalAlignment(SwingConstants.CENTER);

        // Style labels formulaire
        for (JLabel lbl : new JLabel[]{jLabel14, jLabel15, jLabel3, jLabel16}) {
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        // Style champs texte
        for (TextField tf : new TextField[]{textField4, textField5}) {
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        textField4.setColumns(20);
        textField5.setColumns(20);

        // Style date pickers
        jDateChooser1.setPreferredSize(new Dimension(200, 30));
        jDateChooser2.setPreferredSize(new Dimension(200, 30));

        // Style bouton Emprunter (vert)
        button2.setBackground(new Color(51, 204, 51));
        button2.setForeground(Color.WHITE);
        button2.setFont(new Font("Dialog", Font.BOLD, 14));
        button2.setPreferredSize(new Dimension(110, 40));
        button2.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style bouton Retourner (orange)
        button3.setBackground(new Color(255, 153, 51));
        button3.setForeground(Color.WHITE);
        button3.setFont(new Font("Dialog", Font.BOLD, 14));
        button3.setPreferredSize(new Dimension(110, 40));
        button3.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // =====================================================
        //  LAYOUT — Formulaire (GridBagLayout)
        // =====================================================
        jPanel4.setBackground(Color.WHITE);
        jPanel4.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        jPanel4.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ligne 0 — Membre
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        jPanel4.add(jLabel14, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel4.add(textField4, gbc);

        // Ligne 1 — Livre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        jPanel4.add(jLabel15, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel4.add(textField5, gbc);

        // Ligne 2 — Date emprunt
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        jPanel4.add(jLabel3, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel4.add(jDateChooser1, gbc);

        // Ligne 3 — Date retour
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        jPanel4.add(jLabel16, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        jPanel4.add(jDateChooser2, gbc);

        // Colonne boutons (span lignes 0-1 et 2-3)
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        jPanel4.add(button2, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.gridheight = 2;
        jPanel4.add(button3, gbc);

        // =====================================================
        //  LAYOUT — Zone principale (BorderLayout)
        // =====================================================
        jPanel3.setLayout(new BorderLayout(10, 10));
        jPanel3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel entete = new JPanel(new BorderLayout());
        entete.setBackground(Color.WHITE);
        entete.add(jLabel1, BorderLayout.WEST);
        entete.add(jLabel17, BorderLayout.CENTER);

        jPanel3.add(entete, BorderLayout.NORTH);
        jPanel3.add(jPanel4, BorderLayout.CENTER);
        jPanel3.add(jScrollPane1, BorderLayout.SOUTH);
        jScrollPane1.setPreferredSize(new Dimension(0, 220));

        // =====================================================
        //  LAYOUT — Fenêtre principale
        // =====================================================
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.WEST);
        getContentPane().add(jPanel3, BorderLayout.CENTER);

        // =====================================================
        //  ACTIONS BOUTONS
        // =====================================================
        button2.addActionListener(evt -> actionEmprunter());
        button3.addActionListener(evt -> actionRetourner());
        jButton4.addActionListener(evt -> actionDeconnexion());

        pack();
    }

    // =========================================================
    //  CONNEXION BASE DE DONNÉES
    // =========================================================
    private Connection obtenirConnexion() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // =========================================================
    //  ACTION : EMPRUNTER UN LIVRE
    // =========================================================
    private void actionEmprunter() {
        String membre = textField4.getText().trim();
        String livre  = textField5.getText().trim();
        Date   dateEmp = jDateChooser1.getDate();
        Date   dateRet = jDateChooser2.getDate();

        // --- Validation des champs ---
        if (membre.isEmpty() || livre.isEmpty()) {
            afficherErreur("Veuillez saisir l'ID du membre et l'ID du livre.");
            return;
        }
        if (dateEmp == null || dateRet == null) {
            afficherErreur("Veuillez sélectionner les deux dates.");
            return;
        }
        if (!dateRet.after(dateEmp)) {
            afficherErreur("La date de retour doit être postérieure à la date d'emprunt.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // --- Opération DB ---
        String sqlVerifDispo = "SELECT exemplaires_disponibles FROM livres WHERE id = ?";
        String sqlEmprunt    =
            "INSERT INTO emprunts (membre_id, livre_id, date_emprunt, date_retour_prevue, statut) " +
            "VALUES (?, ?, ?, ?, 'en_cours')";
        String sqlMajStock   =
            "UPDATE livres SET exemplaires_disponibles = exemplaires_disponibles - 1 WHERE id = ?";

        try (Connection conn = obtenirConnexion()) {
            conn.setAutoCommit(false); // Transaction atomique

            // Vérifier la disponibilité
            try (PreparedStatement psVerif = conn.prepareStatement(sqlVerifDispo)) {
                psVerif.setInt(1, Integer.parseInt(livre));
                ResultSet rs = psVerif.executeQuery();
                if (!rs.next() || rs.getInt("exemplaires_disponibles") < 1) {
                    conn.rollback();
                    afficherErreur("Aucun exemplaire disponible pour ce livre.");
                    return;
                }
            }

            // Insérer l'emprunt
            try (PreparedStatement psEmp = conn.prepareStatement(sqlEmprunt)) {
                psEmp.setInt(1, Integer.parseInt(membre));
                psEmp.setInt(2, Integer.parseInt(livre));
                psEmp.setString(3, sdf.format(dateEmp));
                psEmp.setString(4, sdf.format(dateRet));
                psEmp.executeUpdate();
            }

            // Décrémenter le stock
            try (PreparedStatement psMaj = conn.prepareStatement(sqlMajStock)) {
                psMaj.setInt(1, Integer.parseInt(livre));
                psMaj.executeUpdate();
            }

            conn.commit(); // Valider la transaction
            afficherSucces("Emprunt enregistré avec succès !");
            viderFormulaire();
            chargerEmprunts();

        } catch (NumberFormatException e) {
            afficherErreur("L'ID membre et l'ID livre doivent être des nombres entiers.");
        } catch (SQLException e) {
            afficherErreur("Erreur base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================================================
    //  ACTION : RETOURNER UN LIVRE
    // =========================================================
    private void actionRetourner() {
        int ligneSelectionnee = jTable1.getSelectedRow();
        if (ligneSelectionnee == -1) {
            afficherErreur("Sélectionnez un emprunt dans la liste pour effectuer le retour.");
            return;
        }

        int empruntId = Integer.parseInt(tableModel.getValueAt(ligneSelectionnee, 0).toString());
        String statut = tableModel.getValueAt(ligneSelectionnee, 5).toString();

        if ("Retourné".equals(statut)) {
            afficherErreur("Ce livre a déjà été retourné.");
            return;
        }

        // Confirmation
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Confirmer le retour de cet emprunt (ID: " + empruntId + ") ?",
            "Confirmer le retour",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateAujourdhui = sdf.format(new Date());

        // Calcul pénalité
        String sqlEmprunt = "SELECT livre_id, date_retour_prevue FROM emprunts WHERE id = ?";
        String sqlRetour  =
            "UPDATE emprunts SET date_retour_reelle = ?, statut = 'rendu', penalite = ? WHERE id = ?";
        String sqlStock   =
            "UPDATE livres SET exemplaires_disponibles = exemplaires_disponibles + 1 WHERE id = ?";

        try (Connection conn = obtenirConnexion()) {
            conn.setAutoCommit(false);

            int    livreId  = 0;
            double penalite = 0.0;

            try (PreparedStatement ps = conn.prepareStatement(sqlEmprunt)) {
                ps.setInt(1, empruntId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    livreId = rs.getInt("livre_id");
                    Date dateRetourPrevue = rs.getDate("date_retour_prevue");
                    Date maintenant       = new Date();
                    if (maintenant.after(dateRetourPrevue)) {
                        long diff = maintenant.getTime() - dateRetourPrevue.getTime();
                        long joursRetard = diff / (1000 * 60 * 60 * 24);
                        penalite = joursRetard * 100.0; // 100 FCFA/jour
                    }
                }
            }

            // Enregistrer le retour avec pénalité
            try (PreparedStatement ps = conn.prepareStatement(sqlRetour)) {
                ps.setString(1, dateAujourdhui);
                ps.setDouble(2, penalite);
                ps.setInt(3, empruntId);
                ps.executeUpdate();
            }

            // Restituer l'exemplaire
            try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                ps.setInt(1, livreId);
                ps.executeUpdate();
            }

            conn.commit();

            String msg = "Retour enregistré avec succès !";
            if (penalite > 0) {
                msg += "\n⚠️ Pénalité de retard : " + (int) penalite + " FCFA";
            }
            afficherSucces(msg);
            chargerEmprunts();

        } catch (SQLException e) {
            afficherErreur("Erreur base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================================================
    //  CHARGEMENT DE LA TABLE DES EMPRUNTS
    // =========================================================
    private void chargerEmprunts() {
        tableModel.setRowCount(0); // Vider la table

        String sql =
            "SELECT e.id, " +
            "       CONCAT(m.prenom, ' ', m.nom) AS membre, " +
            "       l.titre AS livre, " +
            "       e.date_emprunt, " +
            "       e.date_retour_prevue, " +
            "       CASE e.statut " +
            "           WHEN 'en_cours' THEN " +
            "               CASE WHEN e.date_retour_prevue < CURDATE() THEN 'En retard' ELSE 'En cours' END " +
            "           WHEN 'rendu'    THEN 'Retourné' " +
            "           ELSE e.statut " +
            "       END AS statut_affichage " +
            "FROM emprunts e " +
            "JOIN membres m ON e.membre_id = m.id " +
            "JOIN livres  l ON e.livre_id  = l.id " +
            "ORDER BY e.date_emprunt DESC " +
            "LIMIT 100";

        try (Connection conn = obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("membre"),
                    rs.getString("livre"),
                    rs.getString("date_emprunt"),
                    rs.getString("date_retour_prevue"),
                    rs.getString("statut_affichage")
                });
            }

        } catch (SQLException e) {
            // En mode démo sans DB : charger des données fictives
            chargerDonneesDemoSiPasDeDB();
        }
    }

    // =========================================================
    //  DONNÉES DE DÉMONSTRATION (si pas de DB connectée)
    // =========================================================
    private void chargerDonneesDemoSiPasDeDB() {
        tableModel.setRowCount(0);
        Object[][] demo = {
            {1, "Jean Dupont",   "Les Misérables",      "2026-04-01", "2026-04-15", "Retourné"},
            {2, "Marie Curie",   "L'Étranger",          "2026-04-10", "2026-04-24", "En cours"},
            {3, "Paul Biya",     "Perpétue",             "2026-04-12", "2026-04-26", "En retard"},
            {4, "Amina Diallo",  "Le Petit Prince",     "2026-04-20", "2026-05-04", "En cours"},
        };
        for (Object[] ligne : demo) {
            tableModel.addRow(ligne);
        }
    }

    // =========================================================
    //  NAVIGATION — Boutons sidebar
    // =========================================================
    private void configurerNavigationBoutons() {
        jButton1.addActionListener(e -> {
            // new GestionLivres().setVisible(true);
            JOptionPane.showMessageDialog(this, "Navigation → Gestion des Livres");
        });
        jButton2.addActionListener(e -> {
            // new GestionMembres().setVisible(true);
            JOptionPane.showMessageDialog(this, "Navigation → Gestion des Membres");
        });
        jButton3.addActionListener(e -> {
            chargerEmprunts(); // Rafraîchir la liste
        });
    }

    private void actionDeconnexion() {
        int rep = JOptionPane.showConfirmDialog(
            this, "Voulez-vous vraiment vous déconnecter ?",
            "Déconnexion", JOptionPane.YES_NO_OPTION
        );
        if (rep == JOptionPane.YES_OPTION) {
            this.dispose();
            // new Login().setVisible(true);
        }
    }

    // =========================================================
    //  UTILITAIRES UI
    // =========================================================
    private void styliserBoutonSidebar(JButton btn) {
        btn.setBackground(new Color(0, 0, 102));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEtchedBorder());
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 40));
        // Effet survol
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(0, 0, 160)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(0, 0, 102)); }
        });
    }

    private void ajouterAuSidebar(JPanel sidebar, JComponent comp) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(comp);
    }

    private JPanel creerLigneSidebar(JLabel icone, JButton bouton) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        ligne.setBackground(new Color(0, 0, 102));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        ligne.add(icone);
        ligne.add(bouton);
        return ligne;
    }

    private void viderFormulaire() {
        textField4.setText("");
        textField5.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void afficherSucces(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================
    //  MAIN
    // =========================================================
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Nimbus non disponible → Look and Feel par défaut
        }

        EventQueue.invokeLater(() -> new emprunterlivre().setVisible(true));
    }
}
