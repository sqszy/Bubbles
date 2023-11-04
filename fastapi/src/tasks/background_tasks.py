from fastapi import BackgroundTasks
from sqlalchemy.ext.asyncio import AsyncSession
from datetime import datetime
from src.auth.models import User


async def delete_expired_unverified_users(session: AsyncSession, background_tasks: BackgroundTasks):
    current_time = datetime.utcnow()

    expired_users = await session.query(User).filter(
        User.is_verified == False,
        User.verification_token_expiration <= current_time
    ).all()

    for user in expired_users:
        await session.delete(user)
    await session.commit()
