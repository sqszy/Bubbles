from datetime import datetime
from sqlalchemy.orm import relationship
from fastapi_users_db_sqlalchemy import SQLAlchemyBaseUserTable
from sqlalchemy import Column, Integer, String, TIMESTAMP, Boolean, MetaData
from sqlalchemy.ext.declarative import DeclarativeMeta, declarative_base

metadata = MetaData()
Base: DeclarativeMeta = declarative_base(metadata=metadata)


class User(SQLAlchemyBaseUserTable[int], Base):
    __tablename__ = "user"

    id = Column(Integer, primary_key=True)
    email: str = Column(String(length=320), unique=True, index=True, nullable=True)
    username = Column(String, nullable=False)
    hashed_password: str = Column(String(length=1024), nullable=True)
    registered_at = Column(TIMESTAMP, nullable=False, default=datetime.utcnow())
    is_active: bool = Column(Boolean, default=True, nullable=False)
    is_superuser: bool = Column(Boolean, default=False, nullable=False)
    is_verified: bool = Column(Boolean, default=False, nullable=False)
    places = relationship('Place', cascade='all, delete-orphan')
