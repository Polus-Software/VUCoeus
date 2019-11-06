#!/bin/sh

export JAVA_HOME=/app001/java1.8/jdk1.8.0_162/jre
startScript=/app001/tomcat6/bin/startup.sh
stopScript=/app001/tomcat6/bin/shutdown.sh

echo "Attempting to find process id for Tomcat ..."
tomcatPID=$(ps -ef | grep tomcat | grep -v grep | grep -v restart | awk '{print $2}')

if [ "$tomcatPID" == "" ]
then
    echo "Tomcat does not appear to be running..."
else
    $stopScript
    sleep 5

    echo "Checking if Tomcat shutdown cleanly ..."

    tomcatPID=$(ps -ef | grep tomcat | grep -v grep | grep -v restart | awk '{print $2}')

    if [ "$tomcatPID" == "" ]
    then
        echo "Tomcat shut down"
    else

        echo "Killing Tomcat using process id of $tomcatPID ..."
        kill -9 $tomcatPID
        echo "Waiting for process $tomcatPID to end ..."
        while ps -ef | grep $tomcatPID > /dev/null; do sleep 1; done
        echo "Process $tomcatPID has ended"
    fi
fi

echo "Attempting to start Tomcat ..."
$startScript

echo "Process complete!"


        


