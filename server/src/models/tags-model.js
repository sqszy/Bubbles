const { Schema, model } = require("mongoose");

const TagSchema = new Schema({
    id: { type: Number, required: true },
    name: { type: String, required: true },
    color: { type: String, required: true },
});

module.exports = model("Tag", TagSchema);
