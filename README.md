## Building and running the application

Install JDK17. Gradle does not support JDK20 yet and will probably break if you try to build using it.
https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

Once installed, ensure your system's JAVA_HOME environment variable is set to the JDK install directory.
On Windows there's a good chance the install directory will be something like `C:\Program Files\Java\jdk-17XXXXXX`
To set the environment variable:
- Mac: check out https://phoenixnap.com/kb/set-environment-variable-mac
- Windows: check out https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html

Once Java is installed and the env var is set, open a terminal window at the root directory of this project.
Run the command `./gradlew bootRun` to start the application. The gradle wrapper should start, build the project, and run it.
The database within the project is embedded and will save data off to your user directory in files `atmapidb.mv.db`
and `atmapidb.trace.db`.
Schema generation occurs automatically by Spring Data JPA and no startup scripts are required.
If you want to start from scratch or remove the db data from your computer, you can delete these files. 
Kill the server with ctrl+c after it is started.


Once running, API documentation can be found at http://localhost:8080/swagger-ui/index.html
A Postman collection with some pre-configured requests can be found in the `postman` dir at the root of the project.
Import both the request collection and the local environment vars into your Postman app.
https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-postman-data

If you'd like to view the state of the database, navigate to http://localhost:8080/h2-console/. Enter the connection
properties that are located at the top of the `src/main/resources/application.properties` file to connect to it.

## About

### Overview

The ATM API has several components: accounts, sessions, and transactions.

An account must exist to create a session for it.
A session must exist to create a transaction within it.

For example, a typical flow might be something like:
- Create an account
- Start a session
- Perform some transactions
- End the session (optional)

### Account Behavior
Users do not have accounts by default. Once accounts are created, they cannot be closed (for the sake of bookkeeping).
For now, accounts can only be opened in USD, but there is the potential to open accounts in other currencies sometime
in the future.

### Session Behavior
To create a session on an account, the account must exist. A correct PIN must be provided to start a session on an
account, and from that point any transaction operation can be performed.

If implemented correctly, there should only be one active session for an account at a time. Creating a session for an
account while one is already open will extend the session to 5 minutes from the current time. Transactions do not extend
the session.

If a session is open, it can be terminated manually at any time, otherwise it will automatically terminate 5 minutes
after it is created or extended and transaction operations will be rejected. If you try to terminate a session that has
already been terminated, a bad request will be returned.

Sessions will not be deleted when closed, for the sake of auditing and bookkeeping.

### Transaction Behavior

A valid account and active session for that account must exist before performing a transaction. Positive dollar amounts
should generally go through without issue as long as there is an active session. Negative dollar amounts will be
processed as long as an overdraft will not bring the account to below -$100. Overdrafts that result in a balance >-$100 
will be processed but result in a fee processed as a separate -$15 transaction. Overdrafts that bring an account balance
below -$100 will be not be processed and a fee will not be assessed. All transactions, whether successfully applied or 
not, will be kept in the transaction log for auditing and bookkeeping purposes.


## Major Components

- Java 17
- Spring Boot
- Spring Validation
- Spring Data JPA
- Swagger UI
- Lombok
- H2 embedded database
- JUnit
