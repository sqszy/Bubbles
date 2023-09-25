from sqlalchemy import Column, Integer, String, TIMESTAMP, Boolean
from fastapi_users_db_sqlalchemy import SQLAlchemyBaseUserTable
from sqlalchemy.ext.declarative import declarative_base
from datetime import datetime

metadata = MetaData()
Base = declarative_base(metadata=metadata)


class User(SQLAlchemyBaseUserTable[int], Base):
    __tablename__ = "user"
    id = Column(Integer, primary_key=True)
    email: str = Column(String(length=320), unique=True, index=True, nullable=True)
    username = Column(String, nullable=False)
    hashed_password: str = Column(String(length=1024), nullable=True)
    is_active: bool = Column(Boolean, default=True, nullable=False)
    is_superuser: bool = Column(Boolean, default=False, nullable=False)
    is_verified: bool = Column(Boolean, default=False, nullable=False)
    registered_at = Column(TIMESTAMP, nullable=False, default=datetime.utcnow())
