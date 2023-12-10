const placeService = require("../service/place-service");
const ApiError = require("../exceptions/api-error");
const userModel = require("../models/user-model");
const tagService = require("../service/tag-service");
const { validationResult } = require("express-validator");

class PlaceController {
    async createPlace(req, res, next) {
        try {
            const userId = req.user.id;
            const user = await userModel.findById(userId);
            if (!user) {
                throw ApiError.UnauthorizedError();
            }
            const { title, about, latitude, longitude } = req.body;

            const newPlace = await placeService.createPlace(
                title,
                about,
                latitude,
                longitude,
                userId
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
            const { title, about, latitude, longitude } = req.body;

            const updates = {};
            if (title) updates.title = title;
            if (about) updates.about = about;
            if (latitude) updates.latitude = latitude;
            if (longitude) updates.longitude = longitude;

            const result = await placeService.patchPlace(
                placeId,
                userId,
                updates
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

    async addTag(req, res, next) {
        try {
        } catch (e) {
            next(e);
        }
    }

    async patchTag(req, res, next) {
        try {
        } catch (e) {
            next(e);
        }
    }

    async deleteTag(req, res, next) {
        try {
        } catch (e) {
            next(e);
        }
    }
}

module.exports = new PlaceController();
