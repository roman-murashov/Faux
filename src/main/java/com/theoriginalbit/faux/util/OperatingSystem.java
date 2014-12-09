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

import java.io.File;

/**
 * @author theoriginalbit
 */
public enum OperatingSystem {
    LINUX,
    SOLARIS,
    WINDOWS,
    OSX,
    UNKNOWN;

    private static final String APP_FOLDER = AppInfo.NAME.toLowerCase() + "/";

    public static OperatingSystem getPlatform() {
        final String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("win")) return WINDOWS;
        if (s.contains("mac")) return OSX;
        if (s.contains("solaris")) return SOLARIS;
        if (s.contains("sunos")) return SOLARIS;
        if (s.contains("linux")) return LINUX;
        if (s.contains("unix")) return LINUX;
        return UNKNOWN;
    }

    public static File getDataStore() {
        final String userDefined = System.getProperty("com.theoriginalbit." + AppInfo.NAME + ".path");
        File appDir;
        if (userDefined != null && !userDefined.isEmpty()) {
            appDir = new File(userDefined);
        } else {
            final String userHome = System.getProperty("user.home", ".");
            switch (getPlatform()) {
                case WINDOWS:
                    final String appData = System.getenv("APPDATA");
                    appDir = new File(appData != null ? appData : userHome, "." + APP_FOLDER);
                    break;
                case OSX:
                    appDir = new File(userHome, "Library/Application Support/" + APP_FOLDER);
                    break;
                case LINUX:
                    appDir = new File(userHome, "." + APP_FOLDER);
                    break;
                case SOLARIS:
                    appDir = new File(userHome, "." + APP_FOLDER);
                    break;
                default: // all others
                    appDir = new File(userHome, APP_FOLDER);
            }
        }

        if (!appDir.exists() && !appDir.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + appDir);
        }

        return appDir;
    }
}
