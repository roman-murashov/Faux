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
package com.theoriginalbit.faux.ui;

import com.theoriginalbit.faux.AppInfo;
import com.theoriginalbit.faux.Faux;
import com.theoriginalbit.faux.util.DialogUtils;
import com.theoriginalbit.faux.util.OperatingSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author theoriginalbit
 */
public final class FauxMenuBar extends JMenuBar {
    private final Faux faux;
    private final JFrame parent;

    public FauxMenuBar(JFrame frame, Faux fauxInstance) {
        parent = frame;
        faux = fauxInstance;

        addFileMenu();
        addDebugMenu();
    }

    private void addFileMenu() {
        final JMenu menu = new JMenu("File");
        JMenuItem menuItem;

        // if this OS is not OS X then add the "About" menu item
        if (OperatingSystem.getPlatform() != OperatingSystem.OSX) {
            // setup the about menu item
            menuItem = new JMenuItem("About " + AppInfo.NAME);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DialogUtils.showAboutDialog(parent);
                }
            });
            menu.add(menuItem);
            // add the separator below this item
            menu.addSeparator();
        }

        addDeviceCreationMenuItems(menu);

        if (OperatingSystem.getPlatform() != OperatingSystem.OSX) {
            // add the separator above this item
            menu.addSeparator();
            // setup the quit menu item so it confirms before quitting
            menuItem = new JMenuItem("Quit " + AppInfo.NAME);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (DialogUtils.showQuitDialog(parent) == JOptionPane.OK_OPTION) faux.shutdown();
                }
            });
            menu.add(menuItem);
        }

        add(menu);
    }

    private void addDeviceCreationMenuItems(final JMenu parent) {
        JMenuItem menuItem;

        menuItem = new JMenuItem("Create... (coming soon)");
        menuItem.setEnabled(false);
        parent.add(menuItem);
    }

    private void addDebugMenu() {
        final JMenu menu = new JMenu("Debug");
        JMenuItem menuItem;

        menuItem = new JMenuItem("No items");
        menuItem.setEnabled(false);
        menu.add(menuItem);

        add(menu);
    }
}
