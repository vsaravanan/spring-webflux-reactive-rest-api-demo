rootdir=`pwd`
jarname=SpringReactiveMulti
cd /data/$jarname
rm -rf $rootdir/logs/*
rm -rf $rootdir/logPath_IS_UNDEFINED
rm -rf /data/logs/$jarname
java -jar target/$jarname.jar

