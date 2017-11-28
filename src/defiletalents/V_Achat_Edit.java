package defiletalents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.DefaultComboBoxModel;
import methodesVerif.Verify;
import org.joda.time.DateTime;

/**
 * Classe permettant de gérer la vue relative à un enregistrement de la table
 * tal_achat.
 *
 * @author giraudeaup
 */
public class V_Achat_Edit extends javax.swing.JFrame {

    /**
     * Controleur de l'appli.
     */
    private Controller controller;
    /**
     * Identifiant de l'enregistrement.
     */
    private int id = 0;
    /**
     * Mode de la vue. Voir V_Main.
     */
    private int mode = 0;

    /**
     * Date selectionné sur le calendrier.
     */
    private Calendar selectedDate;
    /**
     * Tableau d'entiers représentant les id des clients utilisé pour le
     * comboBox.
     */
    private int[] clientsId;

    /**
     * Constructeur de la vue.
     *
     * @param controller
     */
    public V_Achat_Edit(Controller controller) {
        this.controller = controller;
        initComponents();
        setSelectedDate(Calendar.getInstance());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbDate = new javax.swing.JLabel();
        lbRefResa = new javax.swing.JLabel();
        edDate = new javax.swing.JTextField();
        ckbValide = new javax.swing.JCheckBox();
        edRefResa = new javax.swing.JTextField();
        cbClient = new javax.swing.JComboBox();
        lbClient = new javax.swing.JLabel();
        lbCommentaire = new javax.swing.JLabel();
        btValider = new javax.swing.JButton();
        btAnnuler = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        edCommentaire = new javax.swing.JTextArea();
        btDate = new javax.swing.JButton();
        btListClients = new javax.swing.JButton();
        btNouveau = new javax.swing.JButton();
        lbWarning = new javax.swing.JLabel();

        setResizable(false);

        lbDate.setText("Date :");

        lbRefResa.setText("Référence de réservation : ");

        edDate.setToolTipText("Format: dd/mm/yyyy");
        edDate.setNextFocusableComponent(edRefResa);

        ckbValide.setLabel("Valide ?");

        edRefResa.setEditable(false);
        edRefResa.setNextFocusableComponent(btValider);

        cbClient.setNextFocusableComponent(edDate);

        lbClient.setText("Client : ");

        lbCommentaire.setText("Commentaire : ");

        btValider.setText("Suivant");
        btValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btValiderActionPerformed(evt);
            }
        });

        btAnnuler.setText("Annuler");
        btAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAnnulerActionPerformed(evt);
            }
        });

        edCommentaire.setColumns(20);
        edCommentaire.setLineWrap(true);
        edCommentaire.setRows(5);
        jScrollPane1.setViewportView(edCommentaire);

        btDate.setText("▼");
        btDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDateActionPerformed(evt);
            }
        });

        btListClients.setText("Voir liste");
        btListClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListClientsActionPerformed(evt);
            }
        });

        btNouveau.setText("+");
        btNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNouveauActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lbWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(btValider, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbCommentaire)
                                .addGap(254, 285, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbRefResa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btNouveau)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btListClients))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ckbValide)
                                .addGap(20, 20, 20))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(edDate, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbDate, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(207, 207, 207)
                                            .addComponent(btDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(1, 1, 1)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(edRefResa, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btNouveau)
                    .addComponent(btListClients))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbRefResa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckbValide))
                .addGap(18, 18, 18)
                .addComponent(edRefResa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(lbCommentaire)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbWarning, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btValider)
                        .addComponent(btAnnuler)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Méthode utilisée lorsque que l'on appuie sur le bouton 'Valider'.
     *
     * @param evt
     */
    private void btValiderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btValiderActionPerformed
    {//GEN-HEADEREND:event_btValiderActionPerformed
        try {
            DateTime date_resa = null;
            String ref_resa = edRefResa.getText();
            boolean valide = ckbValide.isSelected();
            String commentaire = edCommentaire.getText();
            int id_client = clientsId[cbClient.getSelectedIndex()];

            try {
                date_resa = new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(edDate.getText() + " " + DateTime.now().toString("HH:mm:ss")));
            }
            catch (Exception e) {
                LogHandler.log().store(e);
                date_resa = new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")));
            }

            if (id_client != -1) {
                int id_achat = controller.updateAchat(id, date_resa, ref_resa, valide, commentaire, id_client);
                controller.getAchatsAllFrame().setVisible(true);
                controller.setMainStats();
                setVisible(false);
                controller.getLigneAllFrame(id_achat).setVisible(true);
            }
            else {
                lbWarning.setText("Erreur: Le champs client est requis.");
            }
        }
        catch (ParseException ex) {
            LogHandler.log().store(ex);
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
     * Méthode utilisée lorsque l'on appuie sur le bouton d'affichage du
     * calendrier.
     *
     * @param evt
     */
    private void btDateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btDateActionPerformed
    {//GEN-HEADEREND:event_btDateActionPerformed
        controller.getCalendarFrame(this).setVisible(true);
    }//GEN-LAST:event_btDateActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton d'affichage de la
     * liste des clients.
     *
     * @param evt
     */
    private void btListClientsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btListClientsActionPerformed
    {//GEN-HEADEREND:event_btListClientsActionPerformed
        controller.getClientSelectFrame(this).setVisible(true);
    }//GEN-LAST:event_btListClientsActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton '+' Pour ajouter un
     * nouveau client.
     *
     * @param evt
     */
    private void btNouveauActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btNouveauActionPerformed
    {//GEN-HEADEREND:event_btNouveauActionPerformed
        controller.getClientEditFrame(V_Main.MODE_AJOUT, this).setVisible(true);
    }//GEN-LAST:event_btNouveauActionPerformed

    /**
     * Surcharge de la méthode setVisible(boolean b).
     *
     * @param b
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(null);
            lbWarning.setText("");
        }
        super.setVisible(b);
    }

    /**
     * Permet de rendre la vue visible et de retourner l'identifiant du client
     * selectionné.
     *
     * @param b
     * @param id
     */
    public void setVisible(boolean b, int id) {
        setVisible(b);
        setListClients();
        setSelectedClient(id);
    }

    /**
     * Permet de remplir le comboBox clients.
     */
    private void setListClients() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        CopyOnWriteArrayList<M_Client> clients = Verify.trierClients(controller.getClients());
        clientsId = new int[clients.size()];
        int i = 0;

        for (M_Client c : clients) {
            model.addElement(c.getNom() + " " + c.getPrenom());
            clientsId[i] = c.getId();
            i++;
        }

        cbClient.setModel(model);

    }

    /**
     * Valorise la date selectionnée.
     *
     * @param date
     */
    public void setSelectedDate(Calendar date) {
        this.selectedDate = date;
        edDate.setText(getSelectedDate().toString("yyyy-MM-dd"));
    }

    /**
     * Retourne la date selectionnée.
     *
     * @return
     */
    public DateTime getSelectedDate() {
        return new DateTime(this.selectedDate.getTime());
    }

    /**
     * Selectionne un client dans le comboBox clients.
     *
     * @param id
     */
    public void setSelectedClient(int id) {
        int i = 0;
        for (i = 0; i < clientsId.length; i++) {
            if (clientsId[i] == id) {
                cbClient.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Met à jour la fenêtre avec les valeurs à utiliser.
     *
     * @param id
     * @param date_resa
     * @param ref_resa
     * @param valide
     * @param commentaire
     * @param id_client
     * @param mode MODE_AJOUT pour un nouvel achat, MODE_EDITION pour modifier
     * un achat et MODE_CONSULT pour consulter uniquement.
     */
    public void setValues(int id, String date_resa, String ref_resa, boolean valide, String commentaire, int id_client, int mode) {
        setListClients();
        this.mode = mode;
        this.id = id;

        switch (mode) {
            case V_Main.MODE_AJOUT:
                setTitle("Ajout d'une nouvelle vente");

                edRefResa.setText(generateRef());
                edDate.setText(getSelectedDate().toString("yyyy-MM-dd"));
                edCommentaire.setText("");
                ckbValide.setSelected(false);
                cbClient.setSelectedIndex(cbClient.getItemCount() == 0 ? -1 : 0);
                break;
            case V_Main.MODE_EDITION:
                setTitle("Edition de la vente " + id);
                edDate.setText(date_resa);
                edRefResa.setText(ref_resa);
                ckbValide.setSelected(valide);
                edCommentaire.setText(commentaire);
                setSelectedClient(id_client);
                break;
            default:
                setTitle("Consultation de la vente " + id);
                edDate.setText(date_resa);
                edRefResa.setText(ref_resa);
                ckbValide.setSelected(valide);
                edCommentaire.setText(commentaire);
                setSelectedClient(id_client);

                enableLabels(false);
                btValider.setVisible(false);
                btAnnuler.setText("Fermer");
                break;
        }
    }

    /**
     * Active ou désactive l'édition des labels de la vue.
     *
     * @param b
     */
    private void enableLabels(boolean b) {
        edDate.setEditable(b);
        btDate.setEnabled(b);
        ckbValide.setEnabled(b);
        edCommentaire.setEditable(b);
        cbClient.setEnabled(b);
        btListClients.setEnabled(b);
        btNouveau.setEnabled(b);
    }

    /**
     * Genere une reference aléatoire n'existant pas deja dans la table
     * tal_achat.
     *
     * @return
     */
    private String generateRef() {
        String result = "";
        Random random = new Random();
        boolean error = true;
        while (error) {
            result = "ref_" + Integer.toString(random.nextInt(9999));
            error = false;
            for (M_Achat a : controller.getAchats()) {
                if (a.getRef_resa().equals(result)) {
                    error = true;
                    break;
                }
            }
        }
        return result;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAnnuler;
    private javax.swing.JButton btDate;
    private javax.swing.JButton btListClients;
    private javax.swing.JButton btNouveau;
    private javax.swing.JButton btValider;
    private javax.swing.JComboBox cbClient;
    private javax.swing.JCheckBox ckbValide;
    private javax.swing.JTextArea edCommentaire;
    private javax.swing.JTextField edDate;
    private javax.swing.JTextField edRefResa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbClient;
    private javax.swing.JLabel lbCommentaire;
    private javax.swing.JLabel lbDate;
    private javax.swing.JLabel lbRefResa;
    private javax.swing.JLabel lbWarning;
    // End of variables declaration//GEN-END:variables

}
