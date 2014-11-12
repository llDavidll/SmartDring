DROP TABLE IF EXISTS "Profiles";
DROP TABLE IF EXISTS "Places";
DROP TABLE IF EXISTS "ContactBlackList";
DROP TABLE IF EXISTS "ContactWhiteList";

CREATE TABLE [Profiles] (
	"profileId"		    integer 	PRIMARY KEY,
	"isDefault"         integer,
	"profileName"       nvarchar,
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
	"placeName"         nvarchar,
	"placeLatitude"     real,
	"placeLongitude"    real,
	"profileId"         integer
	);

CREATE TABLE [ContactWhiteList] (
    "contactId"		    integer 	PRIMARY KEY,
    "ContactName"       nvarchar,
    "contactPhone"      nvarchar
    );

CREATE TABLE [ContactBlackList] (
    "contactID"		    integer 	PRIMARY KEY,
    "ContactName"       nvarchar,
    "contactPhone"      nvarchar
    );

insert into Profiles values (1, 1, "Maison", '0xFF33B5E5', 7,7,7,7,7);
insert into Profiles values (2, 1, "Travail", '0xFFFF8800',2,2,2,2,2);

insert into Places values (1, 1, "Extérieur", 0, 0, 1);
