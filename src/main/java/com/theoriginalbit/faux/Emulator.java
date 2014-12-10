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
import com.theoriginalbit.faux.api.computercraft.IDevice;
import com.theoriginalbit.faux.log.Log;
import com.theoriginalbit.faux.manager.*;
import com.theoriginalbit.faux.util.OperatingSystem;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

/**
 * @author theoriginalbit
 */
public final class Emulator implements IEmulatorInstance {
    public static final Application APP = Application.getApplication();
    public static final File DATASTORE = OperatingSystem.getDataStore();
    private static final int MAX_FPS = 60;

    private final JFrame window;
    private final Canvas contentCanvas;
    private final RenderManager renderManager;
    private final DeviceManager deviceManager;
    private final InputManager inputManager;
    private final WindowManager windowManager;
    private final TickManager tickManager;

    private final Thread tickThread;

    private boolean running;
    private boolean canvasSizeChanged;

    public Emulator(JFrame frame) {
        window = frame;
        contentCanvas = new Canvas();

        renderManager = new RenderManager();
        deviceManager = new DeviceManager();
        inputManager = new InputManager(this);
        windowManager = new WindowManager();
        tickManager = new TickManager(this);

        tickThread = new Thread(tickManager, "Pseudo-World Tick");
        tickThread.start();

        running = true;
        Bootstrap.initialise(this);
        contentCanvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvasSizeChanged = true;
            }
        });
        Thread.currentThread().setName(AppInfo.NAME + " Main");
    }

    /* USED IN THE DEVELOPMENT ENVIRONMENT */
    public static void main(String[] args) {
        new Emulator(new JFrame()).run();
    }

    public JFrame getWindow() {
        return window;
    }

    public Canvas getCanvas() {
        return contentCanvas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IManager<IRendered> getRenderManager() {
        return renderManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IManager<IDevice> getDeviceManager() {
        return deviceManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IManager<IInputConsumer> getInputManager() {
        return inputManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IManager<IWindow> getWindowManager() {
        return windowManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IManager<ITicking> getTickManager() {
        return tickManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return contentCanvas.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return contentCanvas.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return running && (Display.isCreated() && !Display.isCloseRequested());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSystemTime() {
        return tickManager.getSystemTime();
    }

    public final void run() {
        Log.info(AppInfo.NAME + " started!");
        try {
            while (isRunning()) {
                // if the canvas was re-sized, inform OpenGL/LWJGL
                if (canvasSizeChanged) {
                    Log.debug("Canvas has been re-sized to W: %d H: %d", contentCanvas.getWidth(), contentCanvas.getHeight());
                    Bootstrap.setupDisplay(this);
                    canvasSizeChanged = false;
                }

                // clear the display
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                // render any windows
                windowManager.manage();
                // render everything else
                renderManager.manage();

                // update the display
                Display.update();

                // process input
                inputManager.manage();

                Display.sync(MAX_FPS);
            }
        } catch (Throwable t) {
            Log.fatal("Exception in graphics/input loop", t);
            t.printStackTrace();
        } finally {
            if (isRunning()) {
                shutdown();
            }
        }
    }

    public final void shutdown() {
        Log.info("Shutting down %s...", AppInfo.NAME);
        running = false;
        if (Display.isCreated()) {
            Display.destroy();
        }
        window.dispose();
        deviceManager.stopDevices();
        stopThread(tickThread);
    }

    private void stopThread(final Thread thread) {
        if (thread.isAlive()) {
            try {
                Log.info("Gracefully stopping the %s thread", thread.getName());
                thread.join(250L);
            } catch (InterruptedException e) {
                if (thread.isAlive()) {
                    Log.warn("Failed to stop %s thread gracefully - forcing a stop!", thread.getName());
                    thread.interrupt();
                }
            }
        }
    }

}
