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


package org.apache.isis.metamodel.examples.facets.namefile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.metamodel.examples.facets.namefile.NameFileFacet;
import org.apache.isis.metamodel.facets.FacetHolderImpl;


public class NameFileFacetInstantiationTest {

    private FacetHolderImpl holder;

    @Before
    public void setUp() throws Exception {
        holder = new FacetHolderImpl();
    }

    @After
    public void tearDown() throws Exception {
        holder = null;
    }

    @Test
    public void canInstantiate() {
        new NameFileFacet(holder, "foobar");
    }

}
