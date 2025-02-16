# 1. Python 3.11 Slim 이미지 사용 (가볍고 최적화됨)
FROM python:3.11-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 필요한 패키지 설치 (pip 업데이트 + 프로젝트 라이브러리)
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# 4. 소스 코드 복사
COPY app .

# 5. FastAPI 앱 실행 (uvicorn 사용)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "80"]
 