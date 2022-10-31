       _____   __  __   _______ 
      / ____| |  \/  | |__   __|
     | |  __  | \  / |    | |   
     | | |_ | | |\/| |    | |   
     | |__| | | |  | |    | |   
      \_____| |_|  |_|    |_|   
     ---------------------------  
         Gateway Musala Test   
---

**Conditions**

You have to prepare a solution to the proposed problem in the defined period of time. The solution must comply with the requirements 

Once ready, you must send a package with the source code of the solution, so it can be built and reviewed by Chilterra Team. Instructions how to use the solution must also be provided (resource names, SQL scripts to import test data, other scripts, etc.).

**Software Requirements**

Programming language: Java 

Framework: Spring Boot

Database: Postgres

Automated build: Gradle

**Description**

This sample project is managing gateways - master devices that control multiple peripheral devices.
 
Your task is to **create a REST service** (JSON/HTTP) for storing information about these gateways and their associated devices. This information must be stored in the database. 

When storing a gateway, any field marked as “to be validated” must be validated and an error returned if it is invalid. Also, no more that 10 peripheral devices are allowed for a gateway.

The service must also offer an operation for displaying information about all stored gateways (and their devices) and an operation for displaying details for a single gateway. Finally, it must be possible to add and remove a device from a gateway.

Each gateway has:

* a unique serial number (string), 
* human-readable name (string),
* IPv4 address (to be validated),
* multiple associated peripheral devices. 

Each peripheral device has:

* a UID (number),
* vendor (string),
* date created,
* status - online/offline.

**Other considerations**

**No User Interface is necessary for the solution.**

Provide an automated build.

No unit test.
