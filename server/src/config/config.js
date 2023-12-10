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
const AWS_ACCESS_KEY = process.env.AWS_ACCESS_KEY;
const AWS_SECRET_ACCESS_KEY = process.env.AWS_SECRET_ACCESS_KEY;
const AWS_ENDPOINT = process.env.AWS_ENDPOINT;
const AWS_REGION = process.env.AWS_REGION;
const AWS_BUCKET = process.env.AWS_BUCKET;

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
    AWS_ACCESS_KEY,
    AWS_SECRET_ACCESS_KEY,
    AWS_ENDPOINT,
    AWS_REGION,
    AWS_BUCKET,
};
