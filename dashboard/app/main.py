from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles

import os

load_dotenv()

app = FastAPI()
app.mount("/src", StaticFiles(directory="src"), name="src")

@app.get("/", response_class=HTMLResponse)
async def root():
    # HTML 파일 읽기
    with open("src/index.html", "r", encoding="utf-8") as file:
        html_content = file.read()

    # 환경변수 치환
    html_content = html_content.replace("{{WSS_URI_PRODUCER}}", os.getenv("WSS_URI_PRODUCER", "localhost:8000")).replace("{{WSS_URI_CONSUMER_1}}", os.getenv("WSS_URI_CONSUMER_1", "localhost:8001")).replace("{{WSS_URI_CONSUMER_2}}", os.getenv("WSS_URI_CONSUMER_2", "localhost:8002")).replace("{{WSS_URI_CONSUMER_3}}", os.getenv("WSS_URI_CONSUMER_3", "localhost:8003"))

    return HTMLResponse(content=html_content)