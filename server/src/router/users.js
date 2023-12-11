const Router = require("express").Router;
const userController = require("../controller/user-controller");
const authMiddleware = require("../middleware/auth-middleware");
const multer = require("multer");
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

const usersRouter = new Router();

usersRouter.post(
    "/upload-image",
    upload.single("photo"),
    authMiddleware,
    userController.uploadAvatar
);
usersRouter.patch(
    "/:userId/patch-user",
    authMiddleware,
    userController.patchUser
);
usersRouter.patch(
    "/patch-image",
    upload.single("photo"),
    authMiddleware,
    userController.uploadAvatar
);
usersRouter.delete("/delete-image", authMiddleware, userController.deleteImage);
usersRouter.get("/me", authMiddleware, userController.getUser);

usersRouter.get("/showLiked", authMiddleware, userController.showLiked);
usersRouter.post("/:placeId/addLiked", authMiddleware, userController.addLiked);
usersRouter.delete(
    "/:placeId/deleteLiked",
    authMiddleware,
    userController.deleteLiked
);

module.exports = usersRouter;
