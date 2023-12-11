module.exports = class Dto {
    email;
    id;
    isActivated;
    username;
    registeredAt;
    isAdmin;
    image;
    favoritePlaces;

    constructor(model) {
        this.email = model.email;
        this.id = model._id;
        this.isActivated = model.isActivated;
        this.username = model.username;
        this.registeredAt = model.registeredAt;
        this.isAdmin = model.isAdmin;
        this.image = model.image;
        this.favoritePlaces = model.favoritePlaces;
    }
};
