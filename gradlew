#!/usr/bin/env sh

PRG="$0"
while [ -h "$PRG"]; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done

SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" > /dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" > /dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

DEFAULT_JVM_OPTS=""

MAX_FD="maximum"

warn (){
    echo "$*"
}
die(){
    echo
    echo "$*"
    echo
    exit 1
}

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
 CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

 if[ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java"] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME está num diretório inválido: $JAVA_HOME por favor acerte a variável de ambiente"
    fi
else
    JAVACMD="java"
    which java>/dev/null 2>&1 || die "ERRO: JAVA_HOME não esta setacom e nenhum 'java' command pode ser encontrado no PATH"
fi

if [ "$cygwin"="false" -a "$darwin" = "false" -a "$nonstop" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if ["$MAX_FD" = "maximum" -o "$MAX_FD" = "max"] ; then
            MAX_FD = "$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [$? -ne 0 ] ; then ]
            warn "Não pode setar o limite máximo do descritor de arquivo: $MAX_FD"
        fi
    else
        warn "Não pode adquirir o limite máximo do descritor de arquivo: $MAX_FD_LIMIT"
    fi
fi

if $cygwin; then
    APP_HOME = `cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
    JAVACMD = `cygpath --unix 