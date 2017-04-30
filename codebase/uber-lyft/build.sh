rm Uber*.class
hadoop com.sun.tools.javac.Main UberLyft.java
jar cf uberlyft.jar Uber*.class
hdfs dfs -rm -r /application-project/solutions/uber-lyft
hadoop jar uberlyft.jar UberLyft /application-project/input /application-project/solutions/uber-lyft
rm -rf output/*
hdfs dfs -copyToLocal /application-project/solutions/uber-lyft/* output/.
