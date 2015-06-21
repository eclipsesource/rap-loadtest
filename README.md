# Load Tests for RAP

This repository contains code used to run load tests against a [RAP](http://www.eclipse.org/rap/) application.

## Test Application

The folder [org.eclipse.rap.loadtest.demo](org.eclipse.rap.loadtest.demo) contains a simple application that creates a large number of widgets. The application is deployed as a *standalone* WAR, i.e. without OSGi. The war file can be created using `mvn clean package`.

## Gatling scripts

The folder [org.eclipse.rap.loadtest.gatling](org.eclipse.rap.loadtest.gatling) contains [Gatling](http://gatling.io/)  classes used to issue requests at a high rate.
