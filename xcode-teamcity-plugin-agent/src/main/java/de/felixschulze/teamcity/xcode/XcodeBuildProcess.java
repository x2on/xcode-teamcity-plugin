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

import de.felixschulze.teamcity.xcode.tools.XcodeBuildTool;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;
import com.intellij.execution.ExecutionException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XcodeBuildProcess extends CallableBuildProcess {

    @NotNull
    private final AgentRunningBuild build;
    @NotNull
    private final BuildRunnerContext context;

    public XcodeBuildProcess(@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context) {
        this.build = build;
        this.context = context;
    }

    @NotNull
    public BuildFinishedStatus call() throws Exception {
        final BuildProgressLogger logger = build.getBuildLogger();

        logger.targetStarted("Xcode build");

        final String projectFile = getParameter(XcodeConstants.PARAM_PROJECT, true);
        final String target = getParameter(XcodeConstants.PARAM_TARGETNAME, false);
        final String configuration = getParameter(XcodeConstants.PARAM_CONFIGURATION, false);
        final String sdk = getParameter(XcodeConstants.PARAM_SDK, false);

        final Boolean clean = Boolean.parseBoolean(getParameter(XcodeConstants.PARAM_CLEAN, false));

        final Boolean ignoreUnitTests = Boolean.parseBoolean(getParameter(XcodeConstants.PARAM_IGNOREUNITESTS, false));


        if (clean) {
            logger.targetStarted("clean");
            File buildDirectory = new File(context.getWorkingDirectory(), "build");
            logger.message("Clean dir: "+buildDirectory.getAbsolutePath());
            FileUtils.deleteDirectory(buildDirectory);
            logger.targetFinished("clean");

        }

        final long startTime = System.currentTimeMillis();

        if (buildXcodeProject(projectFile, target, configuration, sdk, ignoreUnitTests)) {
            final long endTime = System.currentTimeMillis();
            logger.message("Build finished (took " + ((endTime - startTime) / 1000) + " seconds)");
            logger.targetFinished("Xcode build");
            return BuildFinishedStatus.FINISHED_SUCCESS;
        } else {
            logger.error("Error during Xcode build.");
            return BuildFinishedStatus.FINISHED_FAILED;
        }
    }

    private String getParameter(@NotNull final String parameterName, final boolean isPath) {
        final String value = context.getRunnerParameters().get(parameterName);
        if (value == null || value.trim().length() == 0) return null;
        String result = value.trim();
        if (isPath) {
            result = preparePath(result);
        }
        return result;
    }


    @NotNull
    private String preparePath(@NotNull final String value) {
        return new File(value).isAbsolute() ? value : new File(context.getBuild().getCheckoutDirectory(), value).getAbsolutePath();
    }

    private boolean buildXcodeProject(String projectFile, String target, String configuration, String sdk, Boolean ignoreUnitTests) {
        final BuildProgressLogger logger = build.getBuildLogger();

        logger.targetStarted("xcodebuild");

        XcodeBuildTool buildTool = new XcodeBuildTool(context, ignoreUnitTests);


        final List<String> argsAfter = new ArrayList<String>();
        argsAfter.add("-project");
        argsAfter.add(projectFile);
        argsAfter.add("-target");
        argsAfter.add(target);
        argsAfter.add("-configuration");
        argsAfter.add(configuration);
        argsAfter.add("-sdk");
        argsAfter.add(sdk);

        try {
            buildTool.xcodeBuildExecute(argsAfter);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
            return false;
        } catch (NonZeroExitCodeException e) {
            return false;
        }
        logger.targetFinished("xcodebuild");
        return true;
    }

}
