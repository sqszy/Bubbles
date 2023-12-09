const userModel = require("../models/user-model");
const { S3Client, PutObjectCommand } = require("@aws-sdk/client-s3");
const config = require("../config/config");
const ApiError = require("../exceptions/api-error");
const multer = require("multer");

class S3 {
  constructor() {
    this.storage = multer.memoryStorage();
    this.fileFilter = (req, file, cb) => {
      if (file.mimetype.split("/")[0] === "image") {
        cb(null, true);
      } else {
        cb(new multer.MulterError("LIMIT_UNEXPECTED_FILE"), false);
      }
    };

    this.upload = multer({
      storage: this.storage,
      fileFilter: this.fileFilter,
      limits: { fileSize: 10000000, files: 2 },
    });

    this.uploadToS3 = async (command) => {
      const s3client = new S3Client({
        credentials: {
          accessKeyId: config.aws_access_key,
          secretAccessKey: config.aws_secret_access_key,
        },

        endpoint: "https://" + config.aws_endpoint,
        region: config.aws_region,
      });
      await s3client.send(command);
    };
  }
}

class PhotoService {
  constructor() {
    this.s3 = new S3();
  }
  async s3upload(params) {
    params.Bucket = config.aws_bucket;
    params.ACL = "public-read";
    const command = new PutObjectCommand(params);

    await this.s3.uploadToS3(command);
  }
}

module.exports = new PhotoService();
