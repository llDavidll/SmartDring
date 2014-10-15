DROP TABLE IF EXISTS "Profiles";
DROP TABLE IF EXISTS "Places";

CREATE TABLE [Profiles] (
	"profileId"		integer 	PRIMARY KEY,
	"profileName"   nvarchar	UNIQUE,
	"profileColor"  nvarchar
	);

CREATE TABLE [Places] (
	"placeId"		integer 	PRIMARY KEY,
	"placeName"     nvarchar	UNIQUE,
	"profileId"     integer
	);

insert into Profiles values (1, "Home", "#FF33B5E5");
insert into Profiles values (2, "Work", "#FFFF8800");

insert into Places values (1, "Home", 1);
insert into Places values (2, "UPS", -1);
