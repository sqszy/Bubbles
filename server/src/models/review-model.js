const { Schema, model } = require("mongoose");

const ReviewSchema = new Schema({
    text: { type: String, required: true },
    rating: { type: Number, required: true },
    creatorId: { type: Schema.Types.ObjectId, ref: "User", required: true },
    placeId: { type: Schema.Types.ObjectId, ref: "Place", required: true },
});

module.exports = model("Review", ReviewSchema);
