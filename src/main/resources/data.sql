-- Users
INSERT INTO users (id, first_name, last_name, role_id, email, password)VALUES (1,'USER1', 'ASE', 'ROLE_EMPLOYEE', 'user1_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');
INSERT INTO users (id, first_name, last_name, role_id, email, password)VALUES (2,'USER2', 'ASE', 'ROLE_EMPLOYEE', 'user2_mogilev@yopmail.com','$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');
INSERT INTO users (id, first_name, last_name, role_id, email, password)VALUES (3,'MANAGER1', 'ASE', 'ROLE_MANAGER', 'manager1_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');
INSERT INTO users (id, first_name, last_name, role_id, email, password)VALUES (4,'MANAGER2', 'ASE', 'ROLE_MANAGER', 'manager2_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');
INSERT INTO users (id, first_name, last_name, role_id, email, password)VALUES (5,'ENGINEER1', 'ASE', 'ROLE_ENGINEER', 'engineer1_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');
INSERT INTO users (id, first_name, last_name, role_id, email, password)VALUES (6,'ENGINEER2', 'ASE', 'ROLE_ENGINEER', 'engineer2_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');

--Category
INSERT INTO category (id, name) VALUES(1, 'Application & Service');
INSERT INTO category (id, name) VALUES(2, 'Benefits & Paper Work');
INSERT INTO category (id, name) VALUES(3, 'Hardware & Software');
INSERT INTO category (id, name) VALUES(4, 'People Management');
INSERT INTO category (id, name) VALUES(5, 'Security & Access');
INSERT INTO category (id, name) VALUES(6, 'Workplaces & Facilities');

--Tickets
INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(1, 'simplename', 'dasdadsasd', '2018-09-05', '2018-09-05', 1, 1 , 'NEW', 1, 'LOW', 3);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(2, 'simplename', 'dasdadsasd', '2018-09-05', '2018-09-05', 3, 3 , 'NEW', 2, 'LOW', 3);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(3, 'simpleDone', 'dasdadsasd', '2018-09-05', '2018-09-05', 3, 3 , 'DONE', 3, 'LOW', 3);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(4, 'Ticket for engineer APPROVED', 'dasdadsasd', '2018-09-05', '2018-09-04', 3, 3, 'APPROVED', 4, 'AVERAGE', 5);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(5, 'Ticket for engineer DONE', 'dasdadsasd', '2018-09-05', '2018-09-03', 5, 3 , 'DONE', 5, 'CRITICAL', 5);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(6, 'Ticket for engineer DONE', 'dasdadsasd', '2018-09-05', '2018-09-03', 5, 3 , 'DRAFT', 6, 'CRITICAL', 5);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(7, 'Ticket for engineer DONE', 'dasdadsasd', '2018-09-05', '2018-09-03', 3, 1 , 'DRAFT', 6, 'CRITICAL', 5);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(8, 'Ticket for engineer DONE', 'dasdadsasd', '2018-09-05', '2018-09-03', 3, 1 , 'DRAFT', 6, 'CRITICAL', 5);

INSERT INTO tickets (id, name, description, created_on, desired_resolution_date, assignee_id, owner_id, state_id, category_id, urgency_id, approver_id)
VALUES(9, 'Ticket for engineer DONE', 'dasdadsasd', '2018-09-05', '2018-09-03', 3, 1 , 'NEW', 6, 'CRITICAL', 5);


--History
INSERT INTO history (id, date, action, description, ticket_id, user_id)
VALUES(1, '2022-09-05', 'MOCK ACTION!!!!!!!!', 'MOCK_DESCRIPTION', 1, 1);

INSERT INTO history (id, date, action, description, ticket_id, user_id)
VALUES(2, '2022-09-05', 'MOCK ACTION!!!!!!!!', 'MOCK_DESCRIPTION', 2, 2);

INSERT INTO history (id, date, action, description, ticket_id, user_id)
VALUES(3, '2022-09-05', 'MOCK ACTION!!!!!!!!', 'MOCK_DESCRIPTION', 3, 3);

INSERT INTO history (id, date, action, description, ticket_id, user_id)
VALUES(4, '2022-09-05', 'MOCK ACTION!!!!!!!!', 'MOCK_DESCRIPTION', 4, 4);

INSERT INTO history (id, date, action, description, ticket_id, user_id)
VALUES(5, '2022-09-05', 'MOCK ACTION!!!!!!!!', 'MOCK_DESCRIPTION', 5, 5);

INSERT INTO history (id, date, action, description, ticket_id, user_id)
VALUES(6, '2022-09-05', 'MOCK ACTION!!!!!!!!', 'MOCK_DESCRIPTION', 6, 6);

--Comments
INSERT INTO comments (id, text, date, user_id, ticket_id)
VALUES(1, 'mock text', '2022-09-05', 1, 1);

INSERT INTO comments (id, text, date, user_id, ticket_id)
VALUES(2, 'mock text', '2022-09-05', 2, 2);

INSERT INTO comments (id, text, date, user_id, ticket_id)
VALUES(3, 'mock text', '2022-09-05', 3, 3);

INSERT INTO comments (id, text, date, user_id, ticket_id)
VALUES(4, 'mock text', '2022-09-05', 4, 4);

INSERT INTO comments (id, text, date, user_id, ticket_id)
VALUES(5, 'mock text', '2022-09-05', 5, 5);

INSERT INTO comments (id, text, date, user_id, ticket_id)
VALUES(6, 'mock text', '2022-09-05', 6, 6);
