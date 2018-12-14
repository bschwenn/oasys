---- ENTITY SETS ----

CREATE TABLE Person(
  uid SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  email VARCHAR(256) NOT NULL UNIQUE,
  graduation_year INTEGER,
  photo_path VARCHAR(256) NOT NULL DEFAULT 'https://storage.googleapis.com/oasys-images/default.png',
  links JSON,
  username VARCHAR(256) NOT NULL UNIQUE,
  password VARCHAR(256) NOT NULL,
  bio TEXT
);

CREATE TABLE Role(
  rid SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  description VARCHAR(512)
);

CREATE TABLE PersonRole(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  rid INTEGER NOT NULL REFERENCES Role(rid),
  PRIMARY KEY (uid, rid)
);

CREATE TABLE Flock(
  gid SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(128),
  photo_path VARCHAR(256),
  description text
);

CREATE TABLE Event(
  eid SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  time TIMESTAMP NOT NULL,
  location VARCHAR(256),
  creator_uid INTEGER NOT NULL REFERENCES Person(uid), -- Admin if group event
  summary TEXT,
  gid INTEGER, -- If set, is associated with a group
  public BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Post(
  pid SERIAL NOT NULL PRIMARY KEY,
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  creator_uid INTEGER NOT NULL REFERENCES Person(uid),
  timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
  kind VARCHAR(7) NOT NULL CHECK (kind = 'private' or kind = 'public'),
  body TEXT NOT NULL
);

CREATE TABLE Comment(
  cid SERIAL NOT NULL,
  pid INTEGER NOT NULL REFERENCES Post(pid),
  creator_uid INTEGER NOT NULL REFERENCES Person(uid),
  timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
  body TEXT NOT NULL,
  PRIMARY KEY(cid, pid)
);

CREATE TABLE Interest(
  iid SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  is_study BOOLEAN NOT NULL
);

---- RELATIONSHIP SETS ----

CREATE TABLE Follows(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  PRIMARY KEY(uid, gid)
);

CREATE TABLE Member(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  initiator_uid INTEGER NOT NULL REFERENCES Person(uid),
  PRIMARY KEY(uid, gid)
);

CREATE TABLE MemberRequest(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  PRIMARY KEY(uid, gid)
);

CREATE TABLE Moderates(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  PRIMARY KEY(uid, gid)
);

CREATE TABLE Studies(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  iid INTEGER NOT NULL REFERENCES Interest(iid),
  kind VARCHAR(5) CHECK (kind IS NULL OR kind = 'major' OR kind = 'minor')
);

CREATE TABLE Interested(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  iid INTEGER NOT NULL REFERENCES Interest(iid),
  PRIMARY KEY(uid, iid)
);

CREATE TABLE Tags(
  eid INTEGER NOT NULL REFERENCES Event(eid),
  iid INTEGER NOT NULL REFERENCES Interest(iid),
  PRIMARY KEY(eid, iid)
);

CREATE TABLE Related(
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  iid INTEGER NOT NULL REFERENCES Interest(iid),
  PRIMARY KEY (gid, iid)
);

CREATE TABLE Likes(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  pid INTEGER NOT NULL REFERENCES Post(pid),
  PRIMARY KEY (uid, pid)
);

CREATE TABLE Going(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  eid INTEGER NOT NULL REFERENCES Event(eid),
  PRIMARY KEY(uid, eid)
);

-- This should probably work to check whether or not a tuple is a valid major/minor
CREATE FUNCTION TF_ValidMajorMinor() RETURNS TRIGGER AS $$
BEGIN
  IF EXISTS (SELECT * FROM Interest WHERE iid = NEW.iid AND is_study IS NOT TRUE) THEN
    RAISE EXCEPTION '% is not a valid major or minor', NEW.iid;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TG_ValidMajorMinor
  BEFORE INSERT OR UPDATE OF iid ON Studies
  FOR EACH ROW
  EXECUTE PROCEDURE TF_ValidMajorMinor();

