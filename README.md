# HBase WAL Observer Example

An example WALObserver.

## usage

Build:

    $ mvn clean compile

hbase-env.sh:

    # Extra Java CLASSPATH elements.  Optional.
    export HBASE_CLASSPATH=/Users/ndimiduk/tmp/wal-example/target/wal-example-0.1.0-SNAPSHOT.jar

hbase-site.xml:

    <property>
      <name>hbase.coprocessor.wal.classes</name>
      <value>demo.WalCounter</value>
    </property>

run it:

    $ ./bin/start-hbase.sh

    $ tail -f logs/hbase-ndimiduk-master-soleil.local.log | grep demo
    2013-09-11 22:47:41,053 INFO demo.WalCounter: Coprocessor inialized. counters: {}
    2013-09-11 22:47:41,055 INFO org.apache.hadoop.hbase.coprocessor.CoprocessorHost: System coprocessor demo.WalCounter was loaded successfully with priority (536870911).
    2013-09-11 22:47:51,170 INFO demo.WalCounter: preWALWrite triggered for -ROOT-,,0.70236052. counters: {}
    2013-09-11 22:47:51,171 INFO demo.WalCounter: postWALWrite triggered for -ROOT-,,0.70236052. counters: {-ROOT-,,0.70236052=1}
    2013-09-11 22:47:53,405 INFO demo.WalCounter: preWALWrite triggered for .META.,,1.1028785192. counters: {-ROOT-,,0.70236052=1}
    2013-09-11 22:47:53,405 INFO demo.WalCounter: postWALWrite triggered for .META.,,1.1028785192. counters: {-ROOT-,,0.70236052=1, .META.,,1.1028785192=1}

    $ echo "put 'foo', 'bar', 'f1:bub', 'blah'" | ./bin/hbase shell

    2013-09-11 22:50:10,777 INFO demo.WalCounter: preWALWrite triggered for foo,,1378957206037.1547fbc711229d5e55e0dd9010c42e85.. counters: {-ROOT-,,0.70236052=1, .META.,,1.1028785192=1}
    2013-09-11 22:50:10,777 INFO demo.WalCounter: postWALWrite triggered for foo,,1378957206037.1547fbc711229d5e55e0dd9010c42e85.. counters: {-ROOT-,,0.70236052=1, foo,,1378957206037.1547fbc711229d5e55e0dd9010c42e85.=1, .META.,,1.1028785192=1}

    $ ./bin/stop-hbase.sh

...?

## License

Copyright (C) 2013 Nick Dimiduk

Distributed under the [Apache License, version 2.0][0], the same as HBase.

[0]: http://www.apache.org/licenses/LICENSE-2.0.html
