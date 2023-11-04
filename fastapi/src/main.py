from fastapi import Depends, FastAPI, BackgroundTasks

from auth.base_config import auth_backend, fastapi_users
from auth.base_config import current_user
from auth.models import User
from auth.schemas import UserCreate, UserRead
from src.database import get_async_session
from tasks.background_tasks import delete_expired_unverified_users

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


@app.get("/authenticated-route")
async def authenticated_route(user: User = Depends(current_user)):
    background_tasks = BackgroundTasks()
    session = get_async_session()
    background_tasks.add_task(delete_expired_unverified_users, session, background_tasks)
    return {"message": f"Hello {user.email}!"}
