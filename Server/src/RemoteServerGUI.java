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



import javax.swing.*;

/**
 * The GUI of the remote control server
 * @author Nikos Fotiou
 *
 */
public class RemoteServerGUI extends JFrame {

    /**
     * It creates a new instance of the RemoteServerGUI()
     */
    public RemoteServerGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            java.awt.Image logo = java.awt.Toolkit.getDefaultToolkit().getImage("icon.png");
            this.setIconImage(logo);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        initComponents();

    }
    /**
     * It displays a messagebox
     * @param msg The message to be displayed
     */
    public void showMsgbox (String msg){
        final JDialog dialog = new JDialog(this,"Message",false);        
        java.awt.Dimension sdimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        dialog.getContentPane().setLayout(null);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JButton okButton = new JButton("ok");
        JLabel msgLabel = new JLabel();
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dialog.setVisible(false);
            }
        });
        msgLabel.setText(msg);
        dialog.getContentPane().add(msgLabel);
        msgLabel.setBounds(20, 20, 230, 100);
        dialog.getContentPane().add(okButton);
        okButton.setBounds(100, 130, 60, 23);
        dialog.pack();
        dialog.setLocation(sdimension.width / 2 - 200, sdimension.height / 2 - 200);
        dialog.setSize(282, 216);
        dialog.setVisible(true);
        
    }
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        InfoLabel = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        ExitItem = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        HelpIndexItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        HelpAboutItem = new javax.swing.JMenuItem();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bluetooth Remote Control");
        setResizable(false);
        InfoLabel.setBackground(new java.awt.Color(233, 233, 245));
        InfoLabel.setColumns(1);
        InfoLabel.setEditable(false);
        InfoLabel.setFont(new java.awt.Font("Arial", 0, 12));
        InfoLabel.setRows(1);
        jScrollPane1.setViewportView(InfoLabel);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 240, 130);

        FileMenu.setText("File");
        ExitItem.setText("Exit");
        ExitItem.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ExitItemMouseReleased(evt);
            }
        });

        FileMenu.add(ExitItem);

        jMenuBar1.add(FileMenu);

        HelpMenu.setText("Help");
        HelpIndexItem.setMnemonic('p');
        HelpIndexItem.setText("Index");
        HelpIndexItem.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                HelpIndexItemMouseReleased(evt);
            }
        });

        HelpMenu.add(HelpIndexItem);
        HelpMenu.add(jSeparator1);
        HelpAboutItem.setText("About");
        HelpAboutItem.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                HelpAboutItemMouseReleased(evt);
            }
        });

        HelpMenu.add(HelpAboutItem);
        jMenuBar1.add(HelpMenu);
        setJMenuBar(jMenuBar1);
        pack();
    }

    private void HelpAboutItemMouseReleased(java.awt.event.MouseEvent evt) {                                            
        new About(this, true).setVisible(true);
    }

    private void HelpIndexItemMouseReleased(java.awt.event.MouseEvent evt) {                                            
        try {
            String sysName = System.getProperty("os.name").toLowerCase();
            if (sysName.indexOf("windows") != -1) {
                Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", "help/index.htm"});
            }else{//Linux
                try{//KDE environment
                    Runtime.getRuntime().exec("kfmclient exec help/index.htm");
                }catch(Exception e){}
                try{//Gnome environment
                    Runtime.getRuntime().exec("gnome-open help/index.htm");
                }catch(Exception e){}
            }
                
        } catch (Exception e) {
        }

    }

    private void ExitItemMouseReleased(java.awt.event.MouseEvent evt) {                                       
        System.exit(0);

    }
    private javax.swing.JMenuItem ExitItem;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenuItem HelpAboutItem;
    private javax.swing.JMenuItem HelpIndexItem;
    private javax.swing.JMenu HelpMenu;
    public javax.swing.JTextArea InfoLabel;
    private javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
}
