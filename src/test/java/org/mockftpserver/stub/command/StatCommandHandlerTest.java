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
package org.mockftpserver.stub.command;

import org.mockftpserver.core.command.*;
import org.mockftpserver.core.command.AbstractCommandHandlerTestCase;
import org.mockito.Mockito;

/**
 * Tests for the StatCommandHandler class
 * 
 * @author Chris Mair
 */
public final class StatCommandHandlerTest extends AbstractCommandHandlerTestCase {

    private static final String RESPONSE_DATA = "status info 123.456";
    private static final String PATHNAME = "dir/file";

    private StatCommandHandler commandHandler;

    /**
     * Test the handleCommand() method, when no pathname parameter is specified
     */
    public void testHandleCommand_NoPathname() throws Exception {
        final Command COMMAND = new Command(CommandNames.STAT, EMPTY);
        commandHandler.setStatus(RESPONSE_DATA);
        commandHandler.handleCommand(COMMAND, session);

        Mockito.verify(session).sendReply(ReplyCodes.STAT_SYSTEM_OK, formattedReplyTextFor(ReplyCodes.STAT_SYSTEM_OK, RESPONSE_DATA));
        
        verifyNumberOfInvocations(commandHandler, 1);
        verifyOneDataElement(commandHandler.getInvocation(0), StatCommandHandler.PATHNAME_KEY, null);
    }

    /**
     * Test the handleCommand() method, specifying a pathname parameter
     * @throws Exception - if an error occurs
     */
    public void testHandleCommand_Pathname() throws Exception {
        final Command COMMAND = new Command(CommandNames.STAT, array(PATHNAME));

        commandHandler.setStatus(RESPONSE_DATA);
        commandHandler.handleCommand(COMMAND, session);

        Mockito.verify(session).sendReply(ReplyCodes.STAT_FILE_OK, formattedReplyTextFor(ReplyCodes.STAT_FILE_OK, RESPONSE_DATA));
        
        verifyNumberOfInvocations(commandHandler, 1);
        verifyOneDataElement(commandHandler.getInvocation(0), StatCommandHandler.PATHNAME_KEY, PATHNAME);
    }

    /**
     * Test the handleCommand() method, when the replyCode is explicitly set
     */
    public void testHandleCommand_OverrideReplyCode() throws Exception {
        final Command COMMAND = new Command(CommandNames.STAT, EMPTY);
        commandHandler.setStatus(RESPONSE_DATA);
        commandHandler.setReplyCode(200);
        commandHandler.handleCommand(COMMAND, session);

        Mockito.verify(session).sendReply(200, replyTextFor(200));

        verifyNumberOfInvocations(commandHandler, 1);
        verifyOneDataElement(commandHandler.getInvocation(0), StatCommandHandler.PATHNAME_KEY, null);
    }

    /**
     * Perform initialization before each test
     * 
     * @see org.mockftpserver.core.command.AbstractCommandHandlerTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        commandHandler = new StatCommandHandler();
        commandHandler.setReplyTextBundle(replyTextBundle);
    }

}
