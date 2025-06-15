CREATE TABLE `user` (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(128) NOT NULL,
    profile VARCHAR(255),
    school VARCHAR(128),
    grade VARCHAR(64),
    skill_description VARCHAR(255)
);

CREATE TABLE `group` (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    description VARCHAR(255),
    creation_date DATETIME,
    volume INT,
    visibility INT,
    approval_required INT,
    disbanded INT,
    leader_id INT,
    CONSTRAINT fk_group_leader FOREIGN KEY (leader_id) REFERENCES `user`(user_id)
);

CREATE TABLE `membership` (
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    join_date DATETIME,
    PRIMARY KEY (group_id, user_id),
    CONSTRAINT fk_membership_group FOREIGN KEY (group_id) REFERENCES `group`(group_id),
    CONSTRAINT fk_membership_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

CREATE TABLE `join_request` (
    join_request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    creation_time DATETIME,
    description VARCHAR(255),
    state VARCHAR(32),
    CONSTRAINT fk_joinrequest_user FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    CONSTRAINT fk_joinrequest_group FOREIGN KEY (group_id) REFERENCES `group`(group_id)
);

CREATE TABLE `task` (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    user_id INT,
    title VARCHAR(128),
    description VARCHAR(255),
    state VARCHAR(32),
    deadline DATETIME,
    CONSTRAINT fk_task_group FOREIGN KEY (group_id) REFERENCES `group`(group_id),
    CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

CREATE TABLE `submission` (
    submission_id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    creation_date DATETIME,
    text TEXT,
    file_name VARCHAR(255),
    file_path VARCHAR(255),
    CONSTRAINT fk_submission_task FOREIGN KEY (task_id) REFERENCES `task`(task_id),
    CONSTRAINT fk_submission_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

CREATE TABLE `notification` (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(128),
    content VARCHAR(255),
    join_request_id INT,
    has_read INT,
    creation_time DATETIME,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    CONSTRAINT fk_notification_joinrequest FOREIGN KEY (join_request_id) REFERENCES `join_request`(join_request_id)
);
