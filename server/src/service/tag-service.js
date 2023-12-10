const Tag = require("../models/tags-model");

const initialTags = [
    { id: 1, name: "еда", color: "FFD542" },
    { id: 2, name: "веселье", color: "FF42D5" },
    { id: 3, name: "достопримечательность", color: "1f3438" },
    { id: 4, name: "с друзьями", color: "FF4242" },
    { id: 5, name: "отдых", color: "42A4FF" },
];

class TagService {
    async initializeTags() {
        const existingTags = await Tag.find();

        if (existingTags.length > 0) {
            console.log("Теги уже инициализированы.");
            return;
        }

        await Tag.create(initialTags);
        console.log("Теги успешно инициализированы.");
    }

    async addTagsToPlace(placeId, tagIds) {
        await Place.findByIdAndUpdate(placeId, {
            $addToSet: { tags: { $each: tagIds } },
        });
        console.log("Теги успешно добавлены к месту.");
    }
}

module.exports = new TagService();
