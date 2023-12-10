const express = require("express");
const cors = require("cors");
const cookieParser = require("cookie-parser");
const config = require("./config/config");
const mongoose = require("mongoose");
const TagService = require("./service/tag-service");
const authRouter = require("./router/auth");
const errorMiddleware = require("./middleware/error-middleware");
const usersRouter = require("./router/users");
const placeRouter = require("./router/place");

const app = express();

app.use(express.json());
app.use(cookieParser());
app.use(cors());
TagService.initializeTags();
app.use("/api/auth", authRouter);
app.use("/api/users", usersRouter);
app.use("/api/place", placeRouter);
app.use(errorMiddleware);

const start = async () => {
    try {
        await mongoose.connect(config.DB_URL);
        app.listen(config.PORT, () =>
            console.log(`Server started on PORT = ${config.PORT}`)
        );
    } catch (e) {
        console.log(e);
    }
};

start();
