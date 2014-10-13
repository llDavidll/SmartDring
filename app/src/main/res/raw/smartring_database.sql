DROP TABLE IF EXISTS "Profiles";
CREATE TABLE [Profiles] (
	"profileId"		integer 	PRIMARY KEY,
	"profileName"   nvarchar	UNIQUE,
	"profileColor"         nvarchar
	);

insert into Profiles values (1, "Home", "#FF33B5E5");
insert into Profiles values (2, "Work", "#FFFF8800");
