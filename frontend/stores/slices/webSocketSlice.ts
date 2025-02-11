import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import Stomp from "stompjs";
import { RootState } from "../store";

interface WebSocketState {
  client: Stomp.Client | null;
  isConnected: boolean;
  users:number;
}

const initialState: WebSocketState = {
  client: null,
  isConnected: false,
  users:0,
};

const websocketSlice = createSlice({
  name: "websocket",
  initialState,
  reducers: {
    setWebSocketClient: (state, action: PayloadAction<Stomp.Client | null>) => {
      state.client = action.payload;
    },
    setConnectionStatus: (state, action: PayloadAction<boolean>) => {
      state.isConnected = action.payload;
    },
    setUserCount: (state, action: PayloadAction<number>) => {
      state.users = action.payload;
    },
  },
});

export const { setWebSocketClient, setConnectionStatus, setUserCount } =
  websocketSlice.actions;
export const selectWebsocket = (state: RootState) => state.websocket;
export default websocketSlice.reducer;
