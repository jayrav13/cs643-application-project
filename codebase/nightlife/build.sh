rm Peak*.class
hadoop com.sun.tools.javac.Main Peak.java
jar cf peak.jar Peak*.class
hdfs dfs -rm -r /application-project/solutions/nightlife
hadoop jar peak.jar Peak /application-project/clean /application-project/solutions/nightlife
rm -rf output/*
hdfs dfs -copyToLocal /application-project/solutions/nightlife/* output/.
