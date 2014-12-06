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
package com.theoriginalbit.faux.log;

import com.theoriginalbit.faux.AppInfo;
import com.theoriginalbit.faux.Faux;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;

/**
 * @author theoriginalbit
 */
public final class Log {
    private static final Logger logger = LogManager.getLogger(Faux.class);
    private static final String SEPARATOR = "****************************************";
    private static final String TRACE = "*  at %s%s";
    private static boolean configured = false;

    private static void configure() {
        // inject into System.out and System.err
        System.setOut(new TracingPrintStream(LogManager.getLogger("STDOUT"), System.out));
        System.setErr(new TracingPrintStream(LogManager.getLogger("STDERR"), System.err));
        configured = true;

        Log.info("");
        Log.info("Starting Faux v" + AppInfo.VERSION);
        Log.info("Copyright (c) Joshua Asbury (@theoriginalbit), 2014");
        Log.info("");
    }

    public static void info(String message, Object... args) {
        if (!configured) configure();
        logger.info(String.format(message, args));
    }

    public static void warn(String message, Object... args) {
        if (!configured) configure();
        logger.warn(String.format(message, args));
    }

    public static void error(String message, Object... args) {
        if (!configured) configure();
        logger.error(String.format(message, args));
    }

    public static void fatal(String message, Object... args) {
        if (!configured) configure();
        logger.fatal(SEPARATOR);
        logger.fatal(String.format("* " + message, args));
        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < 8 && i < trace.length; ++i) {
            logger.fatal(String.format(TRACE, trace[i].toString(), i == 7 ? "..." : ""));
        }
        logger.fatal(SEPARATOR);
    }

    public static void debug(String message, Object... args) {
        if (!configured) configure();
        logger.debug(String.format(message, args));
    }

    private static class TracingPrintStream extends PrintStream {
        private final Logger logger;

        public TracingPrintStream(Logger log, PrintStream original) {
            super(original);
            logger = log;
        }

        @Override
        public void println(Object o) {
            logger.info(getPrefix() + o);
        }

        @Override
        public void println(String s) {
            logger.info(getPrefix() + s);
        }

        private String getPrefix() {
            final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            final StackTraceElement element = elements[3]; // The caller is always at depth 2, plus this call.
            return "[" + element.getClassName() + ":" + element.getMethodName() + ":" + element.getLineNumber() + "]: ";
        }

    }
}
