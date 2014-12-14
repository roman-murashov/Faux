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

import com.theoriginalbit.faux.api.IWindow;

/**
 * @author theoriginalbit
 */
public class WindowManager extends Manager<IWindow> {
    public WindowManager() {
        super("Window");
    }

    @Override
    public boolean register(IWindow window) {
        if (super.register(window)) {
            manage(window); // focus the window
            return true;
        }
        return false;
    }

    @Override
    public void manage() {
        // TODO: make this respect z-index and focused window
        for (IWindow window : items) {
            window.draw(null);
        }
    }

    @Override
    public void manage(IWindow item) {
    }
}
