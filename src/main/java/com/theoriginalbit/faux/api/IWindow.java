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
 * A common interface to define the standard functionality of a Window element within the GUI context
 *
 * @author theoriginalbit
 * @see com.theoriginalbit.faux.api.ITicking
 * @see com.theoriginalbit.faux.api.IRendered
 * @see com.theoriginalbit.faux.api.IInputConsumer
 */
public interface IWindow extends IRendered, IInputConsumer, ITicking {
    /**
     * Invoked when the Window has changed from a hidden to a shown state
     */
    public void onShow();

    /**
     * Invoked when the Window has changed from a shown to a hidden state
     */
    public void onHide();

    /**
     * Invoked when the Window is created
     */
    public void onCreate();

    /**
     * Invoked when the Window is destroyed
     */
    public void onDestroy();

    /**
     * Invoked when the Window is resized
     *
     * @param width  the new width
     * @param height the new height
     */
    public void onResize(int width, int height);
}
