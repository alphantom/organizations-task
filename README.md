# Organizations-task
It is a simple REST API Spring project. 

To run the project run `mvn spring-boot:run`. Look for it on localhost:8080/

## API

| PATH                    | METHOD | REQUEST BODY        | RESPONSE BODY              |
| ----------------------- |:------:| -------------------:| --------------------------:|
| api/organization/list   | POST   | name*, inn, isActive | [{id, name, isActive},..] |
| api/organization/{id}   | GET    |                     | id, name, fullName, inn, kpp, address, phone, isActive |
| api/organization/update | POST   | id*, name*, fullName*, inn*, kpp*, address*, phone, isActive | result |
| api/organization/save   | POST   | name*, fullName*, inn*, kpp*, address*, phone, isActive | result |
| api/office/list         | POST   | orgId*, name, phone, isActive | [{id, name, isActive},..] |
| api/office/{id}         | GET    |            | id, name, address, phone, isActive |
| api/office/update       | POST   | id*, name*, address*, phone, isActive | result |
| api/office/save         | POST   | orgId*, name*, address*, phone, isActive | result |
| api/user/list           | POST   | officeId*, firstName, lastName, middleName, position, docCode, citizenshipCode | [{id, nfirstName, lastName, middleName, position},..] |
| api/user/{id}           | GET    |  | id, firstName, lastName, middleName, position, phone, docName, docNumber, docDate, citizenshipName, citizenshipCode |
| api/user/update         | POST   | id*, officeId, firstName*, lastName, middleName, position*, phone, docName, docNumber, docDate, citizenshipCode, isIdentified  | result |
| api/user/save           | POST   | officeId*, firstName*, lastName, middleName, position*, phone, docCode, docName, docNumber, docDate, citizenshipName, isIdentified  | result |
| api/documents           | GET    |   |  [{name, code},..] |
| api/countries           | GET    |   |  [{name, code},..] |

1. Attributes marked with <b>*</b> as not null. 
2. Response wrapped in "data" attribute.

## Integration tests
To execute tests run the following command: `mvn test | grep "Tests" && mvn clean` 

![Controllers and IT](controllers.png)
