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
package device;

import javax.microedition.io.*;
import javax.bluetooth.*;
import java.io.*;

import remoteclient.Remote;

/**
 * This class is used to control the mobile's phone
 * Bluetooth.
 * @author Nikos Fotiou
 *
 */
public class Bluetooth implements DiscoveryListener, Runnable {

    DataInputStream din;
    LocalDevice local;
    DiscoveryAgent agent;
    DataOutputStream dout;
    StreamConnection conn;
    int currentDevice;       //used as an indicator to the device queried for the remote control server
    Remote remote;
    java.util.Vector devices;
    java.util.Vector services;
    java.util.Vector servers;
    private boolean cancel = false; //Used when users cancels query
    Thread screenUpdater;
    long screenTimeout = 2000;
    long keepAliveTimeout = 7000;
    public static final int CONNECTION_NORMAL = 1;
    public static final int CONNECTION_WARNING = 2;
    public static final int CONNECTION_TIMEOUT = -1;
    public int connectionStatus = 1; //1: Normal; 2: Missed 1 beacon; -1: Timeout
    /**
     * It creates a new instance of the Bluetooth class.
     * @param remote  The core Remote class
     */
    public Bluetooth(Remote remote) {
        this.remote = remote;
        currentDevice = 0;
    }

    /**
     * It sends data through the Bluetooth connection.
     * @param data The data to be sent
     */
    public void SendData(String data) throws IOException {
        dout.writeUTF(data);
        dout.flush();
    }

    /**
     * It listens for incoming commands
     */
    public void run() {
        boolean isrunning = true;
        long beacon = System.currentTimeMillis();
        while (isrunning) {
            try {
                String cmd = din.readUTF();
                if (cmd.startsWith("APPTOTAL")) {
                    remote.commandsTable.commandReceived(cmd);
                } else if (cmd.startsWith("APPNAME")) {
                    remote.commandsTable.commandReceived(cmd);
                } else if (cmd.startsWith("CMDKEYS")) {
                    remote.commandsTable.commandReceived(cmd);
                } else if (cmd.startsWith("CMDNAME")) {
                    remote.commandsTable.commandReceived(cmd);
                } else if (cmd.startsWith("KEY")) {
                    remote.commandsTable.commandReceived(cmd);
                }else if (cmd.equalsIgnoreCase("ENDCMD")) {
                    remote.commandsTable.commandReceived(cmd);
                }else if (cmd.startsWith("NOOP")){
                	beacon = System.currentTimeMillis();
                }else if (cmd.startsWith("SCRC")) {
                	//remote.bluetooth.SendData("ACK");
                	long start = System.currentTimeMillis();
                    final int size = Integer.parseInt(cmd.substring(5));
                    byte[] tempsrc = new byte[size];
                    while (true){
                    	if (System.currentTimeMillis()-start > screenTimeout)
                    		break;
                    	if (din.available()>=(size)){
                    		din.readFully(tempsrc, 0, size);
                            remote.mainCanvas.setScreen(tempsrc);
                            remote.bluetooth.SendData("ACK");
                    		break;
                    	}
                    }
                    
                }else{
                	//remote.doAlert(cmd, -1, remote.mainCanvas);
                	remote.bluetooth.resetInput();
                }
                //Track connection status
                int interval = (int) (System.currentTimeMillis()-beacon);
                if (interval<1500){
                	connectionStatus = CONNECTION_NORMAL;
                }else if (interval <3000){
                	connectionStatus = CONNECTION_WARNING;
                }else if (interval>keepAliveTimeout){ //10 seconds
            		connectionStatus=CONNECTION_TIMEOUT;
            		remote.bluetooth.SendData("ACK");
            	}
            	

            } catch (Exception e) {
            	//Restart the thread (thread seem to stop accepting connections after exception)
            	//remote.doAlert(e.toString(), -1, remote.mainCanvas);
            	remote.bluetooth.resetInput();
            	isrunning = false;
            	Thread t = new Thread(this);
            	t.start();
            	//Automatic failure detection and reconnection obsolete this
                /*if (remote.mode != Remote.MOUSE_MODE) {
                    isrunning = false;
                }*/
            }
            
        }
    }

    /**
     * It searches for available servers
     */
    public void FindDevices() throws BluetoothStateException {
        this.cancel = false;
        devices = new java.util.Vector();
        servers = new java.util.Vector();
        local = LocalDevice.getLocalDevice();
        agent = local.getDiscoveryAgent();
        agent.startInquiry(DiscoveryAgent.GIAC, this);
    }
    /**
     * It cancels a search query
     */
    public void CancelQuery(){
        try{
            agent.cancelInquiry(this);
        }catch(Exception e){}
        
    }
    /**
     * It connects to a Remote Control Server
     * @param serverIndex The index of the server in the servers Vector
     * @return URL The url of the server
     */
    public String connetToServer(int serverIndex) throws IOException {
        this.disconnect();
        ServiceRecord service = (ServiceRecord) services.elementAt(serverIndex);
        String url = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        conn = (StreamConnection) Connector.open(url);       //establish the connection
        dout = new DataOutputStream(conn.openOutputStream());//Get the output stream
        din = new DataInputStream(conn.openInputStream());
        Thread t = new Thread(this);
        t.start();
        return url;
    }

    /**
     * It connects to a Remote Control Server
     * @param url The url of the server to connect
     */
    public void connetToServer(String url) throws IOException {
        this.disconnect();
        conn = (StreamConnection) Connector.open(url);       //establish the connection
        dout = new DataOutputStream(conn.openOutputStream());//Get the output stream
        din = new DataInputStream(conn.openInputStream());
        Thread t = new Thread(this);
        t.start();
    }
    
    /**
     * Resets the input stream
     */
    public void resetInput() {
    	try {
			while (din.available()>0)
				din.skip(din.available());
			//din.close();
			//din = new DataInputStream(conn.openInputStream());
		} catch (IOException e) {}
    }

    /**
     * It disconects from the remote server
     */
    private void disconnect() {
        try {
            if (dout != null) {
                dout.writeUTF("CLOSE");
            }
            conn.close();
            dout.close();
            din.close();
        } catch (Exception ex) {
        }
        conn = null;
        dout = null;
        din = null;
    }

    private void FindServices(RemoteDevice device) throws BluetoothStateException {
        this.disconnect();
        UUID[] uuids = new UUID[1];
        uuids[0] = new UUID("27012f0c68af4fbf8dbe6bbaf7aa432a", false);    //The UUID of the service
        local = LocalDevice.getLocalDevice();
        agent = local.getDiscoveryAgent();
        agent.searchServices(null, uuids, device, this);

    }

    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        devices.addElement(remoteDevice);
    }

    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecord) {
        for (int x = 0; x < serviceRecord.length; x++) {
            services.addElement(serviceRecord[x]);
        }
        servers.addElement(devices.elementAt(currentDevice));
    }

    public void inquiryCompleted(int param) {
        switch (param) {
            case DiscoveryListener.INQUIRY_COMPLETED:    //Inquiry completed normally
                if (devices.size() > 0) {                 //At least one device has been found
                    try {
                        //Check if the first device offers the service
                        services = new java.util.Vector();
                        this.FindServices((RemoteDevice) devices.elementAt(0)); //Check if the first device offers the service
                    } catch (BluetoothStateException ex) {
                        remote.searchFinished(null);
                    }
                } else {
                    break;
                }
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        if (currentDevice == devices.size() - 1 && cancel == false) { //all devices have been searched
            devices = null;
            if (servers.size() > 0) {
                String[] serversArray = new String[servers.size()];
                for (int x = 0; x < servers.size(); x++) {
                    try {
                        serversArray[x] = ((RemoteDevice) servers.elementAt(x)).getFriendlyName(false);
                    } catch (IOException e) {
                        remote.doAlert("Erron in initiating search", 4000, null);
                    }
                }

                remote.searchFinished(serversArray);
            } else {
                remote.searchFinished(null);
            }
        } else {                               //search next device
            try {
                if(cancel == false){
                    currentDevice++;
                    this.FindServices((RemoteDevice) devices.elementAt(currentDevice));
                }
            } catch (BluetoothStateException ex) {
                remote.searchFinished(null);
            }
        }
    }
}
