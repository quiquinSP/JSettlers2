/**
 * Java Settlers - An online multiplayer version of the game Settlers of Catan
 * Copyright (C) 2003  Robert S. Thomas <thomas@infolab.northwestern.edu>
 * This file copyright (C) 2008-2009,2012-2013 Jeremy D Monin <jeremy@nand.net>
 * Portions of this file Copyright (C) 2013 Paul Bilnoski <paul@bilnoski.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The maintainer of this program can be reached at jsettlers@nand.net
 **/
package soc.client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import soc.client.SOCPlayerClient.ClientNetwork;
import soc.client.SOCPlayerClient.GameAwtDisplay;
import soc.util.SOCStringManager;
import soc.util.Version;


/**
 * This is the dialog for standalone client startup (JAR or otherwise)
 * if no command-line arguments.  Give choice of connect to server, start local server,
 * or create practice game.  Prompt for parameters for connect or start-server.
 *
 * @author Jeremy D Monin <jeremy@nand.net>
 */
@SuppressWarnings("serial")
public class SOCConnectOrPracticePanel extends Panel
    implements ActionListener, KeyListener
{
    private final GameAwtDisplay gd;

    /** Welcome message, or error after disconnect */
    private Label topText;

    /** "Practice" */
    private Button startGame;

    /**
     * i18n text strings; will use same locale as SOCPlayerClient's string manager.
     * @since 2.0.00
     */
    private static final SOCStringManager strings = SOCStringManager.getClientManager();

    /**
     * Creates a new SOCConnectOrPracticePanel.
     *
     * @param gd      Player client display    /** "Start a server" */
    
    public SOCConnectOrPracticePanel(GameAwtDisplay gd)
    {
        super(new BorderLayout());

        this.gd = gd;

        // same Frame setup as in SOCPlayerClient.main
        setBackground(new Color(Integer.parseInt("61AF71",16)));
        setForeground(Color.black);

        addKeyListener(this);
        initInterfaceElements();
    }

    /**
     * Check with the {@link java.lang.SecurityManager} about being a tcp server.
     * Port {@link SOCPlayerClient.ClientNetwork#SOC_PORT_DEFAULT} and some subsequent ports are checked (to be above 1024).
     * @return True if we have perms to start a server and listen on a port
     */
    public static boolean checkCanLaunchServer()
    {
        try
        {
            SecurityManager sm = System.getSecurityManager();
            if (sm == null)
                return true;
            try
            {
                sm.checkAccept("localhost", ClientNetwork.SOC_PORT_DEFAULT);
                sm.checkListen(ClientNetwork.SOC_PORT_DEFAULT);
            }
            catch (SecurityException se)
            {
                return false;
            }
        }
        catch (SecurityException se)
        {
            // can't read security mgr; check it the hard way
            int port = ClientNetwork.SOC_PORT_DEFAULT;
            for (int i = 0; i <= 100; ++i)
            {
                ServerSocket ss = null;
                try
                {
                    ss = new ServerSocket(i + port);
                    ss.setReuseAddress(true);
                    ss.setSoTimeout(11);  // very short (11 ms)
                    ss.accept();  // will time out soon
                    ss.close();
                }
                catch (SocketTimeoutException ste)
                {
                    // Allowed to bind
                    try
                    {
                        ss.close();
                    }
                    catch (IOException ie) {}
                    return true;
                }
                catch (IOException ie)
                {
                    // maybe already bound: ok, try next port in loop
                }
                catch (SecurityException se2)
                {
                    return false;  // Not allowed to have a server socket
                }
            }
        }
        return false;
    }

    /**
     * Interface setup for constructor.
     * Most elements are part of a sub-panel occupying most of this Panel, and using FlowLayout.
     * The exception is a Label at bottom with the version and build number.
     */
    private void initInterfaceElements()
    {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Panel bp = new Panel(gbl);  // Actual button panel

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        topText = new Label(strings.get("pcli.cpp.welcomeheading"));  // "Welcome to JSettlers!  Please choose an option."
        topText.setAlignment(Label.CENTER);
        gbl.setConstraints(topText, gbc);
        bp.add(topText);

        /**
         * Interface setup: Practice
         */
        startGame = new Button(strings.get("pcli.main.practice"));  // "Practice" - same as SOCPlayerClient button
        gbl.setConstraints(startGame, gbc);
        bp.add(startGame);
        startGame.addActionListener(this);

        // Final assembly setup
        add(bp, BorderLayout.CENTER);
        Label verl = new Label(strings.get("pcli.cpp.jsettlers.versionbuild", Version.version(), Version.buildnum()));
        verl.setAlignment(Label.CENTER);
        verl.setForeground(new Color(252, 251, 243)); // off-white
        add(verl, BorderLayout.SOUTH);
    }

    /**
     * Set the line of text displayed at the top of the panel.
     * @param newText  New text to display
     * @since 1.1.16
     */
    public void setTopText(final String newText)
    {
        topText.setText(newText);
        validate();
    }

    /** React to button clicks */
    public void actionPerformed(ActionEvent ae)
    {
        try {

        Object src = ae.getSource();
        if (src == startGame)
        {
            // Ask client to set up and start a practice game
            gd.clickPracticeButton();
            return;
        }

        }  // try
        catch(Throwable thr)
        {
            System.err.println("-- Error caught in AWT event thread: " + thr + " --");
            thr.printStackTrace();
            while (thr.getCause() != null)
            {
                thr = thr.getCause();
                System.err.println(" --> Cause: " + thr + " --");
                thr.printStackTrace();
            }
            System.err.println("-- Error stack trace end --");
            System.err.println();
        }

    }

    /** Stub required by KeyListener */
    public void keyReleased(KeyEvent arg0) { }

    /** Stub required by KeyListener */
    public void keyTyped(KeyEvent arg0) { }

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

}
