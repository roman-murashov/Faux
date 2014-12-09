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
package com.theoriginalbit.faux;

import com.apple.eawt.Application;
import com.theoriginalbit.faux.api.*;
import com.theoriginalbit.faux.util.OperatingSystem;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author theoriginalbit
 */
public final class Faux implements IFauxInstance {
    public static final Application app = Application.getApplication();
    private final File dataStore;
    private final JFrame window;
    private final Canvas contentCanvas;

    public Faux(JFrame frame) {
        dataStore = OperatingSystem.getDataStore();
        window = frame;
        contentCanvas = new Canvas();

        Bootstrap.initialise(this);
    }

    /* USED IN THE DEVELOPMENT ENVIRONMENT */
    public static void main(String[] args) {
        new Faux(new JFrame());
    }

    public final void shutdown() {
        window.dispose();
    }

    public JFrame getWindow() {
        return window;
    }

    public Canvas getCanvas() {
        return contentCanvas;
    }

    @Override
    public IManager<IInputConsumer> getInputManager() {
        return null;
    }

    @Override
    public IManager<ITicking> getTickManager() {
        return null;
    }

    @Override
    public IManager<IRendered> getRenderManager() {
        return null;
    }
}
