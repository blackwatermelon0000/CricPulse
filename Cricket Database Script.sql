-- Project: Cricket Match Database System
-- Author: Ayesha (User: Ayesha / Password: Ayesha123)
-- Environment: Oracle Database 21c XE
-- sqlplus Ayesha/Ayesha123@localhost:1521/XEPDB1 

------------------------
-- 1. USER SETUP
------------------------
CREATE USER Ayesha IDENTIFIED BY Ayesha123;
GRANT CONNECT, RESOURCE TO Ayesha;
ALTER USER Ayesha DEFAULT TABLESPACE users QUOTA UNLIMITED ON users;

------------------------
-- 2. TABLE CREATION
------------------------
-- 1. Teams Table
CREATE TABLE teams (
    team_id NUMBER PRIMARY KEY,
    team_name VARCHAR2(100) UNIQUE NOT NULL,
    country VARCHAR2(100),
    coach VARCHAR2(100)
);

-- 2. Players Table
CREATE TABLE players (
    player_id NUMBER PRIMARY KEY,
    player_name VARCHAR2(100) NOT NULL,
    age NUMBER CHECK (age BETWEEN 16 AND 50),
    team_id NUMBER REFERENCES teams(team_id),
    batting_style VARCHAR2(50),
    bowling_style VARCHAR2(50),
    role VARCHAR2(50)
);

-- 3. Tournaments Table
CREATE TABLE tournaments (
    tournament_id NUMBER PRIMARY KEY,
    tournament_name VARCHAR2(100),
    tournament_year NUMBER
);

-- 4. Matches Table
CREATE TABLE matches (
    match_id NUMBER PRIMARY KEY,
    tournament_id NUMBER REFERENCES tournaments(tournament_id),
    team1_id NUMBER REFERENCES teams(team_id),
    team2_id NUMBER REFERENCES teams(team_id),
    match_date DATE,
    venue VARCHAR2(100),
    winner_team_id NUMBER REFERENCES teams(team_id)
);

-- 5. Innings Table
CREATE TABLE innings (
    innings_id NUMBER PRIMARY KEY,
    match_id NUMBER REFERENCES matches(match_id),
    batting_team_id NUMBER REFERENCES teams(team_id),
    bowling_team_id NUMBER REFERENCES teams(team_id),
    total_runs NUMBER CHECK (total_runs >= 0),
    wickets_lost NUMBER CHECK (wickets_lost BETWEEN 0 AND 10),
    overs NUMBER CHECK (overs >= 0)
);

-- 6. Balls Table
CREATE TABLE balls (
    ball_id NUMBER PRIMARY KEY,
    match_id NUMBER REFERENCES matches(match_id),
    innings_id NUMBER REFERENCES innings(innings_id),
    over_no NUMBER CHECK (over_no >= 0),
    ball_no NUMBER CHECK (ball_no BETWEEN 1 AND 6),
    batsman_id NUMBER REFERENCES players(player_id),
    bowler_id NUMBER REFERENCES players(player_id),
    runs_scored NUMBER CHECK (runs_scored >= 0),
    is_wicket CHAR(1) CHECK (is_wicket IN ('Y', 'N')),
    dismissal_type VARCHAR2(50)
);

-- 7. Scorecards Table
CREATE TABLE scorecards (
    scorecard_id NUMBER PRIMARY KEY,
    match_id NUMBER REFERENCES matches(match_id),
    player_id NUMBER REFERENCES players(player_id),
    runs_scored NUMBER CHECK (runs_scored >= 0),
    balls_faced NUMBER CHECK (balls_faced >= 0),
    wickets_taken NUMBER CHECK (wickets_taken >= 0),
    overs_bowled NUMBER CHECK (overs_bowled >= 0)
);

-- 8. Extras Table
CREATE TABLE extras (
    extra_id NUMBER PRIMARY KEY,
    ball_id NUMBER REFERENCES balls(ball_id),
    extra_type VARCHAR2(20) CHECK (extra_type IN ('wide', 'no_ball', 'bye', 'leg_bye', 'penalty')),
    runs NUMBER CHECK (runs >= 0)
);

-- 9. Partnerships Table
CREATE TABLE partnerships (
    partnership_id NUMBER PRIMARY KEY,
    match_id NUMBER REFERENCES matches(match_id),
    player1_id NUMBER REFERENCES players(player_id),
    player2_id NUMBER REFERENCES players(player_id),
    runs_scored NUMBER CHECK (runs_scored >= 0)
);

-- 10. Umpires Table
CREATE TABLE umpires (
    umpire_id NUMBER PRIMARY KEY,
    umpire_name VARCHAR2(100),
    nationality VARCHAR2(50)
);

-- 11. Match Umpires Table
CREATE TABLE match_umpires (
    match_id NUMBER REFERENCES matches(match_id),
    umpire_id NUMBER REFERENCES umpires(umpire_id),
    PRIMARY KEY (match_id, umpire_id)
);

-- 12. Player Roles Table
CREATE TABLE player_roles (
    player_id NUMBER REFERENCES players(player_id),
    tournament_id NUMBER REFERENCES tournaments(tournament_id),
    role VARCHAR2(30),
    PRIMARY KEY (player_id, tournament_id)
);
------------------------
-- 3. SEQUENCES
------------------------

-- 1. Sequence for Teams
CREATE SEQUENCE team_seq START WITH 1 INCREMENT BY 1;

-- 2. Sequence for Players
CREATE SEQUENCE player_seq START WITH 1 INCREMENT BY 1;

-- 3. Sequence for Tournaments
CREATE SEQUENCE tournament_seq START WITH 1 INCREMENT BY 1;

-- 4. Sequence for Matches
CREATE SEQUENCE match_seq START WITH 1 INCREMENT BY 1;

-- 5. Sequence for Innings
CREATE SEQUENCE innings_seq START WITH 1 INCREMENT BY 1;

-- 6. Sequence for Balls
CREATE SEQUENCE ball_seq START WITH 51 INCREMENT BY 1;

-- 7. Sequence for Scorecards
CREATE SEQUENCE scorecard_seq START WITH 1 INCREMENT BY 1;

-- 8. Sequence for Extras
CREATE SEQUENCE extra_seq START WITH 1 INCREMENT BY 1;

-- 9. Sequence for Partnerships
CREATE SEQUENCE partnership_seq START WITH 1 INCREMENT BY 1;

-- 10. Sequence for Umpires
CREATE SEQUENCE umpire_seq START WITH 1 INCREMENT BY 1;

------------------------
-- 4. SAMPLE DATA
------------------------

--Sample Teams
INSERT INTO teams (team_id, team_name, country, coach) VALUES (1, 'Lahore Qalandars', 'Pakistan', 'Aaqib Javed');
INSERT INTO teams (team_id, team_name, country, coach) VALUES (2, 'Karachi Kings', 'Pakistan', 'Wahab Riaz');
INSERT INTO teams (team_id, team_name, country, coach) VALUES (3, 'Multan Sultans', 'Pakistan', 'Andy Flower');
INSERT INTO teams (team_id, team_name, country, coach) VALUES (4, 'Islamabad United', 'Pakistan', 'Azhar Mahmood');
INSERT INTO teams (team_id, team_name, country, coach) VALUES (5, 'Quetta Gladiators', 'Pakistan', 'Moin Khan');
INSERT INTO teams (team_id, team_name, country, coach) VALUES (6, 'Peshawar Zalmi', 'Pakistan', 'Daren Sammy');


-- Sample players
-- Lahore Qalandars (team_id = 1)
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (1, 'Fakhar Zaman', 32, 1, 'Left-hand bat', 'Right-arm offbreak', 'Batsman');
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (2, 'Shaheen Afridi', 24, 1, 'Left-hand bat', 'Left-arm fast', 'Bowler');

-- Karachi Kings (team_id = 2)
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (3, 'Imad Wasim', 35, 2, 'Left-hand bat', 'Left-arm orthodox', 'Allrounder');
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (4, 'Mohammad Amir', 31, 2, 'Left-hand bat', 'Left-arm fast', 'Bowler');

-- Multan Sultans (team_id = 3)
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (5, 'Mohammad Rizwan', 31, 3, 'Right-hand bat', 'Right-arm medium', 'Wicketkeeper');
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (6, 'Rilee Rossouw', 35, 3, 'Left-hand bat', 'Right-arm offbreak', 'Batsman');

-- Islamabad United (team_id = 4)
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (7, 'Shadab Khan', 26, 4, 'Right-hand bat', 'Legbreak', 'Allrounder');
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (8, 'Alex Hales', 35, 4, 'Right-hand bat', 'Right-arm medium', 'Batsman');

-- Quetta Gladiators (team_id = 5)
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (9, 'Sarfaraz Ahmed', 37, 5, 'Right-hand bat', 'Right-arm offbreak', 'Wicketkeeper');
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (10, 'Mohammad Hasnain', 24, 5, 'Right-hand bat', 'Right-arm fast', 'Bowler');

-- Peshawar Zalmi (team_id = 6)
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (11, 'Babar Azam', 30, 6, 'Right-hand bat', 'None', 'Batsman');
INSERT INTO players (player_id, player_name, age, team_id, batting_style, bowling_style, role) VALUES (12, 'Wahab Riaz', 38, 6, 'Left-hand bat', 'Left-arm fast', 'Bowler');



--Sample Tournaments
INSERT INTO tournaments (tournament_id, tournament_name, tournament_year) VALUES (1, 'PSL 2023', 2023);
INSERT INTO tournaments (tournament_id, tournament_name, tournament_year) VALUES (2, 'PSL 2024', 2024);
INSERT INTO tournaments (tournament_id, tournament_name, tournament_year) VALUES (3, 'PSL 2025', 2025);


--Sample Matches

-- Matches for PSL 2023 (tournament_id = 1)
INSERT INTO matches (match_id, tournament_id, team1_id, team2_id, match_date, venue, winner_team_id)
VALUES (1, 1, 1, 2, TO_DATE('2023-02-14', 'YYYY-MM-DD'), 'Lahore', 1);

INSERT INTO matches (match_id, tournament_id, team1_id, team2_id, match_date, venue, winner_team_id)
VALUES (2, 1, 3, 4, TO_DATE('2023-02-15', 'YYYY-MM-DD'), 'Multan', 3);

-- Matches for PSL 2024 (tournament_id = 2)
INSERT INTO matches (match_id, tournament_id, team1_id, team2_id, match_date, venue, winner_team_id)
VALUES (3, 2, 5, 6, TO_DATE('2024-02-17', 'YYYY-MM-DD'), 'Karachi', 6);

INSERT INTO matches (match_id, tournament_id, team1_id, team2_id, match_date, venue, winner_team_id)
VALUES (4, 2, 2, 3, TO_DATE('2024-02-18', 'YYYY-MM-DD'), 'Rawalpindi', 3);

INSERT INTO matches (match_id, tournament_id, team1_id, team2_id, match_date, venue, winner_team_id)
VALUES (6, 3, 6, 5, TO_DATE('2025-06-18', 'YYYY-MM-DD'), 'National Stadium', 6);

--Innings Sample
-- Innings for Match 1
INSERT INTO innings VALUES (1, 1, 1, 2, 160, 6, 20);  -- Lahore Qalandars vs Karachi Kings
INSERT INTO innings VALUES (2, 1, 2, 1, 145, 8, 20);

-- Innings for Match 2
INSERT INTO innings VALUES (3, 2, 3, 4, 170, 7, 20);  -- Multan Sultans vs Islamabad United
INSERT INTO innings VALUES (4, 2, 4, 3, 155, 9, 20);

-- Innings for Match 3
INSERT INTO innings VALUES (5, 3, 5, 6, 175, 6, 20);  -- Quetta Gladiators vs Peshawar Zalmi
INSERT INTO innings VALUES (6, 3, 6, 5, 160, 7, 20);

-- Innings for Match 4
INSERT INTO innings VALUES (7, 4, 2, 3, 180, 5, 20);  -- Karachi Kings vs Multan Sultans
INSERT INTO innings VALUES (8, 4, 3, 2, 150, 10, 19.5);

-- Innings for Match 5
INSERT INTO innings VALUES (9, 5, 1, 4, 162, 6, 20);  -- Lahore Qalandars vs Islamabad United
INSERT INTO innings VALUES (10, 5, 4, 1, 157, 8, 20);

-- Innings for Match 6
INSERT INTO innings VALUES (11, 6, 6, 5, 138, 6, 20);  -- Peshawar Zalmi vs Quetta Gladiators
INSERT INTO innings VALUES (12, 6, 5, 6, 132, 8, 20);

COMMIT;

--Balls Sample

-- Match 1, Innings 1 (Lahore Qalandars Batting)
INSERT INTO balls VALUES (1, 1, 1, 1, 1, 1, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (2, 1, 1, 1, 2, 3, 2, 4, 'N', NULL);
INSERT INTO balls VALUES (3, 1, 1, 2, 1, 1, 2, 6, 'N', NULL);
INSERT INTO balls VALUES (4, 1, 1, 2, 2, 3, 2, 0, 'Y', 'Bowled');
INSERT INTO balls VALUES (5, 1, 1, 3, 1, 1, 5, 1, 'N', NULL);
INSERT INTO balls VALUES (6, 1, 1, 3, 2, 6, 5, 2, 'N', NULL);
INSERT INTO balls VALUES (7, 1, 1, 4, 1, 1, 5, 4, 'N', NULL);
INSERT INTO balls VALUES (8, 1, 1, 4, 2, 6, 5, 6, 'N', NULL);
INSERT INTO balls VALUES (9, 1, 1, 5, 1, 1, 5, 0, 'Y', 'Caught');
INSERT INTO balls VALUES (10, 1, 1, 5, 2, 6, 5, 1, 'N', NULL);

COMMIT;


-- Match 1, Innings 2 (Karachi Kings Batting)
INSERT INTO balls VALUES (11, 1, 2, 1, 1, 4, 1, 2, 'N', NULL);
INSERT INTO balls VALUES (12, 1, 2, 1, 2, 5, 1, 3, 'Y', 'LBW');
INSERT INTO balls VALUES (13, 1, 2, 2, 1, 4, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (14, 1, 2, 2, 2, 5, 2, 0, 'N', NULL);
INSERT INTO balls VALUES (15, 1, 2, 3, 1, 4, 2, 4, 'N', NULL);
INSERT INTO balls VALUES (16, 1, 2, 3, 2, 5, 2, 6, 'N', NULL);
INSERT INTO balls VALUES (17, 1, 2, 4, 1, 4, 2, 0, 'Y', 'Caught');
INSERT INTO balls VALUES (18, 1, 2, 4, 2, 5, 2, 2, 'N', NULL);
INSERT INTO balls VALUES (19, 1, 2, 5, 1, 4, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (20, 1, 2, 5, 2, 5, 2, 3, 'N', NULL);

COMMIT;

-- Match 2, Innings 1 (Multan Sultans Batting)
INSERT INTO balls VALUES (21, 2, 3, 1, 1, 3, 4, 2, 'N', NULL);
INSERT INTO balls VALUES (22, 2, 3, 1, 2, 8, 4, 0, 'N', NULL);
INSERT INTO balls VALUES (23, 2, 3, 2, 1, 3, 4, 6, 'N', NULL);
INSERT INTO balls VALUES (24, 2, 3, 2, 2, 8, 4, 1, 'Y', 'Caught');
INSERT INTO balls VALUES (25, 2, 3, 3, 1, 3, 4, 3, 'N', NULL);
INSERT INTO balls VALUES (26, 2, 3, 3, 2, 10, 4, 4, 'N', NULL);
INSERT INTO balls VALUES (27, 2, 3, 4, 1, 3, 4, 2, 'N', NULL);
INSERT INTO balls VALUES (28, 2, 3, 4, 2, 10, 4, 1, 'N', NULL);
INSERT INTO balls VALUES (29, 2, 3, 5, 1, 3, 4, 0, 'N', NULL);
INSERT INTO balls VALUES (30, 2, 3, 5, 2, 10, 4, 5, 'Y', 'Bowled');

COMMIT;


-- Match 2, Innings 2 (Islamabad United Batting)
INSERT INTO balls VALUES (31, 2, 4, 1, 1, 4, 3, 1, 'N', NULL);
INSERT INTO balls VALUES (32, 2, 4, 1, 2, 7, 3, 2, 'N', NULL);
INSERT INTO balls VALUES (33, 2, 4, 2, 1, 4, 3, 0, 'Y', 'LBW');
INSERT INTO balls VALUES (34, 2, 4, 2, 2, 7, 3, 3, 'N', NULL);
INSERT INTO balls VALUES (35, 2, 4, 3, 1, 4, 3, 4, 'N', NULL);
INSERT INTO balls VALUES (36, 2, 4, 3, 2, 10, 3, 1, 'N', NULL);
INSERT INTO balls VALUES (37, 2, 4, 4, 1, 4, 3, 2, 'N', NULL);
INSERT INTO balls VALUES (38, 2, 4, 4, 2, 10, 3, 0, 'N', NULL);
INSERT INTO balls VALUES (39, 2, 4, 5, 1, 4, 3, 3, 'Y', 'Caught');
INSERT INTO balls VALUES (40, 2, 4, 5, 2, 10, 3, 2, 'N', NULL);

COMMIT;


-- Match 3, Innings 1 (Quetta Gladiators Batting)
INSERT INTO balls VALUES (41, 3, 5, 1, 1, 5, 4, 1, 'N', NULL);
INSERT INTO balls VALUES (42, 3, 5, 1, 2, 6, 4, 2, 'N', NULL);
INSERT INTO balls VALUES (43, 3, 5, 2, 1, 5, 4, 0, 'Y', 'Bowled');
INSERT INTO balls VALUES (44, 3, 5, 2, 2, 6, 4, 4, 'N', NULL);
INSERT INTO balls VALUES (45, 3, 5, 3, 1, 5, 4, 6, 'N', NULL);
INSERT INTO balls VALUES (46, 3, 5, 3, 2, 6, 4, 1, 'N', NULL);
INSERT INTO balls VALUES (47, 3, 5, 4, 1, 5, 4, 2, 'N', NULL);
INSERT INTO balls VALUES (48, 3, 5, 4, 2, 6, 4, 0, 'N', NULL);
INSERT INTO balls VALUES (49, 3, 5, 5, 1, 5, 4, 3, 'N', NULL);
INSERT INTO balls VALUES (50, 3, 5, 5, 2, 6, 4, 1, 'N', NULL);

COMMIT;

-- Match 3, Innings 2 (Islamabad United Batting – 6 balls with 1 wicket)
INSERT INTO balls VALUES (ball_seq.NEXTVAL, 3, 2, 1, 1, 7, 6, 1, 'N', NULL);
INSERT INTO balls VALUES (ball_seq.NEXTVAL, 3, 2, 1, 2, 8, 6, 0, 'Y', 'Caught');
INSERT INTO balls VALUES (ball_seq.NEXTVAL, 3, 2, 1, 3, 9, 6, 4, 'N', NULL);
INSERT INTO balls VALUES (ball_seq.NEXTVAL, 3, 2, 1, 4, 10, 6, 6, 'N', NULL);
INSERT INTO balls VALUES (ball_seq.NEXTVAL, 3, 2, 1, 5, 7, 6, 2, 'N', NULL);
INSERT INTO balls VALUES (ball_seq.NEXTVAL, 3, 2, 1, 6, 8, 6, 0, 'N', NULL);

COMMIT;

-- Match 4, Innings 1 (Karachi Kings Batting)
INSERT INTO balls VALUES (51, 4, 6, 1, 1, 2, 1, 1, 'N', NULL);
INSERT INTO balls VALUES (52, 4, 6, 1, 2, 3, 1, 4, 'N', NULL);
INSERT INTO balls VALUES (53, 4, 6, 2, 1, 2, 1, 6, 'N', NULL);
INSERT INTO balls VALUES (54, 4, 6, 2, 2, 3, 1, 0, 'Y', 'LBW');
INSERT INTO balls VALUES (55, 4, 6, 3, 1, 2, 3, 2, 'N', NULL);
INSERT INTO balls VALUES (56, 4, 6, 3, 2, 5, 3, 0, 'N', NULL);
INSERT INTO balls VALUES (57, 4, 6, 4, 1, 2, 3, 4, 'N', NULL);
INSERT INTO balls VALUES (58, 4, 6, 4, 2, 5, 3, 1, 'N', NULL);
INSERT INTO balls VALUES (59, 4, 6, 5, 1, 2, 3, 3, 'N', NULL);
INSERT INTO balls VALUES (60, 4, 6, 5, 2, 5, 3, 0, 'N', NULL);

-- Match 4, Innings 2 (Multan Sultans Batting)
INSERT INTO balls VALUES (61, 4, 7, 1, 1, 8, 2, 2, 'N', NULL);
INSERT INTO balls VALUES (62, 4, 7, 1, 2, 7, 2, 0, 'Y', 'Caught');
INSERT INTO balls VALUES (63, 4, 7, 2, 1, 8, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (64, 4, 7, 2, 2, 9, 2, 4, 'N', NULL);
INSERT INTO balls VALUES (65, 4, 7, 3, 1, 8, 2, 3, 'N', NULL);
INSERT INTO balls VALUES (66, 4, 7, 3, 2, 9, 2, 2, 'N', NULL);
INSERT INTO balls VALUES (67, 4, 7, 4, 1, 8, 2, 0, 'N', NULL);
INSERT INTO balls VALUES (68, 4, 7, 4, 2, 9, 2, 6, 'N', NULL);
INSERT INTO balls VALUES (69, 4, 7, 5, 1, 8, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (70, 4, 7, 5, 2, 9, 2, 2, 'N', NULL);

COMMIT;

-- Match 5, Innings 1 (Lahore Qalandars Batting)
INSERT INTO balls VALUES (71, 5, 8, 1, 1, 1, 4, 2, 'N', NULL);
INSERT INTO balls VALUES (72, 5, 8, 1, 2, 2, 4, 3, 'N', NULL);
INSERT INTO balls VALUES (73, 5, 8, 2, 1, 1, 4, 6, 'N', NULL);
INSERT INTO balls VALUES (74, 5, 8, 2, 2, 2, 4, 0, 'Y', 'Caught');
INSERT INTO balls VALUES (75, 5, 8, 3, 1, 1, 4, 1, 'N', NULL);
INSERT INTO balls VALUES (76, 5, 8, 3, 2, 6, 4, 2, 'N', NULL);
INSERT INTO balls VALUES (77, 5, 8, 4, 1, 1, 4, 4, 'N', NULL);
INSERT INTO balls VALUES (78, 5, 8, 4, 2, 6, 4, 1, 'N', NULL);
INSERT INTO balls VALUES (79, 5, 8, 5, 1, 1, 4, 3, 'N', NULL);
INSERT INTO balls VALUES (80, 5, 8, 5, 2, 6, 4, 1, 'N', NULL);

COMMIT;

-- Match 5, Innings 2 (Karachi Kings Batting)
INSERT INTO balls VALUES (81, 5, 9, 1, 1, 7, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (82, 5, 9, 1, 2, 8, 2, 4, 'N', NULL);
INSERT INTO balls VALUES (83, 5, 9, 2, 1, 7, 2, 6, 'N', NULL);
INSERT INTO balls VALUES (84, 5, 9, 2, 2, 8, 2, 0, 'Y', 'LBW');
INSERT INTO balls VALUES (85, 5, 9, 3, 1, 7, 2, 1, 'N', NULL);
INSERT INTO balls VALUES (86, 5, 9, 3, 2, 9, 2, 2, 'N', NULL);
INSERT INTO balls VALUES (87, 5, 9, 4, 1, 7, 2, 3, 'N', NULL);
INSERT INTO balls VALUES (88, 5, 9, 4, 2, 9, 2, 0, 'N', NULL);
INSERT INTO balls VALUES (89, 5, 9, 5, 1, 7, 2, 2, 'N', NULL);
INSERT INTO balls VALUES (90, 5, 9, 5, 2, 9, 2, 1, 'N', NULL);

COMMIT;
-- Match 6, Innings 1 (Peshawar Zalmi Batting)
INSERT INTO balls VALUES (91, 6, 10, 1, 1, 9, 5, 1, 'N', NULL);
INSERT INTO balls VALUES (92, 6, 10, 1, 2, 10, 5, 2, 'N', NULL);
INSERT INTO balls VALUES (93, 6, 10, 2, 1, 9, 5, 4, 'N', NULL);
INSERT INTO balls VALUES (94, 6, 10, 2, 2, 10, 5, 0, 'Y', 'Caught');
INSERT INTO balls VALUES (95, 6, 10, 3, 1, 9, 5, 6, 'N', NULL);
INSERT INTO balls VALUES (96, 6, 10, 3, 2, 11, 5, 1, 'N', NULL);
INSERT INTO balls VALUES (97, 6, 10, 4, 1, 9, 5, 2, 'N', NULL);
INSERT INTO balls VALUES (98, 6, 10, 4, 2, 11, 5, 3, 'N', NULL);
INSERT INTO balls VALUES (99, 6, 10, 5, 1, 9, 5, 1, 'N', NULL);
INSERT INTO balls VALUES (100, 6, 10, 5, 2, 11, 5, 0, 'N', NULL);

COMMIT;
-- Match 6, Innings 2 (Quetta Gladiators Batting)
INSERT INTO balls VALUES (101, 6, 11, 1, 1, 5, 6, 1, 'N', NULL);
INSERT INTO balls VALUES (102, 6, 11, 1, 2, 6, 6, 2, 'N', NULL);
INSERT INTO balls VALUES (103, 6, 11, 2, 1, 5, 6, 0, 'Y', 'Bowled');
INSERT INTO balls VALUES (104, 6, 11, 2, 2, 6, 6, 4, 'N', NULL);
INSERT INTO balls VALUES (105, 6, 11, 3, 1, 7, 6, 6, 'N', NULL);
INSERT INTO balls VALUES (106, 6, 11, 3, 2, 6, 6, 1, 'N', NULL);
INSERT INTO balls VALUES (107, 6, 11, 4, 1, 7, 6, 2, 'N', NULL);
INSERT INTO balls VALUES (108, 6, 11, 4, 2, 6, 6, 0, 'Y', 'LBW');
INSERT INTO balls VALUES (109, 6, 11, 5, 1, 8, 6, 3, 'N', NULL);
INSERT INTO balls VALUES (110, 6, 11, 5, 2, 9, 6, 1, 'N', NULL);

COMMIT;


--Scorecards Sample

-- Scorecards for Match 1
INSERT INTO scorecards VALUES (1, 1, 1, 40, 30, 0, 0);
INSERT INTO scorecards VALUES (2, 1, 2, 15, 10, 2, 4);

-- Match 2
INSERT INTO scorecards VALUES (3, 2, 3, 55, 45, 0, 0);
INSERT INTO scorecards VALUES (4, 2, 4, 35, 28, 1, 2);

-- Match 3
INSERT INTO scorecards VALUES (5, 3, 5, 60, 50, 0, 0);
INSERT INTO scorecards VALUES (6, 3, 6, 20, 15, 3, 5);

-- Match 4
INSERT INTO scorecards VALUES (7, 4, 2, 70, 52, 0, 0);
INSERT INTO scorecards VALUES (8, 4, 3, 10, 8, 2, 4);

-- Match 5
INSERT INTO scorecards VALUES (9, 5, 1, 45, 35, 0, 0);
INSERT INTO scorecards VALUES (10, 5, 4, 50, 38, 1, 3);


--Extras Sample

-- Extras in Match 1 balls 2 and 11
INSERT INTO extras VALUES (1, 2, 'wide', 1);
INSERT INTO extras VALUES (2, 11, 'no_ball', 1);

-- Match 2 extras
INSERT INTO extras VALUES (3, 22, 'bye', 2);
INSERT INTO extras VALUES (4, 24, 'wide', 1);

-- Match 3
INSERT INTO extras VALUES (5, 43, 'leg_bye', 1);
INSERT INTO extras VALUES (6, 47, 'no_ball', 1);

-- Match 4
INSERT INTO extras VALUES (7, 53, 'wide', 2);
INSERT INTO extras VALUES (8, 58, 'wide', 1);

-- Match 5
INSERT INTO extras (extra_id, ball_id, extra_type, runs) VALUES (9, 71, 'wide', 1);
INSERT INTO extras (extra_id, ball_id, extra_type, runs) VALUES (10, 74, 'no_ball', 1);

-- Match 6
INSERT INTO extras (extra_id, ball_id, extra_type, runs) VALUES (11, 91, 'bye', 2);
INSERT INTO extras (extra_id, ball_id, extra_type, runs) VALUES (12, 93, 'wide', 1);

COMMIT;

--Partnerships Sample

INSERT INTO partnerships VALUES (1, 1, 1, 2, 45);
INSERT INTO partnerships VALUES (2, 2, 3, 4, 67);
INSERT INTO partnerships VALUES (3, 3, 5, 6, 55);
INSERT INTO partnerships VALUES (4, 4, 2, 3, 70);
INSERT INTO partnerships VALUES (5, 5, 1, 4, 60);


--Umpires Sample

INSERT INTO umpires VALUES (1, 'Aleem Dar', 'Pakistan');
INSERT INTO umpires VALUES (2, 'Kumar Dharmasena', 'Sri Lanka');
INSERT INTO umpires VALUES (3, 'Richard Kettleborough', 'England');


--Match_umpires
INSERT INTO match_umpires VALUES (1, 1);
INSERT INTO match_umpires VALUES (1, 2);
INSERT INTO match_umpires VALUES (2, 1);
INSERT INTO match_umpires VALUES (3, 2);
INSERT INTO match_umpires VALUES (4, 3);
INSERT INTO match_umpires VALUES (5, 1);


--Player_roles
INSERT INTO player_roles VALUES (1, 1, 'Opener');
INSERT INTO player_roles VALUES (2, 1, 'Bowler');
INSERT INTO player_roles VALUES (3, 2, 'Wicketkeeper');
INSERT INTO player_roles VALUES (4, 2, 'Allrounder');
INSERT INTO player_roles VALUES (5, 3, 'Batsman');
INSERT INTO player_roles VALUES (6, 3, 'Bowler');
INSERT INTO player_roles VALUES (7, 3, 'Opener');


------------------------
-- 4. SAMPLE QUERY
------------------------
-- 1. Match Summary (runs, wickets, extras)
SELECT m.match_id, t1.team_name AS team1, t2.team_name AS team2,
       COUNT(CASE WHEN b.is_wicket = 'Y' THEN 1 END) AS total_wickets,
       SUM(b.runs_scored + NVL(e.wide, 0) + NVL(e.no_ball, 0) +
           NVL(e.bye, 0) + NVL(e.leg_bye, 0) + NVL(e.penalty, 0)) AS total_runs,
       SUM(NVL(e.wide, 0)) AS wides, SUM(NVL(e.no_ball, 0)) AS no_balls, SUM(NVL(e.bye, 0)) AS byes
FROM matches m
JOIN balls b ON m.match_id = b.match_id
LEFT JOIN extras e ON b.ball_id = e.ball_id
JOIN teams t1 ON m.team1_id = t1.team_id
JOIN teams t2 ON m.team2_id = t2.team_id
GROUP BY m.match_id, t1.team_name, t2.team_name;

-- 2. Top Run Scorers
SELECT p.player_name, SUM(b.runs_scored) AS total_runs
FROM balls b
JOIN players p ON b.batsman_id = p.player_id
GROUP BY p.player_name
ORDER BY total_runs DESC;

-- 3. Top Wicket Takers
SELECT p.player_name, COUNT(*) AS wickets
FROM balls b
JOIN players p ON b.bowler_id = p.player_id
WHERE b.is_wicket = 'Y'
GROUP BY p.player_name
ORDER BY wickets DESC;

-- 4. Bowling Economy
SELECT p.player_name,
       ROUND(SUM(b.runs_scored) / (COUNT(*)/6), 2) AS economy
FROM balls b
JOIN players p ON b.bowler_id = p.player_id
GROUP BY p.player_name
ORDER BY economy;

-- 5. Team Runs Summary
SELECT t.team_name, SUM(b.runs_scored) AS total_runs
FROM balls b
JOIN players p ON b.batsman_id = p.player_id
JOIN teams t ON p.team_id = t.team_id
GROUP BY t.team_name;

-- 6. Match Winners
SELECT m.match_id, t.team_name AS winner
FROM matches m
JOIN teams t ON m.winner_team_id = t.team_id;

-- 7. Matches Played in Tournament
SELECT m.match_id, m.match_date, m.venue, t1.team_name, t2.team_name
FROM matches m
JOIN teams t1 ON m.team1_id = t1.team_id
JOIN teams t2 ON m.team2_id = t2.team_id;

-- 8. Over-by-Over Score Summary
SELECT over_no,
       SUM(b.runs_scored + NVL(e.wide, 0) + NVL(e.no_ball, 0) +
           NVL(e.bye, 0) + NVL(e.leg_bye, 0) + NVL(e.penalty, 0)) AS runs_in_over
FROM balls b
LEFT JOIN extras e ON b.ball_id = e.ball_id
GROUP BY over_no
ORDER BY over_no;

-- 9. Extras Summary by Match
SELECT m.match_id,
       SUM(NVL(e.wide,0)) AS wides,
       SUM(NVL(e.no_ball,0)) AS no_balls,
       SUM(NVL(e.bye,0)) AS byes
FROM balls b
JOIN matches m ON b.match_id = m.match_id
LEFT JOIN extras e ON b.ball_id = e.ball_id
GROUP BY m.match_id;

-- 10. Innings Summary
SELECT i.match_id, t.team_name AS batting_team, i.overs, i.total_runs, i.wickets_lost
FROM innings i
JOIN teams t ON i.batting_team = t.team_id;

-- 11. Most Matches Won
SELECT t.team_name, COUNT(m.winner_team_id) AS matches_won
FROM matches m
JOIN teams t ON m.winner_team_id = t.team_id
GROUP BY t.team_name
ORDER BY matches_won DESC;

-- 12. Most Dot Balls Bowled
SELECT p.player_name,
       COUNT(*) AS dot_balls
FROM balls b
JOIN players p ON b.bowler_id = p.player_id
WHERE b.runs_scored = 0 AND b.is_wicket = 'N'
GROUP BY p.player_name
ORDER BY dot_balls DESC;

-- 13. Dismissal Type Breakdown
SELECT dismissal_type, COUNT(*) AS total
FROM balls
WHERE is_wicket = 'Y'
GROUP BY dismissal_type;

-- 14. Player Runs by Match
SELECT m.match_id, p.player_name, SUM(b.runs_scored) AS runs
FROM balls b
JOIN players p ON b.batsman_id = p.player_id
JOIN matches m ON b.match_id = m.match_id
GROUP BY m.match_id, p.player_name;

-- 15. Player Count Per Team
SELECT t.team_name, COUNT(p.player_id) AS total_players
FROM players p
JOIN teams t ON p.team_id = t.team_id
GROUP BY t.team_name;

-- 16. Partnership Summary
SELECT player1_id, player2_id, runs_scored
FROM partnerships;

-- 17. Tournament Matches
SELECT m.match_id, m.match_date, t.tournament_name
FROM matches m
JOIN tournaments t ON m.tournament_id = t.tournament_id;

-- 18. Team Match History
SELECT winner_team_id, COUNT(*) AS wins
FROM matches
WHERE (team1_id = 1 AND team2_id = 2) OR (team1_id = 2 AND team2_id = 1)
GROUP BY winner_team_id;

-- 19. Average Run Rate Per Over
SELECT ROUND(SUM(b.runs_scored + NVL(e.wide, 0) + NVL(e.no_ball, 0)) / COUNT(DISTINCT b.over_no), 2) AS avg_runs_per_over
FROM balls b
LEFT JOIN extras e ON b.ball_id = e.ball_id;

-- 20. Most Used Bowler (by deliveries bowled)
SELECT p.player_name, COUNT(*) AS deliveries_bowled
FROM balls b
JOIN players p ON b.bowler_id = p.player_id
GROUP BY p.player_name
ORDER BY deliveries_bowled DESC;

-- 21. Most Matches Played by Player
SELECT p.player_name, COUNT(DISTINCT b.match_id) AS matches_played
FROM balls b
JOIN players p ON b.batsman_id = p.player_id
GROUP BY p.player_name
ORDER BY matches_played DESC;

-- 22. Umpires per Match
SELECT m.match_id, COUNT(mu.umpire_id) AS umpire_count
FROM match_umpires mu
JOIN matches m ON mu.match_id = m.match_id
GROUP BY m.match_id;

--23. Match_scorecard_results  
SELECT t1.team_name AS team1, t2.team_name AS team2,
       i1.total_runs AS team1_score,
       i2.total_runs AS team2_score,
       w.team_name AS winner
FROM matches m
JOIN teams t1 ON m.team1_id = t1.team_id
JOIN teams t2 ON m.team2_id = t2.team_id
LEFT JOIN teams w ON m.winner_team_id = w.team_id
JOIN innings i1 ON m.match_id = i1.match_id AND i1.batting_team_id = m.team1_id
JOIN innings i2 ON m.match_id = i2.match_id AND i2.batting_team_id = m.team2_id
WHERE m.match_id = 1;

--24. Match List with Formatted Dates
SELECT m.match_id,
       t1.team_name AS team1,
       t2.team_name AS team2,
       TO_CHAR(m.match_date, 'YYYY-MM-DD') AS match_date
FROM matches m
JOIN teams t1 ON m.team1_id = t1.team_id
JOIN teams t2 ON m.team2_id = t2.team_id;

--25. Ball-by-Ball Details
SELECT b.over_no,
       b.ball_no,
       b.runs_scored,
       b.is_wicket,
       bat.player_name AS batsman,
       bowl.player_name AS bowler
FROM balls b
JOIN players bat ON b.batsman_id = bat.player_id
JOIN players bowl ON b.bowler_id = bowl.player_id
WHERE b.match_id = 1
ORDER BY b.over_no, b.ball_no;

