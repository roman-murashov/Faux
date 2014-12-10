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

import javax.swing.*;
import java.awt.*;

/**
 * @author theoriginalbit
 */
public final class Bootstrap {
    public static void initialise(final Emulator emulator) {
        Log.info("%s starting...", AppInfo.NAME);
        setLookAndFeel();
        initialiseMenuBar(emulator);
        initialiseWindow(emulator);
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

        // add the menu bar
        window.setJMenuBar(new EmulatorMenuBar(window, emulator));

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
            Emulator.app.setAboutHandler(new AboutHandler() {
                @Override
                public void handleAbout(AppEvent.AboutEvent aboutEvent) {
                    DialogUtils.showAboutDialog(emulator.getWindow());
                }
            });

            // setup the quit menu item so it confirms before quitting
            Emulator.app.setQuitHandler(new QuitHandler() {
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
}
