const Router = require("express").Router;
const userController = require("../controller/user-controller");
const authMiddleware = require("../middleware/auth-middleware");
const multer = require("multer");
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

const usersRouter = new Router();

usersRouter.post(
    "/upload-image",
    upload.single("file"),
    authMiddleware,
    userController.uploadAvatar
);
usersRouter.patch(
    "/:userId/patch-user",
    upload.single("file"),
    authMiddleware,
    userController.patchUser
);
usersRouter.delete("/delete-image", authMiddleware, userController.deleteImage);
usersRouter.get("/me", authMiddleware, userController.getUser);

module.exports = usersRouter;
