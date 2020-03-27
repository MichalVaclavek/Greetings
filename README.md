## Greetings

This is Greetings REST service application - service that provides appropriate greeting upon API request according preferred language
and depending requested time of the day.

## Getting Started

Basic steps to make application runnig: 

- import like git project from GitHub into your IDE.
- run on local IDE as Java or Spring Boot application

### Prerequisites

Java 11 installed, Maven, JUnit5

### Installing

1] Cloning project from GitHub to your local Git repository:

Open Git Repositories View. Window -> Show View -> Other -> Git -> Git Repositories
In that new View click on "Clone a Git repository", fill in URI: https://github.com/MichalVaclavek/Greetings and click "Next". If not selected, select "master" branch, and click "Next". Select directory where the local repository will be located/cloned to, click "Finish". 

2] Importing project from local Git repository to Eclipse:
Go to File -> Import -> Maven -> Existing Maven Project -> Next, select Directory, where you have cloned the project (step 1). Click "Finish" and your project is imported into Eclipse and ready to run.


## Running the tests

Standard right click on the /src/test/java directory of the project in your Eclipse Project explorer and select Run jUnit test.
or 'mvn test' in the project's directory.

## Deployment

Run 'mvn clean install' from Eclipse or the project's directory. This will create executable greetings-0.0.1-SNAPSHOT.jar file, which can be run
as 'java -jar greetings-0.0.1-SNAPSHOT.jar &' on your production server with Java 11 installed. 

## Built With

* [Eclipse] - IDE
* [Maven](https://maven.apache.org/) - Dependency Management
* [SpringBoot]
* [JUnit5]

## Authors

* **Michal Václavek**
