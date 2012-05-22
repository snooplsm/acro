    /*
     * Copyright 2012 The Netty Project
     *
     * The Netty Project licenses this file to you under the Apache License,
     * version 2.0 (the "License"); you may not use this file except in compliance
     * with the License. You may obtain a copy of the License at:
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
    * Unless required by applicable law or agreed to in writing, software
    * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    * License for the specific language governing permissions and limitations
    * under the License.
    */
   //The MIT License
   //
   //Copyright (c) 2009 Carl Bystr?m
   //
   //Permission is hereby granted, free of charge, to any person obtaining a copy
   //of this software and associated documentation files (the "Software"), to deal
   //in the Software without restriction, including without limitation the rights
   //to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   //copies of the Software, and to permit persons to whom the Software is
   //furnished to do so, subject to the following conditions:
   //
   //The above copyright notice and this permission notice shall be included in
   //all copies or substantial portions of the Software.
   //
   //THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   //IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   //FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   //AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   //LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   //OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
   //THE SOFTWARE.
   package org.jboss.netty.example.http.websocketx.client;
   
   import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpResponseDecoder;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.json.JSONObject;

import com.happytap.acro.Room;
import com.happytap.acro.models.Player;
   
   public class WebSocketClient {
   
       private final URI uri;
       
       private ClientBootstrap bootstrap;
       private WebSocketClientHandshaker handshaker;
       Channel ch = null;
       public WebSocketClient(URI uri, final AcroListener listener) {
           this.uri = uri;
           String protocol = uri.getScheme();
           if (!protocol.equals("ws")) {
               throw new IllegalArgumentException("Unsupported protocol: " + protocol);
           }
           bootstrap =
                   new ClientBootstrap(
                           new NioClientSocketChannelFactory(
                                   Executors.newCachedThreadPool(),
                                   Executors.newCachedThreadPool()));
           // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
           // If you change it to V00, ping is not supported and remember to change
           // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
           handshaker =
                   new WebSocketClientHandshakerFactory().newHandshaker(
                           uri, WebSocketVersion.V13, null, false, null);
           bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
               public ChannelPipeline getPipeline() throws Exception {
                   ChannelPipeline pipeline = Channels.pipeline();
   
                   pipeline.addLast("decoder", new HttpResponseDecoder());
                   pipeline.addLast("encoder", new HttpRequestEncoder());
                  pipeline.addLast("ws-handler", new WebSocketClientHandler(handshaker,listener));
                  return pipeline;
              }
          });
       }
       
       public void disconnect() throws Exception {
    	   ch.disconnect();
       }
       
       public void connect() throws Exception {
    	   ChannelFuture future =
                   bootstrap.connect(
                           new InetSocketAddress(uri.getHost(), uri.getPort()));
           future.awaitUninterruptibly().rethrowIfFailed();
           ch = future.getChannel();
           handshaker.handshake(ch).awaitUninterruptibly().rethrowIfFailed();
       }
       
       public void autoJoin(String username) throws Exception {
    	   JSONObject req = new JSONObject();
    	   req.put("type", "aj");
    	   req.put("username",username);
    	   ch.write(new TextWebSocketFrame(req.toString()));
       }
       
       public void run() throws Exception {
           
           try {
                   
              // Send 10 messages and wait for responses
              for (int i = 0; i < 10; i++) {
                  ch.write(new TextWebSocketFrame("Message #" + i));
              }
      
              // Ping
              ch.write(new PingWebSocketFrame(ChannelBuffers.copiedBuffer(new byte[]{1, 2, 3, 4, 5, 6})));
      
              // Close

              ch.write(new CloseWebSocketFrame());
      
              // WebSocketClientHandler will close the connection when the server
              // responds to the CloseWebSocketFrame.
              //ch.getCloseFuture().awaitUninterruptibly();
          } finally {
              if (ch != null) {
                  ch.close();
              }
              bootstrap.releaseExternalResources();
          }
      }

	public void requestRoomsList() throws Exception {
 	   JSONObject req = new JSONObject();
 	   req.put("type", "rl");
 	   ch.write(new TextWebSocketFrame(req.toString()));
	}
	
	public void leaveRoom(Player player, Room room) throws Exception {
		JSONObject req = new JSONObject();
		req.put("type", "lv");
		req.put("user_id", player.getId());
		req.put("room", room.getId());
		ch.write(new TextWebSocketFrame(req.toString()));
	}

	public void joinRoom(Player player, Room _room) throws Exception {
		JSONObject req = new JSONObject();
		req.put("type","jr");
		req.put("username", player.firstName);
        req.put("avatar_url", player.avatarUrl);
        req.put("user_id", player.id);
		req.put("room", _room.getId());
		ch.write(new TextWebSocketFrame(req.toString()));
	}
	
	public void chat(String username, String userId, Room _room, String message) throws Exception {
		JSONObject req = new JSONObject();
		req.put("type", "m");
		req.put("username", username);
		req.put("room", _room.getId());
		req.put("user_id", userId);
		req.put("message", message);
		ch.write(new TextWebSocketFrame(req.toString()));
	}
	
	public void submitAcronym(Player player, Room _room, String acronym) throws Exception {		
		JSONObject req = new JSONObject();
		req.put("type", "aa");
		req.put("username",player.getName());
		req.put("user_id",player.getId());
		req.put("room",_room.getId());
		req.put("acronym", acronym);
		ch.write(new TextWebSocketFrame(req.toString()));
	}

	public void setCategory(String firstName, String id, Room __room,
			String __category) throws Exception {
		JSONObject req = new JSONObject();
		req.put("type", "c");
		req.put("username", firstName);
		req.put("user_id",id);
		req.put("room", __room.getId());
		req.put("category", __category);
		ch.write(new TextWebSocketFrame(req.toString()));
	}

	public void voteForAcronym(Player me, String voteForAcronymId, Room __room) throws Exception {
		// TODO Auto-generated method stub
		JSONObject req = new JSONObject();
		req.put("type","vt");
		req.put("room", __room.getId());
		req.put("user_id",me.getId());
		req.put("acronym", voteForAcronymId);
		ch.write(new TextWebSocketFrame(req.toString()));
	}
  
  }
