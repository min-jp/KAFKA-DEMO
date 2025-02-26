import websocket
import json
import asyncio
import threading
from producer import ProdFactory

class UpbitWss:
    uri = 'wss://api.upbit.com/websocket/v1'
    ws_instance = None
    message_queue = asyncio.Queue()

    @classmethod
    def getMessageQueue(cls):
        return cls.message_queue

    @classmethod
    def on_message(cls, ws, message):
        message = json.loads(message)
        data = json.dumps({
            "price": message["trade_price"],
            "timestamp": message["trade_time"]
        })
         # 실행 중인 이벤트 루프 가져오기
        try:
            loop = asyncio.get_running_loop()
            loop.create_task(cls.message_queue.put(data))  # 올바른 루프에서 실행
        except RuntimeError:
            # 이벤트 루프가 없으면 새로운 루프에서 실행
            asyncio.run(cls.message_queue.put(data))
        ProdFactory.publish("stock_trade", data)

    @staticmethod
    def on_error(ws, error):
        print("### error ###")
        print(error)

    @staticmethod
    def on_close(ws, close_status_code, close_msg):
        print("### closed ###")
        print(close_status_code, close_msg)

    @staticmethod
    def on_open(ws):
        print("### Opened connection ###")
        ws.send(json.dumps([
            {"ticket": "test"},
            {"type": "ticker", "codes": ["KRW-BTC"], "is_only_realtime": True}
        ]))

    @classmethod
    def getWebSocket(cls):
        if cls.ws_instance :
            return cls.ws_instance
        
        return websocket.WebSocketApp(
            cls.uri,
            on_open=cls.on_open,
            on_message=cls.on_message,
            on_error=cls.on_error,
            on_close=cls.on_close
        )

    @classmethod
    def startWebSocket(cls):
        def run():
            cls.ws_instance = cls.getWebSocket()
            cls.ws_instance.run_forever()

        thread = threading.Thread(target=run, daemon=True)
        thread.start()

    @classmethod
    def closeWebSocket(cls):
        if cls.ws_instance:
            cls.ws_instance.close()  # 웹소켓 종료
            print("WebSocket closed.")
            cls.ws_instance = None

        else:
            print("No WebSocket connection to close.")