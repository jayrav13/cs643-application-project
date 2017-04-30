/**
* UberLyftCombined
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

public class UberLyftCombined {

  // Mapper class
  public static class UberMapper extends Mapper<Object, Text, Text, IntWritable>{

    // IntWritable will set the context.write(...)'s value
    private final static IntWritable one = new IntWritable(1);

    // Track the current word as a Text() object
		private Text word = new Text();

    // Map function
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

      // Get the current file's path name
      String filepath = ((FileSplit) context.getInputSplit()).getPath().toString();

      String[] row = value.toString().split(",");

      if(!row[0].contains("id") && row[1].contains("Uber") && row.length == 9){
        // word.set (row[0] - time)
        word.set((row[4].split(" ")[0]));
      }

      context.write(word, one);
    }

  }

  // Reducer Class
  public static class UberReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

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

  // Mapper class
  public static class LyftMapper extends Mapper<Object, Text, Text, IntWritable>{

    // IntWritable will set the context.write(...)'s value
    private final static IntWritable one = new IntWritable(1);

    // Track the current word as a Text() object
		private Text word = new Text();

    // Map function
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

      // Get the current file's path name
      String filepath = ((FileSplit) context.getInputSplit()).getPath().toString();

      String[] row = value.toString().split(",");

      if(!row[0].contains("id") && row[1].contains("Lyft") && row.length == 9){
        // word.set (row[0] - time)
        word.set((row[4].split(" ")[0]));
      }

      context.write(word, one);
    }

  }

  // Reducer Class
  public static class LyftReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

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
		 *	Uber Job
		 */
		// Set up a new job and give it a name.
		Job job = Job.getInstance(conf, "Uber Daily Count");

		// When creating jar, use this class name.
		job.setJarByClass(UberLyftCombined.class);

		// Class for mapping.
		job.setMapperClass(UberMapper.class);

		// Class for combining.
		job.setCombinerClass(UberReducer.class);

		// Class for reducing.
		job.setReducerClass(UberReducer.class);

		// What object type is the output key.
		job.setOutputKeyClass(Text.class);

		// What object type is the output value.
		job.setOutputValueClass(IntWritable.class);

		// Set input and output paths.

		FileInputFormat.addInputPath(job, new Path(args[0]) );
		FileOutputFormat.setOutputPath(job, new Path(args[1] + "/uber") );

		// Continue to job 2
		job.waitForCompletion(true);

		/**
		 *	Lyft Job
		 */
		// Set up a new job and give it a name.
		Job job2 = Job.getInstance(conf, "Lyft Daily Count");

		// When creating jar, use this class name.
		job2.setJarByClass(UberLyftCombined.class);

		// Class for mapping.
		job2.setMapperClass(LyftMapper.class);

		// Class for combining.
		job2.setCombinerClass(LyftReducer.class);

		// Class for reducing.
		job2.setReducerClass(LyftReducer.class);

		// What object type is the output key.
		job2.setOutputKeyClass(Text.class);

		// What object type is the output value.
		job2.setOutputValueClass(IntWritable.class);

		// Set input and output paths.

		FileInputFormat.addInputPath(job2, new Path(args[0]) );
		FileOutputFormat.setOutputPath(job2, new Path(args[1] + "/lyft") );

		// Exit when done.
		System.exit(job2.waitForCompletion(true) ? 0 : 1);

	}

}
