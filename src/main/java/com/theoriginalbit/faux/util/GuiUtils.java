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

import com.theoriginalbit.faux.Emulator;

/**
 * @author theoriginalbit
 */
public class GuiUtils {
    /**
     * Scales the supplied pixels up to the display pixels for rendering
     *
     * @param pixels pixels to scale
     * @return scaled pixel size
     */
    public static int toScaledPixels(int pixels) {
        return (int) (pixels * Emulator.getEmulatorSettings().getGuiScale());
    }
}
