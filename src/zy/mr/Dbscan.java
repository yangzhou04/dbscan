package zy.mr;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import com.aliyun.odps.Column;
import com.aliyun.odps.Odps;
import com.aliyun.odps.OdpsType;
import com.aliyun.odps.TableSchema;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.RunningJob;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.conf.SessionState;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;

import zy.util.CommonUtils;
import zy.util.Flag;

public class Dbscan {
	
	public static void main(String[] args) throws Exception {
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(constructOptions(new Options()), args);

		String in = cmd.getOptionValue(Flag.I);
		String out = cmd.getOptionValue(Flag.O);
		String f = cmd.getOptionValue(Flag.F);
		String g = cmd.getOptionValue(Flag.G);
		String pt = cmd.getOptionValue(Flag.P);
		String eps = cmd.getOptionValue(Flag.E);
		String minPts = cmd.getOptionValue(Flag.M);
		String ignore = cmd.getOptionValue(Flag.N);

		if (in == null || out == null || f == null || g == null) {
			System.out.println("in = " + in);
			System.out.println("out = " + out);
			System.out.println("f = " + f);
			System.out.println("g = " + g);
			printUsage();
			System.exit(-1);
		}

		Odps odps = SessionState.get().getOdps();
		TableSchema ts = odps.tables().get(in).getSchema();
		List<Column> columns = ts.getColumns();
		
		TableSchema outputTs = odps.tables().get(in).getSchema();
		outputTs.addColumn(new Column("cluster_id", OdpsType.BIGINT));
		odps.tables().create(out, outputTs);
		
		JobConf job = new JobConf();
		job.setMapperClass(DbscanMapper.class);
		job.setReducerClass(DbscanReducer.class);
		job.setMemoryForReduceTask(4*1024);
		job.setNumReduceTasks(40);

		List<Integer> groupIndex = CommonUtils.getColumnIndex(g);
		Column[] keySchema = new Column[groupIndex.size()];
		for (int i = 0; i< groupIndex.size(); i++) {
			keySchema[i] = columns.get(groupIndex.get(i));
		}
		Column[] valSchema = new Column[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			valSchema[i] = columns.get(i);
		}
		job.setMapOutputKeySchema(keySchema);
		job.setMapOutputValueSchema(valSchema);
		
		job.set(Flag.F, f);
		System.out.println("Flag.F = " + f);
		job.setFloat(Flag.E, eps != null ? Float.parseFloat(eps) : 0.02f);
		job.setInt(Flag.M, minPts != null ? Integer.parseInt(minPts) : 2);
		job.setInt(Flag.N, ignore != null ? Integer.parseInt(ignore) : Integer.MAX_VALUE);

		if (pt != null) {
			InputUtils.addTable(TableInfo.builder().tableName(in).partSpec("dt="+pt).build(), job);
			OutputUtils.addTable(TableInfo.builder().tableName(out).partSpec("dt="+pt).build(), job);
		} else {
			InputUtils.addTable(TableInfo.builder().tableName(in).build(), job);
			OutputUtils.addTable(TableInfo.builder().tableName(out).build(), job);
		}

		RunningJob rj = JobClient.runJob(job);
		rj.waitForCompletion();
	}

	private static Options constructOptions(Options options) {
		// ODPS tables related options
		options.addOption(Flag.I, true, "input table name");
		options.addOption(Flag.O, true, "output table name");
		options.addOption(Flag.P, true, "partition");
		options.addOption(Flag.G, true, "group by");
		options.addOption(Flag.F, true, "feature column index");
		// model related options
		options.addOption(Flag.E, true, "epsilon");
		options.addOption(Flag.M, true, "min points count");
		options.addOption(Flag.N, true, "maximum instance number");
		
		return options;
	}
	
	private static void printUsage() {
		System.out.println("Usage: ");
		
	}
	
}
