from fastapi import FastAPI, UploadFile, File
from starlette.responses import JSONResponse

from auth.base_config import auth_backend
from auth.schemas import UserCreate, UserRead, UserUpdate
from src.auth.base_config import fastapi_users
from src.place.utils import upload_file

app = FastAPI()

app.include_router(
    fastapi_users.get_auth_router(auth_backend, requires_verification=True),
    prefix="/auth/jwt",
    tags=["auth"],
)
app.include_router(
    fastapi_users.get_register_router(UserRead, UserCreate),
    prefix="/auth",
    tags=["auth"],
)
app.include_router(
    fastapi_users.get_verify_router(UserRead),
    prefix="/auth",
    tags=["auth"],
)
app.include_router(
    fastapi_users.get_reset_password_router(),
    prefix="/auth",
    tags=["auth"],
)
app.include_router(
    fastapi_users.get_users_router(UserRead, UserUpdate, requires_verification=True),
    prefix="/users",
    tags=["users"],
)


@app.post('/upload', response_class=JSONResponse)
async def upload(file: UploadFile = File(...)):
    return await upload_file(file)
