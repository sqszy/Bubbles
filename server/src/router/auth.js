const Router = require("express").Router;
const userController = require("../controller/user-controller");
const { body } = require("express-validator");
const authMiddleware = require("../middleware/auth-middleware");

const authRouter = new Router();

authRouter.post(
    "/registration",
    body("email").isEmail(),
    body("password").isLength({ min: 3, max: 32 }),
    userController.registration
);
authRouter.post("/login", userController.login);
authRouter.post("/logout", userController.logout);
authRouter.post("/forgot-password", userController.forgotPassword);
authRouter.post("/reset-password", userController.resetPassword);
authRouter.get("/activate/:link", userController.activate);
authRouter.get("/refresh", userController.refresh);
authRouter.get("/users", authMiddleware, userController.getUsers);

module.exports = authRouter;
