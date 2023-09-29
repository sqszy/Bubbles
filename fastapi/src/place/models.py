from sqlalchemy import Column, ForeignKey, Integer, String, Boolean, MetaData, Float
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import DeclarativeMeta, declarative_base

metadata = MetaData()
Base: DeclarativeMeta = declarative_base(metadata=metadata)

def _resolve_child_model():
    from auth.models import User

    return User

class Place(Base):
    __tablename__ = "place"

    id = Column(Integer, primary_key=True)
    title: str = Column(String(length=320), unique=True, nullable=False)
    latitude: float = Column(Float, nullable=False)
    longitude: float = Column(Float, nullable=False)
    reviews = relationship("Review", back_populates="place")


class PlaceImage(Base):
    __tablename__ = "place_image"

    id = Column(Integer, primary_key=True)
    place_id = Column(Integer, ForeignKey("place.id"), nullable=False)
    photo_name = Column(String, nullable=False)
    photo_url = Column(String, nullable=False)
    is_verified_photo: bool = Column(Boolean, default=False, nullable=False)


class Review(Base):
    __tablename__ = "review"

    id = Column(Integer, primary_key=True)
    text = Column(String, nullable=False)
    place_id = Column(Integer, ForeignKey("place.id"), nullable=False)
    place = relationship("Place", back_populates="reviews")
