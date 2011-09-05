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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    XCode Project File: <strong><props:displayValue name="project" emptyValue="not specified"/></strong>
</div>
<div class="parameter">
    Target: <strong><props:displayValue name="target" emptyValue="not specified"/></strong>
</div>
<div class="parameter">
    Configuration: <strong><props:displayValue name="configuration" emptyValue="not specified"/></strong>
</div>
<div class="parameter">
    SDK: <strong><props:displayValue name="sdk" emptyValue="not specified"/></strong>
</div>
<div class="parameter">
    Clean before build: <strong><props:displayValue name="clean" emptyValue="not specified"/></strong>
</div>

