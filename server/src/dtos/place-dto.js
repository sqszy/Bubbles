module.exports = class PlaceDto {
    _id;
    title;
    about;
    latitude;
    longitude;
    images;
    creatorId;
    reviews;
    tags;

    constructor(model) {
        this._id = model._id;
        this.title = model.title;
        this.about = model.about;
        this.latitude = model.latitude;
        this.longitude = model.longitude;
        this.images = model.images;
        this.creatorId = model.creatorId;
        this.reviews = model.reviews.map((review) => ({
            id: review._id,
            rating: review.rating,
            text: review.text,
        }));
        this.tags = model.tags;
    }
};
