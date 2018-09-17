/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geosparql_jena.implementation.index;

/**
 *
 *
 */
public interface IndexDefaultValues {

    public static final int UNLIMITED_INDEX = -1;

    public static final int NO_INDEX = 0;
    public static final long INDEX_EXPIRY_INTERVAL = 5000l;
    public static final long FULL_INDEX_WARNING_INTERVAL = 30000l;
    public static final long INDEX_CLEANER_INTERVAL = 1000l;
    public static final long MINIMUM_INDEX_CLEANER_INTERVAL = 100l;
    public static final int UNLIMITED_INITIAL_CAPACITY = 50000;
}