from typing import Optional

from pydantic import BaseModel


class PlaceCreate(BaseModel):
    id: int
    title: str
    photo_name: str
    photo_url: str
    is_verified_photo: Optional[bool] = False
    latitude: float
    longitude: float
