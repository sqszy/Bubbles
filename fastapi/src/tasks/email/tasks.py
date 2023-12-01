import smtplib
from email.message import EmailMessage

from src.config import SMTP_PASSWORD, SMTP_USER, SMTP_HOST, SMTP_PORT


def get_email_template_verify(username: str, token: str):
    with open('D:/Bubbles/fastapi/src/tasks/verify.html', 'r', encoding='utf-8') as file:
        verify_content = file.read()

    email = EmailMessage()
    email['Subject'] = 'Bubble - верификация аккаунта'
    email['From'] = SMTP_USER
    email['To'] = SMTP_USER

    verification_url = f'http://127.0.0.1:8000/auth/verify/?token={token}'
    verify_content = verify_content.replace('{username}', username)
    verify_content = verify_content.replace('{verification_url}', verification_url)

    email.set_content(verify_content, subtype='html')
    return email


def get_email_template_reset_password(username: str, token: str):
    with open('D:/Bubbles/fastapi/src/tasks/reset.html', 'r', encoding='utf-8') as file:
        reset_content = file.read()

    email = EmailMessage()
    email['Subject'] = 'Bubble - сброс пароля'
    email['From'] = SMTP_USER
    email['To'] = SMTP_USER

    reset_password_token = token
    reset_content = reset_content.replace('{username}', username)
    reset_content = reset_content.replace('{reset_password_token}', reset_password_token)

    email.set_content(reset_content, subtype='html')
    return email


def send_email_verify(email: str, username: str, token: str):
    email_message = get_email_template_verify(username, token)
    with smtplib.SMTP_SSL(SMTP_HOST, SMTP_PORT) as server:
        server.login(SMTP_USER, SMTP_PASSWORD)
        server.send_message(email_message, from_addr=SMTP_USER, to_addrs=email)


def send_email_reset_password(email: str, username: str, token: str):
    email_message = get_email_template_reset_password(username, token)
    with smtplib.SMTP_SSL(SMTP_HOST, SMTP_PORT) as server:
        server.login(SMTP_USER, SMTP_PASSWORD)
        server.send_message(email_message, from_addr=SMTP_USER, to_addrs=email)
