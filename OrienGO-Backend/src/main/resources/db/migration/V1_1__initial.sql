--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admins (
                               admin_level character varying(255) NOT NULL,
                               department character varying(255) NOT NULL,
                               id bigint NOT NULL,
                               created_by bigint,
                               CONSTRAINT admins_admin_level_check CHECK (((admin_level)::text = ANY ((ARRAY['MANAGER'::character varying, 'STANDARD_ADMIN'::character varying])::text[]))),
                               CONSTRAINT admins_department_check CHECK (((department)::text = ANY ((ARRAY['TECH'::character varying, 'OPERATIONS'::character varying, 'HR'::character varying, 'FINANCE'::character varying])::text[])))
);


ALTER TABLE public.admins OWNER TO postgres;

--
-- Name: answer_option_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.answer_option_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.answer_option_seq OWNER TO postgres;

--
-- Name: answer_options; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.answer_options (
                                       id bigint NOT NULL,
                                       option_index integer NOT NULL,
                                       text character varying(500) NOT NULL,
                                       question_id bigint NOT NULL
);


ALTER TABLE public.answer_options OWNER TO postgres;

--
-- Name: coach_student_connections; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coach_student_connections (
                                                  id bigint NOT NULL,
                                                  requested_at timestamp(6) without time zone NOT NULL,
                                                  responded_at timestamp(6) without time zone,
                                                  status character varying(255) NOT NULL,
                                                  coach_id bigint NOT NULL,
                                                  student_id bigint NOT NULL,
                                                  CONSTRAINT coach_student_connections_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.coach_student_connections OWNER TO postgres;

--
-- Name: coaches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coaches (
                                account_privacy character varying(255) NOT NULL,
                                message_permission character varying(255) NOT NULL,
                                profile_visibility character varying(255) NOT NULL,
                                id bigint NOT NULL,
                                CONSTRAINT coaches_account_privacy_check CHECK (((account_privacy)::text = ANY ((ARRAY['PRIVATE'::character varying, 'PUBLIC'::character varying])::text[]))),
                                CONSTRAINT coaches_message_permission_check CHECK (((message_permission)::text = ANY ((ARRAY['NETWORK'::character varying, 'ALL'::character varying, 'NO_ONE'::character varying])::text[]))),
                                CONSTRAINT coaches_profile_visibility_check CHECK (((profile_visibility)::text = ANY ((ARRAY['PUBLIC'::character varying, 'NETWORK_ONLY'::character varying, 'PRIVATE'::character varying])::text[])))
);


ALTER TABLE public.coaches OWNER TO postgres;

--
-- Name: conn_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.conn_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.conn_seq OWNER TO postgres;

--
-- Name: conv_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.conv_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.conv_seq OWNER TO postgres;

--
-- Name: conversations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conversations (
                                      id bigint NOT NULL,
                                      created_at timestamp(6) without time zone,
                                      last_message_at timestamp(6) without time zone,
                                      version bigint,
                                      coach_id bigint NOT NULL,
                                      student_id bigint NOT NULL
);


ALTER TABLE public.conversations OWNER TO postgres;

--
-- Name: job_advantages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_advantages (
                                       job_id bigint NOT NULL,
                                       advantage character varying(255)
);


ALTER TABLE public.job_advantages OWNER TO postgres;

--
-- Name: job_recommendation_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_recommendation_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.job_recommendation_seq OWNER TO postgres;

--
-- Name: job_recommendations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_recommendations (
                                            id bigint NOT NULL,
                                            highlighted boolean NOT NULL,
                                            match_percentage integer NOT NULL,
                                            job_id bigint NOT NULL,
                                            test_result_id bigint NOT NULL
);


ALTER TABLE public.job_recommendations OWNER TO postgres;

--
-- Name: job_required_skills; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_required_skills (
                                            job_id bigint NOT NULL,
                                            skill character varying(255)
);


ALTER TABLE public.job_required_skills OWNER TO postgres;

--
-- Name: job_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.job_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.job_seq OWNER TO postgres;

--
-- Name: job_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_tags (
                                 job_id bigint NOT NULL,
                                 tag character varying(50)
);


ALTER TABLE public.job_tags OWNER TO postgres;

--
-- Name: job_training_links; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_training_links (
                                           job_id bigint NOT NULL,
                                           training_id bigint NOT NULL
);


ALTER TABLE public.job_training_links OWNER TO postgres;

--
-- Name: jobs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jobs (
                             id bigint NOT NULL,
                             category character varying(50) NOT NULL,
                             description character varying(1000) NOT NULL,
                             education character varying(100),
                             job_market character varying(100),
                             salary_range character varying(100),
                             soft_deleted boolean NOT NULL,
                             title character varying(100) NOT NULL,
                             version bigint,
                             CONSTRAINT jobs_category_check CHECK (((category)::text = ANY ((ARRAY['HEALTH'::character varying, 'EDUCATION'::character varying, 'TECH'::character varying, 'BUSINESS'::character varying, 'ARTS'::character varying])::text[])))
);


ALTER TABLE public.jobs OWNER TO postgres;

--
-- Name: medias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medias (
                               id bigint NOT NULL,
                               content_type character varying(100) NOT NULL,
                               created_at timestamp(6) without time zone,
                               name character varying(255) NOT NULL,
                               size bigint NOT NULL,
                               type character varying(255) NOT NULL,
                               url character varying(500) NOT NULL,
                               user_id bigint NOT NULL,
                               CONSTRAINT medias_type_check CHECK (((type)::text = ANY ((ARRAY['PROFILE_PHOTO'::character varying, 'COVER_PHOTO'::character varying, 'PROFILE_PDF'::character varying, 'RESULT_PDF'::character varying])::text[])))
);


ALTER TABLE public.medias OWNER TO postgres;

--
-- Name: medias_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.medias_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.medias_seq OWNER TO postgres;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
                                 id bigint NOT NULL,
                                 content character varying(2000) NOT NULL,
                                 created_at timestamp(6) without time zone,
                                 read boolean NOT NULL,
                                 read_at timestamp(6) without time zone,
                                 updated_at timestamp(6) without time zone,
                                 conversation_id bigint NOT NULL,
                                 sender_id bigint NOT NULL
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: msg_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.msg_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.msg_seq OWNER TO postgres;

--
-- Name: notif_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notif_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.notif_seq OWNER TO postgres;

--
-- Name: notif_settings_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notif_settings_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.notif_settings_seq OWNER TO postgres;

--
-- Name: notification_settings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification_settings (
                                              id bigint NOT NULL,
                                              added_to_group boolean NOT NULL,
                                              connection_requests boolean NOT NULL,
                                              new_jobs boolean NOT NULL,
                                              new_messages boolean NOT NULL,
                                              profile_views boolean NOT NULL,
                                              receive_email_notifications boolean NOT NULL,
                                              test_completed_fast boolean NOT NULL,
                                              test_completed_full boolean NOT NULL,
                                              test_reminders boolean NOT NULL,
                                              user_id bigint NOT NULL
);


ALTER TABLE public.notification_settings OWNER TO postgres;

--
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notifications (
                                      id bigint NOT NULL,
                                      created_at timestamp(6) without time zone NOT NULL,
                                      deleted boolean NOT NULL,
                                      message character varying(2000) NOT NULL,
                                      read boolean NOT NULL,
                                      read_at timestamp(6) without time zone,
                                      send_email boolean NOT NULL,
                                      title character varying(200) NOT NULL,
                                      type character varying(50) NOT NULL,
                                      url character varying(512),
                                      version bigint,
                                      recipient_id bigint NOT NULL,
                                      CONSTRAINT notifications_type_check CHECK (((type)::text = ANY ((ARRAY['TEST_COMPLETED_FAST'::character varying, 'TEST_COMPLETED_FULL'::character varying, 'CONNECTION_REQUEST_RECEIVED'::character varying, 'CONNECTION_ACCEPTED'::character varying, 'PROFILE_VIEWED'::character varying, 'NEW_MESSAGE_RECEIVED'::character varying, 'ADDED_TO_GROUP'::character varying, 'NEW_JOB_MATCHED'::character varying, 'TEST_REMINDER'::character varying, 'TEST_SUMMARY_PDF_AVAILABLE'::character varying])::text[])))
);


ALTER TABLE public.notifications OWNER TO postgres;

--
-- Name: personalized_job_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.personalized_job_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.personalized_job_seq OWNER TO postgres;

--
-- Name: personalized_jobs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.personalized_jobs (
                                          id bigint NOT NULL,
                                          apply_url character varying(1000),
                                          category character varying(100),
                                          company_name character varying(255),
                                          created_at timestamp(6) without time zone,
                                          description character varying(1000),
                                          duration character varying(100),
                                          expiration_date date,
                                          highlighted boolean NOT NULL,
                                          job_type character varying(50),
                                          location character varying(255),
                                          match_percentage integer NOT NULL,
                                          posted_date date,
                                          salary_range character varying(100),
                                          soft_deleted boolean NOT NULL,
                                          source character varying(100),
                                          title character varying(255) NOT NULL,
                                          job_recommendation_id bigint
);


ALTER TABLE public.personalized_jobs OWNER TO postgres;

--
-- Name: privileges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.privileges (
                                   id bigint NOT NULL,
                                   name character varying(255) NOT NULL
);


ALTER TABLE public.privileges OWNER TO postgres;

--
-- Name: privileges_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.privileges_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.privileges_seq OWNER TO postgres;

--
-- Name: question_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.question_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.question_seq OWNER TO postgres;

--
-- Name: questions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.questions (
                                  id bigint NOT NULL,
                                  category character varying(20) NOT NULL,
                                  soft_deleted boolean NOT NULL,
                                  text character varying(1000) NOT NULL,
                                  CONSTRAINT questions_category_check CHECK (((category)::text = ANY ((ARRAY['REALISTIC'::character varying, 'INVESTIGATIVE'::character varying, 'ARTISTIC'::character varying, 'SOCIAL'::character varying, 'ENTERPRISING'::character varying, 'CONVENTIONAL'::character varying])::text[])))
);


ALTER TABLE public.questions OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
                              id bigint NOT NULL,
                              name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_privileges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles_privileges (
                                         role_id bigint NOT NULL,
                                         privilege_id bigint NOT NULL
);


ALTER TABLE public.roles_privileges OWNER TO postgres;

--
-- Name: roles_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_seq OWNER TO postgres;

--
-- Name: student_job_link_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_job_link_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_job_link_seq OWNER TO postgres;

--
-- Name: student_job_links; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_job_links (
                                          id bigint NOT NULL,
                                          created_at timestamp(6) without time zone NOT NULL,
                                          type character varying(20) NOT NULL,
                                          job_id bigint NOT NULL,
                                          student_id bigint NOT NULL,
                                          CONSTRAINT student_job_links_type_check CHECK (((type)::text = ANY ((ARRAY['SAVED'::character varying, 'FAVORITE'::character varying])::text[])))
);


ALTER TABLE public.student_job_links OWNER TO postgres;

--
-- Name: student_personalized_job_link_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_personalized_job_link_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_personalized_job_link_seq OWNER TO postgres;

--
-- Name: student_personalized_job_links; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_personalized_job_links (
                                                       id bigint NOT NULL,
                                                       created_at timestamp(6) without time zone NOT NULL,
                                                       type character varying(20) NOT NULL,
                                                       personalized_job_id bigint NOT NULL,
                                                       student_id bigint NOT NULL,
                                                       CONSTRAINT student_personalized_job_links_type_check CHECK (((type)::text = ANY ((ARRAY['SAVED'::character varying, 'FAVORITE'::character varying])::text[])))
);


ALTER TABLE public.student_personalized_job_links OWNER TO postgres;

--
-- Name: student_training_link_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_training_link_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_training_link_seq OWNER TO postgres;

--
-- Name: student_training_links; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_training_links (
                                               id bigint NOT NULL,
                                               created_at timestamp(6) without time zone NOT NULL,
                                               type character varying(20) NOT NULL,
                                               student_id bigint NOT NULL,
                                               training_id bigint NOT NULL,
                                               CONSTRAINT student_training_links_type_check CHECK (((type)::text = ANY ((ARRAY['SAVED'::character varying, 'FAVORITE'::character varying])::text[])))
);


ALTER TABLE public.student_training_links OWNER TO postgres;

--
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
                                 account_privacy character varying(255) NOT NULL,
                                 education_level character varying(255) NOT NULL,
                                 field_of_study character varying(100),
                                 address character varying(255),
                                 city character varying(100),
                                 country character varying(100),
                                 region character varying(100),
                                 message_permission character varying(255) NOT NULL,
                                 profile_visibility character varying(255) NOT NULL,
                                 school character varying(100),
                                 id bigint NOT NULL,
                                 CONSTRAINT students_account_privacy_check CHECK (((account_privacy)::text = ANY ((ARRAY['PRIVATE'::character varying, 'PUBLIC'::character varying])::text[]))),
                                 CONSTRAINT students_education_level_check CHECK (((education_level)::text = ANY ((ARRAY['MIDDLE_SCHOOL'::character varying, 'HIGH_SCHOOL'::character varying, 'POST_SECONDARY'::character varying, 'UNIVERSITY'::character varying, 'GRADUATE'::character varying, 'OTHER'::character varying])::text[]))),
                                 CONSTRAINT students_message_permission_check CHECK (((message_permission)::text = ANY ((ARRAY['NETWORK'::character varying, 'ALL'::character varying, 'NO_ONE'::character varying])::text[]))),
                                 CONSTRAINT students_profile_visibility_check CHECK (((profile_visibility)::text = ANY ((ARRAY['PUBLIC'::character varying, 'NETWORK_ONLY'::character varying, 'PRIVATE'::character varying])::text[])))
);


ALTER TABLE public.students OWNER TO postgres;

--
-- Name: test_questions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_questions (
                                       test_id bigint NOT NULL,
                                       question_id bigint NOT NULL
);


ALTER TABLE public.test_questions OWNER TO postgres;

--
-- Name: test_result_scores; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_result_scores (
                                           result_id bigint NOT NULL,
                                           percentage integer NOT NULL,
                                           type character varying(20) NOT NULL,
                                           CONSTRAINT test_result_scores_type_check CHECK (((type)::text = ANY ((ARRAY['REALISTIC'::character varying, 'INVESTIGATIVE'::character varying, 'ARTISTIC'::character varying, 'SOCIAL'::character varying, 'ENTERPRISING'::character varying, 'CONVENTIONAL'::character varying])::text[])))
);


ALTER TABLE public.test_result_scores OWNER TO postgres;

--
-- Name: test_result_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.test_result_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.test_result_seq OWNER TO postgres;

--
-- Name: test_results; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.test_results (
                                     id bigint NOT NULL,
                                     dominant_type character varying(20) NOT NULL,
                                     dominant_type_description character varying(20),
                                     downloaded boolean NOT NULL,
                                     key_points character varying(4000),
                                     shared boolean NOT NULL,
                                     soft_deleted boolean NOT NULL,
                                     pdf_id bigint,
                                     test_id bigint NOT NULL,
                                     CONSTRAINT test_results_dominant_type_check CHECK (((dominant_type)::text = ANY ((ARRAY['REALISTIC'::character varying, 'INVESTIGATIVE'::character varying, 'ARTISTIC'::character varying, 'SOCIAL'::character varying, 'ENTERPRISING'::character varying, 'CONVENTIONAL'::character varying])::text[])))
);


ALTER TABLE public.test_results OWNER TO postgres;

--
-- Name: test_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.test_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.test_seq OWNER TO postgres;

--
-- Name: tests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tests (
                              id bigint NOT NULL,
                              completed_at timestamp(6) without time zone,
                              duration_minutes integer NOT NULL,
                              questions_count integer NOT NULL,
                              soft_deleted boolean NOT NULL,
                              started_at timestamp(6) without time zone NOT NULL,
                              status character varying(20) NOT NULL,
                              type character varying(20) NOT NULL,
                              student_id bigint NOT NULL,
                              CONSTRAINT tests_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[]))),
                              CONSTRAINT tests_type_check CHECK (((type)::text = ANY ((ARRAY['FAST'::character varying, 'COMPLETE'::character varying])::text[])))
);


ALTER TABLE public.tests OWNER TO postgres;

--
-- Name: tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tokens (
                               id bigint NOT NULL,
                               created_at timestamp(6) without time zone NOT NULL,
                               expires_at timestamp(6) without time zone NOT NULL,
                               revoked boolean NOT NULL,
                               revoked_at timestamp(6) without time zone,
                               token_hash character varying(255),
                               token_type character varying(50) NOT NULL,
                               token_value character varying(512) NOT NULL,
                               user_id bigint NOT NULL,
                               CONSTRAINT tokens_token_type_check CHECK (((token_type)::text = ANY ((ARRAY['ACCESS'::character varying, 'REFRESH'::character varying, 'PASSWORD_RESET'::character varying, 'EMAIL_VERIFICATION'::character varying])::text[])))
);


ALTER TABLE public.tokens OWNER TO postgres;

--
-- Name: tokens_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tokens_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tokens_seq OWNER TO postgres;

--
-- Name: training_rec_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.training_rec_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.training_rec_seq OWNER TO postgres;

--
-- Name: training_recommendations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.training_recommendations (
                                                 id bigint NOT NULL,
                                                 highlighted boolean NOT NULL,
                                                 match_percentage integer NOT NULL,
                                                 test_result_id bigint NOT NULL,
                                                 training_id bigint NOT NULL
);


ALTER TABLE public.training_recommendations OWNER TO postgres;

--
-- Name: training_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.training_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.training_seq OWNER TO postgres;

--
-- Name: training_specializations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.training_specializations (
                                                 training_id bigint NOT NULL,
                                                 specialization character varying(100)
);


ALTER TABLE public.training_specializations OWNER TO postgres;

--
-- Name: trainings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trainings (
                                  id bigint NOT NULL,
                                  description character varying(1000),
                                  duration character varying(50),
                                  highlighted boolean NOT NULL,
                                  name character varying(255) NOT NULL,
                                  soft_deleted boolean NOT NULL,
                                  type character varying(50) NOT NULL,
                                  version bigint,
                                  CONSTRAINT trainings_type_check CHECK (((type)::text = ANY ((ARRAY['UNIVERSITY'::character varying, 'VOCATIONAL'::character varying, 'BOOTCAMP'::character varying, 'CERTIFICATION'::character varying, 'ONLINE_COURSE'::character varying, 'INTERNSHIP'::character varying, 'APPRENTICESHIP'::character varying, 'WORKSHOP'::character varying, 'SEMINAR'::character varying, 'SELF_TAUGHT'::character varying])::text[])))
);


ALTER TABLE public.trainings OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              user_type character varying(31) NOT NULL,
                              id bigint NOT NULL,
                              created_at timestamp(6) without time zone,
                              email character varying(365) NOT NULL,
                              enabled boolean NOT NULL,
                              first_name character varying(50) NOT NULL,
                              last_name character varying(50) NOT NULL,
                              last_seen timestamp(6) without time zone,
                              password character varying(255) NOT NULL,
                              token_expired boolean NOT NULL,
                              updated_at timestamp(6) without time zone,
                              age integer NOT NULL,
                              deleted_at timestamp(6) without time zone,
                              gender smallint,
                              is_deleted boolean NOT NULL,
                              last_login_at timestamp(6) without time zone,
                              phone_number character varying(20),
                              suspended boolean NOT NULL,
                              suspended_until timestamp(6) without time zone,
                              suspension_reason character varying(255),
                              CONSTRAINT users_gender_check CHECK (((gender >= 0) AND (gender <= 1)))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
                                    user_id bigint NOT NULL,
                                    role_id bigint NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_seq OWNER TO postgres;

--
-- Name: admins admins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admins_pkey PRIMARY KEY (id);


--
-- Name: answer_options answer_options_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answer_options
    ADD CONSTRAINT answer_options_pkey PRIMARY KEY (id);


--
-- Name: coach_student_connections coach_student_connections_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coach_student_connections
    ADD CONSTRAINT coach_student_connections_pkey PRIMARY KEY (id);


--
-- Name: coaches coaches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coaches
    ADD CONSTRAINT coaches_pkey PRIMARY KEY (id);


--
-- Name: conversations conversations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT conversations_pkey PRIMARY KEY (id);


--
-- Name: tokens idx_token_value; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tokens
    ADD CONSTRAINT idx_token_value UNIQUE (token_value);


--
-- Name: users idx_users_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT idx_users_email UNIQUE (email);


--
-- Name: job_recommendations job_recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_recommendations
    ADD CONSTRAINT job_recommendations_pkey PRIMARY KEY (id);


--
-- Name: job_training_links job_training_links_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_training_links
    ADD CONSTRAINT job_training_links_pkey PRIMARY KEY (job_id, training_id);


--
-- Name: jobs jobs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jobs
    ADD CONSTRAINT jobs_pkey PRIMARY KEY (id);


--
-- Name: medias medias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medias
    ADD CONSTRAINT medias_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: notification_settings notification_settings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_settings
    ADD CONSTRAINT notification_settings_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: personalized_jobs personalized_jobs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personalized_jobs
    ADD CONSTRAINT personalized_jobs_pkey PRIMARY KEY (id);


--
-- Name: privileges privileges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privileges
    ADD CONSTRAINT privileges_pkey PRIMARY KEY (id);


--
-- Name: questions questions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: roles_privileges roles_privileges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT roles_privileges_pkey PRIMARY KEY (role_id, privilege_id);


--
-- Name: student_job_links student_job_links_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_job_links
    ADD CONSTRAINT student_job_links_pkey PRIMARY KEY (id);


--
-- Name: student_personalized_job_links student_personalized_job_links_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_personalized_job_links
    ADD CONSTRAINT student_personalized_job_links_pkey PRIMARY KEY (id);


--
-- Name: student_training_links student_training_links_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_training_links
    ADD CONSTRAINT student_training_links_pkey PRIMARY KEY (id);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- Name: test_questions test_questions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_questions
    ADD CONSTRAINT test_questions_pkey PRIMARY KEY (test_id, question_id);


--
-- Name: test_result_scores test_result_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_result_scores
    ADD CONSTRAINT test_result_scores_pkey PRIMARY KEY (result_id, type);


--
-- Name: test_results test_results_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_results
    ADD CONSTRAINT test_results_pkey PRIMARY KEY (id);


--
-- Name: tests tests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (id);


--
-- Name: tokens tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tokens
    ADD CONSTRAINT tokens_pkey PRIMARY KEY (id);


--
-- Name: training_recommendations training_recommendations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.training_recommendations
    ADD CONSTRAINT training_recommendations_pkey PRIMARY KEY (id);


--
-- Name: trainings trainings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trainings
    ADD CONSTRAINT trainings_pkey PRIMARY KEY (id);


--
-- Name: test_results uk2voayghfxl8nevqyqe8dkxgrs; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_results
    ADD CONSTRAINT uk2voayghfxl8nevqyqe8dkxgrs UNIQUE (test_id);


--
-- Name: coach_student_connections uk4g7fjxq068m485fnfpyidxqtw; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coach_student_connections
    ADD CONSTRAINT uk4g7fjxq068m485fnfpyidxqtw UNIQUE (coach_id, student_id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: conversations uka6hq6endguawa78mp52abp6hu; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT uka6hq6endguawa78mp52abp6hu UNIQUE (coach_id, student_id);


--
-- Name: privileges ukm2tnonbcaquofx1ccy060ejyc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privileges
    ADD CONSTRAINT ukm2tnonbcaquofx1ccy060ejyc UNIQUE (name);


--
-- Name: notification_settings ukm9ggfvif86mvq5382j88cequn; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_settings
    ADD CONSTRAINT ukm9ggfvif86mvq5382j88cequn UNIQUE (user_id);


--
-- Name: roles ukofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);


--
-- Name: test_results ukrnj6sqhdxkgh27s73uwpov0i2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_results
    ADD CONSTRAINT ukrnj6sqhdxkgh27s73uwpov0i2 UNIQUE (pdf_id);


--
-- Name: users uq_users_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uq_users_email UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: idx_answer_option_question; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_answer_option_question ON public.answer_options USING btree (question_id);


--
-- Name: idx_conversation_last_message; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversation_last_message ON public.conversations USING btree (last_message_at);


--
-- Name: idx_job_category; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_job_category ON public.jobs USING btree (category);


--
-- Name: idx_job_expiry; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_job_expiry ON public.personalized_jobs USING btree (expiration_date);


--
-- Name: idx_media_user_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_media_user_type ON public.medias USING btree (user_id, type);


--
-- Name: idx_messages_conversation; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_conversation ON public.messages USING btree (conversation_id, created_at);


--
-- Name: idx_messages_read; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_read ON public.messages USING btree (read);


--
-- Name: idx_notif_settings_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notif_settings_user ON public.notification_settings USING btree (user_id);


--
-- Name: idx_notification_read; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_read ON public.notifications USING btree (read);


--
-- Name: idx_notification_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notification_user ON public.notifications USING btree (recipient_id);


--
-- Name: idx_personalized_job_category; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_personalized_job_category ON public.personalized_jobs USING btree (category);


--
-- Name: idx_question_riasec_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_question_riasec_type ON public.questions USING btree (category);


--
-- Name: idx_recommendation_job; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_recommendation_job ON public.job_recommendations USING btree (job_id);


--
-- Name: idx_recommendation_result; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_recommendation_result ON public.job_recommendations USING btree (test_result_id);


--
-- Name: idx_result_dominant; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_result_dominant ON public.test_results USING btree (dominant_type);


--
-- Name: idx_result_test; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_result_test ON public.test_results USING btree (test_id);


--
-- Name: idx_student_job_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_student_job_type ON public.student_job_links USING btree (student_id, job_id, type);


--
-- Name: idx_student_personalized_job_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_student_personalized_job_type ON public.student_personalized_job_links USING btree (student_id, personalized_job_id, type);


--
-- Name: idx_student_training_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_student_training_type ON public.student_training_links USING btree (student_id, training_id, type);


--
-- Name: idx_tests_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tests_status ON public.tests USING btree (status);


--
-- Name: idx_tests_student; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_tests_student ON public.tests USING btree (student_id);


--
-- Name: idx_token_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_token_user_id ON public.tokens USING btree (user_id);


--
-- Name: idx_training_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_training_type ON public.trainings USING btree (type);


--
-- Name: idx_users_enabled; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_enabled ON public.users USING btree (enabled);


--
-- Name: idx_users_last_seen; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_last_seen ON public.users USING btree (last_seen);


--
-- Name: idx_users_suspended; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_users_suspended ON public.users USING btree (suspended);


--
-- Name: tokens fk2dylsfo39lgjyqml2tbe0b0ss; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tokens
    ADD CONSTRAINT fk2dylsfo39lgjyqml2tbe0b0ss FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: job_training_links fk2kfhxq6ptldklf7q3v7rgddj6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_training_links
    ADD CONSTRAINT fk2kfhxq6ptldklf7q3v7rgddj6 FOREIGN KEY (job_id) REFERENCES public.jobs(id);


--
-- Name: training_specializations fk2mvpwbd4u3560kvdfb5lnd7tv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.training_specializations
    ADD CONSTRAINT fk2mvpwbd4u3560kvdfb5lnd7tv FOREIGN KEY (training_id) REFERENCES public.trainings(id);


--
-- Name: users_roles fk2o0jvgh89lemvvo17cbqvdxaa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: test_result_scores fk3dvpiqgs2l1btkmr0ldn76b0w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_result_scores
    ADD CONSTRAINT fk3dvpiqgs2l1btkmr0ldn76b0w FOREIGN KEY (result_id) REFERENCES public.test_results(id);


--
-- Name: messages fk4ui4nnwntodh6wjvck53dbk9m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk4ui4nnwntodh6wjvck53dbk9m FOREIGN KEY (sender_id) REFERENCES public.users(id);


--
-- Name: test_results fk5ap9cwdk7tao0go85yvrumrpt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_results
    ADD CONSTRAINT fk5ap9cwdk7tao0go85yvrumrpt FOREIGN KEY (pdf_id) REFERENCES public.medias(id);


--
-- Name: roles_privileges fk5duhoc7rwt8h06avv41o41cfy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT fk5duhoc7rwt8h06avv41o41cfy FOREIGN KEY (privilege_id) REFERENCES public.privileges(id);


--
-- Name: tests fk5jo5ip3k2nd4n33tms38306o1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tests
    ADD CONSTRAINT fk5jo5ip3k2nd4n33tms38306o1 FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: student_job_links fk5vxgw4h1evdpxemkd9mu15ohp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_job_links
    ADD CONSTRAINT fk5vxgw4h1evdpxemkd9mu15ohp FOREIGN KEY (job_id) REFERENCES public.jobs(id);


--
-- Name: roles_privileges fk629oqwrudgp5u7tewl07ayugj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT fk629oqwrudgp5u7tewl07ayugj FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: students fk7xqmtv7r2eb5axni3jm0a80su; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT fk7xqmtv7r2eb5axni3jm0a80su FOREIGN KEY (id) REFERENCES public.users(id);


--
-- Name: conversations fk877ed8s0yq49thogxcad5y0au; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fk877ed8s0yq49thogxcad5y0au FOREIGN KEY (coach_id) REFERENCES public.coaches(id);


--
-- Name: job_advantages fk88w2u2fsys5qfmwbu09ro13qa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_advantages
    ADD CONSTRAINT fk88w2u2fsys5qfmwbu09ro13qa FOREIGN KEY (job_id) REFERENCES public.personalized_jobs(id);


--
-- Name: training_recommendations fk9lfbqe54vcaymjj76his6cceg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.training_recommendations
    ADD CONSTRAINT fk9lfbqe54vcaymjj76his6cceg FOREIGN KEY (test_result_id) REFERENCES public.test_results(id);


--
-- Name: job_recommendations fka54oymmerbyitwnxh85psrn1j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_recommendations
    ADD CONSTRAINT fka54oymmerbyitwnxh85psrn1j FOREIGN KEY (test_result_id) REFERENCES public.test_results(id);


--
-- Name: admins fkanhsicqm3lc8ya77tr7r0je18; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT fkanhsicqm3lc8ya77tr7r0je18 FOREIGN KEY (id) REFERENCES public.users(id);


--
-- Name: coaches fkbyei1g9vs5d057vur8psubw3x; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coaches
    ADD CONSTRAINT fkbyei1g9vs5d057vur8psubw3x FOREIGN KEY (id) REFERENCES public.users(id);


--
-- Name: test_results fkeb5e15t9e5hn11gbkuub0xeln; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_results
    ADD CONSTRAINT fkeb5e15t9e5hn11gbkuub0xeln FOREIGN KEY (test_id) REFERENCES public.tests(id);


--
-- Name: coach_student_connections fkf0atgg5908wonrg1tnhvpl1ys; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coach_student_connections
    ADD CONSTRAINT fkf0atgg5908wonrg1tnhvpl1ys FOREIGN KEY (coach_id) REFERENCES public.coaches(id);


--
-- Name: answer_options fkfapodm8kfiu9a9a4o2r43rcgp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.answer_options
    ADD CONSTRAINT fkfapodm8kfiu9a9a4o2r43rcgp FOREIGN KEY (question_id) REFERENCES public.questions(id);


--
-- Name: student_personalized_job_links fkgxrxd97fdqe4gt2be7ubu412p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_personalized_job_links
    ADD CONSTRAINT fkgxrxd97fdqe4gt2be7ubu412p FOREIGN KEY (personalized_job_id) REFERENCES public.personalized_jobs(id);


--
-- Name: job_required_skills fkic59e3odvcv4wol8jctku72aw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_required_skills
    ADD CONSTRAINT fkic59e3odvcv4wol8jctku72aw FOREIGN KEY (job_id) REFERENCES public.personalized_jobs(id);


--
-- Name: users_roles fkj6m8fwv7oqv74fcehir1a9ffy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: training_recommendations fkjrhfo6ej53fa1yc4tck1ycj8c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.training_recommendations
    ADD CONSTRAINT fkjrhfo6ej53fa1yc4tck1ycj8c FOREIGN KEY (training_id) REFERENCES public.trainings(id);


--
-- Name: test_questions fkk171b2q5ikck3f9yk4n9lbyvw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_questions
    ADD CONSTRAINT fkk171b2q5ikck3f9yk4n9lbyvw FOREIGN KEY (question_id) REFERENCES public.questions(id);


--
-- Name: medias fkl96wo4x1syvvt7mxih064je28; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medias
    ADD CONSTRAINT fkl96wo4x1syvvt7mxih064je28 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: student_training_links fklr3qoras1b3722pgx9hirkk66; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_training_links
    ADD CONSTRAINT fklr3qoras1b3722pgx9hirkk66 FOREIGN KEY (training_id) REFERENCES public.trainings(id);


--
-- Name: notification_settings fkmh6alfw96lc851ea0snhijfk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_settings
    ADD CONSTRAINT fkmh6alfw96lc851ea0snhijfk FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: job_training_links fkn0kmpnaeerc5mtcoycrkpgntg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_training_links
    ADD CONSTRAINT fkn0kmpnaeerc5mtcoycrkpgntg FOREIGN KEY (training_id) REFERENCES public.trainings(id);


--
-- Name: student_training_links fkn3gmhpwfbrrb3r5ar842xv2sy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_training_links
    ADD CONSTRAINT fkn3gmhpwfbrrb3r5ar842xv2sy FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: student_job_links fkngtxi4ey5myt895bierjc7m0c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_job_links
    ADD CONSTRAINT fkngtxi4ey5myt895bierjc7m0c FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: conversations fko6u160qfx7o9whns9uoe9ic6w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT fko6u160qfx7o9whns9uoe9ic6w FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: coach_student_connections fkp5yrs9p401e8pyltvlmh5qen; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coach_student_connections
    ADD CONSTRAINT fkp5yrs9p401e8pyltvlmh5qen FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: test_questions fkq1jpgjcbjbulxvhdkctcxhg12; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.test_questions
    ADD CONSTRAINT fkq1jpgjcbjbulxvhdkctcxhg12 FOREIGN KEY (test_id) REFERENCES public.tests(id);


--
-- Name: personalized_jobs fkq8rwm4w2ok3c7qqask9h4e7r2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personalized_jobs
    ADD CONSTRAINT fkq8rwm4w2ok3c7qqask9h4e7r2 FOREIGN KEY (job_recommendation_id) REFERENCES public.job_recommendations(id);


--
-- Name: notifications fkqqnsjxlwleyjbxlmm213jaj3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fkqqnsjxlwleyjbxlmm213jaj3f FOREIGN KEY (recipient_id) REFERENCES public.users(id);


--
-- Name: admins fkqu7et826jq36a9ddcfa0eswbk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT fkqu7et826jq36a9ddcfa0eswbk FOREIGN KEY (created_by) REFERENCES public.admins(id);


--
-- Name: student_personalized_job_links fkrl74qrjff35eefsdbw86fpeuc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_personalized_job_links
    ADD CONSTRAINT fkrl74qrjff35eefsdbw86fpeuc FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: messages fkt492th6wsovh1nush5yl5jj8e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fkt492th6wsovh1nush5yl5jj8e FOREIGN KEY (conversation_id) REFERENCES public.conversations(id);


--
-- Name: job_recommendations fktfq48elu7ctvpwn38edj74cdy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_recommendations
    ADD CONSTRAINT fktfq48elu7ctvpwn38edj74cdy FOREIGN KEY (job_id) REFERENCES public.jobs(id);


--
-- Name: job_tags fky5ln7dquha6f0a63bnux2qlk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_tags
    ADD CONSTRAINT fky5ln7dquha6f0a63bnux2qlk FOREIGN KEY (job_id) REFERENCES public.jobs(id);


--
-- PostgreSQL database dump complete
--

