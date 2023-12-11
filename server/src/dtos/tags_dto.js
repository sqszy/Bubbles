module.exports = class Dto {
    id;
    name;
    color;

    constructor(model) {
        this.id = model._id;
        this.name = model.name;
        this.color = model.color;
    }
};
