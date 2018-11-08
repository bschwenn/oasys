---- ENTITY SETS ----

CREATE TABLE Person(
  uid SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  email VARCHAR(256) NOT NULL UNIQUE,
  graduation_year INTEGER,
  photo_path VARCHAR(256),
  links JSON,
  username VARCHAR(256) NOT NULL UNIQUE,
  password VARCHAR(256) NOT NULL
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
  gid INTEGER PRIMARY KEY NOT NULL,
  name VARCHAR(128),
  photo_path VARCHAR(256)
);

CREATE TABLE Event(
  eid INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  time TIMESTAMP NOT NULL,
  location VARCHAR(256),
  creater_uid INTEGER NOT NULL REFERENCES Person(uid),
  summary TEXT
);

CREATE TABLE Post(
  pid INTEGER NOT NULL PRIMARY KEY,
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  creater_uid INTEGER NOT NULL REFERENCES Person(uid),
  timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
  kind VARCHAR(7) NOT NULL CHECK (kind = 'private' or kind = 'public'),
  body TEXT NOT NULL
);

CREATE TABLE Comment(
  cid INTEGER NOT NULL,
  pid INTEGER NOT NULL REFERENCES Post(pid),
  creater_uid INTEGER NOT NULL REFERENCES Person(uid),
  timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
  body TEXT NOT NULL,
  PRIMARY KEY(cid, pid)
);

CREATE TABLE Thread(
  tid INTEGER NOT NULL PRIMARY KEY,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE Message(
  mid INTEGER NOT NULL,
  tid INTEGER NOT NULL REFERENCES Thread(tid),
  timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
  body TEXT NOT NULL,
  PRIMARY KEY(mid, tid)
);

CREATE TABLE Interest(
  iid INTEGER NOT NULL PRIMARY KEY,
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

CREATE TABLE Moderates(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  gid INTEGER NOT NULL REFERENCES Flock(gid),
  PRIMARY KEY(uid, gid)
);

CREATE TABLE Studies(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  iid INTEGER NOT NULL REFERENCES Interest(iid), -- TODO: create assertion that checks for valid major/minor combinations?
  kind VARCHAR(5) CHECK (kind IS NULL OR kind = 'major' OR kind = 'minor')
);

CREATE TABLE Interested(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  iid INTEGER NOT NULL REFERENCES Interest(iid),
  PRIMARY KEY(uid, iid)
);

CREATE TABLE Participates(
  uid INTEGER NOT NULL REFERENCES Person(uid),
  tid INTEGER NOT NULL REFERENCES Thread(tid),
  PRIMARY KEY(uid, tid)
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

