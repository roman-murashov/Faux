/**
 * Copyright 2014 Joshua Asbury (@theoriginalbit)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.theoriginalbit.faux;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import com.theoriginalbit.faux.log.Log;
import com.theoriginalbit.faux.ui.EmulatorMenuBar;
import com.theoriginalbit.faux.util.DialogUtils;
import com.theoriginalbit.faux.util.OperatingSystem;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author theoriginalbit
 */
public final class Bootstrap {
    public static void initialise(final Emulator emulator) {
        Log.info("%s starting...", AppInfo.NAME);
        setLookAndFeel();
        initialiseMenuBar(emulator);
        initialiseWindow(emulator);
        initialiseLWJGL(emulator);
        initialiseGame(emulator);
    }

    private static void initialiseGame(final Emulator emulator) {
    }

    private static void setLookAndFeel() {
        final JFrame frame = new JFrame();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                System.err.println("Your java failed to provide normal look and feel, trying the old fallback now");
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Throwable t) {
                System.err.println("Unexpected exception setting look and feel.");
                t.printStackTrace();
            }
        }
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("test"));
        frame.add(panel);
        try {
            frame.pack();
        } catch (Throwable t) {
            System.err.println("Custom (broken) theme detected, falling back onto x-platform theme");
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }

        frame.dispose();
    }

    private static void initialiseWindow(final Emulator emulator) {
        final JFrame window = emulator.getWindow();

        // setup the window
        window.getContentPane().removeAll();
        window.setTitle(AppInfo.NAME);
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (DialogUtils.showQuitDialog(emulator.getWindow()) == JOptionPane.OK_OPTION) {
                    emulator.shutdown();
                }
            }
        });

        // add the menu bar
        window.setJMenuBar(new EmulatorMenuBar(window, emulator));

        // add the canvas
        final Canvas canvas = emulator.getCanvas();
        canvas.setIgnoreRepaint(true);
        window.add(canvas);

        // create the window
        window.setPreferredSize(new Dimension(800, 600));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * Sets up the OS specific menu bars where appropriate. This is primarily OS X hacks to make
     * Java use the native application menu bar, but this may be expanded in the future.
     */
    private static void initialiseMenuBar(final Emulator emulator) {
        // OS X has it's own application menu bar, it is about to become functional
        if (OperatingSystem.getPlatform() == OperatingSystem.OSX) {
            Log.info("OS X detected, making %s use the OS X menu bar", AppInfo.NAME);
            // shows JMenuBar as the OS X application menu instead of in the JFrame
            System.setProperty("apple.laf.useScreenMenuBar", "true");

            // setup the about menu item
            Emulator.APP.setAboutHandler(new AboutHandler() {
                @Override
                public void handleAbout(AppEvent.AboutEvent aboutEvent) {
                    DialogUtils.showAboutDialog(emulator.getWindow());
                }
            });

            // setup the quit menu item so it confirms before quitting
            Emulator.APP.setQuitHandler(new QuitHandler() {
                @Override
                public void handleQuitRequestWith(AppEvent.QuitEvent quitEvent, QuitResponse quitResponse) {
                    // if the user selected the ok option, we must shut everything down
                    if (DialogUtils.showQuitDialog(emulator.getWindow()) == JOptionPane.OK_OPTION) {
                        emulator.shutdown();
                        quitResponse.performQuit();
                    }
                    // stops the application quitting
                    quitResponse.cancelQuit();
                }
            });
        }
    }

    private static void initialiseLWJGL(final Emulator emulator) {
        try {
            Log.info("Initialising LWJGL Version: " + Sys.getVersion());

            // setup the display
            Display.setParent(emulator.getCanvas());
            Display.setTitle(AppInfo.NAME);
            Display.create();

            glEnable(GL_TEXTURE_2D); // enable textures
            Util.checkGLError();
            glDisable(GL_DEPTH_TEST); // 2D textures don't have depth

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            Util.checkGLError();

            setupDisplay(emulator);

            // setup the input
            Mouse.create();
            Keyboard.create();
            Keyboard.enableRepeatEvents(true);
        } catch (LWJGLException e) {
            Log.fatal("Failed to initialise LWJGL");
            e.printStackTrace();
            emulator.shutdown();
        }
    }

    public static void setupDisplay(final Emulator emulator) {
        if (!Display.isCreated()) {
            Log.info("Attempting to setup the display without display created");
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0, emulator.getWidth(), emulator.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        glClearColor(0.372549F, 0.388235F, 0.3647059F, 1.0F);
        glViewport(0, 0, emulator.getWidth(), emulator.getHeight());
    }
}
