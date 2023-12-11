const {
    S3Client,
    PutObjectCommand,
    DeleteObjectCommand,
} = require("@aws-sdk/client-s3");
const config = require("../config/config");
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
            limits: { fileSize: 10000000, files: 1 },
        });

        this.uploadPlaceImages = multer({
            storage: this.storage,
            fileFilter: this.fileFilter,
            limits: { fileSize: 10000000, files: 4 },
        });

        this.uploadToS3 = async (command) => {
            const s3client = new S3Client({
                credentials: {
                    accessKeyId: config.AWS_ACCESS_KEY,
                    secretAccessKey: config.AWS_SECRET_ACCESS_KEY,
                },

                endpoint: "https://" + config.AWS_ENDPOINT,
                region: config.AWS_REGION,
            });
            await s3client.send(command);
        };

        this.deleteFromS3 = async (params) => {
            const s3client = new S3Client({
                credentials: {
                    accessKeyId: config.AWS_ACCESS_KEY,
                    secretAccessKey: config.AWS_SECRET_ACCESS_KEY,
                },
                endpoint: "https://" + config.AWS_ENDPOINT,
                region: config.AWS_REGION,
            });

            await s3client.send(new DeleteObjectCommand(params));
        };
    }
}

class PhotoService {
    constructor() {
        this.s3 = new S3();
    }
    async s3upload(params) {
        params.Bucket = config.AWS_BUCKET;
        params.ACL = "public-read";
        const command = new PutObjectCommand(params);

        await this.s3.uploadToS3(command);
    }

    async deleteImageFromS3(filename) {
        const params = {
            Bucket: config.AWS_BUCKET,
            Key: filename,
        };
        await this.s3.deleteFromS3(params);
    }
}

module.exports = new PhotoService();
