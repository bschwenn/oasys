-- Get a user's basic info
SELECT *
FROM Person
WHERE uid = 1; -- 1 can be an interpolated parameter in app code

-- Get a user's interests
SELECT Interest.name
FROM Person, Interest, Interested
WHERE Person.uid = 1
AND Person.uid = Interested.uid
AND Interest.iid = Interested.iid;

-- Get a user's majors
SELECT Interest.name
FROM Person, Interest, Studies
WHERE Person.uid = 1
AND Person.uid = Studies.uid
AND Interest.iid = Studies.iid
AND Studies.kind = 'major';

-- Get a user's minors
SELECT Interest.name
FROM Person, Interest, Studies
WHERE Person.uid = 1
AND Person.uid = Studies.uid
AND Interest.iid = Studies.iid
AND Studies.kind = 'minor';

-- Data to populate a group's (flock's) profile info
SELECT *
FROM Flock
WHERE gid = 1;

-- Data to populate a group's feed
SELECT *
FROM Post
WHERE gid = 1;

-- Data to populate an event's page summary
SELECT *
FROM Event
WHERE eid = 1;

-- Get comments on a post with ID 1
SELECT *
FROM Comment
WHERE pid = 1;
