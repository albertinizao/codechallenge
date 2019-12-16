Feature: The user wants to create a transaction

Scenario Outline: Store a transaction
	Given Transaction with reference <reference> is not stored
	When I save the transaction
   |reference 	|fee	|amount		| date 	| accountIban  | description	|
   |<reference>	|<fee>	|<amount>	|<date>	|<account_iban>| <description>	|
	Then the system returns the http-status 201
		And the transaction with reference <reference> is stored
		And the transaction stored is the same

  Examples:
   |reference	|fee	|amount	| date        				| account_iban        		| description 			|
   |12345A		|3.18	|193.38	|2019-07-16T16:55:42.000Z	|ES9820385778983000760200	| Restaurant payment	|
   |123asd45A	|4.1	|300	|2019-07-16T16:55:52.000Z	|ES9820385778983012760201	| Restaurant payment 2	|
   |12345C		|3.18	|193.38	|							|ES9820385778983000760202	| Restaurant payment	|
   |12345D		|		|193.38	|2019-07-16T16:55:42.000Z	|ES9820385778983000760203	| Restaurant payment	|
   |12345E		|3.18	|193.38	|2019-07-16T16:55:42.000Z	|ES9820385778983000760204	| 						|
   |12345E2		|		|193.38	|							|ES9820385778983000760202	| 						|

Scenario Outline: Store a transaction within reference
	Given No transaction stored
	When I save the transaction
   |fee		|amount		| date 	| accountIban	  | description	|
   |<fee>	|<amount>	|<date>	|<account_iban>	| <description>	|
	Then the system returns the http-status 201
		And one transaction is stored

  Examples:
   |fee	|amount	| date        				| account_iban        		| description 			|
   |3.18|193.38	|2019-07-16T16:55:42.000Z	|ES9820385778983000760205	| Restaurant payment	|
   |4.1	|300	|2019-07-16T16:55:52.000Z	|ES9820385778983012760206	| Restaurant payment 2	|
   
Scenario Outline: Store a transaction withoud mandatory field
	Given Transaction with reference <reference> is not stored
	When I save the transaction
   |reference 	|fee	|amount		| date 	| accountIban  | description	|
   |<reference>	|<fee>	|<amount>	|<date>	|<account_iban>| <description>	|
	Then the system returns the http-status 400
		And Transaction with reference <reference> is not stored

  Examples:
   |reference	|fee	|amount	| date        				| account_iban        		| description 			|
   |12345F		|3.18	|		|2019-07-16T16:55:42.000Z	|ES9820385778983000760207	| Restaurant payment	|
   |12345G		|3.18	|193.38	|2019-07-16T16:55:42.000Z	|							| Restaurant payment	|
   
Scenario Outline: Store a transaction duplicated
	Given Transaction with reference <reference> is stored
	When I save the transaction
   |reference 	|fee	|amount		| date 	| accountIban  | description	|
   |<reference>	|<fee>	|<amount>	|<date>	|<account_iban>| <description>	|
	Then the system returns the http-status 409
		And the transaction with reference <reference> is stored
		And the transaction stored is not modified

  Examples:
   |reference	|fee	|amount	| date        				| account_iban        		| description 			|
   |12345H		|3.18	|193.38	|2019-07-16T16:55:42.000Z	|ES9820385778983000760208	| Restaurant payment	|
   
   
Scenario Outline: Store a transaction with total balance lt 0
	Given Transaction with reference <reference> is not stored
		And Transaction is stored
   |reference 	|fee	|amount		| date 						| accountIban  | description	|
   |1AI			|0.5	|5			| 2019-06-16T16:55:42.000Z	|<account_iban>| <description>1	|
		And Transaction is stored
   |reference 	|fee	|amount		| date 						| accountIban  | description	|
   |1BI			|1.5	|-25		| 2019-05-16T16:55:42.000Z	|<account_iban>| <description>2	|
	When I save the transaction
   |reference 	|fee	|amount		| date 	| accountIban  | description	|
   |<reference>	|<fee>	|<amount>	|<date>	|<account_iban>| <description>	|
	Then the system returns the http-status 400
		And Transaction with reference <reference> is not stored

  Examples:
   |reference	|fee	|amount	| date        				| account_iban        		| description 			|
   |12345I		|0.1	|1		|2019-07-16T16:55:42.000Z	|ES9820385778983000760209	| Restaurant payment	|
   
   
Scenario Outline: Store a transaction with total balance gt 0
	Given Transaction with reference <reference> is not stored
		And Transaction is stored
   |reference 	|fee	|amount		| date 						| accountIban  | description	|
   |1AJ			|0.5	|5			| 2019-06-16T16:55:42.000Z	|<account_iban>| <description>1	|
		And Transaction is stored
   |reference 	|fee	|amount		| date 						| accountIban  | description	|
   |1BJ			|1.5	|25			| 2019-05-16T16:55:42.000Z	|<account_iban>| <description>2	|
	When I save the transaction
   |reference 	|fee	|amount		| date 	| accountIban  | description	|
   |<reference>	|<fee>	|<amount>	|<date>	|<account_iban>| <description>	|
	Then the system returns the http-status 201
		And the transaction with reference <reference> is stored
		And the transaction stored is the same

  Examples:
   |reference	|fee	|amount	| date        				| account_iban        		| description 			|
   |12345J		|1.9	|26		|2019-07-16T16:55:42.000Z	|ES9820385778983000760210	| Restaurant payment	|
   
   
Scenario Outline: Store a transaction with total balance eq 0
	Given Transaction with reference <reference> is not stored
		And Transaction is stored
   |reference 	|fee	|amount		| date 						| accountIban  | description	|
   |1AJ			|0.5	|5			| 2019-06-16T16:55:42.000Z	|<account_iban>| <description>1	|
		And Transaction is stored
   |reference 	|fee	|amount		| date 						| accountIban  | description	|
   |1BJ			|1.5	|25			| 2019-05-16T16:55:42.000Z	|<account_iban>| <description>2	|
	When I save the transaction
   |reference 	|fee	|amount		| date 	| accountIban  | description	|
   |<reference>	|<fee>	|<amount>	|<date>	|<account_iban>| <description>	|
	Then the system returns the http-status 201
		And the transaction with reference <reference> is stored
		And the transaction stored is the same

  Examples:
   |reference	|fee	|amount	| date        				| account_iban        		| description 			|
   |12345J		|		|-28	|2019-07-16T16:55:42.000Z	|ES9820385778983000760210	| Restaurant payment	|