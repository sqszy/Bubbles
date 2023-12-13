const Router = require("express").Router;
const placeController = require("../controller/place-controller");
const authMiddleware = require("../middleware/auth-middleware");
const { body } = require("express-validator");
const multer = require("multer");
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

const placeRouter = new Router();

placeRouter.post("/create-place", authMiddleware, placeController.createPlace);
placeRouter.patch(
    "/id/:placeId/patch-place",
    authMiddleware,
    placeController.patchPlace
);
placeRouter.delete(
    "/id/:placeId/delete-place",
    authMiddleware,
    placeController.deletePlace
);

placeRouter.post(
    "/id/:placeId/upload-images",
    authMiddleware,
    upload.array("photos", 4),
    placeController.uploadPlaceImages
);
placeRouter.patch(
    "/id/:placeId/patch-images",
    authMiddleware,
    upload.array("photos", 4),
    placeController.patchPlaceImages
);
placeRouter.delete(
    "/id/:placeId/delete-images",
    authMiddleware,
    placeController.deleteAllPlaceImages
);

placeRouter.post(
    "/id/:placeId/create-review",
    authMiddleware,
    body("rating").isInt({ min: 0, max: 5 }),
    placeController.createReview
);
placeRouter.patch(
    "/id/:placeId/patch-review/:reviewId",
    authMiddleware,
    body("rating").isInt({ min: 0, max: 5 }),
    placeController.patchReview
);
placeRouter.delete(
    "/reviewId/:reviewId/delete-review",
    authMiddleware,
    placeController.deleteReview
);

placeRouter.get("/all", authMiddleware, placeController.getAllPlaces);
placeRouter.get("/id/:placeId", authMiddleware, placeController.getPlaceById);
placeRouter.get(
    "/id/:placeId/reviews",
    authMiddleware,
    placeController.getReviewsForPlace
);

placeRouter.get("/search", authMiddleware, placeController.search);

module.exports = placeRouter;
