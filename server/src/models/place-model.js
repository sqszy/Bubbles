const { Schema, model } = require("mongoose");

const PlaceSchema = new Schema({
    title: { type: String, required: true },
    about: { type: String, required: true },
    latitude: { type: Number, required: true },
    longitude: { type: Number, required: true },
    creatorId: { type: Schema.Types.ObjectId, ref: "User", required: true },
    images: [{ type: String }],
    reviews: [{ type: Schema.Types.ObjectId, ref: "Review" }],
    tags: [{ type: Schema.Types.ObjectId, ref: "Tag" }],
});

module.exports = model("Place", PlaceSchema);
