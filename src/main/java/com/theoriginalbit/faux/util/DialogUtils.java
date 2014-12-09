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
package com.theoriginalbit.faux.util;

import com.theoriginalbit.faux.AppInfo;

import javax.swing.*;

/**
 * @author theoriginalbit
 */
public final class DialogUtils {
    private static final Object[] quitOptions = {"Exit", "Cancel"};

    public static void showAboutDialog(JFrame parent) {
        JOptionPane.showMessageDialog(parent,
                AppInfo.NAME + " v" + AppInfo.VERSION + " by Joshua Asbury (@theoriginalbit)\n" +
                        "\n" +
                        "ComputerCraft by dan200 visit www.computercraft.info\n" +
                        "",
                null,
                JOptionPane.PLAIN_MESSAGE);
    }

    public static int showQuitDialog(JFrame parent) {
        return JOptionPane.showOptionDialog(parent,
                "Are you sure you want to quit " + AppInfo.NAME + "?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                quitOptions,
                quitOptions[0]);
    }
}
