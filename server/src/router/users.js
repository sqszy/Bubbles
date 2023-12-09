const Router = require("express").Router;
const userController = require("../controller/user-controller");

const authMiddleware = require("../middleware/auth-middleware");

const usersRouter = new Router();

usersRouter.post("/upload-image");
usersRouter.delete("/delete-image");

module.exports = usersRouter;
