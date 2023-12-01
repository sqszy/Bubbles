from typing import List
from fastapi import Depends, HTTPException, UploadFile, File, APIRouter
from sqlalchemy import update
from src.database import async_session_maker
from src.auth.base_config import fastapi_users
from src.auth.models import User
from src.place.utils import upload_files

router = APIRouter()


@router.post('/upload-image/')
async def upload_user_image(current_user: User = Depends(fastapi_users.current_user()),
                            files: List[UploadFile] = File(...)):
    if not current_user:
        raise HTTPException(status_code=401, detail="Not authenticated")

    uploaded_images = await upload_files(files)
    image_to_upload = uploaded_images.get('file_names', [])
    print(f'images: {uploaded_images}')

    if image_to_upload:
        new_image_path = f'https://bubbles.hb.ru-msk.vkcs.cloud/{image_to_upload[0]}'
        async with async_session_maker() as session:
            statement = update(User).where(User.id == current_user.id).values(image=new_image_path)
            await session.execute(statement)
            await session.commit()

    return {"message": "Image uploaded successfully"}


@router.delete('/delete-image/')
async def delete_user_image(current_user: User = Depends(fastapi_users.current_user())):
    if not current_user:
        raise HTTPException(status_code=401, detail="Not authenticated")
    if not current_user.image:
        raise HTTPException(status_code=404, detail="No profile image exists for the user")

    async with async_session_maker() as session:
        statement = update(User).where(User.id == current_user.id).values(image=None)
        await session.execute(statement)
        await session.commit()

    return {"message": "Image deleted successfully"}
