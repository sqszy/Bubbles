const Tag = require("../models/tags-model");

const initialTags = [
    { name: "еда", color: "FFD542" },
    { name: "веселье", color: "FF42D5" },
    { name: "достопримечательность", color: "1f3438" },
    { name: "с друзьями", color: "FF4242" },
    { name: "отдых", color: "42A4FF" },
];

class TagService {
    async initializeTags() {
        const existingTags = await Tag.find();

        if (existingTags.length === 0) {
            await Tag.create(initialTags);
        }
    }

    async getTagIdsByNames(tagNames) {
        const tagIds = await Tag.find({ name: { $in: tagNames } }).distinct(
            "_id"
        );
        return tagIds;
    }

    async addTagsToPlace(placeId, tagIds) {
        await placeModel.findByIdAndUpdate(placeId, {
            $push: { tags: { $each: tagIds } },
        });
    }
}
module.exports = new TagService();
