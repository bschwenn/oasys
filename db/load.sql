INSERT INTO Person VALUES
  (1, 'Alex Boid', 'ab@duke.edu', 2019, '/var/media/1234.png', '{}'),
  (2, 'Christy Denver', 'cd@duke.edu', 2021, '/var/media/5678.png', '{"twitter": "twitter.com/cd"}');

INSERT INTO Flock VALUES
  (1, 'CS 316', '/var/media/1357.png'),
  (2, 'CS Department', '/var/media/2468.png');

INSERT INTO Event VALUES -- not real times :)
  (1, 'CS Fiesta', NOW(), 'LSRC', 1, 'A fiesta! For cs people'),
  (2, 'Milestone 1 Due', NOW(), NULL, 2, 'First project milestone is due at midnight');

INSERT INTO Post VALUES
  (1, 2, 1, NOW(), 'public', 'Hey guys! Be sure to attend the CS fiesta on Friday!');

INSERT INTO Comment VALUES
  (1, 1, 2, NOW(), 'Great post!');

-- Message stuff

INSERT INTO Interest VALUES
  (1, 'Databases', FALSE),
  (2, 'Computer Science', TRUE),
  (3, 'Machine Learning', FALSE),
  (4, 'Politics', FALSE),
  (5, 'Political Science', TRUE);


INSERT INTO Follows VALUES
  (1, 2),
  (2, 1);

INSERT INTO Member VALUES
  (1, 1, 1),
  (2, 2, 1);

INSERT INTO Moderates VALUES
  (1, 1),
  (2, 2);

INSERT INTO Studies VALUES
  (2, 2, 'major'),
  (1, 5, 'major'),
  (1, 2, 'minor');

INSERT INTO Interested VALUES
  (1, 1),
  (1, 3),
  (1, 4),
  (2, 4);

-- Participates / message stuff

INSERT INTO Tags VALUES
  (1, 2),
  (1, 3),
  (2, 1);

INSERT INTO Related VALUES
  (1, 1),
  (1, 2),
  (2, 1),
  (2, 2),
  (2, 3);


