module.exports = class ReviewDto {
    id;
    text;
    rating;
    creatorId;
    reviewId;

    constructor(model) {
        this.id = model._id;
        this.text = model.text;
        this.rating = model.rating;
        this.creatorId = model.creatorId;
        this.reviewId = model.reviewId;
    }
};
