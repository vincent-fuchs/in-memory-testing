Feature: Customers can ask to be upgraded. We'll receive their request and depending on their existing number of commands, their loyalty status will be defined

  Scenario: Different loyalty status is assigned for customer who request it, based on their command history. If customer has 0 command, he/she can't be upgraded.
    #queue check
    Given we receive status upgrade requests for these customers
      | name    | email             |
      | Alice   | alice@gmail.com   |
      | Bob     | bob@hotmail.com   |
      | Praveen | praveen@yahoo.com |
    #REST webservice check
    And command history for users is as followed
      | Alice   | 5  |
      | Bob     | 10 |
      | Praveen | 0  |
    When customer upgrade batch gets the upgrade requests and we wait "2" seconds
    # DB check
    Then customer loyalty repository gets updated with
      | Alice | Silver |
      | Bob   | Gold   |
    # email check
    And emails are sent to people with following subject
      | recipient            | subject                                      |
      | alice@gmail.com      | you have just been upgraded to Silver status |
      | bob@hotmail.com      | you have just been upgraded to Gold status   |
      | support@my-store.com | invalid upgrade request for customer Praveen |
