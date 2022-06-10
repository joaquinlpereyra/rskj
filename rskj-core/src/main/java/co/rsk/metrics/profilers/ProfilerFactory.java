/*
 * This file is part of RskJ
 * Copyright (C) 2019 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.metrics.profilers;

import co.rsk.metrics.profilers.impl.ProxyProfiler;

import javax.annotation.Nullable;

/**
 * ProfilerFactory is used to get the configured Profiler instance.
 * Only one profiler can be defined, once a profiler is set, it cannot be changed.
 * If a profiler isn't configured, the DummyProfiler will be set upon the first request for the instance.
 */
public final class ProfilerFactory {
    
    private static final ProxyProfiler sProxyProfiler = new ProxyProfiler();

    private ProfilerFactory() { /* hidden */ }

    public static void configure(@Nullable Profiler profiler) {
        sProxyProfiler.setProfiler(profiler);
    }

    public static Profiler getInstance() {
        return sProxyProfiler;
    }
}
