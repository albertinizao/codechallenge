# codechallenge


## Introducción
El objetivo del presente proyecto es servir como ejemplo de desarrollo.

## Supuestos
Dado que el documento explicativo del proyecto dejaba varios puntos abiertos, se han tomado ciertas decisiones:

-  Al no disponer de ninguna lógica para generar el campo "reference" en caso de no ser informado por el cliente, se ha optado por utilziar el hashcode del objeto. Cabe destacar que el hashcode puede duplciarse, por lo que, aunque sea improbable, puede haber conflictos de identificadores duplicados.
-  Se ha utilizado una base de datos H2 en memoria. En producción debería usarse una persistencia más sólida.
-  Se ha optado por que para la obtención del status se utilice un verbo POST.
-  Se ha utilizado el paralelismo de junit5 para utilizar varios hilos de ejecucuión y ejecutar pruebas simultáneas.
-  Se validan los códigos de IBAN.
-  El caso de prueba G de TransactionStatus, muestra un comportamiento distinto al resto. Aún así se ha implementado acorde a dicho caso.


## Execution
To test and build, in the project folder, run "mvn clean install"
To execute, go to the project folder and run "java -jar target/codechallenge-0.0.1-SNAPSHOT.jar". Then, go to "http://localhost:8080". In the field "transaction" you could view the endpoints.
To view swagger: "http://localhost:8080/v2/api-docs".
