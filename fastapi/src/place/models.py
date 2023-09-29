from sqlalchemy import Column, Integer, String, Boolean, MetaData, Float
from sqlalchemy.ext.declarative import DeclarativeMeta, declarative_base

metadata = MetaData()
Base: DeclarativeMeta = declarative_base(metadata=metadata)


class Place(Base):
    __tablename__ = "place"

    id = Column(Integer, primary_key=True)
    title: str = Column(String(length=320), unique=True, nullable=False)
    photo_name: str = Column(String, nullable=False)
    photo_url: str = Column(String, nullable=False)
    latitude: float = Column(Float, nullable=False)
    longitude: float = Column(Float, nullable=False)
    is_verified_photo: bool = Column(Boolean, default=False, nullable=False)
