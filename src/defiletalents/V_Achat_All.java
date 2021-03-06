package defiletalents;

import javax.swing.table.DefaultTableModel;

/**
 * Classe permettant de gérer la vue relative à la table tal_achat.
 *
 * @author giraudeaup
 */
public class V_Achat_All extends javax.swing.JFrame {

    /**
     * Le Controleur de l'application.
     */
    private Controller controller;

    /**
     * Constructeur de la vue.
     *
     * @param controller
     */
    public V_Achat_All(Controller controller) {
        this.controller = controller;
        initComponents();
        setTitle("Ventes");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btEdit = new javax.swing.JButton();
        btDel = new javax.swing.JButton();
        btClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAchats = new javax.swing.JTable();
        btPaiements = new javax.swing.JButton();
        btPlaces = new javax.swing.JButton();
        btClient = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(640, 480));
        setResizable(false);
        setSize(new java.awt.Dimension(640, 480));

        btEdit.setText("Modifier...");
        btEdit.setMaximumSize(new java.awt.Dimension(160, 160));
        btEdit.setMinimumSize(new java.awt.Dimension(160, 160));
        btEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditActionPerformed(evt);
            }
        });

        btDel.setText("Supprimer...");
        btDel.setMaximumSize(new java.awt.Dimension(160, 160));
        btDel.setMinimumSize(new java.awt.Dimension(160, 160));
        btDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelActionPerformed(evt);
            }
        });

        btClose.setText("Fermer");
        btClose.setMaximumSize(new java.awt.Dimension(160, 160));
        btClose.setMinimumSize(new java.awt.Dimension(160, 160));
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        tableAchats.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Id", "Date", "Référence", "Client"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAchats.setMaximumSize(new java.awt.Dimension(480, 18));
        tableAchats.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableAchats);
        if (tableAchats.getColumnModel().getColumnCount() > 0) {
            tableAchats.getColumnModel().getColumn(0).setMinWidth(0);
            tableAchats.getColumnModel().getColumn(0).setPreferredWidth(0);
            tableAchats.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        btPaiements.setText("Paiements...");
        btPaiements.setMaximumSize(new java.awt.Dimension(160, 160));
        btPaiements.setMinimumSize(new java.awt.Dimension(160, 160));
        btPaiements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPaiementsActionPerformed(evt);
            }
        });

        btPlaces.setText("Places...");
        btPlaces.setMaximumSize(new java.awt.Dimension(160, 160));
        btPlaces.setMinimumSize(new java.awt.Dimension(160, 160));
        btPlaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPlacesActionPerformed(evt);
            }
        });

        btClient.setText("Voir Client...");
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        btAdd.setText("Ajouter...");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btDel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(btClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btPlaces, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btPaiements, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btClient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btPaiements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(btDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        setBounds(0, 0, 650, 510);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Méthode utilisé lorsque l'on appuie sur le bouton 'Fermer'.
     *
     * @param evt
     */
    private void btCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btCloseActionPerformed
    {//GEN-HEADEREND:event_btCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btCloseActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Modifier'
     *
     * @param evt
     */
    private void btEditActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btEditActionPerformed
    {//GEN-HEADEREND:event_btEditActionPerformed
        if (tableAchats.getSelectedRow() != -1) {
            controller.getAchatsEditFrame((int) tableAchats.getModel().getValueAt(tableAchats.getSelectedRow(), 0), V_Main.MODE_EDITION).setVisible(true);
        }
    }//GEN-LAST:event_btEditActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Supprimer'.
     *
     * @param evt
     */
    private void btDelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btDelActionPerformed
    {//GEN-HEADEREND:event_btDelActionPerformed
        if (tableAchats.getSelectedRow() != -1) {
            controller.getDeleteConfirmFrame("Achat", String.valueOf((int) tableAchats.getModel().getValueAt(tableAchats.getSelectedRow(), 0))).setVisible(true);
        }
    }//GEN-LAST:event_btDelActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Paiements'.
     *
     * @param evt
     */
    private void btPaiementsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btPaiementsActionPerformed
    {//GEN-HEADEREND:event_btPaiementsActionPerformed
        if (tableAchats.getSelectedRow() != -1) {
            controller.getPaiementAllFrame((int) tableAchats.getModel().getValueAt(tableAchats.getSelectedRow(), 0)).setVisible(true);
        }
    }//GEN-LAST:event_btPaiementsActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Places'.
     *
     * @param evt
     */
    private void btPlacesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btPlacesActionPerformed
    {//GEN-HEADEREND:event_btPlacesActionPerformed
        if (tableAchats.getSelectedRow() != -1) {
            controller.getLigneAllFrame((int) tableAchats.getModel().getValueAt(tableAchats.getSelectedRow(), 0)).setVisible(true);
        }
    }//GEN-LAST:event_btPlacesActionPerformed

    /**
     * Méthode utilisée lorsque l'on appuie sur le bouton 'Voir Client'.
     *
     * @param evt
     */
    private void btClientActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btClientActionPerformed
    {//GEN-HEADEREND:event_btClientActionPerformed
        if (tableAchats.getSelectedRow() != -1) {
            int id_client = controller.getClient(controller.getAchat((int) tableAchats.getValueAt(tableAchats.getSelectedRow(), 0)).getId_client()).getId();
            controller.getClientEditFrame(id_client, V_Main.MODE_CONSULT, null).setVisible(true);
        }
    }//GEN-LAST:event_btClientActionPerformed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        controller.getAchatsEditFrame(V_Main.MODE_AJOUT).setVisible(true);
    }//GEN-LAST:event_btAddActionPerformed

    /**
     * Surchage de la méthode setVisible(boolean b).
     *
     * @param b
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(controller.getMainFrame());
            setTable();
        }
        super.setVisible(b);
    }

    /**
     * Met à jour le tableau utilisé par la fenêtre.
     */
    private void setTable() {
        DefaultTableModel model = (DefaultTableModel) tableAchats.getModel();
        int size = controller.getAchats().size();
        int i = 0;
        String client = "null";
        model.setRowCount(size);

        for (M_Achat a : controller.getAchats()) {
            model.setValueAt(a.getId(), i, 0);
            model.setValueAt(a.getDate_resaAsString(), i, 1);
            model.setValueAt(a.getRef_resa(), i, 2);
            client = ((controller.getClient(a.getId_client()) != null) ? controller.getClient(a.getId_client()).getNom() + " " + controller.getClient(a.getId_client()).getPrenom() : "null");
            model.setValueAt(client, i, 3);
            i++;
        }
        tableAchats.setModel(model);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btClient;
    private javax.swing.JButton btClose;
    private javax.swing.JButton btDel;
    private javax.swing.JButton btEdit;
    private javax.swing.JButton btPaiements;
    private javax.swing.JButton btPlaces;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableAchats;
    // End of variables declaration//GEN-END:variables
}
