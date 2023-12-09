const Router = require("express").Router;
const userController = require("../controller/user-controller");
const authMiddleware = require("../middleware/auth-middleware");
const multer = require("multer");

const usersRouter = new Router();

const storage = multer.memoryStorage();

const upload = multer({ storage: storage });

usersRouter.post(
  "/upload-image",
  upload.single("file"),
  authMiddleware,
  userController.uploadAvatar
);
usersRouter.delete("/delete-image");
usersRouter.get("/me", authMiddleware, userController.getUser);

module.exports = usersRouter;
