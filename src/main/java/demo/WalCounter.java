package demo;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.WALCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.WALObserver;
import org.apache.hadoop.hbase.regionserver.wal.HLogKey;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;

import com.google.common.util.concurrent.AtomicLongMap;

/**
 * Simple example demonstrating the complexities of WALObservers and their impact on HBase
 * performance. It does nothing more than maintain a counter of the number of edits that pass
 * through the system.
 */
public class WalCounter implements WALObserver {

  private static final Log LOG = LogFactory.getLog(WalCounter.class);
  private final AtomicLongMap<String> counters = AtomicLongMap.create();

  @Override
  public void start(CoprocessorEnvironment ctx) throws IOException {
    LOG.info("Coprocessor inialized. counters: " + counters);
    LOG.info("", new Exception()); // uncomment me to log invocation stack
  }

  @Override
  public void stop(CoprocessorEnvironment ctx) throws IOException {
    LOG.info("Coprocessor terminated. counters: " + counters);
    LOG.info("", new Exception()); // uncomment me to log invocation stack
  }

  @Override
  public boolean preWALWrite(ObserverContext<WALCoprocessorEnvironment> ctx, HRegionInfo info,
      HLogKey logKey, WALEdit logEdit) throws IOException {
    String r = info.getRegionNameAsString();
    LOG.info("preWALWrite triggered for " + r + ". counters: " + counters);
    return false;
  }

  @Override
  public void postWALWrite(ObserverContext<WALCoprocessorEnvironment> ctx, HRegionInfo info,
      HLogKey logKey, WALEdit logEdit) throws IOException {
    String r = info.getRegionNameAsString();
    counters.addAndGet(r, 1);
    LOG.info("postWALWrite triggered for " + r + ". counters: " + counters);
  }
}
