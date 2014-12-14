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

import com.theoriginalbit.faux.api.IEmulatorInstance;
import com.theoriginalbit.faux.api.IManager;
import com.theoriginalbit.faux.api.IRendered;
import com.theoriginalbit.faux.api.computercraft.IDevice;
import com.theoriginalbit.faux.manager.InputManager;
import com.theoriginalbit.faux.ui.Color;
import com.theoriginalbit.faux.ui.Window;
import com.theoriginalbit.faux.util.ChatUtils;
import com.theoriginalbit.faux.util.Events;
import com.theoriginalbit.faux.util.GuiUtils;
import org.lwjgl.input.Keyboard;

/**
 * @author theoriginalbit
 */
public abstract class DisplayDevice extends Window implements IDevice {
    /**
     * The width of the border in unscaled display pixels
     */
    private static final int BORDER_WIDTH = 12;
    /**
     * The width of a Computer pixel in unscaled display pixels
     */
    private static final int PIXEL_WIDTH = 6;
    /**
     * The height of a Computer pixel in unscaled display pixels
     */
    private static final int PIXEL_HEIGHT = 9;
    private static final float MAX_TIMER = 20.0f;
    protected final DeviceFamily deviceFamily;
    protected final DeviceType deviceType;
    protected final Terminal terminal;
    private float timerReboot = 0.0F;
    private float timerShutdown = 0.0F;
    private float timerTerminate = 0.0F;

    /**
     * Constructs a ComputerCraft device which has a display
     *
     * @param type     the device type for terminal size
     * @param family   the device family for render colour
     * @param instance the emulator instance
     */
    public DisplayDevice(DeviceType type, DeviceFamily family, IEmulatorInstance instance) {
        super(10, 10, getDeviceWindowWidth(type), getDeviceWindowHeight(type), instance);
        deviceType = type;
        deviceFamily = family;
        terminal = new Terminal(type, family);

        // position this window in the centre of the screen when first created
        xPos = (emulator.getWidth() - width) / 2;
        yPos = (emulator.getHeight() - height) / 2;
    }

    /**
     * Calculates the absolute width of the window for this device, taking into account the
     * content area as well as the borders that surround it. The result of this calculation
     * is then scaled up to the emulator GUI scale.
     *
     * @param type the device type for number of pixels
     * @return the scaled, absolute width of the window
     */
    protected static int getDeviceWindowWidth(final DeviceType type) {
        return GuiUtils.toScaledPixels((BORDER_WIDTH * 2) + (type.getWidth() * PIXEL_WIDTH));
    }

    /**
     * Calculates the absolute height of the window for this device, taking into account the
     * content area as well as the borders that surround it. The result of this calculation
     * is then scaled up to the emulator GUI scale.
     *
     * @param type the device type for number of pixels
     * @return the scaled, absolute width of the window
     */
    protected static int getDeviceWindowHeight(final DeviceType type) {
        return GuiUtils.toScaledPixels((BORDER_WIDTH * 2) + (type.getHeight() * PIXEL_HEIGHT));
    }

    /**
     * @return the type of this device, e.g. Pocket Computer
     */
    public DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @return the family of this device, e.g. Advanced
     */
    public DeviceFamily getDeviceFamily() {
        return deviceFamily;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        emulator.getDeviceManager().register(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        emulator.getDeviceManager().unregister(this);
        super.onDestroy();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void stop() {
        // NO-OP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onKeyPressed(int key, char ch) {
        // if the key was escape we don't do anything
        if (key == 1) {
            return;
        }

        // update timers if needed
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
            timerReboot = Keyboard.isKeyDown(Keyboard.KEY_R) ? timerReboot + 1.0f : 0.0f;
            timerShutdown = Keyboard.isKeyDown(Keyboard.KEY_S) ? timerShutdown + 1.0f : 0.0f;
            timerTerminate = Keyboard.isKeyDown(Keyboard.KEY_T) ? timerTerminate + 1.0f : 0.0f;
        }

        // it was a paste key combo
        if (ch == '\026') {
            String clipboard = InputManager.getClipboardContents();
            if (clipboard != null) {
                int newLineIndex1 = clipboard.indexOf("\r");
                int newLineIndex2 = clipboard.indexOf("\n");
                if ((newLineIndex1 >= 0) && (newLineIndex2 >= 0)) {
                    clipboard = clipboard.substring(0, Math.max(newLineIndex1, newLineIndex2));
                } else if (newLineIndex1 >= 0) {
                    clipboard = clipboard.substring(0, newLineIndex1);
                } else if (newLineIndex2 >= 0) {
                    clipboard = clipboard.substring(0, newLineIndex2);
                }

                clipboard = ChatUtils.filerAllowedCharacters(clipboard);

                if (!clipboard.isEmpty()) {
                    if (clipboard.length() > 128) {
                        clipboard = clipboard.substring(0, 128);
                    }

                    queueEvent(Events.PASTE, clipboard);
                }
            }
        }

        // queue the key and possible char event
        queueEvent(Events.KEY, key);
        if (ChatUtils.isAllowedCharacter(ch) && ch < '\256') {
            queueEvent(Events.CHAR, Character.toString(ch));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMouseMoved(int x, int y) {
        // if the click is in the window
        if (hasFocus() && wasClickInContentBounds(x, y)) {
            // TODO: queue event with ComputerCraft pixel coordinates
        } else {
            super.onMouseMoved(x, y);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(String event, Object... args) {
        // TODO: implement
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(IManager<IRendered> manager) {
        /*
           common calculations though-out the render, done once to save calculations during render
           ideally this would not be calculated during render, but there is no better place to do
           so for now
         */
        final int SP_1 = GuiUtils.toScaledPixels(1);
        final int SP_2 = GuiUtils.toScaledPixels(2);
        final int SP_3 = GuiUtils.toScaledPixels(3);
        final int SP_4 = GuiUtils.toScaledPixels(4);
        final int SP_5 = GuiUtils.toScaledPixels(5);
        final int SP_7 = GuiUtils.toScaledPixels(7);
        final int SP_8 = GuiUtils.toScaledPixels(8);
        final int SP_10 = GuiUtils.toScaledPixels(10);
        final int SP_11 = GuiUtils.toScaledPixels(11);
        final int SP_20 = GuiUtils.toScaledPixels(20);
        final int SP_21 = GuiUtils.toScaledPixels(21);
        final int SP_22 = GuiUtils.toScaledPixels(22);
        final int SP_BDR = GuiUtils.toScaledPixels(BORDER_WIDTH);

        /* ****************
         *  SCREEN RENDER *
         **************** */

        // black screen behind pixels
        setColor(Color.D_BLACK);
        drawQuad(xPos + SP_BDR, yPos + SP_BDR, GuiUtils.toScaledPixels(terminal.getWidth() * PIXEL_WIDTH), GuiUtils.toScaledPixels(terminal.getHeight() * PIXEL_HEIGHT));

        // TODO: render terminal

        /* ********************************************************************************
         *                               BORDER RENDER                                    *
         *                                                                                *
         *  To save every millisecond possible during rendering, switching colours with   *
         *  OpenGL often is avoided and quads are rendered in colour groups,              *
         ******************************************************************************** */

        /* **************
         * BLACK BORDER *
         ************** */
        setColor(Color.BLACK);
        // render the inner black border
        drawQuad(xPos + SP_11, yPos + SP_11, width - SP_22, SP_1); // top
        drawQuad(xPos + SP_11, yPos + SP_11, SP_1, height - SP_22); // left
        drawQuad(xPos + SP_11, yPos + height - SP_BDR, width - SP_22, SP_1); // bottom
        drawQuad(xPos + width - SP_BDR, yPos + SP_11, SP_1, height - SP_22); // right
        // render the outer black border
        drawQuad(xPos + SP_2, yPos, width - SP_5, SP_1); // top
        drawQuad(xPos, yPos + SP_2, SP_1, height - SP_5); // left
        drawQuad(xPos + SP_3, yPos + height - SP_1, width - SP_5, SP_1); // bottom
        drawQuad(xPos + width - SP_1, yPos + SP_3, SP_1, height - SP_5); // right
        // black borders to form corners
        drawQuad(xPos + SP_1, yPos + SP_1, SP_1, SP_1); // top-left
        drawQuad(xPos + width - SP_3, yPos + SP_1, SP_1, SP_1); // top-right
        drawQuad(xPos + width - SP_2, yPos + SP_2, SP_1, SP_1); // top-right
        drawQuad(xPos + width - SP_2, yPos + height - SP_2, SP_1, SP_1); // bottom-right
        drawQuad(xPos + SP_1, yPos + height - SP_3, SP_1, SP_1); // bottom-left
        drawQuad(xPos + SP_2, yPos + height - SP_2, SP_1, SP_1); // bottom-left

        /* **************
         * SHADOW BEVEL *
         ************** */
        setColor(deviceFamily.getDarkColor());
        drawQuad(xPos + SP_10, yPos + SP_10, width - SP_20, SP_1); // top
        drawQuad(xPos + SP_10, yPos + SP_11, SP_1, height - SP_21); // left
        drawQuad(xPos + SP_3, yPos + height - SP_3, width - SP_5, SP_2); // bottom
        drawQuad(xPos + width - SP_3, yPos + SP_3, SP_2, height - SP_5); // right
        drawQuad(xPos + width - SP_4, yPos + height - SP_4, SP_1, SP_1); // bottom-right corner

        /* *************
         * LIGHT BEVEL *
         ************* */
        setColor(deviceFamily.getLightColor());
        drawQuad(xPos + SP_11, yPos + height - SP_11, width - SP_21, SP_1); // bottom
        drawQuad(xPos + width - SP_11, yPos + SP_11, SP_1, height - SP_22); // right
        drawQuad(xPos + SP_2, yPos + SP_1, width - SP_5, SP_2); // top
        drawQuad(xPos + SP_1, yPos + SP_2, SP_2, height - SP_5); // left
        drawQuad(xPos + SP_3, yPos + SP_3, SP_1, SP_1); // top-left corner
        drawQuad(xPos + width - SP_3, yPos + SP_2, SP_1, SP_1); // top-right corner

        /* *************
         * MAIN BORDER *
         ************* */
        setColor(deviceFamily.getPrimaryColor());
        drawQuad(xPos + SP_4, yPos + SP_3, width - SP_8, SP_7); // top
        drawQuad(xPos + SP_3, yPos + SP_4, SP_7, height - SP_8); // left
        drawQuad(xPos + SP_3, yPos + height - SP_10, width - SP_7, SP_7); // bottom
        drawQuad(xPos + width - SP_10, yPos + SP_3, SP_7, height - SP_7); // right
        drawQuad(xPos + SP_2, yPos + height - SP_3, SP_1, SP_1); // bottom-left corner
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTick() {
        if (!hasFocus()) {
            return;
        }

        if (timerTerminate >= MAX_TIMER) {
            // TODO: terminate computer
            timerTerminate = 0.0f;
        }
        if (timerReboot >= MAX_TIMER) {
            // TODO: reboot computer
            timerReboot = 0.0f;
        }
        if (timerShutdown >= MAX_TIMER) {
            // TODO: shutdown computer
            timerShutdown = 0.0f;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasClickInMoveBounds(int x, int y) {
        return wasClickInBounds(x, y) && !wasClickInContentBounds(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasClickInContentBounds(int x, int y) {
        int spb = GuiUtils.toScaledPixels(BORDER_WIDTH);
        x = translateX(x);
        y = translateY(y);

        return x > spb && x < GuiUtils.toScaledPixels(terminal.getWidth() * PIXEL_WIDTH) &&
                y > spb && y < GuiUtils.toScaledPixels(terminal.getHeight() * PIXEL_HEIGHT);
    }
}
