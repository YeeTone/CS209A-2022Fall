PRAGMA foreign_keys = TRUE ;

drop table if exists git_user;

create table if not exists git_user(
    user_login varchar primary key,
    full_name varchar,
    user_id integer not null,
    email varchar
);

drop table if exists git_repo;

create table if not exists git_repo(
    repo_id integer primary key,
    repo_owner varchar not null,
    repo_name varchar not null
);

drop table if exists git_commit;

create table if not exists git_commit(
    commit_sha varchar primary key,
    commit_author_name varchar,
    commit_date date,
    repo_owner varchar,
    repo_name varchar,
    daytime_label varchar,
    weektime_label varchar
);

drop table if exists git_issue;

create table if not exists git_issue(
    issue_id integer primary key,
    create_at date,
    title varchar,
    closed_at date,
    creator_login varchar,
    state varchar,
    repo_owner varchar,
    repo_name varchar
);

drop table if exists git_release;

create table if not exists git_release(
    release_id integer primary key,
    published_at date,
    author_login varchar,
    release_name varchar,
    repo_owner varchar,
    repo_name varchar
);

drop table if exists git_hotword;

create table if not exists git_hotword(
    type varchar,
    word varchar,
    repo_owner varchar,
    repo_name varchar
);