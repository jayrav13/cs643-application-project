rm Uber*.class
hadoop com.sun.tools.javac.Main UberLyftCombined.java
jar cf uberlyftcombined.jar Uber*.class
hdfs dfs -rm -r /application-project/solutions/uber-lyft-combined
hadoop jar uberlyftcombined.jar UberLyftCombined /application-project/input /application-project/solutions/uber-lyft-combined
rm -rf output/*
hdfs dfs -copyToLocal /application-project/solutions/uber-lyft-combined/* output/.
