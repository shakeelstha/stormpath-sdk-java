/*
 * Copyright 2013 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.sdk.client

import com.stormpath.sdk.cache.Cache
import com.stormpath.sdk.cache.CacheManager
import com.stormpath.sdk.cache.Caches
import com.stormpath.sdk.impl.cache.DefaultCacheManager
import com.stormpath.sdk.impl.cache.DisabledCacheManager
import org.testng.annotations.Test

import static org.testng.Assert.assertSame
import static org.testng.Assert.assertTrue

/**
 *
 * @since 0.8
 */
class ClientBuilderTest {

    @Test
    void testDefault() {

        Client client = new ClientBuilder().setApiKey(new DefaultApiKey("fakeId", "fakeSecret")).build()

        //caching disabled by default (end-user must explicilty enable it based on their caching preferences):
        assertTrue client.dataStore.cacheManager instanceof DisabledCacheManager
    }

    @Test
    void testSetCustomCacheManager() {

        //dummy implementation:
        def cacheManager = new CacheManager() {
            def <K, V> Cache<K, V> getCache(String name) {
                return null
            }
        }

        Client client = new ClientBuilder()
                .setApiKey(new DefaultApiKey("fakeId", "fakeSecret"))
                .setCacheManager(cacheManager)
                .build()

        assertSame client.dataStore.cacheManager, cacheManager
    }

    @Test
    void testDefaultCacheManager() {

        Client client = new ClientBuilder()
            .setApiKey(new DefaultApiKey("fakeId", "fakeSecret"))
            .setCacheManager(Caches.newCacheManager().build())
            .build()

        def cm = client.dataStore.cacheManager

        assertTrue cm instanceof DefaultCacheManager
    }
}
