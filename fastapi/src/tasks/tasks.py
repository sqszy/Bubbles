import smtplib
from email.message import EmailMessage

from src.config import SMTP_PASSWORD, SMTP_USER, SMTP_HOST, SMTP_PORT


def get_email_template_verify(username: str, token: str):
    email = EmailMessage()
    email['Subject'] = 'Bubble - account verification'
    email['From'] = SMTP_USER
    email['To'] = SMTP_USER

    email.set_content(
        '<div>'
        f'<h1 style="color: red;">Здравствуйте, {username} Your verification code is: {token}. </h1>'
        '</div>',
        subtype='html'
    )
    return email


def send_email_verify(username: str, token: str):
    email = get_email_template_verify(username, token)
    with smtplib.SMTP_SSL(SMTP_HOST, SMTP_PORT) as server:
        server.login(SMTP_USER, SMTP_PASSWORD)
        server.send_message(email)
