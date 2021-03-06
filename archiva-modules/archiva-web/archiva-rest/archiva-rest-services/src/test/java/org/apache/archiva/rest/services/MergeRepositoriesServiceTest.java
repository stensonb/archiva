package org.apache.archiva.rest.services;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.archiva.maven2.model.Artifact;
import org.apache.archiva.rest.api.services.MergeRepositoriesService;
import org.apache.commons.io.FileUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author Olivier Lamy
 */
public class MergeRepositoriesServiceTest
    extends AbstractArchivaRestTest
{

    private File repo = new File( System.getProperty( "builddir" ), "test-repository" );

    private File repoStage = new File( System.getProperty( "builddir" ), "test-repository-stage" );

    @Override
    @Before
    public void startServer()
        throws Exception
    {

        FileUtils.copyDirectory( new File( System.getProperty( "basedir" ), "src/test/repo-with-osgi" ), repo );
        FileUtils.copyDirectory( new File( System.getProperty( "basedir" ), "src/test/repo-with-osgi-stage" ),
                                 repoStage );
        super.startServer();

    }

    @Override
    @After
    public void stopServer()
        throws Exception
    {
        // TODO delete repositories
        super.stopServer();
        FileUtils.deleteDirectory( repo );
        FileUtils.deleteDirectory( repoStage );
    }

    @Test
    public void getMergeConflictedArtifacts()
        throws Exception
    {
        String testRepoId = "test-repository";
        try
        {

            createStagedNeededRepo( testRepoId, repo.getAbsolutePath(), true );

            MergeRepositoriesService service = getMergeRepositoriesService( authorizationHeader );

            List<Artifact> artifactMetadatas = service.getMergeConflictedArtifacts( testRepoId + "-stage", testRepoId );

            log.info( "conflicts: {}", artifactMetadatas );

            assertThat( artifactMetadatas ).isNotNull().isNotEmpty().hasSize( 8 );


        }
        catch ( Exception e )
        {
            log.error( e.getMessage(), e );
            throw e;
        }
        finally
        {
            deleteTestRepo( testRepoId );
        }
    }

    @Test
    public void merge()
        throws Exception
    {
        String testRepoId = "test-repository";
        try
        {
            createStagedNeededRepo( testRepoId, repo.getAbsolutePath(), true );

            String mergedArtifactPath =
                "org/apache/felix/org.apache.felix.bundlerepository/1.6.4/org.apache.felix.bundlerepository-1.6.4.jar";
            String mergedArtifactPomPath =
                "org/apache/felix/org.apache.felix.bundlerepository/1.6.4/org.apache.felix.bundlerepository-1.6.4.pom";

            assertTrue( new File( repoStage, mergedArtifactPath ).exists() );
            assertTrue( new File( repoStage, mergedArtifactPomPath ).exists() );

            MergeRepositoriesService service = getMergeRepositoriesService( authorizationHeader );

            service.mergeRepositories( testRepoId + "-stage", testRepoId, true );

            assertTrue( new File( repo, mergedArtifactPath ).exists() );
            assertTrue( new File( repo, mergedArtifactPomPath ).exists() );

        }
        catch ( Exception e )
        {
            log.error( e.getMessage(), e );
            throw e;
        }
        finally
        {
            deleteTestRepo( testRepoId );
        }
    }
}
