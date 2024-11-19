/*
 * Copyright 2008 the original author or authors.
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

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.core.command.AbstractCommandHandlerTestCase;
import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandNames;
import org.mockftpserver.core.command.ReplyCodes;

import java.net.InetAddress;

/**
 * Tests for the PortCommandHandler class
 *
 * @author Chris Mair
 */
class PortCommandHandlerTest extends AbstractCommandHandlerTestCase {

    private static final String[] PARAMETERS = new String[]{"11", "22", "33", "44", "1", "206"};
    private static final String[] PARAMETERS_INSUFFICIENT = new String[]{"7", "29", "99", "11", "77"};
    private static final int PORT = (1 << 8) + 206;
    private static final InetAddress HOST = inetAddress("11.22.33.44");

    private PortCommandHandler commandHandler;

    @Test
    void testHandleCommand() throws Exception {
        final Command COMMAND = new Command(CommandNames.PORT, PARAMETERS);

        commandHandler.handleCommand(COMMAND, session);

        verify(session).setClientDataPort(PORT);
        verify(session).setClientDataHost(HOST);
        verify(session).sendReply(ReplyCodes.PORT_OK, replyTextFor(ReplyCodes.PORT_OK));

        verifyNumberOfInvocations(commandHandler, 1);
        verifyTwoDataElements(commandHandler.getInvocation(0),
                PortCommandHandler.HOST_KEY, HOST,
                PortCommandHandler.PORT_KEY, PORT);
    }

    @Test
    void testHandleCommand_InsufficientParameters() throws Exception {
        testHandleCommand_InvalidParameters(commandHandler, CommandNames.PORT, PARAMETERS_INSUFFICIENT);
    }

    @BeforeEach
    void setUp() throws Exception {
        commandHandler = new PortCommandHandler();
        commandHandler.setReplyTextBundle(replyTextBundle);
    }

}
