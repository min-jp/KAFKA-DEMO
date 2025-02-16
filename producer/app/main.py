from fastapi import FastAPI, WebSocket
from fastapi.websockets import WebSocketState
from upbitWss import UpbitWss

app = FastAPI()

@app.websocket("/ws/stock-trade")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    # 웹소켓 실행
    if UpbitWss.getMessageQueue().empty():
        UpbitWss.startWebSocket()
    try:
        while websocket.client_state == WebSocketState.CONNECTED:
            message = await UpbitWss.getMessageQueue().get()  # ✅ Queue에서 메시지 가져오기
            await websocket.send_text(message)  # ✅ 클라이언트로 전송
    except Exception as e:
        print(f"WebSocket error: {e}")
    finally :
        UpbitWss.closeWebSocket()
        await websocket.close()
        return