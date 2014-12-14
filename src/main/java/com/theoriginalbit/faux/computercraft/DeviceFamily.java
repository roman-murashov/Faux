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
package com.theoriginalbit.faux.computercraft;

import com.theoriginalbit.faux.ui.Color;

/**
 * @author theoriginalbit
 */
public enum DeviceFamily {
    NORMAL("Normal", new Color(0xc5c5c5), new Color(0xfefefe), new Color(0x5a5a5a)),
    ADVANCED("Advanced", new Color(0xc3c35f), new Color(0xdbda8b), new Color(0x75753d));

    private final String displayName;
    private final Color primaryColor;
    private final Color lightColor;

    private final Color darkColor;

    private DeviceFamily(String name, Color primary, Color light, Color dark) {
        displayName = name;
        primaryColor = primary;
        lightColor = light;
        darkColor = dark;
    }

    /**
     * @return the human readable name of this family
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the primary GUI colour for focused devices
     */
    public Color getPrimaryColor() {
        return primaryColor;
    }

    /**
     * @return the bevel shadow GUI colour for focused devices
     */
    public Color getDarkColor() {
        return darkColor;
    }

    /**
     * @return the bevel light GUI colour for focused devices
     */
    public Color getLightColor() {
        return lightColor;
    }
}
