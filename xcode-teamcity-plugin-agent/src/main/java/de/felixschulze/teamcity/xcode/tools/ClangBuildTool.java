/*
 *  Copyright 2011 Felix Schulze
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.felixschulze.teamcity.xcode.tools;

import com.intellij.execution.ExecutionException;

import java.io.*;
import java.util.Collections;
import java.util.List;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.util.Key;
import de.felixschulze.teamcity.xcode.NonZeroExitCodeException;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import org.jetbrains.annotations.NotNull;

public class ClangBuildTool {

    @NotNull
    private final String toolPath;

    @NotNull
    private final BuildRunnerContext context;

    public ClangBuildTool(@NotNull final BuildRunnerContext context) {
        this.toolPath = "/usr/local/bin/scan-build";
        this.context = context;
    }

    @NotNull
    public void xcodeBuildExecute(@NotNull final List<String> args) throws IOException, ExecutionException, NonZeroExitCodeException {
        Collections.addAll(args);
        execute(Collections.<String>emptyList(), args, "");
    }

    @NotNull
    protected File getWorkingDirectory() {
        return context.getWorkingDirectory();
    }

    protected void execute(@NotNull final List<String> argumentsBefore,
                           @NotNull final List<String> argumentsAfter,
                           @NotNull final String additionalArguments) throws IOException, ExecutionException, NonZeroExitCodeException {

        final GeneralCommandLine commandLine = createCommandLine(argumentsBefore, argumentsAfter, additionalArguments);

        final BuildProgressLogger logger = context.getBuild().getBuildLogger();

        final ClangBuildOutputParser outputParser = new ClangBuildOutputParser(logger, getWorkingDirectory());

        logger.message("Executing command: " + commandLine.getCommandLineString());

        final OSProcessHandler processHandler = createProcessHandler(commandLine);

        processHandler.addProcessListener(new ProcessAdapter() {
            @Override
            public void onTextAvailable(final ProcessEvent event, final Key outputType) {

                outputParser.handleLine(event.getText());

                if (outputType == ProcessOutputTypes.STDOUT) {
                    logger.message(event.getText());
                } else if (outputType == ProcessOutputTypes.STDERR) {
                    logger.error(event.getText());
                }
            }
        });

        processHandler.startNotify();
        processHandler.waitFor();

        final int exitCode = processHandler.getProcess().exitValue();
        if (exitCode != 0) {
            logger.error("Execution failed. Exit code is " + exitCode + ".");
            throw new NonZeroExitCodeException();
        }
    }

    protected void executeWithoutWaiting(@NotNull final List<String> argumentsAfter) throws IOException, ExecutionException {

        final GeneralCommandLine commandLine = createCommandLine(Collections.<String>emptyList(), argumentsAfter, "");

        final BuildProgressLogger logger = context.getBuild().getBuildLogger();

        logger.message("Executing command: " + commandLine.getCommandLineString());

        final OSProcessHandler processHandler = createProcessHandler(commandLine);

        processHandler.startNotify();

    }

    @NotNull
    private static OSProcessHandler createProcessHandler(@NotNull final GeneralCommandLine commandLine) throws ExecutionException {
        return new OSProcessHandler(commandLine.createProcess(), "") {
            @Override
            protected Reader createProcessOutReader() {
                return new InputStreamReader(getProcess().getInputStream());
            }

            @Override
            protected Reader createProcessErrReader() {
                return new InputStreamReader(getProcess().getErrorStream());
            }
        };
    }

    @NotNull
    private GeneralCommandLine createCommandLine(@NotNull final List<String> argumentsBefore,
                                                 @NotNull final List<String> argumentsAfter,
                                                 @NotNull final String additionalArguments) {
        final GeneralCommandLine cmd = new GeneralCommandLine();

        cmd.setExePath(toolPath);
        cmd.setWorkDirectory(getWorkingDirectory().getPath());
        cmd.setPassParentEnvs(false);
        cmd.setEnvParams(context.getBuildParameters().getEnvironmentVariables());
        cmd.addParameters(argumentsBefore);
        cmd.getParametersList().addParametersString(additionalArguments);
        cmd.addParameters(argumentsAfter);

        return cmd;
    }

}