const config = require("../config/config");
const placeModel = require("../models/place-model");
const placeService = require("../service/place-service");
const ApiError = require("../exceptions/api-error");
const userModel = require("../models/user-model");
const photoService = require("../service/photo-service");
const { validationResult } = require("express-validator");
const logger = require("../config/logger");

class PlaceController {
    async createPlace(req, res, next) {
        try {
            const userId = req.user.id;
            const user = await userModel.findById(userId);
            if (!user) {
                throw ApiError.UnauthorizedError();
            }

            const { title, about, latitude, longitude, tagNames } = req.body;

            const newPlace = await placeService.createPlace(
                title,
                about,
                latitude,
                longitude,
                userId,
                tagNames
            );

            res.json(newPlace);
        } catch (e) {
            next(e);
        }
    }

    async createReview(req, res, next) {
        try {
            const errors = validationResult(req);
            if (!errors.isEmpty()) {
                return next(
                    ApiError.BadRequest("Ошибка при валидации", errors.array())
                );
            }
            const userId = req.user.id;
            const placeId = req.params.placeId;
            const { text, rating } = req.body;

            const newReview = await placeService.createReview(
                text,
                rating,
                userId,
                placeId
            );

            res.json(newReview);
        } catch (e) {
            next(e);
        }
    }

    async deletePlace(req, res, next) {
        try {
            const userId = req.user.id;
            const placeId = req.params.placeId;

            const result = await placeService.deletePlace(placeId, userId);

            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async deleteReview(req, res, next) {
        try {
            const userId = req.user.id;
            const reviewId = req.params.reviewId;

            const result = await placeService.deleteReview(reviewId, userId);

            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async patchPlace(req, res, next) {
        try {
            const userId = req.user.id;
            const placeId = req.params.placeId;
            const { title, about, latitude, longitude, tagNames } = req.body;

            const updates = {};
            if (title) updates.title = title;
            if (about) updates.about = about;
            if (latitude) updates.latitude = latitude;
            if (longitude) updates.longitude = longitude;

            const result = await placeService.patchPlace(
                placeId,
                userId,
                updates,
                tagNames
            );

            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async patchReview(req, res, next) {
        try {
            const userId = req.user.id;
            const reviewId = req.params.reviewId;
            const { text, rating } = req.body;

            const updates = {};
            if (text) updates.text = text;
            if (rating) updates.rating = rating;

            const result = await placeService.patchReview(
                reviewId,
                userId,
                updates
            );

            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async getAllPlaces(req, res, next) {
        try {
            const result = await placeService.getAllPlaces();
            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async getPlaceById(req, res, next) {
        try {
            const placeId = req.params.placeId;
            const result = await placeService.getPlaceById(placeId);
            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async getReviewsForPlace(req, res, next) {
        try {
            const placeId = req.params.placeId;
            const result = await placeService.getReviewsForPlace(placeId);
            res.json(result);
        } catch (e) {
            next(e);
        }
    }

    async uploadPlaceImages(req, res, next) {
        try {
            const placeId = req.params.placeId;
            const files = req.files;

            if (!files || files.length === 0) {
                return next(ApiError.BadRequest("No images uploaded"));
            }

            const existingPlace = await placeModel.findById(placeId);

            if (!existingPlace) {
                return next(ApiError.NotFoundError("Place not found"));
            }

            const totalImages = existingPlace.images.length + files.length;

            if (totalImages > 4) {
                return next(
                    ApiError.BadRequest("Exceeded maximum image limit")
                );
            }

            const imageUrls = [];

            for (const file of files) {
                const params = {
                    Key: file.originalname,
                    Body: file.buffer,
                    ContentType: file.mimetype,
                };

                await photoService.s3upload(params);

                const imageUrl = `https://${config.AWS_BUCKET}.${config.AWS_ENDPOINT}/${file.originalname}`;
                imageUrls.push(imageUrl);
            }

            await placeModel.findByIdAndUpdate(placeId, {
                $push: { images: { $each: imageUrls } },
            });

            return res.json({ success: true, images: imageUrls });
        } catch (e) {
            next(e);
        }
    }

    async patchPlaceImages(req, res, next) {
        try {
            const placeId = req.params.placeId;
            const files = req.files;

            if (!files || files.length === 0) {
                return next(ApiError.BadRequest("No images uploaded"));
            }

            const imageUrls = [];

            for (const file of files) {
                const params = {
                    Key: file.originalname,
                    Body: file.buffer,
                    ContentType: file.mimetype,
                };

                await photoService.s3upload(params);

                const imageUrl = `https://${config.AWS_BUCKET}.${config.AWS_ENDPOINT}/${file.originalname}`;
                imageUrls.push(imageUrl);
            }

            await placeModel.findByIdAndUpdate(placeId, {
                $set: { images: imageUrls },
            });

            return res.json({ success: true, images: imageUrls });
        } catch (e) {
            next(e);
        }
    }

    async deleteAllPlaceImages(req, res, next) {
        try {
            const placeId = req.params.placeId;

            const existingPlace = await placeModel.findById(placeId);

            if (!existingPlace) {
                throw ApiError.NotFoundError("Place not found");
            }

            for (const image of existingPlace.images) {
                const filename = image.split("/").pop();
                await photoService.deleteImageFromS3(filename);
            }

            await placeModel.findByIdAndUpdate(placeId, { images: [] });

            res.json({
                success: true,
                message: "All images deleted successfully",
            });
        } catch (error) {
            next(error);
        }
    }

    async search(req, res, next) {
        try {
            const query = req.query.query;
            const tags = req.query.tags;
            logger.debug(req);

            const result = await placeService.search(query, tags);

            return res.json(result);
        } catch (error) {
            next(error);
        }
    }
}

module.exports = new PlaceController();
