rm Uber*.class
hadoop com.sun.tools.javac.Main UberTaxi.java
jar cf ubertaxi.jar Uber*.class
hdfs dfs -rm -r /application-project/solutions/uber-taxi
hadoop jar ubertaxi.jar UberTaxi /application-project/input /application-project/solutions/uber-taxi
rm -rf output/*
hdfs dfs -copyToLocal /application-project/solutions/uber-taxi/* output/.
