/*
Copyright (C) 2008 Nikolaos Fotiou

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */

import java.awt.Cursor;

/**
 * This class is used to display the About dialog which appears
 * when the user selects Help->About
 * @author Nikos Fotiou
 * 	
 */
public class About extends javax.swing.JDialog {

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;

    /** 
     * It creates a new About form 
     * 
     */
    public About(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setSize(400, 250);
        java.awt.Dimension sdimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(sdimension.width / 2 - 200, sdimension.height / 2 - 200);

    }

    /**
     * This method is used to initiate the Form's Components
     */
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        getContentPane().setLayout(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About Bluetooth  Remote Control");
        setResizable(false);
        jLabel1.setText("<html><p align=\"center\"><br>Bluetooth Remote Control<br>Ver 2.0<br>Nikos Fotiou, http://www.miniware.net/remote</p></html>");
        java.net.URL imgURL = getClass().getResource("icon.png");
        jLabel1.setIcon(new javax.swing.ImageIcon(imgURL,"logo"));   
        jLabel1.setBounds(20, 20, 350, 100);
        jLabel1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton1.setText("OK");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        getContentPane().add(jButton1);
        getContentPane().add(jLabel1);
        jButton1.setBounds(175, 150, 60, 23);
        pack();
    }

    /**
     * This method is invoked whenever the OK button is pressed.
     * It closes the About Form 
     */
    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {                                       
        setVisible(false);
        dispose();
    }
}
