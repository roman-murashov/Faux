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
package com.theoriginalbit.faux.ui;

import com.theoriginalbit.faux.api.IEmulatorInstance;
import com.theoriginalbit.faux.api.IWindow;
import com.theoriginalbit.faux.log.Log;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * A window is the main rendered content element of the emulator. This element is a movable element
 * which captures user input, as well as ticking each game loop in order to update. It is rendered
 * before any {@link com.theoriginalbit.faux.api.IRendered} elements, and the rendering respects
 * z-index and focused windows, as opposed to IRendered elements.
 *
 * @author theoriginalbit
 * @see com.theoriginalbit.faux.api.IWindow
 * @see com.theoriginalbit.faux.api.IRendered
 */
public abstract class Window implements IWindow {
    protected final IEmulatorInstance emulator;
    protected int xPos, yPos;
    protected int width, height;
    private boolean focus = true, visible = true;
    private boolean lastClickInBounds;
    private int lastClickX = -1, lastClickY = -1;

    /**
     * Constructs a window which can be rendered to the screen
     *
     * @param x        the x position on the screen
     * @param y        the y position on the screen
     * @param w        the full window width
     * @param h        the full window height
     * @param instance the emulator instance
     */
    public Window(int x, int y, int w, int h, IEmulatorInstance instance) {
        xPos = x;
        yPos = y;
        width = w;
        height = h;
        emulator = instance;
    }

    /**
     * @return whether this window can be re-sized by the user
     */
    public boolean isResizable() {
        return false;
    }

    /**
     * Whether the user clicked in an area that allows them to move the window while dragging
     *
     * @param x the x position of the click
     * @param y the y position of the click
     * @return whether the window should move when dragged
     */
    public boolean wasClickInMoveBounds(int x, int y) {
        return true;
    }

    /**
     * Whether the user clicked in the main content area of this window
     *
     * @param x the x position of the click
     * @param y the y position of the click
     * @return whether the click was on the content
     */
    public boolean wasClickInContentBounds(int x, int y) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFocus() {
        return focus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus(boolean f) {
        focus = f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean visibility) {
        visible = visibility;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        Log.debug("Creating window @ %d, %d", xPos, yPos);
        emulator.getTickManager().register(this);
        emulator.getInputManager().register(this);
        emulator.getWindowManager().register(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        Log.debug("Destroying window @ %d, %d", xPos, yPos);
        emulator.getTickManager().unregister(this);
        emulator.getInputManager().unregister(this);
        emulator.getWindowManager().unregister(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMouseMoved(int x, int y) {
        if (hasFocus() && lastClickInBounds && Mouse.isButtonDown(0)) {
            xPos = x - lastClickX;
            yPos = y - lastClickY;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMouseClick(int x, int y, int button) {
        if (wasClickInBounds(x, y) && button != -1) {
            if (!hasFocus()) {
                emulator.getWindowManager().manage(this);
            }

            if (hasFocus()) {
                lastClickInBounds = wasClickInMoveBounds(x, y);
                lastClickX = translateX(x);
                lastClickY = translateY(y);
            }
        } else {
            lastClickInBounds = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMouseRelease(int x, int y, int button) {
        // NO-OP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMouseScrolled(int x, int y, int direction) {
        // NO-OP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasClickInBounds(int x, int y) {
        x = translateX(x);
        y = translateY(y);
        return x >= 0 && x <= width && y >= 0 && y <= height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResize(int w, int h) {
        if (isResizable()) {
            width = w;
            height = h;
        } else {
            Log.warn("Attempted the resize statically sized window");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShow() {
        // NO-OP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHide() {
        // NO-OP
    }

    /**
     * Sets the render colour of the OpenGL context
     *
     * @param color the colour to set
     */
    protected void setColor(Color color) {
        GL11.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Renders a quad of the specified dimensions to the specified location on the screen
     *
     * @param x the x position
     * @param y the y position
     * @param w the width of the quad
     * @param h the height of the quad
     */
    protected void drawQuad(int x, int y, int w, int h) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();
    }

    /**
     * Turns the supplied x value into a x value relative to the window
     *
     * @param x the raw x value (normally click location)
     * @return the relative x value
     */
    protected int translateX(int x) {
        return x - xPos;
    }

    /**
     * Turns the supplied y value into a y value relative to the window
     *
     * @param y the raw y value (normally click location)
     * @return the relative y value
     */
    protected int translateY(int y) {
        return y - yPos;
    }
}
