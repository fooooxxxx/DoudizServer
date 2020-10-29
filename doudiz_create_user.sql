CREATE USER `landlord_Copy1`@`localhost` IDENTIFIED WITH caching_sha2_password;

GRANT Alter, Alter Routine, Create, Create Routine, Create Temporary Tables, Create View, Delete, Drop, Event, Execute, Grant Option, Index, Insert, Lock Tables, References, Select, Show View, Trigger, Update ON `doudiz`.* TO `landlord_Copy1`@`localhost`;

GRANT Alter, Alter Routine, Create, Create Routine, Create Temporary Tables, Create User, Create View, Delete, Drop, Event, Execute, File, Grant Option, Index, Insert, Lock Tables, Process, References, Reload, Select, Show Databases, Show View, Shutdown, Trigger, Update ON *.* TO `landlord_Copy1`@`localhost`;