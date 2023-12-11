const userModel = require("../models/user-model");
const bcrypt = require("bcrypt");
const uuid = require("uuid");
const mailService = require("./mail-service");
const tokenService = require("./token-service");
const UserDto = require("../dtos/user-dto");
const config = require("../config/config");
const ApiError = require("../exceptions/api-error");

const rand = () => Math.random().toString(36).substr(2, 8);
const generateShortToken = () => rand();

class UserService {
    async registration(email, username, password) {
        const candidate = await userModel.findOne({ email });
        if (candidate) {
            throw ApiError.BadRequest(
                `Пользователь с почтовым адресом ${email} уже существует`
            );
        }
        const hashPassword = await bcrypt.hash(password, 3);
        const activationLink = uuid.v4();

        const user = await userModel.create({
            email,
            username,
            password: hashPassword,
            registeredAt: new Date().toISOString(),
            isAdmin: false,
            image: null,
            activationLink,
            resetToken: "",
            favoritePlaces: [],
        });
        await mailService.sendActivationMail(
            email,
            username,
            `${config.API_URL}/api/auth/activate/${activationLink}`
        );

        const userDto = new UserDto(user); // id, email, isActivated..
        const tokens = tokenService.generateTokens({ ...userDto });
        await tokenService.saveToken(userDto.id, tokens.refreshToken);

        return {
            ...tokens,
            user: userDto,
        };
    }

    async activate(activationLink) {
        const user = await userModel.findOne({ activationLink });
        if (!user) {
            throw ApiError.BadRequest("Некорректная ссылка активации");
        }
        user.isActivated = true;
        await user.save();
    }

    async login(email, password) {
        const user = await userModel.findOne({ email });
        if (!user) {
            throw ApiError.BadRequest("Пользователь с таким email не найден");
        }

        const isPassEquals = await bcrypt.compare(password, user.password);
        if (!isPassEquals) {
            throw ApiError.BadRequest("Неверный пароль");
        }

        const userDto = new UserDto(user);
        const tokens = tokenService.generateTokens({ ...userDto });
        await tokenService.saveToken(userDto.id, tokens.refreshToken);

        return {
            ...tokens,
            user: userDto,
        };
    }

    async logout(refreshToken) {
        const token = await tokenService.removeToken(refreshToken);

        return token;
    }

    async forgotPassword(email) {
        const user = await userModel.findOne({ email });
        if (!user) {
            throw ApiError.BadRequest("Пользователь с таким email не найден");
        }

        const resetToken = generateShortToken();
        user.resetToken = resetToken;
        await user.save();
        await mailService.sendPasswordResetMail(
            user.email,
            user.username,
            resetToken
        );

        return true;
    }

    async resetPassword(token, newPassword) {
        const user = await userModel.findOne({ resetToken: token });

        if (!user) {
            throw ApiError.BadRequest("Пользователь не найден");
        }

        if (token != user.resetToken) {
            console.log("Incorrect reset token:", token);
            throw ApiError.BadRequest("Некорректный токен сброса пароля");
        }

        const hashPassword = await bcrypt.hash(newPassword, 3);
        user.password = hashPassword;
        user.resetToken = null;
        await user.save();

        return true;
    }

    async refresh(refreshToken) {
        if (!refreshToken) {
            throw ApiError.UnauthorizedError();
        }
        const userData = tokenService.validateRefreshToken(refreshToken);
        const TokenFromDb = await tokenService.findToken(refreshToken);
        if (!userData || !TokenFromDb) {
            throw ApiError.UnauthorizedError();
        }

        const user = await userModel.findById(userData.id);
        const userDto = new UserDto(user);
        const tokens = tokenService.generateTokens({ ...userDto });
        await tokenService.saveToken(userDto.id, tokens.refreshToken);

        return {
            ...tokens,
            user: userDto,
        };
    }

    async getUser(userId) {
        const user = await userModel.findById(userId);
        const userDto = new UserDto(user);
        return userDto;
    }

    async patchUser(userId, email, username, password) {
        const user = await userModel.findById(userId);

        if (!user) {
            throw ApiError.NotFoundError("Пользователь не найден");
        }

        if (email) user.email = email;
        if (username) user.username = username;
        if (password) user.password = await bcrypt.hash(password, 3);

        await user.save();

        const updatedUserDto = new UserDto(user);

        return updatedUserDto;
    }

    async showLiked(userId) {
        const user = await userModel
            .findById(userId)
            .populate("favoritePlaces");
        if (!user) {
            return null;
        }
        return user.favoritePlaces;
    }

    async addLiked(userId, placeId) {
        await userModel.findByIdAndUpdate(userId, {
            $addToSet: { favoritePlaces: placeId },
        });
    }

    async deleteLiked(userId, placeId) {
        await userModel.findByIdAndUpdate(userId, {
            $pull: { favoritePlaces: placeId },
        });
    }
}

module.exports = new UserService();
