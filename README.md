# todo-poc





# native-image and SubstrateVM

Download and install as your JDK
https://github.com/oracle/graal/releases/tag/vm-1.0.0-rc12

## CentOS/RHEL
`yum install zlib-devel`

Comment out AfterburnerModule in MatrixModule.java (not compatible)

`mvn clean package -P native-image`


```mv target/todo.todoapplication target/classes/
cd target/classes/
./todo.todoapplication```

`cd ../..`
