SELECT setval(pg_get_serial_sequence('flock', 'gid'), (SELECT max(gid) FROM flock));
SELECT setval(pg_get_serial_sequence('person', 'uid'), (SELECT max(uid) FROM person));
 SELECT setval(pg_get_serial_sequence('comment', 'cid'), (SELECT max(cid) FROM comment));
 SELECT setval(pg_get_serial_sequence('post', 'pid'), (SELECT max(pid) FROM post));
SELECT setval(pg_get_serial_sequence('interest', 'iid'), (SELECT max(iid) FROM interest));
SELECT setval(pg_get_serial_sequence('event', 'eid'), (SELECT max(eid) FROM event));
