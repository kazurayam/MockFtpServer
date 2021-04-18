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
package org.mockftpserver.core.command;

import org.mockito.Mockito;

/**
 * Tests for the UnsupportedCommandHandler class
 *
 * @author Chris Mair
 */
public final class UnsupportedCommandHandlerTest extends AbstractCommandHandlerTestCase {

    private UnsupportedCommandHandler commandHandler;
    private Command command1;

    public void testHandleCommand() throws Exception {
        commandHandler.handleCommand(command1, session);

        Mockito.verify(session).sendReply(ReplyCodes.COMMAND_NOT_SUPPORTED, replyTextFor(ReplyCodes.COMMAND_NOT_SUPPORTED));

        verifyNumberOfInvocations(commandHandler, 1);
        verifyNoDataElements(commandHandler.getInvocation(0));
    }

    protected void setUp() throws Exception {
        super.setUp();
        commandHandler = new UnsupportedCommandHandler();
        commandHandler.setReplyTextBundle(replyTextBundle);
        command1 = new Command("XXXX", EMPTY);
    }

}