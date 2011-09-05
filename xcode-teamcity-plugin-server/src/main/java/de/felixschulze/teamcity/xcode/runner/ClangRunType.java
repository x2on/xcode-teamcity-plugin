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

package de.felixschulze.teamcity.xcode.runner;

import de.felixschulze.teamcity.xcode.ClangConstants;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClangRunType extends RunType {

    @NotNull
    private final Map<String, String> defaultParameters;
    @NotNull
    private final PluginDescriptor pluginDescriptor;
    @NotNull
    private final String editParamsPageName;
    @NotNull
    private final String viewParamsPageName;

    public ClangRunType(@NotNull final RunTypeRegistry runTypeRegistry,
                        @NotNull final PluginDescriptor pluginDescriptor) {
        defaultParameters = new HashMap<String, String>();
        this.pluginDescriptor = pluginDescriptor;
        editParamsPageName = "clangParams.jsp";
        viewParamsPageName = "viewClangParams.jsp";
        runTypeRegistry.registerRunType(this);
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return defaultParameters;
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return pluginDescriptor.getPluginResourcesPath(editParamsPageName);
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return pluginDescriptor.getPluginResourcesPath(viewParamsPageName);
    }

    @NotNull
    @Override
    public String getType() {
        return ClangConstants.RUNNER_TYPE;
    }

    @Override
    public String getDisplayName() {
        return ClangConstants.RUNNER_DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return ClangConstants.RUNNER_DESCRIPTION;
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {

        return new PropertiesProcessor() {
            public Collection<InvalidProperty> process(final Map<String, String> properties) {
                final Collection<InvalidProperty> result = new ArrayList<InvalidProperty>();

                if (PropertiesUtil.isEmptyOrNull(properties.get(ClangConstants.PARAM_PROJECT))) {
                    result.add(new InvalidProperty(ClangConstants.PARAM_PROJECT, "Project File can not be empty"));
                }
                if (PropertiesUtil.isEmptyOrNull(properties.get(ClangConstants.PARAM_TARGETNAME))) {
                    result.add(new InvalidProperty(ClangConstants.PARAM_TARGETNAME, "Target can not be empty"));
                }
                if (PropertiesUtil.isEmptyOrNull(properties.get(ClangConstants.PARAM_CONFIGURATION))) {
                    result.add(new InvalidProperty(ClangConstants.PARAM_CONFIGURATION, "Configuration can not be empty"));
                }
                if (PropertiesUtil.isEmptyOrNull(properties.get(ClangConstants.PARAM_SDK))) {
                    result.add(new InvalidProperty(ClangConstants.PARAM_PROJECT, "SDK can not be empty"));
                }

                return result;
            }
        };
    }
}
