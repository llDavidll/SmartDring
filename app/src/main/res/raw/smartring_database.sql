DROP TABLE IF EXISTS "Profiles";
DROP TABLE IF EXISTS "Places";

CREATE TABLE [Profiles] (
	"profileId"		    integer 	PRIMARY KEY,
	"isDefault"         integer,
	"profileName"       nvarchar	UNIQUE,
	"profileColor"      integer,
	"profilePhoneLvl"   integer,
	"profileNotifLvl"   integer,
	"profileMediaLvl"   integer,
	"profileCallLvl"    integer,
	"profileAlarmLvl"   integer
	);

CREATE TABLE [Places] (
	"placeId"		    integer 	PRIMARY KEY,
	"isDefault"         integer,
	"placeName"         nvarchar	UNIQUE,
	"placeLatitude"     real,
	"placeLongitude"    real,
	"profileId"         integer
	);

insert into Profiles values (1, 1, "Home", '0xFF33B5E5', 7,7,7,7,7);
insert into Profiles values (2, 1, "Work", '0xFFFF8800',2,2,2,2,2);

insert into Places values (1, 1, "", 0, 0, 1);
insert into Places values (2, 0, "Enac", 43.565659, 1.481337, 1);
insert into Places values (3, 0, "Home", 43.554347, 1.466206, -1);
