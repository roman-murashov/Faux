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

import com.theoriginalbit.faux.api.IManager;
import com.theoriginalbit.faux.log.Log;

import java.util.ArrayList;

/**
 * @author theoriginalbit
 */
public abstract class Manager<E> implements IManager<E> {
    protected final ArrayList<E> items = new ArrayList<E>();
    private final String name;

    protected Manager(String managerName) {
        name = managerName;
    }

    @Override
    public boolean register(E item) {
        if (items.contains(item)) {
            Log.warn("Attempting to register %s with the %s manager when it is already registered", item, name);
            return false;
        }
        return items.add(item);
    }

    @Override
    public boolean unregister(E item) {
        if (!items.contains(item)) {
            Log.warn("Attempting to un-register %s with the %s manager when it was not registered", item, name);
            return false;
        }
        return items.remove(item);
    }

    @Override
    public ArrayList<E> getRegisteredItems() {
        return new ArrayList<E>(items);
    }
}
