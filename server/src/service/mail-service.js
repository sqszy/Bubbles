const nodemailer = require("nodemailer");
const config = require("../config/config");
const fs = require("fs");
const path = require("path");

class MailService {
    constructor() {
        this.transporter = nodemailer.createTransport({
            host: config.SMTP_HOST,
            port: config.SMTP_PORT,
            secure: false,
            auth: {
                user: config.SMTP_USER,
                pass: config.SMTP_PASSWORD,
            },
        });
        this.verifyTemplate = fs.readFileSync(
            path.join(__dirname, "verify-email.html"),
            "utf-8"
        );
    }

    async sendActivationMail(to, username, link) {
        const htmlContent = this.verifyTemplate
            .replace("{username}", username)
            .replace("{link}", link);

        await this.transporter.sendMail({
            from: config.SMTP_USER,
            to,
            subject: "Bubble - верификация аккаунта",
            text: "",
            html: htmlContent,
        });
    }

    async sendPasswordResetMail(to, username, link) {
        const resetTemplate = fs.readFileSync(
            path.join(__dirname, "reset-pass.html"),
            "utf-8"
        );

        const htmlContent = resetTemplate
            .replace("{username}", username)
            .replace("{link}", link);

        await this.transporter.sendMail({
            from: config.SMTP_USER,
            to,
            subject: "Bubble - сброс пароля",
            text: "",
            html: htmlContent,
        });
    }
}

module.exports = new MailService();
