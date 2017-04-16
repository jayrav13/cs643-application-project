/**
 *	Peak
 *
 *	Determine peak Uber and FHV usage.
 */

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

public class Peak {

	// Mapper Class
	public static class PeakDataMapper extends Mapper<Object, Text, Text, IntWritable>{

		// IntWritable will set the context.write(...)'s value.
		private final static IntWritable one = new IntWritable(1);

		// Track the current word as a Text() object.
		private Text word = new Text();

		// Map function
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

/*
			// Convert value to StringTokenizer.
			StringTokenizer itr = new StringTokenizer(value.toString());

			// Iterate through StringTokenizer.
			while (itr.hasMoreTokens()) {

				context.write(new Text(filepath + "/" + word.toString()), one);

			}
*/

			// Get the current file's path name.
			String filepath = ((FileSplit) context.getInputSplit()).getPath().toString();

			StringTokenizer itr = new StringTokenizer(value.toString(), ",");

			while(itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				break;
			}

			context.write(word, one);

		}

	}

	// Reducer Class
	public static class PeakDataReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

		// Set up IntWritable
		private IntWritable result = new IntWritable();

		// Reduce function.
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

			String tmp = key.toString().toLowerCase();

			int sum = 1;
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
		Job job = Job.getInstance(conf, "Peak Data Builder");

		// When creating jar, use this class name.
		job.setJarByClass(Peak.class);

		// Class for mapping.
		job.setMapperClass(PeakDataMapper.class);

		// Class for combining.
		job.setCombinerClass(PeakDataReducer.class);

		// Class for reducing.
		job.setReducerClass(PeakDataReducer.class);

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
