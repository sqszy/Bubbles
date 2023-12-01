from sqlalchemy import Column, ForeignKey, Integer, String, MetaData, Float
from sqlalchemy.ext.declarative import DeclarativeMeta, declarative_base
from sqlalchemy.orm import relationship

from src.auth.models import User

metadata = MetaData()
Base: DeclarativeMeta = declarative_base(metadata=metadata)


class Place(Base):
    __tablename__ = "place"

    id = Column(Integer, primary_key=True)
    title: str = Column(String(length=320), unique=True, nullable=False)
    latitude: float = Column(Float, nullable=False)
    longitude: float = Column(Float, nullable=False)
    reviews = relationship("Review", back_populates="place")


class Review(Base):
    __tablename__ = "review"

    id = Column(Integer, primary_key=True)
    text = Column(String, nullable=False)
    creator_id = Column(Integer, ForeignKey(User.id), nullable=False)
    place_id = Column(Integer, ForeignKey("place.id"), nullable=False)

    creator = relationship("User", back_populates="reviews")

    place = relationship("Place", back_populates="reviews")


class PlaceImage(Base):
    __tablename__ = "place_image"

    id = Column(Integer, primary_key=True)
    photo_name = Column(String, nullable=False)
    photo_url = Column(String, nullable=False)
    place_id = Column(Integer, ForeignKey("place.id"), nullable=False)

    place = relationship("Place", back_populates="images")
