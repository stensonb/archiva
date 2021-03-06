package org.apache.archiva.metadata.repository;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Configurable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable;
import org.springframework.stereotype.Service;

/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
@Service("repositorySessionFactory#test")
public class TestRepositorySessionFactory
    implements RepositorySessionFactory
{
    private MetadataResolver resolver;

    @Override
    public RepositorySession createSession()
    {
        return new RepositorySession( new TestMetadataRepository(), resolver );
    }

    public void setResolver( MetadataResolver resolver )
    {
        Configurable configurable;
        Startable startable;
        this.resolver = resolver;
    }
}
