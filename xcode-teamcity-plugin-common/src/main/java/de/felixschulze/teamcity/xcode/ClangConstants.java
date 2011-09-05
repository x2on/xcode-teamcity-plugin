package de.felixschulze.teamcity.xcode;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public interface ClangConstants {

        @NonNls
    @NotNull
    String ENV_CLANG_HOME = "CLANG_HOME";

    @NonNls
    @NotNull
    String RUNNER_TYPE = "Clang";

    @NonNls
    @NotNull
    String PARAM_TARGETNAME = "targetName";

    @NonNls
    @NotNull
    String PARAM_PROJECT = "project";

    @NonNls
    @NotNull
    String PARAM_CONFIGURATION = "configuration";

    @NonNls
    @NotNull
    String PARAM_SDK = "sdk";

    @NonNls
    @NotNull
    String PARAM_CLEAN = "clean";

    @NonNls
    @NotNull
    String RUNNER_DISPLAY_NAME =  "Clang Analyzer";

    @NonNls
    @NotNull
    String RUNNER_DESCRIPTION = "Anaylze XCode Project";
}
