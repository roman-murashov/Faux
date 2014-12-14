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
package com.theoriginalbit.faux.computercraft.device;

import com.theoriginalbit.faux.api.IEmulatorInstance;
import com.theoriginalbit.faux.computercraft.DeviceFamily;
import com.theoriginalbit.faux.computercraft.DeviceType;
import com.theoriginalbit.faux.computercraft.DisplayDevice;

/**
 * @author theoriginalbit
 */
public class Pocket extends DisplayDevice {
    /**
     * Constructs a ComputerCraft Pocket Computer
     *
     * @param family   the device family for render colour
     * @param instance the emulator instance
     */
    public Pocket(DeviceFamily family, IEmulatorInstance instance) {
        super(DeviceType.POCKET, family, instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return 0;
    }
}
