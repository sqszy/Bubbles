require("dotenv").config();
const PORT = process.env.PORT || 5000;
const DB_URL = process.env.DB_URL;
const JWT_ACCESS_SECRET = process.env.JWT_ACCESS_SECRET;
const JWT_REFRESH_SECRET = process.env.JWT_REFRESH_SECRET;
const SMTP_USER = process.env.SMTP_USER;
const SMTP_PASSWORD = process.env.SMTP_PASSWORD;
const SMTP_HOST = process.env.SMTP_HOST;
const SMTP_PORT = process.env.SMTP_PORT;
const API_URL = process.env.API_URL;
const CLIENT_URL = process.env.CLIENT_URL;
const S3_ID = process.env.S3_ID;
const S3_SECRET = process.env.S3_SECRET;

module.exports = {
    PORT,
    DB_URL,
    JWT_ACCESS_SECRET,
    JWT_REFRESH_SECRET,
    SMTP_USER,
    SMTP_PASSWORD,
    SMTP_HOST,
    SMTP_PORT,
    API_URL,
    CLIENT_URL,
    S3_ID,
    S3_SECRET,
};
