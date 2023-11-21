from typing import List

from pydantic import BaseModel


# Place CRUD classes
class PlaceCreate(BaseModel):
    title: str
    latitude: float
    longitude: float


class PlaceUpdate(BaseModel):
    title: str
    latitude: float
    longitude: float


class PlaceRead(BaseModel):
    id: int
    title: str
    latitude: float
    longitude: float

    class Config:
        orm_mode = True


# Review CRUD classes
class ReviewCreate(BaseModel):
    text: str
    creator_id: int
    place_id: int


class ReviewUpdate(BaseModel):
    text: str
    creator_id: int
    place_id: int


class ReviewRead(BaseModel):
    id: int
    text: str
    creator_id: int
    place_id: int

    class Config:
        orm_mode = True


# PlaceImage CRUD classes
class PlaceImageCreate(BaseModel):
    photo_name: str
    photo_url: str
    place_id: int


class PlaceImageUpdate(BaseModel):
    photo_name: str
    photo_url: str
    place_id: int


class PlaceImageRead(BaseModel):
    id: int
    photo_name: str
    photo_url: str
    place_id: int

    class Config:
        orm_mode = True
