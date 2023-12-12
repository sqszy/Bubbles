const placeModel = require("../models/place-model");
const reviewModel = require("../models/review-model");
const PlaceDto = require("../dtos/place-dto");
const ReviewDto = require("../dtos/review-dto");
const ApiError = require("../exceptions/api-error");
const tagService = require("../service/tag-service");
const mongoose = require("mongoose");
const userModel = require("../models/user-model");

class PlaceService {
    async createPlace(title, about, latitude, longitude, creatorId, tagNames) {
        const tagIds = await tagService.getTagIdsByNames(tagNames);
        const place = await placeModel.create({
            title,
            about,
            latitude,
            longitude,
            images: [],
            reviews: [],
            creatorId,
            tags: tagIds,
        });

        const placeDto = new PlaceDto(place);

        return {
            place: placeDto,
        };
    }

    async createReview(text, rating, userId, placeId) {
        const review = await reviewModel.create({
            text,
            rating,
            creatorId: userId,
            placeId,
        });

        const place = await placeModel.findByIdAndUpdate(
            placeId,
            { $push: { reviews: review._id } },
            { new: true }
        );

        const reviewDto = new ReviewDto(review);

        return {
            review: reviewDto,
        };
    }

    async deletePlace(placeId, userId) {
        const place = await placeModel.findOne({
            _id: placeId,
            creatorId: userId,
        });

        if (!place) {
            throw ApiError.NotFoundError("Место не найдено");
        }

        await userModel.updateMany(
            { favoritePlaces: placeId },
            { $pull: { favoritePlaces: placeId } }
        );

        await reviewModel.deleteMany({ placeId });

        await placeModel.findByIdAndDelete(placeId);

        return {
            message: "Место и связанные с ним отзывы успешно удалены",
        };
    }

    async deleteReview(reviewId, userId) {
        const review = await reviewModel.findOne({
            _id: reviewId,
            creatorId: userId,
        });

        if (!review) {
            throw ApiError.NotFoundError(
                "Отзыв не найден или у вас нет прав для удаления"
            );
        }

        await placeModel.updateOne(
            { _id: review.placeId },
            { $pull: { reviews: reviewId } }
        );

        await reviewModel.findByIdAndDelete(reviewId);

        return {
            message: "Отзыв успешно удален",
        };
    }

    async patchPlace(placeId, userId, updates, tagNames) {
        if (tagNames) {
            const tagIds = await tagService.getTagIdsByNames(tagNames);
            updates.tags = tagIds;
        }

        const place = await placeModel.findOne({
            _id: placeId,
            creatorId: userId,
        });

        if (!place) {
            throw ApiError.NotFoundError(
                "Место не найдено или у вас нет прав для изменения"
            );
        }

        await placeModel.updateOne({ _id: placeId }, { $set: updates });

        const updatedPlace = await placeModel.findById(placeId);
        const placeDto = new PlaceDto(updatedPlace);

        return {
            place: placeDto,
        };
    }

    async patchReview(reviewId, userId, updates) {
        const review = await reviewModel.findOne({
            _id: reviewId,
            creatorId: userId,
        });

        if (!review) {
            throw ApiError.NotFoundError(
                "Отзыв не найден или у вас нет прав для изменения"
            );
        }

        await reviewModel.updateOne({ _id: reviewId }, { $set: updates });

        const updatedReview = await reviewModel.findById(reviewId);
        const reviewDto = new ReviewDto(updatedReview);

        return {
            review: reviewDto,
        };
    }

    async getAllPlaces() {
        const places = await placeModel.find({});
        const placeDtos = places.map((place) => new PlaceDto(place));
      return {
            places: placeDtos,
        };
    }

    async getPlaceById(placeId) {
        let place;

        if (mongoose.Types.ObjectId.isValid(placeId)) {
            place = await placeModel.findById(placeId);
        } else {
            place = await placeModel.findOne({
                name: { $regex: new RegExp(placeId, "i") },
            });
        }

        if (!place) {
            throw new ApiError.NotFoundError("Место не найдено");
        }

        const placeDto = new PlaceDto(place);
        return {
            place: placeDto,
        };
    }

    async getReviewsForPlace(placeId) {
        const place = await placeModel.findById(placeId).populate("reviews");
        if (!place) {
            throw ApiError.NotFoundError("Место не найдено");
        }

        const reviews = place.reviews.map((review) => new ReviewDto(review));
        return {
            reviews,
        };
    }

    async search(query, tags) {
        try {
            let place;

            if (query && tags) {
                place = await placeModel.find({
                    title: { $regex: query, $options: "i" },
                    tags: tags,
                });
            } else if (query) {
                place = await placeModel.find({
                    title: { $regex: query, $options: "i" },
                });
            } else if (tags) {
                place = await placeModel.find({ tags: tags });
            } else {
                throw new Error(
                    "Not a valid request for search. Need ?query or ?tags."
                );
            }

            return place;
        } catch (error) {
            console.error(error);
        }
    }
}

module.exports = new PlaceService();