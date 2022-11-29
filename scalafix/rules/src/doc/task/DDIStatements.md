| DDI Statements                                      | task          |  develop |
|-----------------------------------------------------|---------------|----------|
| In Spark 3.0, when inserting a value into a table   |               |          |
|column with a different data type, the type coercion |               |          |
|is performed as per ANSI SQL standard. Certain       |               |          | 
|unreasonable type conversions such as converting     |               |          |
|string to int and double to boolean are disallowed.  |               |          |
|A runtime exception is thrown if the value is        |               |          |
|out-of-range for the data type of the column.        |               |          |
|In Spark version 2.4 and below, type conversions     |               |          |
|during table insertion are allowed as long as they   |               |          |
|are valid Cast. When inserting an out-of-range value |               |          |
|to an integral field, the low-order bits of the value|               |          | 
|is inserted(the same as Java/Scala numeric type      |               |          |
|casting). For example, if 257 is inserted to a field |               |          |
|of byte type, the result is 1. The behavior is       |               |          |
|controlled by the option                             |               |          |
|spark.sql.storeAssignmentPolicy, with a default value|               |          |
|as “ANSI”. Setting the option as “Legacy” restores   |               |          |
|the previous behavior.                               |               |          |
|-----------------------------------------------------|---------------|----------|