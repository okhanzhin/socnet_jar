SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE acc_pictures RESTART IDENTITY;
TRUNCATE TABLE comm_pictures RESTART IDENTITY;
TRUNCATE TABLE accounts RESTART IDENTITY;
TRUNCATE TABLE communities RESTART IDENTITY;
TRUNCATE TABLE chat_rooms RESTART IDENTITY;
TRUNCATE TABLE relations RESTART IDENTITY;
TRUNCATE TABLE members RESTART IDENTITY;
TRUNCATE TABLE phones RESTART IDENTITY;
TRUNCATE TABLE requests RESTART IDENTITY;
TRUNCATE TABLE messages RESTART IDENTITY;
TRUNCATE TABLE posts RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;
