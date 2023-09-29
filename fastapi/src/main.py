from fastapi import FastAPI, Depends
from sqlalchemy import MetaData

from auth.base_config import auth_backend, fastapi_users, current_user
from auth.models import User
from auth.schemas import UserRead, UserCreate
from sqlalchemy.ext.declarative import DeclarativeMeta, declarative_base


app = FastAPI()


app.include_router(
    fastapi_users.get_auth_router(auth_backend),
    prefix="/auth/jwt",
    tags=["auth"],
)
app.include_router(
    fastapi_users.get_register_router(UserRead, UserCreate),
    prefix="/auth",
    tags=["auth"],
)


@app.get("/protected-route")
def protected_route(user: User = Depends(current_user)):
    return f"Hello, {user.username}"
