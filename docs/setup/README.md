## Build
```

	mvn clean install -DdeployTo=<your deployment location>

## Build but Skip test


```

	mvn clean install -DdeployTo=/f/development/workspace1/capehub/capehub/ -Dmaven.test.skip=true
	
	
e.g:
```

	mvn clean install -DdeployTo=/f/development/workspace1/capehub/capehub/ -Dmaven.test.skip=true
