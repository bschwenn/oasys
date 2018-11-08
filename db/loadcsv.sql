\copy Person(uid, name, email, graduation_year, photo_path, links, username, password) FROM 'db/person.csv' DELIMITER ',' CSV;

\copy Flock(gid, name, photo_path) FROM 'db/flock.csv' DELIMITER ',' CSV;

\copy Post(pid, gid, creator_uid, timestamp, kind, body) FROM 'db/post.csv' DELIMITER ',' CSV;

\copy Comment(cid, pid, creator_uid, timestamp, body) FROM 'db/comment.csv' DELIMITER ',' CSV;

\copy Interest(iid, name, is_study) FROM 'db/interest.csv' DELIMITER ',' CSV;

\copy Follows(uid, gid) FROM 'db/follows.csv' DELIMITER ',' CSV;

\copy Member(uid, gid, initiator_uid) FROM 'db/member.csv' DELIMITER ',' CSV;

\copy Studies(uid, iid, kind) FROM 'db/studies.csv' DELIMITER ',' CSV;

\copy Interested(uid, iid) FROM 'db/interested.csv' DELIMITER ',' CSV;

\copy Related(gid, iid) FROM 'db/related.csv' DELIMITER ',' CSV;

\copy Moderates(uid, gid) FROM 'db/moderates.csv' DELIMITER ',' CSV;
