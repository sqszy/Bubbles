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
    "/placeId/:placeId/patch-place",
    authMiddleware,
    placeController.patchPlace
);
placeRouter.delete(
    "/placeId/:placeId/delete-place",
    authMiddleware,
    placeController.deletePlace
);

placeRouter.post(
    "/placeId/:placeId/upload-images",
    authMiddleware,
    upload.array("photos", 4),
    placeController.uploadPlaceImages
);
placeRouter.patch(
    "/placeId/:placeId/patch-images",
    authMiddleware,
    upload.array("photos", 4),
    placeController.patchPlaceImages
);
placeRouter.delete(
    "/placeId/:placeId/delete-images",
    authMiddleware,
    placeController.deleteAllPlaceImages
);

placeRouter.post(
    "/placeId/:placeId/create-review",
    authMiddleware,
    body("rating").isInt({ min: 0, max: 5 }),
    placeController.createReview
);
placeRouter.patch(
    "/placeId/:placeId/patch-review/:reviewId",
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
placeRouter.get(
    "/placeId/:placeId",
    authMiddleware,
    placeController.getPlaceById
);
placeRouter.get(
    "/placeId/:placeId/reviews",
    authMiddleware,
    placeController.getReviewsForPlace
);

placeRouter.get("/search", authMiddleware, placeController.search);

module.exports = placeRouter;
