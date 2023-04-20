/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Marian
 */
public class Meniu_Adaugare extends javax.swing.JFrame {

    public Vector<JTextField> VectorTextBox = new Vector<>(8);
    public Vector<JLabel> VectorLabel = new Vector<>(8);

    public Savepoint spt1;

    /**
     * Creates new form Meniu_Vizualizare
     */
    public Meniu_Adaugare() {
        initComponents();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        try {
            spt1 = Meniu_Principal.con.setSavepoint("svpt1");
            System.out.println("savepoint");
        } catch (Exception e) {

        }
        VectorTextBox.addElement(jTextField1);
        VectorTextBox.addElement(jTextField2);
        VectorTextBox.addElement(jTextField3);
        VectorTextBox.addElement(jTextField4);
        VectorTextBox.addElement(jTextField5);
        VectorTextBox.addElement(jTextField6);
        VectorTextBox.addElement(jTextField7);
        VectorTextBox.addElement(jTextField8);
        for (int i = 0; i < 8; i++) {
            VectorTextBox.elementAt(i).setEnabled(false);
        }

        VectorLabel.addElement(jLabel1);
        VectorLabel.addElement(jLabel2);
        VectorLabel.addElement(jLabel3);
        VectorLabel.addElement(jLabel4);
        VectorLabel.addElement(jLabel5);
        VectorLabel.addElement(jLabel6);
        VectorLabel.addElement(jLabel7);
        VectorLabel.addElement(jLabel8);
        for (int i = 0; i < 8; i++) {
            VectorLabel.elementAt(i).setEnabled(false);
        }
        jComboBox1.setEnabled(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTabela = new javax.swing.JLabel();
        comboTabela = new javax.swing.JComboBox<>();
        btnSelecteaza = new javax.swing.JButton();
        btnAdauga = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        btnRollback = new javax.swing.JButton();
        btnSalvare = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        labelTabela.setText("Selectati tabela in care adaugati:");

        comboTabela.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Statie", "Depou", "Angajat", "Material Rulant", "Garnitura", "Ruta" }));
        comboTabela.setSelectedItem(comboTabela);
        comboTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTabelaActionPerformed(evt);
            }
        });

        btnSelecteaza.setText("Selecteaza");
        btnSelecteaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecteazaActionPerformed(evt);
            }
        });

        btnAdauga.setText("Adauga");
        btnAdauga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdaugaActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        btnRollback.setBackground(new java.awt.Color(255, 51, 51));
        btnRollback.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRollback.setForeground(new java.awt.Color(255, 255, 255));
        btnRollback.setText("Rollback");
        btnRollback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollbackActionPerformed(evt);
            }
        });

        btnSalvare.setBackground(new java.awt.Color(153, 255, 153));
        btnSalvare.setText("Salvare");
        btnSalvare.setDefaultCapable(false);
        btnSalvare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvareActionPerformed(evt);
            }
        });

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSelecteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(311, 311, 311)
                                .addComponent(btnSalvare, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRollback, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(217, 217, 217)
                                .addComponent(btnAdauga, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField1, jTextField2, jTextField3, jTextField4, jTextField5, jTextField6, jTextField7, jTextField8});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(labelTabela)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelecteaza, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRollback, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvare, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(122, 122, 122)
                .addComponent(btnAdauga, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelecteazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecteazaActionPerformed

        int valoare = comboTabela.getSelectedIndex();
        for (int i = 0; i < 6; i++) {
            VectorLabel.elementAt(i).setEnabled(false);
            VectorTextBox.elementAt(i).setEnabled(false);
            VectorLabel.elementAt(i).setText("");
            VectorTextBox.elementAt(i).setText("");
        }
        switch (valoare) {
            case 0:
                for (int i = 0; i < 6; i++) {
                    VectorLabel.elementAt(i).setEnabled(true);
                    VectorTextBox.elementAt(i).setEnabled(true);
                }
                jLabel1.setText("Nume (Char(20))");
                jLabel2.setText("Adresa (Char(50))");
                jLabel3.setText("Email (Char(30))");
                jLabel4.setText("Facilitati (Char(100))");
                jLabel5.setText("Nume_regionala (Char(15))");
                jLabel6.setText("Numar_angajati (Char(4))");
                break;
            case 1:
                try {
                jComboBox1.setVisible(true);
                jComboBox1.setEnabled(true);

                Statement stm = Meniu_Principal.con.createStatement();
                ResultSet res;
                res = stm.executeQuery("select Nume from statie");
                List<String> listStatie = new ArrayList<>();
                while (res.next()) {
                    if (listStatie.contains(res.getString(1).trim()) == false) {
                        listStatie.add(res.getString(1).trim());
                    }
                }
                for (String k : listStatie) {
                    jComboBox1.addItem(k);
                }
            } catch (Exception e) {
            }
            for (int i = 1; i < 4; i++) {
                VectorLabel.elementAt(i).setEnabled(true);
                VectorTextBox.elementAt(i).setEnabled(true);
            }
            jLabel1.setEnabled(true);
            jLabel1.setText("Statia de care apartine");
            jLabel2.setText("Descriere (Char(20))");
            jLabel3.setText("Numar angajati (Numeric(3))");
            jLabel4.setText("Facilitati (Char(100))");
            break;

            case 2:
                try {
                jComboBox1.setVisible(true);
                jComboBox1.setEnabled(true);

                Statement stm = Meniu_Principal.con.createStatement();
                ResultSet res;
                res = stm.executeQuery("select Nume from statie");
                List<String> listStatie = new ArrayList<>();
                while (res.next()) {
                    if (listStatie.contains(res.getString(1).trim()) == false) {
                        listStatie.add(res.getString(1).trim());
                    }
                }
                for (String k : listStatie) {
                    jComboBox1.addItem(k);
                }
            } catch (Exception e) {
            }
            for (int i = 1; i < 6; i++) {
                VectorLabel.elementAt(i).setEnabled(true);
                VectorTextBox.elementAt(i).setEnabled(true);
            }
            jLabel1.setEnabled(true);
            jLabel1.setText("Statia de care apartine");
            jLabel2.setText("Nume (Char(20))");
            jLabel3.setText("Prenume (Char(20))");
            jLabel4.setText("Functie (Char(20))");
            jLabel5.setText("Salariu (Numeric(5))");
            jLabel6.setText("Ani vechime (Numeric(2))");
            break;

            case 3:
                try {
                jComboBox1.setVisible(true);
                jComboBox1.setEnabled(true);

                Statement stm = Meniu_Principal.con.createStatement();
                ResultSet res;
                res = stm.executeQuery("select Descriere from depou");
                List<String> listDepou = new ArrayList<>();
                while (res.next()) {
                    if (listDepou.contains(res.getString(1).trim()) == false) {
                        listDepou.add(res.getString(1).trim());
                    }
                }
                for (String k : listDepou) {
                    jComboBox1.addItem(k);
                }
            } catch (Exception e) {
            }
            for (int i = 1; i < 6; i++) {
                VectorLabel.elementAt(i).setEnabled(true);
                VectorTextBox.elementAt(i).setEnabled(true);
            }
            jLabel1.setEnabled(true);
            jLabel1.setText("Depoul de care apartine");
            jLabel2.setText("Numar parc (Numeric(4))");
            jLabel3.setText("Tip (Char(3))");
            jLabel4.setText("Status (Char(7))");
            jLabel5.setText("Capacitate (Numeric(3))");
            jLabel6.setText("Carburant (Char(1))");
            break;

            default:
                break;
        }
    }//GEN-LAST:event_btnSelecteazaActionPerformed

    private void btnAdaugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdaugaActionPerformed
        int valoare = comboTabela.getSelectedIndex();
        switch (valoare) {
            case 0:
                try {
                PreparedStatement stm;
                stm = Meniu_Principal.con.prepareStatement("insert into statie(nume,adresa,email,facilitati,nume_regionala,numar_angajati) values (?, ?, ?, ?, ?, ?)");
                stm.setString(1, VectorTextBox.elementAt(0).getText().trim());
                stm.setString(2, VectorTextBox.elementAt(1).getText().trim());
                stm.setString(3, VectorTextBox.elementAt(2).getText().trim());
                stm.setString(4, VectorTextBox.elementAt(3).getText().trim());
                stm.setString(5, VectorTextBox.elementAt(4).getText().trim());
                stm.setInt(6, Integer.valueOf(VectorTextBox.elementAt(5).getText()));
                JOptionPane.showMessageDialog(null, "Succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

                stm.executeUpdate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "Eroare", JOptionPane.INFORMATION_MESSAGE);

            }
            break;

            //*************** DEPOU **************
            case 1:
                try {
                PreparedStatement stm;
                stm = Meniu_Principal.con.prepareStatement("insert into depou(id_statie,descriere,numar_angajati,facilitati) values ((select s.id_statie from statie s where lower(trim(s.nume)) = lower(trim(?))), ?, ?, ?)");
                stm.setString(1, jComboBox1.getSelectedItem().toString().trim().toLowerCase());
                stm.setString(2, VectorTextBox.elementAt(1).getText().trim());
                stm.setInt(3, Integer.valueOf(VectorTextBox.elementAt(2).getText()));
                stm.setString(4, VectorTextBox.elementAt(3).getText().trim());
                stm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "Eroare", JOptionPane.INFORMATION_MESSAGE);

            }
            break;

            //*************** Angajat **************
            case 2:
                try {
                PreparedStatement stm;
                stm = Meniu_Principal.con.prepareStatement("insert into angajat(id_statie,nume_persoana,prenume_persoana,functie,salariu,ani_vechime) values ((select s.id_statie from statie s where lower(trim(s.nume)) = lower(trim(?))), ?, ?, ?,?,?)");
                stm.setString(1, jComboBox1.getSelectedItem().toString().trim().toLowerCase());
                stm.setString(2, VectorTextBox.elementAt(1).getText().trim());
                stm.setString(3, VectorTextBox.elementAt(2).getText().trim());
                stm.setString(4, VectorTextBox.elementAt(3).getText().trim());
                stm.setInt(5, Integer.valueOf(VectorTextBox.elementAt(4).getText()));
                stm.setInt(6, Integer.valueOf(VectorTextBox.elementAt(5).getText()));
                stm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "Eroare", JOptionPane.INFORMATION_MESSAGE);

            }
            break;

            case 3:
                try {
                PreparedStatement stm;
                stm = Meniu_Principal.con.prepareStatement("insert into material_rulant(id_depou,numar_parc,tip,status,capacitate,carburant) values ((select d.id_depou from depou d where lower(trim(d.descriere)) = lower(trim(?))), ?, ?, ?,?,?)");
                stm.setString(1, jComboBox1.getSelectedItem().toString().trim().toLowerCase());
                stm.setInt(2, Integer.valueOf(VectorTextBox.elementAt(1).getText()));
                stm.setString(3, VectorTextBox.elementAt(2).getText().trim());
                stm.setString(4, VectorTextBox.elementAt(3).getText().trim());
                if (VectorTextBox.elementAt(4).getText().trim().equals("")) {
                    stm.setNull(5, java.sql.Types.NULL);
                } else {
                    stm.setInt(5, Integer.valueOf(VectorTextBox.elementAt(4).getText()));
                }
                if (VectorTextBox.elementAt(5).getText().trim().equals("")) {
                    stm.setNull(6, java.sql.Types.NULL);
                } else {
                    stm.setString(6, VectorTextBox.elementAt(5).getText().trim());
                }

                stm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "Eroare", JOptionPane.INFORMATION_MESSAGE);

            }
            break;
            default:
                break;
        }
    }//GEN-LAST:event_btnAdaugaActionPerformed

    private void comboTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTabelaActionPerformed

    }//GEN-LAST:event_comboTabelaActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void btnRollbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollbackActionPerformed
        try {
            Meniu_Principal.con.rollback(spt1);
            Meniu_Principal.con.commit();
            System.out.println("rollback");
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnRollbackActionPerformed

    private void btnSalvareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvareActionPerformed
        try {
            Meniu_Principal.con.commit();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnSalvareActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new Meniu_Principal().show();
        this.hide();
    }//GEN-LAST:event_formWindowClosing

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Meniu_Vizualizare.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Meniu_Vizualizare.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Meniu_Vizualizare.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Meniu_Vizualizare.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Meniu_Vizualizare().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdauga;
    private javax.swing.JButton btnRollback;
    private javax.swing.JButton btnSalvare;
    private javax.swing.JButton btnSelecteaza;
    private javax.swing.JComboBox<String> comboTabela;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JLabel labelTabela;
    // End of variables declaration//GEN-END:variables
}