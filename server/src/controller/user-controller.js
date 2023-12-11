const config = require("../config/config");
const userService = require("../service/user-service");
const { validationResult } = require("express-validator");
const ApiError = require("../exceptions/api-error");
const photoService = require("../service/photo-service");
const userModel = require("../models/user-model");

class UserController {
    async registration(req, res, next) {
        try {
            const errors = validationResult(req);
            if (!errors.isEmpty()) {
                return next(
                    ApiError.BadRequest("Ошибка при валидации", errors.array())
                );
            }
            const { email, username, password } = req.body;
            const userData = await userService.registration(
                email,
                username,
                password
            );
            res.cookie("refreshToken", userData.refreshToken, {
                maxAge: 30 * 24 * 60 * 60 * 1000,
                httpOnly: true,
            });

            return res.json(userData);
        } catch (e) {
            next(e);
        }
    }
    async login(req, res, next) {
        try {
            const { email, password } = req.body;
            const userData = await userService.login(email, password);
            res.cookie("refreshToken", userData.refreshToken, {
                maxAge: 30 * 24 * 60 * 60 * 1000,
                httpOnly: true,
            });

            return res.json(userData);
        } catch (e) {
            next(e);
        }
    }
    async logout(req, res, next) {
        try {
            const { refreshToken } = req.cookies;
            const token = await userService.logout(refreshToken);
            res.clearCookie("refreshToken");

            return res.json(token);
        } catch (e) {
            next(e);
        }
    }

    async forgotPassword(req, res, next) {
        try {
            const { email } = req.body;
            await userService.forgotPassword(email);

            return res.json({
                message: "Токен для сброса пароля отправлена на ваш email",
            });
        } catch (e) {
            next(e);
        }
    }

    async resetPassword(req, res, next) {
        try {
            const { token, newPassword } = req.body;
            await userService.resetPassword(token, newPassword);

            return res.json({ message: "Пароль успешно сброшен" });
        } catch (e) {
            next(e);
        }
    }

    async activate(req, res, next) {
        try {
            const activationLink = req.params.link;
            await userService.activate(activationLink);

            return res.redirect(config.CLIENT_URL);
        } catch (e) {
            next(e);
        }
    }

    async refresh(req, res, next) {
        try {
            const { refreshToken } = req.cookies;
            const userData = await userService.refresh(refreshToken);
            res.cookie("refreshToken", userData.refreshToken, {
                maxAge: 30 * 24 * 60 * 60 * 1000,
                httpOnly: true,
            });

            return res.json(userData);
        } catch (e) {
            next(e);
        }
    }

    async getUser(req, res, next) {
        try {
            const user = await userService.getUser(req.user.id);
            res.json(user);
        } catch (e) {
            next(e);
        }
    }

    async uploadAvatar(req, res, next) {
        try {
            req.file.buffer;

            const params = {
                Key: req.file.originalname,
                Body: req.file.buffer,
                ContentType: req.file.mimetype,
            };
            await photoService.s3upload(params);

            const newUrl = `https://${config.AWS_BUCKET}.${config.AWS_ENDPOINT}/${req.file.originalname}`;

            await userModel.updateOne({ _id: req.user.id }, { image: newUrl });

            return res.send({ success: true });
        } catch (e) {
            next(e);
        }
    }

    async deleteImage(req, res, next) {
        try {
            const userId = req.user.id;
            const user = await userModel.findById(userId);

            if (!user.image) {
                return next(
                    ApiError.BadRequest(
                        "У пользователя нет изображения",
                        errors.array()
                    )
                );
            }

            const filename = user.image.split("/").pop();

            await photoService.deleteImageFromS3(filename);

            await userModel.findByIdAndUpdate(userId, { image: null });

            return res.json({ message: "Изображение успешно удалено" });
        } catch (e) {
            next(e);
        }
    }

    async patchUser(req, res, next) {
        try {
            const userId = req.user.id;
            const { email, username, password } = req.body;

            const errors = validationResult(req);
            if (!errors.isEmpty()) {
                return next(
                    ApiError.BadRequest("Ошибка при валидации", errors.array())
                );
            }

            const updatedUser = await userService.patchUser(
                userId,
                email,
                username,
                password
            );

            res.json(updatedUser);
        } catch (e) {
            next(e);
        }
    }

    async patchAvatar(req, res, next) {
        try {
            if (!req.file) {
                return next(ApiError.BadRequest("No new image provided"));
            }

            const params = {
                Key: req.file.originalname,
                Body: req.file.buffer,
                ContentType: req.file.mimetype,
            };
            await photoService.s3upload(params);

            const newUrl = `https://${config.AWS_BUCKET}.${config.AWS_ENDPOINT}/${req.file.originalname}`;

            const userId = req.user.id;
            const user = await userModel.findById(userId);

            if (user.image) {
                const previousFilename = user.image.split("/").pop();
                await photoService.deleteImageFromS3(previousFilename);
            }

            await userModel.updateOne({ _id: userId }, { image: newUrl });

            return res.send({ success: true });
        } catch (e) {
            next(e);
        }
    }

    async showLiked(req, res, next) {
        try {
            const userId = req.user.id;
            const favoritePlaces = await userService.showLiked(userId);
            if (!favoritePlaces) {
                throw ApiError.NotFoundError("User not found");
            }
            res.json(favoritePlaces);
        } catch (e) {
            next(e);
        }
    }

    async addLiked(req, res, next) {
        try {
            const userId = req.user.id;
            const placeId = req.params.placeId;
            await userService.addLiked(userId, placeId);
            res.json({ success: true, message: "Place added to favorites" });
        } catch (e) {
            next(e);
        }
    }

    async deleteLiked(req, res, next) {
        try {
            const userId = req.user.id;
            const placeId = req.params.placeId;
            await userService.deleteLiked(userId, placeId);
            res.json({
                success: true,
                message: "Place removed from favorites",
            });
        } catch (e) {
            next(e);
        }
    }
}

module.exports = new UserController();
