const ReviewDto = require("./review-dto");

module.exports = class PlaceDto {
    id;
    title;
    about;
    latitude;
    longitude;
    images;
    creatorId;
    reviews;
    tags;

    constructor(model) {
        this.id = model._id;
        this.title = model.title;
        this.about = model.about;
        this.latitude = model.latitude;
        this.longitude = model.longitude;
        this.images = model.images;
        this.creatorId = model.creatorId;
        this.reviews = model.reviews.map((review) => new ReviewDto(review));
        this.tags = model.tags;
    }
};
