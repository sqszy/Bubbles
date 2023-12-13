module.exports = class ReviewDto {
    _id;
    text;
    rating;
    creatorId;
    placeId;

    constructor(model) {
        this._id = model._id;
        this.text = model.text;
        this.rating = model.rating;
        this.creatorId = model.creatorId;
        this.placeId = model.placeId;
    }
};
