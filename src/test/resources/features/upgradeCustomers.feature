Feature: Customers can ask to be upgraded. We'll receive their request and depending on their existing number of commands, their loyalty status will be defined

  Scenario: Different loyalty status is assigned for customer who request it, based on their command history. If customer has 0 command, he/she can't be upgraded.
    Given we receive status upgrade requests for these customers
      | name    | email             |
      | Alice   | alice@gmail.com   |
      | Bob     | bob@hotmail.com   |
      | Praveen | praveen@yahoo.com |
    And command history for users is as followed
      | Alice   | 5          |
      | Bob     | 10         |
      | Praveen | 0          |
    When customer upgrade batch gets the upgrade requests
    Then customer localty repository gets updated with
      | customerName | loyaltyStatus |
      | Alice        | silver        |
      | Bob          | gold          |
    And an email is sent to "alice@gmail.com" with subject "you have just been upgraded to silver status"
    And an email is sent to "bob@hotmail.com" with subject "you have just been upgraded to gold status"
    And an email is sent to "support@my-store.com" with subject "invalid upgrade request for customer Praveen"
