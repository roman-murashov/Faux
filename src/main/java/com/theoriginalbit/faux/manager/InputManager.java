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

import com.theoriginalbit.faux.api.IEmulatorInstance;
import com.theoriginalbit.faux.api.IInputConsumer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @author theoriginalbit
 */
public class InputManager extends Manager<IInputConsumer> {
    private final IEmulatorInstance emulator;
    private int mouseLastX = 0, mouseLastY = 0, eventButton = -1;

    public InputManager(final IEmulatorInstance emulatorInstance) {
        emulator = emulatorInstance;
    }

    @Override
    public void manage() {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                handleMouseInput();
            }
        }
        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                handleKeyboardInput();
            }
        }
    }

    @Override
    public void invalidate(IInputConsumer item) {
        // NO-OP
    }

    private void handleMouseInput() {
        final int x = Mouse.getEventX();
        final int y = emulator.getHeight() - Mouse.getEventY() - 1;
        final int b = Mouse.getEventButton();
        final int w = Mouse.getEventDWheel();

        if (eventButton != b && b != -1) {
            eventButton = b;
            for (IInputConsumer consumer : items) {
                consumer.onMouseClick(x, y, eventButton);
            }
        }

        if (!Mouse.isButtonDown(eventButton)) {
            eventButton = -1;
            for (IInputConsumer consumer : items) {
                consumer.onMouseRelease(x, y, eventButton);
            }
        }

        if (mouseLastX != x || mouseLastY != y) {
            mouseLastX = x;
            mouseLastY = y;
            for (IInputConsumer consumer : items) {
                consumer.onMouseMoved(x, y);
            }
        }

        if (w != 0) {
            for (IInputConsumer consumer : items) {
                consumer.onMouseScrolled(x, y, w);
            }
        }
    }

    private void handleKeyboardInput() {
        if (Keyboard.getEventKeyState() || Keyboard.isRepeatEvent()) {
            for (IInputConsumer consumer : items) {
                consumer.onKeyPressed(Keyboard.getEventKey(), Keyboard.getEventCharacter());
            }
        }
    }
}
