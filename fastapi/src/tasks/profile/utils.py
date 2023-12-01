from typing import List
from uuid import uuid4
import boto3
import magic
from botocore.exceptions import ClientError
from fastapi import HTTPException, UploadFile, status, File
from loguru import logger

from src.config import S3_ID, S3_SECRET

SUPPORTED_FILE_TYPES = {
    'image/png': 'png',
    'image/jpeg': 'jpg',
    'image/jpg': 'jpg',
    'image/heic': 'heic',
    'image/avif': 'avif',
    'image/avifs': 'avifs',
    'image/bmp': 'bmp',
    'image/heif': 'heif',
    'image/webp': 'webp',
}

KB = 1024
MB = 1024 * KB
MAX_FILES = 1
AWS_BUCKET = 'bubbles'

session = boto3.session.Session()
s3 = session.client(
    service_name='s3',
    endpoint_url='https://hb.vkcs.cloud',
    aws_access_key_id=S3_ID,
    aws_secret_access_key=S3_SECRET,
    region_name='ru-msk'
)


async def s3_upload(contents: bytes, key: str):
    logger.info(f'Uploading {key} to s3')
    s3.put_object(Bucket=AWS_BUCKET, Key=key, Body=contents, ACL='public-read')


async def s3_download(key: str):
    try:
        return s3.Object(bucket_name=AWS_BUCKET, key=key).get()['Body'].read()
    except ClientError as err:
        logger.error(str(err))


async def upload_files(files: List[UploadFile] = File(...)):
    if not files:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='No files found!!'
        )

    if len(files) > MAX_FILES:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f'Exceeded maximum number of files allowed. Maximum is {MAX_FILES}'
        )

    file_names = []
    for file in files:
        contents = await file.read()
        size = len(contents)

        if not 0 < size <= 3 * MB:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail='Supported file size is 0 - 3 MB'
            )

        file_type = magic.from_buffer(buffer=contents, mime=True)
        if file_type not in SUPPORTED_FILE_TYPES:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f'Unsupported file type: {file_type}. Supported types are {SUPPORTED_FILE_TYPES}'
            )

        file_name = f'{uuid4()}.{SUPPORTED_FILE_TYPES[file_type]}'
        await s3_upload(contents=contents, key=file_name)
        file_names.append(file_name)

    return {'file_names': file_names}
