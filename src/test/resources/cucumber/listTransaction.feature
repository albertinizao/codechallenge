Feature: The user wants to list the transactions

Scenario: Empty list
	Given No transaction stored
	When I list the transactions
	Then the system returns the http-status 200
		And the transaction list is empty

Scenario: Empty list with account_iban
	Given No transaction stored
	When I list the transactions with account_iban ES9820385778983000760200
	Then the system returns the http-status 200
		And the transaction list is empty

Scenario: Empty list with sort
	Given No transaction stored
	When I list the transactions with sort asc
	Then the system returns the http-status 200
		And the transaction list is empty

Scenario: Empty list with account_iban and sort
	Given No transaction stored
	When I list the transactions with account_iban ES9820385778983000760200 and sort desc
	Then the system returns the http-status 200
		And the transaction list is empty

Scenario: Empty list with bad account_iban
	Given No transaction stored
	When I list the transactions with account_iban XXXX
	Then the system returns the http-status 400

Scenario: Empty list with bad sort
	Given No transaction stored
	When I list the transactions with sort XXXX
	Then the system returns the http-status 400

Scenario: Empty list with bad account_iban and bad sort
	Given No transaction stored
	When I list the transactions with account_iban XXX and sort YYY
	Then the system returns the http-status 400
	
Scenario: Get list with elements filter by iban
	Given No transaction stored
		And Multiple transactions persisted with account_iban ES9820385778983000760010
	When I list the transactions with account_iban ES9820385778983000760010
	Then the system returns the http-status 200
		And the transaction list has the elements
	
Scenario: Get list with elements filter by iban sorted asc
	Given No transaction stored 
		And Multiple transactions persisted with account_iban ES9820385778983000760011
	When I list the transactions with account_iban ES9820385778983000760011 and sort asc
	Then the system returns the http-status 200
		And the transaction list has the elements
		And the transation list is ordered asc
	
Scenario: Get list with elements filter by iban sorted desc
	Given No transaction stored 
		And Multiple transactions persisted with account_iban ES9820385778983000760012
	When I list the transactions with account_iban ES9820385778983000760012 and sort desc
	Then the system returns the http-status 200
		And the transaction list has the elements
		And the transation list is ordered desc
	
Scenario: Get list with elements
	Given No transaction stored
		And Multiple transactions persisted with account_iban ES9820385778983000760013
		And Multiple transactions persisted with account_iban ES9820385778983000760016
	When I list the transactions
	Then the system returns the http-status 200
		And the transaction list has the elements
	
Scenario: Get list with elements sorted asc
	Given No transaction stored 
		And Multiple transactions persisted with account_iban ES9820385778983000760014
		And Multiple transactions persisted with account_iban ES9820385778983000760017
	When I list the transactions with sort asc
	Then the system returns the http-status 200
		And the transaction list has the elements
		And the transation list is ordered asc
	
Scenario: Get list with elements sorted desc
	Given No transaction stored 
		And Multiple transactions persisted with account_iban ES9820385778983000760015
		And Multiple transactions persisted with account_iban ES9820385778983000760018
	When I list the transactions with sort desc
	Then the system returns the http-status 200
		And the transaction list has the elements
		And the transation list is ordered desc