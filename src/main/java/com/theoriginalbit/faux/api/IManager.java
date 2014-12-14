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
 * A common interface that defines the functionality of a generic manager within the Faux context.
 * A manager is what contains the instances of the objects within the running context, as opposed to
 * an {@link com.theoriginalbit.faux.api.IRegistry} which contains references of possible instances.
 *
 * @author theoriginalbit
 */
public interface IManager<E> extends IRegistry<E> {
    /**
     * Invoked when the manager should process everything it manages
     */
    public void manage();

    /**
     * Performs immediate processing of the supplied item; this can have different usages for the
     * various managers.
     *
     * For example the {@link com.theoriginalbit.faux.api.IRendered} manager will immediately redraw
     * the element. Whereas the {@link com.theoriginalbit.faux.api.IWindow} manager will bring the
     * supplied window into focus.
     *
     * @param item the item to manage
     */
    public void manage(E item);
}
