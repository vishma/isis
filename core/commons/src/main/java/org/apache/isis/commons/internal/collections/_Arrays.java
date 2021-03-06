/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.commons.internal.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collector;

import javax.annotation.Nullable;

import org.apache.isis.commons.internal._Constants;
import org.apache.isis.commons.internal.base._Casts;
import org.apache.isis.commons.internal.base._NullSafe;

/**
 * <h1>- internal use only -</h1>
 * <p>
 * Common Array idioms.
 * </p>
 * <p>
 * <b>WARNING</b>: Do <b>NOT</b> use any of the classes provided by this package! <br/> 
 * These may be changed or removed without notice!
 * </p>
 * 
 * @since 2.0.0
 */
public final class _Arrays {
	
	private _Arrays(){}
	
    // -- PREDICATES

	/**
	 * @param cls
	 * @return whether {@code cls} represents an array
	 */
    public static boolean isArrayType(@Nullable final Class<?> cls) {
        return cls!=null ? cls.isArray() : false;
    }
    
    /**
     * For convenience also provided in {@link _Collections}.
     * @param cls
     * @return whether {@code cls} implements the java.util.Collection interface 
     * or represents an array
     */
    public static boolean isCollectionOrArrayType(final Class<?> cls) {
        return _Collections.isCollectionType(cls) || _Arrays.isArrayType(cls);
    }
    
    // -- TO-ARRAY COLLECTORS
    
    /**
     * Known-size Collector.
     * @param componentType
     * @param length
     * @return
     */
    public static <T> Collector<T,?,T[]> toArray(final Class<T> componentType, final int length){
    	Objects.requireNonNull(componentType);
		return new _Arrays_Collector<T>(componentType, length);
	}
    
    /**
     * Unknown-size Collector.
     * @param componentType
     * @return
     */
    public static <T> Collector<T,?,T[]> toArray(final Class<T> componentType){
    	Objects.requireNonNull(componentType);
		return new _Arrays_CollectorUnknownSize<T>(componentType);
	}
    
    // -- CONCATENATION
    
    /**
     * Returns a new array containing all components {first, *rest} 
     * @param first (non-null)
     * @param rest (nullable)
     * @return (non-null)
     */
    @SafeVarargs
	public static <T> T[] combine(T first, @Nullable  T... rest) {
    	Objects.requireNonNull(first);
    	final int restLength = _NullSafe.size(rest);
    	final T[] all = _Casts.uncheckedCast(Array.newInstance(first.getClass(), restLength+1));
        all[0] = first;
        if(restLength>0) {
        	System.arraycopy(rest, 0, all, 1, restLength);
        }
        return all;
    }
    
    /**
     * Returns a new array containing all components {*first, *rest} 
     * @param first (nullable)
     * @param rest (nullable)
     * @return (non-null)
     */
    @SafeVarargs
	public static <T> T[] combine(T[] first, T... rest) {
    	final int firstLength = _NullSafe.size(first);
    	final int restLength = _NullSafe.size(rest);
    	if(firstLength + restLength == 0) {
    		return _Casts.uncheckedCast(_Constants.emptyObjects);
    	}
    	final Class<?> componentType = firstLength>0 ? first[0].getClass() : rest[0].getClass();
    	final T[] all = _Casts.uncheckedCast(Array.newInstance(componentType, firstLength + restLength));
    	System.arraycopy(first, 0, all, 0, firstLength);
        System.arraycopy(rest, 0, all, firstLength, restLength);
        return all;
    }

    // -- CONSTRUCTION
    
    /**
     * Copies a collection's elements into an array.
     *
     * @param iterable the iterable to copy
     * @param type the type of the elements
     * @return a newly-allocated array into which all the elements of the iterable
     *     have been copied (non-null)
     */
	public static <T> T[] toArray(@Nullable final Collection<? extends T> collection, final Class<T> componentType) {
		Objects.requireNonNull(componentType);
		return _NullSafe.stream(collection)
				.collect(toArray(componentType, collection!=null ? collection.size() : 0));
	}
    
    /**
     * Copies an iterable's elements into an array.
     *
     * @param iterable the iterable to copy
     * @param type the type of the elements
     * @return a newly-allocated array into which all the elements of the iterable
     *     have been copied (non-null)
     */
	public static <T> T[] toArray(@Nullable final Iterable<? extends T> iterable, final Class<T> componentType) {
		Objects.requireNonNull(componentType);
		if(iterable!=null && (iterable instanceof Collection)) {
			return toArray((Collection<? extends T>) iterable, componentType);
		}
		return _NullSafe.stream(iterable)
				.collect(toArray(componentType));
	}
    
    // -- COMPONENT TYPE INFERENCE
    
    /**
     * Returns the inferred element type of the specified array type 
     * @param type of the array for which to infer the element type 
     * @return inferred type or null if inference fails
     */
    public static @Nullable Class<?> inferComponentTypeIfAny(@Nullable final Class<?> arrayType) {
        if(!isArrayType(arrayType)) {
            return null;
        }
        return arrayType.getComponentType();
    }
    
	// --
	
}
