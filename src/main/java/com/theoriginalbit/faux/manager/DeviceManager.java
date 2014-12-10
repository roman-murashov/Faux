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
package com.theoriginalbit.faux.manager;

import com.theoriginalbit.faux.api.computercraft.IDevice;
import com.theoriginalbit.faux.log.Log;

/**
 * @author theoriginalbit
 */
public class DeviceManager extends Manager<IDevice> {
    @Override
    public void manage() {
        // NO-OP
    }

    @Override
    public void invalidate(IDevice item) {
        // NO-OP
    }

    public void stopDevices() {
        for (IDevice device : items) {
            Log.debug("Stopping device " + device.getID());
            device.stop();
        }
    }
}
