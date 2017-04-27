rm Peak*.class
hadoop com.sun.tools.javac.Main Peak.java
jar cf peak.jar Peak*.class
hdfs dfs -rm -r /application-project/solutions/peak
hadoop jar peak.jar Peak /application-project/input /application-project/solutions/peak
rm -rf output/*
hdfs dfs -copyToLocal /application-project/solutions/peak/* output/.
