/*
 *  Copyright 2010 Felix Schulze
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

import jetbrains.buildServer.agent.BuildProgressLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XCodeBuildOutputParser parses a String and sends a unit test service message to TeamCity.
 *
 * @author <a href="mailto:code@felixschulze.de">Felix Schulze</a>
 */

public class ClangBuildOutputParser {

    private static Pattern BUG_COUNTER = Pattern.compile("scan-build: ([0-9]) bugs found.(.*)", Pattern.DOTALL);
    private static Pattern HTML_FILE = Pattern.compile("scan-build: Run 'scan-view (.*)' to examine bug reports.(.*)", Pattern.DOTALL);

    private final BuildProgressLogger buildListener;
    private final File workingDirectory;

    public ClangBuildOutputParser(BuildProgressLogger buildListener, File workingDirectory) {
        this.buildListener = buildListener;
        this.workingDirectory = workingDirectory;
    }

    protected void handleLine(String line) {
        Matcher m;
            m = BUG_COUNTER.matcher(line);
            if (m.matches()) {
                buildListener.message("##teamcity[buildStatus status='FAILURE' text='Analyzer found: "+m.group(1)+" bugs']");
                return;
            }

            m = HTML_FILE.matcher(line);
            if (m.matches()) {
                File artefactDir = new File(workingDirectory.getAbsolutePath() + "/analyzer");
                File reportDir = new File(m.group(1));
                try {
                    FileUtils.copyDirectory(reportDir, artefactDir);
                } catch (IOException e) {
                    buildListener.message("Problem while copying clang report: "+e.getMessage());
                }
                return;
            }




    }
}
