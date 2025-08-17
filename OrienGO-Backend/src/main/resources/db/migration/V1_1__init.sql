create table jobs
(
    id                   bigint        not null
        primary key,
    active               boolean       not null,
    category             varchar(50)   not null
        constraint jobs_category_check
            check ((category)::text = ANY
        ((ARRAY ['HEALTH'::character varying, 'EDUCATION'::character varying, 'TECH'::character varying, 'BUSINESS'::character varying, 'ARTS'::character varying])::text[])),
    description          varchar(1000) not null,
    education            varchar(100),
    job_market           varchar(100),
    riasec_artistic      numeric(5, 2),
    riasec_conventional  numeric(5, 2),
    riasec_enterprising  numeric(5, 2),
    riasec_investigative numeric(5, 2),
    riasec_realistic     numeric(5, 2),
    riasec_social        numeric(5, 2),
    salary_range         varchar(100),
    soft_deleted         boolean       not null,
    title                varchar(100)  not null,
    version              bigint
);

alter table jobs
    owner to postgres;

create table job_tags
(
    job_id bigint not null
        constraint fky5ln7dquha6f0a63bnux2qlk
            references jobs,
    tag    varchar(50)
);

alter table job_tags
    owner to postgres;

create index idx_job_category
    on jobs (category);

create table privileges
(
    id   bigint       not null
        primary key,
    name varchar(255) not null
        constraint ukm2tnonbcaquofx1ccy060ejyc
            unique
);

alter table privileges
    owner to postgres;

create table questions
(
    id           bigint        not null
        primary key,
    category     varchar(20)   not null
        constraint questions_category_check
            check ((category)::text = ANY
        ((ARRAY ['REALISTIC'::character varying, 'INVESTIGATIVE'::character varying, 'ARTISTIC'::character varying, 'SOCIAL'::character varying, 'ENTERPRISING'::character varying, 'CONVENTIONAL'::character varying])::text[])),
    soft_deleted boolean       not null,
    text         varchar(1000) not null
);

alter table questions
    owner to postgres;

create table answer_options
(
    id           bigint       not null
        primary key,
    option_index integer      not null,
    text         varchar(500) not null,
    question_id  bigint       not null
        constraint fkfapodm8kfiu9a9a4o2r43rcgp
            references questions
);

alter table answer_options
    owner to postgres;

create index idx_answer_option_question
    on answer_options (question_id);

create index idx_question_riasec_type
    on questions (category);

create table roles
(
    id   bigint       not null
        primary key,
    name varchar(255) not null
        constraint ukofx66keruapi6vyqpv6f2or37
            unique
);

alter table roles
    owner to postgres;

create table roles_privileges
(
    role_id      bigint not null
        constraint fk629oqwrudgp5u7tewl07ayugj
            references roles,
    privilege_id bigint not null
        constraint fk5duhoc7rwt8h06avv41o41cfy
            references privileges,
    primary key (role_id, privilege_id)
);

alter table roles_privileges
    owner to postgres;

create table trainings
(
    id           bigint       not null
        primary key,
    description  varchar(1000),
    duration     varchar(50),
    highlighted  boolean      not null,
    name         varchar(255) not null,
    soft_deleted boolean      not null,
    type         varchar(50)  not null
        constraint trainings_type_check
            check ((type)::text = ANY
        ((ARRAY ['UNIVERSITY'::character varying, 'VOCATIONAL'::character varying, 'BOOTCAMP'::character varying, 'CERTIFICATION'::character varying, 'ONLINE_COURSE'::character varying, 'INTERNSHIP'::character varying, 'APPRENTICESHIP'::character varying, 'WORKSHOP'::character varying, 'SEMINAR'::character varying, 'SELF_TAUGHT'::character varying])::text[])),
    version      bigint
);

alter table trainings
    owner to postgres;

create table job_training_links
(
    job_id      bigint not null
        constraint fk2kfhxq6ptldklf7q3v7rgddj6
            references jobs,
    training_id bigint not null
        constraint fkn0kmpnaeerc5mtcoycrkpgntg
            references trainings,
    primary key (job_id, training_id)
);

alter table job_training_links
    owner to postgres;

create table training_specializations
(
    training_id    bigint not null
        constraint fk2mvpwbd4u3560kvdfb5lnd7tv
            references trainings,
    specialization varchar(100)
);

alter table training_specializations
    owner to postgres;

create index idx_training_type
    on trainings (type);

create table users
(
    user_type         varchar(31)  not null,
    id                bigint       not null
        primary key,
    age               integer      not null,
    created_at        timestamp(6),
    deleted_at        timestamp(6),
    email             varchar(365) not null
        constraint uq_users_email
            unique
        constraint idx_users_email
            unique,
    enabled           boolean      not null,
    first_name        varchar(50)  not null,
    gender            varchar(10)
        constraint users_gender_check
            check ((gender)::text = ANY
        ((ARRAY ['MALE'::character varying, 'FEMALE'::character varying, 'OTHER'::character varying])::text[])),
    is_deleted        boolean      not null,
    last_login_at     timestamp(6),
    last_name         varchar(50)  not null,
    last_seen         timestamp(6),
    phone_number      varchar(20),
    suspended         boolean      not null,
    suspended_until   timestamp(6),
    suspension_reason varchar(255),
    token_expired     boolean      not null,
    updated_at        timestamp(6)
);

alter table users
    owner to postgres;

create table admins
(
    admin_level varchar(255) not null
        constraint admins_admin_level_check
            check ((admin_level)::text = ANY
        ((ARRAY ['MANAGER'::character varying, 'STANDARD_ADMIN'::character varying])::text[])),
    department  varchar(255) not null
        constraint admins_department_check
            check ((department)::text = ANY
                   ((ARRAY ['TECH'::character varying, 'OPERATIONS'::character varying, 'HR'::character varying, 'FINANCE'::character varying, 'OTHERS'::character varying])::text[])),
    id          bigint       not null
        primary key
        constraint fkanhsicqm3lc8ya77tr7r0je18
            references users,
    created_by  bigint
        constraint fkqu7et826jq36a9ddcfa0eswbk
            references admins
);

alter table admins
    owner to postgres;

create table coaches
(
    account_privacy    varchar(255) not null
        constraint coaches_account_privacy_check
            check ((account_privacy)::text = ANY
        ((ARRAY ['PRIVATE'::character varying, 'PUBLIC'::character varying])::text[])),
    message_permission varchar(255) not null
        constraint coaches_message_permission_check
            check ((message_permission)::text = ANY
                   ((ARRAY ['NETWORK'::character varying, 'ALL'::character varying, 'NO_ONE'::character varying])::text[])),
    profile_visibility varchar(255) not null
        constraint coaches_profile_visibility_check
            check ((profile_visibility)::text = ANY
                   ((ARRAY ['PUBLIC'::character varying, 'NETWORK_ONLY'::character varying, 'PRIVATE'::character varying])::text[])),
    id                 bigint       not null
        primary key
        constraint fkbyei1g9vs5d057vur8psubw3x
            references users
);

alter table coaches
    owner to postgres;

create table medias
(
    id           bigint       not null
        primary key,
    updated_at   timestamp(6),
    content_type varchar(100) not null,
    created_at   timestamp(6),
    name         varchar(255) not null,
    size         bigint       not null,
    type         varchar(255) not null
        constraint medias_type_check
            check ((type)::text = ANY
        ((ARRAY ['PROFILE_PHOTO'::character varying, 'COVER_PHOTO'::character varying, 'PROFILE_PDF'::character varying, 'RESULT_PDF'::character varying])::text[])),
    url          varchar(500) not null,
    user_id      bigint       not null
        constraint fkl96wo4x1syvvt7mxih064je28
            references users
);

alter table medias
    owner to postgres;

create index idx_media_user_type
    on medias (user_id, type);

create table notification_settings
(
    id                          bigint  not null
        primary key,
    added_to_group              boolean not null,
    connection_requests         boolean not null,
    new_jobs                    boolean not null,
    new_messages                boolean not null,
    profile_views               boolean not null,
    receive_email_notifications boolean not null,
    test_completed_fast         boolean not null,
    test_completed_full         boolean not null,
    test_reminders              boolean not null,
    user_id                     bigint  not null
        constraint ukm9ggfvif86mvq5382j88cequn
            unique
        constraint fkmh6alfw96lc851ea0snhijfk
            references users
);

alter table notification_settings
    owner to postgres;

create index idx_notif_settings_user
    on notification_settings (user_id);

create table notifications
(
    id           bigint        not null
        primary key,
    created_at   timestamp(6)  not null,
    deleted      boolean       not null,
    message      varchar(2000) not null,
    read         boolean       not null,
    read_at      timestamp(6),
    send_email   boolean       not null,
    title        varchar(200)  not null,
    type         varchar(50)   not null
        constraint notifications_type_check
            check ((type)::text = ANY
        ((ARRAY ['TEST_COMPLETED_FAST'::character varying, 'TEST_COMPLETED_FULL'::character varying, 'CONNECTION_REQUEST_RECEIVED'::character varying, 'CONNECTION_ACCEPTED'::character varying, 'PROFILE_VIEWED'::character varying, 'NEW_MESSAGE_RECEIVED'::character varying, 'ADDED_TO_GROUP'::character varying, 'NEW_JOB_MATCHED'::character varying, 'TEST_REMINDER'::character varying, 'TEST_SUMMARY_PDF_AVAILABLE'::character varying])::text[])),
    url          varchar(512),
    recipient_id bigint        not null
        constraint fkqqnsjxlwleyjbxlmm213jaj3f
            references users
);

alter table notifications
    owner to postgres;

create index idx_notification_user
    on notifications (recipient_id);

create index idx_notification_read
    on notifications (read);

create table students
(
    account_privacy    varchar(255) not null
        constraint students_account_privacy_check
            check ((account_privacy)::text = ANY
        ((ARRAY ['PRIVATE'::character varying, 'PUBLIC'::character varying])::text[])),
    education_level    varchar(255) not null
        constraint students_education_level_check
            check ((education_level)::text = ANY
                   ((ARRAY ['MIDDLE_SCHOOL'::character varying, 'HIGH_SCHOOL'::character varying, 'POST_SECONDARY'::character varying, 'UNIVERSITY'::character varying, 'GRADUATE'::character varying, 'OTHER'::character varying])::text[])),
    field_of_study     varchar(100),
    address            varchar(255),
    city               varchar(100),
    country            varchar(100),
    region             varchar(100),
    message_permission varchar(255) not null
        constraint students_message_permission_check
            check ((message_permission)::text = ANY
                   ((ARRAY ['NETWORK'::character varying, 'ALL'::character varying, 'NO_ONE'::character varying])::text[])),
    profile_visibility varchar(255) not null
        constraint students_profile_visibility_check
            check ((profile_visibility)::text = ANY
                   ((ARRAY ['PUBLIC'::character varying, 'NETWORK_ONLY'::character varying, 'PRIVATE'::character varying])::text[])),
    school             varchar(100),
    id                 bigint       not null
        primary key
        constraint fk7xqmtv7r2eb5axni3jm0a80su
            references users
);

alter table students
    owner to postgres;

create table coach_student_connections
(
    id           bigint       not null
        primary key,
    requested_at timestamp(6) not null,
    responded_at timestamp(6),
    status       varchar(255) not null
        constraint coach_student_connections_status_check
            check ((status)::text = ANY
        ((ARRAY ['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])),
    coach_id     bigint       not null
        constraint fkf0atgg5908wonrg1tnhvpl1ys
            references coaches,
    student_id   bigint       not null
        constraint fkp5yrs9p401e8pyltvlmh5qen
            references students,
    constraint uk4g7fjxq068m485fnfpyidxqtw
        unique (coach_id, student_id)
);

alter table coach_student_connections
    owner to postgres;

create table conversations
(
    id              bigint not null
        primary key,
    created_at      timestamp(6),
    last_message_at timestamp(6),
    version         bigint,
    coach_id        bigint not null
        constraint fk877ed8s0yq49thogxcad5y0au
            references coaches,
    student_id      bigint not null
        constraint fko6u160qfx7o9whns9uoe9ic6w
            references students,
    constraint uka6hq6endguawa78mp52abp6hu
        unique (coach_id, student_id)
);

alter table conversations
    owner to postgres;

create index idx_conversation_last_message
    on conversations (last_message_at);

create table messages
(
    id              bigint        not null
        primary key,
    content         varchar(2000) not null,
    created_at      timestamp(6),
    read            boolean       not null,
    read_at         timestamp(6),
    updated_at      timestamp(6),
    conversation_id bigint        not null
        constraint fkt492th6wsovh1nush5yl5jj8e
            references conversations,
    sender_id       bigint        not null
        constraint fk4ui4nnwntodh6wjvck53dbk9m
            references users
);

alter table messages
    owner to postgres;

create index idx_messages_conversation
    on messages (conversation_id, created_at);

create index idx_messages_read
    on messages (read);

create table student_job_links
(
    id         bigint       not null
        primary key,
    created_at timestamp(6) not null,
    type       varchar(20)  not null
        constraint student_job_links_type_check
            check ((type)::text = ANY ((ARRAY ['SAVED'::character varying, 'FAVORITE'::character varying])::text[])),
    job_id     bigint       not null
        constraint fk5vxgw4h1evdpxemkd9mu15ohp
            references jobs,
    student_id bigint       not null
        constraint fkngtxi4ey5myt895bierjc7m0c
            references students
);

alter table student_job_links
    owner to postgres;

create index idx_student_job_type
    on student_job_links (student_id, job_id, type);

create table student_training_links
(
    id          bigint       not null
        primary key,
    created_at  timestamp(6) not null,
    type        varchar(20)  not null
        constraint student_training_links_type_check
            check ((type)::text = ANY ((ARRAY ['SAVED'::character varying, 'FAVORITE'::character varying])::text[])),
    student_id  bigint       not null
        constraint fkn3gmhpwfbrrb3r5ar842xv2sy
            references students,
    training_id bigint       not null
        constraint fklr3qoras1b3722pgx9hirkk66
            references trainings
);

alter table student_training_links
    owner to postgres;

create index idx_student_training_type
    on student_training_links (student_id, training_id, type);

create table tests
(
    id                       bigint       not null
        primary key,
    answered_questions_count integer,
    completed_at             timestamp(6),
    duration_minutes         integer,
    questions_count          integer,
    soft_deleted             boolean      not null,
    started_at               timestamp(6) not null,
    status                   varchar(20)  not null
        constraint tests_status_check
            check ((status)::text = ANY
        ((ARRAY ['PENDING'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])),
    type                     varchar(20)
        constraint tests_type_check
            check ((type)::text = ANY ((ARRAY ['FAST'::character varying, 'COMPLETE'::character varying])::text[])),
    updated_at               timestamp(6) not null,
    student_id               bigint       not null
        constraint fk5jo5ip3k2nd4n33tms38306o1
            references students
);

alter table tests
    owner to postgres;

create table test_questions
(
    id               bigint not null
        primary key,
    chosen_answer_id bigint
        constraint fkmpmha8gvmk8qd2c1kywex2i1i
            references answer_options,
    question_id      bigint not null
        constraint fkk171b2q5ikck3f9yk4n9lbyvw
            references questions,
    test_id          bigint not null
        constraint fkq1jpgjcbjbulxvhdkctcxhg12
            references tests,
    constraint uk87u0mgtt2sr0vp3hln8my1g8h
        unique (test_id, question_id)
);

alter table test_questions
    owner to postgres;

create table test_results
(
    id                        bigint      not null
        primary key,
    dominant_type             varchar(20) not null
        constraint test_results_dominant_type_check
            check ((dominant_type)::text = ANY
        ((ARRAY ['REALISTIC'::character varying, 'INVESTIGATIVE'::character varying, 'ARTISTIC'::character varying, 'SOCIAL'::character varying, 'ENTERPRISING'::character varying, 'CONVENTIONAL'::character varying])::text[])),
    dominant_type_description varchar(20),
    downloaded                boolean     not null,
    key_points                varchar(4000),
    shared                    boolean     not null,
    soft_deleted              boolean     not null,
    pdf_id                    bigint
        constraint ukrnj6sqhdxkgh27s73uwpov0i2
            unique
        constraint fk5ap9cwdk7tao0go85yvrumrpt
            references medias,
    test_id                   bigint      not null
        constraint uk2voayghfxl8nevqyqe8dkxgrs
            unique
        constraint fkeb5e15t9e5hn11gbkuub0xeln
            references tests
);

alter table test_results
    owner to postgres;

create table job_recommendations
(
    id               bigint  not null
        primary key,
    highlighted      boolean not null,
    match_percentage integer not null,
    job_id           bigint  not null
        constraint fktfq48elu7ctvpwn38edj74cdy
            references jobs,
    test_result_id   bigint  not null
        constraint fka54oymmerbyitwnxh85psrn1j
            references test_results
);

alter table job_recommendations
    owner to postgres;

create index idx_recommendation_result
    on job_recommendations (test_result_id);

create index idx_recommendation_job
    on job_recommendations (job_id);

create table personalized_jobs
(
    id                    bigint       not null
        primary key,
    apply_url             varchar(1000),
    category              varchar(100),
    company_name          varchar(255),
    created_at            timestamp(6),
    description           varchar(1000),
    duration              varchar(100),
    expiration_date       date,
    highlighted           boolean      not null,
    job_type              varchar(50),
    location              varchar(255),
    match_percentage      integer      not null,
    posted_date           date,
    salary_range          varchar(100),
    soft_deleted          boolean      not null,
    source                varchar(100),
    title                 varchar(255) not null,
    job_recommendation_id bigint
        constraint fkq8rwm4w2ok3c7qqask9h4e7r2
            references job_recommendations
);

alter table personalized_jobs
    owner to postgres;

create table job_advantages
(
    job_id    bigint not null
        constraint fk88w2u2fsys5qfmwbu09ro13qa
            references personalized_jobs,
    advantage varchar(255)
);

alter table job_advantages
    owner to postgres;

create table job_required_skills
(
    job_id bigint not null
        constraint fkic59e3odvcv4wol8jctku72aw
            references personalized_jobs,
    skill  varchar(255)
);

alter table job_required_skills
    owner to postgres;

create index idx_job_expiry
    on personalized_jobs (expiration_date);

create index idx_personalized_job_category
    on personalized_jobs (category);

create table student_personalized_job_links
(
    id                  bigint       not null
        primary key,
    created_at          timestamp(6) not null,
    type                varchar(20)  not null
        constraint student_personalized_job_links_type_check
            check ((type)::text = ANY ((ARRAY ['SAVED'::character varying, 'FAVORITE'::character varying])::text[])),
    personalized_job_id bigint       not null
        constraint fkgxrxd97fdqe4gt2be7ubu412p
            references personalized_jobs,
    student_id          bigint       not null
        constraint fkrl74qrjff35eefsdbw86fpeuc
            references students
);

alter table student_personalized_job_links
    owner to postgres;

create index idx_student_personalized_job_type
    on student_personalized_job_links (student_id, personalized_job_id, type);

create table test_result_scores
(
    result_id  bigint           not null
        constraint fk3dvpiqgs2l1btkmr0ldn76b0w
            references test_results,
    percentage double precision not null,
    type       varchar(20)      not null
        constraint test_result_scores_type_check
            check ((type)::text = ANY
        ((ARRAY ['REALISTIC'::character varying, 'INVESTIGATIVE'::character varying, 'ARTISTIC'::character varying, 'SOCIAL'::character varying, 'ENTERPRISING'::character varying, 'CONVENTIONAL'::character varying])::text[])),
    primary key (result_id, type)
);

alter table test_result_scores
    owner to postgres;

create index idx_result_test
    on test_results (test_id);

create index idx_result_dominant
    on test_results (dominant_type);

create index idx_tests_student
    on tests (student_id);

create index idx_tests_status
    on tests (status);

create table tokens
(
    id          bigint       not null
        primary key,
    created_at  timestamp(6) not null,
    expired     boolean      not null,
    expires_at  timestamp(6) not null,
    revoked     boolean      not null,
    revoked_at  timestamp(6),
    token_type  varchar(50)  not null
        constraint tokens_token_type_check
            check ((token_type)::text = ANY
        ((ARRAY ['ACCESS'::character varying, 'REFRESH'::character varying, 'PASSWORD_RESET'::character varying, 'EMAIL_VERIFICATION'::character varying])::text[])),
    token_value varchar(512) not null
        constraint idx_token_value
            unique,
    user_id     bigint       not null
        constraint fk2dylsfo39lgjyqml2tbe0b0ss
            references users
);

alter table tokens
    owner to postgres;

create index idx_token_user_id
    on tokens (user_id);

create table training_recommendations
(
    id               bigint  not null
        primary key,
    highlighted      boolean not null,
    match_percentage integer not null,
    test_result_id   bigint  not null
        constraint fk9lfbqe54vcaymjj76his6cceg
            references test_results,
    training_id      bigint  not null
        constraint fkjrhfo6ej53fa1yc4tck1ycj8c
            references trainings
);

alter table training_recommendations
    owner to postgres;

create index idx_users_last_seen
    on users (last_seen);

create index idx_users_enabled
    on users (enabled);

create index idx_users_suspended
    on users (suspended);

create table users_roles
(
    user_id bigint not null
        constraint fk2o0jvgh89lemvvo17cbqvdxaa
            references users,
    role_id bigint not null
        constraint fkj6m8fwv7oqv74fcehir1a9ffy
            references roles,
    primary key (user_id, role_id)
);

alter table users_roles
    owner to postgres;

create sequence answer_option_seq
    increment by 50;

alter sequence answer_option_seq owner to postgres;

create sequence conn_seq
    increment by 50;

alter sequence conn_seq owner to postgres;

create sequence conv_seq
    increment by 50;

alter sequence conv_seq owner to postgres;

create sequence job_recommendation_seq
    increment by 50;

alter sequence job_recommendation_seq owner to postgres;

create sequence job_seq
    increment by 50;

alter sequence job_seq owner to postgres;

create sequence medias_seq
    increment by 50;

alter sequence medias_seq owner to postgres;

create sequence msg_seq
    increment by 50;

alter sequence msg_seq owner to postgres;

create sequence notif_seq
    increment by 50;

alter sequence notif_seq owner to postgres;

create sequence notif_settings_seq
    increment by 50;

alter sequence notif_settings_seq owner to postgres;

create sequence personalized_job_seq
    increment by 50;

alter sequence personalized_job_seq owner to postgres;

create sequence privileges_seq
    increment by 50;

alter sequence privileges_seq owner to postgres;

create sequence question_seq
    increment by 50;

alter sequence question_seq owner to postgres;

create sequence roles_seq
    increment by 50;

alter sequence roles_seq owner to postgres;

create sequence student_job_link_seq
    increment by 50;

alter sequence student_job_link_seq owner to postgres;

create sequence student_personalized_job_link_seq
    increment by 50;

alter sequence student_personalized_job_link_seq owner to postgres;

create sequence student_training_link_seq
    increment by 50;

alter sequence student_training_link_seq owner to postgres;

create sequence test_question_seq
    increment by 50;

alter sequence test_question_seq owner to postgres;

create sequence test_result_seq
    increment by 50;

alter sequence test_result_seq owner to postgres;

create sequence test_seq
    increment by 50;

alter sequence test_seq owner to postgres;

create sequence tokens_seq
    increment by 50;

alter sequence tokens_seq owner to postgres;

create sequence training_rec_seq
    increment by 50;

alter sequence training_rec_seq owner to postgres;

create sequence training_seq
    increment by 50;

alter sequence training_seq owner to postgres;

create sequence users_seq
    increment by 50;

alter sequence users_seq owner to postgres;

