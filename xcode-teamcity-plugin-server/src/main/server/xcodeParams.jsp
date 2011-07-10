<%--
 ~  Copyright 2010 Felix Schulze
 ~
 ~  Licensed under the Apache License, Version 2.0 (the "License");
 ~  you may not use this file except in compliance with the License.
 ~  You may obtain a copy of the License at
 ~
 ~      http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~  Unless required by applicable law or agreed to in writing, software
 ~  distributed under the License is distributed on an "AS IS" BASIS,
 ~  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~  See the License for the specific language governing permissions and
 ~  limitations under the License.
  --%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="XCodeBuild options">

    <tr>
        <th>
            <label for="project">Xcode Project File:</label><l:star/>
        </th>
        <td>
            <props:textProperty name="project" className="longField"/>
            <span class="error" id="error_project"></span>
            <span class="smallNote">XCode project file name relative to checkout root.</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="target">Target:</label><l:star/>
        </th>
        <td>
            <props:textProperty name="targetName" className="longField"/>
            <span class="error" id="error_targetName"></span>
            <span class="smallNote">Name of target to build.</span>
        </td>
    </tr>

    <tr>
        <th>
            <label for="configuration">Configuration:</label><l:star/>
        </th>
        <td><props:textProperty name="configuration" className="longField"/>
            <span class="error" id="error_configuration"></span>
            <span class="smallNote">The build configuration to use when building the target.</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="sdk">SDK:</label><l:star/>
        </th>
        <td>
            <props:textProperty name="sdk"/>
            <span class="error" id="error_sdk"></span>
            <span class="smallNote">Specify a specific SDK to build with. eg: iphoneos3.2, iphonesimulator4.3, etc.  Full list: 'xcodebuild -showsdks'</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="clean">Clean before build:</label>
        </th>
        <td>
            <props:checkboxProperty name="clean"/>
        </td>
    </tr>

</l:settingsGroup>

