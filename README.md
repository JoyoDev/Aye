# Aye

<img src="https://github.com/JoyoDev/Aye/raw/main/images/Aye.png" width="200">

----

Aye is an open source tool for scanning images on Kubernetes cluster. 
It uses Anchore CLI behind the scenes to get information about all images that are currently on the cluster.
It also provides Prometheus metrics for each image, so teams are aware of 
different levels of vulnerabilities found inside them.

Installation
===========================
Official docker image:
``docker pull joyodev/aye``

Configuring Aye
===========================

There are seven environment variables that can be configured.

    export ANCHORE_CLI_URL=http://myanchore.server.com:8228/v1
    export ANCHORE_CLI_USER=admin
    export ANCHORE_CLI_PASS=foobar

Anchore URL defaults to ``http://localhost:8228/v1/`` if you do not specify it.
User and password have to be exported/set explicitly.

Besides these three, you can also set values for application port, logging level, service
delay in milliseconds and detailed metrics.

    export SERVER_PORT=8080
    export LOGGING_LEVEL_ROOT=INFO
    export SERVICE_DELAY_IN_MILLISECONDS=300000
    export ENABLE_DETAILED_METRICS=true

Enabling detailed metrics will result in Aye exposing all vulnerabilities in the image in a form of``Package: package_name URL: vulnerability_url``
to Prometheus. For ``SERVICE_DELAY_IN_MILLISECONDS`` default value is
5 minutes (300 000 milliseconds) - time between loops.

How Aye works
===========================
In each iteration of the loop Aye gets all unique images that are present on the cluster
(goes over all containers inside all pods) and sends them to the Anchore Engine.
It checks status and evaluation status for every image and exposes certain metrics
the Prometheus.

If scanning fails for some reason, Aye will wait for 15 minutes
before trying to send that image to Anchore again.

Metrics of images that are no longer present are deleted automatically
by Aye. This is done by comparing the list of images from the current loop iteration
to the previous one.

Examples
===========================
Number of vulnerabilities for each severity found in the image (``aye_image_severity_vulnerabilities``):

<img src="https://github.com/JoyoDev/Aye/raw/main/images/metrics1.png">

Detailed vulnerabilities metric for image (``aye_image_vulnerability_details``):

<img src="https://github.com/JoyoDev/Aye/raw/main/images/metrics2.png">

- List of all metrics
    - ``aye_added_images_total`` (total number of total images added by Aye)
    - ``aye_failed_images_total`` (total number of images that didn't pass analysis)
    - ``aye_failed_analysis_total`` (total number of unsuccessful analysis)
    - ``aye_image_severity_vulnerabilities`` (number of vulnerabilities for each severity in the image)
    - ``aye_image_vulnerability_details`` (details for all vulnerabilities in the image) - optional, enabled by setting ``ENABLE_DETAILED_METRICS`` to ``true``.

Prometheus metrics are exposed at ``/actuator/prometheus``.

Contact
===========================
Please email us via <a href = "mailto: joyo.development@gmail.com">JoyoDev</a>