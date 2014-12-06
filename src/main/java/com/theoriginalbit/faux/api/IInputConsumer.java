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

/**
 * A common interface for all objects that wish to be able to consume user input generated within the Faux context.
 * The input is raw from LWJGL and has not been transformed or processed prior.
 *
 * @author theoriginalbit
 */
public interface IInputConsumer {
    /**
     * Invoked when there has been a key pressed
     *
     * @param key the key code that was pressed
     * @param ch  the char representation of the key
     */
    public void onKeyPressed(int key, char ch);

    /**
     * Invoked when the mouse is moved
     *
     * @param x the new x position
     * @param y the new y position
     */
    public void onMouseMoved(int x, int y);

    /**
     * Invoked when the mouse is clicked
     *
     * @param x      the x position of the click
     * @param y      the y position of the click
     * @param button the button that was clicked
     */
    public void onMouseClick(int x, int y, int button);

    /**
     * Invoked when the mouse has been released
     *
     * @param x      the x position when released
     * @param y      the y position when released
     * @param button the button that was released
     */
    public void onMouseRelease(int x, int y, int button);

    /**
     * Invoked when the mouse wheel has been scrolled
     *
     * @param x         the x position it was scrolled
     * @param y         the y position it was scrolled
     * @param direction the direction it was scrolled
     */
    public void onMouseScrolled(int x, int y, int direction);

    /**
     * Returns whether the mouse was clicked within the bounds of this element
     *
     * @param x the x position clicked
     * @param y the y position clicked
     * @return whether it was in the bounds
     */
    public boolean wasClickInBounds(int x, int y);
}
