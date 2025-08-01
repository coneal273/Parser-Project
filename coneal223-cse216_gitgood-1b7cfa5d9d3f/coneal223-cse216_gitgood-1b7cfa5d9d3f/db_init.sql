--
-- DATABASE INITIALIZATION COMMANDS GIT GOOD
--

INSERT INTO users VALUES (DEFAULT, 'admin', 'a@lehigh.edu', NULL, NULL, NULL, DEFAULT, DEFAULT);
INSERT INTO posts VALUES (DEFAULT, 1, 'First test post', NULL, DEFAULT, DEFAULT);
INSERT INTO comments VALUES (DEFAULT, 1, 1, 'First test comment', NULL, DEFAULT, DEFAULT);

--probably want a test file uploaded here too