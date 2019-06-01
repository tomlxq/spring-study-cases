[root@localhost opt]# cat /opt/mongodb/slave/mongodb.cfg    
dbpath=/opt/mongodb/master/data
logpath=/opt/mongodb/master/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=21072
slave=true
source=192.168.238.150:21071
[root@localhost opt]# cat /opt/mongodb/master/mongodb.cfg      
dbpath=/opt/mongodb/master/data
logpath=/opt/mongodb/master/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=21071
master=true
source=192.168.238.150:21072