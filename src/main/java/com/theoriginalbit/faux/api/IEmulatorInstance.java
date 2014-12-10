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
package com.theoriginalbit.faux.api;

import com.theoriginalbit.faux.api.computercraft.IDevice;

/**
 * A common interface defining the accessible methods within the emulator instance
 *
 * @author theoriginalbit
 */
public interface IEmulatorInstance {
    /**
     * @return whether the emulator is running
     */
    public boolean isRunning();

    /**
     * @return the height of the drawable area
     */
    public int getWidth();

    /**
     * @return the width of the drawable area
     */
    public int getHeight();

    /**
     * @return the system time in milliseconds
     */
    public long getSystemTime();

    /**
     * @return the input manager for the emulator application
     */
    public IManager<IInputConsumer> getInputManager();

    /**
     * @return the tick manager for the emulator application
     */
    public IManager<ITicking> getTickManager();

    /**
     * @return the render manager for the emulator application
     */
    public IManager<IRendered> getRenderManager();

    /**
     * @return the device manager for the emulator application
     */
    public IManager<IDevice> getDeviceManager();

    /**
     * @return the window manager for the emulator application
     */
    public IManager<IWindow> getWindowManager();
}
