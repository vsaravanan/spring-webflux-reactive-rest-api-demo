mvn clean package install -Dmaven.test.skip=true -T 1C
pm2 restart SpringReactiveMulti
tail -f /data/logs/SpringReactiveMulti/*
