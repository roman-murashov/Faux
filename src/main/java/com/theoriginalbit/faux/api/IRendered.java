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
 * A interface to define the common functionality of a rendered object within the Faux GUI context
 *
 * @author theoriginalbit
 */
public interface IRendered {
    /**
     * @return whether the element has focus
     */
    public boolean hasFocus();

    /**
     * Sets the focus of the object
     *
     * @param focus the new focus
     */
    public void setFocus(boolean focus);

    /**
     * @return whether the element is visible
     */
    public boolean isVisible();

    /**
     * Sets the visibility of the object
     *
     * @param visibility the new visibility
     */
    public void setVisible(boolean visibility);

    /**
     * Invoked when the object should be rendered
     *
     * @param manager the render manager
     */
    public void draw(IManager<IRendered> manager);
}
