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


import java.io.IOException;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class parses the applist.xml document and it sends the 
 * command supported to the remote client
 * @author Nikos Fotiou
 */
public class SendAppList extends DefaultHandler {

    private RemoteServer rs;

    public SendAppList(RemoteServer rs) {
        this.rs = rs;
    }

    /**
     * Static method for parsing the applist.xml file and sending the
     * data to the client
     * @param rs The main RemoteServer Class
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static void startSending(RemoteServer rs) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        xmlReader.setContentHandler(new SendAppList(rs));
        xmlReader.parse("applist.xml");

    }

    @Override
    public void startElement(String uri, String lname, String qname, Attributes attributes) {
        if (qname.equals("applist")) {
            rs.bluetooth.SendData("APPTOTAL " + attributes.getValue("total"));

        }
        if (qname.equals("app")) {
            rs.bluetooth.SendData("APPNAME " + attributes.getValue("name"));

        }
        if (qname.equals("cmd")) {
            rs.bluetooth.SendData("CMDKEYS " + attributes.getValue("keys"));
            rs.bluetooth.SendData("CMDNAME " + attributes.getValue("name"));

        }
        if (qname.equals("key")) {
            rs.bluetooth.SendData("KEY " + attributes.getValue("value"));

        }
    }

    @Override
    public void characters(char[] chars, int start, int length) {
    }

    @Override
    public void endElement(String uri, String lname, String qname) {
        if (qname.equals("app")) {
            rs.bluetooth.SendData("ENDCMD");
        }
    }
}
