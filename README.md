![](https://img.shields.io/badge/STATUS-NOT%20CURRENTLY%20MAINTAINED-red.svg?longCache=true&style=flat)

# Important Notice
We have decided to stop the maintenance of this public GitHub repository.

Sample OData Delta-Token-Enabled Web Service
=====

Installation
------------

>Note: This project is referenced in a series of SAP Community Network blog articles ([link](http://scn.sap.com/community/developer-center/mobility-platform/blog/2015/01/14/build-an-optimized-hybrid-mobile-application-using-the-hana-cloud-platform)).  If you are interested in this code for some other purpose, you can follow the steps outlined below to build and run the project.

__Step 1: Clone this github repository__

__Step 2: Install Maven 3.0.3+__

[Download from here](http://maven.apache.org/download.html)

__Step 3: Ensure maven binaries are on your PATH (ie. you can run `mvn` from anywhere)__

Follow the Maven installation instructions from [here](http://maven.apache.org/download.html#Installation).

__Step 4: CD to the root directory of this project (where this README.md lives)__

__Step 5: Install the third party dependencies into your local repository__

* On all platforms:

		mvn clean install

__Step 6: Install the third party dependencies into your local repository__

__Step 7: Install the built ESPM_V1.war archive in a Tomcat application container__

__Step 8: Naviagte to the web service URL:__

		http://localhost:<tomcat_http_port>/ESPM_V1/api/
