-- NOTE: This migration intentionally lives in user-service/db/migration so that Flyway
-- applies all schema changes (users, devices, alerts) in a single, ordered pass at startup.
-- alert-service does NOT have Flyway enabled; it relies on this table being created here.
-- Relocating this file would invalidate Flyway's checksum on existing databases.
CREATE TABLE `alert`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    `sent` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;