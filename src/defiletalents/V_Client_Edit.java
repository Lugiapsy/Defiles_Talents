package defiletalents;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import methodesVerif.Verify;

/**
 * Classe permettant de gérer la vue relative à un enregistrement de la table
 * tal_client.
 *
 * @author saiirod
 */
public class V_Client_Edit extends javax.swing.JDialog {

    /**
     * Controleur de l'appli.
     */
    private Controller controller;
    /**
     * Tableau d'entiers représentant les identifiants des origines pour le
     * comboBox origine.
     */
    private int[] originesId;

    /**
     * Parent appelant cette fenêtre. Null si le parent est V_Client_All.
     */
    private JFrame parent = null;
    /**
     * Mode de la vue.
     */
    private int mode = 0;
    /**
     * Identifiant de l'enregistrement.
     */
    private int id = 0;

    /**
     * Constructeur de la vue.
     *
     * @param controller
     */
    public V_Client_Edit(Controller controller) {
        this.controller = controller;
        initComponents();
        jScrollPane2.setBorder(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        edNom = new javax.swing.JTextField();
        edPrenom = new javax.swing.JTextField();
        lbNom = new javax.swing.JLabel();
        lbPrenom = new javax.swing.JLabel();
        lbCommentaire = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        edCommentaire = new javax.swing.JTextArea();
        btValider = new javax.swing.JButton();
        btAnnuler = new javax.swing.JButton();
        lbAdresse = new javax.swing.JLabel();
        edAdresse2 = new javax.swing.JTextField();
        edLieuDit = new javax.swing.JTextField();
        edAdresse1 = new javax.swing.JTextField();
        edCodePostal = new javax.swing.JTextField();
        lbBureau = new javax.swing.JLabel();
        edBureau = new javax.swing.JTextField();
        lbTelephone = new javax.swing.JLabel();
        edTelephone = new javax.swing.JTextField();
        lbCourriel = new javax.swing.JLabel();
        edCourriel = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbOrigine = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lbWarning = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        setSize(new java.awt.Dimension(800, 600));

        edNom.setNextFocusableComponent(edPrenom);

        edPrenom.setNextFocusableComponent(edBureau);

        lbNom.setText("Nom:");

        lbPrenom.setText("Prénom:");

        lbCommentaire.setText("Commentaire");

        edCommentaire.setColumns(20);
        edCommentaire.setLineWrap(true);
        edCommentaire.setRows(5);
        jScrollPane1.setViewportView(edCommentaire);

        btValider.setText("Valider");
        btValider.setNextFocusableComponent(btAnnuler);
        btValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btValiderActionPerformed(evt);
            }
        });

        btAnnuler.setText("Annuler");
        btAnnuler.setNextFocusableComponent(edNom);
        btAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAnnulerActionPerformed(evt);
            }
        });

        lbAdresse.setText("Adresse:");

        edAdresse2.setToolTipText("Adresse 2 (complément)");
        edAdresse2.setNextFocusableComponent(edLieuDit);

        edLieuDit.setToolTipText("Lieu dit");
        edLieuDit.setNextFocusableComponent(edCodePostal);

        edAdresse1.setToolTipText("Adresse");
        edAdresse1.setNextFocusableComponent(edAdresse2);

        edCodePostal.setToolTipText("Code postal");
        edCodePostal.setNextFocusableComponent(edCourriel);

        lbBureau.setText("Bureau:");

        edBureau.setNextFocusableComponent(edTelephone);

        lbTelephone.setText("Téléphone:");

        edTelephone.setNextFocusableComponent(edAdresse1);

        lbCourriel.setText("Courriel:");

        edCourriel.setNextFocusableComponent(cbOrigine);

        jLabel1.setText("Origine:");

        cbOrigine.setNextFocusableComponent(btValider);

        lbWarning.setEditable(false);
        lbWarning.setBackground(UIManager.getColor("Label.background"));
        lbWarning.setColumns(20);
        lbWarning.setFont(UIManager.getFont("Label.font"));
        lbWarning.setLineWrap(true);
        lbWarning.setRows(2);
        lbWarning.setTabSize(4);
        lbWarning.setWrapStyleWord(true);
        lbWarning.setBorder(null);
        jScrollPane2.setViewportView(lbWarning);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(edBureau, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbBureau, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbNom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(lbPrenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(edPrenom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(edNom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(edAdresse1)
                                                .addComponent(edAdresse2)
                                                .addComponent(lbAdresse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                    .addComponent(edLieuDit, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(edCodePostal, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(lbCourriel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(230, 230, 230)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cbOrigine, 0, 200, Short.MAX_VALUE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(edCourriel)))))
                            .addComponent(edTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbCommentaire))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btValider, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNom, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAdresse, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(edNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbBureau, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbCourriel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edBureau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edCourriel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(edAdresse1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edAdresse2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edLieuDit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edCodePostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOrigine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbCommentaire, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btAnnuler)
                        .addComponent(btValider))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Valider'.
     *
     * @param evt
     */
    private void btValiderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btValiderActionPerformed
    {//GEN-HEADEREND:event_btValiderActionPerformed
        int idResult = 0;
        String nom = edNom.getText();
        String prenom = edPrenom.getText();
        String adresse1 = edAdresse1.getText();
        String adresse2 = edAdresse2.getText();
        String lieu_dit = edLieuDit.getText();
        String code_postal = edCodePostal.getText();
        String bureau = edBureau.getText();
        String telephone = edTelephone.getText();
        String courriel = edCourriel.getText();
        String commentaire = edCommentaire.getText();
        int id_origine = originesId.length == 0 ? -1 : originesId[cbOrigine.getSelectedIndex()];

        if (!(nom.equals("") || id_origine == -1)) { // Suppresion de la condition || (prenom.equals("") 
            if (courriel.isEmpty() || Verify.toMail(courriel)) {
                idResult = controller.updateClient(id, nom, prenom, adresse1, adresse2, lieu_dit, code_postal, bureau, telephone, courriel, commentaire, id_origine);
                if (parent == null) {
                    controller.getClientAllFrame().setVisible(true);
                }
                else {
                    ((V_Achat_Edit) parent).setVisible(true, idResult);
                }
                setVisible(false);
            }
            else {
                lbWarning.setText("L'adresse mail n'est pas valide.");
            }
        }
        else {
            lbWarning.setText("Erreur: Les champs Nom et Origine sont obligatoires.");
        }
    }//GEN-LAST:event_btValiderActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Annuler'.
     *
     * @param evt
     */
    private void btAnnulerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btAnnulerActionPerformed
    {//GEN-HEADEREND:event_btAnnulerActionPerformed
        enableLabels(true);
        btValider.setVisible(true);
        btAnnuler.setText("Annuler");
        setVisible(false);
    }//GEN-LAST:event_btAnnulerActionPerformed

    /**
     * Surcharge de la méthode setVisible(boolean b).
     *
     * @param b
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(controller.getClientAllFrame());
            lbWarning.setText("");
        }
        super.setVisible(b);
    }

    /**
     * Ajoute des lignes au comboBox pour les codes origines.
     */
    private void setOrigines() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        originesId = new int[controller.getOrigines().size()];

        int i = 0;

        for (M_Origine o : controller.getOrigines()) {
            model.addElement(o.getLibelle());
            originesId[i] = o.getId();
            i++;
        }

        cbOrigine.setModel(model);

    }

    /**
     * Met à jour les valeurs utilisés par la vue. (Lors d'un MODE_AJOUT).
     *
     * @param id
     * @param mode
     * @param parent
     */
    public void setValues(int id, int mode, JFrame parent) {
        setValues(id, null, null, null, null, null, null, null, null, null, null, 0, mode, parent);
    }

    /**
     * Met à jour les valeurs utilisées par la vue.
     *
     * @param id
     * @param nom
     * @param prenom
     * @param adresse1
     * @param adresse2
     * @param lieuDit
     * @param codePostal
     * @param bureau
     * @param telephone
     * @param courriel
     * @param commentaire
     * @param id_origine
     * @param mode
     * @param parent
     */
    public void setValues(int id, String nom, String prenom, String adresse1, String adresse2, String lieuDit, String codePostal, String bureau, String telephone, String courriel, String commentaire, int id_origine, int mode, JFrame parent) {
        this.parent = parent;
        this.mode = mode;
        this.id = id;
        setOrigines();

        switch (mode) {
            case V_Main.MODE_AJOUT:
                setTitle("Ajout d'un nouveau client");
                edNom.setText("");
                edPrenom.setText("");
                edAdresse1.setText("");
                edAdresse2.setText("");
                edLieuDit.setText("");
                edCodePostal.setText("");
                edBureau.setText("");
                edTelephone.setText("");
                edCourriel.setText("");
                edCommentaire.setText("");
                cbOrigine.setSelectedIndex(cbOrigine.getItemCount() == 0 ? -1 : 0);
                break;
            case V_Main.MODE_EDITION:
                setTitle("Edition du client " + id);
                edNom.setText(nom);
                edPrenom.setText(prenom);
                edAdresse1.setText(adresse1);
                edAdresse2.setText(adresse2);
                edLieuDit.setText(lieuDit);
                edCodePostal.setText(codePostal);
                edBureau.setText(bureau);
                edTelephone.setText(telephone);
                edCourriel.setText(courriel);
                edCommentaire.setText(commentaire);
                for (int i = 0; i < originesId.length; i++) {
                    if (originesId[i] == id_origine) {
                        cbOrigine.setSelectedIndex(i);
                        break;
                    }
                }
                break;
            default:
                setTitle("Consultation du client " + id);
                edNom.setText(nom);
                edPrenom.setText(prenom);
                edAdresse1.setText(adresse1);
                edAdresse2.setText(adresse2);
                edLieuDit.setText(lieuDit);
                edCodePostal.setText(codePostal);
                edBureau.setText(bureau);
                edTelephone.setText(telephone);
                edCourriel.setText(courriel);
                edCommentaire.setText(commentaire);
                for (int i = 0; i < originesId.length; i++) {
                    if (originesId[i] == id_origine) {
                        cbOrigine.setSelectedIndex(i);
                        break;
                    }
                }

                enableLabels(false);
                btValider.setVisible(false);
                btAnnuler.setText("Fermer");
                break;
        }
    }

    /**
     * Active ou désactive les labels.
     *
     * @param b
     */
    private void enableLabels(boolean b) {
        edNom.setEditable(b);
        edPrenom.setEditable(b);
        edAdresse1.setEditable(b);
        edAdresse2.setEditable(b);
        edLieuDit.setEditable(b);
        edCodePostal.setEditable(b);
        edBureau.setEditable(b);
        edTelephone.setEditable(b);
        edCourriel.setEditable(b);
        edCommentaire.setEditable(b);
        cbOrigine.setEnabled(b);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAnnuler;
    private javax.swing.JButton btValider;
    private javax.swing.JComboBox<String> cbOrigine;
    private javax.swing.JTextField edAdresse1;
    private javax.swing.JTextField edAdresse2;
    private javax.swing.JTextField edBureau;
    private javax.swing.JTextField edCodePostal;
    private javax.swing.JTextArea edCommentaire;
    private javax.swing.JTextField edCourriel;
    private javax.swing.JTextField edLieuDit;
    private javax.swing.JTextField edNom;
    private javax.swing.JTextField edPrenom;
    private javax.swing.JTextField edTelephone;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbAdresse;
    private javax.swing.JLabel lbBureau;
    private javax.swing.JLabel lbCommentaire;
    private javax.swing.JLabel lbCourriel;
    private javax.swing.JLabel lbNom;
    private javax.swing.JLabel lbPrenom;
    private javax.swing.JLabel lbTelephone;
    private javax.swing.JTextArea lbWarning;
    // End of variables declaration//GEN-END:variables
}
