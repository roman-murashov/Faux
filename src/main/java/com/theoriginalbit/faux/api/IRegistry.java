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

import java.util.ArrayList;

/**
 * A common interface that defines the functionality of a generic registry within the Faux context.
 *
 * @author theoriginalbit
 */
public interface IRegistry<E> {
    /**
     * Invoked when an item is to be registered
     *
     * @param item the item to register
     * @return success of registry
     */
    public boolean register(E item);

    /**
     * Invoked when an item is to be unregistered
     *
     * @param item the item to unregister
     * @return success of unregistering
     */
    public boolean unregister(E item);

    /**
     * @return a list of all the registered items
     */
    public ArrayList<E> getRegisteredItems();
}
