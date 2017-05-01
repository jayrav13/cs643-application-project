rm Peak*.class
hadoop com.sun.tools.javac.Main Peak.java
jar cf peak.jar Peak*.class
hdfs dfs -rm -r /application-project/solutions/hours
hadoop jar peak.jar Peak /application-project/clean /application-project/solutions/hours
rm -rf output/*
hdfs dfs -copyToLocal /application-project/solutions/hours* output/.
