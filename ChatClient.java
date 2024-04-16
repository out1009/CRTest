package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ChatClient {
    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });

            Channel channel = bootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            /*
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                System.out.println("Client : Read");
                
                channel.writeAndFlush(line + "\r\n");
            }
            */
            
            String line = "TEST Check out the latest from iPad, iPad Pro, iPad Air and iPad mini. iPad is so versatile, its more than up to any task.";
            
            //for(int i=1;i<=10;i++) {
            //while (true) {
            	//System.out.println("Client : " + i + " : " + line + "\r");
            	channel.writeAndFlush(line + "\r\n");
            //}
            
            
        } finally {
        	System.out.println("Client : finally");
        	
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
    	for(int i=1;i<=100;i++) {
    		new ChatClient("localhost", 8080).run();
    	}
    }
}