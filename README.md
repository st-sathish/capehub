# Capehub

## Mysql

### Create database by executing below command
```sh
CREATE DATABASE capehub CHARACTER SET utf8 COLLATE utf8_general_ci;
```

### Give grant permission to capehub user
```sh
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,INDEX ON capehub.* TO 'capehub'@'localhost' IDENTIFIED BY 'capehub';
```
### Replace -DdeployTo path and use below mvn command to build JAR files inside project root directory
```sh
	mvn clean install -DdeployTo=/f/development/workspace1/capehub/capehub -Dmaven.test.skip=true
```
### How to Run in Windows
	* Go to bin/start_capehub.bat and set FELIX_HOME=<download_project_path_location>
	* Go to MS-Dos or command prompt. Go the project location bin directory and type start_capehub.bat

### How to Run in Linux
	* Set FELIX_HOME by issuing "export FELIX_HOME=<felix home directory>"
	* Open the terminal. Go the project location bin directory and type ./start_capehub.sh
	* navigate to  http://localhost:8080
	* You may need to increase the memory available to maven using environment variables.  For example:
	```sh
    export MAVEN_OPTS="-Xms256m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=128m"
	```

### Customize Logging configuration
	* Logging configuration can be customized by modifying <felix home>/etc/services/org.ops4j.pax.logging.properties.
	
### Notes
	* The -DdeployTo path and the path in bin/start_capehub.bat FELIX_HOME should match.
	* Make the db changes where all the sql query are in the <Project_root_dir>/docs/script/ddl/mysql5.sql
	* All the bundles(JAR) were inside in your deploy path.