// logger.js

const log4js = require("log4js");
const config = require("../config/config");

const logger = log4js.getLogger();
logger.level = config.LOG_LEVEL;

module.exports = logger;
