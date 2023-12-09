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
const aws_access_key = process.env.AWS_ACCESS_KEY;
const aws_secret_access_key = process.env.AWS_SECRET_ACCESS_KEY;
const aws_region = process.env.AWS_REGION;
const aws_bucket = process.env.AWS_BUCKET;
const aws_endpoint = process.env.AWS_ENDPOINT;

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
  aws_access_key,
  aws_secret_access_key,
  aws_region,
  aws_bucket,
  aws_endpoint,
};
