## Greetings service

This is Greetings REST service application - service that provides appropriate greeting upon API request according preferred language
and depending requested time of the day. Currently supported languages are: British English, American English, Spanish and Czech.

There are two modes of operation:
	<ul>
	<li>Time sensitive</li>
	<li>Time insensitive</li>
	</ul>

Every mode has its own API call structure

**API description:**


* **URL:**

  `/api/greeting/timesensitive`
  <br>
  `/api/greeting/timeinsensitive`

* **Method:**

  `GET`
  


* **URL Params:**

  `lang=[string]`  (example values: lang=cs-CS lang=en-GB or lang=es)
  <br>
  `usersTime=[string]`  (example value: usersTime=05:01)

* **Data Params**

  None


* **Success Response:**<br /><br />
 *Code:* 200 <br />
 *Content:* `greeting text`


* **Error Response:**<br /><br />
 *Code:*  404 NOT FOUND <br />
 *Content:* `{ 	status: "NOT_FOUND",
    			timestamp: "28-03-2020 08:31:50",
    			message: "Language 'aa' not supported.",
    			debugMessage: "" }`
	
  OR<br /><br />
 *Code:*  400 BAD REQUEST <br />
 *Content:* `{ 	status: "BAD_REQUEST",
    			timestamp: "28-03-2020 08:33:23",
    			message: "Invalid value '' or missing parameter 'lang'.",
    			debugMessage: "" }`



**REST API examples for both operation modes:**
	<ul>
	<li>**Time sensitive** (for ''en-US' language): `http://localhost:8080/api/greeting/timesensitive?usersTime=17:10&lang=en-US`</li>
	<li>**Time sensitive** (for 'es' language): `http://localhost:8080/api/greeting/timesensitive?usersTime=05:01&lang=es`</li>
	<li>**Time insensitive** (for 'cs\_CS' language): `http://localhost:8080/api/greeting/timeinsensitive?lang=cs_CS`</li>
	<li>**Time insensitive** (for 'cs\_CS' language): `http://localhost:8080/api/greeting/timeinsensitive?usersTime=15:15?lang=cs_CS`</li>
	</ul>
	



If the greetings for specific new language and COUNTRY shall be introduced, then corresponding properties file has to be created and filled by desired greetings.
Properties files are needed for every allowed locale (i.e. language\_COUNTRY combination).<br>
The properties files must be saved in the **/src/main/resources** directory of the application and their names compose like following: '**messages_lang_COUNTRY.properties**', where 'lang' is the language abbrevation and 'COUNTRY' is the country abbrevation, for example: '**messages_en_GB.properties**'.<br>
If it is expected, that the 'lang' parameter of the API request can contain only language abbrevation (for example lang=es),
then corresponding properties file has to be created in form 'messages\_language.properties', for example: '**messages\_es.properties**'.



## Getting Started

Basic steps to make application runnig: 

- import like git project from GitHub into your IDE.
- run on local IDE as Java or Spring Boot application

### Prerequisites

Java 11 installed, Maven, JUnit5


### Installing

1] Cloning project from GitHub to your local Git repository:

Open Git Repositories View. Window -> Show View -> Other -> Git -> Git Repositories
In that new View click on "Clone a Git repository", fill in URI: https://github.com/MichalVaclavek/Greetings.git and click "Next". If not selected, select "master" branch, and click "Next". Select directory where the local repository will be located/cloned to, click "Finish". 

2] Importing project from local Git repository to Eclipse:
Go to File -> Import -> Maven -> Existing Maven Project -> Next, select Directory, where you have cloned the project (step 1). Click "Finish" and your project is imported into Eclipse and ready to run.


## Running the tests

Standard right click on the /src/test/java directory of the project in your Eclipse Project explorer and select Run JUnit test.
or manually type 'mvn test' within project's directory.

## Deployment

Run as Maven install from Eclipse Project Explorer or 'mvn clean install' manually within project's directory. This will create executable 'greetings-0.0.1-SNAPSHOT.jar' file (in project's /target/ directoy), which can be run as 'java -jar greetings-0.0.1-SNAPSHOT.jar &' on your production server with Java 11 installed.<br>
If production operation is requiered, set the Spring profile to **prod** by setting the variable spring.profiles.active=prod of the src/main/resource/application.properties file before compiling.<br>
The log file of the app. is created in main directory as 'app_greetings.log'. Logging can be configured in 'src/main/resources/logback-spring.xml' file.


## Built With

* [Eclipse] - IDE
* [Maven](https://maven.apache.org/) - Dependency Management
* [SpringBoot]
* [JUnit5]

## Authors

* **Michal Václavek**
