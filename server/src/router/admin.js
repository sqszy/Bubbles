const Router = require("express").Router;
const userController = require("../controller/user-controller");

const authMiddleware = require("../middleware/auth-middleware");

const adminRouter = new Router();

adminRouter.get("/{id}"); // For isAdmin(get user id)
adminRouter.patch("/{id}"); // For isAdmin(patch user id)
adminRouter.delete("/{id}"); // For isAdmin(delete user)

module.exports = adminRouter;
