#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED_PRG="$PRG"

APP_HOME=`dirname "$PRG"`
APP_HOME=`cd "$APP_HOME" && pwd`
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched.
if $cygwin ; then
    [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi

# Attempt to find java
if [ -z "$JAVA_HOME" ] ; then
    if $darwin ; then
        if [ -x '/usr/libexec/java_home' ] ; then
            JAVA_HOME=`/usr/libexec/java_home`
        elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
            JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
        fi
    else
        java_exe_path=$(which java 2>/dev/null)
        if [ -n "$java_exe_path" ] ; then
            java_exe_path=$(readlink -f "$java_exe_path")
            JAVA_HOME=$(dirname "$(dirname "$java_exe_path")")
        fi
    fi
    if [ -z "$JAVA_HOME" ] ; then
        die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    fi
fi

# Set JAVA_EXE
JAVA_EXE="$JAVA_HOME/bin/java"

# Check for JAVA_EXE
if [ ! -x "$JAVA_EXE" ] ; then
    die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Read all wrapper specific properties from the properties file
#
# It's a key value pair and the format is simple, so we can use a simple parser.
#
# Let's say we have a key 'my.key.long'. We would have the following variables:
# - my_key_long_set: 'true'
# - my_key_long_value: 'the value'
#
# This means we can test for the presence of a key with 'if [ "$my_key_long_set" = "true" ]; then'
#
read_wrapper_properties() {
    local wrapper_properties_path="$1"
    if [ -f "$wrapper_properties_path" ]; then
        while IFS= read -r line || [ -n "$line" ]
        do
            if echo "$line" | grep -E -q "^[[:space:]]*[a-zA-Z0-9]+.*=.*"; then
                # Line contains a key/value pair. Let's process it.
                # Find the position of the first '=', and split the line into a key and a value
                local key_value="$line"
                local i_equals=$(echo "$key_value" | awk '{print index($0,"=")}')
                local key=$(echo "$key_value" | awk "{print substr(\$0, 0, $i_equals - 1)}")
                local value=$(echo "$key_value" | awk "{print substr(\$0, $i_equals + 1)}")
                # Trim leading and trailing whitespace from the key and the value
                key=$(echo "$key" | awk '{$1=$1;print}')
                value=$(echo "$value" | awk '{$1=$1;print}')
                # Create a variable name from the key
                #
                # We want to be able to use the script with 'set -u', so we need to make sure that the variable name is valid.
                #
                # The keys in the properties file can contain dots, and we want to be able to use them as variable names.
                # We will replace all dots with underscores.
                #
                # We will also add a '_set' suffix to the variable name, so we can test for the presence of a key.
                local var_name=$(echo "$key" | tr '.' '_')
                eval "${var_name}_set=true"
                eval "${var_name}_value=$value"
            fi
        done < "$wrapper_properties_path"
    fi
}

# Determine the start parameters for the gradle wrapper, that is, the location of the properties file
#
# The properties file is located in the gradle/wrapper directory, and is named gradle-wrapper.properties
#
# The 'gradlew' script is located in the root of the project.
#
# The APP_HOME variable is the directory where the 'gradlew' script is located.
#
# So the properties file is located at "$APP_HOME/gradle/wrapper/gradle-wrapper.properties"
#
set_wrapper_properties_path() {
    WRAPPER_PROPERTIES_PATH="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"
}

# Parse the wrapper properties file
#
set_wrapper_properties_path
read_wrapper_properties "$WRAPPER_PROPERTIES_PATH"

# Set the wrapper jar path. It is configured in the properties file.
#
# The key is 'distributionUrl' and it has a value like 'https://services.gradle.org/distributions/gradle-7.4.2-bin.zip'
#
# We need to extract the version and the distribution type from the URL.
#
# The version is '7.4.2' and the distribution type is 'bin'.
#
# The wrapper jar path is then:
# 'wrapper/dists/gradle-7.4.2-bin/5b6s5f6v7v8v9v/gradle-wrapper-7.4.2.jar'
#
# The '5b6s5f6v7v8v9v' part is a hash of the distribution URL. We don't need to know the exact value, as long as it's unique.
# We will use the 'distributionUrl' to calculate the hash.
#
# The 'distributionBase' and 'distributionPath' properties are used to construct the path to the wrapper jar.
# The default is:
# distributionBase=GRADLE_USER_HOME
# distributionPath=wrapper/dists
#
# The 'zipStoreBase' and 'zipStorePath' properties are used to construct the path to the downloaded zip file.
# The default is:
# zipStoreBase=GRADLE_USER_HOME
# zipStorePath=wrapper/dists
#
# So the path to the wrapper jar is:
# $GRADLE_USER_HOME/wrapper/dists/gradle-7.4.2-bin/5b6s5f6v7v8v9v/gradle-wrapper-7.4.2.jar
#
# We will construct the path to the wrapper jar, and if it doesn't exist, we will download the distribution and unpack it.
#
set_wrapper_jar_path() {
    # The distribution base is the base directory for the distribution.
    # It can be 'GRADLE_USER_HOME' or 'PROJECT'.
    # If it's 'GRADLE_USER_HOME', then the distribution will be stored in the user's home directory.
    # If it's 'PROJECT', then the distribution will be stored in the project's directory.
    if [ "$distributionBase_set" = "true" ] && [ "$distributionBase_value" = "PROJECT" ]; then
        DISTRIBUTION_BASE_DIR="$APP_HOME"
    else
        DISTRIBUTION_BASE_DIR="$HOME"
    fi

    # The distribution path is the path to the distribution, relative to the distribution base.
    if [ "$distributionPath_set" = "true" ]; then
        DISTRIBUTION_PATH_DIR="$distributionPath_value"
    else
        DISTRIBUTION_PATH_DIR="wrapper/dists"
    fi

    # The zip store base is the base directory for the downloaded zip file.
    if [ "$zipStoreBase_set" = "true" ] && [ "$zipStoreBase_value" = "PROJECT" ]; then
        ZIP_STORE_BASE_DIR="$APP_HOME"
    else
        ZIP_STORE_BASE_DIR="$HOME"
    fi

    # The zip store path is the path to the downloaded zip file, relative to the zip store base.
    if [ "$zipStorePath_set" = "true" ]; then
        ZIP_STORE_PATH_DIR="$zipStorePath_value"
    else
        ZIP_STORE_PATH_DIR="wrapper/dists"
    fi

    # The distribution URL is the URL of the distribution to download.
    if [ "$distributionUrl_set" = "true" ]; then
        DISTRIBUTION_URL="$distributionUrl_value"
    else
        # This is a fallback, but it should not be used.
        # The distribution URL should be set in the properties file.
        #
        # If we get here, it means that the properties file is missing or corrupted.
        die "ERROR: distributionUrl is not set in $WRAPPER_PROPERTIES_PATH"
    fi

    # The distribution file name is the last part of the URL.
    DISTRIBUTION_FILE_NAME=$(basename "$DISTRIBUTION_URL")

    # The distribution name is the file name without the extension.
    DISTRIBUTION_NAME=$(echo "$DISTRIBUTION_FILE_NAME" | sed 's/\.zip$//')

    # The distribution directory is where the distribution is unpacked.
    DISTRIBUTION_DIR="$DISTRIBUTION_BASE_DIR/$DISTRIBUTION_PATH_DIR/$DISTRIBUTION_NAME"

    # The distribution zip file is the downloaded zip file.
    DISTRIBUTION_ZIP_FILE="$ZIP_STORE_BASE_DIR/$ZIP_STORE_PATH_DIR/$DISTRIBUTION_FILE_NAME"

    # The distribution hash is a hash of the distribution URL.
    # We will use 'md5sum' or 'md5' to calculate the hash.
    if command -v md5sum >/dev/null 2>&1; then
        DISTRIBUTION_HASH=$(echo "$DISTRIBUTION_URL" | md5sum | awk '{print $1}')
    elif command -v md5 >/dev/null 2>&1; then
        DISTRIBUTION_HASH=$(echo "$DISTRIBUTION_URL" | md5)
    else
        # This is a fallback. It's not as good as a real hash, but it will do.
        DISTRIBUTION_HASH=$(echo "$DISTRIBUTION_URL" | tr -c 'a-zA-Z0-9' '_')
    fi

    # The distribution hash directory is where the distribution is unpacked.
    DISTRIBUTION_HASH_DIR="$DISTRIBUTION_DIR/$DISTRIBUTION_HASH"

    # The wrapper jar file is the jar file that contains the wrapper.
    WRAPPER_JAR_FILE="$DISTRIBUTION_HASH_DIR/gradle-wrapper.jar"
}

# Set the wrapper jar path
set_wrapper_jar_path

# If the wrapper jar file doesn't exist, download the distribution and unpack it.
if [ ! -f "$WRAPPER_JAR_FILE" ]; then
    echo "Downloading $DISTRIBUTION_URL"
    # Create the directories if they don't exist
    mkdir -p "$DISTRIBUTION_HASH_DIR"
    mkdir -p "$(dirname "$DISTRIBUTION_ZIP_FILE")"
    # Download the distribution
    if command -v curl >/dev/null 2>&1; then
        curl -L -o "$DISTRIBUTION_ZIP_FILE" "$DISTRIBUTION_URL"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$DISTRIBUTION_ZIP_FILE" "$DISTRIBUTION_URL"
    else
        die "ERROR: Cannot download '$DISTRIBUTION_URL' because neither 'curl' nor 'wget' is available."
    fi
    # Unpack the distribution
    if command -v unzip >/dev/null 2>&1; then
        unzip -q -d "$DISTRIBUTION_HASH_DIR" "$DISTRIBUTION_ZIP_FILE"
    else
        die "ERROR: Cannot unpack '$DISTRIBUTION_ZIP_FILE' because 'unzip' is not available."
    fi
    # Find the wrapper jar file in the unpacked distribution
    #
    # The wrapper jar file is located in the 'lib' directory of the distribution.
    # The distribution is unpacked in a directory with the same name as the distribution.
    # For example, if the distribution is 'gradle-7.4.2-bin.zip', then the distribution is unpacked in a directory named 'gradle-7.4.2'.
    #
    # The wrapper jar file is named 'gradle-wrapper.jar'.
    # So we need to find the 'gradle-wrapper.jar' file in the unpacked distribution.
    #
    # The structure of the distribution is:
    # gradle-7.4.2
    #  - bin
    #  - docs
    #  - init.d
    #  - lib
    #    - gradle-wrapper-7.4.2.jar
    #    - ...
    #  - media
    #
    # So we need to find the 'gradle-wrapper.jar' file in the 'lib' directory.
    #
    # We will use 'find' to find the file.
    find_result=$(find "$DISTRIBUTION_HASH_DIR" -name "gradle-wrapper.jar" -type f)
    if [ -n "$find_result" ]; then
        # The wrapper jar file was found.
        # We need to move it to the correct location.
        # The correct location is '$DISTRIBUTION_HASH_DIR/gradle-wrapper.jar'
        #
        # The find command will return a path like:
        # '$DISTRIBUTION_HASH_DIR/gradle-7.4.2/lib/gradle-wrapper-7.4.2.jar'
        #
        # The 'gradle-wrapper.jar' file is what we are looking for.
        # We need to move it to '$DISTRIBUTION_HASH_DIR/gradle-wrapper.jar'
        #
        # We can't just move the file, because we don't know the exact path.
        # We will move all files from the 'lib' directory to the 'lib' directory of the hash directory.
        # And we will move all files from the 'bin' directory to the 'bin' directory of the hash directory.
        # And so on.
        #
        # The unpacked distribution has a single directory at the root.
        # We need to find the name of that directory.
        unpacked_dir=$(find "$DISTRIBUTION_HASH_DIR" -mindepth 1 -maxdepth 1 -type d)
        if [ -n "$unpacked_dir" ]; then
            # Move all files and directories from the unpacked directory to the hash directory.
            mv "$unpacked_dir"/* "$DISTRIBUTION_HASH_DIR"
            # Remove the now empty unpacked directory.
            rmdir "$unpacked_dir"
        fi
        # Now the wrapper jar file should be at the correct location.
        # We need to rename it to 'gradle-wrapper.jar'
        find_result=$(find "$DISTRIBUTION_HASH_DIR/lib" -name "gradle-wrapper-*.jar" -type f)
        if [ -n "$find_result" ]; then
            mv "$find_result" "$WRAPPER_JAR_FILE"
        fi
    fi
fi

# Increase the maximum number of open files, if requested.
if [ "$MAX_FD" != "0" ] ; then
    if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
        # Use system's max limit
        if $cygwin || $msys ; then
            MAX_FD_LIMIT=`ulimit -H -n`
        else
            MAX_FD_LIMIT=`ulimit -S -n`
        fi
    else
        MAX_FD_LIMIT="$MAX_FD"
    fi
    if $cygwin || $msys ; then
        ulimit -n $MAX_FD_LIMIT
    else
        ulimit -S -n $MAX_FD_LIMIT
    fi
    if [ $? -ne 0 ] ; then
        warn "Could not set maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# Collect all arguments for the java command, including memory settings and system properties.
#
# The following variables are used to configure the java command:
# - JAVA_OPTS: for all JVM options
# - GRADLE_OPTS: for all gradle options
#
# We will also use the 'DEFAULT_JVM_OPTS' variable to set default JVM options.
#
# We will also set the 'java.endorsed.dirs' system property on Java 8 and older.
#
# The arguments are passed to the 'java' command, so they need to be in the correct format.
#
# The format is:
# java [options] -jar gradle-wrapper.jar [gradle-options]
#
# The [options] are the JVM options.
# The [gradle-options] are the gradle options.
#
# The gradle options are the arguments passed to the 'gradlew' script.
#
# We will collect all JVM options in the 'GRADLE_OPTS' variable.
#
# The 'JAVA_OPTS' variable is used to pass JVM options to the script.
# The 'GRADLE_OPTS' variable is also used to pass JVM options to the script.
#
# We will concatenate the 'DEFAULT_JVM_OPTS', 'JAVA_OPTS' and 'GRADLE_OPTS' variables.
#
# The 'DEFAULT_JVM_OPTS' variable is set at the beginning of the script.
#
# We will also set the 'java.endorsed.dirs' system property on Java 8 and older.
#
# The endorsed directory is used to override the default implementation of some APIs.
# For example, it can be used to override the JAXB implementation.
#
# The endorsed directory is not supported on Java 9 and newer.
#
# We will check the java version, and if it's 8 or older, we will set the 'java.endorsed.dirs' system property.
#
# The java version is extracted from the output of 'java -version'.
# The output looks like:
# java version "1.8.0_292"
# Java(TM) SE Runtime Environment (build 1.8.0_292-b10)
# Java HotSpot(TM) 64-Bit Server VM (build 25.292-b10, mixed mode)
#
# We need to extract the '1.8' part from the first line.
#
# We will use 'sed' to extract the version.
#
JAVA_VERSION=$("$JAVA_EXE" -version 2>&1 | sed -n 's/.*version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')
if [ "$JAVA_VERSION" -lt 9 ]; then
    # Java 8 or older
    GRADLE_OPTS="$GRADLE_OPTS -Djava.endorsed.dirs=$APP_HOME/lib/endorsed"
fi

# Collect all arguments for the java command.
#
# We will use the 'eval' command to expand the variables.
# This is necessary because the variables can contain spaces.
#
# For example, if 'JAVA_OPTS' is '-Xmx512m -Xms512m', then 'eval' will expand it to two arguments.
#
# We will also use 'word-splitting' to split the arguments.
# This is done by not quoting the variables.
#
# We will use 'set -f' to disable globbing.
# This is necessary because the arguments can contain characters that would be interpreted as globs.
#
# We will use 'set --' to set the positional parameters.
# This is necessary because we want to use the arguments as positional parameters.
#
set -f
eval "set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS"
set +f

# Add the wrapper jar file to the classpath.
#
# The classpath is set with the '-cp' option.
#
CLASSPATH="$WRAPPER_JAR_FILE"

# Add the application base name to the gradle properties.
# This is used to identify the script that is running.
# For example, if the script is named 'gradlew', then the property will be 'org.gradle.appname=gradlew'
#
GRADLE_APP_NAME_ARG="-Dorg.gradle.appname=$APP_BASE_NAME"

# The main class of the wrapper is 'org.gradle.wrapper.GradleWrapperMain'
#
MAIN_CLASS="org.gradle.wrapper.GradleWrapperMain"

# Now we can execute the java command.
#
# The command is:
# java [jvm-options] -cp gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain [gradle-options]
#
# The [jvm-options] are the arguments we collected in the 'GRADLE_OPTS' variable.
# The [gradle-options] are the arguments passed to the 'gradlew' script.
#
# We will use 'exec' to replace the current process with the java process.
# This is more efficient than forking a new process.
#
exec "$JAVA_EXE" "$@" -cp "$CLASSPATH" "$GRADLE_APP_NAME_ARG" "$MAIN_CLASS" "$@"
