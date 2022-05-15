# Aye

<img src="https://github.com/JoyoDev/Aye/raw/main/images/Aye.png" width="200">

----

Aye is an open source tool for scanning images on Kubernetes cluster. 
It uses Anchore CLI behind the scenes to get information about all images that are currently on the cluster.
It also provides Prometheus metrics for each image, so teams are aware of 
different levels of vulnerabilities found inside them.

Configuring Aye
===========================

There are seven environment variables that can be configured.

.. code::

    export ANCHORE_CLI_URL=http://myanchore.server.com:8228/v1
    export ANCHORE_CLI_USER=admin
    export ANCHORE_CLI_PASS=foobar

Anchore URL defaults to http://localhost:8228/v1/ if you do not specify it.
User and password have to be exported/set explicitly.

Besides these three, you can also set values for application port, logging level, service
delay in milliseconds and detailed metrics.

.. code::

    export SERVER_PORT=8080
    export LOGGING_LEVEL_ROOT=INFO
    export SERVICE_DELAY_IN_MILLISECONDS=300000
    export ENABLE_DETAILED_METRICS=true
Enabling detailed metrics will result in Aye exposing all vulnerabilities in the image in a form of "Package: package_name URL: vulnerability_url"
to Prometheus.