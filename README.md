# codechallenge


## Introducción
El objetivo del presente proyecto es servir como ejemplo de desarrollo.

## Supuestos
Dado que el documento explicativo del proyecto dejaba varios puntos abiertos, se han tomado ciertas decisiones:

- Al no disponer de ninguna lógica para generar el campo "reference" en caso de no ser informado por el cliente, se ha optado por utilziar el hashcode del objeto. Cabe destacar que el hashcode puede duplciarse, por lo que, aunque sea improbable, puede haber conflictos de identificadores duplicados.
- Se ha utilizado una base de datos H2 en memoria. En producción debería usarse una persistencia más sólida.
- Se ha optado por que para la obtención del status se utilice un verbo POST.
- Se ha utilziado el paralelismo de junit5 para utilizar varios hilos de ejecucuión y ejecutar pruebas simultáneas.
- Se validan los códigos de IBAN.