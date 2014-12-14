/**
 * Copyright 2014 Joshua Asbury (@theoriginalbit)
 *
 * Licensed under the Apache License; Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing; software
 * distributed under the License is distributed on an "AS IS" BASIS;
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND; either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.theoriginalbit.faux.ui;

/**
 * @author theoriginalbit
 */
public class Color {
    /*
       The ComputerCraft text and background colours as found here:
       http://computercraft.info/wiki/Colors_(API)#Colors
     */
    public static final Color WHITE = new Color(0xF0F0F0);
    public static final Color ORANGE = new Color(0xF2B233);
    public static final Color MAGENTA = new Color(0xE57FD8);
    public static final Color L_BLUE = new Color(0x99B2F2);
    public static final Color YELLOW = new Color(0xDEDE6C);
    public static final Color LIME = new Color(0x7FCC19);
    public static final Color PINK = new Color(0xF2B2CC);
    public static final Color GRAY = new Color(0x4C4C4C);
    public static final Color L_GRAY = new Color(0x999999);
    public static final Color CYAN = new Color(0x4C99B2);
    public static final Color PURPLE = new Color(0xB266E5);
    public static final Color BLUE = new Color(0x3366CC);
    public static final Color BROWN = new Color(0x7F664C);
    public static final Color GREEN = new Color(0x57A64E);
    public static final Color RED = new Color(0xCC4C4C);
    public static final Color BLACK = new Color(0x191919);
    public static final Color D_BLACK = new Color(0x000000);

    private static final float MAX_VAL = 256.0f;
    private final float red, green, blue;

    public Color(int hex) {
        red = ((hex & 0xFF0000) >> 16) / MAX_VAL;
        green = ((hex & 0x00FF00) >> 8) / MAX_VAL;
        blue = (hex & 0x0000FF) / MAX_VAL;
    }

    /**
     * @return the red component of the colour
     */
    public float getRed() {
        return red;
    }

    /**
     * @return the green component of the colour
     */
    public float getGreen() {
        return green;
    }

    /**
     * @return the blue component of the colour
     */
    public float getBlue() {
        return blue;
    }
}
