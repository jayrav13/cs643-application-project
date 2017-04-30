/**
* UberLyft
*
* Compare Uber and Lyft pickups per day
**/

// Imports
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.join.TupleWritable;
import java.util.regex.Pattern;
import java.util.*;

public class UberLyft {

  // Mapper class
  public static class UberLyftMapper extends Mapper<Object, Text, Text, IntWritable>{

    // IntWritable will set the context.write(...)'s value
    private final static IntWritable one = new IntWritable(1);

    // Track the current word as a Text() object
		private Text word = new Text();

    // Map function
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

      // Get the current file's path name
      String filepath = ((FileSplit) context.getInputSplit()).getPath().toString();

      String[] row = value.toString().split(",");

      if(!row[0].contains("Date") && row[4].contains("Uber") && row.length == 6){
        // word.set (row[0] - time)
        word.set((row[0].split(" ")[0]).substring(1));
      }

      context.write(word, one);
    }

  }

  // Reducer Class
  public static class UberLyftReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

    // Set up IntWritable
    private IntWritable result = new IntWritable();

    // Reduce function.
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

      String tmp = key.toString().toLowerCase();

      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }

  }


  // Main function.
	public static void main(String[] args) throws Exception {

		// Create new config object.
		Configuration conf = new Configuration();

		/**
		 *	Execute Job.
		 */
		// Set up a new job and give it a name.
		Job job = Job.getInstance(conf, "Uber-Lyft Usage Builder");

		// When creating jar, use this class name.
		job.setJarByClass(UberLyft.class);

		// Class for mapping.
		job.setMapperClass(UberLyftMapper.class);

		// Class for combining.
		job.setCombinerClass(UberLyftReducer.class);

		// Class for reducing.
		job.setReducerClass(UberLyftReducer.class);

		// What object type is the output key.
		job.setOutputKeyClass(Text.class);

		// What object type is the output value.
		job.setOutputValueClass(IntWritable.class);

		// Set input and output paths.

		FileInputFormat.addInputPath(job, new Path(args[0]) );
		FileOutputFormat.setOutputPath(job, new Path(args[1]) );

		// Exit when done.
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
