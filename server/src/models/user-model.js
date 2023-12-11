const { Schema, model } = require("mongoose");

const UserSchema = new Schema({
    email: { type: String, unique: true, required: true },
    username: { type: String, required: true },
    password: { type: String, required: true },
    registeredAt: { type: Date },
    isAdmin: { type: Boolean, default: false },
    isActivated: { type: Boolean, default: false },
    image: { type: String },
    activationLink: { type: String },
    resetToken: { type: String },
    favoritePlaces: [{ type: Schema.Types.ObjectId, ref: "Place" }],
});

module.exports = model("User", UserSchema);
