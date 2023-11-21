import re
from typing import Optional

from fastapi_users import schemas
from pydantic import field_validator


class UserRead(schemas.BaseUser[int]):
    id: int
    username: str
    email: str
    image: str
    is_active: bool = True
    is_superuser: bool = False
    is_verified: bool = False

    class Config:
        orm_mode = True


class UserCreate(schemas.BaseUserCreate):
    username: str
    email: str
    password: str
    is_active: Optional[bool] = True
    is_superuser: Optional[bool] = False
    is_verified: Optional[bool] = False

    @field_validator("email")
    def validate_email(cls, email: str):
        if not re.match(r"[^@]+@[^@]+\.[^@]+", email):
            raise ValueError("Invalid email format")
        return email

    @field_validator("password")
    def valid_password(cls, password: str):
        if len(password) < 5:
            raise ValueError("Password should be at least 5 characters")
        return password


class UserUpdate(schemas.BaseUserUpdate):
    username: Optional[str] = None
    password: Optional[str] = None
    email: Optional[str] = None
    image: Optional[str] = None
    is_active: Optional[bool] = None
    is_superuser: Optional[bool] = None
    is_verified: Optional[bool] = None

    @field_validator("email")
    def validate_email(cls, email: str):
        if not re.match(r"[^@]+@[^@]+\.[^@]+", email):
            raise ValueError("Invalid email format")
        return email

    @field_validator("password")
    def valid_password(cls, password: str):
        if len(password) < 5:
            raise ValueError("Password should be at least 5 characters")
        return password
