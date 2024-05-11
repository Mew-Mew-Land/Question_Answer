create table answers
(
    ID         int           not null comment '唯一标识'
        primary key,
    questionID int           not null comment '标识所属问题',
    answerer   int           not null comment '答主ID',
    content    text          null comment '回答内容',
    commentNum int default 0 null comment '评论',
    updateTime datetime      not null comment '上次更改时间'
);

create index answers__question_index
    on answers (questionID);

create table comments
(
    ID          int      not null
        primary key,
    answerID    int      null comment '所属回答的ID',
    userID      int      null comment '评论的用户ID',
    comment     text     null comment '评论内容',
    postingTime datetime not null
)
    comment '用户的评论表';

create index comments__answer_index
    on comments (answerID);

create index comments_userID_index
    on comments (userID);

create table question
(
    id             int auto_increment comment '问题唯一标识'
        primary key,
    question       varchar(200)               not null comment '问题名字',
    classification varchar(32)                not null comment '所属分区',
    answerNum      int        default 0       not null comment '回答个数',
    isSolved       tinyint(1) default 0       not null,
    UserID         int                        not null comment '发出问题者',
    ViewNum        int        default 0       not null,
    UpdateTime     date       default (now()) not null,
    constraint question_pk
        unique (question)
);

create index ViewNumSort
    on question (ViewNum);

create index question_UserID_index
    on question (UserID);

create table u_recommendation
(
    User_ID       varchar(32)  not null
        primary key,
    Similar_Users varchar(512) null comment '相似用户列表,以$分隔',
    Similarity    varchar(512) null comment '用户之间的相似度'
)
    comment '用户相似度表';

create table user_behavior
(
    User_ID      int          not null comment '用户ID'
        primary key,
    Question_ARR varchar(512) null comment '浏览过的问题ID队列,以$分隔',
    isAns_ARR    varchar(512) null comment '用户是否回答的队列,以$分割',
    isCom_ARR    varchar(512) null comment '用户是否评论的队列,以$分隔'
)
    comment '用户行为';

create table users
(
    id          int auto_increment comment '唯一标识'
        primary key,
    username    varchar(20) not null comment '用户名',
    password    varchar(16) not null comment '密码',
    AccountName varchar(20) null comment '马甲',
    avatar      int         null comment '头像位置根据int值在相应的图片数据库中',
    constraint Users_pk_2
        unique (username),
    constraint Users_pk_3
        unique (AccountName)
);


