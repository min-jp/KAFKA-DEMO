from fastapi import FastAPI, WebSocket
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles
from upbitWss import UpbitWss

app = FastAPI()
app.mount("/src", StaticFiles(directory="src"), name="src")

@app.get("/")
async def root():
    return FileResponse("src/index.html")


@app.websocket("/wss/data")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()


    # 웹소켓 실행
    if UpbitWss.getMessageQueue().empty():
        UpbitWss.startWebSocket()
    else :
        UpbitWss.closeWebSocket()

    try:
        while True:
            message = await UpbitWss.getMessageQueue().get()  # ✅ Queue에서 메시지 가져오기
            await websocket.send_text(message)  # ✅ 클라이언트로 전송

    except Exception as e:
        print(f"WebSocket error: {e}")
