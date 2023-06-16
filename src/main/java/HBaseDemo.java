import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by leibiao on 2017/3/6.
 */
public class HBaseDemo {

    private static final String TABLE_NAME = "mytable";
    private static final String CF_DEFAULT = "cf";
    public static final byte[] QUALIFIER = "col1".getBytes();
    private static final byte[] ROWKEY = "rowkey1".getBytes();

    public static void main(String[] args) {
        Configuration config = HBaseConfiguration.create();
//        String zkAddress = "hb-bp1f5xxxx48a0r17i-001.hbase.rds.aliyuncs.com:2181,hb-bp1f5xxxx48a0r17i-002.hbase.rds.aliyuncs.com:2181,hb-bp1f5xxxx48a0r17i-003.hbase.rds.aliyuncs.com:2181";
        String zkAddress = "47.99.129.175:2181";
//        String zkAddress = "hb-bp1ksg73091y6s3in-001.hbase.rds.aliyuncs.com:16020";
        config.set(HConstants.ZOOKEEPER_QUORUM, zkAddress);

        Connection connection = null;

        try {
            connection = ConnectionFactory.createConnection(config);

            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
            tableDescriptor.addFamily(new HColumnDescriptor(CF_DEFAULT));
            System.out.print("Creating table. ");
            Admin admin = connection.getAdmin();
            System.out.println();
            admin.createTable(tableDescriptor);
            System.out.println(" Done.");
            Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
            try {
                Put put = new Put(ROWKEY);
                put.addColumn(CF_DEFAULT.getBytes(), QUALIFIER, "this is value".getBytes());
                table.put(put);
                Get get = new Get(ROWKEY);
                Result r = table.get(get);
                byte[] b = r.getValue(CF_DEFAULT.getBytes(), QUALIFIER);  // returns current version of value
                System.out.println(new String(b));
            } finally {
                if (table != null) {table.close();}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
