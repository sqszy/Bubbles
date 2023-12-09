module.exports = class Dto {
    email;
    id;
    isActivated;
    username;
    registeredAt;
    isAdmin;

    constructor(model) {
        this.email = model.email;
        this.id = model._id;
        this.isActivated = model.isActivated;
        this.username = model.username;
        this.registeredAt = model.registeredAt;
        this.isAdmin = model.isAdmin;
    }
};
