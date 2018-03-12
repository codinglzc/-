package com.message.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TestServer {
	
	public static void main(String[] args) { 
		try {
			ServerSocket serverSocket=new ServerSocket(4040);
			ThreadPool pool=ThreadPool.getInstance();
			while(true){ 
				Socket socket=serverSocket.accept();
				System.out.println("服务器接收到一个客户端连接");
				ForwardTask task=new ForwardTask(socket);
				pool.addTask(task);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}