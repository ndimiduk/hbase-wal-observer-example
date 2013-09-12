/*
 *
 * Copyright 2013 Nick Dimiduk
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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
