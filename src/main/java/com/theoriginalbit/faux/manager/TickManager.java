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
import com.theoriginalbit.faux.api.IManager;
import com.theoriginalbit.faux.api.ITicking;
import com.theoriginalbit.faux.log.Log;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author theoriginalbit
 */
public class TickManager implements Runnable, IManager<ITicking> {
    private static final long MAX_SLEEP = 50L;
    private static final float SLOW_TICK = 80.0F;
    private final HashMap<ITicking, Boolean> tickers = new HashMap<ITicking, Boolean>();
    private final IEmulatorInstance emulator;
    private float lastTick = 0.0F;

    public TickManager(final IEmulatorInstance emulatorInstance) {
        emulator = emulatorInstance;
    }

    @Override
    public void manage() {
        // NO-OP
    }

    @Override
    public void invalidate(ITicking item) {
        synchronized (tickers) {
            if (!tickers.containsKey(item)) {
                Log.warn("Trying to invalidate an ITicking which was not registered");
                return;
            }
            tickers.put(item, true);
        }
    }

    @Override
    public boolean register(ITicking item) {
        synchronized (tickers) {
            if (tickers.containsKey(item)) {
                Log.warn("Trying to register an ITicking which is already registered");
                return false;
            }
            return tickers.put(item, false);
        }
    }

    @Override
    public boolean unregister(ITicking item) {
        synchronized (tickers) {
            if (!tickers.containsKey(item)) {
                Log.warn("Trying to unregister an ITicking which was not registered");
                return false;
            }
            return tickers.remove(item);
        }
    }

    @Override
    public ArrayList<ITicking> getRegisteredItems() {
        synchronized (tickers) {
            return new ArrayList<ITicking>(tickers.keySet());
        }
    }

    public long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Override
    public void run() {
        while (emulator.isRunning()) {
            try {
                long tickStart = getSystemTime();

                synchronized (tickers) {
                    for (ITicking ticking : tickers.keySet()) {
                        ticking.onTick();
                        // it is no longer dirty, so set the dirty flag in case
                        tickers.put(ticking, false);
                    }
                }

                long tickEnd = getSystemTime();
                long tickTime = tickEnd - tickStart;

                if ((tickStart - lastTick) > SLOW_TICK) {
                    Log.warn(String.format("Cannot keep up [cycle-time: %fms, proc-time: %fms]", (tickEnd - lastTick), (float) tickTime));
                }

                lastTick = tickEnd;

                if (MAX_SLEEP > tickTime) {
                    Thread.sleep(MAX_SLEEP - tickTime);
                }
            } catch (InterruptedException ignored) {
                Log.info("Tick manager was interrupted");
            } catch (Exception e) {
                Log.info("Tick manager has thrown an exception: ", e);
            }
        }
    }
}
