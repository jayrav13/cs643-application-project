## Background

Uber has been a popular, yet controversial company, especially in New York City. After Uber was founded in 2009, there was an evident decrease in the use of traditional NYC taxies and an increase in using Uber via its mobile application. We use pickup data over the course of 6 months from Uber, Lyft, and 8 FHV companies in NYC to determine the following:
  - Heatmap of NYC using 15k points
  - Peak Locations
  - Peak Hours
  - Uber vs Lyft vs FHV (pickups per day) from July - Sept 2014
  - Popular Nightlife Pickups from Friday - Sunday between 12AM and 3AM

## Documentation

### Geocoding API
Download the [Data Science Toolkit](http://www.datasciencetoolkit.org/) and spin up your own AMI, which will be used to convert addresses from the dataset into coordinates.

### Dataset
The dataset we used was from Kaggle, called [Uber Pickups in New York City](https://www.kaggle.com/fivethirtyeight/uber-pickups-in-new-york-city).

### Sanitization
In order to normalize data, use the [python scripts](etl/) and replace the API request URL with the public IP of your DSTK AWS AMI.

### Hadoop/MapReduce
We now need to aggregate this data and the quickest way is to use Hadoop. We used one namenode and three datanodes in a cluster on t1.micro AWS instances. Once you have your Hadoop cluster configured, run the [build.sh](codebase/nightlife/build.sh) for example, in order to compile the Java program, remove unnecessary files, and run the application, and copy the files from hdfs to your local filesystem.

Your output should now be an aggregate key-value pair of the specific application, whether it be peak hours, peak locations, nightlife, uber vs taxi, or uber vs lyft.

### Next Steps
Once you have aggregated data, load it into a database (PostgreSQL/MySQL/etc.) and use something like [Angular Charts](http://jtblin.github.io/angular-chart.js/) to create beautiful, responsive charts. Check out our [web application repo](https://github.com/jayrav13/cs643-web-app) for more details on this.

By Jay Ravaliya and Sapan Patel
