package zy.mr;

import java.io.IOException;

import com.aliyun.odps.Column;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.MapperBase;

public class DbscanMapper extends MapperBase {

	private Record key;
	private Record value;
	private Column[] keySchema;
	private Column[] valSchema;

	@Override
	public void setup(TaskContext context) throws IOException {
		key = context.createMapOutputKeyRecord();
		value = context.createMapOutputValueRecord();
		keySchema = context.getJobConf().getMapOutputKeySchema();
		valSchema = context.getJobConf().getMapOutputValueSchema();
	}

	@Override
	public void map(long recordNum, Record record, TaskContext context)
			throws IOException {
		for (int i = 0; i < keySchema.length; i++) {
			key.set(i, record.get(i));
		}
		for (int i = 0; i < valSchema.length; i++) {
			value.set(i, record.get(i));
		}

		context.write(key, value);
	}
}