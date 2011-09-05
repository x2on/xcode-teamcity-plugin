/*
 * Copyright 2011 Felix Schulze
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.felixschulze.teamcity.xcode;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;

public class ClangBuildRunner implements AgentBuildRunner, AgentBuildRunnerInfo {
    @NotNull
    public BuildProcess createBuildProcess(@NotNull final AgentRunningBuild runningBuild, @NotNull final BuildRunnerContext context) throws RunBuildException {
        return new ClangBuildProcess(runningBuild, context);
    }

    @NotNull
    public AgentBuildRunnerInfo getRunnerInfo() {
        return this;
    }

    @NotNull
    public String getType() {
        return ClangConstants.RUNNER_TYPE;
    }

    public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
        BuildAgentSystemInfo info = agentConfiguration.getSystemInfo();
        if (info.isMac()) {
            return true;
        }
        return false;
    }
}
