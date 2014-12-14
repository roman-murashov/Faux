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
     * The tick manager manages all objects within the emulator which must tick every game loop
     *
     * @return the tick manager for the emulator application
     */
    public IManager<ITicking> getTickManager();

    /**
     * The render manager maintains all rendered elements within the Emulator with the exception
     * of a {@link com.theoriginalbit.faux.api.IWindow} which have their own manager since they
     * must respect a Z-Index while rendering.
     * <p/>
     * Invoking the `manage` method on this will render everything to the screen, while invoking
     * the parametrised `manage` method will have no effect.
     * <p/>
     * {@link com.theoriginalbit.faux.api.IRendered} elements are the last to be rendered by the
     * emulator meaning they will always be on top of all windows. They are rendered in the order
     * they're registered to the manager, meaning newly registered elements will be rendered over
     * the top of older registered elements.
     *
     * @return the render manager for the emulator application
     */
    public IManager<IRendered> getRenderManager();

    /**
     * The device manager is simply a place where all created devices are tracked so that
     * when the emulator is shutting down it may instruct all devices to do the same. Invoking
     * either of the `manage` methods on this will have no effect.
     *
     * @return the device manager for the emulator application
     */
    public IManager<IDevice> getDeviceManager();

    /**
     * The window manager ensures that windows are rendered in the correct order, invoking
     * the `manage` method on this manager will render all the devices, while invoking
     * the parametrised `manage` method will cause that window to gain focus.
     * <p/>
     * Objects that are an {@link com.theoriginalbit.faux.api.IWindow} are rendered before
     * any {@link com.theoriginalbit.faux.api.IRendered} objects.
     *
     * @return the window manager for the emulator application
     */
    public IManager<IWindow> getWindowManager();
}
