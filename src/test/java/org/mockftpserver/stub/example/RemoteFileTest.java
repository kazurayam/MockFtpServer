/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mockftpserver.stub.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.core.command.CommandNames;
import org.mockftpserver.core.command.InvocationRecord;
import org.mockftpserver.stub.StubFtpServer;
import org.mockftpserver.stub.command.RetrCommandHandler;
import org.mockftpserver.test.AbstractTestCase;
import org.mockftpserver.test.IntegrationTest;

import java.io.IOException;

/**
 * Example test using StubFtpServer, with programmatic configuration.
 */
class RemoteFileTest extends AbstractTestCase implements IntegrationTest {

    private static final int PORT = 9981;
    private static final String FILENAME = "dir/sample.txt";

    private RemoteFile remoteFile;
    private StubFtpServer stubFtpServer;

    @Test
    void testReadFile() throws Exception {

        final String CONTENTS = "abcdef 1234567890";

        // Replace the default RETR CommandHandler; customize returned file contents
        RetrCommandHandler retrCommandHandler = new RetrCommandHandler();
        retrCommandHandler.setFileContents(CONTENTS);
        stubFtpServer.setCommandHandler(CommandNames.RETR, retrCommandHandler);
        
        stubFtpServer.start();
        
        String contents = remoteFile.readFile(FILENAME);

        // Verify returned file contents
        assertEquals(CONTENTS, contents);
        
        // Verify the submitted filename
        InvocationRecord invocationRecord = retrCommandHandler.getInvocation(0);
        String filename = invocationRecord.getString(RetrCommandHandler.PATHNAME_KEY);
        assertEquals(FILENAME, filename);
    }

    @Test
    void testReadFileThrowsException() {

        // Replace the default RETR CommandHandler; return failure reply code
        RetrCommandHandler retrCommandHandler = new RetrCommandHandler();
        retrCommandHandler.setFinalReplyCode(550);
        stubFtpServer.setCommandHandler(CommandNames.RETR, retrCommandHandler);
        
        stubFtpServer.start();

        try {
            remoteFile.readFile(FILENAME);
            fail("Expected IOException");
        }
        catch (IOException expected) {
            // Expected this
        }
    }
    
    @BeforeEach
    void setUp() throws Exception {
        remoteFile = new RemoteFile();
        remoteFile.setServer("localhost");
        remoteFile.setPort(PORT);
        stubFtpServer = new StubFtpServer();
        stubFtpServer.setServerControlPort(PORT);
    }

    @AfterEach
    void tearDown() throws Exception {
        stubFtpServer.stop();
    }

}
