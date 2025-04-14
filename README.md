# READ ME

### Build project
* Run `mvn clean install` to build project in terminal.
* Run `mvn test` for unit tests

### Third party API
* https://openexchangerates.org/api/latest.json?app_id=your-app-id&base=%s

### Testing code
* In Postman, import request by pasting the following cURL ```curl --location 'http://localhost:8080/api/calculate' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=CED6AC11A966B1CCE4E0049FE60D15A2' \
--data '{
  "customer": {
    "userType": "EMPLOYEE",
    "tenure": 3.0
  },
  "items": [
    {
      "name": "Pencil",
      "quantity": 10,
      "category": "OTHER",
      "amount": 200.00
    }
  ],
  "targetCurrency": "EUR",
  "originalCurrency": "USD",
  "totalAmount": 200.00
}'```
* Different scenarios can be tested with this.


### Run SonarQube
* Start up docker desktop
* Run command in terminal `docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest`
* Login to `http://localhost:9000/account`
* Go to `http://localhost:9000/account/security` to generate a **User token**
* run command ```mvn clean verify sonar:sonar -Dsonar.projectKey=currency-converter -Dsonar.projectName='currency converter' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=squ_007232ddc4b1835ba44c0cf54a7fda0ca0c56cf7```
* See summary report `http://localhost:9000/dashboard?id=currency-converter`
