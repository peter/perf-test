# Performance Test for Different API Stacks

## Test Case: One JSON HTTP request

The server makes a GET HTTP request to fetch a small piece of JSON that is deserialized/serialized and
served back to the client.

## Hardware

Results below are from my Macbook Air.

## Load Testing Tool

I had [problems](http://superuser.com/questions/323840/apache-bench-test-error-on-os-x-apr-socket-recv-connection-reset-by-peer-54) using Apache Bench so I have been using [Taurus](http://gettaurus.org/docs/Home) instead. I've also looked at [loadtest](https://github.com/alexfernandez/loadtest) as an alternative.

## Serving Static JSON

The files under static are served with nginx.

```
bzt bzt-static.yml -o execution.concurrency=600

14:17:40 INFO: Test duration: 0:01:24
14:17:40 INFO: Samples count: 418864, 0.45% failures
14:17:40 INFO: Average times: total 0.085, latency 0.040, connect 0.007
14:17:40 INFO: Percentile 0.0%: 0.000
14:17:40 INFO: Percentile 50.0%: 0.015
14:17:40 INFO: Percentile 90.0%: 0.029
14:17:40 INFO: Percentile 95.0%: 0.045
14:17:40 INFO: Percentile 99.0%: 1.734
14:17:40 INFO: Percentile 99.9%: 10.169
14:17:40 INFO: Percentile 100.0%: 15.940
```

## TODO/Questions

* Does the test case make sense? Should we also try for example 3 parallel JSON requests from the server?
* Are the different stacks properly configured?
* What is an appropriate concurrency level for benchmarking? Currently 600.
* What is an appropriate timeout? Currently using 10 seconds.
* For how long should the test run? Currently 1 minute.

## Resources

* [Framework Benchmarks from TechEmpower](https://www.techempower.com/benchmarks)
* [Clojure Web Server Benchmarks](https://github.com/ptaoussanis/clojure-web-server-benchmarks)

## API Stack: clojure/jetty

Uses ring-jetty-adapter, clj-http, and cheshire libraries.

Concurrency 600:

```
bzt bzt.yml -o execution.concurrency=600

14:19:37 INFO: Test duration: 0:01:12
14:19:37 INFO: Samples count: 32489, 7.39% failures
14:19:37 INFO: Average times: total 1.205, latency 0.448, connect 0.018
14:19:37 INFO: Percentile 0.0%: 0.002
14:19:37 INFO: Percentile 50.0%: 0.467
14:19:37 INFO: Percentile 90.0%: 1.678
14:19:37 INFO: Percentile 95.0%: 10.002
14:19:37 INFO: Percentile 99.0%: 10.013
14:19:37 INFO: Percentile 99.9%: 10.117
14:19:37 INFO: Percentile 100.0%: 10.148
```

Concurrency 100:

```
bzt bzt.yml -o execution.concurrency=100

14:06:52 INFO: Test duration: 0:01:07
14:06:52 INFO: Samples count: 32445, 0.62% failures
14:06:52 INFO: Average times: total 0.197, latency 0.135, connect 0.000
14:06:52 INFO: Percentile 0.0%: 0.001
14:06:52 INFO: Percentile 50.0%: 0.057
14:06:52 INFO: Percentile 90.0%: 0.113
14:06:52 INFO: Percentile 95.0%: 0.175
14:06:52 INFO: Percentile 99.0%: 9.634
14:06:52 INFO: Percentile 99.9%: 10.006
14:06:52 INFO: Percentile 100.0%: 10.013
```

Concurrency 1:

```
bzt bzt.yml -o execution.concurrency=1

14:08:33 INFO: Test duration: 0:01:02
14:08:33 INFO: Samples count: 29528, 0.00% failures
14:08:33 INFO: Average times: total 0.002, latency 0.002, connect 0.000
14:08:33 INFO: Percentile 0.0%: 0.000
14:08:33 INFO: Percentile 50.0%: 0.001
14:08:33 INFO: Percentile 90.0%: 0.002
14:08:33 INFO: Percentile 95.0%: 0.002
14:08:33 INFO: Percentile 99.0%: 0.003
14:08:33 INFO: Percentile 99.9%: 0.026
14:08:33 INFO: Percentile 100.0%: 6.719
```

The java process grew to a size of around 800 MB.

## API Stack: clojure/http-kit

```
bzt bzt.yml -o execution.concurrency=600

16:39:40 INFO: Test duration: 0:01:11
16:39:40 INFO: Samples count: 165851, 0.00% failures
16:39:40 INFO: Average times: total 0.212, latency 0.207, connect 0.005
16:39:40 INFO: Percentile 0.0%: 0.009
16:39:40 INFO: Percentile 50.0%: 0.170
16:39:40 INFO: Percentile 90.0%: 0.283
16:39:40 INFO: Percentile 95.0%: 0.380
16:39:40 INFO: Percentile 99.0%: 0.884
16:39:40 INFO: Percentile 99.9%: 2.470
16:39:40 INFO: Percentile 100.0%: 3.406
```

## API Stack: clojure/immutant

```
bzt bzt.yml -o execution.concurrency=600

19:29:49 INFO: Test duration: 0:01:05
19:29:49 INFO: Samples count: 32208, 5.59% failures
19:29:49 INFO: Average times: total 1.112, latency 0.520, connect 0.032
19:29:49 INFO: Percentile 0.0%: 0.015
19:29:49 INFO: Percentile 50.0%: 0.376
19:29:49 INFO: Percentile 90.0%: 1.232
19:29:49 INFO: Percentile 95.0%: 10.001
19:29:49 INFO: Percentile 99.0%: 10.006
19:29:49 INFO: Percentile 99.9%: 10.283
19:29:49 INFO: Percentile 100.0%: 10.300
```

## API Stack: clojure/aleph

```
bzt bzt.yml -o execution.concurrency=600

23:01:40 INFO: Test duration: 0:01:33
23:01:40 INFO: Samples count: 379320, 99.76% failures
23:01:40 INFO: Average times: total 0.084, latency 0.069, connect 0.002
23:01:40 INFO: Percentile 0.0%: 0.000
23:01:40 INFO: Percentile 50.0%: 0.014
23:01:40 INFO: Percentile 90.0%: 0.264
23:01:40 INFO: Percentile 95.0%: 0.385
23:01:40 INFO: Percentile 99.0%: 0.641
23:01:40 INFO: Percentile 99.9%: 10.002
23:01:40 INFO: Percentile 100.0%: 12.438

SEVERE: error in deferred handler
java.util.concurrent.RejectedExecutionException
	at io.aleph.dirigiste.Executor.execute(Executor.java:332)
	at manifold.deferred.Deferred$fn__1108.invoke(deferred.clj:396)
	at manifold.deferred.Deferred.success(deferred.clj:396)
	at manifold.deferred$success_BANG_.invokeStatic(deferred.clj:243)
	at manifold.deferred$success_BANG_.invoke(deferred.clj:240)
	at manifold.deferred$eval1262$chain_SINGLEQUOTE____1283.invoke(deferred.clj:737)
	at manifold.deferred$eval1262$chain_SINGLEQUOTE____1283.invoke(deferred.clj:752)
	at manifold.deferred$eval1262$subscribe__1263$fn__1268.invoke(deferred.clj:716)
	at manifold.deferred.Listener.onSuccess(deferred.clj:219)
	at manifold.deferred.Deferred$fn__1116$fn__1117.invoke(deferred.clj:398)
	at clojure.lang.AFn.run(AFn.java:22)
	at io.aleph.dirigiste.Executor$3.run(Executor.java:308)
	at io.aleph.dirigiste.Executor$Worker$1.run(Executor.java:62)
	at manifold.executor$thread_factory$reify__511$f__512.invoke(executor.clj:36)
	at clojure.lang.AFn.run(AFn.java:22)
	at java.lang.Thread.run(Thread.java:745)
```

```
bzt bzt.yml -o execution.concurrency=100

23:03:32 INFO: Test duration: 0:01:08
23:03:32 INFO: Samples count: 140733, 0.00% failures
23:03:32 INFO: Average times: total 0.042, latency 0.042, connect 0.000
23:03:32 INFO: Percentile 0.0%: 0.005
23:03:32 INFO: Percentile 50.0%: 0.038
23:03:32 INFO: Percentile 90.0%: 0.054
23:03:32 INFO: Percentile 95.0%: 0.069
23:03:32 INFO: Percentile 99.0%: 0.124
23:03:32 INFO: Percentile 99.9%: 0.273
23:03:32 INFO: Percentile 100.0%: 0.592
```

```
bzt bzt.yml -o execution.concurrency=1

22:59:20 INFO: Test duration: 0:01:03
22:59:20 INFO: Samples count: 59410, 0.00% failures
22:59:20 INFO: Average times: total 0.001, latency 0.001, connect 0.000
22:59:20 INFO: Percentile 0.0%: 0.000
22:59:20 INFO: Percentile 50.0%: 0.001
22:59:20 INFO: Percentile 90.0%: 0.001
22:59:20 INFO: Percentile 95.0%: 0.002
22:59:20 INFO: Percentile 99.0%: 0.002
22:59:20 INFO: Percentile 99.9%: 0.009
22:59:20 INFO: Percentile 100.0%: 0.109
```

## API Stack: Ruby on Rails

Using thin web server:

```
bzt bzt.yml -o execution.concurrency=600

15:11:58 INFO: Test duration: 0:01:10
15:11:58 INFO: Samples count: 5719, 50.38% failures
15:11:58 INFO: Average times: total 6.496, latency 1.364, connect 0.231
15:11:58 INFO: Percentile 0.0%: 0.000
15:11:58 INFO: Percentile 50.0%: 8.004
15:11:58 INFO: Percentile 90.0%: 10.227
15:11:58 INFO: Percentile 95.0%: 12.652
15:11:58 INFO: Percentile 99.0%: 17.343
15:11:58 INFO: Percentile 99.9%: 18.634
15:11:58 INFO: Percentile 100.0%: 18.980
```

```
bzt bzt.yml -o execution.concurrency=300

14:56:46 INFO: Test duration: 0:01:06
14:56:46 INFO: Samples count: 7237, 18.86% failures
14:56:46 INFO: Average times: total 2.504, latency 1.004, connect 0.365
14:56:46 INFO: Percentile 0.0%: 0.000
14:56:46 INFO: Percentile 50.0%: 1.350
14:56:46 INFO: Percentile 90.0%: 9.544
14:56:46 INFO: Percentile 95.0%: 10.004
14:56:46 INFO: Percentile 99.0%: 10.210
14:56:46 INFO: Percentile 99.9%: 10.783
14:56:46 INFO: Percentile 100.0%: 12.818
```

## API Stack: Elixir/Phoenix

```
bzt bzt.yml -o execution.concurrency=600

15:32:31 INFO: Test duration: 0:01:04
15:32:31 INFO: Samples count: 36792, 28.77% failures
15:32:31 INFO: Average times: total 0.957, latency 0.944, connect 0.012
15:32:31 INFO: Percentile 0.0%: 0.000
15:32:31 INFO: Percentile 50.0%: 0.883
15:32:31 INFO: Percentile 90.0%: 1.886
15:32:31 INFO: Percentile 95.0%: 2.280
15:32:31 INFO: Percentile 99.0%: 2.744
15:32:31 INFO: Percentile 99.9%: 3.744
15:32:31 INFO: Percentile 100.0%: 4.079
```

## API Stack: Node.js/Express

```
bzt bzt.yml -o execution.concurrency=600

16:09:36 INFO: Test duration: 0:01:05
16:09:36 INFO: Samples count: 30266, 11.49% failures
16:09:36 INFO: Average times: total 1.178, latency 0.847, connect 0.016
16:09:36 INFO: Percentile 0.0%: 0.001
16:09:36 INFO: Percentile 50.0%: 0.721
16:09:36 INFO: Percentile 90.0%: 1.670
16:09:36 INFO: Percentile 95.0%: 3.860
16:09:36 INFO: Percentile 99.0%: 10.005
16:09:36 INFO: Percentile 99.9%: 10.250
16:09:36 INFO: Percentile 100.0%: 11.347
```
